package com.tomash.contactgetter.main;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.SparseArray;

import com.tomash.contactgetter.entity.Address;
import com.tomash.contactgetter.entity.Contact;
import com.tomash.contactgetter.entity.Email;
import com.tomash.contactgetter.entity.PhoneNumber;
import com.tomash.contactgetter.interfaces.WithLabel;
import com.tomash.contactgetter.interfaces.WithLabelCreator;
import com.tomash.contactgetter.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DbHandler {
    private ContentResolver mResolver;
    final private String MAIN_DATA_KEY = "data1";
    final private String LABEL_DATA_KEY = "data2";
    final private String CUSTOM_LABEL_DATA_KEY = "data3";
    final private String ID_KEY = "contact_id";


    public DbHandler(Context context) {
        mResolver = context.getContentResolver();
    }

    private Cursor getMainContactsCursor() {
        String selection = ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER + " = 1 ";
        Cursor cur = mResolver.query(ContactsContract.Contacts.CONTENT_URI,
            null, selection, null, null);
        return cur;
    }

    public List<Contact> getContacts() {
        List<Contact> contactsList = new ArrayList<>();
        Cursor c = getMainContactsCursor();
        SparseArray<List<PhoneNumber>> phonesDataMap = getDataMap(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new WithLabelCreator<PhoneNumber>() {
            @Override
            public PhoneNumber create(String mainData, int contactId, int labelId, String labelName) {
                return new PhoneNumber(mainData, contactId, labelId, labelName);
            }
        });
        SparseArray<List<Address>> addressDataMap = getDataMap(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI, new WithLabelCreator<Address>() {
            @Override
            public Address create(String mainData, int contactId, int labelId, String labelName) {
                return new Address(mainData, contactId, labelId, labelName);
            }
        });
        SparseArray<List<Email>> emailDataMap = getDataMap(ContactsContract.CommonDataKinds.Email.CONTENT_URI, new WithLabelCreator<Email>() {
            @Override
            public Email create(String mainData, int contactId, int labelId, String labelName) {
                return new Email(mainData, contactId, labelId, labelName);
            }
        });
        SparseArray<List<String>> websitesDataMap = getWebSitesMap();
        SparseArray<String> notesDataMap = getNotesMap();
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));
            long date = c.getLong(c.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));
            contactsList.add(new Contact()
                .setContactId(id)
                .setLastModificationDate(date)
                .setPhoneList(phonesDataMap.get(id))
                .setAddressesList(addressDataMap.get(id))
                .setEmailList(emailDataMap.get(id))
                .setWebsitesList(websitesDataMap.get(id))
                .setNote(notesDataMap.get(id))
                .setCompositeName(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))));
        }
        c.close();
        return contactsList;
    }


    private SparseArray<List<String>> getWebSitesMap() {
        SparseArray<List<String>> idSiteMap = new SparseArray<>();
        String orgWhere = ContactsContract.Data.MIMETYPE + " = ?";
        String[] orgWhereParams = new String[]{ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE};
        Cursor websiteCur = mResolver.query(ContactsContract.Data.CONTENT_URI,
            null, orgWhere, orgWhereParams, null);
        if (websiteCur != null) {
            while (websiteCur.moveToNext()) {
                int id = websiteCur.getInt(websiteCur.getColumnIndex(ID_KEY));
                String website = websiteCur.getString(websiteCur.getColumnIndex(MAIN_DATA_KEY));
                List<String> currentWebsiteList = idSiteMap.get(id);
                if (currentWebsiteList == null) {
                    currentWebsiteList = new ArrayList<>();
                    currentWebsiteList.add(website);
                    idSiteMap.put(id, currentWebsiteList);
                } else currentWebsiteList.add(website);
            }
            websiteCur.close();
        }
        return idSiteMap;
    }

    private SparseArray<String> getNotesMap() {
        SparseArray<String> idNoteMap = new SparseArray<>();
        String orgWhere = ContactsContract.Data.MIMETYPE + " = ?";
        String[] orgWhereParams = new String[]{ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
        Cursor noteCur = mResolver.query(ContactsContract.Data.CONTENT_URI,
            null, orgWhere, orgWhereParams, null);
        if (noteCur != null) {
            while (noteCur.moveToNext()) {
                int id = noteCur.getInt(noteCur.getColumnIndex(ID_KEY));
                String note = noteCur.getString(noteCur.getColumnIndex(MAIN_DATA_KEY));
                if (note != null) idNoteMap.put(id, note);
            }
            noteCur.close();
        }
        return idNoteMap;
    }

    private <T extends WithLabel> SparseArray<List<T>> getDataMap(Uri uri, WithLabelCreator<T> creator) {
        SparseArray<List<T>> dataSparseArray = new SparseArray<>();
        Cursor pCur = mResolver.query(uri, null,
            null,
            null, null);
        if (pCur != null) {
            while (pCur.moveToNext()) {
                int id = pCur.getInt(pCur.getColumnIndex(ID_KEY));
                String data = pCur.getString(pCur.getColumnIndex(MAIN_DATA_KEY));
                int labelId = pCur.getInt(pCur.getColumnIndex(LABEL_DATA_KEY));
                String customLabel = pCur.getString(pCur.getColumnIndex(CUSTOM_LABEL_DATA_KEY));
                T current = creator.create(data, id, labelId, customLabel);
                List<T> currentDataList = dataSparseArray.get(id);
                if (currentDataList == null) {
                    currentDataList = new ArrayList<>();
                    currentDataList.add(current);
                    dataSparseArray.put(id, currentDataList);
                } else currentDataList.add(current);
            }
            pCur.close();
        }
        return dataSparseArray;
    }
    //label data key = 0 , data3 = custom

    public boolean deleteContactWithId(String contactId) {
        Uri uri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, contactId);
        int deleted = mResolver.delete(uri, null, null);
        return deleted > 0;
    }

    public int addContact(Contact contact) {
        ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>();
        Map<String, Integer> mobileLabelMap = Util.getLabelTypeMap(true);
        Map<String, Integer> otherLabelMap = Util.getLabelTypeMap(false);
        op_list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            .build());

        op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getCompositeName())
            .build());

//        for (Pair<String, String> emailLabelPair : contact.getEmailList()) {
//            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                .withValueBackReference(ContactsContract.Contacts.Data.RAW_CONTACT_ID, 0)
//                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
//                .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailLabelPair.first)
//                .withValue(ContactsContract.CommonDataKinds.Email.TYPE, otherLabelMap.get(emailLabelPair.second))
//                .build());
//        }
//        for (Pair<String, String> phoneLabelPair : contact.getPhoneList()) {
//            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneLabelPair.first)
//                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, mobileLabelMap.get(phoneLabelPair.second))
//                .build());
//        }
//        for (Pair<String, String> addressLabelPair : contact.getAddressesList()) {
//            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
//                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, addressLabelPair.first)
//                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, otherLabelMap.get(addressLabelPair.second))
//                .build());
//        }
        for (String webSite : contact.getWebsitesList()) {
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.DATA1, webSite)
                .build());
        }
        if (!contact.getNote().isEmpty()) {
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.DATA1, contact.getNote())
                .build());
        }
        try {
            ContentProviderResult[] results = mResolver.applyBatch(ContactsContract.AUTHORITY, op_list);
//            return Single.just(Integer.parseInt(results[0].uri.getLastPathSegment()));

        } catch (Exception e) {
//            return Single.just(-1);
        }
        return 1;
    }

}