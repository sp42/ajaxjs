<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="com.ajaxjs.framework.spring.filter.dbconnection.DataBaseConnection,
java.util.*, com.ajaxjs.data.jdbc_helper.JdbcConn, com.ajaxjs.data.CRUD"%>
<!DOCTYPE html>
<html>
<%@ include file="common/head.jsp" %>
<body>
    <%@ include file="common/nav.jsp" %>
    <h1>对访问统计</h1>
<%
    DataBaseConnection.initDb();
    List<Map<String, Object>> mapList = CRUD.listMap("perf_objects_summary", null);

    JdbcConn.closeDb();
%>

    <%-- Your JSP content goes here --%>
    <table>
        <thead>
        <tr>
             <th>对 象</th><th>记录数<br />count_star</th><th>等待时间<br />WAIT_MS</th><th>最小等待时间<br />MIN_WAIT_MS</th><th>最大等待时间<br/>MAX_WAIT_MS</th><th>平均等待时间 <br/>AVG_WAIT_MS</th>
        </tr>
        </thead>

        <%@ include file="common/table.jsp" %>
    </table>
</body>
</html>
