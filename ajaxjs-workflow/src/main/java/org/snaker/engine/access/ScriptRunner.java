/*
 * Copyright 2013-2015 www.snakerflow.com. * * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in
 * compliance with the License. * You may obtain a copy of the License at * * http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by
 * applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the
 * License.
 */

package org.snaker.engine.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import org.snaker.engine.WorkflowUtils;

import com.ajaxjs.util.logger.LogHelper;

/**
 * SQL脚本运行类
 * 
 * @author yuqs
 * @since 2.0
 */
public class ScriptRunner {
	public static final LogHelper LOGGER = LogHelper.getLog(ScriptRunner.class);

	private static final String DEFAULT_DELIMITER = ";";

	// 数据库连接对象
	private Connection connection;

	// 是否自动提交
	private boolean autoCommit;

	// 默认的分隔符;
	private String delimiter = DEFAULT_DELIMITER;

	public ScriptRunner(Connection connection, boolean autoCommit) {
		this.connection = connection;
		this.autoCommit = autoCommit;
	}

	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	public void runScript(String resource) throws IOException, SQLException {
		Objects.requireNonNull(resource);
		InputStream input = WorkflowUtils.getStreamFromClasspath(resource);
		runScript(new InputStreamReader(input, "UTF-8"));
	}

	/**
	 * 根据reader读取sql脚本，并运行
	 * 
	 * @param reader 脚本资源
	 */
	public void runScript(Reader reader) throws IOException, SQLException {
		Objects.requireNonNull(connection);
		try {
			boolean originalAutoCommit = connection.getAutoCommit();
			try {
				if (originalAutoCommit != this.autoCommit)
					connection.setAutoCommit(this.autoCommit);

				runScript(connection, reader);
			} finally {
				connection.setAutoCommit(originalAutoCommit);
			}
		} catch (IOException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException("Error running script.  Cause: " + e, e);
		}
	}

	/**
	 * 根据给定的sql脚本资源、数据库连接对象，执行sql脚本
	 * 
	 * @param conn   数据库连接对象
	 * @param reader sql脚本资源
	 * @throws IOException  io异常
	 * @throws SQLException sql异常
	 */
	private void runScript(Connection conn, Reader reader) throws IOException, SQLException {
		StringBuffer command = null;
		try {
			LineNumberReader lineReader = new LineNumberReader(reader);
			String line = null;
			while ((line = lineReader.readLine()) != null) {
				if (command == null) {
					command = new StringBuffer();
				}
				String trimmedLine = line.trim();
				if (trimmedLine.startsWith("--")) {
					LOGGER.info(trimmedLine);
				} else if (trimmedLine.length() < 1 || trimmedLine.startsWith("//")) {
					// Do nothing
				} else if (trimmedLine.length() < 1 || trimmedLine.startsWith("--")) {
					// Do nothing
				} else if (trimmedLine.equals(getDelimiter()) || trimmedLine.endsWith(getDelimiter())) {
					command.append(line.substring(0, line.lastIndexOf(getDelimiter())));
					command.append(" ");
					Statement statement = conn.createStatement();

					LOGGER.info(command.toString());
					try {
						statement.execute(command.toString());
					} catch (SQLException e) {
						e.fillInStackTrace();
						LOGGER.warning("Error executing: " + command);
					}

					if (autoCommit && !conn.getAutoCommit()) {
						conn.commit();
					}
					command = null;
					try {
						statement.close();
					} catch (Exception e) {
						// ignore
					}
					Thread.yield();
				} else {
					command.append(line);
					command.append(" ");
				}
			}
			if (!autoCommit) {
				conn.commit();
			}
		} catch (SQLException e) {
			e.fillInStackTrace();
			throw e;
		} catch (IOException e) {
			e.fillInStackTrace();
			throw e;
		}
	}

	/**
	 * 获取分隔符
	 * 
	 * @return delimiter
	 */
	private String getDelimiter() {
		return delimiter;
	}
}
