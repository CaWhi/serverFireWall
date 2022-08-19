package com.xgw.serverFireWall.service;

import com.xgw.serverFireWall.dao.Subscribe;

import java.util.List;

public interface AsyncInActiveWarnService {
    void inActiveTaskExecute(List<Subscribe> allSubscribe) throws InterruptedException;
}
