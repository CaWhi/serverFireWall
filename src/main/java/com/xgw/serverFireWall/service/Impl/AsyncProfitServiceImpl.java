package com.xgw.serverFireWall.service.Impl;

import com.xgw.serverFireWall.Vo.ethermine.CurrentStatistics;
import com.xgw.serverFireWall.Vo.ethermine.Payout;
import com.xgw.serverFireWall.dao.Profit;
import com.xgw.serverFireWall.dao.Subscribe;
import com.xgw.serverFireWall.dao.mapper.ProfitMapper;
import com.xgw.serverFireWall.service.AsyncProfitService;
import com.xgw.serverFireWall.service.MonitorService;
import com.xgw.serverFireWall.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

//@Service("asyncProfitService")
public class AsyncProfitServiceImpl implements AsyncProfitService {
    private static Logger logger = LoggerFactory.getLogger(AsyncProfitServiceImpl.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    MonitorService monitorService;

    @Resource
    ProfitMapper profitMapper;

    @Async("syncExecutorPool")
    @Override
    public void inActiveTaskExecute(List<Subscribe> allSubscribes) throws InterruptedException {
        try{
            System.out.println(Thread.currentThread().getName());
            if(CollectionUtils.isEmpty(allSubscribes)){
                logger.info("{},没有账户需要计算收益", LocalDateTime.now());
                return;
            }

            // 分页
            List<List<Subscribe>> subscribePages = CommonUtils.pageList(allSubscribes, 50);

            // 收益日期
            Calendar last = Calendar.getInstance();
            last.add(Calendar.DATE, -1);
            String profitDate = sdf.format(last.getTime());

            // 分批计算收益
            for(List<Subscribe> subscribes : subscribePages){
                try{
                    //获取最新的收益记录
                    Set<String> openidSet = new HashSet<>();
                    Set<String> walletSet = new HashSet<>();
                    for(Subscribe subscribe : subscribes){
                        if(StringUtils.isBlank(subscribe.getWallet())){
                            continue;
                        }

                        openidSet.add(subscribe.getOpenid());
                        walletSet.add(StringUtils.lowerCase(subscribe.getWallet()));
                    }
                    List<Profit> lastProfits = profitMapper.getLastProfit(new ArrayList<>(openidSet), new ArrayList<>(walletSet));
                    Map<String, Profit> openidWalletProfit = new HashMap<>();
                    for(Profit profit : lastProfits){
                        openidWalletProfit.put(profit.getOpenid()+profit.getWallet(), profit);
                    }

                    List<Profit> profitList = new ArrayList<>();
                    //遍历计算昨天收益
                    for(Subscribe subscribe : subscribes){
                        String openid = subscribe.getOpenid();
                        String wallet = subscribe.getWallet();
                        try{
                            if(StringUtils.isBlank(subscribe.getWallet())){
                                continue;
                            }

                            Calendar now = Calendar.getInstance();
                            CurrentStatistics currentStatistics = monitorService.getMinerCurrentStats(wallet);
                            if(currentStatistics == null){
                                currentStatistics = new CurrentStatistics();
                                currentStatistics.setUnpaid(new BigInteger("0"));
                                currentStatistics.setReportedHashrate(0l);
                                currentStatistics.setAverageHashrate(0l);
                            }
                            if(currentStatistics.getUnpaid() == null){
                                currentStatistics.setUnpaid(new BigInteger("0"));
                            }
                            Double currentUnpaid = CommonUtils.dealCoinAmount(currentStatistics.getUnpaid());
                            Double reportHashRate = Double.valueOf(currentStatistics.getReportedHashrate()) / 1000000;
                            Double averageHashrate = Double.valueOf(currentStatistics.getAverageHashrate()) / 1000000;

                            Double lastUnpaid = getLastUnpaid(openidWalletProfit.get(openid+StringUtils.lowerCase(wallet)), profitDate);
                            Profit profit = new Profit();
                            //之前没有收益记录，第一天不计算收益
                            if(lastUnpaid == null){
                                profit.setOpenid(openid);
                                profit.setWallet(StringUtils.lowerCase(wallet));
                                profit.setCurrentUnpaid(currentUnpaid);
                                profit.setReportHashRate(reportHashRate);
                                profit.setAverageHashRate(averageHashrate);
                                profit.setProfitTime(last.getTime());
                                profit.setCreateTime(now.getTime());
                            } else {
                                //计算昨天收益
                                BigInteger lastPaid = getLastPaid(wallet, profitDate);
                                //昨日收益 = 昨日支付总额 + 当前未支付余额 - 昨天未支付余额
                                //todo 可能存在精度问题
                                Double lastDayProfit = CommonUtils.dealCoinAmount(lastPaid.add(currentStatistics.getUnpaid())) - lastUnpaid;
                                if(lastDayProfit < 0){
                                    lastDayProfit = 0d;
                                }

                                profit.setOpenid(openid);
                                profit.setWallet(StringUtils.lowerCase(wallet));
                                profit.setCurrentUnpaid(currentUnpaid);
                                profit.setReportHashRate(reportHashRate);
                                profit.setAverageHashRate(averageHashrate);
                                profit.setLastDayProfit(lastDayProfit);
                                profit.setProfitTime(last.getTime());
                                profit.setCreateTime(now.getTime());
                            }
                            profitList.add(profit);
                            logger.info("计算余额，openid:{},wallet:{},currentUnpaid:{},lastDayProfit:{}", openid, wallet, currentUnpaid, profit.getLastDayProfit());
                            Thread.sleep(1000);
                        }
                        catch (Exception e){
                            logger.error("每日收益单个计算失败,openid:{},wallet:{}", openid, wallet, e);
                        }
                    }

                    profitMapper.batchInsert(profitList);
//                    System.out.println(JSON.toJSONString(profitList));
                }
                catch (Exception e){
                    logger.error("每日收益分批计算失败", e);
                }
            }
        }
        catch (Exception e){
            logger.error("每日计算收益失败", e);
        }
    }

    /**
     * 获取昨天支付的数额
     * @param wallet
     * @param profitDate
     * @return
     */
    private BigInteger getLastPaid(String wallet, String profitDate){
        List<Payout> payouts = monitorService.getMinerPayouts(wallet);

        if(CollectionUtils.isEmpty(payouts)){
            return new BigInteger("0");
        }

        BigInteger lastPaid = new BigInteger("0");
        for(Payout payout : payouts){
            Date time = new Date(payout.getPaidOn() * 1000);
            String timeStr = sdf.format(time);
            if(StringUtils.equals(timeStr, profitDate)){
                lastPaid = lastPaid.add(payout.getAmount());
            }
        }

        return lastPaid;
    }

    /**
     * 获取今天开始时未支付余额
     * @param profit
     * @param profitDate
     * @return
     */
    private Double getLastUnpaid(Profit profit, String profitDate){
        if(profit == null || profit.getCurrentUnpaid() == null){
            return null;
        }

        String date = sdf.format(profit.getCreateTime());
        if(!StringUtils.equals(date, profitDate)){
            return null;
        }

        return profit.getCurrentUnpaid();
    }
}
