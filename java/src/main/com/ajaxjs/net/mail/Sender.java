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
package com.ajaxjs.net.mail;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.ajaxjs.Constant;
import com.ajaxjs.util.LogHelper;

/**
 * 参考 http://fuyanqing03.iteye.com/blog/796860
 * http://www.quepublishing.com/articles/article.aspx?p=26672&seqNum=4
 * http://blog.csdn.net/jia20003/article/details/7358991
 * http://blog.csdn.net/jia20003/article/details/7380142
 * JavaMail邮件发送-能发送附件和带背景音乐的邮件的小系统 http://cuisuqiang.iteye.com/blog/1750866
 * 
 * @author frank
 *
 */
public class Sender extends Socket {
	private static final LogHelper LOGGER = LogHelper.getLog(Sender.class);

	/**
	 * 
	 * @param bean
	 *            邮件实体
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public Sender(Mail bean) throws UnknownHostException, IOException {
		super(bean.getMailServer(), bean.getPort());
		this.bean = bean;
	}

	private Mail bean;

	private static final int ok_250_Code = 250;

	private BufferedReader in; // 接受指令用的缓冲区

	private DataOutputStream os; // 发送指令用的流

	/**
	 * 发送邮件
	 * 
	 * @return
	 * @throws MailException
	 */
	public boolean sendMail() throws MailException {

		try (BufferedReader in = new BufferedReader(new InputStreamReader(getInputStream()));
				DataOutputStream os = new DataOutputStream(getOutputStream());) {
			this.in = in;
			this.os = os;

			String result = in.readLine();// 初始化连接
			if (!isOkCode(result, 220)) 
				throw new MailException("初始化连接：" + result, 220);

			// 进行握手
			result = sendCommand("HELO %s", bean.getMailServer());
			if (!isOkCode(result, ok_250_Code)) 
				throw new MailException("握手失败：" + result, ok_250_Code);

			// 验证发信人信息
			result = sendCommand("AUTH LOGIN");
			if (!isOkCode(result, 334)) 
				throw new MailException("验证发信人信息失败：" + result, 334);

			result = sendCommand(toBase64(bean.getAccount()));
			if (!isOkCode(result, 334)) 
				throw new MailException("发信人名称发送失败：" + result, 334);

			result = sendCommand(toBase64(bean.getPassword()));
			if (!isOkCode(result, 235)) 
				throw new MailException("認証不成功" + result, 235);

			// 发送指令
			result = sendCommand("Mail From:<%s>", bean.getFrom());
			if (!isOkCode(result, ok_250_Code)) 
				throw new MailException("发送指令 From 不成功" + result, ok_250_Code);// 235?

			result = sendCommand("RCPT TO:<%s>", bean.getTo());
			if (!isOkCode(result, ok_250_Code)) 
				throw new MailException("发送指令 To 不成功" + result, ok_250_Code);

			result = sendCommand("DATA");
			if (!isOkCode(result, 354)) 
				throw new MailException("認証不成功" + result, 354);

			result = sendCommand(data());
			if (!isOkCode(result, ok_250_Code)) 
				throw new MailException("发送邮件失败：" + result, ok_250_Code);

			result = sendCommand("QUIT");// quit
			if (!isOkCode(result, 221)) 
				throw new MailException("QUIT 失败：" + result, 221);
			
		} catch (UnknownHostException e) {
			LOGGER.warning("初始化 失败！建立连接失败！", e);
			return false;
		} catch (IOException e) {
			LOGGER.warning("初始化 失败！读取流失败！", e);
			return false;
		} finally {
			try {
				close();
			} catch (IOException e) {
				LOGGER.warning(e);
			}
		}

		return true;
	}

	/**
	 * 生成正文 
	 * @return
	 */
	private String data() {
		StringBuilder sb = new StringBuilder();
		sb.append("From:<" + bean.getFrom() + ">" + Constant.lineFeet);
		sb.append("To:<" + bean.getTo() + ">" + Constant.lineFeet);
		sb.append("Subject:=?UTF-8?B?" + toBase64(bean.getSubject()) + "?=" + Constant.lineFeet);
		sb.append("Date:2016/10/27 17:30" + Constant.lineFeet);
//		sb.append("MIME-Version: 1.0" + Constant.lineFeet);
		sb.append((bean.isHTML_body() ? "Content-Type:text/html;charset=\"utf-8\"" : "Content-Type:text/plain;charset=\"utf-8\"") + Constant.lineFeet);
		sb.append("Content-Transfer-Encoding: base64" + Constant.lineFeet);
		sb.append(Constant.lineFeet);
		sb.append(toBase64(bean.getContent()));
		sb.append(Constant.lineFeet + ".");
		
		return sb.toString();
	}

	private String sendCommand(String string, String from) {
		return sendCommand(String.format(string, from));
	}

	/**
	 * 发送smtp指令 并返回服务器响应信息
	 * 
	 * @param msg
	 *            指令，会在字符串后面自动加上 Constant.lineFeet
	 * @return
	 */
	private String sendCommand(String msg) {
		try {
			os.writeBytes(msg + Constant.lineFeet);
			os.flush();
			return in.readLine(); // 读取服务器端响应信息
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	public static String toBase64(String str) {
		return new String(Base64.encode(str.getBytes()));
	}
	
	/**
	 * 
	 * @param str
	 * @param code
	 * @return
	 */
	private static boolean isOkCode(String str, int code) {
		int _code = 0;

		Pattern p = Pattern.compile("^\\d+");
		Matcher m = p.matcher(str);
		while (m.find()) {
			_code = Integer.parseInt(m.group(0));
			break;
		}

		return _code == code;
	}
}
