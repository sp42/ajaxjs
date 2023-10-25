package com.ajaxjs.framework.embeded_tomcat.jar_scan;

import java.net.URL;

import org.apache.tomcat.util.scan.Constants;

public class ClassPathEntry {
    private final boolean jar;
    private final String name;

    public ClassPathEntry(URL url) {
        String path = url.getPath();
        int end = path.indexOf(Constants.JAR_EXT);

        if (end != -1) {
            jar = true;
            int start = path.lastIndexOf('/', end);
            name = path.substring(start + 1, end + 4);
        } else {
            jar = false;
            if (path.endsWith("/"))
                path = path.substring(0, path.length() - 1);

            int start = path.lastIndexOf('/');
            name = path.substring(start + 1);
        }
    }

    public boolean isJar() {
        return jar;
    }

    public String getName() {
        return name;
    }
}
