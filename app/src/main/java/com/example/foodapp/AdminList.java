package com.example.foodapp;

public class AdminList {
    String adminId,name,email,phone,password,shopId,usergroup,idproof;

    public AdminList() {
    }

    public AdminList(String adminId, String name, String email, String phone, String password, String shopId, String usergroup, String idproof) {
        this.adminId = adminId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.shopId = shopId;
        this.usergroup = usergroup;
        this.idproof = idproof;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup;
    }

    public String getIdproof() {
        return idproof;
    }

    public void setIdproof(String idproof) {
        this.idproof = idproof;
    }
}
