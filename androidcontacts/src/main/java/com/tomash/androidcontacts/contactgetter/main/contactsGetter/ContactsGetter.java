package com.tomash.androidcontacts.contactgetter.main.contactsGetter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Im;
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
import com.tomash.androidcontacts.contactgetter.interfaces.WithLabel;
import com.tomash.androidcontacts.contactgetter.main.FieldType;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Event;
import static android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import static android.provider.ContactsContract.CommonDataKinds.Nickname;
import static android.provider.ContactsContract.CommonDataKinds.Note;
import static android.provider.ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE;
import static android.provider.ContactsContract.CommonDataKinds.Organization.TITLE;
import static android.provider.ContactsContract.CommonDataKinds.Phone;
import static android.provider.ContactsContract.CommonDataKinds.SipAddress;
import static android.provider.ContactsContract.CommonDataKinds.StructuredName;
import static android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import static android.provider.ContactsContract.CommonDataKinds.Website;

class ContactsGetter {
    private ContentResolver mResolver;
    private Context mCtx;
    private List<FieldType> mEnabledFields;
    private String[] mSelectionArgs;
    private String mSorting;
    private String mSelection;
    private static final String MAIN_DATA_KEY = "data1";
    private static final String LABEL_DATA_KEY = "data2";
    private static final String CUSTOM_LABEL_DATA_KEY = "data3";
    private static final String ID_KEY = "contact_id";
    private static final String[] WITH_LABEL_PROJECTION = new String[]{ID_KEY, MAIN_DATA_KEY, LABEL_DATA_KEY, CUSTOM_LABEL_DATA_KEY};
    private static final String[] CONTACTS_PROJECTION = new String[]{ContactsContract.Contacts._ID, ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP,
        ContactsContract.Contacts.PHOTO_URI, ContactsContract.Contacts.LOOKUP_KEY, ContactsContract.Contacts.DISPLAY_NAME};
    private static final String[] CONTACTS_PROJECTION_LOW_API = new String[]{ContactsContract.Contacts._ID,
        ContactsContract.Contacts.PHOTO_URI, ContactsContract.Contacts.LOOKUP_KEY, ContactsContract.Contacts.DISPLAY_NAME};
    private Class<? extends ContactData> mContactDataClass;


    public ContactsGetter(Context ctx, List<FieldType> enabledFields, String sorting, String[] selectionArgs, String selection) {
        this.mCtx = ctx;
        this.mResolver = ctx.getContentResolver();
        this.mEnabledFields = enabledFields;
        this.mSelectionArgs = selectionArgs;
        this.mSorting = sorting;
        this.mSelection = selection;
    }

    ContactsGetter setContactDataClass(Class<? extends ContactData> mContactDataClass) {
        this.mContactDataClass = mContactDataClass;
        return this;
    }

    private Cursor getContactsCursorWithSelection(String ordering, String selection, String[] selectionArgs) {
        return mResolver.query(ContactsContract.Contacts.CONTENT_URI,
            android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1 ? CONTACTS_PROJECTION : CONTACTS_PROJECTION_LOW_API, selection, selectionArgs, ordering);
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


    <T extends ContactData> List<T> getContacts() {
        Cursor c = getContactsCursorWithSelection(mSorting, mSelection, mSelectionArgs);
        List<T> contactsList = new ArrayList<>();
        if (c == null)
            return contactsList;
        SparseArray<List<PhoneNumber>> phonesDataMap = mEnabledFields.contains(FieldType.PHONE_NUMBERS) ? getDataMap(getCursorFromContentType(WITH_LABEL_PROJECTION, Phone.CONTENT_ITEM_TYPE), new WithLabelCreator<PhoneNumber>() {
            @Override
            public PhoneNumber create(String mainData, int contactId, int labelId, String labelName) {
                PhoneNumber number;
                if (labelName != null)
                    number = new PhoneNumber(mainData, labelName);
                else
                    number = new PhoneNumber(mCtx, mainData, labelId);
                number.setContactId(contactId);
                return number;
            }
        }) : new SparseArray<List<PhoneNumber>>();
        SparseArray<List<Address>> addressDataMap = mEnabledFields.contains(FieldType.ADDRESS) ? getDataMap(getCursorFromContentType(WITH_LABEL_PROJECTION, StructuredPostal.CONTENT_ITEM_TYPE), new WithLabelCreator<Address>() {
            @Override
            public Address create(String mainData, int contactId, int labelId, String labelName) {
                Address address;
                if (labelName != null)
                    address = new Address(mainData, labelName);
                else
                    address = new Address(mCtx, mainData, labelId);
                address.setContactId(contactId);
                return address;
            }
        }) : new SparseArray<List<Address>>();
        SparseArray<List<Email>> emailDataMap = mEnabledFields.contains(FieldType.EMAILS) ? getDataMap(getCursorFromContentType(WITH_LABEL_PROJECTION, CommonDataKinds.Email.CONTENT_ITEM_TYPE), new WithLabelCreator<Email>() {
            @Override
            public Email create(String mainData, int contactId, int labelId, String labelName) {
                Email email;
                if (labelName != null)
                    email = new Email(mainData, labelName);
                else
                    email = new Email(mCtx, mainData, labelId);
                email.setContactId(contactId);
                return email;
            }
        }) : new SparseArray<List<Email>>();
        SparseArray<List<SpecialDate>> specialDateMap = mEnabledFields.contains(FieldType.SPECIAL_DATES) ? getDataMap(getCursorFromContentType(WITH_LABEL_PROJECTION, Event.CONTENT_ITEM_TYPE), new WithLabelCreator<SpecialDate>() {
            @Override
            public SpecialDate create(String mainData, int contactId, int labelId, String labelName) {
                SpecialDate specialData;
                if (labelName != null)
                    specialData = new SpecialDate(mainData, labelName);
                else
                    specialData = new SpecialDate(mCtx, mainData, labelId);
                specialData.setContactId(contactId);
                return specialData;
            }
        }) : new SparseArray<List<SpecialDate>>();
        SparseArray<List<Relation>> relationMap = mEnabledFields.contains(FieldType.RELATIONS) ? getDataMap(getCursorFromContentType(WITH_LABEL_PROJECTION, CommonDataKinds.Relation.CONTENT_ITEM_TYPE), new WithLabelCreator<Relation>() {
            @Override
            public Relation create(String mainData, int contactId, int labelId, String labelName) {
                Relation relation;
                if (labelName != null)
                    relation = new Relation(mainData, labelName);
                else
                    relation = new Relation(mCtx, mainData, labelId);
                relation.setContactId(contactId);
                return relation;
            }
        }) : new SparseArray<List<Relation>>();
        SparseArray<List<IMAddress>> imAddressesDataMap = mEnabledFields.contains(FieldType.IM_ADDRESSES) ? getIMAddressesMap() : new SparseArray<List<IMAddress>>();
        SparseArray<List<String>> websitesDataMap = mEnabledFields.contains(FieldType.WEBSITES) ? getWebSitesMap() : new SparseArray<List<String>>();
        SparseArray<String> notesDataMap = mEnabledFields.contains(FieldType.NOTES) ? getStringDataMap(Note.CONTENT_ITEM_TYPE) : new SparseArray<String>();
        SparseArray<String> nicknameDataMap = mEnabledFields.contains(FieldType.NICKNAME) ? getStringDataMap(Nickname.CONTENT_ITEM_TYPE) : new SparseArray<String>();
        SparseArray<String> sipDataMap = mEnabledFields.contains(FieldType.SIP) ? getStringDataMap(SipAddress.CONTENT_ITEM_TYPE) : new SparseArray<String>();
        SparseArray<Organization> organisationDataMap = mEnabledFields.contains(FieldType.ORGANIZATION) ? getOrganizationDataMap() : new SparseArray<Organization>();
        SparseArray<NameData> nameDataMap = mEnabledFields.contains(FieldType.NAME_DATA) ? getNameDataMap() : new SparseArray<NameData>();
        SparseArray<List<Group>> groupsDataMap = mEnabledFields.contains(FieldType.GROUPS) ? getGroupsDataMap() : new SparseArray<List<Group>>();
        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID));
            long date = 0;
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1)
                date = c.getLong(c.getColumnIndex(ContactsContract.Contacts.CONTACT_LAST_UPDATED_TIMESTAMP));
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
        Cursor websiteCur = getCursorFromContentType(new String[]{ID_KEY, MAIN_DATA_KEY}, Website.CONTENT_ITEM_TYPE);
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
        Cursor groupMembershipCursor = getCursorFromContentType(new String[]{ID_KEY, MAIN_DATA_KEY}, GroupMembership.CONTENT_ITEM_TYPE);
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
        Cursor nameCursor = getCursorFromContentType(new String[]{ID_KEY, StructuredName.DISPLAY_NAME, StructuredName.GIVEN_NAME, StructuredName.PHONETIC_MIDDLE_NAME, StructuredName.PHONETIC_FAMILY_NAME,
            StructuredName.FAMILY_NAME, StructuredName.PREFIX, StructuredName.MIDDLE_NAME, StructuredName.SUFFIX, StructuredName.PHONETIC_GIVEN_NAME}, StructuredName.CONTENT_ITEM_TYPE);
        SparseArray<NameData> nameDataSparseArray = new SparseArray<>();
        if (nameCursor != null) {
            while (nameCursor.moveToNext()) {
                int id = nameCursor.getInt(nameCursor.getColumnIndex(ID_KEY));
                if (nameDataSparseArray.get(id) == null)
                    nameDataSparseArray.put(id, new NameData()
                        .setFullName(nameCursor.getString(nameCursor.getColumnIndex(StructuredName.DISPLAY_NAME)))
                        .setFirstName(nameCursor.getString(nameCursor.getColumnIndex(StructuredName.GIVEN_NAME)))
                        .setSurname(nameCursor.getString(nameCursor.getColumnIndex(StructuredName.FAMILY_NAME)))
                        .setNamePrefix(nameCursor.getString(nameCursor.getColumnIndex(StructuredName.PREFIX)))
                        .setMiddleName(nameCursor.getString(nameCursor.getColumnIndex(StructuredName.MIDDLE_NAME)))
                        .setNameSuffix(nameCursor.getString(nameCursor.getColumnIndex(StructuredName.SUFFIX)))
                        .setPhoneticFirst(nameCursor.getString(nameCursor.getColumnIndex(StructuredName.PHONETIC_GIVEN_NAME)))
                        .setPhoneticMiddle(nameCursor.getString(nameCursor.getColumnIndex(StructuredName.PHONETIC_MIDDLE_NAME)))
                        .setPhoneticLast(nameCursor.getString(nameCursor.getColumnIndex(StructuredName.PHONETIC_FAMILY_NAME)))
                    );
            }
            nameCursor.close();
        }


        return nameDataSparseArray;
    }

    private SparseArray<List<IMAddress>> getIMAddressesMap() {
        SparseArray<List<IMAddress>> idImAddressMap = new SparseArray<>();
        Cursor cur = getCursorFromContentType(new String[]{ID_KEY, MAIN_DATA_KEY, Im.PROTOCOL, Im.CUSTOM_PROTOCOL}, Im.CONTENT_ITEM_TYPE);
        if (cur != null) {
            while (cur.moveToNext()) {
                int id = cur.getInt(cur.getColumnIndex(ID_KEY));
                String data = cur.getString(cur.getColumnIndex(MAIN_DATA_KEY));
                int labelId = cur.getInt(cur.getColumnIndex(Im.PROTOCOL));
                String customLabel = cur.getString(cur.getColumnIndex(Im.CUSTOM_PROTOCOL));
                IMAddress current;
                if (customLabel == null)
                    current = new IMAddress(mCtx, data, labelId);
                else
                    current = new IMAddress(data, customLabel);
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
        Cursor noteCur = getCursorFromContentType(new String[]{ID_KEY, MAIN_DATA_KEY}, contentType);
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
        Cursor noteCur = getCursorFromContentType(new String[]{ID_KEY, MAIN_DATA_KEY, TITLE}, CONTENT_ITEM_TYPE);
        if (noteCur != null) {
            while (noteCur.moveToNext()) {
                int id = noteCur.getInt(noteCur.getColumnIndex(ID_KEY));
                String organizationName = noteCur.getString(noteCur.getColumnIndex(MAIN_DATA_KEY));
                String organizationTitle = noteCur.getString(noteCur.getColumnIndex(TITLE));
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

    private Cursor getCursorFromContentType(String[] projection, String contentType) {
        String orgWhere = ContactsContract.Data.MIMETYPE + " = ?";
        String[] orgWhereParams = new String[]{contentType};
        return mResolver.query(ContactsContract.Data.CONTENT_URI,
            projection, orgWhere, orgWhereParams, null);
    }

    interface WithLabelCreator<T extends WithLabel> {
        T create(String mainData, int contactId, int labelId, String labelName);
    }

}