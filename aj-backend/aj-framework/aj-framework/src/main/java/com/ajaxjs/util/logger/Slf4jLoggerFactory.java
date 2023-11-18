package com.ajaxjs.util.logger;

import org.slf4j.ILoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Slf4jLoggerFactory implements ILoggerFactory {
    private final ConcurrentMap<String, Slf4jLogHelper> loggerMap = new ConcurrentHashMap<>();

    @Override
    public Slf4jLogHelper getLogger(String name) {
        Slf4jLogHelper logger = loggerMap.get(name);

        if (logger != null)
            return logger;
        else {
            Slf4jLogHelper newInstance = new Slf4jLogHelper(name);
            Slf4jLogHelper oldInstance = loggerMap.putIfAbsent(name, newInstance);

            return oldInstance == null ? newInstance : oldInstance;
        }
    }

    /**
     * Clear the internal logger cache.
     */
    void reset() {
        loggerMap.clear();
    }
}
