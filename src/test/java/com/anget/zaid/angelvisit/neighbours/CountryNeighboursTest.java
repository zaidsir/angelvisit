package com.anget.zaid.angelvisit.neighbours;

import com.anget.zaid.angelvisit.ApplicationConfig;
import com.anget.zaid.angelvisit.countryInformation.Neighbours;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CountryNeighboursTest {

    @Test
    void givenValidAndExistingCountryThenNeighboursShouldLoad() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource("JO.json").getFile());
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setFileLocation(file.getParent());
        CountryNeighbours countryNeighbours = new CountryNeighbours(applicationConfig);
        Neighbours neighbours = countryNeighbours.retrieveNeighbours("JO");
        assertEquals(5, neighbours.getCountyInformation().size());
    }

    @Test
    void givenInValidAndCountryFileThenShouldFail() {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File file = new File(classLoader.getResource("INVJO.json").getFile());
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setFileLocation(file.getParent());
        CountryNeighbours countryNeighbours = new CountryNeighbours(applicationConfig);
        assertThrows(RuntimeException.class, () ->
                countryNeighbours.retrieveNeighbours("INVJO"));
    }
}