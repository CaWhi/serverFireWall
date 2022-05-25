package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;

public class Statistic extends BaseStatistic implements Serializable {
    private static final long serialVersionUID = -3541470400305579267L;

    private Integer activeWorkers;

    public Integer getActiveWorkers() {
        return activeWorkers;
    }

    public void setActiveWorkers(Integer activeWorkers) {
        this.activeWorkers = activeWorkers;
    }
}
