package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigInteger;

public class Payout implements Serializable {
    private static final long serialVersionUID = 5879089677222694585L;

    private Long paidOn;

    private Long start;

    private Long end;

    private BigInteger amount;

    private String txHash;

    public Long getPaidOn() {
        return paidOn;
    }

    public void setPaidOn(Long paidOn) {
        this.paidOn = paidOn;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Long getEnd() {
        return end;
    }

    public void setEnd(Long end) {
        this.end = end;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }
}
