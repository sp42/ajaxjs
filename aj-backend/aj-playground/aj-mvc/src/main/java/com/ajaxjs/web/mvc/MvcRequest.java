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
package com.ajaxjs.web.mvc;

import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.MappingValue;
import com.ajaxjs.util.io.StreamHelper;
import com.ajaxjs.util.map.MapTool;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * 通过 HttpServletRequestWrapper （装饰模式的应用）增强 HttpServletRequest的功能。
 *
 * @author sp42 frank@ajaxjs.com
 */
public class MvcRequest extends HttpServletRequestWrapper {
    /**
     * 创建一个 MVC 请求对象。构造方法会自动加入 UTF-8 编码。
     *
     * @param req 原始请求对象
     */
    public MvcRequest(HttpServletRequest req) {
        super(req);
        req.setAttribute("requestTimeRecorder", System.currentTimeMillis()); // 记录执行的时间

        try {
            // 为防止中文乱码，统一设置 UTF-8，设置请求编码方式
            setCharacterEncoding(StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定 JSP 时候，获取原请求的 uri，而非模版所在的 uri
     */
    @Override
    public String getRequestURI() {
        Object obj = getAttribute("javax.servlet.forward.request_uri");

        if (obj != null && !CommonUtil.isEmptyString((String) obj)) return (String) obj;
        else return super.getRequestURI();
    }

    /**
     * 获取资源 URI，忽略项目前缀和最后的文件名（如 index.jsp） 分析 URL 目标资源（最原始的版本）
     *
     * @return 请求路径
     */
    public String getRoute() {
        String route = getRequestURI().replaceAll("^" + getContextPath(), "");
        return route.replaceFirst("/\\w+\\.\\w+$", ""); // 删除最后的 index.jsp
    }

    /**
     * 目录部分
     *
     * @return 目录
     */
    public String getFolder() {
//		return getRequestURI().replace(getContextPath(), "").replaceFirst("^/", "").replaceFirst("/\\w+\\.\\w+$", "");
        return getRoute().replaceFirst("^/", "");
    }

    private Map<String, Object> putRequestData;

    /**
     * 获取 PUT 请求所提交的内容。 Servlet 不能获取 PUT 请求内容，所以这里写一个方法
     *
     * @return 参数、值集合
     */
    public Map<String, Object> getPutRequestData() {
        if (putRequestData == null) try (InputStream in = getInputStream()) {
            String params = StreamHelper.byteStream2string(in);
            putRequestData = MapTool.toMap(params.split("&"), v -> MappingValue.toJavaValue(Encode.urlDecode(v)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return putRequestData;
    }

    /**
     * 去取 url 上的值
     *
     * @param value 方法上的路径
     * @param param 参数名称
     * @return 值
     */
    public String getValueFromPath(String value, String param) {
        /* 如果 context path 上有数字那就bug，所以先去掉 */
        String url = getRoute(), regExp = "(" + value.replace("{" + param + "}", ")(\\d+)");/* 获取正则 暂时写死 数字 TODO */

//		Matcher m = Pattern.compile(regExp).matcher(url);
//		String result = m.find() ? m.group(m.groupCount()) : null;
        String result = CommonUtil.regMatch(regExp, url, -1);
        return Objects.requireNonNull(result, "在[ " + url + "]不能获取[ " + param + "]参数");
    }

    public Map<String, Object> getMap() {
        if (getMethod() != null && getMethod().equalsIgnoreCase("PUT"))
            return getPutRequestData(); // Servlet 没有 PUT 获取表单，要自己处理
        else return MapTool.as(getParameterMap(), arr -> MappingValue.toJavaValue(arr[0]));
    }

    /**
     * 支持自动获取请求参数并封装到 Bean 内
     *
     * @param <T> Bean 类型
     * @param clz Bean 的类引用
     * @return Java Bean
     */
    public <T> T getBean(Class<T> clz) {
        Map<String, Object> map = getMap();

        /*
         * 抛出 IllegalArgumentException 这个异常 有可能是参数类型不一致造成的， 要求的是 string 因为 map 从 request
         * 所以最后一个参数为 true
         */
        return MapTool.map2Bean(map, clz, true);
    }

    /**
     * 全局的 callback 参数名
     */
    public static final String CALLBACK_PARAM = "callback";

    /**
     * 保存到 request
     *
     * @param map 请求参数
     */
    public void saveToReuqest(Map<String, Object> map) {
        map.forEach(this::setAttribute);
    }

    /*
     * 为获取请求的上下文，能够在控制器中拿到最常用的对象，例如 HttpServletRequest 和 HttpServletResponse 等的对象（甚至
     * Web App 的启动上下文（ 在web.xml 中配置的参数） ），因此还需要设计一个 RequestHelper 类，通过
     * ThreadLocal让控制器能轻易地访问到这些对象。 一个容器，向这个容器存储的对象，在当前线程范围内都可以取得出来，向 ThreadLocal
     * 里面存东西就是向它里面的Map存东西的，然后 ThreadLocal 把这个 Map 挂到当前的线程底下，这样 Map 就只属于这个线程了
     */
    private static final ThreadLocal<HttpServletRequest> threadLocalRequest = new ThreadLocal<>();

    private static final ThreadLocal<HttpServletResponse> threadLocalResponse = new ThreadLocal<>();

    /**
     * 保存一个 request 对象
     *
     * @param req 请求对象
     */
    public static void setHttpServletRequest(HttpServletRequest req) {
        threadLocalRequest.set(req);
    }

    /**
     * 获取请求对象
     *
     * @return 请求对象
     */
    public static HttpServletRequest getHttpServletRequest() {
        return threadLocalRequest.get();
    }

    /**
     * 获取请求对象
     *
     * @return 请求对象
     */
    public static MvcRequest getMvcRequest() {
        HttpServletRequest req = getHttpServletRequest();
        if (!(req instanceof MvcRequest)) throw new RuntimeException("非法 MvcRequest 类型");

        return (MvcRequest) req;
    }

    /**
     * 保存一个 response 对象
     *
     * @param resp 响应对象
     */
    public static void setHttpServletResponse(HttpServletResponse resp) {
        threadLocalResponse.set(resp);
    }

    /**
     * 获取上下文中 response 对象
     *
     * @return response 响应对象
     */
    public static HttpServletResponse getHttpServletResponse() {
        HttpServletResponse resp = threadLocalResponse.get();
        if (resp == null) throw new RuntimeException("响应对象未初始化");

        return resp;
    }

    /**
     * 清空 request 和 response
     */
    public static void clean() {
        threadLocalRequest.set(null);
        threadLocalResponse.set(null);
    }

    /**
     * 获取磁盘真實地址
     *
     * @param cxt          Web 上下文
     * @param relativePath 相对地址
     * @return 绝对地址
     */
    public static String mappath(ServletContext cxt, String relativePath) {
        String absolute = cxt.getRealPath(relativePath);

        if (absolute != null) absolute = absolute.replace('\\', '/');
        return absolute;
    }

    /**
     * 输入一个相对地址，补充成为绝对地址 相对地址转换为绝对地址，并转换斜杠
     *
     * @param relativePath 相对地址，例如 /images
     * @return 绝对地址
     */
    public String mappath(String relativePath) {
        return mappath(getServletContext(), relativePath);
    }

    /**
     * 返回协议+主机名+端口+项目前缀（如果为 80 端口的话就默认不写 80）
     *
     * @return 网站名称
     */
    public String getBasePath() {
        String prefix = getScheme() + "://" + getServerName();

        int port = getServerPort();
        if (port != 80) prefix += ":" + port;

        return prefix + getContextPath();
    }

    /**
     * 获取请求 ip
     *
     * @return 客户端 ip
     */
    public String getIp() {
        String ip = getHeader("x-forwarded-for");

        if (!"unknown".equalsIgnoreCase(ip) && ip != null && ip.length() != 0) {
            int index = ip.indexOf(",");

            if (index != -1) ip = ip.substring(0, index);

            return ip.startsWith("::ffff:") ? ip.replaceAll("::ffff:", "") : ip;
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = getHeader("Proxy-Client-IP");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = getHeader("WL-Proxy-Client-IP");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = getHeader("X-Real-Ip");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) ip = getRemoteAddr();

        return ip.startsWith("::ffff:") ? ip.replaceAll("::ffff:", "") : ip;
    }

    /**
     * 如果有这个参数返回 true。
     *
     * @param key 键名称
     * @return true 表示为有这个键
     */
    public boolean hasParameter(String key) {
        String v = getParameter(key);

        if (CommonUtil.isEmptyString(v)) {
            return false;
        } else
            return MappingValue.toBoolean(v);
    }

    /**
     * 获取参数的值。若为 null 时的默认值
     *
     * @param key          键名称
     * @param defaultValue 若为 null 时的默认值
     * @return 参数内容
     */
    public String getParameter(String key, String defaultValue) {
        String v = getParameter(key);

        return CommonUtil.isEmptyString(v) ? defaultValue : v;
    }

    /**
     * 返回 SQL 类型安全的参数，必须为数字
     *
     * @param key 键名称
     * @return Id
     */
    public String getIdOnly(String key) {
        String p = getParameter(key);

        if (!p.matches("\\d+")) throw new IllegalArgumentException("参数 [" + key + "[必须为数字");

        return p;
    }
}
