package com.xgw.serverFireWall.service;

import com.xgw.serverFireWall.Vo.ethermine.*;

import java.util.List;

public interface MonitorService {
    /**
     * Dashboard
     * @param wallet
     * @return
     */
    Data getMinerDashboard(String wallet);

    /**
     * History
     * @param wallet
     * @return
     */
    List<Statistic> getMinerHistory(String wallet);

    /**
     * Payouts
     * @param wallet
     * @return
     */
    List<Payout> getMinerPayouts(String wallet);

    /**
     * Rounds
     * @param wallet
     * @return
     */
    List<Round> getMinerRounds(String wallet);

    /**
     * Settings
     * @param wallet
     * @return
     */
    Settings getMinerSettings(String wallet);

    /**
     * Statistics
     * @param wallet
     * @return
     */
    CurrentStatistics getMinerCurrentStats(String wallet);

    /**
     * All worker statistics
     * @param wallet
     * @return
     */
    List<Worker> getWorkers(String wallet);

    /**
     * Individual historical worker statistics
     * @param wallet
     * @param worker
     * @return
     */
    List<Worker> getWorkerHistory(String wallet, String worker);

    /**
     * Individual worker statistics
     * @param wallet
     * @param worker
     * @return
     */
    Worker getWorkerCurrentStats(String wallet, String worker);

    /**
     * Worker monitoring
     * @param wallet
     * @param worker
     * @return
     */
    Worker getWorkerMonitor(String wallet, String worker);

    /**
     * Basic Pool Stats
     * @return
     */
    PoolStatsData getPoolStats();

    /**
     * Mined Blocks Stats
     * @return
     */
    List<Block> getBlocksHistory();

    /**
     * Network Statistics
     * @return
     */
    NetworkStats getNetworkStats();

    /**
     * Server Hashrate Stats
     * @return
     */
    List<ServerStat> getServersHistory();
}
