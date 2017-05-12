<%-- 分页列表的 JSON 输出 --%>
<%@page contentType="application/json; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:useBean id="js" class="com.ajaxjs.js.Bean2Json" />
{
	"msg" : "${empty ServiceException ? 'ok' : ServiceException}",
	"total" : ${empty PageResult.totalCount ? 0 : PageResult.totalCount},
	"result" : ${empty MapOutput ? js.list(PageResult.rows) : MapOutput}
}