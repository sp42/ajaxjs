<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="java.util.*, java.sql.*, com.ajaxjs.cms.controller.DataBaseShowStruController,com.ajaxjs.framework.dao.MockDataSource, com.ajaxjs.mvc.filter.DataBaseFilter,com.ajaxjs.orm.JdbcConnection"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
	<title>表设计文档</title>
	<link rel="stylesheet" type="text/css" href="../asset/css/sqlDoc.css" />
</head>
<body>
	<div class="leftSide">
		<br /> <br /> <br />
		<h3>${aj_allConfig.site.titlePrefix} 表设计文档</h3>
		<h5>2017.6.20 更新</h5>
		<menu>
			<ol class="level_1">

				<li>
					<h4>用户</h4>
					<ol>
						<li><a href="#user">用户信息表</a></li>
						<li><a href="#user_common_auth">用户普通口令登录</a></li>
						<li><a href="#user_oauth">用户第三方登录</a></li>
						<li><a href="#user_login_log">用户登录日志</a></li>
					</ol>
				</li>
				<li>
					<h4>实体类型</h4>
					<ol>
						<li><a href="#article">文章</a></li>
						<li><a href="#album">专辑</a></li>
						<li><a href="#topic">专题</a></li>
						<li><a href="#vod">视频点播</a></li>
						<li><a href="#album_pic">相册</a></li>
					</ol>
				</li>
				<li>
					<h4>栏目</h4>
					<ol>
						<li><a href="#section_info">栏目信息</a></li>
						<li><a href="#section_content">栏目列表</a></li>
					</ol>
				</li>
				<li>
					<h4>系统通用表</h4>
					<ol>
						<li><a href="#general_catelog">分类</a></li>
						<li><a href="#general_data_dict">数据字典</a></li>
						<li><a href="#general_log">全局操作日志</a></li>
						<li><a href="#attachment">附件</a></li>
						<li><a href="#attachment_picture">附件：图片</a></li>
					</ol>
				</li>

			</ol>
		</menu>


		<div
			style="height: 30px; width: 82%; margin: 0 5%; overflow: hidden; line-height: 30px; white-space: nowrap; font-size: .9rem; color: gray; position: relative;">
			<div class="mask"></div>
			<div class="m" style="width: 100%; overflow: hidden;">Powered
				by AJAXJS Framework!</div>
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
			DataBaseFilter.initDb();
			Connection conn = JdbcConnection.getConnection();
		
			List<String> tables = DataBaseShowStruController.getAllTableName(conn);
			Map<String, String> tablesComment = DataBaseShowStruController.getCommentByTableName(conn, tables);
			Map<String, List<Map<String, String>>> infos = DataBaseShowStruController.getColumnCommentByTableName(conn, tables);

			for (String tableName : tables) {
				if ("template".equals(tableName))
					continue;
				List<Map<String, String>> info = infos.get(tableName);
		%>
		<a name="<%=tableName%>"></a>
		<h3><%=tableName%></h3>
		<p><%=tablesComment.get(tableName)%></p>

		<table class="ajaxjs-niceTable">
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
			JdbcConnection.clean();
		%>
	</div>
</body>
</html>