/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.snmp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

import org.springframework.util.StringUtils;

import com.ajaxjs.mysql.config.MyPerfContext;
import com.ajaxjs.util.logger.LogHelper;

import lombok.Data;

/**
 * Store SNMP settings for community, version, etc.
 */
@Data
public class SNMPSettings implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private static final LogHelper LOGGER = LogHelper.getLog(SNMPSettings.class);

	private SNMPSetting siteSetting;// default site setting, if provided
	// group level setting, if any. The key is dbGroupName
	private Map<String, SNMPSetting> groupSettings = new HashMap<>();
	// host level settings, if any. The key is dbGroupName+"|"+hostName
	private Map<String, SNMPSetting> hostSettings = new HashMap<>();
	private String myperfSnmpConfigPath;
	private String rootPath = "myperf_config";
	private String SNMPPATH = "snmp";
	private MyPerfContext frameworkContext;

	public SNMPSettings() {
	}

	public void setGroupSetting(String dbGroup, SNMPSetting groupSetting) {
		this.groupSettings.put(dbGroup, groupSetting);
	}

	public void setHostSetting(String dbGroupName, String hostName, SNMPSetting hostSetting) {
		this.hostSettings.put(dbGroupName + "|" + hostName, hostSetting);
	}

	/**
	 * @param dbGroupHostName dbGroupName+"|"+hostName
	 * @param hostSetting
	 */
	public void setHostSetting(String dbGroupHostName, SNMPSetting hostSetting) {
		hostSettings.put(dbGroupHostName, hostSetting);
	}

	public SNMPSetting getGroupSetting(String dbGroupName) {
		if (groupSettings.containsKey(dbGroupName))
			return groupSettings.get(dbGroupName);

		return siteSetting;
	}

	public SNMPSetting getHostSetting(String dbgroup, String host) {
		if ((!StringUtils.hasText(dbgroup)) && (!StringUtils.hasText(host)))
			return siteSetting;

		if (hostSettings.containsKey(dbgroup + "|" + host))
			return hostSettings.get(dbgroup + "|" + host);
		else
			return getGroupSetting(dbgroup);
	}

	public boolean updateSnmpSetting(String dbgroup, String host, String community, String ver, String username, String password, String authprotocol,
			String privacypassphrase, String privacyprotocol, String context, String enabled) {

		SNMPSetting setting = new SNMPSetting(community, ver);

		if ("3".equals(ver)) {
			setting.setUsername(username);
			setting.setPassword(password);
			setting.setAuthProtocol(authprotocol);
			setting.setPrivacyPassphrase(privacypassphrase);
			setting.setPrivacyProtocol(privacyprotocol);
			setting.setContext(context);
		}

		setting.setEnabled("no".equalsIgnoreCase(enabled) ? "no" : "yes");

		if ((!StringUtils.hasText(dbgroup)) && (!StringUtils.hasText(host))) {
			siteSetting = setting;
			LOGGER.info("Add snmp setting for site, version " + setting.getVersion());
		} else if (!StringUtils.hasText(host)) {
			setGroupSetting(dbgroup, setting);
			LOGGER.info("Add snmp setting for group" + dbgroup + ", version " + setting.getVersion());
		} else {
			setHostSetting(dbgroup, host, setting);
			LOGGER.info("Add snmp setting for host (" + dbgroup + ", " + host + ") version " + setting.getVersion());
		}

		return this.store();
	}

	public void init(MyPerfContext ctx) {
		LOGGER.info("Read customized SNMP configuration ...");

		this.frameworkContext = ctx;
		File snmproot = new File(new File(rootPath), SNMPPATH);

		if (!snmproot.exists())
			snmproot.mkdirs();

		myperfSnmpConfigPath = snmproot.getAbsolutePath() + File.separatorChar + "snmp.json";
		this.readConfig();
	}

	/**
	 * 将配置信息存储到指定的文件中
	 * 
	 * @return 返回false表示存储配置失败
	 */
	synchronized public boolean store() {
		File cfgFile = new File(myperfSnmpConfigPath);

		if (!cfgFile.exists())
			LOGGER.info("There is no customized snmp configuration, create one.");

		try (Writer pw = new FileWriter(cfgFile)) {
			return writeToJson(pw);
		} catch (IOException ex) {
			LOGGER.warning("Failed to store configurations to file " + myperfSnmpConfigPath, ex);
		}

		return false;
	}

	/**
	 * 从指定的配置文件中读取配置信息，并将解析后的配置信息存储到相应的数据结构中
	 * 
	 * @return 返回true表示成功读取配置；返回false，表示读取配置失败
	 */
	synchronized private boolean readConfig() {
		File cfgFile = new File(myperfSnmpConfigPath); // 创建配置文件对象

		if (!cfgFile.exists()) { // 如果配置文件不存在
			LOGGER.info("There is no customized snmp configuration file");
			return true; // 返回true表示成功读取配置
		}

		try (FileInputStream in = new FileInputStream(cfgFile)) {
			// 使用JsonReader读取输入流中的JSON数据
			JsonReader jr = javax.json.Json.createReader(in);
			JsonObject jsonObject = jr.readObject();
			jr.close();

			setSiteSetting(readFromJson(jsonObject.getJsonObject("site"))); // 从JSON对象中读取"site"字段的配置信息，并设置到当前对象中
			JsonArray groups = jsonObject.getJsonArray("group"); // 获取JSON数组"group"

			if (groups != null) {
				for (int i = 0; i < groups.size(); i++) {
					JsonObject grp = groups.getJsonObject(i); // 获取数组中的每个JSON对象
					SNMPSetting grpSetting = readFromJson(grp); // 解析JSON对象并返回对应的SNMPSetting对象
					String grpName = grp.getString("dbgroup", null); // 获取JSON对象中的"dbgroup"字段的值

					if (grpName != null && grpSetting != null)
						groupSettings.put(grpName, grpSetting); // 将组名和组配置信息存入groupSettings集合中
				}
			}

			JsonArray hosts = jsonObject.getJsonArray("host");

			if (hosts != null) {
				for (int i = 0; i < hosts.size(); i++) {
					JsonObject host = hosts.getJsonObject(i); // 获取数组中的每个JSON对象
					SNMPSetting hostSetting = readFromJson(host); // 解析JSON对象并返回对应的SNMPSetting对象
					String hostName = host.getString("dbhost", null); // 获取JSON对象中的"dbhost"字段的值

					if (hostName != null && hostSetting != null)
						hostSettings.put(hostName, hostSetting); // 将主机名和主机配置信息存入hostSettings集合中
				}
			}

			return true; // 返回true表示成功读取配置
		} catch (IOException ex) { // 捕获可能发生的异常
			LOGGER.warning("Failed to read SNMP configuration file " + cfgFile.getAbsolutePath(), ex);
		}

		return false; // 返回false表示读取配置失败
	}

	private SNMPSetting readFromJson(JsonObject jobj) {
		String version = jobj.getString(SNMPSetting.VERSION, null);
		String community = jobj.getString(SNMPSetting.COMMUNITY, null);
		String enabled = jobj.getString(SNMPSetting.ENABLEED, "yes");

		if ((version != null && !version.isEmpty()) || (community != null && !community.isEmpty())) {
			SNMPSetting snmp = new SNMPSetting();
			snmp.setVersion(version);
			snmp.setCommunity(community);
			snmp.setEnabled(enabled);

			if ("3".equals(version)) {
				snmp.setUsername(jobj.getString(SNMPSetting.V3_USERNAME, null));
				String pwd = jobj.getString(SNMPSetting.V3_PASSWORD, null);

				if (pwd != null)
					pwd = frameworkContext.getMetaDb().dec(pwd);

				snmp.setPassword(pwd);
				snmp.setAuthProtocol(jobj.getString(SNMPSetting.V3_AUTHPROTOCOL, null));
				pwd = jobj.getString(SNMPSetting.V3_PRIVACYPASSPHRASE, null);

				if (pwd != null)
					pwd = frameworkContext.getMetaDb().dec(pwd);

				snmp.setPrivacyPassphrase(pwd);
				snmp.setPrivacyProtocol(jobj.getString(SNMPSetting.V3_PRIVACYPROTOCOL, null));
				snmp.setContext(jobj.getString(SNMPSetting.V3_CONTEXT, null));
			}
		}

		return null;
	}

	private boolean writeToJson(Writer writer) {
		JsonObjectBuilder settingBuilder = Json.createObjectBuilder();
		settingBuilder.add("site", toJson(siteSetting));

		JsonArrayBuilder groupBuilder = Json.createArrayBuilder();

		for (String key : groupSettings.keySet()) {
			JsonObjectBuilder grp = toJson(groupSettings.get(key));
			grp.add("dbgroup", key);

			groupBuilder.add(grp);
		}

		settingBuilder.add("group", groupBuilder);

		JsonArrayBuilder hostBuilder = Json.createArrayBuilder();
		for (Map.Entry<String, SNMPSetting> e : hostSettings.entrySet()) {
			JsonObjectBuilder host = toJson(e.getValue());
			host.add("dbhost", e.getKey());
			hostBuilder.add(host);
		}

		settingBuilder.add("host", hostBuilder);

		try (JsonWriter wr = Json.createWriter(writer)) {
			wr.writeObject(settingBuilder.build());

			return true;
		} catch (Exception ex) {
			LOGGER.warning("Failed to store snmp json", ex);
		}

		return false;
	}

	private JsonObjectBuilder toJson(SNMPSetting setting) {
		JsonObjectBuilder sb = Json.createObjectBuilder();

		if (setting != null) {
			if (setting.getVersion() != null && !setting.getVersion().isEmpty())
				sb.add(SNMPSetting.VERSION, setting.getVersion());
			if (setting.getCommunity() != null && !setting.getCommunity().isEmpty())
				sb.add(SNMPSetting.COMMUNITY, setting.getCommunity());
			if ("3".equals(setting.getVersion())) {
				if (setting.getUsername() != null && !setting.getUsername().isEmpty())
					sb.add(SNMPSetting.V3_USERNAME, setting.getUsername());
				if (setting.getPassword() != null && !setting.getPassword().isEmpty())
					sb.add(SNMPSetting.V3_PASSWORD, frameworkContext.getMetaDb().enc(setting.getPassword()));

				if (setting.getAuthProtocol() != null && !setting.getAuthProtocol().isEmpty())
					sb.add(SNMPSetting.V3_AUTHPROTOCOL, setting.getAuthProtocol());
				if (setting.getPrivacyPassphrase() != null && !setting.getPrivacyPassphrase().isEmpty())
					sb.add(SNMPSetting.V3_PRIVACYPASSPHRASE, frameworkContext.getMetaDb().enc(setting.getPrivacyPassphrase()));
				if (setting.getPrivacyProtocol() != null && !setting.getPrivacyProtocol().isEmpty())
					sb.add(SNMPSetting.V3_PRIVACYPROTOCOL, setting.getPrivacyProtocol());
				if (setting.getContext() != null && !setting.getContext().isEmpty())
					sb.add(SNMPSetting.V3_CONTEXT, setting.getContext());
			}

			sb.add(SNMPSetting.ENABLEED, setting.getEnabled());
		}
		return sb;
	}
}
