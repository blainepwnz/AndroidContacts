package com.tomash.androidcontacts.contactgetter.main.contactsGetter;

import android.content.Context;
import android.provider.ContactsContract;

import com.tomash.androidcontacts.contactgetter.entity.ContactData;
import com.tomash.androidcontacts.contactgetter.interfaces.BaseFilter;
import com.tomash.androidcontacts.contactgetter.main.FieldType;
import com.tomash.androidcontacts.contactgetter.main.Sorting;
import com.tomash.androidcontacts.contactgetter.utils.FilterUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by root on 3/3/17.
 */

public class ContactsGetterBuilder {
    private Context mCtx;
    private Sorting mSortOrder = Sorting.BY_DISPLAY_NAME_ASC;
    private StringBuilder mSelectionBuilder = new StringBuilder();
    private List<String> mParamsList = new ArrayList<>(2);
    private List<BaseFilter> mFilterList = new ArrayList<>(8);
    private List<FieldType> mEnabledFields = new ArrayList<>(8);

    public ContactsGetterBuilder(Context ctx) {
        mCtx = ctx;
    }

    /**
     * <p>
     * Sets sort order for all contacts
     * </p>
     * <p>
     * Sort types could be found here {@link Sorting}
     * </p>
     * <p>
     * By default is ascending by display name
     * </p>
     *
     * @param sortOrder order to sort
     */
    public ContactsGetterBuilder setSortOrder(Sorting sortOrder) {
        this.mSortOrder = sortOrder;
        return this;
    }

    /**
     * <p>
     * Should get all contacts or contacts only with phones
     * </p>
     * <p>
     * Note : Will automatically query for phone numbers.
     * </p>
     * <p>
     * No need to explicitly add Phone numbers to field list
     * </p>
     * By default returns all contacts
     */
    public ContactsGetterBuilder onlyWithPhones() {
        if (mSelectionBuilder.length() != 0)
            mSelectionBuilder.append(" AND ");
        mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER)
            .append(" = 1");
        addField(FieldType.PHONE_NUMBERS);
        return this;
    }

    /**
     * <p>
     * Should get contacts only with photos or not
     * </p>
     * By default returns all contacts
     */
    public ContactsGetterBuilder onlyWithPhotos() {
        if (mSelectionBuilder.length() != 0)
            mSelectionBuilder.append(" AND ");
        mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
            .append(" IS NOT NULL");
        return this;
    }

    /**
     * Searches for contacts with name that contains sequence
     *
     * @param nameLike sequence to search for
     */
    public ContactsGetterBuilder withNameLike(String nameLike) {
        if (mSelectionBuilder.length() != 0)
            mSelectionBuilder.append(" AND ");
        mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            .append(" LIKE ?");
        mParamsList.add("%" + nameLike + "%");
        return this;
    }

    /**
     * Searches for contacts with this name
     *
     * @param name name to search for
     */
    public ContactsGetterBuilder withName(String name) {
        if (mSelectionBuilder.length() != 0)
            mSelectionBuilder.append(" AND ");
        mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            .append(" = ?");
        mParamsList.add(name);
        return this;
    }

    /**
     * Searches for contacts that contains this number sequence
     *
     * @param number number sequence to search for
     */
    public ContactsGetterBuilder withPhoneLike(final String number) {
        mFilterList.add(FilterUtils.withPhoneLikeFilter(number));
        return onlyWithPhones();
    }

    /**
     * Searches for contacts with this number
     *
     * @param number number to search for
     */
    public ContactsGetterBuilder withPhone(final String number) {
        mFilterList.add(FilterUtils.withPhoneFilter(number));
        return onlyWithPhones();
    }

    /**
     * Searches for contacts with this email
     * Implicitly adds Email field
     *
     * @param email email to search for
     */
    public ContactsGetterBuilder withEmail(final String email) {
        addField(FieldType.EMAILS);
        mFilterList.add(FilterUtils.withEmailFilter(email));
        return this;
    }

    /**
     * Searches for contacts that contains sequence
     * Implicitly adds Email field
     *
     * @param sequence sequence to search for
     */
    public ContactsGetterBuilder withEmailLike(final String sequence) {
        addField(FieldType.EMAILS);
        mFilterList.add(FilterUtils.withEmailLikeFilter(sequence));
        return this;
    }

    /**
     * Searches for contacts with this number
     * Implicitly adds Address field
     *
     * @param number number to search for
     */
    public ContactsGetterBuilder withAddress(final String number) {
        addField(FieldType.ADDRESS);
        mFilterList.add(FilterUtils.withAddressFilter(number));
        return this;
    }

    /**
     * Searches for addresses that contains this sequence
     * Implicitly adds Address field
     *
     * @param sequence sequence to search for
     */
    public ContactsGetterBuilder withAddressLike(final String sequence) {
        addField(FieldType.ADDRESS);
        mFilterList.add(FilterUtils.withAddressLikeFilter(sequence));
        return this;
    }


    private <T extends ContactData> List<T> applyFilters(List<T> contactList) {
        for (BaseFilter filter : mFilterList) {
            for (Iterator<T> iterator = contactList.iterator(); iterator.hasNext(); ) {
                ContactData contact = iterator.next();
                if (!filter.passedFilter(contact))
                    iterator.remove();
            }
        }
        return contactList;
    }

    /**
     * <p>
     * Applies custom filter to query on contacts list
     * </p>
     * <p>
     * Additional filters and example implementations could be found here {@link FilterUtils}
     * </p>
     *
     * @param filter filter to apply
     */
    public ContactsGetterBuilder applyCustomFilter(BaseFilter filter) {
        mFilterList.add(filter);
        return this;
    }

    /**
     * <p>
     * Enables all fields for query
     * </p>
     * <p>
     * Note : Consider to enable fields you need with {@link #addField(FieldType...)} to increase performance
     * </p>
     */
    public ContactsGetterBuilder allFields() {
        addField(FieldType.values());
        return this;
    }

    /**
     * <p>
     * Enables fields that should be queried
     * </p>
     * <p>
     * Number of fields influence on performance
     * </p>
     *
     * @param fieldType field type you want to add
     */
    public ContactsGetterBuilder addField(FieldType... fieldType) {
        mEnabledFields.addAll(Arrays.asList(fieldType));
        return this;
    }

    private ContactsGetter initGetter() {
        ContactsGetter getter;
        if (mSelectionBuilder.length() == 0)
            getter = new ContactsGetter(mCtx, mEnabledFields, mSortOrder.getSorting(), null, null);
        else
            getter = new ContactsGetter(mCtx, mEnabledFields, mSortOrder.getSorting(), generateSelectionArgs(), generateSelection());
        return getter;
    }


    /**
     * Builds list of contacts
     *
     * @param T class of object you want to get data
     */
    public <T extends ContactData> List<T> buildList(Class<? extends ContactData> T) {
        return applyFilters((List<T>) initGetter()
            .setContactDataClass(T)
            .getContacts());
    }

    /**
     * Builds list of contacts
     */
    public List<ContactData> buildList() {
        return applyFilters(initGetter().getContacts());
    }

    /**
     * Gets contact by local id
     *
     * @param id id to search for
     * @return contact with data specified by options or null if no contact with this id
     */
    public ContactData getById(int id) {
        if (mSelectionBuilder.length() != 0)
            mSelectionBuilder.append(" AND ");
        mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone._ID)
            .append(" = ?");
        mParamsList.add(String.valueOf(id));
        return firstOrNull();
    }

    /**
     * Gets contact by local id
     *
     * @param id id to search for
     * @param T  class of object you want to get data
     * @return contact with data specified by options or null if no contact with this id
     */
    public <T extends ContactData> T getById(int id, Class<T> T) {
        if (mSelectionBuilder.length() != 0)
            mSelectionBuilder.append(" AND ");
        mSelectionBuilder.append(ContactsContract.CommonDataKinds.Phone._ID)
            .append(" = ?");
        mParamsList.add(String.valueOf(id));
        return firstOrNull(T);
    }

    /**
     * Get first contact of null if no contacts with these params
     */
    public ContactData firstOrNull() {
        List<ContactData> contacts = buildList();
        if (contacts.isEmpty())
            return null;
        else
            return contacts.get(0);
    }

    /**
     * Get first contact of null if no contacts with these params
     *
     * @param T class of object you want to get data
     */
    public <T extends ContactData> T firstOrNull(Class<T> T) {
        List<T> contacts = buildList(T);
        if (contacts.isEmpty())
            return null;
        else
            return contacts.get(0);
    }

    private String generateSelection() {
        return mSelectionBuilder.toString();
    }

    private String[] generateSelectionArgs() {
        return mParamsList.toArray(new String[mParamsList.size()]);
    }
}
