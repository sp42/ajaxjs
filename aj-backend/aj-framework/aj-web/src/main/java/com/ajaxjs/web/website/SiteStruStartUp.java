package com.ajaxjs.web.website;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.json_db.map_traveler.MapTraveler;
import com.ajaxjs.json_db.map_traveler.MapUtils;

//@WebListener
public abstract class SiteStruStartUp implements ServletContextListener {
    private Function<String, List<Map<String, Object>>> makeChildren;

    public void setMakeChildren(Function<String, List<Map<String, Object>>> makeChildren) {
        this.makeChildren = makeChildren;
    }

    public Function<String, List<Map<String, Object>>> getMakeChildren() {
        return makeChildren;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        String path = context.getRealPath("/WEB-INF/website-stru.json");
        SiteStru json = new SiteStru();
        json.setFilePath(path);
        json.load();

        if (json.isLoaded()) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> list = (List<Map<String, Object>>) json.getJsonMap().get("website");

            if (makeChildren != null) {
                MapTraveler traveler = new MapTraveler();

                traveler.setOnMap((map, superMap, level) -> {
                    if (map.containsKey("dbNode")) {
                        List<Map<String, Object>> children = makeChildren.apply(map.get("dbNode").toString());
                        map.put(MapUtils.CHILDREN, children);

                        System.out.println(map.get("name"));
                    }
                    return true;
                });

                traveler.traveler(list);
            }
            MapUtils.buildPath(list);
        }

        context.setAttribute("WEBSITE_STRU", json);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
