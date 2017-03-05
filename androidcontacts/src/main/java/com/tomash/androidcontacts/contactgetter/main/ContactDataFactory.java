package com.tomash.androidcontacts.contactgetter.main;

import com.tomash.androidcontacts.contactgetter.entity.ContactData;

/**
 * Created by Andrew on 05.03.2017.
 */

public class ContactDataFactory {
    /**
     * Creates empty contact data object
     * @return
     */
    public static ContactData createEmpty(){
        return new ContactData() {};
    }
}
