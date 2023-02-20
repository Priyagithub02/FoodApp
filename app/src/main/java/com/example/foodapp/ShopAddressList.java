package com.example.foodapp;

public class ShopAddressList {
    String address1,address2,landmark,shopcity,shopstate,pincode,location;

    public ShopAddressList(String uid, String shop_add1, String shop_add2, String landmark, String shopcity, String shopstate, String postalcode, String location) {
    }

    public ShopAddressList(String address1, String address2, String landmark, String shopcity, String shopstate, String pincode,String location) {
        this.address1 = address1;
        this.address2 = address2;
        this.landmark = landmark;
        this.shopcity = shopcity;
        this.shopstate = shopstate;
        this.pincode = pincode;
        this.location = location;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getShopcity() {
        return shopcity;
    }

    public void setShopcity(String shopcity) {
        this.shopcity = shopcity;
    }

    public String getShopstate() {
        return shopstate;
    }

    public void setShopstate(String shopstate) {
        this.shopstate = shopstate;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
