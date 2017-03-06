package com.tomash.androidcontacts.tests;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.tomash.androidcontacts.contactgetter.entity.Address;
import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.entity.Email;
import com.tomash.androidcontacts.contactgetter.entity.IMAddress;
import com.tomash.androidcontacts.contactgetter.entity.Organization;
import com.tomash.androidcontacts.contactgetter.entity.PhoneNumber;
import com.tomash.androidcontacts.contactgetter.entity.Relation;
import com.tomash.androidcontacts.contactgetter.entity.SpecialDate;
import com.tomash.androidcontacts.contactgetter.main.ContactDataFactory;
import com.tomash.androidcontacts.contactgetter.main.contactsGetter.ContactsGetterBuilder;
import com.tomash.androidcontacts.contactgetter.main.contactsSaver.ContactsSaverBuilder;
import com.tomash.androidcontacts.utils.TestUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.tomash.androidcontacts.utils.TestUtils.deleteAllContacts;
import static com.tomash.androidcontacts.utils.TestUtils.generateRandomWithLabel;
import static com.tomash.androidcontacts.utils.TestUtils.generateRandomString;
import static com.tomash.androidcontacts.utils.TestUtils.generateRandomWithLabel;
import static com.tomash.androidcontacts.utils.TestUtils.getRandomContactData;

/**
 * Created by Andrew on 06.03.2017.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class InsertTest {

    private Context mCtx;

    @Before
    public void setUp() throws Exception {
        mCtx = InstrumentationRegistry.getTargetContext();
        deleteAllContacts(mCtx);
    }

    @Test
    public void correctlyInsertsDataList() throws Exception {
        List<ContactData> dataList = getMockContactDataList();
        int[] ids = new ContactsSaverBuilder(mCtx)
            .saveContactsList(dataList);
        Assert.assertEquals(100, ids.length);
        //asserting data inside
        Set<String> namesSet = new HashSet<>();
        Set<Integer> idsSet = new HashSet<>();
        for (int i = 0; i < ids.length; i++) {
            idsSet.add(ids[i]);
            namesSet.add(dataList.get(i).getCompositeName());
        }
        List<ContactData> fromDb = new ContactsGetterBuilder(mCtx)
            .allFields()
            .buildList();
        for (ContactData contactData : fromDb) {
            Assert.assertTrue(namesSet.contains(contactData.getCompositeName()));
            Assert.assertTrue(idsSet.contains(contactData.getContactId()));
        }
    }

    @Test
    public void correctlyInsertsOneData() throws Exception {
        ContactData contactData =getRandomContactData(mCtx);
        int id = new ContactsSaverBuilder(mCtx)
            .saveContact(contactData);
        Assert.assertTrue(id > 0);
        ContactData savedContact = new ContactsGetterBuilder(mCtx)
            .allFields()
            .getById(id);
        assertContacts(contactData,savedContact);
    }

    private void assertContacts(ContactData contactThis,ContactData contactThat){
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


    private List<ContactData> getMockContactDataList() {
        List<ContactData> contactDatas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ContactData data = ContactDataFactory.createEmpty();
            data.setCompositeName(generateRandomString());
            contactDatas.add(data);
        }
        return contactDatas;
    }
}
