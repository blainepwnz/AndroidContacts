package com.tomash.androidcontacts.contactgetter.entity;

import android.content.Context;
import android.provider.ContactsContract;

import com.tomash.androidcontacts.contactgetter.interfaces.WithLabel;

/**
 * Created by andrew on 2/24/17.
 */

public class SpecialDate extends WithLabel {
    public static final int TYPE_ANNIVERSARY = 1;
    public static final int TYPE_OTHER = 2;
    public static final int TYPE_BIRTHDAY = 3;

    public SpecialDate(String mainData, String labelName) {
        super(mainData, labelName);
    }

    public SpecialDate(Context ctx, String mainData, int labelId) {
        super(ctx, mainData, labelId);
    }

    public SpecialDate(Context ctx, String mainData) {
        super(ctx, mainData);
    }

    @Override
    protected String getLabelNameResId(Context ctx, int id) {
        return ctx.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(id));
    }

    @Override
    protected int getDefaultLabelId() {
        return TYPE_ANNIVERSARY;
    }

    @Override
    protected boolean isValidLabel(int id) {
        return id >= 1 && id <= 3;
    }

    @Override
    public int getCustomLabelId() {
        return 0;
    }
}
