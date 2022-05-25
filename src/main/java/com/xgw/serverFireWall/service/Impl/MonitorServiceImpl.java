package com.xgw.serverFireWall.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xgw.serverFireWall.Vo.ethermine.*;
import com.xgw.serverFireWall.service.MonitorService;
import com.xgw.serverFireWall.utils.HttpClientUtil;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service("monitorService")
public class MonitorServiceImpl implements MonitorService {
    private static Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);

    private static final String baseURI = "https://api.ethermine.org";

    private static final Boolean proxy = false;

    private static final String OK = "OK";
    @Override
    public Data getMinerDashboard(String wallet) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/dashboard", baseURI, wallet), OK, proxy);
            Data result = JSON.parseObject(str, Data.class);

            return result;
        }
        catch (Exception e){
            logger.error("getMinerDashboard error", e);
            return null;
        }
    }

    @Override
    public List<Statistic> getMinerHistory(String wallet) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/history", baseURI, wallet), OK, proxy);
            List<Statistic> result = JSON.parseArray(str, Statistic.class);

            return result;
        }
        catch (Exception e){
            logger.error("getMinerHistory error", e);
            return null;
        }
    }

    @Override
    public List<Payout> getMinerPayouts(String wallet) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/payouts", baseURI, wallet), OK, proxy);
            List<Payout> result = JSON.parseArray(str, Payout.class);

            return result;
        }
        catch (Exception e){
            logger.error("getMinerPayouts error", e);
            return null;
        }
    }

    @Override
    public List<Round> getMinerRounds(String wallet) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/rounds", baseURI, wallet), OK, proxy);
            List<Round> result = JSON.parseArray(str, Round.class);

            return result;
        }
        catch (Exception e){
            logger.error("getMinerRounds error", e);
            return null;
        }
    }

    @Override
    public Settings getMinerSettings(String wallet) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/settings", baseURI, wallet), OK, proxy);
            Settings result = JSON.parseObject(str, Settings.class);

            return result;
        }
        catch (Exception e){
            logger.error("getMinerSettings error", e);
            return null;
        }
    }

    @Override
    public CurrentStatistics getMinerCurrentStats(String wallet) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/currentStats", baseURI, wallet), OK, proxy);
            CurrentStatistics result = JSON.parseObject(str, CurrentStatistics.class);

            return result;
        }
        catch (Exception e){
            logger.error("getMinerCurrentStats error", e);
            return null;
        }
    }

    @Override
    public List<Worker> getWorkers(String wallet) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/workers", baseURI, wallet), OK, proxy);
            List<Worker> result = JSON.parseArray(str, Worker.class);

            return result;
        }
        catch (Exception e){
            logger.error("getWorkers error", e);
            return null;
        }
    }

    @Override
    public List<Worker> getWorkerHistory(String wallet, String worker) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/worker/%s/history", baseURI, wallet, worker), OK, proxy);
            List<Worker> result = JSON.parseArray(str, Worker.class);

            return result;
        }
        catch (Exception e){
            logger.error("getWorkerHistory error", e);
            return null;
        }
    }

    @Override
    public Worker getWorkerCurrentStats(String wallet, String worker) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/worker/%s/currentStats", baseURI, wallet, worker), OK, proxy);
            Worker result = JSON.parseObject(str, Worker.class);

            return result;
        }
        catch (Exception e){
            logger.error("getWorkerCurrentStats error", e);
            return null;
        }
    }

    @Override
    public Worker getWorkerMonitor(String wallet, String worker) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/worker/%s/monitor", baseURI, wallet, worker), OK, proxy);
            Worker result = JSON.parseObject(str, Worker.class);

            return result;
        }
        catch (Exception e){
            logger.error("getWorkerMonitor error", e);
            return null;
        }
    }

    @Override
    public PoolStatsData getPoolStats() {
        try{
            String str = HttpClientUtil.get(String.format("%s/poolStats", baseURI), OK, proxy);
            PoolStatsData result = JSON.parseObject(str, PoolStatsData.class);

            return result;
        }
        catch (Exception e){
            logger.error("getPoolStats error", e);
            return null;
        }
    }

    @Override
    public List<Block> getBlocksHistory() {
        try{
            String str = HttpClientUtil.get(String.format("%s/blocks/history", baseURI), OK, proxy);
            List<Block> result = JSON.parseArray(str, Block.class);

            return result;
        }
        catch (Exception e){
            logger.error("getBlocksHistory error", e);
            return null;
        }
    }

    @Override
    public NetworkStats getNetworkStats() {
        try{
            String str = HttpClientUtil.get(String.format("%s/networkStats", baseURI), OK, proxy);
            NetworkStats result = JSON.parseObject(str, NetworkStats.class);

            return result;
        }
        catch (Exception e){
            logger.error("getNetworkStats error", e);
            return null;
        }
    }

    @Override
    public List<ServerStat> getServersHistory() {
        try{
            String str = HttpClientUtil.get(String.format("%s/servers/history", baseURI), OK, proxy);
            List<ServerStat> result = JSON.parseArray(str, ServerStat.class);

            return result;
        }
        catch (Exception e){
            logger.error("getServersHistory error", e);
            return null;
        }
    }

    private void writeToResponse(HttpResponse responseHttp, HttpServletResponse response) throws IOException {
        ServletOutputStream os = null;
        try {
            os = response.getOutputStream();
            response.setContentType("application/json;charset=UTF-8");
//            response.setHeader("Content-disposition",
//                    "attachment;filename= " + fileName);
            responseHttp.getEntity().writeTo(os);
            os.flush();
            os.close();
        } catch (IOException e) {
            logger.error("获取文件出错，IO流异常!", e);
        } finally {
            if (os != null) {
                os.close();
            }
        }
    }
}
