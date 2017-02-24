package com.tomash.contactgetter.entity;

import android.util.SparseArray;

import com.tomash.contactgetter.interfaces.WithLabel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by andrew on 2/24/17.
 */

public class Address extends WithLabel {
    private static SparseArray<String> addressMap;
    static {
        addressMap = new SparseArray<>();
        addressMap.put(1,"Home");
        addressMap.put(2,"Work");
        addressMap.put(3,"Other");
    }

    @Override
    public SparseArray<String> getLabelNameMap() {
        return addressMap;
    }

    public Address(String mainData, int contactId, int labelId, String labelName) {

        super(mainData, contactId, labelId, labelName);
    }
}
