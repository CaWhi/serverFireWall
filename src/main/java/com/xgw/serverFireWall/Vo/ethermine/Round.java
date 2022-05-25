package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigInteger;

public class Round implements Serializable {
    private static final long serialVersionUID = -6436429187267304823L;

    /**
     * 	Block number of the round
     */
    private Integer block;

    /**
     * Amount in base units allocated to the miner in the round
     */
    private BigInteger amount;

    public Integer getBlock() {
        return block;
    }

    public void setBlock(Integer block) {
        this.block = block;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }
}
