<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="js" class="com.ajaxjs.js.Bean2Json" />
<%
	if (request.getParameter(com.ajaxjs.mvc.controller.MvcRequest.callback_param) == null) {
%>
{ "isOk" : true, "result" : ${js.singlePojo(info)} }
<%
	} else {
%>
${param.callback_param}(
	{ "isOk" : true, "result" : ${js.singlePojo(info)} }
);
<%
	}
%>