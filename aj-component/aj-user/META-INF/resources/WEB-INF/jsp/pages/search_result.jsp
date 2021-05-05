<%-- <%@page import="com.ajaxjs.framework.config.ConfigService"%>
<%@page pageEncoding="UTF-8" 
import="java.util.*, java.io.*, com.ajaxjs.util.*, com.ajaxjs.util.map.MapTool, com.ajaxjs.util.io.*, com.ajaxjs.net.http.*"
%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<% 
	// http://zhannei.baidu.com/cse/search?s=1253001271484243495&entry=1&q=%E8%A7%A3%E5%86%B3%E6%96%B9%E6%A1%88
	String url = "http://zhannei.baidu.com/cse/search?s=%s&entry=1&q=%s";
	String html = NetUtil.get(String.format(url, ConfigService.get("baidu_site_seach_id"), request.getParameter("q")));
	
	int start = html.indexOf("<div id=\"results\" class=\"content-main\" style=\"\">"),
		end = html.lastIndexOf("<div class=\"extra\">");
	
	request.setAttribute("html", html.substring(start, end));
%>

<tags:content bannerText="搜索结果" showPageHelper="false">
	<style>
		.result {
			margin: 2%;
		}
		.c-title{
			margin: 10px 0;
		}
		.c-title a {
			color:black;
		}
		.c-showurl{
			color:gray;
		}
	</style>
	<h3 class="aj-center-title">搜索关键字：${param.q}</h3>
	<div class="resultList">
	${html}
</tags:content> --%>