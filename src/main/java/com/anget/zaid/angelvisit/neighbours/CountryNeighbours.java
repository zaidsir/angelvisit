package com.anget.zaid.angelvisit.neighbours;

import com.anget.zaid.angelvisit.ApplicationConfig;
import com.anget.zaid.angelvisit.countryInformation.CountyInformation;
import com.anget.zaid.angelvisit.countryInformation.Neighbours;
import com.anget.zaid.angelvisit.helper.CountryHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Arrays.asList;

@Component
public class CountryNeighbours {
//i had to cash the results because i only have 500 calls
    private final ApplicationConfig applicationConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${geolocationAPIKey}")
    private String geolocationAPIKey;

    public CountryNeighbours(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public Neighbours retrieveNeighbours(String countryCode) {
        File file = loadFile(countryCode);
        if (file.exists() && !file.isDirectory()) {
            return parseJsonFile(file);
        } else {
            return callNeighboursServiceAndCacheResults(countryCode);
        }
    }

    private File loadFile(String countryCode) {
        String filePath;
        filePath = getFilePath();
        return new File(filePath + "/" + countryCode + ".json");
    }

    private String getFilePath() {
        String filePath;
        if (applicationConfig.getFileLocation().equals("java.io.tmpdir")) {
            filePath = System.getProperty("java.io.tmpdir");
        } else {
            filePath = applicationConfig.getFileLocation();
        }
        return filePath;
    }

    private Neighbours callNeighboursServiceAndCacheResults(String countryCode) {
        String iso2countryCode = CountryHelper.iso3CountryCodeToIso2CountryCode(countryCode);
        String response = restTemplate.getForObject(
                "https://api.geodatasource.com/neighbouring-countries?key="
                        + geolocationAPIKey +
                        "&format=json&country_code=" + iso2countryCode, String.class
        );
        saveFile(response, countryCode);
        return createNeighbours(response);
    }

    private Neighbours createNeighbours(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CountyInformation[] countyInformatics = objectMapper.readValue(response, CountyInformation[].class);
            Neighbours neighbours = new Neighbours();
            neighbours.setCountyInformation(asList(countyInformatics));
            return neighbours;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing response");
        }
    }

    private void saveFile(String neighbours, String countryCode) {
        try {
            Path path = Paths.get(getFilePath() + "\\" + countryCode + ".json");
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                writer.write(neighbours);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while saving file");
        }
    }

    private Neighbours parseJsonFile(File countryFile) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CountyInformation[] countyInformatics = objectMapper.readValue(countryFile, CountyInformation[].class);
            Neighbours neighbours = new Neighbours();
            neighbours.setCountyInformation(asList(countyInformatics));
            return neighbours;
        } catch (IOException e) {
            throw new RuntimeException("Error reading country file");
        }
    }
}
