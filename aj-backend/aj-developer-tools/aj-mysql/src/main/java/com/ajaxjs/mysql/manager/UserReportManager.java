/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ajaxjs.mysql.model.ColumnDescriptor;
import com.ajaxjs.mysql.model.ResultList;
import com.ajaxjs.mysql.model.ResultRow;

/**
 * handle reports for a single user
 *
 * @author xrao
 */
public class UserReportManager {
	private static Logger logger = Logger.getLogger(UserReportManager.class.getName());

	public static final char lineSeparator = '\n';

	private String rootpath = "myperf_reports";// root path to store reports, default to reports under current directory
	private String idxfile = "rep.idx";// index file name
	private String username;

	private volatile boolean initiated = false;

	private File userroot;

	volatile int maxId = 0;

	public UserReportManager(String username, String rootpath) {
		this.username = username;
		if (rootpath != null && rootpath.trim().length() > 0)
			this.rootpath = rootpath.trim();
		// else use default
	}

	public void init() {
		if (!initiated) {
			File reproot = new File(rootpath);
			if (!reproot.exists())
				reproot.mkdir();

			userroot = new File(reproot, username);
			if (!userroot.exists())
				userroot.mkdir();
			this.initiated = true;
		}
	}

	synchronized public void addReportEntry(UserReport urp) {
		File idxFile = new File(userroot, idxfile);
		FileWriter fw = null;

		if (maxId == 0)
			this.list();
		this.maxId++;
		urp.setId(this.maxId);
		
		try {
			fw = new FileWriter(idxFile, true);
			String line = urp.encodeLine() + lineSeparator;
			fw.write(line);
		} catch (Exception ex) {

			logger.log(Level.SEVERE, "Exception", ex);
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (Exception iex) {
			}
		}
	}

	synchronized public List<UserReport> list() {
		List<UserReport> repList = new java.util.ArrayList<>();
		File idxFile = new File(userroot, idxfile);
		if (idxFile.exists()) {
			BufferedReader br = null;

			try {
				br = new BufferedReader(new FileReader(idxFile));

				String line = null;

				while ((line = br.readLine()) != null) {
					UserReport rep = UserReport.decodeLine(line);
					if (rep != null) {
						repList.add(rep);
						if (rep.getId() >= this.maxId)
							maxId = rep.getId();
					}
				}
			} catch (Exception ex) {
				logger.log(Level.SEVERE, "Exception", ex);
			} finally {
				try {
					if (br != null)
						br.close();
				} catch (Exception iex) {
				}
			}
		}
		return repList;
	}

	public ResultList listResultList() {
		List<UserReport> repList = list();

		ResultList rList = new ResultList();
		ColumnDescriptor desc = new ColumnDescriptor();
		rList.setColumnDescriptor(desc);
		int idx = 0;
		desc.addColumn("ID", false, idx++);
		desc.addColumn("TYPE", false, idx++);
		desc.addColumn("DB", false, idx++);
		desc.addColumn("HOST", false, idx++);
		desc.addColumn("CREATED", false, idx++);
		desc.addColumn("COMPLETED", false, idx++);
		desc.addColumn("FORMAT", false, idx++);
		desc.addColumn("PARAMETER", false, idx++);
		desc.addColumn("FILENAME", false, idx++);

		// list in reverse order
		for (int i = repList.size() - 1; i >= 0; i--) {
			UserReport urp = repList.get(i);
			ResultRow row = new ResultRow();
			List<String> cols = new java.util.ArrayList<>(8);
			row.setColumnDescriptor(desc);
			row.setColumns(cols);
			cols.add(String.valueOf(urp.getId()));
			cols.add(urp.getType());
			cols.add(urp.getDbname());
			cols.add(urp.getDbhost());
			cols.add(urp.getCreated_timestamp());
			cols.add(urp.getCompleted_timestamp());
			cols.add(urp.getFormat());
			cols.add(urp.getParameters().toString());
			cols.add(urp.getFilename());
			rList.addRow(row);
		}
		return rList;
	}

	/**
	 * Refresh the idx file, for example, after delete an entry
	 *
	 * @param repList
	 */
	public void update(List<UserReport> repList) {
		File idxFile = new File(userroot, idxfile);
		FileWriter fw = null;

		try {
			fw = new FileWriter(idxFile);
			for (UserReport urp : repList) {
				String line = urp.encodeLine() + lineSeparator;
				fw.write(line);
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Exception", ex);
		} finally {
			try {
				if (fw != null)
					fw.close();
			} catch (Exception iex) {
			}
		}

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getIdxfile() {
		return idxfile;
	}

	public void setIdxfile(String idxfile) {
		this.idxfile = idxfile;
	}

	public String getRootpath() {
		return rootpath;
	}

	public void setRootpath(String rootpath) {
		this.rootpath = rootpath;
	}

	public File getFileForDownload(String fname)// retrieve file for download
	{
		File f = new File(new File(new File(getRootpath()), getUsername()), fname);
		if (!f.exists() || !f.getAbsolutePath().endsWith(fname))// check file and security
			return null;
		return f;
	}

	public boolean deleteFile(String fname, String id) {
		if (fname == null && id == null)
			return false;
		// first check if we have such file or not
		// update idx
		int rpid = -1;
		try {
			rpid = Integer.parseInt(id);
		} catch (Exception ex) {
		}
		List<UserReport> repList = this.list();
		boolean findOne = false;
		for (int i = repList.size() - 1; i >= 0; i--) {
			if (repList.get(i).getFilename().equalsIgnoreCase(fname) || rpid == repList.get(i).getId()) {
				fname = repList.get(i).getFilename();
				repList.remove(i);
				findOne = true;
				break;
			}
		}
		if (!findOne)
			return false;
		this.update(repList);
		File f = this.getFileForDownload(fname);
		if (f != null && !"rep.idx".equalsIgnoreCase(f.getName())) {
			f.delete();
			return true;
		}
		return false;
	}
}
