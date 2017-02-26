package com.tomash.androidcontacts.contactgetter.main;

import android.provider.ContactsContract;

/**
 * Enum represents types of sorting that can be applied to contacts query.
 */

public enum Sorting {


    BY_DISPLAY_NAME_ASC {
        @Override
        public String getSorting() {
            return ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
        }
    },
    BY_DISPLAY_NAME_DESC {
        @Override
        public String getSorting() {
            return ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " DESC";
        }
    },
    BY_ID_ASC {
        @Override
        public String getSorting() {
            return ContactsContract.CommonDataKinds.Phone._ID +" ASC";
        }
    },
    BY_ID_DESC {
        @Override
        public String getSorting() {
            return ContactsContract.CommonDataKinds.Phone._ID +" DESC";
        }
    };

    public abstract String getSorting();
}
