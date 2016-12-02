<%@ page pageEncoding="UTF-8" import="java.util.*, com.ajaxjs.util.*, com.ajaxjs.net.http.Get, com.ajaxjs.web.*"%>
<%!

	/**
	 * html 编辑器使用的 iframe
	 */
	private static final String htmleditor_iframe = "<html>"+
	"	<head>"+
	"		<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"+
	"		<title>New Document</title>"+
	"		<style type=\"text/css\">"+
	"			body {"+
	"				font-size: 14px;"+
	"				padding-top: 3px;"+
	"				padding-left: 3px;"+
	"				padding-right: 0px;"+
	"				margin: 0;"+
	"				font-family: \"Microsoft YaHei\",\"ff-tisa-web-pro-1\",\"ff-tisa-web-pro-2\",\"Lucida Grande\",\"Hiragino Sans GB\",\"Hiragino Sans GB W3\",Arial;"+
	"			}"+
	"			p {"+
	"				margin: 0;padding: 0;"+
	"			}"+
	"		</style>"+
	"		<base href=\"${param.basePath}/\" />"+
	"	</head>"+
	"	<body></body>"+
	"</html>";
	
%>

<%
	String action = request.getParameter("action");
	
	if (action == null) {
		
		out.println("没有 action 参数！");
	
	} else if ("under_construction_img".equals(action)) {// 网站构建中图片
	
		out.println("<img style=\"width:60%;max-width: 400px;margin: 0 auto;margin-top:50px;display: block;\" src=\"\" />");
	
	} else if ("htmleditor_iframe".equals(action)) {	 // 网站构建中图片
		
		String html = htmleditor_iframe;
		if(request.getParameter("basePath") != null) {
			html = htmleditor_iframe.replace("${param.basePath}", request.getParameter("basePath"));
		}
		out.println(html);
		
	} else if("http_proxy".equals(action)) { // http 代理，请谨慎外显，为了不必要的流量和运算
		
		String url = request.getParameter("url");
		String params = MapHelper.join(new RequestData(request).ignoreField("url").getParameterMap_String(), "&"); // 不要 url 参数
		out.println(Get.GET(url + '?' + params));
		
	} else if("remoteConsole".equals(action)) { // 手机调试
		
		String msg = "";
		Map<String, String> requestMap = new RequestData(request).getParameterMap_String();
		if (requestMap.size() > 0) {
			msg = StringUtil.HashJoin(requestMap, '&');
		} else {
			msg = "没有参数";
		}

		System.out.println("手机调试参数：" + msg);
		// 可能是 jsonp
		out.println(String.format("(function(){console.log('%s');})();", msg));	
		
	} else if("ip_location".equals(action)){	// 根据请求 ip 显示所在区域

		/*
		 * https://www.ipip.net/
		 * http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=123.123.123.12
		 * 
		 */
		String json = Get.GET("http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=js&ip=" + new Requester(request).getIP());
		out.println(json.replace("var remote_ip_info = ", "").replaceAll(";$", ""));
	} else if ("captchaImg".equals(action)) { // 图片验证码
		Captcha.init(pageContext);
	}  else {
		out.println("nothing2do");
	}
//	else if (request.getRequestURI().contains("weather")) {
//		getWeather();
//	} else if (request.getRequestURI().contains("sendMail")) {
//		sendMail(request, response);
//	} 

	// 如果没有指定城市，则默认依据 ip 地址来得到城市

//		if (request.getParameter("city") != null) {
//			request.isAutoEncodeChinese = true;
//			String city = request.get("city");
//			response.outputJSON(Weather.getWeather(city));
//		} else {
//			String jsCode = getIp_JSON();
//			Map<String, Object> json = Mapper.callExpect_Map(jsCode, "remote_ip_info"); // ip
//																						// 来源城市
//
//			if (json != null && json.get("city") != null) {
//				String city = json.get("city").toString();
//				response.outputJSON(Weather.getWeather(city));
//			} else {
//				response.outputJSON("Get Weather fails！查询天气失败！");
//			}
//		}

%>