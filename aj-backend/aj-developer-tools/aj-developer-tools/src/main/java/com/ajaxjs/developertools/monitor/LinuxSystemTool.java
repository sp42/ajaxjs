package com.ajaxjs.developertools.monitor;

import com.ajaxjs.util.ObjectHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * @author <a href="https://blog.csdn.net/hj7jay/article/details/51979939">...</a>
 */
public class LinuxSystemTool {
    /**
     * 内存的信息 get memory by used info
     *
     * @return int[] result
     * result.length==4;int[0]=MemTotal;int[1]=MemFree;int[2]=SwapTotal;int[3]=SwapFree;
     */
    public static int[] getMemInfo() {
        File file = new File("/proc/meminfo");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())))) {
            int[] result = new int[4];
            String str;
            StringTokenizer token;

            while ((str = br.readLine()) != null) {
                token = new StringTokenizer(str);
                if (!token.hasMoreTokens()) continue;

                str = token.nextToken();
                if (!token.hasMoreTokens()) continue;

                if (str.equalsIgnoreCase("MemTotal:")) result[0] = Integer.parseInt(token.nextToken());
                else if (str.equalsIgnoreCase("MemFree:")) result[1] = Integer.parseInt(token.nextToken());
                else if (str.equalsIgnoreCase("SwapTotal:")) result[2] = Integer.parseInt(token.nextToken());
                else if (str.equalsIgnoreCase("SwapFree:")) result[3] = Integer.parseInt(token.nextToken());
            }

            Arrays.toString(result);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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