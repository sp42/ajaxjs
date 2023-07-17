package com.ajaxjs.mysql.model;

import java.net.HttpURLConnection;
import java.net.UnknownHostException;

import com.ajaxjs.mysql.common.CommonUtils;
import com.ajaxjs.mysql.config.MyPerfContext;

import lombok.Data;

/**
 * Add Hipchat integration. So far it is only for alert notification purpose. We
 * can add command notification in the future.
 *
 * @author xrao
 */
@Data
public class HipchatIntegration {
	private String hipchatURL; // must be in the format like
								// https://xxx.hipchat.com/v2/room/{roomnumber}/notification?
	private String authToken; // we will append authToken to construct the full url
	private String hostname;
	private boolean enabled = false;

	public void init(MyPerfContext ctx) {
		this.enabled = false;
		try {
			this.hostname = java.net.InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		this.hipchatURL = ctx.getMyperfConfig().getHipchatUrl();
		this.authToken = ctx.getMyperfConfig().getHipchatAuthToken();

		if (authToken != null && !authToken.isEmpty() && hipchatURL != null && !hipchatURL.isEmpty())
			this.enabled = true;

		if (this.enabled) {
			// do a test
			this.enabled = this.sendMessage("MySQL Perf Analzyer Hipchat Integration Initiated on " + this.hostname);
			if (this.enabled)
				System.out.println("Hipchat integration is enabed.");
		}
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * This should be invoked if isEnabled = true
	 */
	public boolean sendMessage(String msg) {
		if (!this.enabled) {
//			logger.info("Hichat is not enabled, ignore.");
			return false;
		}

		java.io.OutputStream out = null;
		java.io.InputStream in = null;
		String url = this.hipchatURL + "auth_token=" + this.authToken;
		try {

			java.net.URL hipchatUrl = new java.net.URL(url);
			HttpURLConnection conn = HttpURLConnection.class.cast(hipchatUrl.openConnection());
			conn.setDoOutput(true);
			conn.addRequestProperty("Content-Type", "application/json");
			String jsonMsg = this.constructJsonMessage("Source: " + this.hostname + "\n" + msg);
			// logger.info("Sending message to hipchat (" + url + "): " + jsonMsg);
			byte[] jsonByte = jsonMsg.getBytes();
			conn.addRequestProperty("Content-Length", String.valueOf(jsonByte.length));
			out = conn.getOutputStream();
			out.write(jsonByte);
			out.flush();
			int code = conn.getResponseCode();
			in = conn.getInputStream();
			// logger.info("Recieve response code " + code);
			if (code >= 200 && code < 400)
				return true;
//			logger.warning("Failed hipchat integration with URL: " + this.hipchatURL + ", code: " + code + ", data: " + jsonMsg);
		} catch (Throwable th) {
//			logger.log(Level.WARNING, "Failed to send message: " + msg, th);
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (Exception ex) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception ex) {
				}
			}
		}

		return false;
	}

	private String constructJsonMessage(String str) {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"color\":\"green\",\"message\":\"");
		sb.append(CommonUtils.escapeJson(str));
		sb.append("\",\"notify\":false,\"message_format\":\"text\"}");
		return sb.toString();
	}
}