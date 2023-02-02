package com.palria.learnera.network;

import com.google.gson.annotations.SerializedName;

public class CountryResponseDataModel {

    @SerializedName("countryCode")
    private  String countryCode;
    @SerializedName("country")
    private  String country;
    @SerializedName("city")
    private String city;



    public CountryResponseDataModel(String countryCode, String country, String city) {
        this.countryCode = countryCode;
        this.country = country;
        this.city = city;

    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    @Override
    public String toString() {
        return "CountryResponseDataModel{" +
                "countryCode='" + countryCode + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
