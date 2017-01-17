<%@page pageEncoding="UTF-8"%>
<head>
	<meta charset="utf-8" />
	<link rel="shortcut icon" href="../images/favicon.ico" type="image/x-icon" />
	<style type="text/css">
		body,dl,dt,dd,ul,li,pre,form,fieldset,input,p,blockquote,th,td,h1,h2,h3,h4,h5{margin:0;padding:0;}
		img{border:0;vertical-align:top}ul li{list-style-type:none}.hide{display:none}
		body{
			font-size:100%;
			-webkit-font-smoothing:antialiased;
			font-family:"Microsoft YaHei","ff-tisa-web-pro-1","ff-tisa-web-pro-2","Lucida Grande","Hiragino Sans GB","Hiragino Sans GB W3",Arial;
		}
		*{	
			<%-- 很多Android 浏览器的 a 链接有边框，这里取消它  --%>
			-webkit-tap-highlight-color: rgba(0, 0, 0, 0); 
			<%-- 在IOS浏览器里面，假如用户长按a标签，都会出现默认的弹出菜单事件  --%>  
			-webkit-touch-callout: none;    			
		}
		a{
			color:black;
		}
		a:hover{
			color:#7b7400;
		}
		// code pretty
		.pln{color:#000}@media screen{.str{color:#080}.kwd{color:#008}.com{color:#800}.typ{color:#606}.lit{color:#066}.pun,.opn,.clo{color:#660}.tag{color:#008}.atn{color:#606}.atv{color:#080}.dec,.var{color:#606}.fun{color:red}}@media print,projection{.str{color:#060}.kwd{color:#006;font-weight:bold}.com{color:#600;font-style:italic}.typ{color:#404;font-weight:bold}.lit{color:#044}.pun,.opn,.clo{color:#440}.tag{color:#006;font-weight:bold}.atn{color:#404}.atv{color:#060}}pre.prettyprint{padding:2px;border:1px solid #888}ol.linenums{margin-top:0;margin-bottom:0}li.L0,li.L1,li.L2,li.L3,li.L5,li.L6,li.L7,li.L8{list-style-type:none}li.L1,li.L3,li.L5,li.L7,li.L9{background:#eee}
	</style>
	<meta name="viewport" content="width=320,user-scalable=0,initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0" />
	<%
		request.setAttribute("bigfoot", request.getContextPath() + "/bigfoot");
	%>
<!-- 		<link rel="stylesheet" type="text/css" href="http://192.168.1.9/lessService/?picPath=http://localhost:8080/ui_demo/asset/images&MainDomain=&ns=C:/Users/temp/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ui_demo/asset/less&lessFile=C:/Users/temp/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ui_demo/asset/less/main.less&isdebug=true" /> -->
<!-- 		<link rel="stylesheet" type="text/css" href="http://192.168.1.10/lessService/?picPath=http://localhost:8080/ui_demo/asset/images&MainDomain=&ns=C:/Users/frank/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ui_demo/asset/less&lessFile=C:/Users/frank/workspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ui_demo/asset/less/main.less&isdebug=true" /> -->
	<link rel="stylesheet" type="text/css" href="http://192.168.1.141/lessService/?picPath=http://localhost:8080/cmcc/asset/images&MainDomain=&ns=C:/Users/frank/workspace_new/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ui_demo/asset/less&lessFile=C:/Users/frank/workspace_new/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/jsp_demo/asset/less/main.less&isdebug=true" />

<%-- 	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/asset/css/css.css" /> --%>
    <title>Bigfoot.js ${title}</title>
	<script src="${bigfoot}/js/lang.js"></script>
    <script src="${bigfoot}/js/dom.js"></script>
    <script src="${bigfoot}/js/libs/run_prettify.js"></script>  
</head>