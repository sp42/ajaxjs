<%@page pageEncoding="UTF-8"%>
<%@include file="tools.jsp" %>
<!DOCTYPE html>
    <html>
    <head>
        <meta charset="UTF-8">
        <title>AJAXJS Web</title>
        <link rel="icon" type="image/x-icon" href="asset/images/favicon.ico" />
        <link rel="shortcut icon" type="image/x-icon" href="asset/images/favicon.ico" />
        <meta name="keywords" content="ajaxjs java js javascript web ui framework html5 ioc aop orm restful" />
        <meta name="description" content="AJAXJS Web 是一款基于 Java 平台的开源 Web 框架，继承了 Java 平台的高效、安全、稳定、跨平台等诸多优势， 但却摒弃了传统企业级架构所带来的庞大和臃肿，强调轻量级，非常适合互联网小型网站的快速应用。" />
        <meta name="viewport" content="width=320, user-scalable=0, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
        <link  type="text/css" rel="stylesheet" href="asset/style.css" />
    </head>

    <body>
    <%
    	String targetLanguage = getLanguage(request);
		if (targetLanguage.equals("eng")) {
	%>
          <%@include file="index-eng.jsp" %>
            <%
		} else if (targetLanguage.equals("zhCN") || targetLanguage.equals("zhTW")) {
	%>
            <style>
                p,  li {
                    letter-spacing: 1px;
                    text-align: justify;
                    word-break: break-all;
                }
            </style>
            <section class="page-header">
                <h1>AJAXJS Web Framework</h1>
                <h2 class="project-tagline">全栈 、轻量级、函数式&amp;响应式编程</h2>
            </section>

            <section class="main-content">
                <!-- Editable AREA|START -->
                <%=get()%>
                <footer>
                 	粤ICP备18053388号 | ©1999-2018 Frank 
                  	<span class="language"><a href="?force=eng">English</a> | <a href="?force=zhTW">正体中文</a></span>
                </footer>
                <!-- Editable AREA|END -->
            </section>

           <%
		}
        
        if (targetLanguage.equals("zhTW")) {
        	%>
        	<script>
        	
        	</script>
        	<%
        }
	%>
		<!-- CNZZ 统计 -->
		<div style="display: none;">
		    <script src="https://s11.cnzz.com/z_stat.php?id=1261173351&web_id=1261173351"></script>
		</div>
		<!-- // CNZZ 统计 -->
    </body>
    </html>