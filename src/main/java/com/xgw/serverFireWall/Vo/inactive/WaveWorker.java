package com.xgw.serverFireWall.Vo.inactive;

public class WaveWorker {
    private String worker;

    private String lastReportHashrate;

    private String reportHashrate;

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getLastReportHashrate() {
        return lastReportHashrate;
    }

    public void setLastReportHashrate(String lastReportHashrate) {
        this.lastReportHashrate = lastReportHashrate;
    }

    public String getReportHashrate() {
        return reportHashrate;
    }

    public void setReportHashrate(String reportHashrate) {
        this.reportHashrate = reportHashrate;
    }
}
