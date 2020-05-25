package com.tomash.testapp

import android.content.Context
import com.tomash.androidcontacts.contactgetter.entity.ContactData
import com.tomash.androidcontacts.contactgetter.main.contactsDeleter.ContactsDeleter

class DeleteExample(
    val deleter: ContactsDeleter
) {

    fun create(context: Context) {
        val contactsDeleter = ContactsDeleter(context)
    }

    fun deleteOneContact(contactData: ContactData) {
        //usual delete with no need of callbacks
        deleter.deleteContact(contactData)
        //full range of callbacks, implement any you need
        deleter.deleteContact(contactData) {
            onCompleted { }
            onFailure { }
            onResult { }
            doFinally { }
        }
    }

    fun deleteMultipleContacts(contactDatas: List<ContactData>) {
        //usual delete with no need of callbacks
        deleter.deleteContacts(contactDatas)
        //full range of callbacks, implement any you need
        deleter.deleteContacts(contactDatas) {
            onCompleted { }
            onFailure { }
            onResult { }
            doFinally { }
        }
    }
}
