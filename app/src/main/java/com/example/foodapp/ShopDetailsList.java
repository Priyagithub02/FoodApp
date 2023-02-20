package com.example.foodapp;

public class ShopDetailsList {
    String firstcontactname,firstcontactmail,firstcontactphone,firstcontactid;

    public ShopDetailsList() {
    }

    public ShopDetailsList(String firstcontactname, String firstcontactmail, String firstcontactphone, String firstcontactid) {
        this.firstcontactname = firstcontactname;
        this.firstcontactmail = firstcontactmail;
        this.firstcontactphone = firstcontactphone;
        this.firstcontactid = firstcontactid;
    }

    public String getFirstcontactname() {
        return firstcontactname;
    }

    public void setFirstcontactname(String firstcontactname) {
        this.firstcontactname = firstcontactname;
    }

    public String getFirstcontactmail() {
        return firstcontactmail;
    }

    public void setFirstcontactmail(String firstcontactmail) {
        this.firstcontactmail = firstcontactmail;
    }

    public String getFirstcontactphone() {
        return firstcontactphone;
    }

    public void setFirstcontactphone(String firstcontactphone) {
        this.firstcontactphone = firstcontactphone;
    }

    public String getFirstcontactid() {
        return firstcontactid;
    }

    public void setFirstcontactid(String firstcontactid) {
        this.firstcontactid = firstcontactid;
    }
}
