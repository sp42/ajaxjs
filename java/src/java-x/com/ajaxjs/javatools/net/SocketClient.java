package com.ajaxjs.javatools.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;

import com.ajaxjs.core.Constant;

/**
 * Socket客户端,向服务端发送消息
 * @author http://blog.csdn.net/5iasp/article/details/4268731
 * Socket 基礎知識
 * http://blog.csdn.net/jia20003/article/details/17104791
 * http://blog.csdn.net/jia20003/article/details/8134666
 * http://blog.csdn.net/jia20003/article/details/8142532
 * http://blog.csdn.net/jia20003/article/details/8195226
 * http://blog.csdn.net/jia20003/article/details/8248221
 * http://blog.csdn.net/jia20003/article/details/8272985
 */
public class SocketClient {
	private static final int MAX_TIMEOUT = 10;

	/**
	 * 向服务端发送消息
	 * @param host 主机Host或IP
	 * @param port  端口
	 * @param timeout 超时,单位秒
	 * @param content  发送内容
	 */
	public static void send(String host, int port, int timeout, String content) {
		Socket s = null;
		PrintWriter out = null;
		
		try {
			s = new Socket(host, port);
			s.setSoTimeout((timeout > MAX_TIMEOUT ? MAX_TIMEOUT : timeout) * 1000);
			out = new PrintWriter(s.getOutputStream());
			out.write(content);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
				}
			if (out != null)out.close();
			s = null;
			out = null;
		}
	}

	/**
	 * 向SocketServer发送通信指令并获取回复数据
	 * @param host 主机名称或IP
	 * @param port  端口
	 * @param timeout 超时时间(秒)
	 * @param content 指令内容
	 * @return
	 */
	public static String sendAndGetReply(String host, int port, int timeout,String content) {
		String encode = Constant.encoding_UTF8;
		Socket s = null;
		BufferedReader in = null;
		PrintWriter out = null;
		String line = null;
		
		try {
			content = URLEncoder.encode(content, encode);
			s = new Socket(host, port);
			s.setSoTimeout((timeout > MAX_TIMEOUT ? MAX_TIMEOUT : timeout) * 1000);
			in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
			out.println(content);
			line = in.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
				}
			if (out != null)out.close();
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
			s = null;
			out = null;
			in = null;
		}
		
		try {
			line = URLEncoder.encode(line, encode);
		} catch (UnsupportedEncodingException e) {
		}
		return line;
	}

	/**
	 * 向SocketServer发送通信指令,无同步回复消息
	 * @param host 主机名称或IP
	 * @param port 端口
	 * @param timeout  超时时间(秒)
	 * @param content  指令内容
	 * @return
	 */
	public static void sendAndNoReply(String host, int port, int timeout,String content) {
		String encode = "utf-8";
		Socket s = null;
		PrintWriter out = null;
		
		try {
			content = URLEncoder.encode(content, encode);
			s = new Socket(host, port);
			s.setSoTimeout((timeout > MAX_TIMEOUT ? MAX_TIMEOUT : timeout) * 1000);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
			out.println(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
				}
			if (out != null)
				out.close();
			s = null;
			out = null;
		}
	}
}