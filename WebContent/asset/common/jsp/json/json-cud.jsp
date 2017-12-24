<%@ page contentType="application/json; charset=UTF-8"
	pageEncoding="UTF-8" import="com.ajaxjs.mvc.view.PageTag"
	trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="/ajaxjs"%><%
	request.setAttribute("PageUtil", new PageTag());
%><c:choose>
	<c:when test="${not empty errMsg}">
{
	"isOk" : false,
	"msg" : "${isUpdate || not empty isUpdate? '修改' : '创建'}失败！原因：${PageUtil.jsonString_covernt(errMsg)}"
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