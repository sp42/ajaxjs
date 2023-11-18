package org.slf4j.impl;

import com.ajaxjs.util.logger.Slf4jLoggerFactory;
import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;


public class StaticLoggerBinder implements LoggerFactoryBinder {
    private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();

    // to avoid constant folding by the compiler, this field must *not* be final
    // !final
    public static String REQUESTED_API_VERSION = "1.7";

    private static final String loggerFactoryClassStr = Slf4jLoggerFactory.class.getName();

    private final ILoggerFactory loggerFactory;

    private StaticLoggerBinder() {
        loggerFactory = new Slf4jLoggerFactory();
    }

    @Override
    public ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    @Override
    public String getLoggerFactoryClassStr() {
        return loggerFactoryClassStr;
    }

    public static StaticLoggerBinder getSingleton() {
        return SINGLETON;
    }
}
