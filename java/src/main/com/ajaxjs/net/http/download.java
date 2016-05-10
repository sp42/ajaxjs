package com.ajaxjs.net.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class download {
	public static void down(String URL, long nPos, String savePathAndFile) {
		try {
			HttpURLConnection httpConnection = (HttpURLConnection) (new URL(URL)).openConnection();
			// 设置User-Agent
			httpConnection.setRequestProperty("User-Agent", "NetFox");
			// 设置断点续传的开始位置
			httpConnection.setRequestProperty("RANGE", "bytes=" + nPos);

			RandomAccessFile oSavedFile = new RandomAccessFile(savePathAndFile, "rw");

			// 定位文件指针到nPos位置
			oSavedFile.seek(nPos);
			byte[] b = new byte[1024];
			int nRead;

			// 获得输入流
			try (InputStream input = httpConnection.getInputStream();) {
				// 从输入流中读入字节流，然后写到文件中
				while ((nRead = input.read(b, 0, 1024)) > 0) {
					oSavedFile.write(b, 0, nRead);
				}
			}

			httpConnection.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取远程资源的大小
	 * @param url
	 * @return
	 */
	public static long getRemoteSize(String url) {
		long size = 0;

		try {
			HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
			size = conn.getContentLength();
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return size;
	}
	
	/**
	 * 基本原理：利用URLConnection获取要下载文件的长度、头部等相关信息，并设置响应的头部信息。并且通过URLConnection获取输入流，将文件分成指定的块，每一块单独开辟一个线程完成数据的读取、写入。通过输入流读取下载文件的信息，然后将读取的信息用RandomAccessFile随机写入到本地文件中。同时，每个线程写入的数据都文件指针也就是写入数据的长度，需要保存在一个临时文件中。这样当本次下载没有完成的时候，下次下载的时候就从这个文件中读取上一次下载的文件长度，然后继续接着上一次的位置开始下载。并且将本次下载的长度写入到这个文件中。 
	 * Java 多线程断点下载文件 http://blog.csdn.net/ycb1689/article/details/8229585
	 */
	public void multi_line_download(){}

	public static void main(String[] args) {
		String url = "http://www.videosource.cgogo.com/media/0/16/8678/8678.flv";
		String savePath = "F:\\";
		String fileName = url.substring(url.lastIndexOf("/"));
		String fileNam = fileName;
		HttpURLConnection conn = null;

		try {
			conn = (HttpURLConnection) (new URL(url)).openConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		File file = new File(savePath + fileName);
		// 获得远程文件大小
		long remoteFileSize = getRemoteSize(url);
		System.out.println("远程文件大小=" + remoteFileSize);

		int i = 0;
		if (file.exists()) {
			// 先看看是否是完整的，完整，换名字，跳出循环，不完整，继续下载
			long localFileSize = file.length();
			System.out.println("已有文件大小为:" + localFileSize);

			if (localFileSize < remoteFileSize) {
				System.out.println("文件续传");
				down(url, localFileSize, savePath + fileName);
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
				down(url, 0, savePath + fileName);
			}
			// 下面表示文件存在，改名字

		} else {
			try {
				file.createNewFile();
				System.out.println("下载中");
				down(url, 0, savePath + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
