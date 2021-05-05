<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.*, java.sql.*,com.ajaxjs.developer.controller.DataBaseStruController,com.ajaxjs.framework.filter.DataBaseFilter,com.ajaxjs.sql.JdbcConnection"
	pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%
	DataBaseFilter.initDb();
	Connection conn = JdbcConnection.getConnection();
	
	List<String> tables = DataBaseStruController.getAllTableName(conn);
	Map<String, String> tablesComment = DataBaseStruController.getTableComment(conn, tables);
	Map<String, List<Map<String, String>>> infos = DataBaseStruController.getColumnComment(conn, tables);
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
	<title>表设计文档</title>
	<link rel="stylesheet" href="${aj_static_resource}/dist/css/all.css" />
	<style type="text/css">
		h3 {
			padding-top: 4%;
		}
		
		p {
			padding: 1% 0;
		}
		
		h3, table, p {
			width: 90%;
			margin: 0 auto;
		}
		
		
		
		.leftSide {
			float: left;
			width: 18%;
			height: 100%;
			border-right: 1px solid lightgray;
			box-sizing: border-box;
			background-color: #e6e6e6;
			overflow: auto;
		}
		
		.rightSide {
			float: right;
			width: 82%;
			height: 100%;
		}
		
		h3 {
			text-align: center;
			font-size:20px;
		}
		
		h4 {
			letter-spacing: 2px;
			margin: 4% 0;
			font-size: .95rem;
		}
		
		h5 {
			text-align: center;
			color: gray;
			font-weight: normal;
			margin-top: 0;
		}
		
		iframe {
			width: 100%;
			height: 99.2%;
			border: 0;
		}
		
		ol.level_1 {
			list-style-type: cjk-ideographic;
		}
		
		ol.level_1 li {
			
		}
		
		ol.level_1 li li {
			margin-bottom: 2%;
			font-size: .9rem;
		}
		
		ol ol {
			padding-left: 5%;
		}
		
		a {
			color: black;
			text-decoration: none;
		}
		
		menu {
			padding-left: 20px;
		}
		
		.rightSide{
			overflow: scroll;
		}
	</style>
</head>
<body>
	<div class="leftSide">
		<br /> <br /> <br />
		<h3>${aj_allConfig.site.titlePrefix} 表设计文档</h3>
		<h5></h5>
		<menu>
			<ol class="level_1">
			<c:foreach items="<%=tables%>" var="item">
				<li><a href="#${item}">${item}</a></li>
			</c:foreach>
			</ol>
		</menu>

		<div
			style="height: 30px; width: 82%; margin: 0 5%; overflow: hidden; line-height: 30px; white-space: nowrap; font-size: .9rem; color: gray; position: relative;">
			<div class="mask"></div>
			<div class="m" style="width: 100%; overflow: hidden;">Powered by AJAXJS Framework!</div>
			<script>
				window.setInterval(function() { // 跑马灯
					var m = document.querySelector('.m');
					var queen = m.innerHTML.split('');
					queen.push(queen.shift());
					m.innerHTML = queen.join('');
				}, 500);
			</script>
		</div>
	</div>
	<div class="rightSide">
		<%
			for (String tableName : tables) {
				if ("template".equals(tableName))
					continue;
				List<Map<String, String>> info = infos.get(tableName);
		%>
		<a name="<%=tableName%>"></a>
		<h3><%=tableName%></h3>
		<p><%=tablesComment.get(tableName)%></p>

		<table class="aj-niceTable">
			<thead>
				<th>字段名</th>
				<th>类型（长度）</th>
				<th>允许为空</th>
				<th>字段说明</th>
			</thead>
		<%
			for (Map<String, String> i : info) {
					request.setAttribute("info", i);
		%>
			<tr>
				<td width="20%" style="text-align: left; padding-left: 3%;">${info.name}</td>
				<td width="15%">${info.type.toUpperCase()}</td>
				<td width="10%">${info['null'].toLowerCase()}</td>
				<td style="text-align: left;">${info.comment}</td>
			</tr>

		<%
			}
		%>

		</table>
		<%
			}
		
			JdbcConnection.closeDb();
		%>
	</div>
</body>
</html>