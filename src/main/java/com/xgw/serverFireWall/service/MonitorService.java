package com.xgw.serverFireWall.service;

import com.xgw.serverFireWall.Vo.ethermine.*;

import java.util.List;

public interface MonitorService {
    /**
     * Dashboard
     * @param wallet
     * @return
     */
    Data getMinerDashboard(String wallet, String url);

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
    List<Payout> getMinerPayouts(String wallet, String url);

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
    Settings getMinerSettings(String wallet, String url);

    /**
     * Statistics
     * @param wallet
     * @return
     */
    CurrentStatistics getMinerCurrentStats(String wallet, String url);

    /**
     * All worker statistics
     * @param wallet
     * @return
     */
    List<Worker> getWorkers(String wallet, String url);

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
    PoolStatsData getPoolStats(String url);

    /**
     * Mined Blocks Stats
     * @return
     */
    List<Block> getBlocksHistory();

    /**
     * Network Statistics
     * @return
     */
    NetworkStats getNetworkStats(String url);

    /**
     * Server Hashrate Stats
     * @return
     */
    List<ServerStat> getServersHistory();

    String getCoinUrl(String coin);
}
