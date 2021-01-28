<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!doctype html>
<html>
	<head>
		<!-- 全局统一的 HTML 文件头 -->
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
	</head>
	<body>
		<div class="bank">	
		

			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">${uiName}一览</template>
			</aj-admin-header>
			
			<div class="soft-container">
				<div class="box padding">用于创建、维护流程</div>
				<div class="box">
					<!-- 菜单工具栏-->
					<aj-entity-toolbar :create="false" :save="false" :dele-btn="false">
						<li>设计新流程</li>
						<li>上传定义文件</li>
					</aj-entity-toolbar>
				</div>
				
				<div class="main-panel"> 
					<div class="theader-bg"></div>
					<table>
						<thead>
							<tr><th><input type="checkbox" class="top-checkbox" @click="selectAll" /> </th>
							<th>#</th><th>名称</th><th>说明</th><th>版本号</th><th>状态</th><th>创建日期</th><th>操作</th></tr>
						</thead>
						<tr v-show="showAddNew" is="aj-grid-inline-edit-row-create" :columns="['name', 'branch', 'account', 'uid', null]"></tr>
						<tr is="aj-grid-inline-edit-row" v-for="value in list" v-bind:key="value.id" :row-data="value"
							:show-id-col="true" :columns="gridCols()" :enable-inline-edit="false" :control-ui="'<aj-wf-process-btns id=' + value.id + '></aj-wf-process-btns>'"
							:filter-field="['id', 'createDate']" dele-api=".">
						</tr>

					</table>
				</div>
				
				<!-- 菜单工具栏-->
				<div class="box bottom-bar padding">
					<aj-pager class="right" api-url="." ref="pager"></aj-pager>
					<div class="stateMsg" v-show="selectedTotal != 0">已选择[{{selectedTotal}}]笔记录</div>
				</div>
			</div>
		</div>
		
		<script>
			// 启动流程
			function start(id) {
				aj.xhr.get('start/' + id + '/', j => alert(j.msg));
			}
		
			Vue.component('aj-wf-process-btns', {
				props: ['id'],
				template: '<span><a :href="\'javascript:start(\' + id + \');\'">启动</a> <a :href="\'design/\' + id">设计</a></span>'
			});
			
			var avatar = data => {
				return '<aj-avatar avatar="https://gdhdc-org.nos-eastchina1.126.net/user_avater/758338592786350080.jpg"></aj-avatar>';	
			}
			
			GRID = new Vue({
				el: '.bank',
				mixins: [aj.SectionModel, aj.Grid.common],
				methods: {
					gridCols() {
						return ['name', 'displayName', 'version', data => {
							// 参考 WorkflowConstant.java
							switch(data.stat) {
/* 							case 0:
								return '活动中';
							case 1:
								return '已结束';
							case 2:
								return '已终止'; */
							case 0:
								return '禁用';
							case 1:
								return '启用';
							}
						}, data => new Date(data.createDate).format("yyyy-MM-dd hh:mm")];
					},
					// 编辑按钮事件
					onEditClk(id) {
						location.assign(id + '/');
					},
					
				}
			}); 
			
			GRID.updateApi = '.';
		</script>
	</body>
</html>
