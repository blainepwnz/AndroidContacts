package com.tomash.androidcontacts.contactgetter.interfaces;

import com.tomash.androidcontacts.contactgetter.entity.Contact;

/**
 * <p>
 * Base class used for filtering data from fieds that are contained in {@link Contact} object.
 * </p>
 *
 * @param <T> Container for filterable data , for this class always {@link Contact} object
 * @param <V> Type of field you want to filter
 */

public abstract class FieldFilter<T, V> extends BaseFilter<Contact, V> {
    @Override
    public boolean passedFilter(Contact contact) {
        return getFilterCondition(getFilterData(contact), getFilterPattern());
    }
}
