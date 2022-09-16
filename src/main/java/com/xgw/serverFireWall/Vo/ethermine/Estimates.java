package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class Estimates implements Serializable {
    private static final long serialVersionUID = -8993197039606130398L;

    private String time;

    private Double blockReward;

    private BigInteger hashrate;

    private Double blockTime;

    private BigDecimal gasPrice;

    private Long ttttd;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getBlockReward() {
        return blockReward;
    }

    public void setBlockReward(Double blockReward) {
        this.blockReward = blockReward;
    }

    public BigInteger getHashrate() {
        return hashrate;
    }

    public void setHashrate(BigInteger hashrate) {
        this.hashrate = hashrate;
    }

    public Double getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Double blockTime) {
        this.blockTime = blockTime;
    }

    public BigDecimal getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigDecimal gasPrice) {
        this.gasPrice = gasPrice;
    }

    public Long getTtttd() {
        return ttttd;
    }

    public void setTtttd(Long ttttd) {
        this.ttttd = ttttd;
    }
}
