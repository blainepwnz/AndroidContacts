package com.tomash.androidcontacts.tests;

import com.tomash.androidcontacts.BaseTest;
import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.main.contactsGetter.ContactsGetterBuilder;
import com.tomash.androidcontacts.contactgetter.main.contactsSaver.ContactsSaverBuilder;
import com.tomash.androidcontacts.utils.TestUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class GetContactsTests extends BaseTest {


    @Test
    public void correctlyGetsCorrectData() throws Exception {
        List<ContactData> savedData = generateListOfRandomContacts();
        new ContactsSaverBuilder(mCtx)
            .saveContactsList(savedData);
        for (int i = 0; i < savedData.size(); i++) {
            ContactData randomContact = savedData.get(i);
            List<ContactData> savedList = new ContactsGetterBuilder(mCtx)
                .allFields()
                .withName(randomContact.getCompositeName())
                .buildList();
            if (savedList.size() == 1)
                assertContacts(randomContact, savedList.get(0), ++i);
            else
                throw new AssertionError();
        }
    }

    private List<ContactData> generateListOfRandomContacts() throws Exception {
        List<ContactData> dataList = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            dataList.add(TestUtils.createRandomContactData(mCtx, i));
        }
        return dataList;
    }


}
