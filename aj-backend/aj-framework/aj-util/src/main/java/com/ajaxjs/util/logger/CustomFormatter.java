package com.ajaxjs.util.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomFormatter extends Formatter {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_CYAN = "\u001B[36m";

    private final Date dat = new Date();
    private static final String format = "%1$s %2$tb %2$td, %2$tY %2$tl:%2$tM:%2$tS %2$Tp %3$s%n%5$s: %6$s%7$s%n";

    @Override
    public String format(LogRecord record) {
        dat.setTime(record.getMillis());
        String source;
        if (record.getSourceClassName() != null) {
            source = record.getSourceClassName();
            if (record.getSourceMethodName() != null) {
                source += " " + record.getSourceMethodName();
            }
        } else {
            source = record.getLoggerName();
        }
        String message = formatMessage(record);
        String throwable = "";
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            record.getThrown().printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }

        switch (record.getLevel().toString()) {
            case "INFO":
                return String.format(format, ANSI_CYAN, dat, source, record.getLoggerName(),
                        record.getLevel().getLocalizedName(), message + ANSI_RESET, throwable);
            case "WARNING":
                return LogHelper.DEBUG_COLOUR + message + ANSI_RESET;

//                return String.format(format, ANSI_YELLOW, dat, source, record.getLoggerName(),
//                        record.getLevel().getLocalizedName(), message + ANSI_RESET, throwable);
            case "SEVERE":
                return String.format(format, ANSI_RED, dat, source, record.getLoggerName(),
                        record.getLevel().getLocalizedName(), message + ANSI_RESET, throwable);
            default:
                return String.format(format, dat, source, record.getLoggerName(),
                        record.getLevel().getLocalizedName(), message, throwable);
        }
    }
}