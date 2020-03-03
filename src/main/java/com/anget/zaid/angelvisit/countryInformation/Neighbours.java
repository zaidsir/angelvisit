package com.anget.zaid.angelvisit.countryInformation;

import java.util.List;

public class Neighbours {
    public List<CountyInformation> getCountyInformation() {
        return countyInformation;
    }

    public void setCountyInformation(List<CountyInformation> countyInformation) {
        this.countyInformation = countyInformation;
    }

    List<CountyInformation> countyInformation;
}
