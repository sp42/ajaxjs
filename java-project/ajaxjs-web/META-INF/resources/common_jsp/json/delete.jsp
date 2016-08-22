<%@ page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs" %>
<c:choose>
    <c:when test="${not empty errMsg}">
{
	"isOk" : false,
	"msg" : "删除失败！原因：${errMsg}"
}
    </c:when>
 
    <c:otherwise>
{
	"isOk" : true,
	"msg" : "删除成功！"
} 
    </c:otherwise>
</c:choose>
