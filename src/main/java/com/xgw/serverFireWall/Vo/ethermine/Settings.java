package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigInteger;

public class Settings implements Serializable {
    private static final long serialVersionUID = 5226468809201804895L;

    /**
     * Masked Email address of the miner
     */
    private String email;

    /**
     * Monitoring enabled (1 for yes, 0 for no)
     */
    private Integer monitor;

    /**
     * 	Minimum payout amount defined by the miner in base units
     */
    private BigInteger minPayout;

    private Integer suspended;

    /**
     * 	Masked IP address of one randomly selected worker
     */
    private String ip;

    private Integer method;

    private Integer gasPriceLimit;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getMonitor() {
        return monitor;
    }

    public void setMonitor(Integer monitor) {
        this.monitor = monitor;
    }

    public BigInteger getMinPayout() {
        return minPayout;
    }

    public void setMinPayout(BigInteger minPayout) {
        this.minPayout = minPayout;
    }

    public Integer getSuspended() {
        return suspended;
    }

    public void setSuspended(Integer suspended) {
        this.suspended = suspended;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getMethod() {
        return method;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public Integer getGasPriceLimit() {
        return gasPriceLimit;
    }

    public void setGasPriceLimit(Integer gasPriceLimit) {
        this.gasPriceLimit = gasPriceLimit;
    }
}
