package com.example.foodapp;

public class ShopPaymentList {
    String accno,ifsc,branch_name,accholder_name,upiid;

    public ShopPaymentList() {
    }

    public ShopPaymentList(String accno, String ifsc, String branch_name, String accholder_name, String upiid) {
        this.accno = accno;
        this.ifsc = ifsc;
        this.branch_name = branch_name;
        this.accholder_name = accholder_name;
        this.upiid = upiid;
    }

    public String getAccno() {
        return accno;
    }

    public void setAccno(String accno) {
        this.accno = accno;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getAccholder_name() {
        return accholder_name;
    }

    public void setAccholder_name(String accholder_name) {
        this.accholder_name = accholder_name;
    }

    public String getUpiid() {
        return upiid;
    }

    public void setUpiid(String upiid) {
        this.upiid = upiid;
    }
}
