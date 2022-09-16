package com.xgw.serverFireWall.dao.mapper;

import com.xgw.serverFireWall.dao.Coin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CoinMapper {
    List<Coin> getCoins(String coin);
}
