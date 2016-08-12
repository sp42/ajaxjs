<%@page pageEncoding="UTF-8" isErrorPage="true" import="java.io.*"%>
<%!/**
	 * 收集错误信息 输出到网页
	 * 
	 * @param request
	 *            请求对象
	 */
	public static OutputStream getError(HttpServletRequest request, Throwable ex) {
		OutputStream os = new ByteArrayOutputStream();// 创建一个空的字节流，保存错误信息
		PrintStream ps = new PrintStream(os);
		
		// 不要用 java 7 的 autoClose，因为要在 tomcat 里面手动打开
		try{
			// 收集错误信息
			ps.println("错误代码: " +     request.getAttribute("javax.servlet.error.status_code")); 
			ps.println("异常 Servlet: " + request.getAttribute("javax.servlet.error.servlet_name"));
			ps.println("出错页面地址: " + request.getAttribute("javax.servlet.error.request_uri"));
			ps.println("访问的路径: " + 	 request.getAttribute("javax.servlet.forward.request_uri"));
			ps.println();
	
			for (String key : request.getParameterMap().keySet()) {
				ps.println("请求中的 Parameter 包括：");
				ps.println(key + "=" + request.getParameter(key));
				ps.println();
			}
			
			if(request.getCookies() != null){
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
		} finally{
			try{
				os.close();
				ps.close();
			} catch(IOException e){
				e.printStackTrace();
			}
		}
	}
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8" />
	<title>错误页面 code：${requestScope['javax.servlet.error.status_code']}</title>
	<style>
		body {
			max-width: 600px;
			min-width: 320px;
			margin: 0 auto;
			padding-top: 2%;
		}
		textarea {
			width: 100%;
			min-height: 300px;
			outline:none;
			border:1px solid gray;
			padding:1%;
		}
		h1 {
			text-align: right;
			color: lightgray;
		}
		div {
			margin-top: 1%;
		}
	</style>
</head>
<body>
	<h1>抱 歉……</h1>
	<div style="padding:2% 0;text-indent:2em;">尊敬的用户：我们致力于提供更好的服务，但人算不如天算，有些错误发生了，希望是在控制的范围内。如果问题重复出现，请向系统管理员反馈。</div>
	<textarea><%
	 out.print(getError(request, exception));
%></textarea>
	<div align="center">
			<a href="${pageContext.request.contextPath}">回首页</a> | <a href="javascript:history.go(-1);">上一页</a>
	</div>
</body>
</html>