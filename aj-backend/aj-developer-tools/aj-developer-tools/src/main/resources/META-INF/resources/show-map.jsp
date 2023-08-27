<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*, com.ajaxjs.developertools.mpb.MVC,
com.ajaxjs.developertools.mpb.Explain"%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="common/head.jsp" %>
        <link rel="stylesheet" type="text/css" href="http://www.ajaxjs.com/public/mbp-list.css" />
    </head>
    <body>
        <%@ include file="common/nav.jsp" %>
        <h2>${param.title}</h2><h3>${not empty param.note ? param.note : ''}</h3>
    <%
        String id = request.getParameter("id");
        Map<String, String> map = MVC.showMap(id);
    %>
        <%-- Your JSP content goes here --%>
        <table>
            <thead>
            <tr>
                 <th>名称</th><th>值</th>
            </tr>
            </thead>
               <%for(String key: map.keySet()) {
                String explain = Explain.show(id, key);
               %>
               <tr>
                   <td><%=key%><div class="explain" title="<%=explain%>"><%=explain%></div></td>
                   <td><%=map.get(key)%></td>
               </tr>
               <%}%>
        </table>
        <div class="total" style="text-align:center;color:gray;font-size:12px;margin-top:5px"> 共 <%=map.size()%> 项</div>
    </body>
</html>
