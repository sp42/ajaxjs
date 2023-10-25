package com.ajaxjs.framework.embeded_tomcat;

import org.apache.catalina.*;
import org.apache.catalina.WebResourceRoot.ResourceSetType;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.coyote.AbstractProtocol;
import org.apache.coyote.ProtocolHandler;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Tomcat 的功能
 */
public class TomcatFunction {
    Context context;

    Tomcat tomcat;

    TomcatConfig cfg;

    /**
     * SSI（服务器端嵌入）
     */
    void ssi() {
        context.setPrivileged(true);
        Wrapper servlet = Tomcat.addServlet(context, "ssi", "org.apache.catalina.ssi.SSIServlet");
        servlet.addInitParameter("buffered", "1");
        servlet.addInitParameter("inputEncoding", "UTF-8");
        servlet.addInitParameter("outputEncoding", "UTF-8");
        servlet.addInitParameter("debug", "0");
        servlet.addInitParameter("expires", "666");
        servlet.addInitParameter("isVirtualWebappRelative", "4");
        servlet.setLoadOnStartup(4);
        servlet.setOverridable(true);

        // Servlet mappings
        context.addServletMappingDecoded("*.html", "ssi");
        context.addServletMappingDecoded("*.shtml", "ssi");
    }

    /**
     * 设置 URI 编码支持中文
     */
    void setUriChinese() {
        tomcat.getConnector().setURIEncoding("UTF-8");
    }

    /**
     * 设置 Tomcat 配置
     */
    void setConfig() {
        ProtocolHandler handler = tomcat.getConnector().getProtocolHandler();

        if (handler instanceof AbstractProtocol) {
            AbstractProtocol<?> protocol = (AbstractProtocol<?>) handler;

            if (cfg.getMinSpareThreads() > 0)
                protocol.setMinSpareThreads(cfg.getMinSpareThreads());

            if (cfg.getMaxThreads() > 0)
                protocol.setMaxThreads(cfg.getMaxThreads());

            if (cfg.getConnectionTimeout() > 0)
                protocol.setConnectionTimeout(cfg.getConnectionTimeout());

            if (cfg.getMaxConnections() > 0)
                protocol.setMaxConnections(cfg.getMaxConnections());

            if (cfg.getAcceptCount() > 0)
                protocol.setAcceptCount(cfg.getAcceptCount());
        }
    }

    /**
     * context load WEB-INF/web.xml from classpath
     */
    void addWebXmlMountListener() {
        context.addLifecycleListener(event -> {
            if (event.getType().equals(Lifecycle.BEFORE_START_EVENT)) {
                Context context = (Context) event.getLifecycle();
                WebResourceRoot resources = context.getResources();

                if (resources == null) {
                    resources = new StandardRoot(context);
                    context.setResources(resources);
                }

                /*
                 * when run as embedded tomcat, context.getParentClassLoader() is AppClassLoader,so it can load "WEB-INF/web.xml" from app classpath.
                 */
                URL resource = context.getParentClassLoader().getResource("WEB-INF/web.xml");

                if (resource != null) {
                    String webXmlUrlString = resource.toString();
                    URL root;

                    try {
                        root = new URL(webXmlUrlString.substring(0, webXmlUrlString.length() - "WEB-INF/web.xml".length()));
                        resources.createWebResourceSet(ResourceSetType.RESOURCE_JAR, "/WEB-INF", root, "/WEB-INF");
                    } catch (MalformedURLException ignored) {
                    }
                }
            }
        });
    }

    /**
     * 禁用 JSP
     */
    void disableJsp() {
        LifecycleListener tmplf = null;

        for (LifecycleListener lfl : context.findLifecycleListeners()) {
            if (lfl instanceof Tomcat.DefaultWebXmlListener) {
                tmplf = lfl;
                break;
            }
        }

        if (tmplf != null)
            context.removeLifecycleListener(tmplf);

        context.addLifecycleListener(event -> {
            if (Lifecycle.BEFORE_START_EVENT.equals(event.getType())) {
                Context context = (Context) event.getLifecycle();
                Tomcat.initWebappDefaults(context);
                // 去掉JSP
                context.removeServletMapping("*.jsp");
                context.removeServletMapping("*.jspx");
                context.removeChild(context.findChild("jsp"));
            }
        });

    }
}
