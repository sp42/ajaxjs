package com.ajaxjs.monitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * 
 * @author https://blog.csdn.net/hj7jay/article/details/51979939
 *
 */
public class LinuxSystemTool {
	/**
	 * 内存的信息
	 * 
	 * get memory by used info
	 * 
	 * @return int[] result
	 *         result.length==4;int[0]=MemTotal;int[1]=MemFree;int[2]=SwapTotal;int[3]=SwapFree;
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static int[] getMemInfo() throws IOException, InterruptedException {
		File file = new File("/proc/meminfo");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		int[] result = new int[4];
		String str = null;
		StringTokenizer token = null;
		while ((str = br.readLine()) != null) {
			token = new StringTokenizer(str);
			if (!token.hasMoreTokens())
				continue;

			str = token.nextToken();
			if (!token.hasMoreTokens())
				continue;

			if (str.equalsIgnoreCase("MemTotal:"))
				result[0] = Integer.parseInt(token.nextToken());
			else if (str.equalsIgnoreCase("MemFree:"))
				result[1] = Integer.parseInt(token.nextToken());
			else if (str.equalsIgnoreCase("SwapTotal:"))
				result[2] = Integer.parseInt(token.nextToken());
			else if (str.equalsIgnoreCase("SwapFree:"))
				result[3] = Integer.parseInt(token.nextToken());
		}

		br.close();
		Arrays.toString(result);
		return result;
	}

	/**
	 * get memory by used info
	 * 
	 * @return float efficiency
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static float getCpuInfo() throws IOException, InterruptedException {
		File file = new File("/proc/stat");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		StringTokenizer token = new StringTokenizer(br.readLine());
		token.nextToken();
		int user1 = Integer.parseInt(token.nextToken());
		int nice1 = Integer.parseInt(token.nextToken());
		int sys1 = Integer.parseInt(token.nextToken());
		int idle1 = Integer.parseInt(token.nextToken());
		br.close();
		Thread.sleep(1000);

		br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		token = new StringTokenizer(br.readLine());
		token.nextToken();
		int user2 = Integer.parseInt(token.nextToken());
		int nice2 = Integer.parseInt(token.nextToken());
		int sys2 = Integer.parseInt(token.nextToken());
		int idle2 = Integer.parseInt(token.nextToken());

		br.close();

		return (float) ((user2 + sys2 + nice2) - (user1 + sys1 + nice1)) / (float) ((user2 + nice2 + sys2 + idle2) - (user1 + nice1 + sys1 + idle1));
	}
}