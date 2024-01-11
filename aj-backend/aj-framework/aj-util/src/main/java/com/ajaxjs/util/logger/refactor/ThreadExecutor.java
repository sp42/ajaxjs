package com.ajaxjs.util.logger.refactor;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂生产线程
 */
public class ThreadExecutor implements ThreadFactory {
    /**
     * 表示当前 JVM 是否启用了安全管理器。如果启用了安全管理器，则使用安全管理器获取线程组信息，否则使用当前线程的线程组。
     */
    private final boolean isSecurityEnabled;

    /**
     * 线程组是一组线程的集合，它们共享相同的属性
     */
    private final ThreadGroup group;

    /**
     * 返回一个递增的数字，用于标识线程编号
     */
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    /**
     * 线程名称前缀
     */
    private final static String namePrefix = "FileHandlerLogFilesCleaner-";

    {
        SecurityManager s = System.getSecurityManager();
        this.isSecurityEnabled = s != null;
        this.group = isSecurityEnabled ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            // Threads should not be created by the webapp classloader
            if (isSecurityEnabled) {
                /* 如果启用了安全管理器，则执行 AccessController.doPrivileged() 方法来设置类加载器；否则直接使用当前类加载器 */
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    @Override
                    public Void run() {
                        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
                        return null;
                    }
                });
            } else
                Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

            // 创建一个新的线程并将其设置为守护线程
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement());
            /* 将线程 t 设置为守护线程。守护线程是在后台运行的线程，它不会阻止程序的终止。当所有的非守护线程都运行完毕时，程序就会退出。与之相反，非守护线程则会一直运行，直到运行完任务或者被强制终止 */
            t.setDaemon(true);

            return t;
        } finally {
            if (isSecurityEnabled) {
                AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                    Thread.currentThread().setContextClassLoader(loader);
                    return null;
                });
            } else
                Thread.currentThread().setContextClassLoader(loader);
        }
    }
}
