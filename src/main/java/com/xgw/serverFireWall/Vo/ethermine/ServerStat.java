package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigInteger;

public class ServerStat implements Serializable {
    private static final long serialVersionUID = -1985192653176583024L;

    /**
     * 	Unix timestamp of the statistic entry
     */
    private Long time;

    /**
     * Server name
     */
    private String server;

    /**
     * 	Current hashratein H/s
     */
    private BigInteger hashrate;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public BigInteger getHashrate() {
        return hashrate;
    }

    public void setHashrate(BigInteger hashrate) {
        this.hashrate = hashrate;
    }
}
