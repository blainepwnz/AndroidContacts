package com.tomash.contactgetter.entity;

import android.util.SparseArray;

import com.tomash.contactgetter.interfaces.WithLabel;

/**
 * Created by Andrew on 25.02.2017.
 */

public class IMAddress extends WithLabel {
    private static SparseArray<String> imAddressMap;
    static {
        imAddressMap = new SparseArray<>();
        imAddressMap.put(0,"AIM");
        imAddressMap.put(1,"Windows Live");
        imAddressMap.put(2,"Yahoo");
        imAddressMap.put(3,"Skype");
        imAddressMap.put(4,"QQ");
        imAddressMap.put(5,"Hangouts");
        imAddressMap.put(6,"ICQ");
        imAddressMap.put(7,"Jabber");
    }

    public IMAddress(String mainData, int contactId, int labelId, String labelName) {
        super(mainData, contactId, labelId, labelName);
    }

    @Override
    public SparseArray<String> getLabelNameMap() {
        return imAddressMap;
    }
}
