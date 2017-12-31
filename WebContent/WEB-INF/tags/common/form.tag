<%@tag pageEncoding="UTF-8" description="表单标签封装" import="java.util.HashMap"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@attribute name="type" type="String" required="true" description="哪一个模块"%>
<%
	if ("captcha".equals(type)) { // 验证码
%>
	<!-- 验证码 -->
	<input type="text"
		name="<%=com.ajaxjs.simpleApp.controller.CaptchaImgController.submitedFieldName%>"
		value="" style="width: 40%; float: left;" placeholder="输入右侧验证码"
		data-regexp="integer" required />
	<img src="${pageContext.request.contextPath}/showCaptchaImg/show.do"
		onclick="this.src=this.src + '?' + new Date;" alt="点击刷新图片"
		title="点击刷新图片" style="width: 30%; cursor: pointer; margin-left: 2%;height:26px;width:60px;" />
	<!-- // 验证码 -->

<%
	}
%>
