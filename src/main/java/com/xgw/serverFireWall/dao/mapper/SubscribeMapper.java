package com.xgw.serverFireWall.dao.mapper;

import com.xgw.serverFireWall.dao.Subscribe;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;

@Mapper
public interface SubscribeMapper {

    Subscribe getByOpenIdWallet(String openid, String wallet);

    Subscribe getByOpenId(String openid);

    List<Subscribe> getUnexpired(Date now);

    void insert(Subscribe subscribe);

    void update(Subscribe subscribe);
}
