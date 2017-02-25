package com.tomash.contactgetter.entity;

import android.util.SparseArray;

import com.tomash.contactgetter.interfaces.WithLabel;

/**
 * Created by andrew on 2/24/17.
 */

public class SpecialDate extends WithLabel {
    private static SparseArray<String> dateArray;
    static {
        dateArray = new SparseArray<>();
        dateArray.put(1,"Anniversary");
        dateArray.put(2,"Other");
        dateArray.put(3,"Birthday");
    }

    public SpecialDate(String mainData, int contactId, int labelId, String labelName) {
        super(mainData, contactId, labelId, labelName);
    }

    @Override
    public SparseArray<String> getLabelNameMap() {
        return dateArray;
    }
}
