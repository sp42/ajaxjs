package com.ajaxjs.developertools.tools.mysql.tools;

import java.io.IOException;
import java.io.InputStream;

public class MySqlMonitor {
    public static String getInfoMySqlIsOpen(String username, String password) {
        try {
            Process p = new ProcessBuilder("mysqladmin", "-u" + username, "-p" + password, "ping").start();
            byte[] b = new byte[1024];
            int readbytes;
            StringBuilder sb = new StringBuilder();

            try (InputStream in = p.getInputStream()) {
                while ((readbytes = in.read(b)) != -1)
                    sb.append(new String(b, 0, readbytes));
            }

            return sb.toString();
        } catch (IOException e) {
            return "获取mysql是否停止异常";
        }
    }

    public static void restartMysql() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("net stop mysql57");
            runtime.exec("net start mysql57");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
