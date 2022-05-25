package com.xgw.serverFireWall.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xgw.serverFireWall.Vo.ethermine.Data;
import com.xgw.serverFireWall.Vo.ethermine.Worker;
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

    private static final String OK = "OK";
    @Override
    public Data getMinerDashboard(String wallet) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/dashboard", baseURI, wallet), OK, true);
            Data result = JSON.parseObject(str, Data.class);

            return result;
        }
        catch (Exception e){
            logger.error("getDashboard error", e);
            return null;
        }
    }

    @Override
    public List<Worker> getWorkers(String wallet) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/workers", baseURI, wallet), OK, true);
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
            String str = HttpClientUtil.get(String.format("%s/miner/%s/worker/%s/history", baseURI, wallet, worker), OK, true);
            List<Worker> result = JSON.parseArray(str, Worker.class);

            return result;
        }
        catch (Exception e){
            logger.error("getWorkers error", e);
            return null;
        }
    }

    @Override
    public Worker getWorkerCurrentStats(String wallet, String worker) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/worker/%s/currentStats", baseURI, wallet, worker), OK, true);
            Worker result = JSON.parseObject(str, Worker.class);

            return result;
        }
        catch (Exception e){
            logger.error("getWorkers error", e);
            return null;
        }
    }

    @Override
    public Worker getWorkerMonitor(String wallet, String worker) {
        try{
            String str = HttpClientUtil.get(String.format("%s/miner/%s/worker/%s/monitor", baseURI, wallet, worker), OK, true);
            Worker result = JSON.parseObject(str, Worker.class);

            return result;
        }
        catch (Exception e){
            logger.error("getWorkers error", e);
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
