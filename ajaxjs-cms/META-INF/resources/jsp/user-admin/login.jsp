<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/jsp/common/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
		<jsp:param name="title" value="${uiName}管理" />
	</jsp:include>
	<style>
		.form {
			margin: 0 auto;
			background: rgba(0, 0, 0, 0) url('${ctc}/asset/common/images/admin-user-login/wrap.png') no-repeat scroll 0% 0%;
			width: 436px;
			height: 100%;
		}
	</style>
</head>
<body>
	<div class="form">
		<form>
			<table>
				<tr>
					<td>用户名</td>
					<td><input type="text" /></td>
				</tr>
				<tr>
					<td>密码</td>
					<td><input type="password" /></td>
				</tr>
				<tr>
					<td>用户名</td>
					<td><input type="text" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
