package com.anget.zaid.angelvisit.api.dto;

import java.math.BigDecimal;
import java.util.List;

public class VisitResponse {
    List<VisitedCountry> visitedCountryList;
    private Boolean isValid;
    private int numberOfTrips;
    private BigDecimal remainingAmount;
    private String errorDescription;

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public List<VisitedCountry> getVisitedCountryList() {
        return visitedCountryList;
    }

    public void setVisitedCountryList(List<VisitedCountry> visitedCountryList) {
        this.visitedCountryList = visitedCountryList;
    }

    public Boolean isValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public int getNumberOfTrips() {
        return numberOfTrips;
    }

    public void setNumberOfTrips(int numberOfTrips) {
        this.numberOfTrips = numberOfTrips;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }
}
