package com.tomash.androidcontacts.contactgetter.entity;

import android.content.Context;
import android.provider.ContactsContract;

import com.tomash.androidcontacts.contactgetter.interfaces.WithLabel;

/**
 * Created by andrew on 2/24/17.
 */

public class Relation extends WithLabel {

    public static final int TYPE_ASSISTANT = 1;
    public static final int TYPE_BROTHER = 2;
    public static final int TYPE_CHILD = 3;
    public static final int TYPE_DOMESTIC_PARTNER = 4;
    public static final int TYPE_FATHER = 5;
    public static final int TYPE_FRIEND = 6;
    public static final int TYPE_MANAGER = 7;
    public static final int TYPE_MOTHER = 8;
    public static final int TYPE_PARENT = 9;
    public static final int TYPE_PARTNER = 10;
    public static final int TYPE_REFERRED_BY = 11;
    public static final int TYPE_RELATIVE = 12;
    public static final int TYPE_SISTER = 13;
    public static final int TYPE_SPOUSE = 14;


    public Relation(String mainData, String labelName) {
        super(mainData, labelName);
    }

    public Relation(Context ctx, String mainData, int labelId) {
        super(ctx, mainData, labelId);
    }

    public Relation(Context ctx, String mainData) {
        super(ctx, mainData);
    }

    @Override
    protected String getLabelNameResId(Context ctx,int id) {
        return ctx.getString(ContactsContract.CommonDataKinds.Relation.getTypeLabelResource(id));
    }

    @Override
    protected int getDefaultLabelId() {
        return TYPE_ASSISTANT;
    }

    @Override
    protected boolean isValidLabel(int id) {
        return id>=1 && id<=14;
    }

    @Override
    public int getCustomLabelId() {
        return 0;
    }
}
