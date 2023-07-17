/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Data;

/**
 * MySQL process list info
 *
 * @author xrao
 */
@Data
public class ProcessListEntry implements java.io.Serializable, Comparable<ProcessListEntry> {
	private static final long serialVersionUID = 1L;

	private String id;
	private String user;
	private String host;
	private String db;
	private String command;
	private int time;
	private String state;
	private String info;

	// note not all MySQL servers have these metrics (percona 5.5.10 or later). For
	// 5.6, we can use performance schema
	private long time_ms = -1L;
	private long rows_read = 0;
	private long rows_examined = 0;
	private long rows_sent = 0;

	/**
	 * Construct from one row
	 */
	public ProcessListEntry(ResultSet rs) {
		try {
			this.setId(rs.getString("ID"));
			this.setUser(rs.getString("USER"));
			this.setHost(rs.getString("HOST"));
			this.setDb(rs.getString("DB"));
			this.setCommand(rs.getString("COMMAND"));
			this.setState(rs.getString("STATE"));
			this.setTime(rs.getInt("TIME"));
			this.setInfo(rs.getString("INFO"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param rowInfo If true, with ROWS_SENT, ROWS_EXAMINED and ROWS_READ
	 */
	public ProcessListEntry(ResultSet rs, boolean rowInfo, boolean hasRowsRead) {
		try {
			this.setId(rs.getString("ID"));
			this.setUser(rs.getString("USER"));
			this.setHost(rs.getString("HOST"));
			this.setDb(rs.getString("DB"));
			this.setCommand(rs.getString("COMMAND"));
			this.setState(rs.getString("STATE"));
			this.setTime(rs.getInt("TIME"));
			this.setInfo(rs.getString("INFO"));

			if (rowInfo) {
				this.setRows_examined(rs.getLong("ROWS_EXAMINED"));
				if (hasRowsRead)
					this.setRows_read(rs.getLong("ROWS_READ"));
				this.setRows_sent(rs.getLong("ROWS_SENT"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void setHost(String host) {
		if (host == null)
			this.host = host;
		int idx = host.indexOf(":");// strip :

		if (idx < 0)
			this.host = host;
		else
			this.host = host.substring(0, idx);
	}

	@Override
	public int compareTo(ProcessListEntry obj) {
		if (obj == null)
			return 1;
		String s = obj.getInfo();

		if ((s == null || s.isEmpty()) && (this.info == null || this.info.isEmpty()))
			return 0;// both are empty
		else if (s == null || s.isEmpty())
			return 1;// the other is empty
		else if (this.info == null || this.info.isEmpty())
			return -1;// we are empty

		try {
			// nobody empty
			return this.info.compareTo(s);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("info: " + this.info + ", comparedTo " + s);
		}

		return -1;
	}
}
