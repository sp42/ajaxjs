<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, com.ajaxjs.database_meta.tools.MVC"%>
<!DOCTYPE html>
<html>
<%@ include file="common/head.jsp" %>
<body>
    <%@ include file="common/nav.jsp" %>
    <h2>${param.title}</h2>
<%
    List<Map<String, Object>> mapList = MVC.showList(request.getParameter("id"));
%>
    <%-- Your JSP content goes here --%>
    <table>
        <thead>
        <tr>
        <%for (String th : MVC.getTh(request)) {%>
             <th><%=th%></th>
        <%}%>
        </tr>
        </thead>
    <% for(Map<String, Object> map : mapList ){%>
        <tr>
        <%for(String key: map.keySet()){%>
            <td><%=map.get(key)%></td>
        <%}%>
        </tr>
    <%}%>
    </table>
    <div class="total" style="text-align:center;color:gray;font-size:12px;margin-top:5px;height:50px"> 共 <%=mapList.size()%> 项</div>
</body>
</html>
