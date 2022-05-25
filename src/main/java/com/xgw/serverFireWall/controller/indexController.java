package com.xgw.serverFireWall.controller;

import com.xgw.serverFireWall.utils.CmdClient;
import com.xgw.serverFireWall.utils.NetworkUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class indexController {
    private static Logger logger = LoggerFactory.getLogger(indexController.class);

    private static List<String> ports;

    static {
        ports = new ArrayList<>();
        ports.add("5555");
        ports.add("1199");
        ports.add("443");
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test(HttpServletRequest request) {
        try{
            logger.info(NetworkUtil.getIpAddress(request));
            return NetworkUtil.getIpAddress(request);
        }
        catch (Exception e){
            logger.error("",e);
            return "";
        }
    }

    @RequestMapping(value = "/testSh", method = RequestMethod.GET)
    @ResponseBody
    public String testSH(HttpServletRequest request) {
        try{
//            String homeDirectory = System.getProperty("/usr/local/");
//            return CallCMD.callCMD(String.format("sh -c cd %s; ll", homeDirectory), null);
//            String homeDirectory = System.getProperty("user.home");
//            Process process;
//            process = Runtime.getRuntime()
//                    .exec(String.format("sh -c cd %s;ll", homeDirectory));
//            StreamGobbler streamGobbler =
//                    new StreamGobbler(process.getInputStream(), System.out::println);
//            Executors.newSingleThreadExecutor().submit(streamGobbler);
//            int exitCode = process.waitFor();
////            assert exitCode == 0;
//            return String.valueOf(exitCode);
            CmdClient client = new CmdClient();
            client.run("cd /usr/local");
            client.run("ls");

            return "0";
        }
        catch (Exception e){
            logger.error("",e);
            return "";
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @ResponseBody
    public String updateIP(HttpServletRequest request) {
        try{
//            String homeDirectory = System.getProperty("/usr/local/");
//            return CallCMD.callCMD(String.format("sh -c cd %s; ll", homeDirectory), null);
//            String homeDirectory = System.getProperty("user.home");
//            Process process;
//            process = Runtime.getRuntime()
//                    .exec(String.format("sh -c cd %s;ll", homeDirectory));
//            StreamGobbler streamGobbler =
//                    new StreamGobbler(process.getInputStream(), System.out::println);
//            Executors.newSingleThreadExecutor().submit(streamGobbler);
//            int exitCode = process.waitFor();
////            assert exitCode == 0;
//            return String.valueOf(exitCode);
            String ip = NetworkUtil.getIpAddress(request);
            logger.info("本次ip:"+ip);
            CmdClient client = new CmdClient();
            client.setOutHandler(new CmdClient.OutHandler() {
                @Override
                public void handle(String result) {
                    CmdClient client1 = new CmdClient();
                    if(!StringUtils.isBlank(result)){
                        client1.run(String.format("firewall-cmd --permanent --remove-rich-rule=\"%s\"", result));
                    }

                }
            });
            client.run("firewall-cmd --list-rich-rules");
            Thread.sleep(3000);
            client.setOutHandler(null);
            for(String port : ports){
                client.run(String.format("firewall-cmd --permanent --add-rich-rule=\"rule family=\"ipv4\" source address=\"%s\" port port=\"%s\" protocol=\"tcp\" accept\"", ip, port));
            }

            client.run("firewall-cmd --reload");

            return ip+" updated successed";
        }
        catch (Exception e){
            logger.error("updateIP error",e);
            return "failed";
        }
    }
}
