package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.util.List;

public class PoolStatsData implements Serializable {
    private static final long serialVersionUID = -5941090337480837768L;

    private List<MinedBlock> minedBlocks;

    /**
     * General pool stats
     */
    private PoolStats poolStats;

    /**
     * Price information
     */
    private Price price;

    /**
     *
     */
    private Estimates estimates;

    public List<MinedBlock> getMinedBlocks() {
        return minedBlocks;
    }

    public void setMinedBlocks(List<MinedBlock> minedBlocks) {
        this.minedBlocks = minedBlocks;
    }

    public PoolStats getPoolStats() {
        return poolStats;
    }

    public void setPoolStats(PoolStats poolStats) {
        this.poolStats = poolStats;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Estimates getEstimates() {
        return estimates;
    }

    public void setEstimates(Estimates estimates) {
        this.estimates = estimates;
    }
}
