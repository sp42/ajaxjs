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

/**
 * 邮件模型
 * 
 * @author sp42 frank@ajaxjs.com
 */
public class Mail {
	private String mailServer;
	private String from;
	private String to;
	private String content;
	private String account;
	private String password;
	private String subject;
	private boolean isHTML_body;
	private int port = 25;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMailServer() {
		if (mailServer == null)
			throw new IllegalArgumentException("没有指定 MailServer！");

		return mailServer;
	}

	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}

	public String getFrom() {
		if (from == null)
			throw new IllegalArgumentException("没有指定发件人！");

		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		if (to == null)
			throw new IllegalArgumentException("没有指定收件人！");

		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public boolean isHTML_body() {
		return isHTML_body;
	}

	public void setHTML_body(boolean isHTML_body) {
		this.isHTML_body = isHTML_body;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
