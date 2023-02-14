package com.hscompany.appchool;

import android.util.Log;

public class ItemData {

    String appAndWeb;
    String title;
    String link;
    String appLink;
    String indexes;
    String appPackage;
    String content;
    long hour;
    long minute;
    boolean setTouchStatus = true;

    public ItemData() {
    }

    public ItemData(String appAndWeb, String title, String link, String appLink, String indexes, String appPackage, String content, long hour, long minute, boolean setTouchStatus) {
        this.appAndWeb = appAndWeb;
        this.title = title;
        this.link = link;
        this.appLink = appLink;
        this.indexes = indexes;
        this.appPackage = appPackage;
        this.content = content;
        this.hour = hour;
        this.minute = minute;
        this.setTouchStatus = setTouchStatus;
    }

    public String getAppAndWeb() {
        return appAndWeb;
    }

    public void setAppAndWeb(String appAndWeb) {
        this.appAndWeb = appAndWeb;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getIndexes() {
        return indexes;
    }

    public void setIndexes(String indexes) {
        this.indexes = indexes;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getHour() {
        return hour;
    }

    public void setHour(long hour) {
        this.hour = hour;
    }

    public long getMinute() {
        return minute;
    }

    public void setMinute(long minute) {
        this.minute = minute;
    }

    public boolean getSetTouchStatus() {
        return setTouchStatus;
    }

    public void setSetTouchStatus(boolean setTouchStatus) {
        this.setTouchStatus = setTouchStatus;
    }

    @Override
    public String toString() {
        return "ItemData{" +
                "appAndWeb='" + appAndWeb + '\'' +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", appLink='" + appLink + '\'' +
                ", indexes='" + indexes + '\'' +
                ", appPackage='" + appPackage + '\'' +
                ", content='" + content + '\'' +
                ", hour=" + hour +
                ", minute=" + minute +
                ", setTouchStatus=" + setTouchStatus +
                '}';
    }
}
