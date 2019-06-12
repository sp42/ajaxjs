package com.ajaxjs.net.http;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PicDownload {
	public CountDownLatch latch;
	private String[] arr;
	private String saveFolder;
	public PicDownload(String[] arr, String saveFolder) {
		latch = new CountDownLatch(arr.length);
		this.arr = arr;
		this.saveFolder = saveFolder;
	}
	
	public void exec(String url, int i) {
		try {
//			System.out.println(Thread.currentThread().getName() + " 开始下载");
			String newFileName = NetUtil.download(url, saveFolder); // 下载图片
//			System.out.println(Thread.currentThread().getName() + " 下载完成");
			arr[i] = newFileName;
		} finally {
			latch.countDown();// 每个子线程中，不管是否成功，是否有异常
		}
	}
	
	public void start() {
		for (int i = 0; i < arr.length; i++) {
			final int j = i;
			new Thread(() -> exec(arr[j], j)).start();
		}
		
		try {
			latch.await(20, TimeUnit.SECONDS); // 给主线程设置一个最大等待超时时间 20秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		String[] testArr = new String[] { 
				"https://bbsimage.res.meizu.com/forum/2019/01/23/153122zrz85kuvubbiibbs.jpg", 
				"http://531.yishu1000.com/201906/004/1.jpg",
				"http://531.yishu1000.com/201906/004/2.jpg",
				"http://531.yishu1000.com/201906/004/3.jpg" };
		
		new PicDownload(testArr, "c:/temp").start();
	
		System.out.println("全部完成" + Arrays.toString(testArr));
	}
}
