<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:useBean id="js" class="com.ajaxjs.javascript.RhinoMapper" />
{
	"isOk" : true,
	"result" : ${js.singlePojo(info)}
} 