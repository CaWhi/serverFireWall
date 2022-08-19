package com.xgw.serverFireWall.dao.mapper;

import com.xgw.serverFireWall.dao.Warn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface WarnMapper {
    List<Warn> batchGetWarnUnDealed(String openid, String wallet);

    void batchInsert(List<Warn> warns);

    void batchUpdateDealed(@Param("warns") List<Warn> warns, @Param("updateTime") Date updateTime);

    void delete(List<Long> ids);
}
