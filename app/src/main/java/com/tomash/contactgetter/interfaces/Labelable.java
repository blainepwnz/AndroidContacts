package com.tomash.contactgetter.interfaces;

import android.util.SparseArray;

import java.util.Map;

/**
 * Created by Andrew on 25.02.2017.
 */

public interface Labelable {
    /**
     * Gets label resource by id
     * @param id id of this label
     * @return string id of this label
     */
    int getLabelNameResId(int id);
}
