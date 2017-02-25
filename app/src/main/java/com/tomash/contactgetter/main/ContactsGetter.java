package com.tomash.contactgetter.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.SparseArray;

import com.tomash.contactgetter.entity.Address;
import com.tomash.contactgetter.entity.Contact;
import com.tomash.contactgetter.entity.Email;
import com.tomash.contactgetter.entity.IMAddress;
import com.tomash.contactgetter.entity.PhoneNumber;
import com.tomash.contactgetter.entity.Relation;
import com.tomash.contactgetter.entity.SpecialDate;
import com.tomash.contactgetter.interfaces.WithLabel;
import com.tomash.contactgetter.interfaces.WithLabelCreator;

import java.util.ArrayList;
import java.util.List;

public class ContactsGetter {
    private ContentResolver mResolver;
    final private String MAIN_DATA_KEY = "data1";
    final private String LABEL_DATA_KEY = "data2";
    final private String CUSTOM_LABEL_DATA_KEY = "data3";
    final private String LABEL_DATA_KEY_IM_ADDRESS = "data5";
    final private String CUSTOM_LABEL_DATA_KEY_IM_ADDRESS = "data6";
    final private String ID_KEY = "contact_id";


    private ContactsGetter(ContentResolver resolver) {
        this.mResolver = resolver;
    }

    private Cursor getAllContactsCursor(String ordering) {
        return mResolver.query(ContactsContract.Contacts.CONTENT_URI,
            null, null, null, ordering);
    }

    private Cursor getContactsCursorWithSelection(String ordering, String selection, String[] selectionArgs) {
        return mResolver.query(ContactsContract.Contacts.CONTENT_URI,
            null, selection, selectionArgs, ordering);
    }

    private List<Contact> getContacts(Cursor c) {
        List<Contact> contactsList = new ArrayList<>();
        SparseArray<List<PhoneNumber>> phonesDataMap = getDataMap(getCursorFromUri(ContactsContract.CommonDataKinds.Phone.CONTENT_URI), new WithLabelCreator<PhoneNumber>() {
            @Override
            public PhoneNumber create(String mainData, int contactId, int labelId, String labelName) {
                return new PhoneNumber(mainData, contactId, labelId, labelName);
            }
        });
        SparseArray<List<Address>> addressDataMap = getDataMap(getCursorFromUri(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI), new WithLabelCreator<Address>() {
            @Override
            public Address create(String mainData, int contactId, int labelId, String labelName) {
                return new Address(mainData, contactId, labelId, labelName);
            }
        });
        SparseArray<List<Email>> emailDataMap = getDataMap(getCursorFromUri(ContactsContract.CommonDataKinds.Email.CONTENT_URI), new WithLabelCreator<Email>() {
            @Override
            public Email create(String mainData, int contactId, int labelId, String labelName) {
                return new Email(mainData, contactId, labelId, labelName);
            }
        });
        SparseArray<List<SpecialDate>> specialDateMap = getDataMap(getCursorFromContentType(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE), new WithLabelCreator<SpecialDate>() {
            @Override
            public SpecialDate create(String mainData, int contactId, int labelId, String labelName) {
                return new SpecialDate(mainData, contactId, labelId, labelName);
            }
        });
        SparseArray<List<Relation>> relationMap = getDataMap(getCursorFromContentType(ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE), new WithLabelCreator<Relation>() {
            @Override
            public Relation create(String mainData, int contactId, int labelId, String labelName) {
                return new Relation(mainData, contactId, labelId, labelName);
            }
        });
        SparseArray<List<IMAddress>> imAddressesDataMap = getIMAddressesMap();
//        getEventsMap();
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
                .setImAddressesList(imAddressesDataMap.get(id))
                .setRelationsList(relationMap.get(id))
                .setSpecialDatesList(specialDateMap.get(id))
                .setCompositeName(c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))));
        }
        c.close();
        return contactsList;
    }


    private SparseArray<List<String>> getWebSitesMap() {
        SparseArray<List<String>> idSiteMap = new SparseArray<>();
        Cursor websiteCur = getCursorFromContentType(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE);
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

    private SparseArray<List<IMAddress>> getIMAddressesMap() {
        SparseArray<List<IMAddress>> idImAddressMap = new SparseArray<>();
        Cursor cur = getCursorFromContentType(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
        if (cur != null) {
            while (cur.moveToNext()) {
                int id = cur.getInt(cur.getColumnIndex(ID_KEY));
                String data = cur.getString(cur.getColumnIndex(MAIN_DATA_KEY));
                int labelId = cur.getInt(cur.getColumnIndex(LABEL_DATA_KEY_IM_ADDRESS));
                String customLabel = cur.getString(cur.getColumnIndex(CUSTOM_LABEL_DATA_KEY_IM_ADDRESS));
                IMAddress current = new IMAddress(data, id, labelId, customLabel);
                List<IMAddress> currentWebsiteList = idImAddressMap.get(id);
                if (currentWebsiteList == null) {
                    currentWebsiteList = new ArrayList<>();
                    currentWebsiteList.add(current);
                    idImAddressMap.put(id, currentWebsiteList);
                } else currentWebsiteList.add(current);
            }
            cur.close();
        }
        return idImAddressMap;
    }


    private SparseArray<String> getNotesMap() {
        SparseArray<String> idNoteMap = new SparseArray<>();
        Cursor noteCur = getCursorFromContentType(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE);
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


    private <T extends WithLabel> SparseArray<List<T>> getDataMap(Cursor dataCursor, WithLabelCreator<T> creator) {
        SparseArray<List<T>> dataSparseArray = new SparseArray<>();
        if (dataCursor != null) {
            while (dataCursor.moveToNext()) {
                int id = dataCursor.getInt(dataCursor.getColumnIndex(ID_KEY));
                String data = dataCursor.getString(dataCursor.getColumnIndex(MAIN_DATA_KEY));
                int labelId = dataCursor.getInt(dataCursor.getColumnIndex(LABEL_DATA_KEY));
                String customLabel = dataCursor.getString(dataCursor.getColumnIndex(CUSTOM_LABEL_DATA_KEY));
                T current = creator.create(data, id, labelId, customLabel);
                List<T> currentDataList = dataSparseArray.get(id);
                if (currentDataList == null) {
                    currentDataList = new ArrayList<>();
                    currentDataList.add(current);
                    dataSparseArray.put(id, currentDataList);
                } else currentDataList.add(current);
            }
            dataCursor.close();
        }
        return dataSparseArray;
    }


    private Cursor getCursorFromUri(Uri uri) {
        return mResolver.query(uri, null,
            null,
            null, null);
    }

    private Cursor getCursorFromContentType(String contentType) {
        String orgWhere = ContactsContract.Data.MIMETYPE + " = ?";
        String[] orgWhereParams = new String[]{contentType};
        return mResolver.query(ContactsContract.Data.CONTENT_URI,
            null, orgWhere, orgWhereParams, null);
    }

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////BUILDER//////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static class Builder {
        private ContentResolver mResolver;
        private Sorting mSortOrder = Sorting.BY_DISPLAY_NAME_ASC;
        private StringBuilder mSelectionBuilder = new StringBuilder();
        private List<String> mParamsList = new ArrayList<>();

        public Builder(Context ctx) {
            mResolver = ctx.getContentResolver();
        }

        /**
         * Sets sort order for all contacts
         * By default is ascending by display name
         *
         * @param sortOrder order to sort
         */
        public Builder setSortOrder(Sorting sortOrder) {
            this.mSortOrder = sortOrder;
            return this;
        }

        /**
         * Should get all contacts or contacts only with phones
         * By default returns all contacts
         */
        public Builder onlyWithPhones() {
            if (mSelectionBuilder.length() != 0)
                mSelectionBuilder.append(" AND ");
            mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER)
                .append(" = 1");
            return this;
        }

        /**
         * Searches for contacts with name that contains sequence
         *
         * @param nameLike sequence to search for
         */
        public Builder withNameLike(String nameLike) {
            if (mSelectionBuilder.length() != 0)
                mSelectionBuilder.append(" AND ");
            mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                .append(" LIKE ?");
            mParamsList.add("%" + nameLike + "%");
            return this;
        }

        /**
         * Searches for contacts that contains this number sequence
         *
         * @param number number sequence to search for
         */
        public Builder withPhoneLike(String number) {
            if (mSelectionBuilder.length() != 0)
                mSelectionBuilder.append(" AND ");
            mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.NUMBER)
                .append(" LIKE ?");
            mParamsList.add(number);
            return this;
        }


        /**
         * Builds list of contacts
         */
        public List<Contact> build() {
            ContactsGetter getter = new ContactsGetter(mResolver);
            Cursor mainCursor;
            if (mSelectionBuilder.length() == 0)
                mainCursor = getter.getAllContactsCursor(mSortOrder.getSorting());
            else
                mainCursor = getter.getContactsCursorWithSelection(mSortOrder.getSorting(), generateSelection(), generateSelectionArgs());
            return new ContactsGetter(mResolver).getContacts(mainCursor);
        }

        /**
         * Get first contact of null if no contacts with these params
         */
        public Contact firstOrNull() {
            List<Contact> contacts = build();
            if (contacts.isEmpty())
                return null;
            else
                return contacts.get(0);
        }

        private String generateSelection() {
            return mSelectionBuilder.toString();
        }

        private String[] generateSelectionArgs() {
            return mParamsList.toArray(new String[mParamsList.size()]);
        }

    }


}