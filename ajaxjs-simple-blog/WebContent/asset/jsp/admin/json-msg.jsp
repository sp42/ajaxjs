<%@ page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs" %>
<c:choose>
    <c:when test="${not empty errMsg}">
{
	"isOk" : false,
	"msg" : "操作失败！原因：${errMsg}"
}
    </c:when>
 
    <c:otherwise>
{
	"isOk" : true,
	"msg" : "操作成功！${okMsg}"
} 
    </c:otherwise>
</c:choose>
