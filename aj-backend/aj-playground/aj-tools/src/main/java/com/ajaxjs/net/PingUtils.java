package com.ajaxjs.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.*;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ping ip 工具类
 */
public class PingUtils {
    public static final String LINUX = "Linux";
    public static final String WINDOWS = "Windows";

    /**
     * 检测 ip 和 端口 是否能连接
     *
     * @param host
     * @param port
     * @param timeOut 多少毫秒超时
     * @return
     */
    public static boolean connect(String host, int port, int timeOut) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), timeOut);
            //boolean res = socket.isConnected();//通过现有方法查看连通状态
        } catch (IOException e) {
            //当连不通时，直接抛异常，异常捕获即可
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * windows 和linux 总和
     *
     * @param ipAddress ip
     * @param pingTimes ping的次数
     * @param timeOut   多少秒超时
     * @return
     */
    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        Properties props = System.getProperties(); //获得系统属性集
        String osName = props.getProperty("os.name"); //操作系统名称
        String osArch = props.getProperty("os.arch"); //操作系统构架
        String osVersion = props.getProperty("os.version"); //操作系统版本

        System.out.println(osName);
        System.out.println(osArch);
        System.out.println(osVersion);
        boolean result = false;

        if (osName.contains(LINUX))
            result = pingForLinux(ipAddress, pingTimes, timeOut);
        else if (osName.contains(WINDOWS))
            result = pingForWindows(ipAddress, pingTimes, timeOut);

        return result;
    }

    /**
     * windows 相当于cmd运行 ping 127.0.0.1 -n 5 -w 3000
     *
     * @param ipAddress ip
     * @param pingTimes ping的次数
     * @param timeOut   多少秒超时
     * @return
     */
    public static boolean pingForWindows(String ipAddress, int pingTimes, int timeOut) {
        String pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut * 1000;

        try {
            Process p = Runtime.getRuntime().exec(pingCommand);// 执行命令并获取输出
            try (InputStreamReader inputStreamReader = new InputStreamReader(p.getInputStream());
                 BufferedReader in = new BufferedReader(inputStreamReader)) {
                // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
                int connectedCount = 0;
                String line;

                while ((line = in.readLine()) != null)
                    connectedCount += getCheckResultForWindows(line);

                return connectedCount == pingTimes;// 出现的次数=测试次数则返回真
            }
        } catch (Exception e) {
            e.printStackTrace(); // 出现异常则返回假
            return false;
        }
    }


    /**
     * 相当于 Linux执行  timeout 10s ping 192.168.1.124 -c 5 -i 0
     *
     * @param ipAddress ip
     * @param pingTimes ping的次数
     * @param timeOut   多少秒超时
     * @return
     */
    public static boolean pingForLinux(String ipAddress, int pingTimes, int timeOut) {
        String pingCommandStr = "ping " + ipAddress + " -c " + pingTimes;

        try {
            Process process = Runtime.getRuntime().exec(pingCommandStr);
            try (InputStreamReader r = new InputStreamReader(process.getInputStream())) {
                LineNumberReader returnData = new LineNumberReader(r);
                int connectedCount = 0;
                String line = "";

                while ((line = returnData.readLine()) != null)
                    connectedCount += getCheckResultForLinux(line);

                //log.info("connectedCount===="+connectedCount);
                return connectedCount == pingTimes; // 出现的次数=测试次数则返回真
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
    private static int getCheckResultForWindows(String line) {  // System.out.println("控制台输出的结果为:"+line);
        Matcher matcher = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE).matcher(line);

        while (matcher.find())
            return 1;

        return 0;
    }

    // 若line含有ttl=64 time=0.015 ms字样,说明已经ping通,返回1,否則返回0.
    public static int getCheckResultForLinux(String line) {  // System.out.println("控制台输出的结果为:"+line);
        Matcher matcher = Pattern.compile("(ttl=\\d+)(\\s+)(time=+\\w)", Pattern.CASE_INSENSITIVE).matcher(line);

        while (matcher.find())
            return 1;

        return 0;
    }

///** Linux start **/
//[root@iZuf6g20pbfuqtc2santjkZ ~]# ping -c 5 -i 0  127.0.0.1
//    PING 127.0.0.1 (127.0.0.1) 56(84) bytes of data.
//64 bytes from 127.0.0.1: icmp_seq=1 ttl=64 time=0.015 ms
//64 bytes from 127.0.0.1: icmp_seq=2 ttl=64 time=0.003 ms
//64 bytes from 127.0.0.1: icmp_seq=3 ttl=64 time=0.002 ms
//64 bytes from 127.0.0.1: icmp_seq=4 ttl=64 time=0.002 ms
//64 bytes from 127.0.0.1: icmp_seq=5 ttl=64 time=0.002 ms
//
//--- 127.0.0.1 ping statistics ---
//            5 packets transmitted, 5 received, 0% packet loss, time 0ms
//    rtt min/avg/max/mdev = 0.002/0.004/0.015/0.005 ms, ipg/ewma 0.021/0.009 ms
//[root@iZuf6g20pbfuqtc2santjkZ ~]# ping -c 5 -i 0 192.168.1.2
//    PING 192.168.1.2 (192.168.1.2) 56(84) bytes of data.
//
//--- 192.168.1.2 ping statistics ---
//            5 packets transmitted, 0 received, 100% packet loss, time 59ms
///** Linux end **/
//
///** windows  start **/
//
//C:\Users\Administrator>ping -n 5 -w 1000 192.168.1.1
//
//正在 Ping 192.168.1.1 具有 32 字节的数据:
//来自 192.168.1.1 的回复: 字节=32 时间<1ms TTL=64
//来自 192.168.1.1 的回复: 字节=32 时间<1ms TTL=64
//来自 192.168.1.1 的回复: 字节=32 时间<1ms TTL=64
//来自 192.168.1.1 的回复: 字节=32 时间<1ms TTL=64
//来自 192.168.1.1 的回复: 字节=32 时间<1ms TTL=64
//
//192.168.1.1 的 Ping 统计信息:
//数据包: 已发送 = 5，已接收 = 5，丢失 = 0 (0% 丢失)，
//往返行程的估计时间(以毫秒为单位):
//最短 = 0ms，最长 = 0ms，平均 = 0ms
//
//C:\Users\Administrator>ping -n 5 -w 1000 192.168.2.1
//
//正在 Ping 192.168.2.1 具有 32 字节的数据:
//请求超时。
//请求超时。
//请求超时。
//请求超时。
//请求超时。
//
//        192.168.2.1 的 Ping 统计信息:
//数据包: 已发送 = 5，已接收 = 0，丢失 = 5 (100% 丢失)，
///** windows  end **/
}