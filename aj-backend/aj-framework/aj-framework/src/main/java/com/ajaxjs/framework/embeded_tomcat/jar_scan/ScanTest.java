package com.ajaxjs.framework.embeded_tomcat.jar_scan;

import java.io.IOException;
import java.net.URL;

import org.apache.tomcat.Jar;
import org.apache.tomcat.util.scan.JarFactory;


public class ScanTest {
    private static final String TLD_EXT = ".tld";

    public static void scan(Jar jar, String webappPath, boolean isWebapp) throws IOException {
        URL jarFileUrl = jar.getJarFileURL();
        System.out.println("xxxx------" + jarFileUrl.toString());
        jar.nextEntry();

        for (String entryName = jar.getEntryName(); entryName != null; jar.nextEntry(), entryName = jar.getEntryName()) {
            if (!(entryName.startsWith("META-INF/") && entryName.endsWith(TLD_EXT)))
                continue;

            URL entryUrl = JarFactory.getJarEntryURL(jarFileUrl, entryName);
            System.out.println(entryName + ": " + entryUrl);
            entryUrl.openStream();
        }

    }

}