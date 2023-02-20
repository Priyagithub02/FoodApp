package com.example.foodapp;

public class DeliveryPersonList {
    String deliverypersonId,name, deliveryemail, phone_num,deliverypassword,usergroup ,idproof;

    public DeliveryPersonList() {
    }

    public DeliveryPersonList(String deliverypersonId, String name, String email, String phone_num, String password,String usergroup, String idproof) {
        this.deliverypersonId = deliverypersonId;
        this.name = name;
        this.deliveryemail = email;
        this.phone_num = phone_num;
        this.deliverypassword = password;
        this.usergroup = usergroup;
        this.idproof = idproof;
    }

    public String getDeliverypersonId() {
        return deliverypersonId;
    }

    public void setDeliverypersonId(String deliverypersonId) {
        this.deliverypersonId = deliverypersonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeliveryemail() {
        return deliveryemail;
    }

    public void setDeliveryemail(String deliveryemail) {
        this.deliveryemail = deliveryemail;
    }

    public String getDeliverypassword() {
        return deliverypassword;
    }

    public void setDeliverypassword(String deliverypassword) {
        this.deliverypassword = deliverypassword;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
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

