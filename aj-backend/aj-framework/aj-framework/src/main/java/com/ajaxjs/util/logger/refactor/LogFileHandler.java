package com.ajaxjs.util.logger.refactor;

import java.io.*;
import java.sql.Timestamp;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.*;

/**
 * 日志保存器
 */
public class LogFileHandler extends Handler {
    /**
     * 当前打开的日志文件的日期。如果没有打开的日志文件，则为一个长度为零的字符串
     * The as-of date for the currently open log file, or a zero-length string if there is no open log file.
     */
    private volatile String date = "";

    /**
     * 日志文件的保存目录 The directory in which log files are created.
     */
    private String directory;

    /**
     * 日志文件名的前缀 The prefix that is added to log file filenames.
     */
    private String prefix;

    /**
     * 日志文件名的后缀 The suffix that is added to log file filenames.
     */
    private String suffix;

    /**
     * 确定日志文件是否可滚动。如果可滚动，则会根据一定的条件进行日志文件的轮转 Determines whether the log file is rotatable
     */
    private boolean rotatable = true;

    /**
     * 当前正在写入日志的 PrintWriter 对象。如果没有正在写入的日志文件，则为 null The PrintWriter to which we are currently logging, if any.
     */
    private volatile PrintWriter writer = null;

    /**
     * 用于控制对 writer 的访问的读写锁
     * 通过使用读写锁来控制对 writer 的并发访问，保证了多线程环境下的线程安全性
     * Lock used to control access to the writer.
     */
    protected ReadWriteLock writerLock = new ReentrantReadWriteLock();

    /**
     * 日志缓冲区的大小 Log buffer size.
     */
    private int bufferSize = -1;

    public LogFileHandler() {
        this(null, null, null);
    }

    public LogFileHandler(String directory, String prefix, String suffix) {
        this.directory = directory;
        this.prefix = prefix;
        this.suffix = suffix;
        configure();
        openWriter();
    }

    /**
     * Configure from <code>LogManager</code> properties.
     */
    private void configure() {
        String tsString = new Timestamp(System.currentTimeMillis()).toString().substring(0, 19);
        date = tsString.substring(0, 10); // 设置当前日期

        String className = getClass().getName(); // allow classes to override
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        // Retrieve configuration of logging file name
        rotatable = Boolean.parseBoolean(getProperty(className + ".rotatable", "true"));// 配置文件中日志文件名是否可轮转的配置项 rotatable，并将其转换为布尔值

        if (directory == null)
            directory = getProperty(className + ".directory", "logs");

        if (prefix == null)
            prefix = getProperty(className + ".prefix", "juli.");

        if (suffix == null)
            suffix = getProperty(className + ".suffix", ".log");

        // 如果不需要进行文件名分隔符检查（!rotatable && !prefix.isEmpty() && !suffix.isEmpty()），则跳过此步骤
        // https://bz.apache.org/bugzilla/show_bug.cgi?id=61232
        boolean shouldCheckForRedundantSeparator = !rotatable && !prefix.isEmpty() && !suffix.isEmpty();
        // assuming separator is just one char, if there are use cases with  more, the notion of separator might be introduced
        if (shouldCheckForRedundantSeparator && (prefix.charAt(prefix.length() - 1) == suffix.charAt(0)))
            suffix = suffix.substring(1);

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
        setLevel(Level.parse(getProperty(className + ".level", String.valueOf(Level.WARNING))));

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
        } else
            setFormatter(new SimpleFormatter());

        // Set error manager
        setErrorManager(new ErrorManager());
    }

    private String getProperty(String name, String defaultValue) {
        String value = LogManager.getLogManager().getProperty(name);
        return value == null ? defaultValue : value.trim();
    }

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
        File pathname = new File(dir.getAbsoluteFile(), prefix + (rotatable ? date : "") + suffix);
        File parent = pathname.getParentFile();

        if (!parent.mkdirs() && !parent.isDirectory()) {
            reportError("Unable to create [" + parent + "]", null, ErrorManager.OPEN_FAILURE);
            writer = null;
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(pathname, true);
             OutputStream os = bufferSize > 0 ? new BufferedOutputStream(fos, bufferSize) : fos
        ) {
            String encoding = getEncoding();
            OutputStreamWriter outputStreamWriter = (encoding != null) ? new OutputStreamWriter(os, encoding) : new OutputStreamWriter(os);

            writer = new PrintWriter(outputStreamWriter, false);
            writer.write(getFormatter().getHead(this));
        } catch (Exception e) {
            reportError(null, e, ErrorManager.OPEN_FAILURE);
            writer = null;
        } finally {
            writerLock.writeLock().unlock();
        }
    }

    /**
     * Format and publish a <tt>LogRecord</tt>.
     *
     * @param record description of the log event
     */
    @Override
    public void publish(LogRecord record) {
        if (!isLoggable(record))
            return;

        // Construct the timestamp we will use, if requested
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        String tsDate = ts.toString().substring(0, 10);
        writerLock.readLock().lock();

        try {
            // If the date has changed, switch log files
            if (rotatable && !date.equals(tsDate)) {
                // Upgrade to writeLock before we switch
                writerLock.readLock().unlock();
                writerLock.writeLock().lock();

                try {
                    // Make sure another thread hasn't already done this
                    if (!date.equals(tsDate)) {
                        close();
                        date = tsDate;
                        openWriter();
                    }
                } finally {
                    // Downgrade to read-lock. This ensures the writer remains valid until the log message is written
                    writerLock.readLock().lock();
                    writerLock.writeLock().unlock();
                }
            }

            try {
                if (writer != null) {
                    writer.write(getFormatter().format(record));

                    if (bufferSize < 0)
                        writer.flush();
                } else
                    reportError("FileHandler is closed or not yet initialized, unable to log", null, ErrorManager.WRITE_FAILURE);
            } catch (Exception e) {
                reportError(null, e, ErrorManager.WRITE_FAILURE);
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
}
