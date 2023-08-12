package com.ajaxjs.developertools.monitor;

import com.ajaxjs.util.ObjectHelper;
import com.ajaxjs.util.io.StreamHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Map;

/**
 * @author <a href="https://blog.csdn.net/hj7jay/article/details/51979939">...</a>
 */
public class LinuxSystemTool {
    /**
     * 内存的信息(kb)
     */
    public static Map<String, Object> getMemInfo() {
        File file = new File("/proc/meminfo");
        Map<String, Object> map = new HashMap<>();

        try {
            StreamHelper.read(Files.newInputStream(file.toPath()), line -> {
                StringTokenizer token = new StringTokenizer(line);
                if (!token.hasMoreTokens()) return;

                line = token.nextToken();
                if (!token.hasMoreTokens()) return;

                if (line.equalsIgnoreCase("MemTotal:"))
                    map.put("MemTotal", token.nextToken());
                else if (line.equalsIgnoreCase("MemFree:"))
                    map.put("MemFree", token.nextToken());
                else if (line.equalsIgnoreCase("SwapTotal:"))
                    map.put("SwapTotal", token.nextToken());
                else if (line.equalsIgnoreCase("SwapFree:"))
                    map.put("SwapFree", token.nextToken());
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * get memory by used info
     *
     * @return float efficiency
     */
    public static float getCpuInfo() {
        File file = new File("/proc/stat");
        StringTokenizer token;
        int user1, nice1, sys1, idle1;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())))) {
            token = new StringTokenizer(br.readLine());
            token.nextToken();

            user1 = Integer.parseInt(token.nextToken());
            nice1 = Integer.parseInt(token.nextToken());
            sys1 = Integer.parseInt(token.nextToken());
            idle1 = Integer.parseInt(token.nextToken());
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        ObjectHelper.sleep(1);

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())))) {
            token = new StringTokenizer(br.readLine());
            token.nextToken();

            int user2 = Integer.parseInt(token.nextToken());
            int nice2 = Integer.parseInt(token.nextToken());
            int sys2 = Integer.parseInt(token.nextToken());
            int idle2 = Integer.parseInt(token.nextToken());

            return (float) ((user2 + sys2 + nice2) - (user1 + sys1 + nice1)) / (float) ((user2 + nice2 + sys2 + idle2) - (user1 + nice1 + sys1 + idle1));
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

    }
}