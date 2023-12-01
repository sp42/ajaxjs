package com.ajaxjs.util.logger.refactor;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class DeleteLogFile {
    /**
     * 日志文件名的前缀 The prefix that is added to log file filenames.
     */
    private String prefix;

    /**
     * 日志文件名的后缀 The suffix that is added to log file filenames.
     */
    private String suffix;

    /**
     * 日志文件的保存目录 The directory in which log files are created.
     */
    private String directory;

    /**
     * 日志文件保留的最大天数。超过指定天数的日志文件将被删除，-1 则不删除
     * Maximum number of days to keep the log files
     */
    private final int maxDays = -1;

    /**
     * 日志文件名的模式。模式格式为 {prefix}{date}{suffix}，其中日期部分的格式为 YYYY-MM-DD。该字段用于进行日志文件名的匹配和处理。
     * Represents a file name pattern of type {prefix}{date}{suffix}. The date is YYYY-MM-DD
     */
    private Pattern pattern;

    /**
     * 单线程的  ExecutorService，用于执行删除文件的任务
     */
    private static final ExecutorService DELETE_FILES_SERVICE = Executors.newSingleThreadExecutor(new ThreadExecutor());

    public void clean() {
        if (maxDays <= 0)
            return;

        DELETE_FILES_SERVICE.submit(() -> {
            for (File file : streamFilesForDelete()) {
                file.delete();
//                if (!file.delete())
//                    reportError("Unable to delete log files older than [" + maxDays + "] days", null, ErrorManager.GENERIC_FAILURE);
            }
        });
    }

    private File[] streamFilesForDelete() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, -maxDays);

        final Date maxDaysOffset = cal.getTime();
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        return new File(directory).listFiles((dir, name) -> {
            boolean result = false;
            String date = obtainDateFromFilename(name);

            if (date != null) {
                try {
                    Date dateFromFile = formatter.parse(date);
                    result = dateFromFile.before(maxDaysOffset);
                } catch (ParseException e) {
                    // no-op
                }
            }

            return result;
        });
    }

    private String obtainDateFromFilename(String name) {
        String date = name;

        if (pattern.matcher(date).matches()) {
            date = date.substring(prefix.length());
            return date.substring(0, date.length() - suffix.length());
        } else
            return null;
    }

}
