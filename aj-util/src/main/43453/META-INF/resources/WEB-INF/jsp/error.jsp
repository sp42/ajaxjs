<%@page pageEncoding="UTF-8" isErrorPage="true" import="java.io.*" trimDirectiveWhitespaces="true"%>
<%!
	/**
	 * 获取相关信息
	 * 
	 * @param request 请求对象
	 */
	public static String getSth(HttpServletRequest request, String key) {
		Object obj = request.getAttribute(key);
		if(obj == null) {
			return null;
		} else {
			return obj.toString().replaceAll("<|>", "");
		}
	}

	/**
	 * 收集错误信息 输出到网页
	 * 
	 * @param request 请求对象
	 */
	public static OutputStream getError(HttpServletRequest request, Throwable ex) {
		OutputStream os = new ByteArrayOutputStream();// 创建一个空的字节流，保存错误信息
		PrintStream ps = new PrintStream(os);

		// 不要用 java 7 的 autoClose，因为要在 tomcat 里面手动打开
		try {
			// 收集错误信息
			ps.println("错误代码: " + getSth(request, "javax.servlet.error.status_code"));
			ps.println("异常 Servlet: " + getSth(request, "javax.servlet.error.servlet_name"));
			ps.println("出错页面地址: " + getSth(request, "javax.servlet.error.request_uri"));
			ps.println("访问的路径: " + getSth(request, "javax.servlet.forward.request_uri"));
			ps.println();

			for (String key : request.getParameterMap().keySet()) {
				ps.println("请求中的 Parameter 包括：");
				ps.println(key + "=" + request.getParameter(key));
				ps.println();
			}

			if (request.getCookies() != null) {
				for (Cookie cookie : request.getCookies()) {
					ps.println("请求中的 Cookie 包括：");
					ps.println(cookie.getName() + "=" + cookie.getValue());
					ps.println();
				}
			}
			// javax.servlet.jspException 等于 JSP 里面的 exception 对象
			if (ex != null) {
				ps.println("堆栈信息");
				ex.printStackTrace(ps);
				ps.println();
			}

			return os;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				os.close();
				ps.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<meta name="viewport" content="width=320,user-scalable=0,initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0" />
	<title>错误页面 code：${requestScope['javax.servlet.error.status_code']}</title>
	<style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        
        td {
            padding: 6px;
            color: white;
            font-size: 1.3rem;
        }
		
		hr {
			width: 98%;
		}
		
		textarea {
			width: 96%;
			min-height: 600px;
			outline: none;
			border: 1px solid gray;
			padding: 1%;
			margin: 1%;
			font-family: Courier New;
		}
	</style>
</head>
<body leftMargin="0" topMargin="0">
	<table>
		<tr>
			<td bgColor="#ff6600" rowSpan="2">
				服务端异常-HTTPStatusCode:${requestScope['javax.servlet.error.status_code']}
			</td>
			<td width="32" bgColor="#ff6600" height="32"></td>
			<td width="32" bgColor="#ff9933" height="32"></td>
			<td width="32" bgColor="#ffcc66" height="32"></td>
			<td width="32" bgColor="#ffee99" height="32"></td>
		</tr>
		<tr>
			<td width="32" bgColor="#ff9933" height="32"></td>
			<td width="32" bgColor="#ffcc66" height="32"></td>
			<td width="32" bgColor="#ffee99" height="32"></td>
			<td width="32" height="32"></td>
		</tr>
	</table>
	<p>&nbsp;&nbsp;&nbsp;非常抱歉，服务器运行出错！异常信息：</p>
	<textarea><%
			out.print(getError(request, exception));
		%></textarea>
	<div align="center">
		<a href="${pageContext.request.contextPath}">回首页</a> | <a
			href="javascript:history.go(-1);">上一页</a>
	</div>
</body>
</html>