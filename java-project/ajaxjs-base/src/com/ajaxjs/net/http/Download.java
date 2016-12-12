/**
 * Copyright 2015 Frank Cheung
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.io.FileUtil;

/**
 * HTTP GET 请求
 * @author frank
 *
 */
public class Download {
	private static final LogHelper LOGGER = LogHelper.getLog(Download.class);
 
	
	/**
	 * 下载任意 web 文件到本地 
	 * 
	 * @param url
	 *            目标地址
	 * @param filePathName
	 *            保存本地的完整路径
	 */
	public static void download2disk(String url, final String filePathName) {
		final Client client = new Client(url);
		
		try {
			client.setCallback(new Request.Callback<Client>() {
				@Override
				public void onDataLoad(InputStream is) {
//					try {
						if ("gzip".equals(client.getConnection().getHeaderField("Content-Encoding"))) {
//							System.out.println(FileUtil.readText(new GZIPInputStream(is)));
//							FileUtil.readText(new GZIPInputStream(is));
//							FileUtil.write(new GZIPInputStream(is), new FileOutputStream(filePathName), true); // 自动处理图片是否经过服务器gzip压缩的问题
						} else {
//							FileUtil.write(is, new FileOutputStream(filePathName), true);
						}
//					} catch (IOException e) {
//						LOGGER.warning(e);
//					}
				}
			}).connect();
		} catch (ConnectException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 断点下载文件
	 * 
	 * @param url
	 *            目标地址
	 * @param filePathName
	 *            保存本地的完整路径
	 * @param startPos
	 *            开始位置
	 */
	public static void download2disk(String url, String filePathName, long startPos) {
		try {
			HttpURLConnection httpConnection = (HttpURLConnection) (new URL(url)).openConnection();
			// 设置User-Agent
			httpConnection.setRequestProperty("User-Agent", "NetFox");
			// 设置断点续传的开始位置
			httpConnection.setRequestProperty("RANGE", "bytes=" + startPos);
	
			byte[] b = new byte[1024];
			int nRead;
	
			// 获得输入流
			try (RandomAccessFile oSavedFile = new RandomAccessFile(filePathName, "rw");
				InputStream input = httpConnection.getInputStream();) {
				oSavedFile.seek(startPos);	// 定位文件指针到nPos位置
				
				while ((nRead = input.read(b, 0, 1024)) > 0) { // 从输入流中读入字节流，然后写到文件中
					oSavedFile.write(b, 0, nRead);
				}
			}
	
			httpConnection.disconnect();
	 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 基本原理：利用URLConnection获取要下载文件的长度、头部等相关信息，并设置响应的头部信息。并且通过URLConnection获取输入流，将文件分成指定的块，每一块单独开辟一个线程完成数据的读取、写入。通过输入流读取下载文件的信息，然后将读取的信息用RandomAccessFile随机写入到本地文件中。同时，每个线程写入的数据都文件指针也就是写入数据的长度，需要保存在一个临时文件中。这样当本次下载没有完成的时候，下次下载的时候就从这个文件中读取上一次下载的文件长度，然后继续接着上一次的位置开始下载。并且将本次下载的长度写入到这个文件中。 
	 * Java 多线程断点下载文件 http://blog.csdn.net/ycb1689/article/details/8229585
	 * TODO
	 */
	public static void multi_line_download(){}

	/**
	 * 大文件断点续传下载
	 */
	public static void bigDown() {
		String url = "http://www.videosource.cgogo.com/media/0/16/8678/8678.flv";
		String savePath = "F:\\";
		String fileName = url.substring(url.lastIndexOf("/"));
		String fileNam = fileName;
	
		File file = new File(savePath + fileName);
		// 获得远程文件大小
		long remoteFileSize = Client.getRemoteSize(url);
		System.out.println("远程文件大小=" + remoteFileSize);
	
		int i = 0;
		if (file.exists()) {
			// 先看看是否是完整的，完整，换名字，跳出循环，不完整，继续下载
			long localFileSize = file.length();
			System.out.println("已有文件大小为:" + localFileSize);
	
			if (localFileSize < remoteFileSize) {
				System.out.println("文件续传");
				download2disk(url, savePath + fileName, localFileSize);
			} else {
				System.out.println("文件存在，重新下载");
				do {
					i++;
					fileName = fileNam.substring(0, fileNam.indexOf(".")) + "(" + i + ")"
							+ fileNam.substring(fileNam.indexOf("."));
	
					file = new File(savePath + fileName);
				} while (file.exists());
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				download2disk(url, savePath + fileName, 0);
			}
			// 下面表示文件存在，改名字
	
		} else {
			try {
				file.createNewFile();
				System.out.println("下载中");
				download2disk(url, savePath + fileName, 0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
