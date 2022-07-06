package com.xgw.serverFireWall.service;

public interface InActiveWarnService {
    /**
     * 开启掉线提醒
     * @param loginCode 微信登录code
     * @param wallet 钱包
     * @return
     */
    Boolean openInActiveWarn(String loginCode, String wallet);

    void InActiveTaskExecute();
}
