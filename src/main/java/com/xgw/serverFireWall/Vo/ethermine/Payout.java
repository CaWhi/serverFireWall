package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigInteger;

public class Payout implements Serializable {
    private static final long serialVersionUID = 5879089677222694585L;

    /**
     * 	Unix timestamp of the payout
     */
    private Long paidOn;

    /**
     * 	Start block of payout
     */
    private Long start;

    /**
     * End block of payout
     */
    private Long end;

    /**
     * 	Paid amount in base units
     */
    private BigInteger amount;

    /**
     * 	Hash of the payout transaction
     */
    private String txHash;

    private String startHash;
    private String endHash;

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

    public String getStartHash() {
        return startHash;
    }

    public void setStartHash(String startHash) {
        this.startHash = startHash;
    }

    public String getEndHash() {
        return endHash;
    }

    public void setEndHash(String endHash) {
        this.endHash = endHash;
    }
}
