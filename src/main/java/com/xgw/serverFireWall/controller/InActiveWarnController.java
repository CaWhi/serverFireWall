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
import javax.servlet.http.HttpServletRequest;

@Controller()
@RequestMapping("/inactive")
public class InActiveWarnController {
    private static Logger logger = LoggerFactory.getLogger(InActiveWarnController.class);

    @Resource
    InActiveWarnService inActiveWarnService;

    /**
     * 开启掉线提醒
     * @param wallet
     * @return
     */
    @RequestMapping(value = "/openInActiveWarn", method = RequestMethod.GET)
    @ResponseBody
    public String openInActiveWarn(HttpServletRequest request, String wallet){
        return inActiveWarnService.openInActiveWarn(request.getHeader("x-wx-openid"), wallet).toString();
    }

    /**
     * 更新用户钱包
     * @param wallet
     * @return
     */
    @RequestMapping(value = "/updateWallet", method = RequestMethod.GET)
    @ResponseBody
    public String updateWallet(HttpServletRequest request, String wallet, String email, Integer threshold){
        return inActiveWarnService.updateWallet(request.getHeader("x-wx-openid"), wallet, email, threshold).toString();
    }

    /**
     * 获取用户历史收益
     * @param wallet
     * @return
     */
    @RequestMapping(value = "/getProfits", method = RequestMethod.GET)
    @ResponseBody
    public String getProfits(HttpServletRequest request, String wallet, Integer pageIndex){
        return JSON.toJSONString(inActiveWarnService.getProfits(request.getHeader("x-wx-openid"), wallet, pageIndex, 25));
    }

//    @RequestMapping(value = "/test", method = RequestMethod.GET)
//    @ResponseBody
//    public String test(HttpServletRequest request){
////        String uri = "https://api.weixin.qq.com/cgi-bin/token?appid=wx1c4e008cbe2d3846&secret=9f09b7dc70383df555813642091bb6f4&grant_type=client_credential";
//        return request.getHeader("x-wx-openid");
//    }
}
