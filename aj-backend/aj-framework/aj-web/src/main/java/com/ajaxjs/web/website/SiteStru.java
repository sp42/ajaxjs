package com.ajaxjs.web.website;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.ajaxjs.json_db.SimpleJsonDB;
import com.ajaxjs.json_db.map_traveler.MapUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网站结构的配置
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SiteStru extends SimpleJsonDB {
    private final static String PAGE_NODE = "PAGE_Node";

    /**
     * 获取当前页面节点，并带有丰富的节点信息
     *
     * @param uri         请求地址，例如 "menu/menu-1"
     * @param contextPath 项目名称
     * @return 当前页面节点
     */
//    public Map<String, Object> getPageNode(String uri, String contextPath) {
//        // 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源
//        String path = uri.replace(contextPath, "").replaceAll("/\\d+", "").replaceFirst("/\\w+\\.\\w+$", "");
//
//        return loaded ? ListMap.findByPath(path, this) : null;
//    }

    /**
     * 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源
     *
     * @param request 请求对象
     * @return 资源 URI
     */
    private static String getPath(HttpServletRequest request) {
        return request.getRequestURI().replace(request.getContextPath(), "").replaceFirst("/\\w+\\.\\w+$", "");
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getData() {
        return (List<Map<String, Object>>) getJsonMap().get("website");
    }

    /**
     * 获取当前页面节点，并带有丰富的节点信息
     *
     * @param request 请求对象
     * @return 当前页面节点
     */

    @SuppressWarnings("unchecked")
    public Map<String, Object> getPageNode(HttpServletRequest request) {
        Object obj = request.getAttribute(PAGE_NODE);
        Map<String, Object> map;

        if (obj == null) {
            String path = getPath(request);
            List<Map<String, Object>> list = getData();

            map = MapUtils.findByPath(path, list);
            request.setAttribute(PAGE_NODE, map);

//			System.out.println(path);
//			System.out.println(map);
        } else
            map = (Map<String, Object>) obj;

        return map;
    }

    /**
     * 用于 current 的对比 <li
     * ${pageContext.request.contextPath.concat('/').concat(menu.fullPath).
     * concat('/') == pageContext.request.requestURI ? ' class=selected' : ''}> IDE
     * 语法报错，其实正确的 于是，为了不报错 <li ${PageNode.isCurrentNode(menu) ? ' class=selected' :
     * ''}>
     *
     * @param node 节点
     * @return true 表示为是当前节点
     */
    public boolean isCurrentNode(Map<String, ?> node, HttpServletRequest request) {
        if (node == null || node.get(MapUtils.PATH) == null)
            return false;

        String uri = request.getRequestURI(), contextPath = request.getContextPath();
        String fullPath = node.get(MapUtils.PATH).toString(), ui = contextPath.concat("/").concat(fullPath).concat("/");

        return uri.equals(ui) || uri.contains(fullPath);
    }

    /**
     * 获取导航
     *
     * @return 导航数据
     */
    public List<Map<String, Object>> getNavBar() {
        return getData();
    }

    /**
     * 生成二级节点
     *
     * @param request 请求对象
     * @return 二级节点菜单
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getSecondLevelNode(HttpServletRequest request) {
        if (request.getAttribute("secondLevel_Node") == null) {
            String path = getPath(request);

            if (!StringUtils.hasText(path))
                return null;

            path = path.substring(1);
            String second = path.split("/")[0];
            Map<String, Object> map = MapUtils.findByPath(second, getData());
            request.setAttribute("secondLevel_Node", map); // 保存二级栏目节点之数据

            return map;
        } else
            return (Map<String, Object>) request.getAttribute("secondLevel_Node");
    }

    /**
     * 生成二级节点菜单所需的数据
     *
     * @param request 请求对象
     * @return 二级节点菜单列表
     */
    @SuppressWarnings({"unchecked"})
    public List<Map<String, Object>> getMenu(HttpServletRequest request) {
        Map<String, Object> map = getSecondLevelNode(request);

        return map != null && map.get(MapUtils.CHILDREN) != null
                ? (List<Map<String, Object>>) map.get(MapUtils.CHILDREN)
                : null;
    }

    private static final String TABLE = "<table class=\"siteMap\"><tr><td>%s</td></tr></table>",
            A_LINK = "<a href=\"%s/\" class=\"indentBlock_%s\"><span class=\"dot\">·</span>%s</a>\n ",
            NEW_COL = "\n\t</td>\n\t<td>\n\t\t";

    private String siteMapCache;

    /**
     * 获取页脚的网站地图
     *
     * @return 页脚的网站地图
     */
    public String getSiteMap(HttpServletRequest request) {
        if (siteMapCache == null) {
            StringBuilder sb = new StringBuilder();
            getSiteMap(getData(), sb, request.getContextPath());

            siteMapCache = String.format(TABLE, sb);
        }

        return siteMapCache;
    }

    /**
     * 该函数递归使用，故须独立成一个函数
     */
    @SuppressWarnings("unchecked")
    private static void getSiteMap(List<Map<String, Object>> list, StringBuilder sb, String cxtPath) {
        for (Map<String, Object> map : list) {
            if (map != null) {
                Object isHidden = map.get("isHidden");

                if (isHidden != null && (boolean) isHidden) // 隐藏的
                    continue;

                if (0 == (int) map.get(MapUtils.LEVEL)) // 新的一列
                    sb.append(NEW_COL);

                sb.append(String.format(A_LINK, cxtPath + map.get(MapUtils.PATH).toString(),
                        map.get(MapUtils.LEVEL).toString(), map.get("name").toString()));

                Object children = map.get(MapUtils.CHILDREN);

                if (children != null && children instanceof List)
                    getSiteMap((List<Map<String, Object>>) children, sb, cxtPath);
            }
        }
    }

    /**
     * 面包屑导航
     *
     * @param request
     * @return
     */
    public String buildBreadCrumb(HttpServletRequest request) {
        String ctx = request.getContextPath(), uri = request.getRequestURI();
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("<nav class=\"anchor\">您的位置 ：<a href=\"%s\">首 页 </a>", ctx));
        // MVC模式下，url 路径还是按照 JSP 的而不是 Servlet 的，我们希望统一的路径是按照 Servlet 的，故所以这里 Servlet 优先

        Map<String, Object> node = getPageNode(request);

        if (node == null) {
            if (uri.equals(request.getContextPath() + "/") || uri.indexOf(ctx + "/index") != -1) { // 重复了 TODO
                // 首页
            } else {
                System.err.println("不能渲染导航定位，该页面可能：1、未引用 head.jsp 创建 NODE 节点；2、未定义该路径之说明。" + uri);
                return "";
            }
        }

        String tpl = " » <a href=\"%s\">%s</a>";

        if (node == null && uri.indexOf(ctx + "/index") != -1) {

        } else if (node != null) {
            if (node.get("supers") != null) {
                String _supers = (String) node.get("supers");
                String[] supers = _supers.split(",");

                for (String _super : supers) {
                    String[] arr = _super.split(":");

                    if (!ObjectUtils.isEmpty(arr) && arr.length >= 2)
                        sb.append(String.format(tpl, ctx + arr[0], arr[1]));
                }
            }

            sb.append(String.format(tpl, ctx + node.get(MapUtils.PATH), node.get("name")));
        }

        sb.append("</nav>");

        // 如果有分类的话，先显示分类 （适合列表的情形）
        return sb.toString();
    }

    /**
     * 列表标签
     */
    private final static String LI = "<li%s><a href=\"%s/\">%s</a></li>";

    /**
     * 连接后面没有斜杠
     */
    private final static String LI_NO_END = "<li%s><a href=\"%s\">%s</a></li>";

    /**
     *
     */
    private final static String LI_EXT = "<li%s><a href=\"%s/\">%s</a><ul>%s</ul></li></li>";

    /**
     * 导航条
     *
     * @param request
     * @return
     */
    public String buildNav(HttpServletRequest request) {
        String ctx = request.getContextPath();
        StringBuilder sb = new StringBuilder();
        boolean hasSelected = false;
        Object _customNavLi = request.getAttribute("customNavLi");
        boolean showNavSubMenu = request.getAttribute("showNavSubMenu") != null
                && (boolean) request.getAttribute("showNavSubMenu");
        boolean customSubMenu = false;
        String showNavSubMenuUl = null, showNavSubMenuLi = null;

        if (showNavSubMenu) {
            Object _showNavSubMenuUl = request.getAttribute("showNavSubMenuUl");

            if (_showNavSubMenuUl != null) {
                customSubMenu = true;
                showNavSubMenuUl = (String) _showNavSubMenuUl;
                showNavSubMenuLi = (String) request.getAttribute("showNavSubMenuLi");
            }
        }

        if (getNavBar() != null) {
            for (Map<String, Object> item : getNavBar()) {

                Object isHidden = item.get("isHidden");
                if (isHidden != null && ((boolean) isHidden)) // 隐藏的
                    continue;

                boolean isSelected = isCurrentNode(item, request);

                String url = ctx + "/" + item.get(MapUtils.ID);
                url = addParam(url, item);

                if (_customNavLi == null)
                    sb.append(String.format(LI, isSelected ? " class=\"selected\"" : "", url, item.get("name")));
                else {
                    String _li = _customNavLi.toString();
                    if (isSelected) {
                        _li = _li.replace("class=\"", "class=\"selected ");
                    }

                    if (showNavSubMenu) {
                        if (customSubMenu)
                            sb.append(String.format(_li, url, item.get("name"),
                                    buildSubMenu(showNavSubMenuUl, showNavSubMenuLi, item, ctx)));
                        else {
                            // 默认标签的菜单
                        }
                    } else
                        sb.append(String.format(_li, url, item.get("name")));
                }

                if (isSelected)
                    hasSelected = true;
            }
        }
        if (_customNavLi == null) {
            return String.format(LI, !hasSelected ? " class=\"home selected\"" : " class=\"home\"",
                    "".equals(ctx) ? "" : ctx, "首页") + sb;
        } else {
            String _li = _customNavLi.toString();
            if (showNavSubMenu)
                return String.format(_li.replace("class=\"", "class=\"home "), "".equals(ctx) ? "/" : ctx, "首页", "")
                        + sb;
            else
                return String.format(_li.replace("class=\"", "class=\"home "), "".equals(ctx) ? "/" : ctx, "首页")
                        + sb;
        }
    }

    /**
     * @param showNavSubMenuUl
     * @param showNavSubMenuLi
     * @param item
     * @param ctx
     * @return
     */
    private String buildSubMenu(String showNavSubMenuUl, String showNavSubMenuLi, Map<String, Object> item,
                                String ctx) {
        StringBuilder sb = new StringBuilder();
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> menu = (List<Map<String, Object>>) item.get(MapUtils.CHILDREN);

        for (Map<String, Object> m : menu) {
            String url = ctx + m.get("fullPath").toString();
            sb.append(String.format(showNavSubMenuLi, url, m.get("name").toString()));
        }

        return String.format(showNavSubMenuUl, sb);
    }

    /**
     * 向 URL 中添加参数
     *
     * @param url  原始 URL
     * @param item 包含参数的 Map 对象
     * @return 添加参数后的 URL
     */
    private static String addParam(String url, Map<String, Object> item) {
        Object param = item.get("param");

        if (param != null)
            url += (String) param;

        return url;
    }

    /**
     * 次级菜单，只是该节点下面的 children
     *
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    public String buildSubMenu(HttpServletRequest request) {
        String ctx = request.getContextPath();
        StringBuilder sb = new StringBuilder();

        // node 在 head.jsp 中保存
        Map<String, Object> node = getPageNode(request);
        List<Map<String, Object>> nodes = (List<Map<String, Object>>) node.get(MapUtils.CHILDREN);

        for (Map<String, Object> item : nodes) {
            Object isHidden = item.get("isHidden");
            if (isHidden != null && ((boolean) isHidden)) // 隐藏的
                continue;

            String url = ctx + item.get(MapUtils.PATH);
            url = addParam(url, item);

            boolean isSelected = isCurrentNode(item, request);
            sb.append(String.format(LI, isSelected ? " class=\"selected\"" : "", url, item.get("name")));
        }

        return sb.toString();
    }

    /**
     * 二级菜单
     *
     * @param request
     * @return
     */
    public String buildSecondLevelMenu(HttpServletRequest request) {
        String ctx = request.getContextPath();
        StringBuilder sb = new StringBuilder();

        if (getMenu(request) != null) {
            boolean showSubMenu = request.getAttribute("showSubMenu") != null;

            for (Map<String, Object> item : getMenu(request)) {
                Object isHidden = item.get("isHidden");
                if (isHidden != null && ((boolean) isHidden)) // 隐藏的
                    continue;

                String url = ctx + item.get(MapUtils.PATH);
                url = addParam(url, item);
                boolean isSelected = isCurrentNode(item, request);

                if (showSubMenu) {
                    StringBuilder subMenu = new StringBuilder();
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> menu = (List<Map<String, Object>>) item.get(MapUtils.CHILDREN);

                    if (!CollectionUtils.isEmpty(menu))
                        for (Map<String, Object> m : menu) {
                            String _url = ctx + m.get("fullPath").toString();
                            subMenu.append(String.format(LI, "", _url, "» " + m.get("name").toString()));
                        }

                    sb.append(String.format(LI_EXT, isSelected ? " class=\"selected\"" : "", url, item.get("name"), subMenu));
                } else
                    sb.append(String.format(url.contains("?") ? LI_NO_END : LI,
                            isSelected ? " class=\"selected\"" : "", url, item.get("name")));
            }
        }

        return sb.toString();
    }

}
