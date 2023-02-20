package com.example.foodapp;

public class ShopList {
    String shopId, shopname,shopmail,password,shopmobile, shoptelephone, category;

    public ShopList() {
    }

    public ShopList(String shopId, String shopname, String shopmail, String password, String shoptelephone,String shopmobile, String category) {
        this.shopId = shopId;
        this.shopname = shopname;
        this.shopmail = shopmail;
        this.password = password;
        this.shopmobile = shopmobile;
        this.shoptelephone = shoptelephone;
        this.category = category;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopmail() {
        return shopmail;
    }

    public void setShopmail(String shopmail) {
        this.shopmail = shopmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShopmobile() {
        return shopmobile;
    }

    public void setShopmobile(String shopmobile) {
        this.shopmobile = shopmobile;
    }

    public String getShoptelephone() {
        return shoptelephone;
    }

    public void setShoptelephone(String shoptelephone) {
        this.shoptelephone = shoptelephone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
