<%-- 分页列表的 JSON 输出 --%>
<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" import="com.ajaxjs.js.JsonHelper"%>
<jsp:useBean id="js" class="com.ajaxjs.js.JsonHelper" />
{
	"msg" : "实体列表",
	"total" : ${empty PageResult.totalCount ? 0 : PageResult.totalCount},
	"result" : ${empty MapOutput ? js.beans2json(PageResult.rows) : MapOutput}
}