package com.tomash.androidcontacts.contactgetter.entity;

import android.content.Context;
import android.provider.ContactsContract;

import com.tomash.androidcontacts.contactgetter.interfaces.WithLabel;

/**
 * Created by Andrew on 25.02.2017.
 */

public class IMAddress extends WithLabel {
    public static final int PROTOCOL_AIM = 0;
    public static final int PROTOCOL_MSN = 1;
    public static final int PROTOCOL_YAHOO = 2;
    public static final int PROTOCOL_SKYPE = 3;
    public static final int PROTOCOL_QQ = 4;
    public static final int PROTOCOL_GOOGLE_TALK = 5;
    public static final int PROTOCOL_ICQ = 6;
    public static final int PROTOCOL_JABBER = 7;
    public static final int PROTOCOL_NETMEETING = 8;

    public IMAddress(String mainData, String labelName) {
        super(mainData, labelName);
    }

    public IMAddress(Context ctx, String mainData, int labelId) {
        super(ctx, mainData, labelId);
    }

    public IMAddress(Context ctx, String mainData) {
        super(ctx, mainData);
    }

    @Override
    protected String getLabelNameResId(Context ctx, int id) {
        return ctx.getString(ContactsContract.CommonDataKinds.Im.getProtocolLabelResource(id));
    }

    @Override
    protected int getDefaultLabelId() {
        return PROTOCOL_AIM;
    }

    @Override
    protected boolean isValidLabel(int id) {
        return id >= 0 && id <= 8;
    }

    @Override
    public int getCustomLabelId() {
        return -1;
    }
}
