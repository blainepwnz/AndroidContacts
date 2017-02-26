package com.tomash.contactgetter.entity;

import android.util.SparseArray;

import com.tomash.contactgetter.interfaces.Labelable;
import com.tomash.contactgetter.interfaces.WithLabel;

/**
 * Created by andrew on 2/24/17.
 */

public class PhoneNumber extends WithLabel {
    private static SparseArray<String> phoneNumberMap;
    static {
        phoneNumberMap = new SparseArray<>();
        phoneNumberMap.put(1,"Home");
        phoneNumberMap.put(2,"Mobile");
        phoneNumberMap.put(3,"Work");
        phoneNumberMap.put(4,"Work Fax");
        phoneNumberMap.put(5,"Home Fax");
        phoneNumberMap.put(6,"Pager");
        phoneNumberMap.put(7,"Other");
        phoneNumberMap.put(12,"Main");
    }

    public PhoneNumber(String mainData, int contactId, int labelId, String labelName) {
        super(mainData, contactId, labelId, labelName);
    }

    @Override
    public SparseArray<String> getLabelNameMap() {
        return phoneNumberMap;
    }
}
