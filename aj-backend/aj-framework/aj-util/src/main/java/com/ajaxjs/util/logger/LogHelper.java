/**
 * Copyright Sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.util.logger;

import com.ajaxjs.util.io.Resources;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Arrays;
import java.util.logging.*;

/**
 * 自定义日志工具类，封装了 Java 自带的日志类 java.util.logging.Logger。
 *
 * @author sp42 frank@ajaxjs.com
 */
public class LogHelper {
    /**
     * 所在的类名
     */
    public final String className;

    /**
     * 包装这个 logger
     */
    private final Logger logger;

    /**
     * 创建一个日志类
     *
     * @param clazz 当前日志记录的那个类
     */
    public LogHelper(Class<?> clazz) {
        this(clazz.getName().trim());
    }

    public LogHelper(String className) {
        this.className = className;
        logger = Logger.getLogger(className);
//        logger.setFilter(filter);

        URL url = LogHelper.class.getClassLoader().getResource("");
        String logFolder;

        if (url == null) { // jar running
            logFolder = Resources.getJarDir();
        } else {
            logFolder = url.getPath();
            logFolder = new File(logFolder).toString();
            logFolder = logFolder.replace("classes", "LogHelper");
        }

//        logger.addHandler(new JsonHandler(logFolder));
        logger.addHandler(new JsonHandler(logFolder, null, ".json"));

//        if (!Version.isDebug) {
//            logger.addHandler(new TomcatFileHandler(logFolder, null, ".log"));// 初始化保存到磁盤的處理器
//        }
    }

    /**
     * 简单打印信息
     *
     * @param msg 消息，可多个消息
     */
    public static void p(Object... msg) {
        Logger.getGlobal().info(Arrays.toString(msg));
    }

    private static final int NORMAL = 0;
    private static final int BRIGHT = 1;
    // private static final int FOREGROUND_BLACK = 30;
    private static final int FOREGROUND_RED = 31;
    private static final int FOREGROUND_GREEN = 32;
    private static final int FOREGROUND_YELLOW = 33;
    // private static final int FOREGROUND_BLUE = 34;
//	private static final int FOREGROUND_MAGENTA = 35;
    private static final int FOREGROUND_CYAN = 36;
//	private static final int FOREGROUND_WHITE = 37;

    private static final String PREFIX = "\u001b[";
    private static final String SUFFIX = "m ";
    private static final char SEPARATOR = ';';
//	private static final String END_COLOUR = PREFIX + SUFFIX;

    private static final String WARN_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_YELLOW + SUFFIX;
    private static final String INFO_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_GREEN + SUFFIX;
    private static final String FATAL_COLOUR = PREFIX + BRIGHT + SEPARATOR + FOREGROUND_RED + SUFFIX;
    private static final String COLOUR_END = PREFIX + "0m";
    // private static final String ERROR_COLOUR = PREFIX + NORMAL + SEPARATOR +
    // FOREGROUND_RED + SUFFIX;
    public static final String DEBUG_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_CYAN + SUFFIX;
//	private static final String TRACE_COLOUR = PREFIX + NORMAL + SEPARATOR + FOREGROUND_BLUE + SUFFIX;

    /**
     * 过滤器，是否要日志服务
     */
    private final static Filter filter = record -> record.getMessage() != null && !record.getMessage().contains("no log");

    /**
     * 获取自定义的 logger。这是外界调用本类最主要的方法。例如：
     *
     * <pre>
     * private static final LogHelper LOGGER = LogHelper.getLog(SqlProvider.class);
     * </pre>
     *
     * @param clazz 被日志记录的类
     * @return 日志管理器
     */
    public static LogHelper getLog(Class<?> clazz) {
        return new LogHelper(clazz); // 如果没有，则新建一个并保存到缓存中
    }

    /**
     * 打印日志，支持 {0}、{1}...的占位符
     *
     * @param level 日志级别
     * @param msg   日志信息
     */
    public void logMsg(Level level, String msg) {
        logger.logp(level, className, getMethodName(), msg);
    }

    /**
     * 打印日志，支持 {0}、{1}...的占位符
     *
     * @param level  日志级别
     * @param msgTpl 日志信息模版
     * @param params 日志信息参数
     */
    public void logMsg(Level level, String msgTpl, Object... params) {
        logger.logp(level, className, getMethodName(), msgTpl, params);
    }

    /**
     * 打印一个日志。这是调试级别的。
     *
     * @param msg 日志信息
     */
    public void debug(String msg) {
        logMsg(Level.INFO, DEBUG_COLOUR + msg + COLOUR_END);
    }

    /**
     * 打印一个日志，支持 {0}、{1}...的占位符。这是调试级别的。
     *
     * @param msgTpl 信息语句之模板
     * @param params 信息参数
     */
    public void debug(String msgTpl, Object... params) {
        info(msgTpl, params);
    }

    /**
     * 打印一个日志
     *
     * @param msg 日志信息
     */
    public void info(String msg) {
        logMsg(Level.INFO, INFO_COLOUR + msg + COLOUR_END);
    }

    public void info(Object obj) {
        if (obj == null)
            info(null);
        else
            info(obj.toString());
    }

    /**
     * 打印一个日志，支持 {0}、{1}...的占位符
     *
     * @param msgTpl 信息语句之模板
     * @param params 信息参数
     */
    public void info(String msgTpl, Object... params) {
        logMsg(Level.INFO, INFO_COLOUR + msgTpl + COLOUR_END, params);
    }

    public void infoGreen(String msg) {
        info(INFO_COLOUR + msg + COLOUR_END);
    }

    public void infoYellow(String msg) {
        info(WARN_COLOUR + msg + COLOUR_END);
    }

    public void infoCYAN(String msg) {
        info(DEBUG_COLOUR + msg + COLOUR_END);
    }

    /**
     * 打印一个日志（警告级别）
     *
     * @param msg 警告信息
     */
    public void warning(String msg) {
        logMsg(Level.WARNING, FATAL_COLOUR + msg + COLOUR_END);
    }

    /**
     * 打印一个日志（警告级别），支持 {0}、{1}...的占位符
     *
     * @param msgTpl 信息语句之模板
     * @param params 信息参数
     */
    public void warning(String msgTpl, Object... params) {
        logMsg(Level.WARNING, FATAL_COLOUR + msgTpl + COLOUR_END, params);
    }

    /**
     * 打印一个日志（警告级别）
     *
     * @param msg 警告信息
     * @param ex  任意异常信息
     */
    public void warning(Throwable ex, String msg) {
        logger.logp(Level.WARNING, className, getMethodName(), FATAL_COLOUR + msg + COLOUR_END, ex);
    }

    /**
     * 打印一个日志（警告级别） ，支持 {0}、{1}...的占位符e.g: log.warning("脚本引擎 {0} 没有 {1}() 这个方法",
     * "js", "foo");
     *
     * @param e      任意异常信息
     * @param msg    警告信息
     * @param params 信息参数
     */
    public void warning(Throwable e, String msg, Object... params) {
        for (int i = 0; i < params.length; i++) // JDK 没有这个方法的重载，写一个吧
            msg = msg.replace("{" + i + "}", params[i] == null ? "[NULL]" : params[i].toString());

        warning(e, msg);
    }

    /**
     * 打印一个日志（警告级别）
     *
     * @param e 任意异常信息
     */
    public void warning(Throwable e) {
//		e.printStackTrace();
        warning(e, e.getMessage());
    }

    /**
     * 获取所在的方法，调用时候
     *
     * @return 方法名称
     */
    private String getMethodName() {
        StackTraceElement frame = null;

        // Thread.getCurrentThread().getStackTrace() 暴露了当前线程的运行栈信息
        for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
            String clzName = ste.getClassName();

            if (ste.isNativeMethod() || clzName.equals(Thread.class.getName()) || clzName.equals(getClass().getName()))
                continue;// 过滤不要的类

            if (clzName.equals(className)) {// className是字符串，表示所在的类名
                frame = ste;
                break;
            }
        }

        if (frame != null) // 超链接，跳到源码所在行数
            return String.format("%s (%s:%s)", frame.getMethodName(), frame.getFileName(), frame.getLineNumber());
        else
            return null;
    }

    /**
     * 获取异常栈信息
     *
     * @param throwable 异常
     * @return 异常信息
     */
    public static String getStackTrace(Throwable throwable) {
        try (StringWriter sw = new StringWriter(); PrintWriter pw = new PrintWriter(sw, Boolean.TRUE)) {
            throwable.printStackTrace(pw);

            return sw.getBuffer().toString();
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }
}
