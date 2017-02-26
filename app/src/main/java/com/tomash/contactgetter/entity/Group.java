package com.tomash.contactgetter.entity;



public class Group {
    private int groupId;
    private String groupTitle;

    public int getGroupId() {
        return groupId;
    }

    public Group setGroupId(int groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public Group setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
        return this;
    }
}
