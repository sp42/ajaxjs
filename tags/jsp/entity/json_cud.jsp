<%@ page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
    <c:when test="${not empty errMsg}">
{
	"isOk" : false,
	"msg" : "${not empty isUpdate ? '修改' : '创建'}失败！原因：${errMsg}"
}
    </c:when>
     <c:when test="${not empty isUpdate}">
{
	"isOk" : true,
	"msg" : "修改成功！"
}
    </c:when>
    <c:otherwise>
{
	"isOk" : true,
	"msg" : "创建成功",
	"newlyId" : ${newlyId}
} 

    </c:otherwise>
</c:choose>
