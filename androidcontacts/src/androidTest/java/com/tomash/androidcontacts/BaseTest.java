package com.tomash.androidcontacts;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.tomash.androidcontacts.contactgetter.entity.Address;
import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.entity.Email;
import com.tomash.androidcontacts.contactgetter.entity.IMAddress;
import com.tomash.androidcontacts.contactgetter.entity.PhoneNumber;
import com.tomash.androidcontacts.contactgetter.entity.Relation;
import com.tomash.androidcontacts.contactgetter.entity.SpecialDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import androidx.test.InstrumentationRegistry;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.runner.AndroidJUnit4;

import static com.tomash.androidcontacts.utils.TestUtils.deleteAllContacts;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class BaseTest {

    protected Context mCtx;
    @Rule
    public GrantPermissionRule mRuntimePermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS);

    @Before
    public void setUp() throws Exception {
        mCtx = InstrumentationRegistry.getTargetContext();
        deleteAllContacts(mCtx);
    }

    protected void assertContacts(ContactData userContact, ContactData contactFromDb, int bitmapSize) throws Exception {
        Bitmap saved = getBitmapFromContactData(contactFromDb);
        Assert.assertTrue(saved.getHeight() == bitmapSize && saved.getWidth() == bitmapSize);
        Assert.assertEquals(userContact.getCompositeName(), contactFromDb.getCompositeName());
        Assert.assertEquals(userContact.getNote(), contactFromDb.getNote());
        Assert.assertEquals(userContact.getNickName(), contactFromDb.getNickName());
        Assert.assertEquals(userContact.getSipAddress(), contactFromDb.getSipAddress());
        Assert.assertEquals(userContact.getOrganization(), contactFromDb.getOrganization());
        Assert.assertEquals(userContact.getAccountType(), contactFromDb.getAccountType());
        Assert.assertEquals(userContact.getAccountName(), contactFromDb.getAccountName());
        Assert.assertEquals(userContact.isFavorite(), contactFromDb.isFavorite());
        for (Email email : userContact.getEmailList()) {
            Assert.assertTrue(contactFromDb.getEmailList().contains(email));
        }
        for (PhoneNumber phone : userContact.getPhoneList()) {
            Assert.assertTrue(contactFromDb.getPhoneList().contains(phone));
        }
        for (Address address : userContact.getAddressesList()) {
            Assert.assertTrue(contactFromDb.getAddressesList().contains(address));
        }
        for (String site : userContact.getWebsitesList()) {
            Assert.assertTrue(contactFromDb.getWebsitesList().contains(site));
        }
        for (IMAddress address : userContact.getImAddressesList()) {
            Assert.assertTrue(contactFromDb.getImAddressesList().contains(address));
        }
        for (SpecialDate date : userContact.getSpecialDatesList()) {
            Assert.assertTrue(contactFromDb.getSpecialDatesList().contains(date));
        }
        for (Relation relation : userContact.getRelationsList()) {
            Assert.assertTrue(contactFromDb.getRelationsList().contains(relation));
        }
    }

    protected Bitmap getBitmapFromContactData(ContactData contactFromDb) throws Exception {
        return MediaStore.Images.Media.getBitmap(mCtx.getContentResolver(), contactFromDb.getPhotoUri());
    }

}
