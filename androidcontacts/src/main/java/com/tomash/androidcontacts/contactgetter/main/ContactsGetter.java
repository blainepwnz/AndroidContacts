package com.tomash.androidcontacts.contactgetter.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.SparseArray;

import com.tomash.androidcontacts.contactgetter.entity.Address;
import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.entity.Email;
import com.tomash.androidcontacts.contactgetter.entity.Group;
import com.tomash.androidcontacts.contactgetter.entity.IMAddress;
import com.tomash.androidcontacts.contactgetter.entity.NameData;
import com.tomash.androidcontacts.contactgetter.entity.Organization;
import com.tomash.androidcontacts.contactgetter.entity.PhoneNumber;
import com.tomash.androidcontacts.contactgetter.entity.Relation;
import com.tomash.androidcontacts.contactgetter.entity.SpecialDate;
import com.tomash.androidcontacts.contactgetter.interfaces.BaseFilter;
import com.tomash.androidcontacts.contactgetter.interfaces.WithLabel;
import com.tomash.androidcontacts.contactgetter.interfaces.WithLabelCreator;
import com.tomash.androidcontacts.contactgetter.utils.FilterUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class ContactsGetter {
    private ContentResolver mResolver;
    private Context mCtx;
    private List<FieldType> mEnabledFields;
    private String[] mSelectionArgs;
    private String mSorting;
    private String mSelection;
    final private String MAIN_DATA_KEY = "data1";
    final private String LABEL_DATA_KEY = "data2";
    final private String CUSTOM_LABEL_DATA_KEY = "data3";
    final private String ID_KEY = "contact_id";
    private Class<? extends ContactData> mContactDataClass;


    public ContactsGetter(Context ctx, List<FieldType> enabledFields, String sorting, String[] selectionArgs, String selection) {
        this.mCtx = ctx;
        this.mResolver = ctx.getContentResolver();
        this.mEnabledFields = enabledFields;
        this.mSelectionArgs = selectionArgs;
        this.mSorting = sorting;
        this.mSelection = selection;
    }

    private ContactsGetter setContactDataClass(Class<? extends ContactData> mContactDataClass) {
        this.mContactDataClass = mContactDataClass;
        return this;
    }

    private Cursor getContactsCursorWithSelection(String ordering, String selection, String[] selectionArgs) {
        return mResolver.query(ContactsContract.Contacts.CONTENT_URI,
            null, selection, selectionArgs, ordering);
    }

    private <T extends ContactData> T getContactData() {
        if (mContactDataClass == null) {
            return (T) new ContactData() {
            };
        }
        try {
            return (T) mContactDataClass.getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    private <T extends ContactData> List<T> getContacts() {
        Cursor c = getContactsCursorWithSelection(mSorting, mSelection, mSelectionArgs);
        List<T> contactsList = new ArrayList<>();
        SparseArray<List<PhoneNumber>> phonesDataMap = mEnabledFields.contains(FieldType.PHONE_NUMBERS) ? getDataMap(getCursorFromUri(ContactsContract.CommonDataKinds.Phone.CONTENT_URI), new WithLabelCreator<PhoneNumber>() {
            @Override
            public PhoneNumber create(String mainData, int contactId, int labelId, String labelName) {
                return new PhoneNumber(mainData, contactId, labelId, labelName, mCtx);
            }
        }) : new SparseArray<List<PhoneNumber>>();
        SparseArray<List<Address>> addressDataMap = mEnabledFields.contains(FieldType.ADDRESS) ? getDataMap(getCursorFromUri(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI), new WithLabelCreator<Address>() {
            @Override
            public Address create(String mainData, int contactId, int labelId, String labelName) {
                return new Address(mainData, contactId, labelId, labelName, mCtx);
            }
        }) : new SparseArray<List<Address>>();
        SparseArray<List<Email>> emailDataMap = mEnabledFields.contains(FieldType.EMAILS) ? getDataMap(getCursorFromUri(ContactsContract.CommonDataKinds.Email.CONTENT_URI), new WithLabelCreator<Email>() {
            @Override
            public Email create(String mainData, int contactId, int labelId, String labelName) {
                return new Email(mainData, contactId, labelId, labelName, mCtx);
            }
        }) : new SparseArray<List<Email>>();
        SparseArray<List<SpecialDate>> specialDateMap = mEnabledFields.contains(FieldType.SPECIAL_DATES) ? getDataMap(getCursorFromContentType(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE), new WithLabelCreator<SpecialDate>() {
            @Override
            public SpecialDate create(String mainData, int contactId, int labelId, String labelName) {
                return new SpecialDate(mainData, contactId, labelId, labelName, mCtx);
            }
        }) : new SparseArray<List<SpecialDate>>();
        SparseArray<List<Relation>> relationMap = mEnabledFields.contains(FieldType.RELATIONS) ? getDataMap(getCursorFromContentType(ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE), new WithLabelCreator<Relation>() {
            @Override
            public Relation create(String mainData, int contactId, int labelId, String labelName) {
                return new Relation(mainData, contactId, labelId, labelName, mCtx);
            }
        }) : new SparseArray<List<Relation>>();
        SparseArray<List<IMAddress>> imAddressesDataMap = mEnabledFields.contains(FieldType.IM_ADDRESSES) ? getIMAddressesMap() : new SparseArray<List<IMAddress>>();
        SparseArray<List<String>> websitesDataMap = mEnabledFields.contains(FieldType.WEBSITES) ? getWebSitesMap() : new SparseArray<List<String>>();
        SparseArray<String> notesDataMap = mEnabledFields.contains(FieldType.NOTES) ? getStringDataMap(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE) : new SparseArray<String>();
        SparseArray<String> nicknameDataMap = mEnabledFields.contains(FieldType.NICKNAME) ? getStringDataMap(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE) : new SparseArray<String>();
        SparseArray<String> sipDataMap = mEnabledFields.contains(FieldType.SIP) ? getStringDataMap(ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE) : new SparseArray<String>();
        SparseArray<Organization> organisationDataMap = mEnabledFields.contains(FieldType.ORGANIZATION) ? getOrganizationDataMap() : new SparseArray<Organization>();
        SparseArray<NameData> nameDataMap = mEnabledFields.contains(FieldType.NAME_DATA) ? getNameDataMap() : new SparseArray<NameData>();
        SparseArray<List<Group>> groupsDataMap = mEnabledFields.contains(FieldType.GROUPS) ? getGroupsDataMap() : new SparseArray<List<Group>>();
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));
            long date = c.getLong(c.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));
            String photoUriString = c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI));
            String lookupKey = c.getString(c.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri photoUri = photoUriString == null ? Uri.EMPTY : Uri.parse(c.getString(c.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)));
            contactsList.add((T) getContactData()
                .setContactId(id)
                .setLookupKey(lookupKey)
                .setLastModificationDate(date)
                .setPhoneList(phonesDataMap.get(id))
                .setAddressesList(addressDataMap.get(id))
                .setEmailList(emailDataMap.get(id))
                .setWebsitesList(websitesDataMap.get(id))
                .setNote(notesDataMap.get(id))
                .setImAddressesList(imAddressesDataMap.get(id))
                .setRelationsList(relationMap.get(id))
                .setSpecialDatesList(specialDateMap.get(id))
                .setNickName(nicknameDataMap.get(id))
                .setOrganization(organisationDataMap.get(id))
                .setSipAddress(sipDataMap.get(id))
                .setNameData(nameDataMap.get(id))
                .setPhotoUri(photoUri)
                .setGroupList(groupsDataMap.get(id))
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

    private SparseArray<Group> getGroupsMap() {
        SparseArray<Group> idGroupMap = new SparseArray<>();
        Cursor groupCursor = mResolver.query(
            ContactsContract.Groups.CONTENT_URI,
            new String[]{
                ContactsContract.Groups._ID,
                ContactsContract.Groups.TITLE
            }, null, null, null
        );
        if (groupCursor != null) {
            while (groupCursor.moveToNext()) {
                int id = groupCursor.getInt(groupCursor.getColumnIndex(ContactsContract.Groups._ID));
                String title = groupCursor.getString(groupCursor.getColumnIndex(ContactsContract.Groups.TITLE));
                idGroupMap.put(id, new Group()
                    .setGroupId(id)
                    .setGroupTitle(title));
            }
            groupCursor.close();
        }
        return idGroupMap;
    }

    private SparseArray<List<Group>> getGroupsDataMap() {
        SparseArray<List<Group>> idListGroupMap = new SparseArray<>();
        SparseArray<Group> groupMapById = getGroupsMap();
        Cursor groupMembershipCursor = getCursorFromContentType(ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE);
        if (groupMembershipCursor != null) {
            while (groupMembershipCursor.moveToNext()) {
                int id = groupMembershipCursor.getInt(groupMembershipCursor.getColumnIndex(ID_KEY));
                int groupId = groupMembershipCursor.getInt(groupMembershipCursor.getColumnIndex(MAIN_DATA_KEY));
                List<Group> currentIdGroupList = idListGroupMap.get(id);
                if (currentIdGroupList == null) {
                    currentIdGroupList = new ArrayList<>();
                    currentIdGroupList.add(groupMapById.get(groupId));
                    idListGroupMap.put(id, currentIdGroupList);
                } else
                    currentIdGroupList.add(groupMapById.get(groupId));
            }
            groupMembershipCursor.close();
        }
        return idListGroupMap;
    }


    private SparseArray<NameData> getNameDataMap() {
        Cursor nameCursor = getCursorFromContentType(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        SparseArray<NameData> nameDataSparseArray = new SparseArray<>();
        if (nameCursor != null) {
            while (nameCursor.moveToNext()) {
                int id = nameCursor.getInt(nameCursor.getColumnIndex(ID_KEY));
                if (nameDataSparseArray.get(id) == null)
                    nameDataSparseArray.put(id, new NameData()
                        .setFullName(nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME)))
                        .setFirstName(nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME)))
                        .setSurname(nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME)))
                        .setNamePrefix(nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PREFIX)))
                        .setMiddleName(nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME)))
                        .setNameSuffix(nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.SUFFIX)))
                        .setPhoneticFirst(nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME)))
                        .setPhoneticMiddle(nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME)))
                        .setPhoneticLast(nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME)))
                    );
            }
            nameCursor.close();
        }


        return nameDataSparseArray;
    }

    private SparseArray<List<IMAddress>> getIMAddressesMap() {
        SparseArray<List<IMAddress>> idImAddressMap = new SparseArray<>();
        Cursor cur = getCursorFromContentType(ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
        if (cur != null) {
            while (cur.moveToNext()) {
                int id = cur.getInt(cur.getColumnIndex(ID_KEY));
                String data = cur.getString(cur.getColumnIndex(MAIN_DATA_KEY));
                int labelId = cur.getInt(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.PROTOCOL));
                String customLabel = cur.getString(cur.getColumnIndex(ContactsContract.CommonDataKinds.Im.CUSTOM_PROTOCOL));
                IMAddress current = new IMAddress(data, id, labelId, customLabel, mCtx);
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


    private SparseArray<String> getStringDataMap(String contentType) {
        SparseArray<String> idNoteMap = new SparseArray<>();
        Cursor noteCur = getCursorFromContentType(contentType);
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

    private SparseArray<Organization> getOrganizationDataMap() {
        SparseArray<Organization> idOrganizationMap = new SparseArray<>();
        Cursor noteCur = getCursorFromContentType(ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
        if (noteCur != null) {
            while (noteCur.moveToNext()) {
                int id = noteCur.getInt(noteCur.getColumnIndex(ID_KEY));
                String organizationName = noteCur.getString(noteCur.getColumnIndex(MAIN_DATA_KEY));
                String organizationTitle = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));
                idOrganizationMap.put(id, new Organization()
                    .setName(organizationName)
                    .setTitle(organizationTitle));
            }
            noteCur.close();
        }
        return idOrganizationMap;
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
        private Context mCtx;
        private Sorting mSortOrder = Sorting.BY_DISPLAY_NAME_ASC;
        private StringBuilder mSelectionBuilder = new StringBuilder();
        private List<String> mParamsList = new ArrayList<>(2);
        private List<BaseFilter> mFilterList = new ArrayList<>(8);
        private List<FieldType> mEnabledFields = new ArrayList<>(8);

        public Builder(Context ctx) {
            mCtx = ctx;
        }

        /**
         * <p>
         * Sets sort order for all contacts
         * </p>
         * <p>
         * Sort types could be found here {@link Sorting}
         * </p>
         * <p>
         * By default is ascending by display name
         * </p>
         *
         * @param sortOrder order to sort
         */
        public Builder setSortOrder(Sorting sortOrder) {
            this.mSortOrder = sortOrder;
            return this;
        }

        /**
         * <p>
         * Should get all contacts or contacts only with phones
         * </p>
         * <p>
         * Note : Will automatically query for phone numbers.
         * </p>
         * <p>
         * No need to explicitly add Phone numbers to field list
         * </p>
         * By default returns all contacts
         */
        public Builder onlyWithPhones() {
            if (mSelectionBuilder.length() != 0)
                mSelectionBuilder.append(" AND ");
            mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER)
                .append(" = 1");
            addField(FieldType.PHONE_NUMBERS);
            return this;
        }

        /**
         * <p>
         * Should get contacts only with photos or not
         * </p>
         * By default returns all contacts
         */
        public Builder onlyWithPhotos() {
            if (mSelectionBuilder.length() != 0)
                mSelectionBuilder.append(" AND ");
            mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
                .append(" IS NOT NULL");
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
         * Searches for contacts with this name
         *
         * @param name name to search for
         */
        public Builder withName(String name) {
            if (mSelectionBuilder.length() != 0)
                mSelectionBuilder.append(" AND ");
            mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                .append(" = ?");
            mParamsList.add(name);
            return this;
        }

        /**
         * Searches for contacts that contains this number sequence
         *
         * @param number number sequence to search for
         */
        public Builder withPhoneLike(final String number) {
            mFilterList.add(FilterUtils.withPhoneLikeFilter(number));
            return onlyWithPhones();
        }

        /**
         * Searches for contacts with this number
         *
         * @param number number to search for
         */
        public Builder withPhone(final String number) {
            mFilterList.add(FilterUtils.withPhoneFilter(number));
            return onlyWithPhones();
        }

        /**
         * Searches for contacts with this email
         * Implicitly adds Email field
         *
         * @param email email to search for
         */
        public Builder withEmail(final String email) {
            addField(FieldType.EMAILS);
            mFilterList.add(FilterUtils.withEmailFilter(email));
            return this;
        }

        /**
         * Searches for contacts that contains sequence
         * Implicitly adds Email field
         *
         * @param sequence sequence to search for
         */
        public Builder withEmailLike(final String sequence) {
            addField(FieldType.EMAILS);
            mFilterList.add(FilterUtils.withEmailLikeFilter(sequence));
            return this;
        }

        /**
         * Searches for contacts with this number
         * Implicitly adds Address field
         *
         * @param number number to search for
         */
        public Builder withAddress(final String number) {
            addField(FieldType.ADDRESS);
            mFilterList.add(FilterUtils.withAddressFilter(number));
            return this;
        }

        /**
         * Searches for addresses that contains this sequence
         * Implicitly adds Address field
         *
         * @param sequence sequence to search for
         */
        public Builder withAddressLike(final String sequence) {
            addField(FieldType.ADDRESS);
            mFilterList.add(FilterUtils.withAddressLikeFilter(sequence));
            return this;
        }


        private <T extends ContactData> List<T> applyFilters(List<T> contactList) {
            for (BaseFilter filter : mFilterList) {
                for (Iterator<T> iterator = contactList.iterator(); iterator.hasNext(); ) {
                    ContactData contact = iterator.next();
                    if (!filter.passedFilter(contact))
                        iterator.remove();
                }
            }
            return contactList;
        }

        /**
         * <p>
         * Applies custom filter to query on contacts list
         * </p>
         * <p>
         * Additional filters and example implementations could be found here {@link FilterUtils}
         * </p>
         *
         * @param filter filter to apply
         */
        public Builder applyCustomFilter(BaseFilter filter) {
            mFilterList.add(filter);
            return this;
        }

        /**
         * <p>
         * Enables all fields for query
         * </p>
         * <p>
         * Note : Consider to enable fields you need with {@link #addField(FieldType...)} to increase performance
         * </p>
         */
        public Builder allFields() {
            addField(FieldType.values());
            return this;
        }

        /**
         * <p>
         * Enables fields that should be queried
         * </p>
         * <p>
         * Number of fields influence on performance
         * </p>
         *
         * @param fieldType field type you want to add
         */
        public Builder addField(FieldType... fieldType) {
            mEnabledFields.addAll(Arrays.asList(fieldType));
            return this;
        }

        private ContactsGetter initGetter() {
            ContactsGetter getter;
            if (mSelectionBuilder.length() == 0)
                getter = new ContactsGetter(mCtx, mEnabledFields, mSortOrder.getSorting(), null, null);
            else
                getter = new ContactsGetter(mCtx, mEnabledFields, mSortOrder.getSorting(), generateSelectionArgs(), generateSelection());
            return getter;
        }


        /**
         * Builds list of contacts
         * @param T class of object you want to get data
         */
        public <T extends ContactData> List<T> buildList(Class<? extends ContactData> T) {
            return applyFilters((List<T>) initGetter()
                .setContactDataClass(T)
                .getContacts());
        }

        /**
         * Builds list of contacts
         */
        public List<ContactData> buildList() {
            return applyFilters(initGetter().getContacts());
        }
        /**
         * Gets contact by local id
         *
         * @param id id to search for
         * @return contact with data specified by options or null if no contact with this id
         */
        public ContactData getById(int id) {
            if (mSelectionBuilder.length() != 0)
                mSelectionBuilder.append(" AND ");
            mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone._ID)
                .append(" = ?");
            mParamsList.add(String.valueOf(id));
            return firstOrNull();
        }

        /**
         * Gets contact by local id
         *
         * @param id id to search for
         * @param T class of object you want to get data
         * @return contact with data specified by options or null if no contact with this id
         */
        public<T extends ContactData> T getById(int id,Class<T> T) {
            if (mSelectionBuilder.length() != 0)
                mSelectionBuilder.append(" AND ");
            mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone._ID)
                .append(" = ?");
            mParamsList.add(String.valueOf(id));
            return firstOrNull(T);
        }

        /**
         * Get first contact of null if no contacts with these params
         */
        public ContactData firstOrNull() {
            List<ContactData> contacts = buildList();
            if (contacts.isEmpty())
                return null;
            else
                return contacts.get(0);
        }

        /**
         * Get first contact of null if no contacts with these params
         * @param T class of object you want to get data
         */
        public <T extends ContactData> T firstOrNull(Class<T> T) {
            List<T> contacts = buildList(T);
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