package com.tomash.androidcontacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.entity.PhoneNumber;
import com.tomash.androidcontacts.contactgetter.main.ContactDataFactory;
import com.tomash.androidcontacts.contactgetter.main.contactsSaver.ContactsSaverBuilder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Andrew on 06.03.2017.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class ExampleTest {


    @Before
    public void setUp() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        deleteAll(appContext);
    }

    @Test
    public void correctlyInsertsDataList() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        int[] ids = new ContactsSaverBuilder(appContext)
            .saveContactsList(getMockContactDataList());
        Assert.assertEquals(100,ids.length);
    }

    @Test
    public void correctlyInsertsOneData() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        int id = new ContactsSaverBuilder(appContext)
            .saveContact(ContactDataFactory.createEmpty().setCompositeName("efasfsfs"));
        Assert.assertTrue(id>0);
    }

    private List<ContactData> getMockContactDataList(){
        List<ContactData> contactDatas = new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            ContactData data =ContactDataFactory.createEmpty();
            data.setCompositeName(String.valueOf(new Random().nextInt(42424242)));
            contactDatas.add(data);
        }
        return contactDatas;
    }

    private void deleteAll(Context context){
        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null);
        while (cur.moveToNext()) {
            try{
                String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
                cr.delete(uri, null, null);
            }
            catch(Exception e) {
            }
        }
    }
}
