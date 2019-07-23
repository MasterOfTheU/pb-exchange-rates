package com.exchangerates.model;

import com.google.gson.annotations.SerializedName;


/**
 * Represents an object of NBU currency.
 */
public class NbuCurrency {

    @SerializedName("cc")
    private String ccy;

    @SerializedName("rate")
    private Double rate;

    @SerializedName("txt")
    private String ccyName;

    public NbuCurrency(String ccy, Double rate, String ccyName) {
        this.ccy = ccy;
        this.rate = rate;
        this.ccyName = ccyName;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getCcyName() {
        return ccyName;
    }

    public void setCcyName(String ccyName) {
        this.ccyName = ccyName;
    }

    @Override
    public String toString() {
        return "NbuCurrency{" +
                "ccy='" + ccy + '\'' +
                ", rate=" + rate +
                ", ccyName='" + ccyName + '\'' +
                '}';
    }

}
