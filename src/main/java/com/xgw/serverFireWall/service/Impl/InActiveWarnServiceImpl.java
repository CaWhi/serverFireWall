package com.xgw.serverFireWall.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xgw.serverFireWall.Vo.ethermine.CurrentStatistics;
import com.xgw.serverFireWall.Vo.ethermine.Payout;
import com.xgw.serverFireWall.Vo.ethermine.Worker;
import com.xgw.serverFireWall.dao.Profit;
import com.xgw.serverFireWall.dao.Subscribe;
import com.xgw.serverFireWall.dao.Warn;
import com.xgw.serverFireWall.dao.mapper.ProfitMapper;
import com.xgw.serverFireWall.dao.mapper.SubscribeMapper;
import com.xgw.serverFireWall.dao.mapper.WarnMapper;
import com.xgw.serverFireWall.service.InActiveWarnService;
import com.xgw.serverFireWall.service.MonitorService;
import com.xgw.serverFireWall.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service("inActiveWarnService")
public class InActiveWarnServiceImpl implements InActiveWarnService {
    private static Logger logger = LoggerFactory.getLogger(InActiveWarnServiceImpl.class);

    private static final int inactiveTime = 1200;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Resource
    SubscribeMapper subscribeMapper;

    @Resource
    WarnMapper warnMapper;

    @Resource
    MonitorService monitorService;

    @Resource
    ProfitMapper profitMapper;

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public Boolean openInActiveWarn(String openid, String wallet) {
        try{
//            String openid = getOpenID(loginCode);
//            if(StringUtils.isBlank(openid) || StringUtils.isBlank(wallet)){
//                return false;
//            }

            Subscribe subscribe = subscribeMapper.getByOpenId(openid);

            Calendar calendar = Calendar.getInstance();
            if(subscribe != null){
                subscribe.setUpdateTime(calendar.getTime());
                subscribe.setWallet(wallet);

                calendar.set(2099, 11, 30, 0, 0, 0);
                subscribe.setExpireTime(calendar.getTime());

                subscribeMapper.update(subscribe);
            } else {
                subscribe = new Subscribe();
                subscribe.setOpenid(openid);
                subscribe.setWallet(wallet);
                subscribe.setCreateTime(calendar.getTime());

                calendar.set(2099, 11, 30, 0, 0, 0);
                subscribe.setExpireTime(calendar.getTime());

                subscribeMapper.insert(subscribe);
            }

            return true;
        }
        catch (Exception e){
            logger.error("开启掉线提醒失败，wallet:{}", wallet, e);
            return false;
        }
    }

    @Override
    public Boolean updateWallet(String openid, String wallet) {
        try{
//            String openid = getOpenID(loginCode);
//            if(StringUtils.isBlank(openid) || StringUtils.isBlank(wallet)){
//                return false;
//            }

            Subscribe subscribe = subscribeMapper.getByOpenId(openid);

            Calendar calendar = Calendar.getInstance();
            if(subscribe != null){
                subscribe.setUpdateTime(calendar.getTime());
                subscribe.setWallet(wallet);

                subscribeMapper.update(subscribe);
            } else {
                subscribe = new Subscribe();
                subscribe.setOpenid(openid);
                subscribe.setWallet(wallet);
                subscribe.setCreateTime(calendar.getTime());

                subscribeMapper.insert(subscribe);
            }

            return true;
        }
        catch (Exception e){
            logger.error("更新钱包失败，wallet:{}", wallet, e);
            return false;
        }
    }

    @Override
    public List<Profit> getProfits(String openid, String wallet, Integer pageIndex, Integer pageSize) {
        try{
//            String openid = getOpenID(loginCode);
//            if(StringUtils.isBlank(openid) || StringUtils.isBlank(wallet)){
//                return new ArrayList<>();
//            }

            if(pageIndex == null || pageIndex < 0){
                pageIndex = 0;
            }
            if(pageSize == null || pageSize < 0){
                pageSize = 25;
            }

            return profitMapper.getUserLastProfit(openid, StringUtils.lowerCase(wallet), pageIndex*pageSize, pageSize);
        }
        catch (Exception e){
            logger.error("获取历史收益失败，wallet:{}", wallet, e);
            return new ArrayList<>();
        }
    }

//    private String getOpenID(String loginCode){
//        String str = HttpClientUtil.get(String.format(LOGIN_URL, loginCode), null, false, null, null);
//        String result = JSON.parseObject(str).containsKey("openid") ? (String)JSON.parseObject(str).get("openid") : null;
//
//        return result;
//    }

    @Override
    public void profitTaskExecute() {
        try{
            Calendar recent = Calendar.getInstance();
            recent.add(Calendar.DATE, -7);
            //获取7天内活跃账号
            List<Subscribe> allSubscribe = subscribeMapper.getRecent(recent.getTime());
            if(CollectionUtils.isEmpty(allSubscribe)){
                logger.info("{},没有账户需要计算收益", LocalDateTime.now());
                return;
            }

            // 分页
            List<List<Subscribe>> subscribePages = CommonUtils.pageList(allSubscribe, 50);

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
                        Calendar now = Calendar.getInstance();
                        String openid = subscribe.getOpenid();
                        String wallet = subscribe.getWallet();
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

                    profitMapper.batchInsert(profitList);
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

    @Override
    public void inActiveTaskExecute() {
        Calendar calendar = Calendar.getInstance();
        //获取所有已开启掉线提醒的账号
        List<Subscribe> subscribes = subscribeMapper.getUnexpired(calendar.getTime());
        if(CollectionUtils.isEmpty(subscribes)){
            logger.info("{},没有用户需要执行掉线提醒", LocalDateTime.now());
            return;
        }

        SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);//跟上述sql区别
        WarnMapper warnMapperSession = sqlSession.getMapper(WarnMapper.class);

        //挨个查询每个用户矿机情况
        for(Subscribe subscribe : subscribes){
            String openid = subscribe.getOpenid();
            String wallet = subscribe.getWallet();

            //获取矿工状态
            List<Worker> workers = monitorService.getWorkers(wallet);
            if(CollectionUtils.isEmpty(workers)){
                continue;
            }
            Map<String, List<Worker>> workerMap = getInActiveWorkers(workers);
            List<Worker> inActiveWorkers = workerMap.get("inActive");
            List<Worker> activeWorkers = workerMap.get("active");

            //获取未上线的所有矿机
            List<Warn> warns = warnMapper.batchGetWarnUnDealed(openid, wallet);

            List<Warn> dealedWarns = getDealedWarns(warns, activeWorkers);
            List<Warn> warnWorker = getWarnWorkers(warns, inActiveWorkers, openid, wallet);
            if(!CollectionUtils.isEmpty(dealedWarns)){
                warnMapperSession.batchUpdateDealed(dealedWarns);
            }
            if(!CollectionUtils.isEmpty(warnWorker)){
                warnMapperSession.batchInsert(warnWorker);
            }

            Set<String> warnWorkerNames = new HashSet<>();
            for(Warn warn : warnWorker){
                warnWorkerNames.add(warn.getInActiveWorker());
            }
            System.out.println(JSON.toJSONString(warnWorkerNames));
        }

        sqlSession.commit();
    }

    //获取掉线且未提醒的矿机
    private List<Warn> getWarnWorkers(List<Warn> warns, List<Worker> inActiveWorkers, String openid, String wallet){
        Map<String, Warn> warnMap = new HashMap<>();
        for(Warn warn : warns){
            warnMap.put(warn.getInActiveWorker(), warn);
        }

        Calendar calendar = Calendar.getInstance();
        List<Warn> warnList = new ArrayList<>();
        Calendar lastSeen = Calendar.getInstance();
        for(Worker worker : inActiveWorkers){
            Warn warn = warnMap.get(worker.getWorker());
            //未提醒过
            if(warn == null){
                warn = new Warn();
                warn.setId(UUID.randomUUID().toString());
                warn.setOpenid(openid);
                warn.setWallet(wallet);
                warn.setDealed(false);
                warn.setInActiveWorker(worker.getWorker());
                lastSeen.setTimeInMillis(worker.getLastSeen()*1000L);
                warn.setLastSeen(lastSeen.getTime());
                warn.setCreateTime(calendar.getTime());
                warnList.add(warn);
            }
        }

        return warnList;
    }

    //获取已经上线的矿机，更新数据库状态
    private List<Warn> getDealedWarns(List<Warn> warns, List<Worker> activeWorkers){
        Set<String> activeWorkerNames = new HashSet<>();
        for(Worker worker : activeWorkers){
            activeWorkerNames.add(worker.getWorker());
        }

        Calendar calendar = Calendar.getInstance();
        List<Warn> dealedWarns = new ArrayList<>();
        for(Warn warn : warns){
            if(activeWorkerNames.contains(warn.getInActiveWorker())){
                warn.setDealed(true);
                warn.setUpdateTime(calendar.getTime());
                dealedWarns.add(warn);
            }
        }

        return dealedWarns;
    }

    private Map<String, List<Worker>> getInActiveWorkers(List<Worker> workers){
        List<Worker> inActiveWorkers = new ArrayList<>();
        List<Worker> activeWorkers = new ArrayList<>();

        for(Worker worker : workers){
            if(worker.getCurrentHashrate() <= 0 || worker.getTime() - worker.getLastSeen() > inactiveTime){
                inActiveWorkers.add(worker);
            } else {
                activeWorkers.add(worker);
            }
        }
        Map<String, List<Worker>> result = new HashMap<>();
        result.put("active", activeWorkers);
        result.put("inActive", inActiveWorkers);

        return result;
    }
}
