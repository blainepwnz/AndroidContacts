package com.tomash.androidcontacts.contactgetter.interfaces;

import android.content.Context;

/**
 * Created by Andrew on 24.02.2017.
 */

public abstract class WithLabel implements Labelable {
    private String mainData;
    private int contactId;
    private int labelId;
    private String labelName;

    public WithLabel(String mainData, int contactId, int labelId, String labelName, Context ctx) {
        this.mainData = mainData;
        this.contactId = contactId;
        this.labelId = labelId;
        if (labelName == null)
            this.labelName = ctx.getString(getLabelNameResId(labelId));
        else
            this.labelName = labelName;
    }


    public String getMainData() {
        return mainData;
    }

    public int getContactId() {
        return contactId;
    }

    public int getLabelId() {
        return labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WithLabel withLabel = (WithLabel) o;

        if (contactId != withLabel.contactId) return false;
        if (labelId != withLabel.labelId) return false;
        if (!mainData.equals(withLabel.mainData)) return false;
        return labelName.equals(withLabel.labelName);

    }

    @Override
    public int hashCode() {
        return mainData.hashCode();
    }
}
