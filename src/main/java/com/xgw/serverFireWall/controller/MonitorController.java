package com.xgw.serverFireWall.controller;

import com.alibaba.fastjson.JSON;
import com.xgw.serverFireWall.service.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller()
@RequestMapping("/monitor")
public class MonitorController {
    private static Logger logger = LoggerFactory.getLogger(MonitorController.class);

    @Resource
    MonitorService monitorService;

    @RequestMapping(value = "/poolStats", method = RequestMethod.GET)
    @ResponseBody
    public String poolStats(){
        return JSON.toJSONString(monitorService.getPoolStats());
    }

//    @RequestMapping(value = "/blocksHistory", method = RequestMethod.GET)
//    @ResponseBody
//    public String blocksHistory(){
//        return JSON.toJSONString(monitorService.getBlocksHistory());
//    }

    @RequestMapping(value = "/networkStats", method = RequestMethod.GET)
    @ResponseBody
    public String networkStats(){
        return JSON.toJSONString(monitorService.getNetworkStats());
    }

//    @RequestMapping(value = "/serversHistory", method = RequestMethod.GET)
//    @ResponseBody
//    public String serversHistory(){
//        return JSON.toJSONString(monitorService.getServersHistory());
//    }

    @RequestMapping(value = "/minerDashboard", method = RequestMethod.GET)
    @ResponseBody
    public String minerDashboard(String wallet){
        return JSON.toJSONString(monitorService.getMinerDashboard(wallet));
    }

//    @RequestMapping(value = "/minerHistory", method = RequestMethod.GET)
//    @ResponseBody
//    public String minerHistory(String wallet){
//        return JSON.toJSONString(monitorService.getMinerHistory(wallet));
//    }

//    @RequestMapping(value = "/minerPayouts", method = RequestMethod.GET)
//    @ResponseBody
//    public String minerPayouts(String wallet){
//        return JSON.toJSONString(monitorService.getMinerPayouts(wallet));
//    }
//
//    @RequestMapping(value = "/minerRounds", method = RequestMethod.GET)
//    @ResponseBody
//    public String minerRounds(String wallet){
//        return JSON.toJSONString(monitorService.getMinerRounds(wallet));
//    }

    @RequestMapping(value = "/minerSettings", method = RequestMethod.GET)
    @ResponseBody
    public String minerSettings(String wallet){
        return JSON.toJSONString(monitorService.getMinerSettings(wallet));
    }

    @RequestMapping(value = "/minerCurrentStats", method = RequestMethod.GET)
    @ResponseBody
    public String minerCurrentStats(String wallet){
        return JSON.toJSONString(monitorService.getMinerCurrentStats(wallet));
    }

//    @RequestMapping(value = "/minerWorkers", method = RequestMethod.GET)
//    @ResponseBody
//    public String minerWorkers(String wallet){
//        return JSON.toJSONString(monitorService.getWorkers(wallet));
//    }

//    @RequestMapping(value = "/minerWorkerHistory", method = RequestMethod.GET)
//    @ResponseBody
//    public String minerWorkerHistory(String wallet, String worker){
//        return JSON.toJSONString(monitorService.getWorkerHistory(wallet,worker));
//    }

//    @RequestMapping(value = "/minerWorkerCurrentStats", method = RequestMethod.GET)
//    @ResponseBody
//    public String minerWorkerCurrentStats(String wallet, String worker){
//        return JSON.toJSONString(monitorService.getWorkerCurrentStats(wallet,worker));
//    }
//
//    @RequestMapping(value = "/minerWorkerMonitor", method = RequestMethod.GET)
//    @ResponseBody
//    public String minerWorkerMonitor(String wallet, String worker){
//        return JSON.toJSONString(monitorService.getWorkerMonitor(wallet,worker));
//    }
}
