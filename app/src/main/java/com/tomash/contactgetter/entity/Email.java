package com.tomash.contactgetter.entity;

import android.util.SparseArray;

import com.tomash.contactgetter.interfaces.WithLabel;

/**
 * Created by andrew on 2/24/17.
 */

public class Email extends WithLabel {

    private static SparseArray<String> emailMap;
    static {
        emailMap = new SparseArray<>();
        emailMap.put(1,"Home");
        emailMap.put(2,"Work");
        emailMap.put(3,"Other");
    }

    public Email(String mainData, int contactId, int labelId, String labelName) {
        super(mainData, contactId, labelId, labelName);
    }

    @Override
    public SparseArray<String> getLabelNameMap() {
        return emailMap;
    }
}
