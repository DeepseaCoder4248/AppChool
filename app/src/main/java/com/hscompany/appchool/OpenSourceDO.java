package com.hscompany.appchool;

public class OpenSourceDO {

    public String name;
    public String description;
    public String etc;

    public OpenSourceDO() {

    }


    public OpenSourceDO(String name, String description, String etc) {
        this.name = name;
        this.description = description;
        this.etc = etc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }
}
