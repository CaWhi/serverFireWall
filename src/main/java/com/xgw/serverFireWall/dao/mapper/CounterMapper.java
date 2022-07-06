package com.xgw.serverFireWall.dao.mapper;

import com.xgw.serverFireWall.dao.Counter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CounterMapper {
    Counter getById(@Param("id") Integer id);
}
