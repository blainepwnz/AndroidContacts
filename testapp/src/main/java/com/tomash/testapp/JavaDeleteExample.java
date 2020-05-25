package com.tomash.testapp;

import android.content.Context;
import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.main.contactsDeleter.ContactsDeleter;
import kotlin.Unit;

import java.util.List;

/**
 * Examples how delete contacts
 */
public class JavaDeleteExample {
    private ContactsDeleter contactsDeleter;

    /**
     * Example of creation of ContactsDeleter object
     */
    private ContactsDeleter createContactsDeleter(Context context) {
        return ContactsDeleter.Companion.invoke(context);
    }

    /**
     * Example of deleting one ContactData
     */
    private void deleteOneContact(ContactData contactData) {
        contactsDeleter.deleteContact(contactData, contactDataExceptionACResult -> {
            contactDataExceptionACResult.onResult(deletedContactData -> {
                // do something with successfully deleted contact
                return Unit.INSTANCE;
            });
            contactDataExceptionACResult.onCompleted(() -> {
                // do something when successfully deleted contact
                return Unit.INSTANCE;
            });
            contactDataExceptionACResult.doFinally(() -> {
                // do something in case of success or error
                return Unit.INSTANCE;
            });
            contactDataExceptionACResult.onFailure(error -> {
                // do something in case of error
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        });
    }

    /**
     * Example of deleting list of ContactData
     */
    private void deleteMultipleContacts(List<ContactData> contactData) {
        contactsDeleter.deleteContacts(contactData, contactDataExceptionACResult -> {
            contactDataExceptionACResult.onResult(contactDataList -> {
                // do something with successfully deleted contacts
                return Unit.INSTANCE;
            });
            return Unit.INSTANCE;
        });
    }

}
