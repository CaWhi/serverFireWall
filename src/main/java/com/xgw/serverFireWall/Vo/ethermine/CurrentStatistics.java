package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class CurrentStatistics extends Statistic implements Serializable {
    private static final long serialVersionUID = -2117536030334034496L;

    /**
     * Unpaid balance (in base units) of the miner
     */
    private BigInteger unpaid;

    private BigInteger unconfirmed;

    /**
     * 	Estimated number of coins mined per minute
     * 	(based on your average hashrate as well as the average block time and difficulty of the network over the last 24 hours.)
     */
    private BigDecimal coinsPerMin;

    /**
     * Estimated number of USD mined per minute
     * (based on your average hashrate as well as the average block time and difficulty of the network over the last 24 hours.)
     */
    private BigDecimal usdPerMin;

    /**
     * 	Estimated number of BTC mined per minute
     * 	(based on your average hashrate as well as the average block time and difficulty of the network over the last 24 hours.)
     */
    private BigDecimal btcPerMin;

    public BigInteger getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(BigInteger unpaid) {
        this.unpaid = unpaid;
    }

    public BigDecimal getCoinsPerMin() {
        return coinsPerMin;
    }

    public void setCoinsPerMin(BigDecimal coinsPerMin) {
        this.coinsPerMin = coinsPerMin;
    }

    public BigDecimal getUsdPerMin() {
        return usdPerMin;
    }

    public void setUsdPerMin(BigDecimal usdPerMin) {
        this.usdPerMin = usdPerMin;
    }

    public BigDecimal getBtcPerMin() {
        return btcPerMin;
    }

    public void setBtcPerMin(BigDecimal btcPerMin) {
        this.btcPerMin = btcPerMin;
    }

    public BigInteger getUnconfirmed() {
        return unconfirmed;
    }

    public void setUnconfirmed(BigInteger unconfirmed) {
        this.unconfirmed = unconfirmed;
    }
}
