package com.xgw.serverFireWall.dao;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class WorkerDao {
    private Long id;

    private String openid;

    private String wallet;

    private String worker;

    private Long workerTime;

    private Long lastseen;

    private Long reportHashrate;

    private Long currentHashrate;

    private Long averageHashrate;

    private Integer validShares;

    private Integer invalidShares;

    private Integer staleShares;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public Long getWorkerTime() {
        return workerTime;
    }

    public void setWorkerTime(Long workerTime) {
        this.workerTime = workerTime;
    }

    public Long getLastseen() {
        return lastseen;
    }

    public void setLastseen(Long lastseen) {
        this.lastseen = lastseen;
    }

    public Long getReportHashrate() {
        return reportHashrate;
    }

    public void setReportHashrate(Long reportHashrate) {
        this.reportHashrate = reportHashrate;
    }

    public Long getCurrentHashrate() {
        return currentHashrate;
    }

    public void setCurrentHashrate(Long currentHashrate) {
        this.currentHashrate = currentHashrate;
    }

    public Long getAverageHashrate() {
        return averageHashrate;
    }

    public void setAverageHashrate(Long averageHashrate) {
        this.averageHashrate = averageHashrate;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
