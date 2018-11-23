<%-- 分页列表的 JSON 输出 --%>
<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
{
	"isOk" : true,
	"msg" : "实体列表",
	"total" :  ${empty PageResult.getTotalCount() || PageResult.isZero() ?  0 	: PageResult.getTotalCount()},
	"result" : ${empty PageResult.getTotalCount() || PageResult.isZero() ? '[]': MapOutput}
}