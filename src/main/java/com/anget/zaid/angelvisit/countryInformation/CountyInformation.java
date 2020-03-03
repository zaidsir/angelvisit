package com.anget.zaid.angelvisit.countryInformation;

import com.fasterxml.jackson.annotation.JsonAlias;

public class CountyInformation {
    @JsonAlias("country_code")
    private String countryCode;
    @JsonAlias("country_name")
    private String countryName;

    public CountyInformation() {
    }

    public CountyInformation(String countryCode, String countryName) {
        this.countryCode = countryCode;
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String value) {
        this.countryCode = value;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String value) {
        this.countryName = value;
    }
}
