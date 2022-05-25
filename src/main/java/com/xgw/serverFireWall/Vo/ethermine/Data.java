package com.xgw.serverFireWall.Vo.ethermine;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    private static final long serialVersionUID = -6044185005709602170L;

    /**
     * 	Array of the miner statistics
     */
    private List<Statistic> statistics;

    /**
     * Workers
     */
    private List<Worker> workers;

    /**
     * Array of the current miner statistics
     */
    private CurrentStatistics currentStatistics;

    /**
     * Array of miner settings
     */
    private Settings settings;

    public List<Statistic> getStatistics() {
        return statistics;
    }

    public void setStatistics(List<Statistic> statistics) {
        this.statistics = statistics;
    }

    public List<Worker> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Worker> workers) {
        this.workers = workers;
    }

    public CurrentStatistics getCurrentStatistics() {
        return currentStatistics;
    }

    public void setCurrentStatistics(CurrentStatistics currentStatistics) {
        this.currentStatistics = currentStatistics;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
