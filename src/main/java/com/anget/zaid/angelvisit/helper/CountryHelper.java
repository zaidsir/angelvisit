package com.anget.zaid.angelvisit.helper;

import java.util.*;

public class CountryHelper {
    static Map<String, Locale> countryCurrency = fillMap();
    static Map<String, String> currenciesList = getAvailableCurrencies();

    private static Map<String, Locale> fillMap() {
        String[] countries = Locale.getISOCountries();
        Map<String, Locale> localeMap = new HashMap<>(countries.length);
        for (String country : countries) {
            Locale locale = new Locale("", country);
            localeMap.put(locale.getISO3Country().toUpperCase(), locale);
        }
        return localeMap;
    }

    public static String iso3CountryCodeToIso2CountryCode(String iso3CountryCode) {
        return countryCurrency.get(iso3CountryCode).getCountry();
    }

    public static String iso2CountryCodeToIso3CountryCode(String iso2CountryCode) {
        Locale locale = new Locale("", iso2CountryCode);
        return locale.getISO3Country();
    }

    public static String currencyOf(String countryCode) {
        return currenciesList.get(countryCode);
    }

    private static Map<String, String> getAvailableCurrencies() {
        Locale[] locales = Locale.getAvailableLocales();
        // We use TreeMap so that the order of the data in the map sorted
        // based on the country name.
        Map<String, String> currencies = new TreeMap<>();
        for (Locale locale : locales) {
            try {
                currencies.put(locale.getCountry(), Currency.getInstance(locale).getCurrencyCode());
            } catch (Exception e) {
                // when the locale is not supported
            }
        }
        return currencies;
    }
}
