package com.anget.zaid.angelvisit.exchangerate;

import com.anget.zaid.angelvisit.exchangerate.dto.ExchangeRate;

//this is an interface in case we need to implement the service from another provider
public interface ExchangeService {
    ExchangeRate getQuotation();
}
