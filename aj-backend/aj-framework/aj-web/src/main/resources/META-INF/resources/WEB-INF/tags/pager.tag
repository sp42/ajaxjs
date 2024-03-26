<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" import="com.ajaxjs.web.website.data.GetData"%>

<%
    // 获取参数
    int start = request.getParameter("start") == null ? 0 : Integer.parseInt(request.getParameter("start"));
    int pageSize = request.getParameter("pageSize") == null ? 10 : Integer.parseInt(request.getParameter("pageSize"));
    // 获取总记录数
    int total = (int) request.getAttribute("PAGE_TOTAL");
    // 计算总页数
    int totalPage = (total + pageSize - 1) / pageSize;

    String params = GetData.getQueryString(request);
    if (params == null)
        params = "";
%>
<div class="pagination">
    <ul>
        <li><a href="?start=0&pageSize=<%=pageSize%><%=params%>">首页</a></li>
        <li><a
            href="?start=<%=(start - pageSize) >= 0 ? (start - pageSize) : 0%>&pageSize=<%=pageSize%><%=params%>">上一页</a></li>
        <%
        for (int i = 1; i <= totalPage; i++) {
            if (i == (start / pageSize + 1)) {
        %>

        <li class="current">
            <%
            } else {
            %>
        <li>
            <%
            }
            %> <a
            href="?start=<%=(i - 1) * pageSize%>&pageSize=<%=pageSize%><%=params%>"><%=i%></a>
        </li>
        <%
        }
        %>
        <li><a
            href="?start=<%=(start + pageSize) < total ? (start + pageSize) : total%>&pageSize=<%=pageSize%><%=params%>">下一页</a></li>
        <li><a
            href="?start=<%=totalPage * pageSize - 1%>&pageSize=<%=pageSize%><%=params%>">尾页</a></li>
        <li>共<%=totalPage%>页，全部<%=total%>条记录</li>
    </ul>
</div>