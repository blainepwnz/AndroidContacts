package com.tomash.androidcontacts.contactgetter.interfaces;

import android.content.Context;
import android.util.SparseArray;

import java.util.Map;

/**
 * Created by Andrew on 25.02.2017.
 */

interface Labelable {
    /**
     * Gets label resource by id
     * @param id id of this label
     * @return string id of this label
     */
    String getLabelNameResId(Context ctx,int id);

    /**
     * Gets label id using label name
     * @param name name of label
     * @return id of label
     */
    int getLabelIdByName(Context ctx,String name);
}
