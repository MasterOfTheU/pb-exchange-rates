package com.exchangerates.util;

public abstract class Constants {

    public static final String EXCHANGE_RATES_URL =
            "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    public static final String NBU_CURRENCIES_URL =
            "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?json";

    public static final String NBU_CURRENCIES_DATE_URL =
            "https://bank.gov.ua/NBUStatService/v1/statdirectory/exchange?date=";

}
