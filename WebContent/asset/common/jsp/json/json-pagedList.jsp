<%-- 分页列表的 JSON 输出 --%>
<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
{
	"msg" : "实体列表",
	"total" : ${empty PageResult.totalCount ? 0 : PageResult.totalCount},
	"result" : ${MapOutput}
}