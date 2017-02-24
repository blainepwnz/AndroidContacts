package com.tomash.contactgetter.entity;

import android.util.Pair;

import java.util.List;

/**
 * Created by andrew on 2/24/17.
 */
public class Contact {
    private int contactId;
    private List<Email> emailList;
    private List<PhoneNumber> phoneList;
    private List<Address> addressesList;
    private List<String> websitesList;
    private String note;
    private String compositeName;
    private long lastModificationDate;


    public int getContactId() {
        return contactId;
    }

    public Contact setContactId(int contactId) {
        this.contactId = contactId;
        return this;
    }

    public List<Email> getEmailList() {
        return emailList;
    }

    public Contact setEmailList(List<Email> emailList) {
        this.emailList = emailList;
        return this;
    }

    public List<PhoneNumber> getPhoneList() {
        return phoneList;
    }

    public Contact setPhoneList(List<PhoneNumber> phoneList) {
        this.phoneList = phoneList;
        return this;
    }

    public List<Address> getAddressesList() {
        return addressesList;
    }

    public Contact setAddressesList(List<Address> addressesList) {
        this.addressesList = addressesList;
        return this;
    }

    public String getCompositeName() {
        return compositeName;
    }

    public Contact setCompositeName(String compositeName) {
        this.compositeName = compositeName;
        return this;
    }

    public List<String> getWebsitesList() {
        return websitesList;
    }

    public Contact setWebsitesList(List<String> websitesList) {
        this.websitesList = websitesList;
        return this;
    }

    public String getNote() {
        return note;
    }

    public Contact setNote(String note) {
        this.note = note;
        return this;
    }

    public long getLastModificationDate() {
        return lastModificationDate;
    }

    public Contact setLastModificationDate(long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
        return this;
    }


}