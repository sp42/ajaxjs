/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import lombok.Data;

/**
 * Information about an individual database server
 *
 * @author xrao
 */
@Data
public class DBInstanceInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private static final int MAX_16BIT_UINT = 65536;

	private int instance = 0;// keep it for future to include Oracle

	private String dbType = "mysql";// other value: Oracle
	private String hostName;// db host name
	private String port = "3306";// tcp port
	private String databaseName = "information_schema";// MySQL database name for default connect
	private String dbGroupName;// database group or cluster name
	// For mysql, we can use it to group servers
	// used for the same purpose

	private boolean useTunneling;// when ssh tunneling is used
	private String localHostName = "localhost";// local hostname when use tunneling
	private String localPort;// local port when use tunneling

	private String username;
	private String password;

	private boolean connectionVerified;// if we use probe all instances, some instance might not
	// be verified for its connections,
	// especially when ssh tunneling is used

	private boolean testConnection;// used for form only
	private boolean probeAllInstance;// used for form only, keep it for Oracle
	private boolean storeCredential;// user for form only

	private boolean virtualHost;// some system does not allow direct host/sid access, for Oracle
	private String owner;// the user who creates this entry

	private int dbid; // primary key, generated. Will be used for metrics
	private boolean snmpEnabled = true;// by default, we assume SNMP is enabled.
	private boolean metricsEnabled = true;
	private boolean alertEnabled = true;

	public void setPort(String port) {
		if (port != null) {
			try {
				int lp = Integer.parseInt(port);
				if (lp < 0) {
					lp = MAX_16BIT_UINT + lp;
				}

				this.port = String.valueOf(lp);
			} catch (Exception ignored) {
			}
		}
	}

	public short getPortShort() {
		return (short) Integer.parseInt(port);
	}

	public void setLocalPort(String localPort) {
		int lp = Integer.parseInt(localPort);
		if (lp < 0)
			lp = MAX_16BIT_UINT + lp;

		this.localPort = String.valueOf(lp);
	}

	public short getLocalPortShort() {
		return (short) Integer.parseInt(localPort);
	}

	/**
	 * Make a value copy
	 */
	public DBInstanceInfo copy() {
		DBInstanceInfo db = new DBInstanceInfo();
		db.setDbType(this.dbType);
		db.setDbGroupName(this.dbGroupName);
		db.setHostName(this.hostName);
		db.setPort(this.port);
		db.setDatabaseName(this.databaseName);
		db.setLocalHostName(this.localHostName);
		db.setUseTunneling(this.useTunneling);
		db.setLocalPort(this.localPort);
		db.setUsername(this.username);
		db.setPassword(this.password);
		db.setInstance(this.instance);
		db.setSnmpEnabled(this.snmpEnabled);
		db.setMetricsEnabled(this.metricsEnabled);
		db.setConnectionVerified(this.connectionVerified);
		db.setAlertEnabled(this.alertEnabled);

		return db;
	}

	public String getConnectionString() {
		// Add allowPublicKeyRetrieval to support sha256 password
		if ("mysql".equalsIgnoreCase(this.dbType)) {
			if (!this.useTunneling)
				return "jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.databaseName
						+ "?useSSL=true&enabledTLSProtocols=TLSv1.2&verifyServerCertificate=false";
			else
				return "jdbc:mysql://" + this.localHostName + ":" + this.localPort + "/" + this.databaseName
						+ "?useSSL=true&enabledTLSProtocols=TLSv1.2&verifyServerCertificate=false";
		} else if ("oracle".equalsIgnoreCase(this.dbType)) {
			if (!this.useTunneling) {
				if (!this.virtualHost)
					return "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=" + hostName + ")(PORT=" + port
							+ "))(CONNECT_DATA=(SERVER=DEDICATED)(SID=" + databaseName + ")))";
				else
					return "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=" + hostName + ")(PORT=" + port
							+ "))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=" + databaseName + ")))";
			} else {
				if (!this.virtualHost)
					return "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=" + this.localHostName + ")(PORT=" + this.localPort
							+ "))(CONNECT_DATA=(SERVER=DEDICATED)(SID=" + databaseName + ")))";
				else
					return "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=" + this.localHostName + ")(PORT=" + this.localPort
							+ "))(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=" + databaseName + ")))";
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return "[" + this.dbGroupName + "," + this.hostName + "," + this.dbid + "," + this.instance + "," + this.port + "," + this.databaseName + ", ssh: "
				+ this.useTunneling + "," + this.localHostName + "," + this.localPort + ",virtual:" + this.virtualHost + "," + this.username + "]";
	}

	public void setDbType(String dbType) {
		if (dbType != null)
			this.dbType = dbType.trim().toLowerCase();
		else
			this.dbType = null;
	}

	/**
	 * Whether we can query other db server or instance of the samegroup to retrieve
	 * status of this db, for example, Oracle to use gv$ or dba_hist views
	 */
	public boolean supportClusterQuery() {
		return "Oracle".equalsIgnoreCase(this.dbType);
	}
}
