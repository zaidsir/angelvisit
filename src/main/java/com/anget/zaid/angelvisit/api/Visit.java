package com.anget.zaid.angelvisit.api;


import com.anget.zaid.angelvisit.api.dto.VisitRequest;
import com.anget.zaid.angelvisit.api.dto.VisitResponse;
import com.anget.zaid.angelvisit.neighbours.CountryNeighbours;
import com.anget.zaid.angelvisit.exchangerate.ExchangeService;
import com.anget.zaid.angelvisit.usecase.VisitUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class Visit {
    private final CountryNeighbours countryNeighbours;
    private final ExchangeService exchangeService;

    @Autowired
    public Visit(CountryNeighbours countryNeighbours, ExchangeService exchangeService) {
        this.countryNeighbours = countryNeighbours;
        this.exchangeService = exchangeService;
    }

    @PostMapping(value = "/visit", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<VisitResponse> visit(@RequestBody() VisitRequest visitRequest) {
        //check auth...
        VisitResponse response = new VisitUseCase(countryNeighbours, exchangeService).process(visitRequest);
        if (!response.isValid())
            return ResponseEntity.badRequest().body(response);
        return new ResponseEntity<>(response, OK);

    }

}
