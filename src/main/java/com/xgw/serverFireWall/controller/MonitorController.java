package com.xgw.serverFireWall.controller;

import com.alibaba.fastjson.JSON;
import com.xgw.serverFireWall.service.MonitorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@Controller()
@RequestMapping("/monitor")
public class MonitorController {
    @Resource
    MonitorService monitorService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test(String wallet){
        return JSON.toJSONString(monitorService.getMinerDashboard(wallet));
    }

    @RequestMapping(value = "/minerWorkers", method = RequestMethod.GET)
    @ResponseBody
    public String minerWorkers(String wallet, HttpServletResponse response){
        return JSON.toJSONString(monitorService.getMinerDashboard(wallet));
    }
}
