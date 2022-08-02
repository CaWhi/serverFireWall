package com.xgw.serverFireWall;

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
	void contextLoads() {
		inActiveWarnService.profitTaskExecute();
	}

}
