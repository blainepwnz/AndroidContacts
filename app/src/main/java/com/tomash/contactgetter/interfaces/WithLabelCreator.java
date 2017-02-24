package com.tomash.contactgetter.interfaces;

/**
 * Created by Andrew on 25.02.2017.
 */

public interface WithLabelCreator<T extends WithLabel> {
    T create(String mainData, int contactId, int labelId, String labelName);
}
