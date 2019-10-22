<%@page pageEncoding="UTF-8" import="java.util.*, java.net.*, java.io.*, com.ajaxjs.net.http.NetUtil"%>
<%! 
	public static String getLanguage(HttpServletRequest request) {
		String  targetLanguage = null;
		String force = request.getParameter("force");
	
		if (force != null) {
			targetLanguage = force;
		} else {
			java.util.Locale l = request.getLocale();
			String locate = l.toString();
		
			if (locate.indexOf("en") != -1) {
				targetLanguage = "eng";
			} else if (locate.indexOf("zh") != -1 || locate == null) {
				targetLanguage = "zhCN";
			}
		}
		
		return targetLanguage;
	}

	public static String getUrl(String url) throws Throwable {
		String html = NetUtil.get("https://gitee.com/sp42_admin/ajaxjs", true, conn -> {
			NetUtil.setUserAgentDefault.accept(conn);
		});
		
		return html;
	}

	public static String get() {
		try{
			String str = substringsBetween(getUrl("https://gitee.com/sp42_admin/ajaxjs"), "<div class='file_content markdown-body'>", "if ($('.markdown-body').children(\"style\").length != 0) {");
			str = str.replace("<script>", "").replace("use strict", "").replace("</div>","").replace("</div>","").replace("\"\";", "");
			str = str.replace("", "");
			str = str.replaceAll("/sp42_admin/ajaxjs/blob", "https://gitee.com/sp42_admin/ajaxjs/blob");
			return str;
		}catch(Throwable e){
			e.printStackTrace();
			return null;
		}
	}
	public static String substringsBetween(final String str, final String open, final String close) {
		final int strLen = str.length();
		final int closeLen = close.length();
		final int openLen = open.length();
		final List<String> list = new ArrayList<>();
		int pos = 0;
		while (pos < strLen - closeLen) {
			int start = str.indexOf(open, pos);
			if (start < 0) {
				break;
			}
			start += openLen;
			final int end = str.indexOf(close, start);
			if (end < 0) {
				break;
			}
			list.add(str.substring(start, end));
			pos = end + closeLen;
		}
		
		return String.join("", list);
	}
%>