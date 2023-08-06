<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Developer Tools</title>
        <%@ include file="common/head.jsp" %>
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
                    <a href="">首页</a>
                </li>
            </ul>
        </menu>
        <div class="container">
            <iframe src="dashboard.jsp" frameborder="0" name="iframe"></iframe>
        </div>
    </body>
</html>