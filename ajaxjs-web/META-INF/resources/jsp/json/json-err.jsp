<%-- 错误信息 JSON 输出 --%>
<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="json" class="com.ajaxjs.js.JsonHelper" scope="request" /> 
{
	"errorMsg": "${json.javaValue2jsonValue(errMsg)}",
	"isOk": false
}