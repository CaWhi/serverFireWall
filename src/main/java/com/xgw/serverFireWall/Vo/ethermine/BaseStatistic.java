package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;

public class BaseStatistic  implements Serializable {
    private static final long serialVersionUID = 1937243682670528421L;

    private Long time;

    private Long lastSeen;

    private Long reportedHashrate;

    private Long currentHashrate;

    private Long averageHashrate;

    private Integer validShares;

    private Integer invalidShares;

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
