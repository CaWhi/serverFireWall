package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.math.BigInteger;

public class CurrentStatistics extends Statistic implements Serializable {
    private static final long serialVersionUID = -2117536030334034496L;

    private BigInteger unpaid;

    public BigInteger getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(BigInteger unpaid) {
        this.unpaid = unpaid;
    }
}
