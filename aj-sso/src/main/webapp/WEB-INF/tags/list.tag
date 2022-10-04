<%@ tag pageEncoding="UTF-8" import="java.sql.Connection, com.ajaxjs.util.JspHelper" %>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="/ajaxjs" %>
<%@attribute fragment="false" required="true" name="namespace" description="命名"%>
<%@attribute fragment="false" required="true" name="namespace_chs" description="命名（中文）"%>
<%@attribute fragment="false" required="false" name="show_create" type="Boolean" description=""%>
<%
	Connection conn = JspHelper.init(request);
	JspHelper.getList(request);
	conn.close();
%> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<jsp:include page="/common/head.jsp" />
<style>
html, body, body>table {
	height: 100%;
}

h1 {
	font-weight: bold;
	font-size: 18pt;
	text-align: left;
	letter-spacing: 2px;
}
.list_panel {
	min-height: 600px;
}

.search-panel {
	text-align: left;
}

.search-panel form {
	float: right;
}

.list_panel .list {
	clear: both;
	margin-top: 20px;
}

.list_panel .list table {
	margin: 10px 0;
	width: 100%;
}
</style>
</head>
<body class="middleHeight">
	<table>
		<tr>
			<td>
				<div class="list_panel">
					<h1>${namespace_chs}管理</h1>
					<div class="search-panel">
						<form class="form">
							<input type="text" />
							<button><i class="fa-solid fa-magnifying-glass" style="color:green;"></i>搜索</button>
						</form>
						<c:if test="${empty show_create or show_create}">
							<button style="margin-top: 20px" onclick="location.assign('../info/${namespace}.jsp');">
							<i class="fa-solid fa-plus" style="color:green;"></i>新增${namespace_chs}</button>
						</c:if>
					</div>
					<div class="list">
						<jsp:doBody />
						<div style="color: gray; font-size: 9pt; margin: 20px;text-align: center;">共
							${JSP_HELPER.getListSize(list)} 笔记录</div>
					</div>
				</div>
			</td>
		</tr>
	</table>
	<script>
		function del(id, name) {
			if(confirm('警告：\n是否删除记录 [' + (name || id) + "]？")) {
				alert('deleted!')
			}
		}
	</script>
</body>
</html>