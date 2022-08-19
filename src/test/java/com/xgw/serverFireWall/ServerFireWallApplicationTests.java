package com.xgw.serverFireWall;

import com.xgw.serverFireWall.dao.mapper.SubscribeMapper;
import com.xgw.serverFireWall.service.InActiveWarnService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ServerFireWallApplicationTests {
	@Resource
	SubscribeMapper subscribeMapper;

	@Resource
	InActiveWarnService inActiveWarnService;

	@Test
//	@Ignore
	void contextLoads() {
//		try{
//			for(int i=0;i<1;i++){
//				inActiveWarnService.profitTaskExecute();
//			}
//		}
//		catch (Exception e){
//			e.printStackTrace();
//		}
//		subscribeMapper.getByOpenId("oQyaC5A-OVPr0Er9BmPqFrIoqIe0");
//		System.out.println(JSON.toJSONString(profitMapper.getUserLastProfit("oQyaC5K8-2w1DCY20BsVzmKW95GI", "0xa2c4c39c7e17900e48613a0292a882468773a6e0", 0, 25)));
		inActiveWarnService.inActiveTaskExecute();
	}

}
