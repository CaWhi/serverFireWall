package com.xgw.serverFireWall.dao.mapper;

import com.xgw.serverFireWall.dao.Warn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WarnMapper {
    List<Warn> batchGetWarnUnDealed(String openid, String wallet);

    void batchInsert(List<Warn> warns);

    void batchUpdateDealed(List<Warn> warns);
}
