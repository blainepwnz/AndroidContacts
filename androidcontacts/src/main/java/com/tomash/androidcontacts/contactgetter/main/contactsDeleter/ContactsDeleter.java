package com.tomash.androidcontacts.contactgetter.main.contactsDeleter;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by Andrew on 04.03.2017.
 */

class ContactsDeleter {
    ContentResolver mResolver;
    public boolean deleteContactWithId(String contactId) {
        Uri uri = Uri.withAppendedPath(ContactsContract.RawContacts.CONTENT_URI, contactId);
        int deleted = mResolver.delete(uri, null, null);
        return deleted > 0;
    }

}
