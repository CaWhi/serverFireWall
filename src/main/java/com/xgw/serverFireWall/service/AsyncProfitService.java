package com.xgw.serverFireWall.service;

import com.xgw.serverFireWall.dao.Subscribe;

import java.util.List;

public interface AsyncProfitService {
    void inActiveTaskExecute(List<Subscribe> allSubscribes) throws InterruptedException;
}
