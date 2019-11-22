<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
	</head>
	<body>
		<div class="vue">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${uiName}一览</template>
			</ajaxjs-admin-header>
		</div>
		
		<script>
			new Vue({el:'body > div'});
		</script>

		<!-- 列表渲染，采用传统后端 MVC 渲染 -->
		<table class="ajaxjs-niceTable listTable">
			<colgroup>
				<col />
				<col />
				<col />
				<col />
				<col />
				<col />
				<col style="text-align: center;" align="center" />
			</colgroup>
			<thead>
				<tr>
					<th>#</th>
					<th class="name">${uiName}名称</th>
					<th>留言时间</th>
					<th>用户</th>
					<th>用户手机</th>
					<th>是否回复</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="8">点击“用户”记录可以过滤显示特定的数据。</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td title="${current.intro}">${current.name}</td>
						<td><c:dateFormatter value="${current.createDate}" /></td>
						<td>	
							<!-- 读取用户标识 -->
							<c:if test="${empty current.userId}">
								匿名用户
							</c:if>
							<c:if test="${not empty current.userId}">
								<a href="?filterField=userId&filterValue=${current.userId}">
									${ empty current.userName ? current.userNickName : current.userName}
								</a>
							</c:if>
						</td>
						<td>${current.phone}</td>
						<td>${empty current.feedback ? '未回复': '已回复'}</td>
						<td>
						<c:if test="${not empty current.userId}">
							<a href="${ctx}/user/center/info/${current.userId}/">
								<img src="${commonAssetIcon}/user.png" style="width:16px;vertical-align: sub;" />用户详情
							</a>
						</c:if>
							<a href="${ctx}/admin/${shortName}/${current.id}/"><img src="${commonAssetIcon}/update.gif" style="vertical-align: sub;" /> 编辑</a>
							<a href="javascript:aj.admin.del('${current.id}', '${current.name}');"><img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" /> 删除</a>
						</td>
					</tr>
				</c:foreach>
			</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
	</body>
</html>