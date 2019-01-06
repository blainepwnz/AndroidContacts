package com.tomash.androidcontacts.contactgetter.main.contactsSaver;

import android.content.Context;

import com.tomash.androidcontacts.contactgetter.entity.ContactData;

import java.util.Collections;
import java.util.List;

public class ContactsSaverBuilder {
    private Context mCtx;

    public ContactsSaverBuilder(Context mCtx) {
        this.mCtx = mCtx;
    }

    /**
     * Saves to phone database list of contacts
     *
     * @param contactDataList list of contacts you want to save
     * @return array with newly created contacts ids
     */
    public int[] saveContactsList(List<ContactData> contactDataList) {
        return new ContactsSaver(mCtx.getContentResolver())
            .insertContacts(contactDataList);
    }

    /**
     * Saves contacts with all data to phone database
     *
     * @param contactData contact you want to save
     * @return newly created contacts id
     */
    public int saveContact(ContactData contactData) {
        List<ContactData> contactDatas = Collections.singletonList(contactData);
        int[] ids = new ContactsSaver(mCtx.getContentResolver())
            .insertContacts(contactDatas);
        return ids[0];
    }
}
