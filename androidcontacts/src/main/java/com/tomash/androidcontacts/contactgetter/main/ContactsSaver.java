package com.tomash.androidcontacts.contactgetter.main;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.ContactsContract;

import com.tomash.androidcontacts.contactgetter.entity.ContactData;

import java.util.ArrayList;

/**
 * Created by andrew on 2/25/17.
 */
class ContactsSaver {
    private ContentResolver mResolver;

    public ContactsSaver(ContentResolver resolver) {
        mResolver = resolver;
    }

    public boolean deleteContactWithId(String contactId) {
        Uri uri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, contactId);
        int deleted = mResolver.delete(uri, null, null);
        return deleted > 0;
    }

    public int addContact(ContactData contact) {
        ArrayList<ContentProviderOperation> op_list = new ArrayList<ContentProviderOperation>();
//        Map<String, Integer> mobileLabelMap = Util.getLabelTypeMap(true);
//        Map<String, Integer> otherLabelMap = Util.getLabelTypeMap(false);
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
//                .buildList());
//        }
//        for (Pair<String, String> phoneLabelPair : contact.getPhoneList()) {
//            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
//                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneLabelPair.first)
//                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, mobileLabelMap.get(phoneLabelPair.second))
//                .buildList());
//        }
//        for (Pair<String, String> addressLabelPair : contact.getAddressesList()) {
//            op_list.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
//                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
//                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
//                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, addressLabelPair.first)
//                .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, otherLabelMap.get(addressLabelPair.second))
//                .buildList());
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
