package com.tomash.contactgetter.entity;

import android.util.SparseArray;

import com.tomash.contactgetter.interfaces.WithLabel;

/**
 * Created by andrew on 2/24/17.
 */

public class Relation extends WithLabel {
    private static SparseArray<String> relationsMap;
    static {
        relationsMap = new SparseArray<>();
        relationsMap.put(1,"Assistant");
        relationsMap.put(2,"Brother");
        relationsMap.put(3,"Child");
        relationsMap.put(4,"Domestic Partner");
        relationsMap.put(5,"Father");
        relationsMap.put(6,"Friend");
        relationsMap.put(7,"Manager");
        relationsMap.put(8,"Mother");
        relationsMap.put(9,"Parent");
        relationsMap.put(10,"Partner");
        relationsMap.put(11,"Referred by");
        relationsMap.put(12,"Relative");
        relationsMap.put(13,"Sister");
        relationsMap.put(14,"Spouse");
    }

    public Relation(String mainData, int contactId, int labelId, String labelName) {
        super(mainData, contactId, labelId, labelName);
    }

    @Override
    public SparseArray<String> getLabelNameMap() {
        return relationsMap;
    }
}
