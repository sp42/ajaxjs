<%@ page pageEncoding="utf-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<meta charset="utf-8" />
	<title></title>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
</head>
<body>
	<!-- 列表渲染，采用传统后端 MVC 渲染 -->
		<table class="ajaxjs-niceTable listTable" border="1">
			<thead>
				<tr>
					<th>#</th>
					<th>银行</th>
					<th>名称</th>
					<th>卡号</th>
					<th>查询密码</th>
					<th>取款密码</th>
					<th>是否信用卡</th>
					<th>额度</th>
					<th>备注</th>
					<th>使用人</th>
				</tr>
			</thead>
	
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td class="bankName" data-bank-id="${current.bankId}">${Banks[current.bankId].name}</td>
						<td>${current.name}</td>
						<td style="mso-number-format:'\@';">${current.no}</td>
						<td>${current.queryPsw}</td>
						<td>${current.psw}</td>
						<td>${empty current.creditLimit ? '否' : '是'}</td>
						<td>${current.creditLimit}</td>
						<td>${current.content}</td>
						<td>${WhoUses[current.whoUse].name}</td>
					</tr>
				</c:foreach>
			</tbody>
		</table>
</body>
</html>
