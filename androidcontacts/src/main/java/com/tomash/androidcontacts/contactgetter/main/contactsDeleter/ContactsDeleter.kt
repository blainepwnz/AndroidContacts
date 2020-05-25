package com.tomash.androidcontacts.contactgetter.main.contactsDeleter

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.ContactsContract
import com.tomash.androidcontacts.contactgetter.acresult.ACResult
import com.tomash.androidcontacts.contactgetter.acresult.ACResult.Companion.completed
import com.tomash.androidcontacts.contactgetter.acresult.ACResult.Companion.failure
import com.tomash.androidcontacts.contactgetter.acresult.ACResult.Companion.result
import com.tomash.androidcontacts.contactgetter.entity.ContactData

private class ContactsDeleterImpl(context: Context) : ContactsDeleter {
    private val resolver: ContentResolver = context.contentResolver

    override fun deleteContact(contact: ContactData,
        func: ACResult<ContactData, Exception>.() -> Unit) {
        deleteContacts(listOf(contact)) {
            onCompleted { func.completed() }
            onResult { func.result(it.first()) }
            onFailure {
                if (it.isNotEmpty()) {
                    func.failure(it.values.first())
                }
            }
        }
    }

    override fun deleteContacts(contacts: List<ContactData>,
        func: ACResult<List<ContactData>, Map<ContactData, Exception>>.() -> Unit) {
        try {
            if (contacts.isEmpty()) {
                return func.completed()
            }
            val batch = ArrayList(contacts.map {
                Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, it.lookupKey)
            }.map { ContentProviderOperation.newDelete(it).build() })
            val results = resolver.applyBatch(ContactsContract.AUTHORITY, batch)
            val errors = results
                .filter { it.count == 0 }
                .mapNotNull { contacts.getOrNull(results.indexOf(it)) }
                .map { it to UnsuccessfulDeleteException() }
                .toMap()
            val successes = results
                .filter { it.count == 1 }
                .mapNotNull { contacts.getOrNull(results.indexOf(it)) }

            errors.takeIf { it.isNotEmpty() }
                ?.let { func.failure(it) }

            successes.takeIf { it.isNotEmpty() }
                ?.let { func.result(it) }

            if (errors.isEmpty()) {
                func.completed()
            }
        } catch (exception: Exception) {
            func.failure(contacts.map { it to exception }.toMap())
        }
    }
}

class UnsuccessfulDeleteException : RuntimeException()

interface ContactsDeleter {

    fun deleteContact(contact: ContactData,
        func: ACResult<ContactData, Exception>.() -> Unit = {})

    fun deleteContacts(contacts: List<ContactData>,
        func: ACResult<List<ContactData>, Map<ContactData, Exception>>.() -> Unit = {})

    companion object {
        operator fun invoke(context: Context): ContactsDeleter = ContactsDeleterImpl(context)
    }
}
