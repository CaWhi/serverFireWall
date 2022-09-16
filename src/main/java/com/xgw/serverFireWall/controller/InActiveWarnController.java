package com.xgw.serverFireWall.controller;

import com.alibaba.fastjson.JSON;
import com.xgw.serverFireWall.service.InActiveWarnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/inactive")
public class InActiveWarnController {
    private static Logger logger = LoggerFactory.getLogger(InActiveWarnController.class);

    @Resource
    InActiveWarnService inActiveWarnService;

    /**
     * 开启掉线提醒
     * @param wallet
     * @return
     */
//    @RequestMapping(value = "/openInActiveWarn", method = RequestMethod.GET)
//    @ResponseBody
//    public String openInActiveWarn(HttpServletRequest request, String wallet){
//        return inActiveWarnService.openInActiveWarn(request.getHeader("x-wx-openid"), wallet).toString();
//    }

    /**
     * 更新用户钱包
     * @param wallet
     * @return
     */
    @RequestMapping(value = "/updateWallet", method = RequestMethod.GET)
    @ResponseBody
    public String updateWallet(HttpServletRequest request, String wallet, String email, Integer threshold){
        return inActiveWarnService.updateWallet(request.getHeader("x-wx-openid"), wallet, email, threshold, request.getHeader("coin")).toString();
    }

    /**
     * 获取用户历史收益
     * @param wallet
     * @return
     */
    @RequestMapping(value = "/getProfits", method = RequestMethod.GET)
    @ResponseBody
    public String getProfits(HttpServletRequest request, String wallet, Integer pageIndex){
        return JSON.toJSONString(inActiveWarnService.getProfits(request.getHeader("x-wx-openid"), wallet, pageIndex, 25, request.getHeader("coin")));
    }

	/**
	 * 获取用户历史收益
	 * @param wallet
	 * @return
	 */
	@RequestMapping(value = "/getProfitsByDate", method = RequestMethod.GET)
	@ResponseBody
	public String getProfitsByDate(HttpServletRequest request, String wallet, String start, String end){
		return JSON.toJSONString(inActiveWarnService.getProfitsByDate(request.getHeader("x-wx-openid"), wallet, start, end, request.getHeader("coin")));
	}

    @RequestMapping(value = "/getTest", method = RequestMethod.GET)
    @ResponseBody
    public String getTest(HttpServletRequest request){
//        String uri = "https://api.weixin.qq.com/cgi-bin/token?appid=wx1c4e008cbe2d3846&secret=9f09b7dc70383df555813642091bb6f4&grant_type=client_credential";
//        try{
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
//			logger.error("test", e);
//		}
        return request.getHeader("x-wx-openid")+request.getHeader("coin");
    }
}
