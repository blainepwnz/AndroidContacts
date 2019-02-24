package com.tomash.androidcontacts.contactgetter.utils;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static <T> List<T> getValuesFromSparseArray(SparseArray<T> sparseArray) {
        List<T> result = new ArrayList<>();
        for (int i = 0; i < sparseArray.size(); i++) {
            result.add(sparseArray.valueAt(i));
        }
        return result;
    }
}
