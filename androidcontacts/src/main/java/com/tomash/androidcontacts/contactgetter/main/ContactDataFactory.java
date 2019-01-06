package com.tomash.androidcontacts.contactgetter.main;

import com.tomash.androidcontacts.contactgetter.entity.ContactData;

public class ContactDataFactory {
    /**
     * @return Creates empty contact data object
     */
    public static ContactData createEmpty() {
        return new ContactData() {
        };
    }
}
