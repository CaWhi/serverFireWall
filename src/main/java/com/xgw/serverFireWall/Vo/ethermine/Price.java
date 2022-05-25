package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigDecimal;

public class Price implements Serializable {
    private static final long serialVersionUID = 4189058742974615989L;

    /**
     * 	Unix timestamp of the statistic entry
     */
    private String time;

    /**
     * 	Current price in USD
     */
    private BigDecimal usd;

    /**
     * 	Current price in BTC
     */
    private BigDecimal btc;

    /**
     * 	Current price in EUR
     */
    private BigDecimal eur;

    /**
     * 	Current price in CNY
     */
    private BigDecimal cny;

    /**
     * 	Current price in RUB
     */
    private BigDecimal rub;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getUsd() {
        return usd;
    }

    public void setUsd(BigDecimal usd) {
        this.usd = usd;
    }

    public BigDecimal getBtc() {
        return btc;
    }

    public void setBtc(BigDecimal btc) {
        this.btc = btc;
    }

    public BigDecimal getEur() {
        return eur;
    }

    public void setEur(BigDecimal eur) {
        this.eur = eur;
    }

    public BigDecimal getCny() {
        return cny;
    }

    public void setCny(BigDecimal cny) {
        this.cny = cny;
    }

    public BigDecimal getRub() {
        return rub;
    }

    public void setRub(BigDecimal rub) {
        this.rub = rub;
    }
}
