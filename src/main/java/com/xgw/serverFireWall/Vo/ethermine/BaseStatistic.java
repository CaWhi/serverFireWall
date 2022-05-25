package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;

public class BaseStatistic  implements Serializable {
    private static final long serialVersionUID = 1937243682670528421L;

    /**
     * 	Unix timestamp of the statistic entry
     */
    private Long time;

    /**
     * 	Unix timestamp of when the worker was last seen by the pool
     */
    private Long lastSeen;

    /**
     * Reported hashrate of the miner in H/s
     */
    private Long reportedHashrate;

    /**
     * Current hashrate of the miner in H/s
     */
    private Long currentHashrate;

    /**
     * Average hashrate of the miner in H/s during the last 24h
     */
    private Long averageHashrate;

    /**
     * 	Valid shares submitted by the miner
     */
    private Integer validShares;

    /**
     * 	Inalid shares submitted by the miner
     */
    private Integer invalidShares;

    /**
     * 	Stale shares submitted by the miner
     */
    private Integer staleShares;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Long getReportedHashrate() {
        return reportedHashrate;
    }

    public void setReportedHashrate(Long reportedHashrate) {
        this.reportedHashrate = reportedHashrate;
    }

    public Long getCurrentHashrate() {
        return currentHashrate;
    }

    public void setCurrentHashrate(Long currentHashrate) {
        this.currentHashrate = currentHashrate;
    }

    public Integer getValidShares() {
        return validShares;
    }

    public void setValidShares(Integer validShares) {
        this.validShares = validShares;
    }

    public Integer getInvalidShares() {
        return invalidShares;
    }

    public void setInvalidShares(Integer invalidShares) {
        this.invalidShares = invalidShares;
    }

    public Integer getStaleShares() {
        return staleShares;
    }

    public void setStaleShares(Integer staleShares) {
        this.staleShares = staleShares;
    }

    public Long getAverageHashrate() {
        return averageHashrate;
    }

    public void setAverageHashrate(Long averageHashrate) {
        this.averageHashrate = averageHashrate;
    }
}
