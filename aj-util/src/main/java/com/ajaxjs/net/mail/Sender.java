/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
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

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 简易邮件发送器
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class Sender extends Socket {
	private static final LogHelper LOGGER = LogHelper.getLog(Sender.class);

	/**
	 * 发送一封邮件
	 * 
	 * @param bean 邮件实体
	 * @throws IOException          IO异常
	 * @throws UnknownHostException 未知主机异常
	 */
	public Sender(Mail bean) throws UnknownHostException, IOException {
		super(bean.getMailServer(), bean.getPort());
		this.bean = bean;
	}

	public static final String LINEFEET = "\r\n"; // 换行符常量

	private static final int OK_250_CODE = 250;// 成功标识

	private Mail bean; // 邮件信息

	private BufferedReader in; // 接受指令用的缓冲区

	private DataOutputStream os; // 发送指令用的流

	/**
	 * 发送邮件
	 * 
	 * @return 是否成功
	 * @throws MailException 邮件异常
	 */
	public boolean sendMail() throws MailException {
		LOGGER.info("发送邮件:" + bean.getSubject());

		try (BufferedReader in = new BufferedReader(new InputStreamReader(getInputStream())); DataOutputStream os = new DataOutputStream(getOutputStream());) {
			this.in = in;
			this.os = os;

			String result = in.readLine();// 初始化连接
			if (!isOkCode(result, 220))
				throw new MailException("初始化连接：" + result, 220);

			// 进行握手
			result = sendCommand("HELO %s", bean.getMailServer());
			if (!isOkCode(result, OK_250_CODE))
				throw new MailException("握手失败：" + result, OK_250_CODE);

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
			if (!isOkCode(result, OK_250_CODE))
				throw new MailException("发送指令 From 不成功" + result, OK_250_CODE);// 235?

			result = sendCommand("RCPT TO:<%s>", bean.getTo());
			if (!isOkCode(result, OK_250_CODE))
				throw new MailException("发送指令 To 不成功" + result, OK_250_CODE);

			result = sendCommand("DATA");
			if (!isOkCode(result, 354))
				throw new MailException("認証不成功" + result, 354);

			result = sendCommand(data());
			if (!isOkCode(result, OK_250_CODE))
				throw new MailException("发送邮件失败：" + result, OK_250_CODE);

			result = sendCommand("QUIT");// quit
			if (!isOkCode(result, 221))
				throw new MailException("QUIT 失败：" + result, 221);

		} catch (UnknownHostException e) {
			System.err.println("初始化 失败！建立连接失败！");
			LOGGER.warning(e);
			return false;
		} catch (IOException e) {
			System.err.println("初始化 失败！读取流失败！");
			LOGGER.warning(e);
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
	 * 
	 * @return 正文
	 */
	private String data() {
		StringBuilder sb = new StringBuilder();
		sb.append("From:<" + bean.getFrom() + ">" + LINEFEET);
		sb.append("To:<" + bean.getTo() + ">" + LINEFEET);
		sb.append("Subject:=?UTF-8?B?" + toBase64(bean.getSubject()) + "?=" + LINEFEET);
		sb.append("Date:2016/10/27 17:30" + LINEFEET);
		// sb.append("MIME-Version: 1.0" + lineFeet);
		sb.append((bean.isHTML_body() ? "Content-Type:text/html;charset=\"utf-8\"" : "Content-Type:text/plain;charset=\"utf-8\"") + LINEFEET);
		sb.append("Content-Transfer-Encoding: base64" + LINEFEET);
		sb.append(LINEFEET);
		sb.append(toBase64(bean.getContent()));
		sb.append(LINEFEET + ".");

		return sb.toString();
	}

	/**
	 * 发送smtp指令 并返回服务器响应信息
	 * 
	 * @param string 指令
	 * @param from   指令参数
	 * @return 服务器响应信息
	 */
	private String sendCommand(String string, String from) {
		return sendCommand(String.format(string, from));
	}

	/**
	 * 发送smtp指令 并返回服务器响应信息
	 * 
	 * @param msg 指令，会在字符串后面自动加上 lineFeet
	 * @return 服务器响应信息
	 */
	private String sendCommand(String msg) {
		try {
			os.writeBytes(msg + LINEFEET);
			os.flush();
			return in.readLine(); // 读取服务器端响应信息
		} catch (IOException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * Base64 编码的一种实现
	 * 
	 * @param str 待编码的字符串
	 * @return 已编码的字符串
	 */
	public static String toBase64(String str) {
		return StrUtil.base64Encode(str);
	}

	/**
	 * 输入期望 code，然后查找字符串中的数字，看是否与之匹配。匹配则返回 true。
	 * 
	 * @param str  输入的字符串，应该要包含数字
	 * @param code 期望值
	 * @return 是否与之匹配
	 */
	private static boolean isOkCode(String str, int code) {
		int _code = Integer.parseInt(StrUtil.regMatch("^\\d+", str));

		return _code == code;
	}

	/**
	 * 发送邮件
	 * 
	 * @param mail 服务器信息和邮件信息
	 * @return true 表示为发送成功，否则为失败
	 */
	public static boolean send(Mail mail) {
		try (Sender sender = new Sender(mail)) {
			return sender.sendMail();
		} catch (IOException | MailException e) {
			LOGGER.warning(e);
			return false;
		}
	}
}
