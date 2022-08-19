package com.xgw.serverFireWall.dao.mapper;

import com.xgw.serverFireWall.dao.WorkerDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WorkerMapper {
    List<WorkerDao> getWorke(String openid, String wallet);

    void delete(String openid, String wallet);

    void insert(List<WorkerDao> workers);
}
