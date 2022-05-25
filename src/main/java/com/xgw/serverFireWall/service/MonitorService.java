package com.xgw.serverFireWall.service;

import com.xgw.serverFireWall.Vo.ethermine.Data;
import com.xgw.serverFireWall.Vo.ethermine.Payout;
import com.xgw.serverFireWall.Vo.ethermine.Statistic;
import com.xgw.serverFireWall.Vo.ethermine.Worker;

import java.util.List;

public interface MonitorService {
    Data getMinerDashboard(String wallet);

    List<Statistic> getMinerHistory(String wallet);

    List<Payout> getMinerPayouts(String wallet);

    List<Worker> getWorkers(String wallet);

    List<Worker> getWorkerHistory(String wallet, String worker);

    Worker getWorkerCurrentStats(String wallet, String worker);

    Worker getWorkerMonitor(String wallet, String worker);
}
