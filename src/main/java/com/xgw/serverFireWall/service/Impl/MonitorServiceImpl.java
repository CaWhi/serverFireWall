package com.xgw.serverFireWall.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xgw.serverFireWall.Vo.ethermine.*;
import com.xgw.serverFireWall.service.MonitorService;
import com.xgw.serverFireWall.utils.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("monitorService")
public class MonitorServiceImpl implements MonitorService {
    private static Logger logger = LoggerFactory.getLogger(MonitorServiceImpl.class);

//    private static final String baseURI = "https://api.ethermine.org";
    private static final String baseURI = "https://funmm.top/api-server";

    private static final Boolean proxy = false;

    private static final String OK = "OK";

    private static final Map<String, Long> wallet2Time = new HashMap<>();

    private static final String path = System.getProperty("user.dir") + File.separator + "data";

    private static final String split = "|";

    private static final String statusProp = "status";

    private static final String dataProp = "data";

    /**
     * 十分钟
     */
    private static final int duration = -1;

    static {
        try{
            File file = new File(path);
            if(!file.exists()){
                file.mkdir();
            }

            file = new File(path + File.separator + "RequestTime.txt");

            if(!file.exists()){
                file.createNewFile();
            }
        }
        catch (IOException e){
            logger.error("读取请求时间异常，", e);
        }

        try(FileInputStream fis = new FileInputStream(path + File.separator + "RequestTime.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));){
            String line = br.readLine();
            while (line != null){
                wallet2Time.put(StringUtils.split(line,split)[0], Long.valueOf(StringUtils.split(line,split)[1]));
                line = br.readLine();
            }
        }
        catch (IOException e){
            logger.error("读取请求时间异常，", e);
        }
    }

    private boolean getFromCache(String wallet){
        if(!wallet2Time.containsKey(wallet)){
            return false;
        }

        Long time = wallet2Time.get(wallet);
        File file = new File(path+File.separator+wallet+".txt");

        return file.exists() && (System.currentTimeMillis() - time) <= duration;
    }

    private String getCache(String wallet){
        try(FileInputStream fis = new FileInputStream(path+File.separator+wallet+".txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));){
            String line = br.readLine();
            StringBuilder sb = new StringBuilder();
            while (line != null){
                sb.append(line);
                line = br.readLine();
            }

            return sb.toString();
        }
        catch (IOException e){
            logger.error("读取请求缓存异常，", e);
            return "";
        }
    }

    private void setCache(String wallet, String data){
        try{
            File file = new File(path+File.separator+wallet+".txt");
            if(!file.exists()){
                file.createNewFile();
            }
        }
        catch (IOException e){
            logger.error("写入请求缓存异常，", e);
            return;
        }
        try(FileOutputStream fos = new FileOutputStream(path+File.separator+wallet+".txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));){
            bw.write(data);
            bw.flush();
        }
        catch (IOException e){
            logger.error("写入请求缓存异常，", e);
            return;
        }
        setRequestTime(wallet);
    }

    private void setRequestTime(String wallet){
        wallet2Time.put(wallet, System.currentTimeMillis());
        List<String> strs = new ArrayList<>();
        for(Map.Entry<String, Long> entry : wallet2Time.entrySet()){
            strs.add(entry.getKey() + split + entry.getValue().toString());
        }

        try(FileOutputStream fos = new FileOutputStream(path+File.separator+"RequestTime.txt");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));){
            for(String str : strs){
                bw.write(str);
                bw.newLine();
            }
            bw.flush();
        }
        catch (IOException e){
            logger.error("写入请求缓存异常，", e);
        }
    }

    @Override
    public Data getMinerDashboard(String wallet) {
        try{
            String str;
            if(getFromCache(wallet)){
                str = getCache(wallet);

                if(StringUtils.isBlank(str)){
                    str = HttpClientUtil.get(String.format("%s/miner/%s/dashboard", baseURI, wallet), OK, proxy, statusProp, dataProp);
                    setCache(wallet,str);
                }
            } else {
                str = HttpClientUtil.get(String.format("%s/miner/%s/dashboard", baseURI, wallet), OK, proxy, statusProp, dataProp);
                setCache(wallet,str);
            }
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
            String str = HttpClientUtil.get(String.format("%s/miner/%s/history", baseURI, wallet), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/miner/%s/payouts", baseURI, wallet), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/miner/%s/rounds", baseURI, wallet), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/miner/%s/settings", baseURI, wallet), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/miner/%s/currentStats", baseURI, wallet), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/miner/%s/workers", baseURI, wallet), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/miner/%s/worker/%s/history", baseURI, wallet, worker), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/miner/%s/worker/%s/currentStats", baseURI, wallet, worker), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/miner/%s/worker/%s/monitor", baseURI, wallet, worker), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/poolStats", baseURI), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/blocks/history", baseURI), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/networkStats", baseURI), OK, proxy, statusProp, dataProp);
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
            String str = HttpClientUtil.get(String.format("%s/servers/history", baseURI), OK, proxy, statusProp, dataProp);
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
