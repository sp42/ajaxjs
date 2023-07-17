package com.ajaxjs.mysql.snmp;

import lombok.Data;

@Data
public class SNMPSetting {
	public SNMPSetting() {
	}

	public SNMPSetting(String community, String version) {
		this.community = community;
		this.version = version;
	}

	public SNMPSetting copy() {
		return new SNMPSetting(community, version);
	}

	public static final String DEFAULT_COMMUNITY = "public";
	public static final String DEFAULT_VERSION = "2c";
	public static final String COMMUNITY = "community";
	public static final String VERSION = "version";
	public static final String ENABLEED = "enabled";
	public static final String V3_USERNAME = "username";
	public static final String V3_PASSWORD = "password";
	public static final String V3_AUTHPROTOCOL = "authprotocol";
	public static final String V3_PRIVACYPASSPHRASE = "privacypassphrase";
	public static final String V3_PRIVACYPROTOCOL = "privacyprotocol";
	public static final String V3_CONTEXT = "context";

	private String community;
	private String version;
	private String username;// v3
	private String password;// v3
	private String authProtocol;// v3
	private String privacyPassphrase;// v3
	private String privacyProtocol;// v3
	private String context;// v3
	private String enabled = "yes";
}
