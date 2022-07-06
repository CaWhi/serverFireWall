package com.xgw.serverFireWall.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;

public class HttpClientUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String get(String uri, String successCode, boolean proxy, String statusProp, String dataProp) {
        try (CloseableHttpClient httpclient = createSSLClient()) {
            HttpResponse responseHttp = null;
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            logger.info("HttpClientUtil get uri:" + httpGet.getURI().toString());
            //使用代理
            if (proxy) {
                HttpHost httpHost = new HttpHost("127.0.0.1", 10809);
                RequestConfig config = RequestConfig.custom().setProxy(httpHost).build();
                httpGet.setConfig(config);
            } else {
                httpGet.setConfig(RequestConfig.custom().build());
            }
            // 执行请求
            responseHttp = httpclient.execute(httpGet);
            logger.info("HttpClientUtil get code:" + httpGet.getURI() + ":" + responseHttp.getStatusLine().getStatusCode());
            if (responseHttp == null || responseHttp.getStatusLine().getStatusCode() != 200) {
                return null;
            }

            String str = EntityUtils.toString(responseHttp.getEntity());

            if (StringUtils.isNotBlank(statusProp) && !StringUtils.equals(successCode, JSON.parseObject(str).getString(statusProp))) {
                return null;
            }
            return StringUtils.isNotBlank(dataProp) ? JSON.toJSONString(JSON.parseObject(str).get(dataProp)) : str;
        } catch (IOException e) {
            logger.error("HttpClientUtil get error", e);
            return null;
        }
    }


    //构造能进行https通讯，httpClient对象
    public static CloseableHttpClient createSSLClient() {

        SSLContext sslContext = null;
        try {
//这个TLSV1.2必须是和服务器匹配的版本号（一定要匹配）
            sslContext = SSLContext.getInstance("TLSV1.3");
            HttpsTrustManager hm = new HttpsTrustManager();//自定义的信任管理器
            sslContext.init(null, new X509TrustManager[]{hm}, new SecureRandom());
            SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            return HttpClients.custom().setSSLSocketFactory(factory).build();
        } catch (Exception e) {
            return null;
        }


    }
}
