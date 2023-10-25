package com.ajaxjs.framework.embeded_tomcat;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.ContextConfig;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.descriptor.web.WebXml;
import org.apache.tomcat.Jar;
import org.apache.tomcat.util.scan.JarFactory;

/**
 * Support jar in jar. when boot by spring boot loader, jar url will be: fat.jar!/lib/!/test.jar!/ .
 */
public class EmbededContextConfig extends ContextConfig {
    private static final Log log = LogFactory.getLog(EmbededContextConfig.class);

    /**
     * 扫描包含 web-fragment.xml 文件的 JAR 文件，以查看它们是否还包含静态资源，并将其添加到上下文中。
     * 如果找到静态资源，则按照 web-fragment.xml 的优先级顺序添加。
     * Scan JARs that contain web-fragment.xml files that will be used to
     * configure this application to see if they also contain static resources. If static resources are found,
     * add them to the context. Resources are added in web-fragment.xml priority order.
     */
    @Override
    protected void processResourceJARs(Set<WebXml> fragments) {
        for (WebXml fragment : fragments) {
            URL url = fragment.getURL();
            String urlString = url.toString();

            // It's a nested jar, but we now don't want the suffix
            // because Tomcat is going to try and locate it as a root URL (not the resource inside it)
            if (isInsideNestedJar(urlString))
                urlString = urlString.substring(0, urlString.length() - 2);

            try {
                url = new URL(urlString);

                if ("jar".equals(url.getProtocol())) {
                    try (Jar jar = JarFactory.newInstance(url)) {
                        jar.nextEntry();
                        String entryName = jar.getEntryName();

                        while (entryName != null) {
                            if (entryName.startsWith("META-INF/resources/")) {
                                context.getResources().createWebResourceSet(
                                        WebResourceRoot.ResourceSetType.RESOURCE_JAR,
                                        "/", url, "/META-INF/resources");
                                break;
                            }

                            jar.nextEntry();
                            entryName = jar.getEntryName();
                        }
                    }
                } else if ("file".equals(url.getProtocol())) {
                    File file = new File(url.toURI());
                    File resources = new File(file, "META-INF/resources/");

                    if (resources.isDirectory())
                        context.getResources().createWebResourceSet(
                                WebResourceRoot.ResourceSetType.RESOURCE_JAR, "/",
                                resources.getAbsolutePath(), null, "/");
                }
            } catch (IOException | URISyntaxException ioe) {
                log.error(sm.getString("contextConfig.resourceJarFail", url, context.getName()));
            }
        }
    }

    private static boolean isInsideNestedJar(String dir) {
        return dir.indexOf("!/") < dir.lastIndexOf("!/");
    }
}
