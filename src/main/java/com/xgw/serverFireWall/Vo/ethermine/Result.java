package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;

public class Result implements Serializable {
    private static final long serialVersionUID = -7016951367434177910L;

    private String status;

    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
