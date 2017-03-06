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
        for (ContactData randomContact : savedData) {
            List<ContactData> savedList = new ContactsGetterBuilder(mCtx)
                .allFields()
                .withName(randomContact.getCompositeName())
                .buildList();
            if (savedList.isEmpty())
                throw new AssertionError();
            else if (savedList.size() == 1)
                assertContacts(randomContact, savedList.get(0));
        }
    }

    private List<ContactData> generateListOfRandomContacts() {
        List<ContactData> dataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            dataList.add(TestUtils.createRandomContactData(mCtx));
        }
        return dataList;
    }


}
