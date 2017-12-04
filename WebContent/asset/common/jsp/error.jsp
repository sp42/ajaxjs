<%@page pageEncoding="UTF-8" isErrorPage="true" import="java.io.*"%>
<%!
	/**
	 * 收集错误信息 输出到网页
	 * 
	 * @param request
	 *            请求对象
	 */
	public static OutputStream getError(HttpServletRequest request, Throwable ex) {
		OutputStream os = new ByteArrayOutputStream();// 创建一个空的字节流，保存错误信息
		PrintStream ps = new PrintStream(os);

		// 不要用 java 7 的 autoClose，因为要在 tomcat 里面手动打开
		try {
			// 收集错误信息
			ps.println("错误代码: " + request.getAttribute("javax.servlet.error.status_code"));
			ps.println("异常 Servlet: " + request.getAttribute("javax.servlet.error.servlet_name"));
			ps.println("出错页面地址: " + request.getAttribute("javax.servlet.error.request_uri"));
			ps.println("访问的路径: " + request.getAttribute("javax.servlet.forward.request_uri"));
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
	<title>错误页面
		code：${requestScope['javax.servlet.error.status_code']}</title>
	<style>
	td {
		font-size: 9pt;
		padding: 6px;
	}
	
	ul {
		padding-left: 30px;
	}
	
	hr {
		width: 98%;
	}
	
	textarea {
			width: 90%;
			min-height: 600px;
			outline:none;
			border:1px solid gray;
			padding:1%;
			margin:1%;
		}
	</style>
</head>
<body leftMargin="0" topMargin="0">
	<table cellSpacing="0" cellPadding="0" border="0" width="100%">
		<tr>
			<td bgColor="#ff6600" rowSpan="2">
				<table cellPadding="12">
					<tr>
						<td width="100%"><B> <FONT color="#ffffff" size="4">集群节点目前无法访问-HTTP
									StatusCode:${requestScope['javax.servlet.error.status_code']}</FONT>
						</B></td>
					</tr>
				</table>
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
	<table borderColor="#111111" cellPadding="12" width="100%" border="0">
		<tr>
			<td><B>欢迎访问！</B> <br /> <br />
				由于服务器负荷较重，目前服务已经将资源分散到集群的不同节点上。由于条件的限制，某些节点没有采取故障转移方案，因此在某些特定节点无法访问时将出现问题。
				<p>
					您访问的节点目前无法访问，这可能是由于服务器升级或停电造成的。由于人为原因(例如升级，这种情况比较常见)暂时无法访问的节点会在2分钟以内恢复。绝大多数情况下，您再次刷新本页就能看到预期的效果。
				</p>
				<p>感谢您的支持！</p> <!-- 
				<p align="right">
					Ext中文网工作室 2007年9月10日 <br /> <A
						href="mailto:mailto:support@ajaxjs.com">mailto:support@ajaxjs.com</A>
				</p>
				 --></td>
		</tr>
	</table>
	<hr />
	<table borderColor="#111111" cellPadding="12" border="0">
		<tr>
			<td width="50%">
				<CENTER>
					<P>
						<b>通常引起中断服务的原因</b>
					</P>
				</CENTER>
				<P>在维护服务器的过程中，我们偶尔会人为地中止一个或多个服务节点的服务，其目的是使服务节点更为有效地服务，或提升其安全性。常见的原因包括：</P>
				<ul>
					<li><b>应用新的安全补丁 <br />
					</b>尽管多数情况下我们会尽量避免重新启动整个节点，但某些安全补丁是针对操作系统核心部件的，因此某些重启是无法避免的。</li>
					<li><b>服务器组件调试</b> <br />
						这种情形并不需要重新启动整个服务节点，但可能需要重新启动Web或其他服务。</li>
					<li><b>停电之前的预防性关机</b> <br />
						如果接到电力部门“即将停电”的通知，我们可能会预先关闭服务器，这样做的目的是保护服务器不受停电的冲击，确保服务器的安全。</li>
					<li><b>不可控因素</b> <br />
						例如停电，光缆突然出现故障等。这类情况我们将和有关部门密切合作尽快解决问题。</li>
				</ul>
			</td>
			<td width="100%">
				<table borderColor="#111111" cellPadding="12" bgColor="#ffffcc" border="1">
					<tr>
						<td><font color="#000080"> <b>即将进行的工作<FONT face="Wingdings">&#216;</FONT></b>
						</font>
							<P>
								<b>将本服务升级为更新版本的系统</b>
							</P>
							<P>我们会尽一切可能降低这一过程对核心服务造成的影响。目前为止，所有的调试过程都是在单独的计算机上进行的，一旦调试完成，我们会尽快完成迁移和升级动作。</P>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<hr />
	<div align="center">
			<a href="${pageContext.request.contextPath}">回首页</a> | <a href="javascript:history.go(-1);">上一页</a>
	</div>
	&nbsp;&nbsp;&nbsp;异常信息：
	<textarea><%
	 out.print(getError(request, exception));
%></textarea>
</body>
</html>