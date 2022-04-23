package com.ajaxjs.net.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.regex.Pattern;

/**
 * 获取本机的外网 IP
 * 
 * @author https://xxxxg.blog.csdn.net/article/details/120505486
 *
 */
public class ExternalIPUtil {
	/**
	 * IP 地址校验的正则表达式
	 */
	private static final Pattern IPV4_PATTERN = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	/**
	 * 获取 IP 地址的服务列表
	 */
	private static final String[] IPV4_SERVICES = {
			// @formatter:off
            "http://checkip.amazonaws.com/",
            "https://ipv4.icanhazip.com/",
            "http://bot.whatismyipaddress.com/"
            // and so on ...
            // @formatter:on
	};

	/**
	 * 获取本机的外网 IP
	 * 
	 * @return
	 */
	public static String get() {
		List<Callable<String>> callables = new ArrayList<>();

		for (String ipService : IPV4_SERVICES)
			callables.add(() -> get(ipService));

		/*
		 * 线程池的 ExecutorService.invokeAny(callables)
		 * 方法用于并发执行多个线程，并拿到最快的执行成功的线程的返回值，只要有一个执行成功，其他失败的任务都会忽略。但是如果全部失败，例如本机根本就没有连外网，
		 * 那么就会抛出 ExecutionException 异常。
		 */
		ExecutorService executorService = Executors.newCachedThreadPool();

		try {
			return executorService.invokeAny(callables);// 返回第一个成功获取的 IP
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}

		return null;
	}

	private static String get(String url) throws IOException {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
			String ip = in.readLine();

			if (IPV4_PATTERN.matcher(ip).matches())
				return ip;
			else
				throw new IOException("invalid IPv4 address: " + ip);
		}
	}
}
