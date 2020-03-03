package com.anget.zaid.angelvisit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig {
    @Value("${file.storageLocation}")
    private String fileLocation;
    @Value("${geolocationAPIKey}")
    private String geolocationAPIKey;

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public String getGeolocationAPIKey() {
        return geolocationAPIKey;
    }

    public void setGeolocationAPIKey(String geolocationAPIKey) {
        this.geolocationAPIKey = geolocationAPIKey;
    }
}
