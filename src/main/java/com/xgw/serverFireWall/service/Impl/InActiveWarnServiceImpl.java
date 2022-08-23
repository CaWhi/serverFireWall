package com.xgw.serverFireWall.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xgw.serverFireWall.Vo.ethermine.CurrentStatistics;
import com.xgw.serverFireWall.Vo.ethermine.Payout;
import com.xgw.serverFireWall.Vo.ethermine.Worker;
import com.xgw.serverFireWall.Vo.inactive.WaveWorker;
import com.xgw.serverFireWall.constant.Constants;
import com.xgw.serverFireWall.dao.Profit;
import com.xgw.serverFireWall.dao.Subscribe;
import com.xgw.serverFireWall.dao.Warn;
import com.xgw.serverFireWall.dao.WorkerDao;
import com.xgw.serverFireWall.dao.mapper.ProfitMapper;
import com.xgw.serverFireWall.dao.mapper.SubscribeMapper;
import com.xgw.serverFireWall.dao.mapper.WarnMapper;
import com.xgw.serverFireWall.dao.mapper.WorkerMapper;
import com.xgw.serverFireWall.service.InActiveWarnService;
import com.xgw.serverFireWall.service.MonitorService;
import com.xgw.serverFireWall.utils.CommonUtils;
import com.xgw.serverFireWall.utils.SendEmail;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service("inActiveWarnService")
public class InActiveWarnServiceImpl implements InActiveWarnService {
    private static Logger logger = LoggerFactory.getLogger(InActiveWarnServiceImpl.class);

    //20分钟
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
    WorkerMapper workerMapper;

    @Resource
    private SqlSessionTemplate sqlSessionTemplate;

//    @Resource
//    private AsyncProfitService asyncProfitService;
//
//    @Resource
//    private AsyncInActiveWarnService asyncInActiveWarnService;

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
    public Boolean updateWallet(String openid, String wallet, String email, Integer threshold) {
        try{
//            String openid = getOpenID(loginCode);
//            if(StringUtils.isBlank(openid) || StringUtils.isBlank(wallet)){
//                return false;
//            }
            if(StringUtils.isBlank(wallet)){
                return false;
            }

            Subscribe subscribe = subscribeMapper.getByOpenId(openid);

            Calendar calendar = Calendar.getInstance();
            if(subscribe != null){
                subscribe.setUpdateTime(calendar.getTime());
                subscribe.setWallet(wallet);
                if(StringUtils.isNotBlank(email)){
                    subscribe.setEmail(email);
                }
                //开启波动提醒
                if(threshold != null && threshold > 0 && threshold <= 100){
                    subscribe.setThreshold(threshold);
                }
                //关闭波动提醒
                if(threshold != null && threshold <= 0){
                    subscribe.setThreshold(threshold);
                }

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
                                BigInteger lastPaid = getLastPaid(wallet, openidWalletProfit.get(openid+StringUtils.lowerCase(wallet)), now.getTime());
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
     * @param profit
     * @return
     */
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
        try{
            //获取所有已开启掉线提醒的账号
            List<Subscribe> allSubscribe = subscribeMapper.getSetWarn();
            if(CollectionUtils.isEmpty(allSubscribe)){
                logger.info("{},没有用户需要执行掉线提醒", LocalDateTime.now());
                return;
            }

            // 分页
            List<List<Subscribe>> subscribePages = CommonUtils.pageList(allSubscribe, 50);

            // 分页处理
            for(List<Subscribe> subscribes : subscribePages){
                try{
                    SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);//跟上述sql区别
                    WarnMapper warnMapperSession = sqlSession.getMapper(WarnMapper.class);
                    WorkerMapper workerMapperSession = sqlSession.getMapper(WorkerMapper.class);

                    //挨个查询每个用户矿机情况
                    for(Subscribe subscribe : subscribes){
                        String openid = subscribe.getOpenid();
                        String wallet = subscribe.getWallet();
                        try{
                            if(StringUtils.isBlank(wallet)){
                                continue;
                            }

                            //获取矿工状态
                            List<Worker> workers = monitorService.getWorkers(wallet);
                            if(CollectionUtils.isEmpty(workers)){
                                continue;
                            }
                            Map<String, List<Worker>> workerMap = getInActiveWorkers(workers);
                            // 掉线矿工
                            List<Worker> inActiveWorkers = workerMap.get("inActive");
                            // 在线矿工
                            List<Worker> activeWorkers = workerMap.get("active");

                            // 矿池端所有矿机
                            Set<String> allWorkers = new HashSet<>();
                            for(Worker worker : workers){
                                allWorkers.add(worker.getWorker());
                            }

                            //掉线名单
                            Set<String> warnWorkerNames = inActiveDeal(openid, wallet, allWorkers, warnMapperSession, activeWorkers, inActiveWorkers);

                            Integer threshold = subscribe.getThreshold();
                            List<WaveWorker> waveWorkers = new ArrayList<>();
                            if(threshold != null && threshold > 0 && threshold <= 100){
                                waveWorkers = waveDeal(openid, wallet, workerMapperSession, activeWorkers, threshold, warnMapperSession);
                            }

//                            System.out.println(JSON.toJSONString(warnWorkerNames));
//                            System.out.println(JSON.toJSONString(waveWorkers));

                            StringBuilder sb = new StringBuilder();
                            sb.append("钱包：").append(wallet).append("<br/>");
                            Calendar now = Calendar.getInstance();
                            sb.append("记录时间：").append(sdf.format(now.getTime())).append("<br/>");
                            if(warnWorkerNames.size() > 0){
                                sb.append("已掉线矿机：");
                                for(String name : warnWorkerNames){
                                    sb.append(name).append("、");
                                }
                                sb.deleteCharAt(sb.length()-1);
                                sb.append("<br/>");
                            }
                            if(!CollectionUtils.isEmpty(waveWorkers)){
                                sb.append("矿机算力出现波动：");
                                for(WaveWorker worker : waveWorkers){
                                    sb.append("名称：").append(worker.getWorker()).append(" ");
                                    sb.append("原算力：").append(worker.getLastReportHashrate()).append(" ");
                                    sb.append("当前算力：").append(worker.getReportHashrate()).append(";");
                                }
                                sb.deleteCharAt(sb.length()-1);
                            }

                            SendEmail.send(sb.toString(), subscribe.getEmail());
                        }
                        catch (Exception e){
                            logger.error("掉线提醒任务异常，单个，openid:{},wallet:{}", openid, wallet, e);
                        }
                    }

                    sqlSession.commit();
                }
                catch (Exception e){
                    logger.error("掉线提醒任务异常，批量", e);
                }
            }
        }
        catch (Exception e){
            logger.error("掉线提醒任务异常", e);
        }
    }

    private List<WaveWorker> waveDeal(String openid, String wallet, WorkerMapper workerMapperSession, List<Worker> activeWorkers,
                                      Integer threshold, WarnMapper warnMapperSession){
        //获取上次矿工状态
        List<WorkerDao> workerDaosLast = workerMapper.getWorke(openid, wallet);
        if(CollectionUtils.isEmpty(workerDaosLast)){
            //插入所有本次在线矿工
            insertActiveWorkers(openid, wallet, activeWorkers, workerMapperSession);
            return null;
        }
        //删除所有记录矿工
        workerMapperSession.delete(openid, wallet);
        //插入所有本次在线矿工
        insertActiveWorkers(openid, wallet, activeWorkers, workerMapperSession);

        Map<String, WorkerDao> workerDaoMap = new HashMap<>();
        for(WorkerDao dao : workerDaosLast){
            workerDaoMap.put(dao.getWorker(), dao);
        }

        List<WaveWorker> waveWorkers = new ArrayList<>();
        List<Warn> waveWarns = new ArrayList<>();
        Calendar lastSeen = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        for(Worker worker : activeWorkers){
            System.out.println(JSON.toJSONString(worker));
            WorkerDao dao = workerDaoMap.get(worker.getWorker());
            if(dao == null){
                continue;
            }
            System.out.println(JSON.toJSONString(dao));
            BigDecimal last = new BigDecimal(String.valueOf(dao.getReportHashrate()));
            BigDecimal cur = new BigDecimal(String.valueOf(worker.getReportedHashrate()));
            if(cur.compareTo(last) >= 0){
                continue;
            }
            Integer low = cur.subtract(last).divide(last, 5, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue();
            low = Math.abs(low);
            System.out.println(JSON.toJSONString(low));
            if(low >= threshold){
                WaveWorker wave = new WaveWorker();
                wave.setWorker(worker.getWorker());
                wave.setLastReportHashrate(CommonUtils.getHashRate(dao.getReportHashrate()));
                wave.setReportHashrate(CommonUtils.getHashRate(worker.getReportedHashrate()));

                waveWorkers.add(wave);

                Warn waveWarn = new Warn();
                waveWarn.setOpenid(openid);
                waveWarn.setWallet(wallet);
                waveWarn.setDealed(true);
                waveWarn.setInActiveWorker(worker.getWorker());
                waveWarn.setWarnType(Constants.WAVE);
                lastSeen.setTimeInMillis(worker.getLastSeen()*1000L);
                waveWarn.setLastSeen(lastSeen.getTime());
                waveWarn.setCreateTime(calendar.getTime());

                waveWarns.add(waveWarn);
            }
        }
        System.out.println(JSON.toJSONString(waveWarns));

        if(!CollectionUtils.isEmpty(waveWarns)){
            warnMapperSession.batchInsert(waveWarns);
        }

        return waveWorkers;
    }

    private Set<String> inActiveDeal(String openid, String wallet, Set<String> allWorkers, WarnMapper warnMapperSession,
                              List<Worker> activeWorkers, List<Worker> inActiveWorkers){
        //获取已提醒未上线的所有矿机
        List<Warn> warns = warnMapper.batchGetWarnUnDealed(openid, wallet);

        // 获取已提醒掉线，但是已经不存在的矿机，并删除
        List<Long> noWarns = new ArrayList<>();
        Iterator<Warn> iterator = warns.iterator();
        while(iterator.hasNext()){
            Warn warn = iterator.next();
            if(!allWorkers.contains(warn.getInActiveWorker())){
                noWarns.add(warn.getId());
                iterator.remove();
            }
        }
        if(!CollectionUtils.isEmpty(noWarns)){
            warnMapperSession.delete(noWarns);
        }

        //已提醒掉线，但是已上线矿机
        List<Warn> dealedWarns = getDealedWarns(warns, activeWorkers);
        //未提醒掉线矿机
        List<Warn> warnWorker = getWarnWorkers(warns, inActiveWorkers, openid, wallet);
        if(!CollectionUtils.isEmpty(dealedWarns)){
            warnMapperSession.batchUpdateDealed(dealedWarns, dealedWarns.get(0).getUpdateTime());
        }
        if(!CollectionUtils.isEmpty(warnWorker)){
            warnMapperSession.batchInsert(warnWorker);
        }

        Set<String> warnWorkerNames = new HashSet<>();
        for(Warn warn : warnWorker){
            warnWorkerNames.add(warn.getInActiveWorker());
        }

        return warnWorkerNames;
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
                warn.setOpenid(openid);
                warn.setWallet(wallet);
                warn.setDealed(false);
                warn.setInActiveWorker(worker.getWorker());
                warn.setWarnType(Constants.INACTIVE);
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

    private void insertActiveWorkers(String openid, String wallet, List<Worker> workers, WorkerMapper workerMapperSession){
        if(CollectionUtils.isEmpty(workers)){
            return;
        }

        Calendar calendar = Calendar.getInstance();
        List<WorkerDao> workerDaos = new ArrayList<>();
        for(Worker worker : workers){
            WorkerDao dao = new WorkerDao();
            dao.setOpenid(openid);
            dao.setWallet(wallet);
            dao.setWorker(worker.getWorker());
            dao.setWorkerTime(worker.getTime());
            dao.setLastseen(worker.getLastSeen());
            dao.setReportHashrate(worker.getReportedHashrate());
            dao.setCurrentHashrate(worker.getCurrentHashrate());
            dao.setAverageHashrate(worker.getAverageHashrate());
            dao.setValidShares(worker.getValidShares());
            dao.setInvalidShares(worker.getInvalidShares());
            dao.setStaleShares(worker.getStaleShares());
            dao.setCreateTime(calendar.getTime());

            workerDaos.add(dao);
        }

        workerMapperSession.insert(workerDaos);
    }

    @Override
    public void profitTaskExecuteAsync() {
//        try{
//            Calendar recent = Calendar.getInstance();
//            recent.add(Calendar.DATE, -7);
//            //获取7天内活跃账号
//            List<Subscribe> allSubscribe = subscribeMapper.getRecent(recent.getTime());
//            if(CollectionUtils.isEmpty(allSubscribe)){
//                logger.info("{},没有账户需要计算收益", LocalDateTime.now());
//                return;
//            }
//
//            // 分页
//            List<List<Subscribe>> subscribePages = CommonUtils.pageList(allSubscribe, 500);
//
//            for(List<Subscribe> subscribes : subscribePages){
//                asyncProfitService.inActiveTaskExecute(subscribes);
//            }
//        }
//        catch (Exception e){
//            logger.error("收益计算异常", e);
//        }
    }

    @Override
    public void inActiveTaskExecuteAsync() {
//        try{
//            //获取所有已开启掉线提醒的账号
//            List<Subscribe> allSubscribe = subscribeMapper.getSetWarn();
//            if(CollectionUtils.isEmpty(allSubscribe)){
//                logger.info("{},没有用户需要执行掉线提醒", LocalDateTime.now());
//                return;
//            }
//
//            // 分页
//            List<List<Subscribe>> subscribePages = CommonUtils.pageList(allSubscribe, 500);
//
//            // 分页处理
//            for(List<Subscribe> subscribes : subscribePages){
//                asyncInActiveWarnService.inActiveTaskExecute(subscribes);
//            }
//        }
//        catch (Exception e){
//            logger.error("掉线提醒任务异常", e);
//        }
    }

    @Override
    public List<Profit> getProfitsByDate(String openid, String wallet, String startDate, String endDate) {
        try{
            if(StringUtils.isBlank(wallet) || StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)){
                return null;
            }
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            List<Profit> profits = profitMapper.getUserProfitByDate(openid, StringUtils.lowerCase(wallet), start, end);
            Map<String, Profit> profitMap = new HashMap<>();
            for(Profit profit : profits){
                profitMap.put(sdf.format(profit.getProfitTime()), profit);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(start);
            List<Profit> result = new ArrayList<>();
            while (calendar.getTime().compareTo(end) <= 0){
                Profit profit = profitMap.get(sdf.format(calendar.getTime()));
                if(profit == null) {
                    profit = new Profit();
                    profit.setOpenid(openid);
                    profit.setWallet(wallet);
                    profit.setProfitTime(calendar.getTime());
                }
                result.add(profit);

                calendar.add(Calendar.DATE, 1);
            }

            Collections.sort(result, new Comparator<Profit>() {
                @Override
                public int compare(Profit o1, Profit o2) {
                    return o1.getProfitTime().compareTo(o2.getProfitTime());
                }
            });

            return result;
        }
        catch (Exception e){
            logger.error("按时间查询每日收益异常，", e);
            return null;
        }
    }
}
