package com.tomash.androidcontacts.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;

import com.tomash.androidcontacts.contactgetter.entity.Address;
import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.entity.Email;
import com.tomash.androidcontacts.contactgetter.entity.IMAddress;
import com.tomash.androidcontacts.contactgetter.entity.Organization;
import com.tomash.androidcontacts.contactgetter.entity.PhoneNumber;
import com.tomash.androidcontacts.contactgetter.entity.Relation;
import com.tomash.androidcontacts.contactgetter.entity.SpecialDate;
import com.tomash.androidcontacts.contactgetter.interfaces.WithLabel;
import com.tomash.androidcontacts.contactgetter.main.ContactDataFactory;

import java.security.SecureRandom;
import java.util.Random;

public class TestUtils {

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static SecureRandom rnd = new SecureRandom();

    public static <T extends WithLabel> T generateRandomWithLabel(Class<T> clazz, Context ctx) throws Exception {
        String mainData = randomString(7);
        int labelId = new Random().nextInt(3) + 1;
        return clazz.getConstructor(Context.class, String.class, int.class).newInstance(ctx, mainData, labelId);
    }

    public static <T extends WithLabel> T generateRandomWithLabel(Class<T> clazz) throws Exception {
        String mainData = randomString(8);
        String labelName = randomString(4);
        return clazz.getConstructor(String.class, String.class).newInstance(mainData, labelName);
    }

    public static ContactData createRandomContactData(Context ctx, int bitmapSize) throws Exception {
        ContactData contactData = ContactDataFactory.createEmpty();
        contactData.setUpdatedBitmap(Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_8888));
        contactData.setCompositeName(generateRandomString());
        contactData.setNickName(generateRandomString());
        contactData.setSipAddress(generateRandomString());
        contactData.setAccountType(generateRandomString());
        contactData.setAccountName(generateRandomString());
        contactData.setOrganization(new Organization(generateRandomString(), generateRandomString()));
        contactData.setNote(generateRandomString());
        contactData.setFavorite(rnd.nextBoolean());
        contactData.getPhoneList().add(generateRandomWithLabel(PhoneNumber.class));
        contactData.getPhoneList().add(generateRandomWithLabel(PhoneNumber.class, ctx));
        contactData.getPhoneList().add(generateRandomWithLabel(PhoneNumber.class, ctx));
        contactData.getAddressesList().add(generateRandomWithLabel(Address.class, ctx));
        contactData.getAddressesList().add(generateRandomWithLabel(Address.class));
        contactData.getAddressesList().add(generateRandomWithLabel(Address.class, ctx));
        contactData.getEmailList().add(generateRandomWithLabel(Email.class));
        contactData.getEmailList().add(generateRandomWithLabel(Email.class, ctx));
        contactData.getEmailList().add(generateRandomWithLabel(Email.class, ctx));
        contactData.getRelationsList().add(generateRandomWithLabel(Relation.class, ctx));
        contactData.getRelationsList().add(generateRandomWithLabel(Relation.class));
        contactData.getRelationsList().add(generateRandomWithLabel(Relation.class, ctx));
        contactData.getSpecialDatesList().add(generateRandomWithLabel(SpecialDate.class, ctx));
        contactData.getSpecialDatesList().add(generateRandomWithLabel(SpecialDate.class, ctx));
        contactData.getSpecialDatesList().add(generateRandomWithLabel(SpecialDate.class));
        contactData.getImAddressesList().add(generateRandomWithLabel(IMAddress.class));
        contactData.getImAddressesList().add(generateRandomWithLabel(IMAddress.class, ctx));
        contactData.getImAddressesList().add(generateRandomWithLabel(IMAddress.class, ctx));
        contactData.getWebsitesList().add(generateRandomString());
        contactData.getWebsitesList().add(generateRandomString());
        contactData.getWebsitesList().add(generateRandomString());
        return contactData;
    }

    public static String generateRandomString() {
        return randomString(7);
    }

    private static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    public static void deleteAllContacts(Context ctx) {
        ContentResolver cr = ctx.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null);
        while (cur.moveToNext()) {
            try {
                String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                cr.delete(uri, null, null);
            } catch (Exception e) {
            }
        }
    }

}
