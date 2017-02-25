package com.tomash.contactgetter.interfaces;

/**
 * Created by Andrew on 24.02.2017.
 */

public abstract class WithLabel implements Labelable {
    private String mainData;
    private int contactId;
    private int labelId;
    private String labelName;

    public WithLabel(String mainData, int contactId, int labelId, String labelName) {
        this.mainData = mainData;
        this.contactId = contactId;
        this.labelId = labelId;
        if (labelName == null)
            this.labelName = getLabelNameMap().get(labelId);
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
}
