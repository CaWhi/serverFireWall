package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigInteger;

public class Block implements Serializable {
    private static final long serialVersionUID = -7260018521629077066L;

    /**
     * 	Unix timestamp of the statistic entry
     */
    private Long time;

    /**
     * 	Number of blocks mined during that hour
     */
    private Double nbrBlocks;

    /**
     * 	Max difficulty during that hour
     */
    private BigInteger difficulty;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Double getNbrBlocks() {
        return nbrBlocks;
    }

    public void setNbrBlocks(Double nbrBlocks) {
        this.nbrBlocks = nbrBlocks;
    }

    public BigInteger getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(BigInteger difficulty) {
        this.difficulty = difficulty;
    }
}
