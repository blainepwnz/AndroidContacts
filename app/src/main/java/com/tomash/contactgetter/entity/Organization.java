package com.tomash.contactgetter.entity;

/**
 * Created by Andrew on 26.02.2017.
 */

public class Organization {
    private String name="";
    private String title="";

    public String getName() {
        return name;
    }

    public Organization setName(String name) {
        if(name==null)return this;
        this.name = name;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Organization setTitle(String title) {
        if(title==null)return this;
        this.title = title;
        return this;
    }
}
