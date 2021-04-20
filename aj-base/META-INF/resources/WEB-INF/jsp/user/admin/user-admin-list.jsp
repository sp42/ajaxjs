<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
		
		<!-- Admin 公共前端资源 -->
		<link rel="stylesheet" href="${aj_static_resource}/dist/css/admin/admin.css" />
		<script src="${aj_static_resource}/dist/admin/admin.js"></script>
	</head>
	<body>
		<div class="user-list">	
			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">${uiName}管理</template>
				<template slot="btns"><a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | </template>
			</aj-admin-header>

            <aj-grid api-url="grid.json">
                <template v-slot="{grid}">
					<div class="box padding">
						<div class="right">
							分 类 
							<select name="roleId" class="aj-select"></select>
						</div>
						查看系统所有的会员资料
					</div>
					
					<div class="box">
						<!-- 菜单工具栏-->
						<aj-entity-toolbar :create="false" :save="false"> 
							<li onclick="window.open('${ctx}/user/register/');"> 用户注册</li>
							<li class="fa fa-user-o" onclick="aj.user.admin.USER_GROUP.$refs.layer.show();"> 用户组</li>
							<li class="fa fa-user-circle-o" onclick="aj.user.admin.ASSIGN_RIGHT.$refs.assignRight.show();"> 角色分配</li>
						</aj-entity-toolbar>
					</div>
					
					<div class="main-panel">
						<div class="theader-bg"></div>
						<table >
							<thead>
								<tr><th><input type="checkbox" class="top-checkbox" @click="grid.selectAll" /> </th>
									<th>#</th>
									<th>头像</th>
									<th>${uiName}账号</th>
									<th>用户姓名</th>
									<th>性别</th>
									<th>邮件/手机</th>
									<th>注册日期</th>
									<th>用户组</th>
									<th>状态</th>
									<th>控 制</th>
								</tr>
							</thead>
							<tr is="aj-grid-inline-edit-row" v-for="value in grid.list" v-bind:key="value.id" :init-row-data="value"
								:show-id-col="false" :columns="gridCols()" :enable-inline-edit="false">
							</tr>
						</table> 
					</div>
					<div class="box bottom-bar padding">
						<aj-list ref="store" class="right in-one-line" api-url="../listJson/" ref="pager" :show-default-ui="false"></aj-list>
						<div class="stateMsg" v-show="grid.selectedTotal != 0">已选择[{{grid.selectedTotal}}]笔记录</div>
					</div>
                </template>
            </aj-grid>
            
		</div>
		
 		<%@include file="user-group.jsp" %> 
 		<%@include file="assign-right.jsp" %>
 		
		<script>
			window.UserGroups = ${UserGroupsJSON};
			window.UserGroups_IdAsKey = ${UserGroups_IdAsKey};
		</script>
		<script src="${aj_static_resource}/dist/user/admin/list.js"></script>
		<script src="${aj_static_resource}/dist/user/admin/user-group.js"></script>
		<script src="${aj_static_resource}/dist/user/admin/assign-right.js"></script>
	</body>
</html>