package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;

public class MinedBlock implements Serializable {
    private static final long serialVersionUID = -178574809252365725L;

    /**
     * 	Block number
     */
    private Long number;

    /**
     * minedBy
     */
    private String miner;

    /**
     * 	Mined on (unix timestamp)
     */
    private Long time;

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
