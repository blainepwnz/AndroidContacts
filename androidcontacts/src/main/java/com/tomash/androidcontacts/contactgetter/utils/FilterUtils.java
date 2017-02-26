package com.tomash.androidcontacts.contactgetter.utils;

import com.tomash.androidcontacts.contactgetter.entity.Address;
import com.tomash.androidcontacts.contactgetter.entity.Contact;
import com.tomash.androidcontacts.contactgetter.entity.Email;
import com.tomash.androidcontacts.contactgetter.entity.PhoneNumber;
import com.tomash.androidcontacts.contactgetter.interfaces.BaseFilter;
import com.tomash.androidcontacts.contactgetter.interfaces.FieldFilter;
import com.tomash.androidcontacts.contactgetter.interfaces.ListFilter;

import java.util.List;

public class FilterUtils {
    public static BaseFilter<PhoneNumber, String> withPhoneLikeFilter(final String number) {
        return new ListFilter<PhoneNumber, String>() {
            @Override
            protected String getFilterPattern() {
                return number;
            }

            @Override
            protected String getFilterData(PhoneNumber data) {
                return data.getMainData();
            }

            @Override
            protected List<PhoneNumber> getFilterContainer(Contact contact) {
                return contact.getPhoneList();
            }

            @Override
            protected boolean getFilterCondition(String data, String pattern) {
                return data.toLowerCase().contains(pattern.toLowerCase());
            }
        };
    }

    public static BaseFilter<PhoneNumber, String> withPhoneFilter(final String number) {
        return new ListFilter<PhoneNumber, String>() {
            @Override
            protected String getFilterPattern() {
                return number;
            }

            @Override
            protected String getFilterData(PhoneNumber data) {
                return data.getMainData();
            }

            @Override
            protected List<PhoneNumber> getFilterContainer(Contact contact) {
                return contact.getPhoneList();
            }

            @Override
            protected boolean getFilterCondition(String data, String pattern) {
                return data.equalsIgnoreCase(pattern);
            }
        };
    }

    public static BaseFilter<Email, String> withEmailFilter(final String email) {
        return new ListFilter<Email, String>() {
            @Override
            protected String getFilterPattern() {
                return email;
            }

            @Override
            protected String getFilterData(Email data) {
                return data.getMainData();
            }

            @Override
            protected List<Email> getFilterContainer(Contact contact) {
                return contact.getEmailList();
            }

            @Override
            protected boolean getFilterCondition(String data, String pattern) {
                return data.equalsIgnoreCase(pattern);
            }
        };
    }

    public static BaseFilter<Email, String> withEmailLikeFilter(final String email) {
        return new ListFilter<Email, String>() {
            @Override
            protected String getFilterPattern() {
                return email;
            }

            @Override
            protected String getFilterData(Email data) {
                return data.getMainData();
            }

            @Override
            protected List<Email> getFilterContainer(Contact contact) {
                return contact.getEmailList();
            }

            @Override
            protected boolean getFilterCondition(String data, String pattern) {
                return data.toLowerCase().contains(pattern.toLowerCase());
            }
        };
    }

    public static BaseFilter<Address, String> withAddressLikeFilter(final String address) {
        return new ListFilter<Address, String>() {
            @Override
            protected String getFilterPattern() {
                return address;
            }

            @Override
            protected String getFilterData(Address data) {
                return data.getMainData();
            }

            @Override
            protected List<Address> getFilterContainer(Contact contact) {
                return contact.getAddressesList();
            }

            @Override
            protected boolean getFilterCondition(String data, String pattern) {
                return data.toLowerCase().contains(pattern.toLowerCase());
            }
        };
    }

    public static BaseFilter<Address, String> withAddressFilter(final String address) {
        return new ListFilter<Address, String>() {
            @Override
            protected String getFilterPattern() {
                return address;
            }

            @Override
            protected String getFilterData(Address data) {
                return data.getMainData();
            }

            @Override
            protected List<Address> getFilterContainer(Contact contact) {
                return contact.getAddressesList();
            }

            @Override
            protected boolean getFilterCondition(String data, String pattern) {
                return data.equalsIgnoreCase(pattern);
            }
        };
    }

    public static BaseFilter<Contact, String> withNoteLike(final String noteLike) {
        return new FieldFilter<Contact, String>() {
            @Override
            protected String getFilterPattern() {
                return noteLike;
            }

            @Override
            protected String getFilterData(Contact data) {
                return data.getNote();
            }

            @Override
            protected boolean getFilterCondition(String data, String pattern) {
                return data.toLowerCase().contains(pattern.toLowerCase());
            }
        };
    }

    public static BaseFilter<Contact, String> withNote(final String note) {
        return new FieldFilter<Contact, String>() {
            @Override
            protected String getFilterPattern() {
                return note;
            }

            @Override
            protected String getFilterData(Contact data) {
                return data.getNote();
            }

            @Override
            protected boolean getFilterCondition(String data, String pattern) {
                return data.equalsIgnoreCase(pattern);
            }
        };
    }


}
