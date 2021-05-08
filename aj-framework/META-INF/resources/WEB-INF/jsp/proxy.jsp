<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
	import="java.util.*, java.io.*, com.ajaxjs.util.*, com.ajaxjs.util.map.MapTool, com.ajaxjs.util.io.*, com.ajaxjs.util.cryptography.SymmetriCipher, com.ajaxjs.net.http.*"%>
<%!
	// 网络代理
	
	/**
	 * 生成加密字符串
	 * 
	 * @return Token
	 */
	public static String makeToken() {
		return Encode.urlEncode(SymmetriCipher.AES_Encrypt(PERFIX + System.currentTimeMillis(), KEY));
	}
	

	// TEST TOKEN: GLeuu144wigskmNmJY777uniCT12Mv8A21sU4Tw%2FAJA%3D
	private final static String KEY = "^4S5$0F";

	private final static String PERFIX = "ajaxjs-proxy";

	String get(HttpServletRequest request) {
		//System.out.println("request........");
		String token = request.getParameter("token"), url = request.getParameter("url");

		String output = "empty";

		if (CommonUtil.isEmptyString(token) || CommonUtil.isEmptyString(url))
			output = "缺少参数 token/url";
		else {
			try {
				if (SymmetriCipher.AES_Decrypt(token, KEY).startsWith(PERFIX)) {
					if("GET".equalsIgnoreCase(request.getMethod())) {
						String qs = request.getQueryString();
						qs = qs.replace("url=" + url, "").replace("token=" + Encode.urlEncode(token), "").replace("&", "");
						if (!CommonUtil.isEmptyString(qs))
							url += "?" + qs;
						
						output = NetUtil.get(url);
					} else if("POST".equalsIgnoreCase(request.getMethod())) {
						Map<String, Object> map = MapTool.as(request.getParameterMap());
						map.remove("url");
						map.remove("token");
						//System.out.println("::" + map);
						output = NetUtil.post(url, map);
						//output = NetUtil.get(url);
					} else 
						output ="不支持的 HTTP 方法";
				} else
					output = token + " 校验失败";
			} catch (Throwable e) {
				e.printStackTrace();
				output = token + " 校验失败";
			}
		}

		//System.out.println("output:"+output);
		return output;
	}%>


<%=get(request)%>