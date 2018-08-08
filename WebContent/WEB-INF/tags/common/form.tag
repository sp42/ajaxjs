<%@tag pageEncoding="UTF-8" description="表单标签封装" import="java.util.HashMap"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@attribute name="type" type="String" required="true" description="哪一个模块"%>
<%
	if ("captcha".equals(type)) { // 验证码
%>
	<!-- 验证码 -->
	<table>
		<tr>
			<td>
				<input type="text" name="" value="" style="width: 80%; float: left;" placeholder="输入右侧验证码" data-regexp="integer" required />
			
			</td>
			<td style="vertical-align: top;">
				<img src="${pageContext.request.contextPath}/Captcha/"
					onclick="this.src=this.src + '?' + new Date;" alt="点击刷新图片"
					title="点击刷新图片" style="width: 30%; cursor: pointer; margin-left: 2%;height:26px;width:60px;" />
			</td>
		</tr>
	</table>
	<!-- // 验证码 -->

<%
	}
%>