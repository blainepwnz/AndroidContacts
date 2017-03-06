package com.tomash.androidcontacts.tests;

import com.tomash.androidcontacts.BaseTest;
import com.tomash.androidcontacts.contactgetter.entity.Address;
import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.entity.Email;
import com.tomash.androidcontacts.contactgetter.entity.IMAddress;
import com.tomash.androidcontacts.contactgetter.entity.PhoneNumber;
import com.tomash.androidcontacts.contactgetter.entity.Relation;
import com.tomash.androidcontacts.contactgetter.entity.SpecialDate;
import com.tomash.androidcontacts.contactgetter.main.ContactDataFactory;
import com.tomash.androidcontacts.contactgetter.main.contactsGetter.ContactsGetterBuilder;
import com.tomash.androidcontacts.contactgetter.main.contactsSaver.ContactsSaverBuilder;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tomash.androidcontacts.utils.TestUtils.generateRandomWithLabel;
import static com.tomash.androidcontacts.utils.TestUtils.generateRandomString;
import static com.tomash.androidcontacts.utils.TestUtils.generateRandomWithLabel;
import static com.tomash.androidcontacts.utils.TestUtils.createRandomContactData;

public class InsertTests extends BaseTest{

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
        ContactData contactData = createRandomContactData(mCtx);
        int id = new ContactsSaverBuilder(mCtx)
            .saveContact(contactData);
        Assert.assertTrue(id > 0);
        ContactData savedContact = new ContactsGetterBuilder(mCtx)
            .allFields()
            .getById(id);
        assertContacts(contactData,savedContact);
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
