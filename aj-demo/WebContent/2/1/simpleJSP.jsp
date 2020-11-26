<%@page pageEncoding="UTF-8" import="java.util.Date"%>
<%@page trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title><%="欢迎！"%></title>
    <style>
    body {
        margin: 5%;
        padding: 0;
    }
    </style>
    <link rel="stylesheet" type="text/css" href="../../common/main.css" />
</head>
<body>
            当前日期是 <%= new Date().toString()%> 
    <%@include file="../../WEB-INF/jsp/footer.jsp"%>
</body>
</html>