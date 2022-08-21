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
//		CommonUtils.getHashRate(91888433l);
//		profit.setCreateTime(new Date(1660841247000l));
//		getLastPaid("0x6dbdaacd224ec1574cef479d9d077d7cedb55e9f",profit,new Date(1660927865000l));
//		}
//		catch (Exception e){
//			e.printStackTrace();
//		}
//		subscribeMapper.getByOpenId("oQyaC5A-OVPr0Er9BmPqFrIoqIe0");
//		System.out.println(JSON.toJSONString(profitMapper.getUserLastProfit("oQyaC5K8-2w1DCY20BsVzmKW95GI", "0xa2c4c39c7e17900e48613a0292a882468773a6e0", 0, 25)));
//		inActiveWarnService.inActiveTaskExecute();
//		try{
//			Set<String> inactive = new HashSet<>();
//			inactive.add("3080");
//			List<WaveWorker> waveWorkers = new ArrayList<>();
//			WaveWorker waveWorker = new WaveWorker();
//			waveWorker.setWorker("3080");
//			waveWorker.setLastReportHashrate("91MH/s");
//			waveWorker.setReportHashrate("22MH/s");
//			waveWorkers.add(waveWorker);
//
//			StringBuilder sb = new StringBuilder();
//			if(inactive.size() > 0){
//				sb.append("已掉线矿机：");
//				for(String name : inactive){
//					sb.append(name).append("、");
//				}
//				sb.deleteCharAt(sb.length()-1);
//				sb.append("<br/>");
//			}
//			if(!CollectionUtils.isEmpty(waveWorkers)){
//				sb.append("矿机算力出现波动：");
//				for(WaveWorker worker : waveWorkers){
//					sb.append("名称：").append(worker.getWorker()).append(" ");
//					sb.append("原算力：").append(worker.getLastReportHashrate()).append(" ");
//					sb.append("当前算力：").append(worker.getReportHashrate()).append(";");
//				}
//				sb.deleteCharAt(sb.length()-1);
//			}
//
//			SendEmail.send(sb.toString(),"974555368@qq.com");
//		}
//		catch (Exception e){
//
//		}
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
