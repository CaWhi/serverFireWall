package com.xgw.serverFireWall.service;

import com.xgw.serverFireWall.dao.Profit;

import java.util.List;

public interface InActiveWarnService {
    /**
     * 开启掉线提醒
     * @param openid 微信登录code
     * @param wallet 钱包
     * @return
     */
    Boolean openInActiveWarn(String openid, String wallet);

    /**
     * 更新钱包
     * @param openid 微信登录code
     * @param wallet 钱包
     * @return
     */
    Boolean updateWallet(String openid, String wallet, String email, Integer threshold);

    /**
     * 获取用户历史收益
     * @param openid
     * @param wallet
     * @return
     */
    List<Profit> getProfits(String openid, String wallet, Integer pageIndex, Integer pageSize);

    void inActiveTaskExecute();

    void profitTaskExecute();
}
