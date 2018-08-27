<%-- 分页列表的 JSON 输出 --%>
<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" %>
{
	"isOk" : true,
	"msg" : "实体列表",
	"total" :  ${empty PageResult.totalCount || PageResult.zero ? 0 	: PageResult.totalCount},
	"result" : ${empty PageResult.totalCount || PageResult.zero ? '[]' 	: MapOutput}
}