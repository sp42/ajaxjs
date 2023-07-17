/*
 * Copyright 2015, Yahoo Inc.
 * Copyrights licensed under the Apache License.
 * See the accompanying LICENSE file for terms.
 */
package com.ajaxjs.mysql.manager;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.mysql.config.MyPerfContext;
import com.ajaxjs.mysql.model.DBGroupInfo;
import com.ajaxjs.mysql.model.DBInstanceInfo;
import com.ajaxjs.mysql.model.InstanceStates;
import com.ajaxjs.util.logger.LogHelper;

/**
 * store realtime status
 *
 * @author xrao
 */
public class InstanceStatesManager {
	private static final LogHelper LOGGER = LogHelper.getLog(InstanceStatesManager.class);

	public static String OBJ_FILE_NAME = "instance_status.ser";
	public static final String STORAGE_DIR = "autoscan";
	private String rootPath = "myperf_reports";// data will be stored under $rootPath/autoscan

	private Map<Integer, InstanceStates> statesMap = new HashMap<>();

	/**
	 * Make the data persistent
	 */
	public void saveStatus() {
		LOGGER.info("Store instance status snapshots");
		File root = new File(new File(this.rootPath), STORAGE_DIR);
		File objFile = new File(root, OBJ_FILE_NAME);
		storeObject(objFile);
	}

	public void storeObject(File objf) {
		ObjectOutputStream outputStream = null;

		try {
			// Construct the LineNumberReader object
			outputStream = new ObjectOutputStream(Files.newOutputStream(objf.toPath()));
			outputStream.writeObject(this.statesMap);

		} catch (Exception ex) {
			LOGGER.info("Exception when store instance status object", ex);
		} finally {
			// Close the ObjectOutputStream
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Map<Integer, InstanceStates> readObject(File objf) {
		try (ObjectInputStream input = new ObjectInputStream(Files.newInputStream(objf.toPath()))) {
			return (Map<Integer, InstanceStates>) input.readObject();
		} catch (IOException | ClassNotFoundException ex) {
			LOGGER.warning("Exception when read instance status object", ex);

			return null;
		}
	}

	synchronized public InstanceStates getStates(int dbid) {
		if (statesMap.containsKey(dbid))
			return statesMap.get(dbid);

		return null;
	}

	synchronized public void addInstanceStates(int dbid) {
		if (statesMap.containsKey(dbid))
			return;

		this.statesMap.put(dbid, new InstanceStates());
	}

	synchronized public boolean removeInstanceStates(int dbid) {
		if (statesMap.containsKey(dbid)) {
			statesMap.remove(dbid);
			return true;
		}
		return false;
	}

	/**
	 * This should be called during context initialization
	 *
	 * @param context
	 */
	public void init(MyPerfContext context) {
//		logger.info("Initialize InstanceStatesManager");
		File root = new File(new File(this.rootPath), STORAGE_DIR);
		if (!root.exists())
			root.mkdirs();

		File objFile = new File(root, OBJ_FILE_NAME);
		if (objFile.exists()) {
//			logger.info("Load saved status");
			Map<Integer, InstanceStates> savedState = readObject(objFile);

			if (savedState != null) {
				for (Map.Entry<Integer, InstanceStates> e : savedState.entrySet())
					this.statesMap.put(e.getKey(), e.getValue());
			}
		}

		for (Map.Entry<String, DBGroupInfo> e : context.getDbInfoManager().getClusters().entrySet()) {
			for (DBInstanceInfo dbinfo : e.getValue().getInstances()) {
				if (!this.statesMap.containsKey(dbinfo.getDbid()))
					this.statesMap.put(dbinfo.getDbid(), new InstanceStates());
			}
		}

//		logger.info("Initialized InstanceStatesManager");
	}
}
