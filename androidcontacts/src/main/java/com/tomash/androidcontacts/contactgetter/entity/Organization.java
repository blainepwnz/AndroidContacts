package com.tomash.androidcontacts.contactgetter.entity;

/**
 * Created by Andrew on 26.02.2017.
 */

public class Organization {
    private String name = "";
    private String title = "";

    public String getName() {
        return name;
    }

    public Organization setName(String name) {
        if (name == null) return this;
        this.name = name;
        return this;
    }

    public Organization(String name, String title) {
        setName(name);
        setTitle(title);
    }

    public Organization() {
    }

    public String getTitle() {
        return title;
    }

    public Organization setTitle(String title) {
        if (title == null) return this;
        this.title = title;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        if (!name.equals(that.name)) return false;
        return title.equals(that.title);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + title.hashCode();
        return result;
    }
}
