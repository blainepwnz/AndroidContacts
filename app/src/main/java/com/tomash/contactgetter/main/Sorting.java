package com.tomash.contactgetter.main;

import android.provider.ContactsContract;

/**
 * Created by andrew on 2/25/17.
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
