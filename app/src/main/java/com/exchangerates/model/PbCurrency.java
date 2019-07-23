package com.exchangerates.model;

import com.google.gson.annotations.SerializedName;


/**
 * Represents an object of PrivatBank currency.
 */
public class PbCurrency {

    /**
     * Currency code.
     */
    @SerializedName("ccy")
    private String ccy;

    @SerializedName("buy")
    private Double buyRate;

    @SerializedName("sale")
    private Double saleRate;

    public PbCurrency(String ccy, Double buyRate, Double saleRate) {
        this.ccy = ccy;
        this.buyRate = buyRate;
        this.saleRate = saleRate;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public Double getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(Double buyRate) {
        this.buyRate = buyRate;
    }

    public Double getSaleRate() {
        return saleRate;
    }

    public void setSaleRate(Double saleRate) {
        this.saleRate = saleRate;
    }

    @Override
    public String toString() {
        return "PbCurrency{" +
                "ccy='" + ccy + '\'' +
                ", buyRate=" + buyRate +
                ", saleRate=" + saleRate +
                '}';
    }

}
