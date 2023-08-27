<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Developer Tools</title>
        <%@ include file="common/head.jsp" %>
        <style>
            html, body {
                height:100%;
            }
        </style>
    </head>
    <body>
        <menu>
            <h3 style="margin-left:13px;color:#2f518c">Developer Tools</h3>
            <br />
            <ul>
                <li>
                    <a href="dashboard.jsp" target="iframe">首页</a>
                </li>
                <li>
                    <a href="dashboard.jsp" target="iframe">系统</a> | <a href="jvm.jsp" target="iframe">JVM</a> | <a href="tomcat.jsp" target="iframe">Tomcat</a>
                </li>
                <li>
                    <a target="iframe" href="mysql_probe/">MySQL 探针</a> | <a href="database-doc">MySQL 浏览器</a> | <a href="toolkit.jsp">MySQL 监控</a>
                </li>
                <li>
                    <a target="iframe" href="toolkit/task.html">定时任务管理</a> | <a target="iframe" href="toolkit/toolkit.jsp">小工具</a>
                </li>
            </ul>
        </menu>
        <div class="container">
            <iframe src="dashboard.jsp" frameborder="0" name="iframe"></iframe>
        </div>
    </body>
</html>