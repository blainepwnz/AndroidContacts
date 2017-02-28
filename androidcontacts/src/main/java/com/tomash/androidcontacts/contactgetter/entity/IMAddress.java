package com.tomash.androidcontacts.contactgetter.entity;

import android.content.Context;
import android.provider.ContactsContract;

import com.tomash.androidcontacts.contactgetter.interfaces.WithLabel;

/**
 * Created by Andrew on 25.02.2017.
 */

public class IMAddress extends WithLabel {

    public IMAddress(String mainData, int contactId, int labelId, String labelName, Context ctx) {
        super(mainData, contactId, labelId, labelName, ctx);
    }

    @Override
    protected String getLabelNameResId(Context ctx, int id) {
        return ctx.getString(ContactsContract.CommonDataKinds.Im.getProtocolLabelResource(id));
    }

    public IMAddress() {
    }
}
