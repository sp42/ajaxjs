<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!doctype html>
<html>
	<head>
		<!-- 全局统一的 HTML 文件头 -->
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="title" value="数据字典管理" />
		</jsp:include>
		
		<!-- Admin 公共前端资源 -->
		<link rel="stylesheet" href="${aj_static_resource}/dist/css/admin/admin.css" />
		<script src="${aj_static_resource}dist/admin/admin.js"></script>
	</head>
	<body>
		<div class="DataDict">	
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">数据字典一览</template>
			</ajaxjs-admin-header>
			
			<div class="soft-container">
				<div class="box padding">
					<div class="right">
						分 类  <select class="aj-select" style="width:150px;" v-model="tid">
							<c:foreach items="${DataDicts}">
								<option value="${item.value}" ${item.value == param.typeId ? 'selected' : ''}>${item.key}</option>
							</c:foreach>
						</select>
					</div>
					数据字典（Data Dictionary）是一种用户可以访问的记录数据库和应用程序元数据的目录。
				</div>
				<div class="box">
					<!-- 菜单工具栏-->
					<aj-entity-toolbar :between-date="false" @on-create-btn-clk="showAddNew = !showAddNew"
					 @on-save-btn-clk="onDirtySaveClk"></aj-entity-toolbar>
				</div>
				<div class="main-panel">
					<div class="theader-bg"></div>
					<table>
						<thead>
							<tr><th><input type="checkbox" class="top-checkbox" @change="selectAll" /></th>
							<th>#</th><th>键</th><th>值</th><th>操作</th></tr>
						</thead>
						<tr v-show="showAddNew" is="aj-grid-inline-edit-row-create" :columns="['key', 'value']" create-api="."></tr>
						<tr is="aj-grid-inline-edit-row" v-for="value in list" v-bind:key="value.id" :row-data="value" 
							:enable-inline-edit="true" :columns="['key', 'value']" dele-api="."
							:filter-field="['tid', 'createDate', 'updateDate', 'uid']">
						</tr>
					</table>
				</div>
				<div class="box bottom-bar padding">
					<div class="stateMsg left" v-show="selectedTotal != 0">已选择[{{selectedTotal}}]笔记录</div>
					<aj-pager class="right" api-url="." ref="pager" :auto-load="false" :is-page="false"></aj-pager>
				</div> 
			</div>
		</div>
		
		<script>
			GRID = new Vue({
				el: '.DataDict',
				mixins: [aj.SectionModel, aj.Grid.common],
				data: {
					tid: 1
				},
				mounted() {
					this.$refs.pager.extraParam.tid = this.tid;
					this.$refs.pager.get();
					
					this.BUS.$on('before-add-new', j => j.tid = this.tid); // 添加分类
				},
				watch:{
					tid() {
						this.$refs.pager.extraParam.tid = this.tid;
						this.reload();
					}
				}
			});
		</script>
	</body>
</html>
