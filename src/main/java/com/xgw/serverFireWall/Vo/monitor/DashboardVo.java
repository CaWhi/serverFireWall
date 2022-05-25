package com.xgw.serverFireWall.Vo.monitor;

import java.io.Serializable;

public class DashboardVo implements Serializable {
    private static final long serialVersionUID = -2018082950427326726L;

    private String time;

    private Long reportedHashrate;

    private Long currentHashrate;

    private Long averageHashrate;

    private Integer validShares;

    private Integer invalidShares;

    private Integer staleShares;
}
