package com.hscompany.appchool;

public class SettingsUserInfoItem {

    String title;
    String subTitle;

    public SettingsUserInfoItem(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    public SettingsUserInfoItem(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}
