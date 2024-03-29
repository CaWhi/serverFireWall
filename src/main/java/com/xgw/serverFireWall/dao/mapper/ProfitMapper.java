package com.xgw.serverFireWall.dao.mapper;

import com.xgw.serverFireWall.dao.Profit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProfitMapper {
    List<Profit> getLastProfit(@Param("openids") List<String> openids, @Param("wallets") List<String> wallets, @Param("coinTable") String coinTable);

    List<Profit> getUserLastProfit(@Param("openid") String openid, @Param("wallet") String wallet,
                                   @Param("offset") Integer offset, @Param("pageSize") Integer pageSize, @Param("coinTable") String coinTable);

    void batchInsert(@Param("profits") List<Profit> profits, @Param("coinTable") String coinTable);

    List<Profit> getUserProfitByDate(@Param("openid") String openid, @Param("wallet") String wallet,
                                     @Param("start") Date start, @Param("end") Date end, @Param("coinTable") String coinTable);
}
