package com.example.foodapp;

public class ShopDocumentsList {
    String regis_certificate,date_of_register,noc_building,certification_standard,gstin,no_years_shoprunning;

    public ShopDocumentsList(String uid, String uri1, String dateof_reg, String s, String s1, String gstin, String no_ofyears) {
    }
    public ShopDocumentsList(String regis_certificate,String date_of_register, String noc_building, String certification_standard, String gstin, String no_years_shoprunning) {
        this.regis_certificate = regis_certificate;
        this.date_of_register = date_of_register;
        this.noc_building = noc_building;
        this.certification_standard = certification_standard;
        this.gstin = gstin;
        this.no_years_shoprunning = no_years_shoprunning;
    }


    public String getRegis_certificate() {
        return regis_certificate;
    }

    public void setRegis_certificate(String regis_certificate) {
        this.regis_certificate = regis_certificate;
    }

    public String getDate_of_register() {
        return date_of_register;
    }

    public void setDate_of_register(String date_of_register) {
        this.date_of_register = date_of_register;
    }

    public String getNoc_building() {
        return noc_building;
    }

    public void setNoc_building(String noc_building) {
        this.noc_building = noc_building;
    }

    public String getCertification_standard() {
        return certification_standard;
    }

    public void setCertification_standard(String certification_standard) {
        this.certification_standard = certification_standard;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getNo_years_shoprunning() {
        return no_years_shoprunning;
    }

    public void setNo_years_shoprunning(String no_years_shoprunning) {
        this.no_years_shoprunning = no_years_shoprunning;
    }


}
