package com.ajaxjs.util.logger;
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// this is form Tomcat
// another http://ymfans.iteye.com/blog/240944 http://www.tuicool.com/articles/vy6Zrye
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class FileHandler extends Handler {
	public FileHandler() {
		this(null, null, null);
	}

	public FileHandler(String directory, String prefix, String suffix) {
		this.directory = directory;
		this.prefix = prefix;
		this.suffix = suffix;
		configure();
		openWriter();
	}

	/**
	 * The as-of date for the currently open log file, or a zero-length string
	 * if there is no open log file.
	 */
	private volatile String date = "";

	/**
	 * The directory in which log files are created.
	 */
	private String directory = null;

	/**
	 * The prefix that is added to log file filenames.
	 */
	private String prefix = null;

	/**
	 * The suffix that is added to log file filenames.
	 */
	private String suffix = null;

	/**
	 * Determines whether the logfile is rotatable
	 */
	private boolean rotatable = true;

	/**
	 * The PrintWriter to which we are currently logging, if any.
	 */
	private volatile PrintWriter writer = null;

	/**
	 * Lock used to control access to the writer.
	 */
	protected ReadWriteLock writerLock = new ReentrantReadWriteLock();

	/**
	 * Log buffer size.
	 */
	private int bufferSize = -1;

	/**
	 * Format and publish a <tt>LogRecord</tt>.
	 *
	 * @param record
	 *            description of the log event
	 */
	@Override
	public void publish(LogRecord record) {

		if (!isLoggable(record)) {
			return;
		}

		// Construct the timestamp we will use, if requested
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String tsString = ts.toString().substring(0, 19);
		String tsDate = tsString.substring(0, 10);

		try {
			writerLock.readLock().lock();
			// If the date has changed, switch log files
			if (rotatable && !date.equals(tsDate)) {
				try {
					// Update to writeLock before we switch
					writerLock.readLock().unlock();
					writerLock.writeLock().lock();

					// Make sure another thread hasn't already done this
					if (!date.equals(tsDate)) {
						close();
						date = tsDate;
						openWriter();
					}
				} finally {
					writerLock.writeLock().unlock();
					// Down grade to read-lock. This ensures the writer remains
					// valid
					// until the log message is written
					writerLock.readLock().lock();
				}
			}

			String result = null;
			try {
				result = getFormatter().format(record);
			} catch (Exception e) {
				reportError(null, e, ErrorManager.FORMAT_FAILURE);
				return;
			}

			try {
				if (writer != null) {
					writer.write(result);
					if (bufferSize < 0) {
						writer.flush();
					}
				} else {
					reportError("FileHandler is closed or not yet initialized, unable to log [" + result + "]", null,
							ErrorManager.WRITE_FAILURE);
				}
			} catch (Exception e) {
				reportError(null, e, ErrorManager.WRITE_FAILURE);
				return;
			}
		} finally {
			writerLock.readLock().unlock();
		}
	}

	/**
	 * Close the currently open log file (if any).
	 */
	@Override
	public void close() {
		writerLock.writeLock().lock();
		try {
			if (writer == null)
				return;
			writer.write(getFormatter().getTail(this));
			writer.flush();
			writer.close();
			writer = null;
			date = "";
		} catch (Exception e) {
			reportError(null, e, ErrorManager.CLOSE_FAILURE);
		} finally {
			writerLock.writeLock().unlock();
		}
	}

	/**
	 * Flush the writer.
	 */
	@Override
	public void flush() {

		writerLock.readLock().lock();
		try {
			if (writer == null)
				return;
			writer.flush();
		} catch (Exception e) {
			reportError(null, e, ErrorManager.FLUSH_FAILURE);
		} finally {
			writerLock.readLock().unlock();
		}

	}

	/**
	 * Configure from <code>LogManager</code> properties.
	 */
	private void configure() {

		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String tsString = ts.toString().substring(0, 19);
		date = tsString.substring(0, 10);

		String className = this.getClass().getName(); // allow classes to
														// override

		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		// Retrieve configuration of logging file name
		rotatable = Boolean.parseBoolean(getProperty(className + ".rotatable", "true"));
		if (directory == null)
			directory = getProperty(className + ".directory", "logs");
		if (prefix == null)
			prefix = getProperty(className + ".prefix", "ajaxjs.");
		if (suffix == null)
			suffix = getProperty(className + ".suffix", ".log");
		String sBufferSize = getProperty(className + ".bufferSize", String.valueOf(bufferSize));
		try {
			bufferSize = Integer.parseInt(sBufferSize);
		} catch (NumberFormatException ignore) {
			// no op
		}
		// Get encoding for the logging file
		String encoding = getProperty(className + ".encoding", null);
		if (encoding != null && encoding.length() > 0) {
			try {
				setEncoding(encoding);
			} catch (UnsupportedEncodingException ex) {
				// Ignore
			}
		}

		// Get logging level for the handler
		setLevel(Level.parse(getProperty(className + ".level", "" + Level.ALL)));

		// Get filter configuration
		String filterName = getProperty(className + ".filter", null);
		if (filterName != null) {
			try {
				setFilter((Filter) cl.loadClass(filterName).newInstance());
			} catch (Exception e) {
				// Ignore
			}
		}

		// Set formatter
		String formatterName = getProperty(className + ".formatter", null);
		if (formatterName != null) {
			try {
				setFormatter((Formatter) cl.loadClass(formatterName).newInstance());
			} catch (Exception e) {
				// Ignore and fallback to defaults
				setFormatter(new SimpleFormatter());
			}
		} else {
			setFormatter(new SimpleFormatter());
		}

		// Set error manager
		setErrorManager(new ErrorManager());
	}

	private String getProperty(String name, String defaultValue) {
		String value = LogManager.getLogManager().getProperty(name);
		if (value == null) {
			value = defaultValue;
		} else {
			value = value.trim();
		}
		return value;
	}

	/**
	 * Open the new log file for the date specified by <code>date</code>.
	 */
	protected void openWriter() {
		// Create the directory if necessary
		File dir = new File(directory);
		if (!dir.mkdirs() && !dir.isDirectory()) {
			reportError("Unable to create [" + dir + "]", null, ErrorManager.OPEN_FAILURE);
			writer = null;
			return;
		}

		// Open the current log file
		writerLock.writeLock().lock();
		FileOutputStream fos = null;
		OutputStream os = null;
		File pathname = new File(dir.getAbsoluteFile(), prefix + (rotatable ? date : "") + suffix);
		File parent = pathname.getParentFile();
		if (!parent.mkdirs() && !parent.isDirectory()) {
			reportError("Unable to create [" + parent + "]", null, ErrorManager.OPEN_FAILURE);
			writer = null;
			return;
		}
		
		String encoding = getEncoding();
		
		try {
			fos = new FileOutputStream(pathname, true);
			os = bufferSize > 0 ? new BufferedOutputStream(fos, bufferSize) : fos;
			writer = new PrintWriter(
					(encoding != null) ? new OutputStreamWriter(os, encoding) : new OutputStreamWriter(os), false);
			writer.write(getFormatter().getHead(this));
		} catch (Exception e) {
			reportError(null, e, ErrorManager.OPEN_FAILURE);
			writer = null;
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					// Ignore
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e1) {
					// Ignore
				}
			}
		} finally {
			writerLock.writeLock().unlock();
		}

	}

}
