package com.tomash.androidcontacts.contactgetter.interfaces;

import com.tomash.androidcontacts.contactgetter.entity.ContactData;

/**
 * <p>
 * Base class used for filtering data from fieds that are contained in {@link ContactData} object.
 * </p>
 *
 * @param <T> Container for filterable data , for this class always {@link ContactData} object
 * @param <V> Type of field you want to filter
 */

public abstract class FieldFilter<T, V> extends BaseFilter<ContactData, V> {
    @Override
    public boolean passedFilter(ContactData contact) {
        return getFilterCondition(getFilterData(contact), getFilterPattern());
    }
}
