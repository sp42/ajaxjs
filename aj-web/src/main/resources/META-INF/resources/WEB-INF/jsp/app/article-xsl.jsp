<%@page import="java.util.*, java.io.*" pageEncoding="utf-8" contentType="application/msexcel"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>XSL</title>
	<meta charset="utf-8" />
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
</head>
<body>
	<table border="1">
		<thead>
			<tr>
				<th>#</th>
				<th class="name">${uiName}名称</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>分 类</th>
				<th>是否上线</th>
			</tr>
		</thead>

		<tbody>
			<c:foreach items="${PageResult}">
				<tr>
					<td style="mso-number-format:'/@';" sdnum="2052;0;@">${item.id}</td>
					<td title="${item.intro}">${item.name}</td>
					<td><c:dateFormatter value="${item.createDate}" format="yyyy-MM-dd" /></td>
					<td><c:dateFormatter value="${item.updateDate}" format="yyyy-MM-dd" /></td>
					<td>${newsCatalogs[item.catalogId].name}</td>
					<td>${(empty item.stat || item.stat == 1) ? '已上线': '已下线'}</td>
				</tr>
			</c:foreach>
		</tbody>
	</table>
</body>
</html>
