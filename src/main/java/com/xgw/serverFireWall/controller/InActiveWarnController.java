package com.xgw.serverFireWall.controller;

import com.alibaba.fastjson.JSON;
import com.xgw.serverFireWall.service.InActiveWarnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller()
@RequestMapping("/inactive")
public class InActiveWarnController {
    private static Logger logger = LoggerFactory.getLogger(InActiveWarnController.class);

    @Resource
    InActiveWarnService inActiveWarnService;

    /**
     * 开启掉线提醒
     * @param loginCode
     * @param wallet
     * @return
     */
    @RequestMapping(value = "/openInActiveWarn", method = RequestMethod.GET)
    @ResponseBody
    public String openInActiveWarn(String loginCode, String wallet){
        return JSON.toJSONString(inActiveWarnService.openInActiveWarn(loginCode, wallet));
    }

    /**
     * 更新用户钱包
     * @param loginCode
     * @param wallet
     * @return
     */
    @RequestMapping(value = "/updateWallet", method = RequestMethod.GET)
    @ResponseBody
    public String updateWallet(String loginCode, String wallet){
        return JSON.toJSONString(inActiveWarnService.updateWallet(loginCode, wallet));
    }

    /**
     * 获取用户历史收益
     * @param loginCode
     * @param wallet
     * @return
     */
    @RequestMapping(value = "/getProfits", method = RequestMethod.GET)
    @ResponseBody
    public String getProfits(String loginCode, String wallet, Integer pageIndex){
        return JSON.toJSONString(inActiveWarnService.getProfits(loginCode, wallet, pageIndex, 25));
    }
}
