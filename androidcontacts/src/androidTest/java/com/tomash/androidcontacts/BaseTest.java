package com.tomash.androidcontacts;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.tomash.androidcontacts.contactgetter.entity.Address;
import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.entity.Email;
import com.tomash.androidcontacts.contactgetter.entity.IMAddress;
import com.tomash.androidcontacts.contactgetter.entity.PhoneNumber;
import com.tomash.androidcontacts.contactgetter.entity.Relation;
import com.tomash.androidcontacts.contactgetter.entity.SpecialDate;

import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;

import static com.tomash.androidcontacts.utils.TestUtils.deleteAllContacts;

/**
 * Created by root on 3/6/17.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class BaseTest {

    protected Context mCtx;

    @Before
    public void setUp() throws Exception {
        mCtx = InstrumentationRegistry.getTargetContext();
        deleteAllContacts(mCtx);
    }

    protected void assertContacts(ContactData contactThis, ContactData contactThat){
        Assert.assertTrue(contactThis.getCompositeName().equals(contactThat.getCompositeName()));
        Assert.assertTrue(contactThis.getNote().equals(contactThat.getNote()));
        Assert.assertTrue(contactThis.getNickName().equals(contactThat.getNickName()));
        Assert.assertTrue(contactThis.getSipAddress().equals(contactThat.getSipAddress()));
        Assert.assertTrue(contactThis.getOrganization().equals(contactThat.getOrganization()));
        for (Email email : contactThis.getEmailList()) {
            Assert.assertTrue(contactThat.getEmailList().contains(email));
        }
        for (PhoneNumber phone : contactThis.getPhoneList()) {
            Assert.assertTrue(contactThat.getPhoneList().contains(phone));
        }
        for (Address address : contactThis.getAddressesList()) {
            Assert.assertTrue(contactThat.getAddressesList().contains(address));
        }
        for (String site : contactThis.getWebsitesList()) {
            Assert.assertTrue(contactThat.getWebsitesList().contains(site));
        }
        for (IMAddress address : contactThis.getImAddressesList()) {
            Assert.assertTrue(contactThat.getImAddressesList().contains(address));
        }
        for (SpecialDate date : contactThis.getSpecialDatesList()) {
            Assert.assertTrue(contactThat.getSpecialDatesList().contains(date));
        }
        for (Relation relation : contactThis.getRelationsList()) {
            Assert.assertTrue(contactThat.getRelationsList().contains(relation));
        }
    }

}
