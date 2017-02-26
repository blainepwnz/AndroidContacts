package com.tomash.androidcontacts.contactgetter.entity;



public class Group {
    private int groupId;
    private String groupTitle="";

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
        if(groupTitle==null) return this;
        this.groupTitle = groupTitle;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Group group = (Group) o;

        if (groupId != group.groupId) return false;
        return groupTitle.equals(group.groupTitle);

    }

    @Override
    public int hashCode() {
        int result = groupId;
        result = 31 * result + groupTitle.hashCode();
        return result;
    }
}
