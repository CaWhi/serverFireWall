package com.xgw.serverFireWall.service.Impl;

import com.alibaba.fastjson.JSON;
import com.xgw.serverFireWall.Vo.ethermine.Worker;
import com.xgw.serverFireWall.Vo.inactive.WaveWorker;
import com.xgw.serverFireWall.constant.Constants;
import com.xgw.serverFireWall.dao.Subscribe;
import com.xgw.serverFireWall.dao.Warn;
import com.xgw.serverFireWall.dao.WorkerDao;
import com.xgw.serverFireWall.dao.mapper.ProfitMapper;
import com.xgw.serverFireWall.dao.mapper.SubscribeMapper;
import com.xgw.serverFireWall.dao.mapper.WarnMapper;
import com.xgw.serverFireWall.dao.mapper.WorkerMapper;
import com.xgw.serverFireWall.service.AsyncInActiveWarnService;
import com.xgw.serverFireWall.service.AsyncProfitService;
import com.xgw.serverFireWall.service.MonitorService;
import com.xgw.serverFireWall.utils.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

//@Service("asyncInActiveWarnService")
public class AsyncInActiveWarnServiceImpl implements AsyncInActiveWarnService {
    private static Logger logger = LoggerFactory.getLogger(AsyncInActiveWarnServiceImpl.class);


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

    @Resource
    private AsyncProfitService asyncProfitService;

    @Async("syncExecutorPool")
    @Override
    public void inActiveTaskExecute(List<Subscribe> allSubscribe) throws InterruptedException {
        try{
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

                            System.out.println(JSON.toJSONString(warnWorkerNames));
                            System.out.println(JSON.toJSONString(waveWorkers));
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
            WorkerDao dao = workerDaoMap.get(worker.getWorker());
            if(dao == null){
                continue;
            }

            BigDecimal last = new BigDecimal(String.valueOf(dao.getReportHashrate()));
            BigDecimal cur = new BigDecimal(String.valueOf(worker.getReportedHashrate()));
            Integer low = cur.subtract(last).divide(last).multiply(new BigDecimal("100")).intValue();
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
}
