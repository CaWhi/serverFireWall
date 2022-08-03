package com.xgw.serverFireWall;

import com.alibaba.fastjson.JSON;
import com.xgw.serverFireWall.dao.mapper.ProfitMapper;
import com.xgw.serverFireWall.service.InActiveWarnService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ServerFireWallApplicationTests {
	@Resource
	ProfitMapper profitMapper;

	@Resource
	InActiveWarnService inActiveWarnService;

	@Test
//	@Ignore
	void contextLoads() {
//		inActiveWarnService.profitTaskExecute();
		System.out.println(JSON.toJSONString(profitMapper.getUserLastProfit("oQyaC5K8-2w1DCY20BsVzmKW95GI", "0xa2c4c39c7e17900e48613a0292a882468773a6e0", 0, 25)));
	}

}
