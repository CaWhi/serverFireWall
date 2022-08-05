package com.xgw.serverFireWall.dao;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Warn {
    private String id;

    private String openid;

    private String wallet;

    private String inActiveWorker;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date LastSeen;

    private Boolean dealed;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getInActiveWorker() {
        return inActiveWorker;
    }

    public void setInActiveWorker(String inActiveWorker) {
        this.inActiveWorker = inActiveWorker;
    }

    public Boolean getDealed() {
        return dealed;
    }

    public void setDealed(Boolean dealed) {
        this.dealed = dealed;
    }

    public Date getLastSeen() {
        return LastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        LastSeen = lastSeen;
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
