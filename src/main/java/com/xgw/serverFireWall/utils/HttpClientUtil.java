package com.xgw.serverFireWall.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String get(String uri, String successCode, boolean proxy){
        try(CloseableHttpClient httpclient = HttpClients.createDefault()){
            HttpResponse responseHttp = null;
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            logger.info("HttpClientUtil get uri:"+httpGet.getURI().toString());
            //使用代理
            if(proxy){
                HttpHost httpHost = new HttpHost("127.0.0.1",10809);
                RequestConfig config = RequestConfig.custom().setProxy(httpHost).build();
                httpGet.setConfig(config);
            } else {
                httpGet.setConfig(RequestConfig.custom().build());
            }
            // 执行请求
            responseHttp = httpclient.execute(httpGet);
            logger.info("HttpClientUtil get code:"+httpGet.getURI()+":"+responseHttp.getStatusLine().getStatusCode());
            if(responseHttp == null || responseHttp.getStatusLine().getStatusCode() != 200){
                return null;
            }

            String str = EntityUtils.toString(responseHttp.getEntity());

            if(!StringUtils.equals(successCode, JSON.parseObject(str).getString("status"))){
                return null;
            }
            return JSON.toJSONString(JSON.parseObject(str).get("data"));
        }
        catch (IOException e){
            logger.error("getDashboard error", e);
            return null;
        }
    }
}
