package com.anget.zaid.angelvisit.exchangerate.dto;

import java.util.Map;

public class ExchangeRate {
  private Map<String, Double> rates;
  private String base;
  private String date;

  public Map<String, Double> getRates() { return rates; }
  public void setRates(Map<String, Double> value) { this.rates = value; }

  public String getBase() { return base; }
  public void setBase(String value) { this.base = value; }

  public String getDate() { return date; }
  public void setDate(String value) { this.date = value; }
}
