package com.tomash.androidcontacts.contactgetter.entity;

/**
 * Created by Andrew on 26.02.2017.
 */

public class NameData {
    private String fullName = "";
    private String firstName = "";
    private String surname = "";
    private String namePrefix = "";
    private String middleName = "";
    private String nameSuffix = "";
    private String phoneticFirst = "";
    private String phoneticMiddle = "";
    private String phoneticLast = "";

    public String getFullName() {
        return fullName;
    }

    public NameData setFullName(String fullName) {
        if (fullName == null) return this;
        this.fullName = fullName;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public NameData setFirstName(String firstName) {
        if (firstName == null) return this;
        this.firstName = firstName;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public NameData setSurname(String surname) {
        if (surname == null) return this;
        this.surname = surname;
        return this;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public NameData setNamePrefix(String namePrefix) {
        if (namePrefix == null) return this;
        this.namePrefix = namePrefix;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public NameData setMiddleName(String middleName) {
        if (middleName == null) return this;
        this.middleName = middleName;
        return this;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public NameData setNameSuffix(String nameSuffix) {
        if (nameSuffix == null) return this;
        this.nameSuffix = nameSuffix;
        return this;
    }

    public String getPhoneticFirst() {
        return phoneticFirst;
    }

    public NameData setPhoneticFirst(String phoneticFirst) {
        if (phoneticFirst == null) return this;
        this.phoneticFirst = phoneticFirst;
        return this;
    }

    public String getPhoneticMiddle() {
        return phoneticMiddle;
    }

    public NameData setPhoneticMiddle(String phoneticMiddle) {
        if (phoneticMiddle == null) return this;
        this.phoneticMiddle = phoneticMiddle;
        return this;
    }

    public String getPhoneticLast() {
        return phoneticLast;
    }

    public NameData setPhoneticLast(String phoneticLast) {
        if (phoneticLast == null) return this;
        this.phoneticLast = phoneticLast;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NameData nameData = (NameData) o;

        if (!fullName.equals(nameData.fullName)) return false;
        if (!firstName.equals(nameData.firstName)) return false;
        if (!surname.equals(nameData.surname)) return false;
        if (!namePrefix.equals(nameData.namePrefix)) return false;
        if (!middleName.equals(nameData.middleName)) return false;
        if (!nameSuffix.equals(nameData.nameSuffix)) return false;
        if (!phoneticFirst.equals(nameData.phoneticFirst)) return false;
        if (!phoneticMiddle.equals(nameData.phoneticMiddle)) return false;
        return phoneticLast.equals(nameData.phoneticLast);

    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }
}
