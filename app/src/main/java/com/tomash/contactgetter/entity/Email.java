package com.tomash.contactgetter.entity;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.SparseArray;

import com.tomash.contactgetter.interfaces.WithLabel;

/**
 * Created by andrew on 2/24/17.
 */

public class Email extends WithLabel {

    public Email(String mainData, int contactId, int labelId, String labelName, Context ctx) {
        super(mainData, contactId, labelId, labelName, ctx);
    }

    @Override
    public int getLabelNameResId(int id) {
        return ContactsContract.CommonDataKinds.Email.getTypeLabelResource(id);
    }
}
