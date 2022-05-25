package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NetworkStats implements Serializable {
    private static final long serialVersionUID = 2625228513661868909L;

    /**
     * 	Unix timestamp of the statistic entry
     */
    private Long time;

    /**
     * 	Current block time of the network
     */
    private Double blockTime;

    /**
     * Current difficulty of the network
     */
    private BigInteger difficulty;

    /**
     * Current hashrate of the network in H/s
     */
    private BigInteger hashrate;

    /**
     * 	Current price in USD
     */
    private BigDecimal usd;

    /**
     * 	Current price in BTC
     */
    private BigDecimal btc;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Double blockTime) {
        this.blockTime = blockTime;
    }

    public BigInteger getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(BigInteger difficulty) {
        this.difficulty = difficulty;
    }

    public BigInteger getHashrate() {
        return hashrate;
    }

    public void setHashrate(BigInteger hashrate) {
        this.hashrate = hashrate;
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
}
