package com.ajaxjs.net.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.ajaxjs.net.ftp.sun.TelnetInputStream;
import com.ajaxjs.net.ftp.sun.TelnetOutputStream;
import com.ajaxjs.net.ftp.sun.ftp.FtpClient;

public class UploadFtp extends FtpClient {
	public UploadFtp(String server, int port) throws IOException  {
		super(server, port);
	}

	/**
	 * 用书上传本地文件到ftp服务器上
	 * 
	 * @param source
	 *            上传文件的本地路径
	 * @param target
	 *            上传到ftp的文件路径
	 */
	public void upload(String source, String target) {
		TelnetOutputStream ftp = null;
		InputStream file = null;
		
		try {
			binary();
			
			ftp = put(target);
			file = new FileInputStream(new File(source));
			BufferedInputStream in = new BufferedInputStream(file);
			
			new ProgressListener().copy(in, new BufferedOutputStream(ftp), in.available());
			
			System.out.print("put file suc from " + source + "   to  " + target + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ftp != null)ftp.close();
				if(file != null)file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 从ftp上下载所需要的文件
	 * 
	 * @param source
	 *            在ftp上路径及文件名
	 * @param target
	 *            要保存的本地的路径
	 */
	public void getFile(String source, String target) {
		TelnetInputStream ftp = null;
		OutputStream file = null;
		
		try {
			binary();
			ftp = get(source);
			file = new FileOutputStream(new File(target));
			
			ProgressListener listener = new ProgressListener();
			listener.setFileName(target);
			listener.copy(
				new BufferedInputStream(ftp), 
				new BufferedOutputStream(file), 
				getFileSize(source, ftp)
			);
	
			System.out.print("get file suc from " + source + "   to  " + target + "\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(ftp != null)ftp.close();
				if(file != null)file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 为了计算下载速度和百分比，读取ftp该文件的大小
	 * 
	 * @param source
	 * @param ftp
	 * @return
	 * @throws IOException
	 */
	private int getFileSize(String source, TelnetInputStream ftp) throws IOException {
		// 这里的组合使用是必须得 sendServer 后到 readServerResponse
		sendServer("SIZE " + source + "\r\n");
		
		if (readServerResponse() == 213) {
			String msg = getResponseString();
			
			try {
				return Integer.parseInt(msg.substring(3).trim());
			} catch (Exception e) {}
		}
		
		return 0;
	}
}