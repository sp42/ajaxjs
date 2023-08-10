package com.ajaxjs.developertools.tools.mysql.tools;

import java.io.IOException;
import java.io.InputStream;

import com.ajaxjs.util.logger.LogHelper;

public class MysqlUtils {
    private static final LogHelper LOGGER = LogHelper.getLog(MysqlUtils.class);

    /**
     * 只能在 Linux 下执行
     *
     * @author <a href="https://github.com/535404515/MYSQL-TOMCAT-MONITOR/blob/master/nlpms-task-monitor/src/main/java/com/nuoli/mysqlprotect/timer/MysqlServiceJob.java">...</a>
     */
    public static String ping(String username, String password) {
        Process p;

        try {
            p = new ProcessBuilder("mysqladmin", "-u" + username, "-p" + password, "ping").start();
        } catch (IOException e) {
            return "获取 mysql 是否停止异常";
        }

        byte[] b = new byte[1024];
        int readBytes;
        StringBuilder sb = new StringBuilder();

        try (InputStream in = p.getInputStream()) {
            while ((readBytes = in.read(b)) != -1) sb.append(new String(b, 0, readBytes));
        } catch (IOException e) {
            return "读取流异常";
        }

        return sb.toString();
    }

    public static void restartMysql() {
        Runtime runtime = Runtime.getRuntime();

        try {
            runtime.exec("net stop mysql57");
            runtime.exec("net start mysql57");
        } catch (IOException e) {
            LOGGER.warning(e);
        }
    }
}
