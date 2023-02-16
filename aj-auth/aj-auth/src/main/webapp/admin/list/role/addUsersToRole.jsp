<%@ page pageEncoding="UTF-8"%>
<%@ taglib prefix="myTag" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<jsp:include page="/common/head.jsp" />
<style type="text/css">
	html, body, body>table {
		height: 100%;
	}
</style>

</head>

<body class="middleHeight">
	<table>
		<tr>
			<td>
				<div class="list_panel">
					<h1 style="font-size:17pt;display:inline-block">添加用户到角色：${param.roleName}</h1>
					<div style="width: 100%; border-bottom: 1px solid lightgray; height: 1px; margin: 20px 0;"></div>
					<div>
						
						<input placeholder="用户名称" />
					</div>
					<div style="min-height:600px">
						<div class="tree">
						
						
						</div>
						<table>
							<c:foreach items="${list}" var="item">
								<tr>
									<td>${item.id}</td>
									<td>${item.name}</td>
									<td>${item.code}</td>
									<td>${item.type}</td>
									<td>${item.appName}</td>
									<td>${item.sysName}</td>
									<td>${item.tenantName}</td>
									<myTag:list-common-rol style="31" item="${item}" namespace="${namespace}" namespace_chs="${namespace_chs}" >
										
									</myTag:list-common-rol>
								</tr>
							</c:foreach>
						</table>
						<button onclick="location.assign('addUsersToRole.jsp');">确定</button>
					</div>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>