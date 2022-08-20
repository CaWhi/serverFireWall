package com.xgw.serverFireWall;

import com.xgw.serverFireWall.Vo.ethermine.Payout;
import com.xgw.serverFireWall.dao.Profit;
import com.xgw.serverFireWall.service.InActiveWarnService;
import com.xgw.serverFireWall.service.MonitorService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@SpringBootTest
class ServerFireWallApplicationTests {
	@Resource
	MonitorService monitorService;

	@Resource
	InActiveWarnService inActiveWarnService;

	@Test
//	@Ignore
	void contextLoads() {
//		try{
//			for(int i=0;i<1;i++){
//				inActiveWarnService.profitTaskExecute();
//			}
//		Profit profit = new Profit();
//		profit.setCreateTime(new Date(1660841247000l));
//		getLastPaid("0x6dbdaacd224ec1574cef479d9d077d7cedb55e9f",profit,new Date(1660927865000l));
//		}
//		catch (Exception e){
//			e.printStackTrace();
//		}
//		subscribeMapper.getByOpenId("oQyaC5A-OVPr0Er9BmPqFrIoqIe0");
//		System.out.println(JSON.toJSONString(profitMapper.getUserLastProfit("oQyaC5K8-2w1DCY20BsVzmKW95GI", "0xa2c4c39c7e17900e48613a0292a882468773a6e0", 0, 25)));
//		inActiveWarnService.inActiveTaskExecute();
	}

	private BigInteger getLastPaid(String wallet, Profit profit, Date now){
		List<Payout> payouts = monitorService.getMinerPayouts(wallet);

		if(CollectionUtils.isEmpty(payouts)){
			return new BigInteger("0");
		}

		Date last = profit.getCreateTime();

		BigInteger lastPaid = new BigInteger("0");
		for(Payout payout : payouts){
			Date time = new Date(payout.getPaidOn() * 1000);
//            String timeStr = sdf.format(time);
			if(time.compareTo(last) > 0 && time.compareTo(now) <= 0){
				lastPaid = lastPaid.add(payout.getAmount());
			}
		}

		return lastPaid;
	}

}
