package com.anget.zaid.angelvisit.exchangerate;

import com.anget.zaid.angelvisit.exchangerate.dto.ExchangeRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExchangeRatesIo implements ExchangeService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${exchangeRate.url}")
    private String exchangeServiceUrl;

    @Override
    public ExchangeRate getQuotation() {
        return restTemplate.getForObject(exchangeServiceUrl, ExchangeRate.class);
    }
}
