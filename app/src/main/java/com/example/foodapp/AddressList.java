package com.example.foodapp;

public class AddressList {
    String doorno,streetname, cityname, statename, pincde;

    public AddressList() {
    }

    public AddressList(String doorno, String streetname, String cityname, String statename, String pincde) {
        this.doorno = doorno;
        this.streetname = streetname;
        this.cityname = cityname;
        this.statename = statename;
        this.pincde = pincde;
    }

    public String getDoorno() {
        return doorno;
    }

    public void setDoorno(String doorno) {
        this.doorno = doorno;
    }

    public String getStreetname() {
        return streetname;
    }

    public void setStreetname(String streetname) {
        this.streetname = streetname;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getPincde() {
        return pincde;
    }

    public void setPincde(String pincde) {
        this.pincde = pincde;
    }
}
