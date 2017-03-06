package com.tomash.androidcontacts.contactgetter.entity;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


public abstract class ContactData {
    private int contactId;
    private String lookupKey;
    private List<Email> emailList = new ArrayList<>();
    private List<PhoneNumber> phoneList = new ArrayList<>();
    private List<Address> addressesList = new ArrayList<>();
    private List<String> websitesList = new ArrayList<>();
    private List<IMAddress> imAddressesList = new ArrayList<>();
    private List<Relation> relationsList = new ArrayList<>();
    private List<SpecialDate> specialDatesList = new ArrayList<>();
    private List<Group> groupList = new ArrayList<>();
    private String note = "";
    private String nickName = "";
    private String sipAddress = "";
    private Uri photoUri = Uri.EMPTY;
    private Organization organization = new Organization();
    private NameData nameData = new NameData();
    private String compositeName;
    private long lastModificationDate;


    public int getContactId() {
        return contactId;
    }

    public ContactData setContactId(int contactId) {
        this.contactId = contactId;
        return this;
    }

    public List<Email> getEmailList() {
        return emailList;
    }

    public ContactData setEmailList(List<Email> emailList) {
        if (emailList == null) return this;
        this.emailList = emailList;
        return this;
    }

    public List<PhoneNumber> getPhoneList() {
        return phoneList;
    }

    public ContactData setPhoneList(List<PhoneNumber> phoneList) {
        if (phoneList == null) return this;
        this.phoneList = phoneList;
        return this;
    }

    /**
     * Returns {@link Uri#EMPTY} in case of no photo , otherwise returns Uri with photo for current contact
     */
    public Uri getPhotoUri() {
        return photoUri;
    }

    public ContactData setPhotoUri(Uri photoUri) {
        if (photoUri == null) return this;
        this.photoUri = photoUri;
        return this;
    }

    public List<Address> getAddressesList() {
        return addressesList;
    }

    public ContactData setAddressesList(List<Address> addressesList) {
        if (addressesList == null) return this;
        this.addressesList = addressesList;
        return this;
    }

    public String getCompositeName() {
        return compositeName;
    }

    public ContactData setCompositeName(String compositeName) {
        this.compositeName = compositeName;
        return this;
    }

    public List<String> getWebsitesList() {
        return websitesList;
    }

    public ContactData setWebsitesList(List<String> websitesList) {
        if (websitesList == null) return this;
        this.websitesList = websitesList;
        return this;
    }

    public String getNote() {
        return note;
    }

    public ContactData setNote(String note) {
        if (note == null) return this;
        this.note = note;
        return this;
    }

    /**
     * <p>
     *     Gets last modification timestamp in Unix time
     * </p>
     * <p>
     *     AVAILABLE FROM 18 API
     * </p>
     *
     */
    public long getLastModificationDate() {
        return lastModificationDate;
    }

    public ContactData setLastModificationDate(long lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
        return this;
    }

    public List<IMAddress> getImAddressesList() {
        return imAddressesList;
    }

    public ContactData setImAddressesList(List<IMAddress> imAddressesList) {
        if (imAddressesList == null) return this;
        this.imAddressesList = imAddressesList;
        return this;
    }

    public List<Relation> getRelationsList() {
        return relationsList;
    }

    public ContactData setRelationsList(List<Relation> relationsList) {
        if (relationsList == null) return this;
        this.relationsList = relationsList;
        return this;
    }

    public List<SpecialDate> getSpecialDatesList() {
        return specialDatesList;
    }

    public ContactData setSpecialDatesList(List<SpecialDate> specialDatesList) {
        if (specialDatesList == null) return this;
        this.specialDatesList = specialDatesList;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public ContactData setNickName(String nickName) {
        if (nickName == null) return this;
        this.nickName = nickName;
        return this;
    }

    public String getSipAddress() {
        return sipAddress;
    }

    public ContactData setSipAddress(String sipAddress) {
        if (sipAddress == null) return this;
        this.sipAddress = sipAddress;
        return this;
    }

    public Organization getOrganization() {
        return organization;
    }

    public ContactData setOrganization(Organization organization) {
        if (organization == null) return this;
        this.organization = organization;
        return this;
    }

    public NameData getNameData() {
        return nameData;
    }

    public ContactData setNameData(NameData nameData) {
        if (nameData == null) return this;
        this.nameData = nameData;
        return this;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public ContactData setGroupList(List<Group> groupList) {
        if(groupList==null) return this;
        this.groupList = groupList;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactData contact = (ContactData) o;

        if (contactId != contact.contactId) return false;
        if (!emailList.equals(contact.emailList)) return false;
        if (!phoneList.equals(contact.phoneList)) return false;
        if (!addressesList.equals(contact.addressesList)) return false;
        if (!websitesList.equals(contact.websitesList)) return false;
        if (!imAddressesList.equals(contact.imAddressesList)) return false;
        if (!relationsList.equals(contact.relationsList)) return false;
        if (!specialDatesList.equals(contact.specialDatesList)) return false;
        if (!groupList.equals(contact.groupList)) return false;
        if (!note.equals(contact.note)) return false;
        if (!nickName.equals(contact.nickName)) return false;
        if (!sipAddress.equals(contact.sipAddress)) return false;
        if (!photoUri.equals(contact.photoUri)) return false;
        if (!organization.equals(contact.organization)) return false;
        if (!nameData.equals(contact.nameData)) return false;
        return compositeName.equals(contact.compositeName);

    }

    public String getLookupKey() {
        return lookupKey;
    }

    public ContactData setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
        return this;
    }

    @Override
    public int hashCode() {
        return contactId;
    }
}