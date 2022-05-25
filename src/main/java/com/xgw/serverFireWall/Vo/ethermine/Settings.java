package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigInteger;

public class Settings implements Serializable {
    private static final long serialVersionUID = 5226468809201804895L;

    private String email;

    private Integer monitor;

    private BigInteger minPayout;

    private Integer suspended;

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
}
