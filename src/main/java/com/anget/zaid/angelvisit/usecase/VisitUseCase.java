package com.anget.zaid.angelvisit.usecase;

import com.anget.zaid.angelvisit.api.dto.VisitRequest;
import com.anget.zaid.angelvisit.api.dto.VisitResponse;
import com.anget.zaid.angelvisit.api.dto.VisitedCountry;
import com.anget.zaid.angelvisit.neighbours.CountryNeighbours;
import com.anget.zaid.angelvisit.countryInformation.Neighbours;
import com.anget.zaid.angelvisit.exchangerate.ExchangeService;
import com.anget.zaid.angelvisit.exchangerate.dto.ExchangeRate;
import com.anget.zaid.angelvisit.usecase.exceptions.InvalidRequestException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.anget.zaid.angelvisit.helper.CountryHelper.currencyOf;
import static java.math.BigDecimal.valueOf;
import static java.util.Objects.isNull;

public class VisitUseCase {
    private final CountryNeighbours countryNeighbours;
    private final ExchangeService exchangeService;

    public VisitUseCase(CountryNeighbours countryNeighbours, ExchangeService exchangeService) {
        this.countryNeighbours = countryNeighbours;
        this.exchangeService = exchangeService;
    }

    public VisitResponse process(VisitRequest visitRequest) {
        try {
            validateRequest(visitRequest);
            Neighbours neighbours = countryNeighbours.retrieveNeighbours(visitRequest.getStartingCountry());
            validateNeighbours(neighbours, visitRequest.getStartingCountry());
            return calculateTrip(visitRequest, neighbours);
        } catch (InvalidRequestException ex) {
            VisitResponse result = new VisitResponse();
            result.setIsValid(false);
            result.setErrorDescription(ex.getMessage());
            return result;
        }
    }

    private void validateNeighbours(Neighbours neighbours, String startingCountry) {
        if (isNull(neighbours.getCountyInformation()) || neighbours.getCountyInformation().isEmpty())
            throw new InvalidRequestException("Country " + startingCountry + " has no neighbours!");
    }

    private VisitResponse calculateTrip(VisitRequest visitRequest, Neighbours neighbours) {
        VisitResponse result = new VisitResponse();

        if (visitRequest.getTotalBudget().compareTo(valueOf(visitRequest.getBudgetPerCountry().doubleValue() * neighbours.getCountyInformation().size())) < 0) {
            throw new InvalidRequestException("insufficient trip fund");
        }


        result.setVisitedCountryList(extractVisitedCountries(neighbours));
        ExchangeRate exchangeRate = exchangeService.getQuotation();
        // for (int i = 0; i < numberOfTrips; i++) {
        executeTrip(exchangeRate, result, visitRequest);
        //}
        result.setIsValid(true);
        return result;
    }


    private List<VisitedCountry> extractVisitedCountries(Neighbours neighbours) {
        List<VisitedCountry> result = new ArrayList<>();
        neighbours.getCountyInformation().forEach(n -> {
            VisitedCountry visitedCountry = new VisitedCountry();
            visitedCountry.setCountry(n.getCountryCode());
            visitedCountry.setCurrency(currencyOf(n.getCountryCode()));
            visitedCountry.setAmountSpent(BigDecimal.ZERO);
            result.add(visitedCountry);
        });
        return result;
    }

    private void executeTrip(ExchangeRate exchangeRate, VisitResponse visitResponse, VisitRequest visitRequest) {
        if (visitRequest.getTotalBudget().compareTo(valueOf(visitResponse.getVisitedCountryList().size() * visitRequest.getBudgetPerCountry().doubleValue())) < 0)
            return;
        VisitedCountry visitedCountry = new VisitedCountry();
        visitedCountry.setAmountSpent(BigDecimal.ZERO);
        final double[] totalBudget = {visitRequest.getTotalBudget().doubleValue()};
        visitResponse.getVisitedCountryList().forEach(v -> {
            Double exchangeValue = exchangeRate.getRates().get(currencyOf(v.getCountry()));
            BigDecimal amountSpent;
            if (Objects.isNull(exchangeValue)) {
                amountSpent = v.getAmountSpent().add(visitRequest.getBudgetPerCountry());
                v.setAmountSpent(amountSpent);
                v.setCurrency(visitRequest.getCurrency());
            } else {
                amountSpent = v.getAmountSpent()
                        .add(visitRequest.getBudgetPerCountry().multiply(valueOf(exchangeValue)));
                v.setAmountSpent(amountSpent);
            }
            totalBudget[0] = totalBudget[0] - visitRequest.getBudgetPerCountry().doubleValue();
            visitResponse.setRemainingAmount(valueOf(totalBudget[0]));
        });
        visitRequest.setTotalBudget(valueOf(totalBudget[0]));
        visitResponse.setNumberOfTrips(visitResponse.getNumberOfTrips() + 1);
        executeTrip(exchangeRate, visitResponse, visitRequest);
    }

    private void validateRequest(VisitRequest visitRequest) {
        if (isNull(visitRequest))
            throw new InvalidRequestException("Request Cannot be null");
        if (isNull(visitRequest.getStartingCountry()) ||
                isNull(visitRequest.getTotalBudget()) ||
                isNull(visitRequest.getBudgetPerCountry()) ||
                isNull(visitRequest.getCurrency())

        ) {
            throw new InvalidRequestException("Incorrect request information");
        }

    }
}
