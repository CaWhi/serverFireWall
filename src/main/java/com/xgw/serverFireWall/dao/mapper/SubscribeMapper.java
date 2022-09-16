package com.xgw.serverFireWall.dao.mapper;

import com.xgw.serverFireWall.dao.Subscribe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface SubscribeMapper {

    Subscribe getByOpenIdWallet(String openid, String wallet);

    Subscribe getByOpenId(@Param("openid") String openid, @Param("coin") String coin);

    List<Subscribe> getUnexpired(Date now);

    List<Subscribe> getSetWarn();

    List<Subscribe> getRecent(@Param("last") Date last, @Param("coin") String coin);

    void insert(Subscribe subscribe);

    void update(Subscribe subscribe);
}
