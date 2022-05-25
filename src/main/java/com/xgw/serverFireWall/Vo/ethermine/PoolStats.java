package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigInteger;

public class PoolStats implements Serializable {
    private static final long serialVersionUID = -7878690302200560901L;

    /**
     * 	Current hashrate of the pool in H/s
     */
    private BigInteger hashRate;

    /**
     * Currently active miners
     */
    private Integer miners;

    /**
     * Currently active workers
     */
    private Integer workers;

    private Double blocksPerHour;

    public BigInteger getHashRate() {
        return hashRate;
    }

    public void setHashRate(BigInteger hashRate) {
        this.hashRate = hashRate;
    }

    public Integer getMiners() {
        return miners;
    }

    public void setMiners(Integer miners) {
        this.miners = miners;
    }

    public Integer getWorkers() {
        return workers;
    }

    public void setWorkers(Integer workers) {
        this.workers = workers;
    }

    public Double getBlocksPerHour() {
        return blocksPerHour;
    }

    public void setBlocksPerHour(Double blocksPerHour) {
        this.blocksPerHour = blocksPerHour;
    }
}
