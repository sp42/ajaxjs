/**
 * Copyright sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.net.http;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 图片批量下载
 * 
 * @author Frank Cheung
 *
 */
public class PicDownload {
	public CountDownLatch latch;
	private String[] arr;
	private String saveFolder;
	private Supplier<String> newFileNameFn;

	/**
	 * 
	 * @param arr
	 * @param saveFolder
	 * @param newFileNameFn How to specify a new file name, if it is null then use
	 * old name
	 */
	public PicDownload(String[] arr, String saveFolder, Supplier<String> newFileNameFn) {
		latch = new CountDownLatch(arr.length);
		this.arr = arr;
		this.saveFolder = saveFolder;
		this.newFileNameFn = newFileNameFn;
	}

	/**
	 * Download
	 * 
	 * @param url
	 * @param i
	 */
	public void exec(String url, int i) {
		String newFileName;

		try {
//			System.out.println(Thread.currentThread().getName() + " 开始下载");
			if (newFileNameFn == null) {
				newFileName = NetUtil.download(url, saveFolder);
			} else {
				newFileName = NetUtil.download(url, saveFolder, newFileNameFn.get());
			}

//			System.out.println(Thread.currentThread().getName() + " 下载完成");
			String[] _arr = newFileName.split("\\\\");
			String f = _arr[_arr.length - 1];
			arr[i] = f;
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
		String[] testArr = new String[] { "https://bbsimage.res.meizu.com/forum/2019/01/23/153122zrz85kuvubbiibbs.jpg", "http://531.yishu1000.com/201906/004/1.jpg", "http://531.yishu1000.com/201906/004/2.jpg",
				"http://531.yishu1000.com/201906/004/3.jpg" };

		new PicDownload(testArr, "c:/temp", null).start();

		System.out.println("全部完成" + Arrays.toString(testArr));
	}
}
