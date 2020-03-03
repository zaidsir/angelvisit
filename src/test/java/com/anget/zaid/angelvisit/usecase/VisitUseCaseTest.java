package com.anget.zaid.angelvisit.usecase;

import com.anget.zaid.angelvisit.api.dto.VisitRequest;
import com.anget.zaid.angelvisit.api.dto.VisitResponse;
import com.anget.zaid.angelvisit.api.dto.VisitedCountry;
import com.anget.zaid.angelvisit.neighbours.CountryNeighbours;
import com.anget.zaid.angelvisit.countryInformation.CountyInformation;
import com.anget.zaid.angelvisit.countryInformation.Neighbours;
import com.anget.zaid.angelvisit.exchangerate.ExchangeService;
import com.anget.zaid.angelvisit.exchangerate.dto.ExchangeRate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.math.BigDecimal.valueOf;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class VisitUseCaseTest {

    @Test
    public void givenValidUseCaseRequestThenShouldReturnValidResponse() {
        ExchangeService exchangeService = mock(ExchangeService.class);
        CountryNeighbours neighbours = mock(CountryNeighbours.class);
        doReturn(loadExchange()).when(exchangeService).getQuotation();
        doReturn(loadNeighbours()).when(neighbours).retrieveNeighbours(anyString());
        VisitUseCase visitUseCase = new VisitUseCase(neighbours, exchangeService);
        VisitRequest request = new VisitRequest();
        request.setTotalBudget(valueOf(1200L));
        request.setBudgetPerCountry(valueOf(100));
        request.setCurrency("EUR");
        request.setStartingCountry("JOR");
        VisitResponse response = visitUseCase.process(request);
        assertTrue(response.isValid());
        assertNull(response.getErrorDescription());
        assertEquals(5, response.getVisitedCountryList().size());
        assertEquals(2, response.getNumberOfTrips());
        VisitedCountry il = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("IL")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(761.0400).setScale(4), il.getAmountSpent());
        assertEquals("ILS", il.getCurrency());
        VisitedCountry iq = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("IQ")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(200), iq.getAmountSpent());
        assertEquals("EUR", iq.getCurrency());
        VisitedCountry ps = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("PS")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(200), ps.getAmountSpent());
        assertEquals("EUR", ps.getCurrency());
        VisitedCountry sa = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("SA")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(200), sa.getAmountSpent());
        assertEquals("EUR", sa.getCurrency());
        VisitedCountry sy = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("SY")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(200), sy.getAmountSpent());
        assertEquals("EUR", sy.getCurrency());
        assertEquals(valueOf(200).setScale(1), response.getRemainingAmount());
    }

    @Test
    public void givenValidForSerbiaUseCaseRequestThenShouldReturnValidResponse() {
        ExchangeService exchangeService = mock(ExchangeService.class);
        CountryNeighbours neighbours = mock(CountryNeighbours.class);
        doReturn(loadExchange()).when(exchangeService).getQuotation();
        doReturn(loadNeighboursSerbia()).when(neighbours).retrieveNeighbours(anyString());
        VisitUseCase visitUseCase = new VisitUseCase(neighbours, exchangeService);
        VisitRequest request = new VisitRequest();
        request.setTotalBudget(valueOf(1200L));
        request.setBudgetPerCountry(valueOf(100));
        request.setCurrency("EUR");
        request.setStartingCountry("JOR");
        VisitResponse response = visitUseCase.process(request);
        assertTrue(response.isValid());
        assertNull(response.getErrorDescription());
        assertEquals(8, response.getVisitedCountryList().size());
        assertEquals(1, response.getNumberOfTrips());
        VisitedCountry al = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("AL")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(100), al.getAmountSpent());
        assertEquals("EUR", al.getCurrency());
        VisitedCountry ba = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("BA")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(100), ba.getAmountSpent());
        assertEquals("EUR", ba.getCurrency());
        VisitedCountry bg = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("BG")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(195.5800).setScale(4), bg.getAmountSpent());
        assertEquals("BGN", bg.getCurrency());
        VisitedCountry hr = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("HR")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(746.9500).setScale(4), hr.getAmountSpent());
        assertEquals("HRK", hr.getCurrency());
        VisitedCountry hu = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("HU")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(33757.00).setScale(2), hu.getAmountSpent());
        assertEquals("HUF", hu.getCurrency());
        VisitedCountry me = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("ME")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(100), me.getAmountSpent());
        assertEquals("EUR", me.getCurrency());

        VisitedCountry mk = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("MK")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(100), mk.getAmountSpent());
        assertEquals("EUR", mk.getCurrency());
        VisitedCountry ro = response.getVisitedCountryList().stream().filter(f -> f.getCountry().equals("RO")).findFirst().orElseThrow(RuntimeException::new);
        assertEquals(valueOf(481.300).setScale(3), ro.getAmountSpent());
        assertEquals("RON", ro.getCurrency());
        assertEquals(1, response.getNumberOfTrips());
    }


    @Test
    public void givenValidUseCaseRequestWithInsufficientFundThenShouldReturnInvalidResponse() {
        ExchangeService exchangeService = mock(ExchangeService.class);
        CountryNeighbours neighbours = mock(CountryNeighbours.class);
        doReturn(loadExchange()).when(exchangeService).getQuotation();
        doReturn(loadNeighbours()).when(neighbours).retrieveNeighbours(anyString());
        VisitUseCase visitUseCase = new VisitUseCase(neighbours, exchangeService);
        VisitRequest request = new VisitRequest();
        request.setTotalBudget(valueOf(50));
        request.setBudgetPerCountry(valueOf(100));
        request.setCurrency("EUR");
        request.setStartingCountry("JOR");
        VisitResponse response = visitUseCase.process(request);
        assertFalse(response.isValid());
        assertEquals("insufficient trip fund", response.getErrorDescription());
        assertEquals(0, response.getNumberOfTrips());

    }

    @Test
    public void givenInValidUseCaseRequestWithNullValuesThenShouldReturnInvalidResponse() {
        ExchangeService exchangeService = mock(ExchangeService.class);
        CountryNeighbours neighbours = mock(CountryNeighbours.class);
        doReturn(loadExchange()).when(exchangeService).getQuotation();
        doReturn(loadNeighbours()).when(neighbours).retrieveNeighbours(anyString());
        VisitUseCase visitUseCase = new VisitUseCase(neighbours, exchangeService);
        VisitRequest request = new VisitRequest();
        VisitResponse response = visitUseCase.process(request);
        assertFalse(response.isValid());
        assertEquals("Incorrect request information", response.getErrorDescription());
        assertEquals(0, response.getNumberOfTrips());

    }

    @Test
    public void givenValidUseCaseRequestWithNoNeighboursThenShouldReturnInvalidResponse() {
        ExchangeService exchangeService = mock(ExchangeService.class);
        CountryNeighbours neighbours = mock(CountryNeighbours.class);
        doReturn(loadExchange()).when(exchangeService).getQuotation();
        doReturn(island()).when(neighbours).retrieveNeighbours(anyString());
        VisitUseCase visitUseCase = new VisitUseCase(neighbours, exchangeService);
        VisitRequest request = new VisitRequest();
        request.setTotalBudget(valueOf(50));
        request.setBudgetPerCountry(valueOf(100));
        request.setCurrency("EUR");
        request.setStartingCountry("JOR");
        VisitResponse response = visitUseCase.process(request);
        assertFalse(response.isValid());
        assertEquals("Country JOR has no neighbours!", response.getErrorDescription());
        assertEquals(0, response.getNumberOfTrips());
    }


    private Neighbours loadNeighbours() {
        Neighbours result = new Neighbours();
        List<CountyInformation> values = new ArrayList<>();
        values.add(new CountyInformation("IL", "Israel"));
        values.add(new CountyInformation("IQ", "Iraq"));
        values.add(new CountyInformation("PS", "Palestine"));
        values.add(new CountyInformation("SA", "Saudi Arabia"));
        values.add(new CountyInformation("SY", "Syria"));
        result.setCountyInformation(values);
        return result;
    }

    private Neighbours loadNeighboursSerbia() {
        Neighbours result = new Neighbours();
        List<CountyInformation> values = new ArrayList<>();
        values.add(new CountyInformation("AL", "Albania"));
        values.add(new CountyInformation("BA", "Bosnia and Herzegovina"));
        values.add(new CountyInformation("BG", "Bulgaria"));
        values.add(new CountyInformation("HR", "Croatia"));
        values.add(new CountyInformation("HU", "Hungary"));
        values.add(new CountyInformation("ME", "Montenegro"));
        values.add(new CountyInformation("MK", "North Macedonia"));
        values.add(new CountyInformation("RO", "Romania"));
        result.setCountyInformation(values);
        return result;
    }


    private Neighbours island() {
        Neighbours result = new Neighbours();
        List<CountyInformation> values = new ArrayList<>();
        result.setCountyInformation(values);
        return result;
    }

    private ExchangeRate loadExchange() {
        ExchangeRate result = new ExchangeRate();
        result.setBase("EUR");
        result.setDate("2020-02-28");
        Map<String, Double> ratesValue = new HashMap<>();
        ratesValue.put("CAD", 1.4757);
        ratesValue.put("HKD", 8.555);
        ratesValue.put("ISK", 139.3);
        ratesValue.put("PHP", 56.027);
        ratesValue.put("DKK", 7.4723);
        ratesValue.put("HUF", 337.57);
        ratesValue.put("CZK", 25.39);
        ratesValue.put("AUD", 1.6875);
        ratesValue.put("RON", 4.813);
        ratesValue.put("SEK", 10.6738);
        ratesValue.put("IDR", 15749.25);
        ratesValue.put("INR", 79.285);
        ratesValue.put("BRL", 4.9232);
        ratesValue.put("RUB", 73.6096);
        ratesValue.put("HRK", 7.4695);
        ratesValue.put("JPY", 119.36);
        ratesValue.put("THB", 34.632);
        ratesValue.put("CHF", 1.0614);
        ratesValue.put("SGD", 1.5317);
        ratesValue.put("PLN", 4.3259);
        ratesValue.put("BGN", 1.9558);
        ratesValue.put("TRY", 6.8348);
        ratesValue.put("CNY", 7.6662);
        ratesValue.put("NOK", 10.3888);
        ratesValue.put("NZD", 1.7608);
        ratesValue.put("ZAR", 17.0961);
        ratesValue.put("USD", 1.0977);
        ratesValue.put("MXN", 21.637);
        ratesValue.put("ILS", 3.8052);
        ratesValue.put("GBP", 0.85315);
        ratesValue.put("KRW", 1324.98);
        ratesValue.put("MYR", 4.6263);

        result.setRates(ratesValue);
        return result;
    }
}