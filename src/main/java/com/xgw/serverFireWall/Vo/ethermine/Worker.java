package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;

public class Worker extends BaseStatistic implements Serializable {
    private static final long serialVersionUID = 8796122676877051496L;

    /**
     * 	Worker name
     */
    private String worker;

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }
}
