package com.xgw.serverFireWall.service;

import com.xgw.serverFireWall.dao.Profit;

import java.util.List;

public interface InActiveWarnService {
    /**
     * 开启掉线提醒
     * @param loginCode 微信登录code
     * @param wallet 钱包
     * @return
     */
    Boolean openInActiveWarn(String loginCode, String wallet);

    /**
     * 更新钱包
     * @param loginCode 微信登录code
     * @param wallet 钱包
     * @return
     */
    Boolean updateWallet(String loginCode, String wallet);

    /**
     * 获取用户历史收益
     * @param loginCode
     * @param wallet
     * @return
     */
    List<Profit> getProfits(String loginCode, String wallet, Integer pageIndex, Integer pageSize);

    void inActiveTaskExecute();

    void profitTaskExecute();
}
