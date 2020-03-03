package com.anget.zaid.angelvisit;

import com.anget.zaid.angelvisit.exchangerate.ExchangeRatesIo;
import com.anget.zaid.angelvisit.exchangerate.ExchangeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class AngelVisitApplication {
    private final static Logger LOGGER = LoggerFactory.getLogger(AngelVisitApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AngelVisitApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

 }
