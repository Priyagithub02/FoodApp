package com.example.foodapp;

public class UserList {
    String userId,user,phonenum,mail,pass;

    public UserList() {
    }

    public UserList(String userId, String user, String phonenum, String mail, String pass) {
        this.userId = userId;
        this.user = user;
        this.phonenum = phonenum;
        this.mail = mail;
        this.pass = pass;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
