<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="js" class="com.ajaxjs.util.json.JsLib" />
{
	"isOk" : true,
	"result" : ${js.singlePojo(info)}
} 