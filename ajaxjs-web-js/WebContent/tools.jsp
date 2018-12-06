<%@page pageEncoding="UTF-8" import="java.util.*, java.net.*, java.io.*"%>
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
		StringBuilder result = new StringBuilder();

		// InputStreamReader 从一个数据源读取字节，并自动将其转换成 Unicode 字符
		// 相对地，OutputStreamWriter 将字符的 Unicode 编码写到字节输出流
		try (InputStreamReader inReader = new InputStreamReader(new URL(url).openStream(), java.nio.charset.StandardCharsets.UTF_8);
				/*
				 * Decorator，装饰模式，又称为 Wrapper，使它具有了缓冲功能 BufferedInputStream、BufferedOutputStream
				 * 只是在这之前动态的为它们加上一些功能（像是缓冲区功能）
				 */
				BufferedReader reader = new BufferedReader(inReader);) {

			String line = null;
			while ((line = reader.readLine()) != null) { // 一次读入一行，直到读入null为文件结束
				// 指定编码集的另外一种方法 line = new String(line.getBytes(), encodingSet);
				result.append(line);
				result.append('\n');
			}
		}
		
		return result.toString();
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