/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.process;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ajaxjs.mysql.common.DBUtils;
import com.ajaxjs.mysql.config.ConfigBlock;
import com.ajaxjs.mysql.config.ConfigHistory;
import com.ajaxjs.mysql.config.MyPerfContext;
import com.ajaxjs.mysql.db.UserDBConnections;
import com.ajaxjs.mysql.model.AppUser;
import com.ajaxjs.mysql.model.DBConnectionWrapper;
import com.ajaxjs.mysql.model.DBCredential;
import com.ajaxjs.mysql.model.DBGroupInfo;
import com.ajaxjs.mysql.model.DBInstanceInfo;

public class GlobalVariableChangeScanTask implements Runnable {
	private static Logger logger = Logger.getLogger(GlobalVariableChangeScanTask.class.getName());

	private MyPerfContext context;
	private static final String STORAGE_DIR = "autoscan";
	private AppUser appUser;
	private UserDBConnections conns;
	java.text.SimpleDateFormat sdf;

	public GlobalVariableChangeScanTask(MyPerfContext context, AppUser user) {
		this.context = context;
		this.appUser = user;
	}

	public void setAppUser(AppUser appUser) {
		this.appUser = appUser;
	}

	public AppUser getAppUser() {
		return this.appUser;
	}

	@Override
	public void run() {
		Thread.currentThread().setName("GlobalVariableChangeScanTask");
		conns = new UserDBConnections();
		conns.setAppUser(appUser.getName());
		conns.setFrameworkContext(context);

		File root = new File(new File(this.context.getFileReposirtoryPath()), STORAGE_DIR);

		// get all dbids
		List<DBInstanceInfo> dbs = new ArrayList<DBInstanceInfo>();
		for (Map.Entry<String, DBGroupInfo> e : context.getDbInfoManager().getClusters().entrySet()) {
			DBGroupInfo g = e.getValue();
			for (DBInstanceInfo i : g.getInstances()) {
				dbs.add(i);
			}
		}

		// now get current timestamp
		sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		for (DBInstanceInfo db : dbs)// now we need scan for each db
		{
			ConfigBlock cb = scanHost(db);
			if (cb != null) {
				updateConfigBlock(db, cb);
			}
		}

	}

	private void updateConfigBlock(DBInstanceInfo db, ConfigBlock cb) {
		File root = new File(new File(this.context.getFileReposirtoryPath()), STORAGE_DIR);
		ConfigHistory ch = ConfigHistory.load(root, db);

		if (ch == null)
			ch = new ConfigHistory();// a brand new
		if (ch.updateLast(cb))// if any changes, update
			ch.store(root, db);

	}

	private ConfigBlock scanHost(DBInstanceInfo dbinfo) {
		DBCredential cred = DBUtils.findDBCredential(context, dbinfo.getDbGroupName(), appUser);
		if (cred == null) {
			logger.info("No credential for cluster " + dbinfo.getDbGroupName() + ", skip it");
			return null;// log the error
		}

		logger.info("Scan for host (" + dbinfo + ") as user " + cred.getUsername());
		DBConnectionWrapper conn = null;// find one connection is enough
		Statement stmt = null;
		ResultSet rs = null;
		ConfigBlock cb = null;

		try {
			conn = conns.checkoutConnection(dbinfo, cred);
			if (conn == null) {
				logger.info("Failed to access " + dbinfo + ", skip it");
				return null;
			}

			String sqlText = "show global variables";
			stmt = conn.getConnection().createStatement();
			rs = stmt.executeQuery(sqlText);
			cb = new ConfigBlock();
			while (rs != null && rs.next()) {
				String key = rs.getString("VARIABLE_NAME").toUpperCase();
				if ("TIMESTAMP".equalsIgnoreCase(key))
					continue;// exclude a timestamp variable
				cb.addVariable(key, rs.getString("VALUE"));
			}

			Calendar c = Calendar.getInstance();
			Date dt = c.getTime();
			cb.setTime(sdf.format(dt));
		} catch (Exception ex) {
			logger.log(Level.WARNING, "exception", ex);
			// we should not used failed data
			return null;
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
			if (conn != null)
				conns.checkinConnectionAndClose(conn);
		}

		logger.info("Done configuration scan for host (" + dbinfo + ") as user " + cred.getUsername());
		return cb;
	}
}
