package com.tomash.androidcontacts.tests;

import com.tomash.androidcontacts.BaseTest;
import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.main.ContactDataFactory;
import com.tomash.androidcontacts.contactgetter.main.contactsGetter.ContactsGetterBuilder;
import com.tomash.androidcontacts.contactgetter.main.contactsSaver.ContactsSaverBuilder;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.tomash.androidcontacts.utils.TestUtils.createRandomContactData;
import static com.tomash.androidcontacts.utils.TestUtils.generateRandomString;

public class InsertTests extends BaseTest {

    @Test
    public void correctlyInsertsDataList() throws Exception {
        int listSize = 100;
        List<ContactData> dataList = getMockContactDataList(listSize);
        int[] ids = new ContactsSaverBuilder(mCtx)
            .saveContactsList(dataList);
        Assert.assertEquals(100, ids.length);
        //asserting data inside
        Set<String> namesSet = new HashSet<>();
        Set<Integer> idsSet = new HashSet<>();
        for (int i = 0; i < listSize; i++) {
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
        int bitmapSize = 10;
        ContactData contactData = createRandomContactData(mCtx, bitmapSize);
        int id = saveToDb(contactData);
        Assert.assertTrue(id > 0);
        ContactData savedContact = getFromDbById(id);
        assertContacts(contactData, savedContact, bitmapSize);
    }

    @Test
    public void photoUriIsChangedCorrectly() throws Exception {
        int firstBitmapSize = 10;
        int secondBitmapSize = 20;
        //creating and saving first contactData to db
        ContactData contactData1 = createRandomContactData(mCtx, firstBitmapSize);
        int firstId = saveToDb(contactData1);
        contactData1 = getFromDbById(firstId);
        //creating second contact data and assigning uri of bitmap from first to it
        ContactData contactData2 = createRandomContactData(mCtx, secondBitmapSize);
        contactData2.setUpdatedPhotoUri(contactData1.getPhotoUri());
        int secondId = saveToDb(contactData2);
        contactData2 = getFromDbById(secondId);
        Assert.assertEquals(getBitmapFromContactData(contactData2).getHeight(), firstBitmapSize);
    }

    private ContactData getFromDbById(int id) {
        return new ContactsGetterBuilder(mCtx)
            .allFields()
            .getById(id);
    }

    private int saveToDb(ContactData contactData) {
        return new ContactsSaverBuilder(mCtx)
            .saveContact(contactData);
    }

    private List<ContactData> getMockContactDataList(int size) {
        List<ContactData> contactDatas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ContactData data = ContactDataFactory.createEmpty();
            data.setCompositeName(generateRandomString());
            contactDatas.add(data);
        }
        return contactDatas;
    }
}
