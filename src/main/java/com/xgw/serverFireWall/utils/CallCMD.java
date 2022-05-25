package com.xgw.serverFireWall.utils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.InputStreamReader;

public class CallCMD{
    // 关流方法
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                System.out.println("close stream failed.");
            }
        }
    }

    // 此方法用来执行脚本或命令
    // String cmd：脚本（绝对路径）位置或命令内容
    // File dir：执行命令的子进程的工作目录（null则表示和当前主进程工作目录相同）
    public static String callCMD(String cmd, File dir) {
        StringBuilder result = new StringBuilder();
        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;
        try {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(cmd, null, dir);
            // 方法阻塞,等待命令执行完成
            process.waitFor();
            // 获取命令执行结果, 有两个结果: 正常的输出和错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn =
                    new BufferedReader(
                            new InputStreamReader(process.getInputStream(), "UTF-8")); // 正常的输出
            bufrError =
                    new BufferedReader(
                            new InputStreamReader(process.getErrorStream(), "UTF-8")); // 错误的输出
            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);
            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }
        // 返回执行结果
        return result.toString();
    }
}