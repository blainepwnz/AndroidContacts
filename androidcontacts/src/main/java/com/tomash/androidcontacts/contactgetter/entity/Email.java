package com.tomash.androidcontacts.contactgetter.entity;

import android.content.Context;
import android.provider.ContactsContract;

import com.tomash.androidcontacts.contactgetter.interfaces.WithLabel;

/**
 * Created by andrew on 2/24/17.
 */

public class Email extends WithLabel {
    public static final int TYPE_HOME = 1;
    public static final int TYPE_WORK = 2;
    public static final int TYPE_OTHER = 3;
    public static final int TYPE_MOBILE = 4;

    public Email(String mainData, String labelName) {
        super(mainData, labelName);
    }

    public Email(Context ctx, String mainData, int labelId) {
        super(ctx, mainData, labelId);
    }

    public Email(Context ctx, String mainData) {
        super(ctx, mainData);
    }

    @Override
    protected String getLabelNameResId(Context ctx,int id) {
        return ctx.getString(ContactsContract.CommonDataKinds.Email.getTypeLabelResource(id));
    }

    @Override
    protected int getDefaultLabelId() {
        return TYPE_HOME;
    }

    @Override
    protected boolean isValidLabel(int id) {
        return id>=1 && id<=4;
    }

    @Override
    public int getCustomLabelId() {
        return 0;
    }
}
