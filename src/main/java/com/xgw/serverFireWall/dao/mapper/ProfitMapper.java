package com.xgw.serverFireWall.dao.mapper;

import com.xgw.serverFireWall.dao.Profit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfitMapper {
    List<Profit> getLastProfit(@Param("openids") List<String> openids, @Param("wallets") List<String> wallets);

    List<Profit> getUserLastProfit(@Param("openid") String openid, @Param("wallet") String wallet,
                                   @Param("offset") Integer offset, @Param("pageSize") Integer pageSize);

    void batchInsert(List<Profit> profits);
}
