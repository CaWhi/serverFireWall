package com.xgw.serverFireWall.dao;

import java.util.Date;

public class Profit {
    private Integer id;

    private String openid;

    private String wallet;

    private Double currentUnpaid;

    private Double reportHashRate;

    private Double averageHashRate;

    private Double lastDayProfit;

    private Date profitTime;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Double getCurrentUnpaid() {
        return currentUnpaid;
    }

    public void setCurrentUnpaid(Double currentUnpaid) {
        this.currentUnpaid = currentUnpaid;
    }

    public Double getLastDayProfit() {
        return lastDayProfit;
    }

    public void setLastDayProfit(Double lastDayProfit) {
        this.lastDayProfit = lastDayProfit;
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

    public Date getProfitTime() {
        return profitTime;
    }

    public void setProfitTime(Date profitTime) {
        this.profitTime = profitTime;
    }

    public Double getReportHashRate() {
        return reportHashRate;
    }

    public void setReportHashRate(Double reportHashRate) {
        this.reportHashRate = reportHashRate;
    }

    public Double getAverageHashRate() {
        return averageHashRate;
    }

    public void setAverageHashRate(Double averageHashRate) {
        this.averageHashRate = averageHashRate;
    }
}
