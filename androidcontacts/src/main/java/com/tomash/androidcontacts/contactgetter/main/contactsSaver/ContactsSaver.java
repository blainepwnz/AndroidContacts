package com.tomash.androidcontacts.contactgetter.main.contactsSaver;

import android.content.*;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import com.tomash.androidcontacts.contactgetter.entity.*;
import com.tomash.androidcontacts.contactgetter.interfaces.WithLabel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

class ContactsSaver {
    private ContentResolver mResolver;

    public ContactsSaver(ContentResolver resolver) {
        mResolver = resolver;
    }

    public int[] insertContacts(List<ContactData> contactDataList) {
        ArrayList<ContentValues> cvList = new ArrayList<>(100);

        ContentProviderResult[] results = createContacts(contactDataList);
        int[] ids = new int[results.length];
        for (int i = 0; i < results.length; i++) {
            int id = Integer.parseInt(results[i].uri.getLastPathSegment());
            generateInsertOperations(cvList, contactDataList.get(i), id);
            ids[i] = id;
        }
        mResolver.bulkInsert(ContactsContract.Data.CONTENT_URI, cvList.toArray(new ContentValues[cvList.size()]));
        return ids;
    }

    private void generateInsertOperations(List<ContentValues> contentValuesList, ContactData contactData, int id) {
        for (PhoneNumber number : contactData.getPhoneList()) {
            contentValuesList.add(getPhonesCV(number, id));
        }
        for (Address address : contactData.getAddressesList()) {
            contentValuesList.add(getWithLabelCV(ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE, address, id));
        }
        for (Email email : contactData.getEmailList()) {
            contentValuesList.add(getWithLabelCV(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE, email, id));
        }
        for (SpecialDate specialDate : contactData.getSpecialDatesList()) {
            contentValuesList.add(getWithLabelCV(ContactsContract.CommonDataKinds.Event.CONTENT_ITEM_TYPE, specialDate, id));
        }
        for (Relation relation : contactData.getRelationsList()) {
            contentValuesList.add(getWithLabelCV(ContactsContract.CommonDataKinds.Relation.CONTENT_ITEM_TYPE, relation, id));
        }
        for (String webSite : contactData.getWebsitesList()) {
            contentValuesList.add(getStringTypeCV(ContactsContract.CommonDataKinds.Website.CONTENT_ITEM_TYPE, webSite, id));
        }
        for (IMAddress imAddress : contactData.getImAddressesList()) {
            contentValuesList.add(getImAddressCV(imAddress, id));
        }
        if (!contactData.getNote().isEmpty())
            contentValuesList.add(getStringTypeCV(ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE, contactData.getNote(), id));
        if (!contactData.getNickName().isEmpty())
            contentValuesList.add(getStringTypeCV(ContactsContract.CommonDataKinds.Nickname.CONTENT_ITEM_TYPE, contactData.getNickName(), id));
        if (!contactData.getSipAddress().isEmpty())
            contentValuesList.add(getStringTypeCV(ContactsContract.CommonDataKinds.SipAddress.CONTENT_ITEM_TYPE, contactData.getSipAddress(), id));
        contentValuesList.add(getNameDataCV(contactData, id));
        Organization currentOrganization = contactData.getOrganization();
        if (!currentOrganization.getName().isEmpty() || !currentOrganization.getTitle().isEmpty())
            contentValuesList.add(getOrganizationTypeCV(currentOrganization, id));
        saveUpdatedPhoto(id, contactData);
    }

    /**
     * Save updated photo for the specified raw-contact.
     */
    private void saveUpdatedPhoto(long rawContactId, ContactData contactData) {
        try {
            InputStream inputStream;
            if (contactData.getUpdatedPhotoUri() != null) {
                inputStream = mResolver.openInputStream(contactData.getUpdatedPhotoUri());
                contactData.setUpdatedPhotoUri(null);
            } else if (contactData.getUpdatedBitmap() != null) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                contactData.getUpdatedBitmap().compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();
                inputStream = new ByteArrayInputStream(bitmapdata);
                contactData.setUpdatedBitmap(null);
            } else {
                return;
            }

            final Uri outputUri = Uri.withAppendedPath(
                ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI, rawContactId),
                ContactsContract.RawContacts.DisplayPhoto.CONTENT_DIRECTORY);

            FileOutputStream outputStream;
            outputStream = mResolver
                .openAssetFileDescriptor(outputUri, "rw").createOutputStream();
            final byte[] buffer = new byte[16 * 1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0)
                outputStream.write(buffer, 0, length);
            outputStream.close();
            inputStream.close();
        } catch (Exception ignored) {
        }
    }

    private ContentValues getWithLabelCV(String contentType, WithLabel withLabel, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        contentValues.put(ContactsContract.Data.MIMETYPE, contentType);
        contentValues.put(ContactsContract.Data.DATA1, withLabel.getMainData());
        contentValues.put(ContactsContract.Data.DATA2, withLabel.getLabelId());
        if (withLabel.getLabelId() == withLabel.getCustomLabelId())
            contentValues.put(ContactsContract.Data.DATA3, withLabel.getLabelName());
        return contentValues;
    }

    private ContentValues getNameDataCV(ContactData contactData, int id) {
        NameData current = contactData.getNameData();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, current.getFullName().isEmpty() ? contactData.getCompositeName() : current.getFullName());
        if (!current.getFirstName().isEmpty())
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, current.getFirstName());
        if (!current.getSurname().isEmpty())
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME, current.getSurname());
        if (!current.getNamePrefix().isEmpty())
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.PREFIX, current.getNamePrefix());
        if (!current.getNameSuffix().isEmpty())
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.SUFFIX, current.getNameSuffix());
        if (!current.getMiddleName().isEmpty())
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.MIDDLE_NAME, current.getMiddleName());
        if (!current.getPhoneticFirst().isEmpty())
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_GIVEN_NAME, current.getPhoneticFirst());
        if (!current.getPhoneticMiddle().isEmpty())
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_MIDDLE_NAME, current.getPhoneticMiddle());
        if (!current.getPhoneticLast().isEmpty())
            contentValues.put(ContactsContract.CommonDataKinds.StructuredName.PHONETIC_FAMILY_NAME, current.getPhoneticLast());
        return contentValues;
    }

    private ContentValues getImAddressCV(IMAddress imAddress, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.Data.DATA1, imAddress.getMainData());
        contentValues.put(ContactsContract.Data.DATA2, ContactsContract.CommonDataKinds.Im.TYPE_HOME);
        contentValues.put(ContactsContract.Data.DATA5, imAddress.getLabelId());
        if (imAddress.getLabelId() == imAddress.getCustomLabelId())
            contentValues.put(ContactsContract.Data.DATA6, imAddress.getLabelName());
        return contentValues;
    }

    private ContentValues getPhonesCV(PhoneNumber phoneNumber, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.Data.DATA1, phoneNumber.getMainData());
        contentValues.put(ContactsContract.Data.DATA2, phoneNumber.getLabelId());
        contentValues.put(ContactsContract.Data.IS_PRIMARY, phoneNumber.isPrimary());
        if (phoneNumber.getLabelId() == phoneNumber.getCustomLabelId())
            contentValues.put(ContactsContract.Data.DATA3, phoneNumber.getLabelName());
        return contentValues;
    }

    private ContentValues getStringTypeCV(String contentType, String data, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        contentValues.put(ContactsContract.Data.MIMETYPE, contentType);
        contentValues.put(ContactsContract.Data.DATA1, data);
        return contentValues;
    }

    private ContentValues getOrganizationTypeCV(Organization organization, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, id);
        contentValues.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE);
        contentValues.put(ContactsContract.Data.DATA1, organization.getName());
        contentValues.put(ContactsContract.Data.DATA4, organization.getTitle());
        return contentValues;
    }

    private ContentProviderResult[] createContacts(List<ContactData> contacts) {
        ContentProviderResult[] results = null;
        ArrayList<ContentProviderOperation> op_list = new ArrayList<>();
        for (int i = 0; i < contacts.size(); i++) {
            ContactData contactData = contacts.get(i);
            op_list.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, contactData.getAccountType())
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, contactData.getAccountName())
                .withValue(ContactsContract.RawContacts.STARRED, contactData.isFavorite() ? 1 : 0)
                .build());
        }
        try {
            results = mResolver.applyBatch(ContactsContract.AUTHORITY, op_list);
        } catch (Exception ignored) {
        }
        return results;
    }

}
