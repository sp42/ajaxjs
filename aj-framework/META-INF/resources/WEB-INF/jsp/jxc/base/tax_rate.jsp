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
		<div class="TaxRate">	
			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">${uiName}一览</template>
			</aj-admin-header>
			
			
			<div class="soft-container">
				<div class="box padding">税率用于定义各税种的税率</div>
				<div class="box">
					<!-- 菜单工具栏-->
					<aj-entity-toolbar @on-create-btn-clk="showAddNew = !showAddNew" @on-save-btn-clk="onDirtySaveClk"></aj-entity-toolbar>
				</div>
				
				<div class="main-panel"> 
					<div class="theader-bg"></div>
					<table>
						<thead>
							<tr><th><input type="checkbox" class="top-checkbox" @click="selectAll" /> </th>
							<th>#</th><th>税种</th><th>税率%</th><th>创建日期</th><th>操作</th></tr>
						</thead>
						<tr v-show="showAddNew" is="aj-grid-inline-edit-row-create" :columns="['name', 'rate',null]" create-api="."></tr>
						<tr is="aj-grid-inline-edit-row" v-for="value in list" v-bind:key="value.id" :row-data="value"
							:show-id-col="false" :columns="gridCols()" :enable-inline-edit="true"
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
			GRID = new Vue({
				el: '.TaxRate',
				mixins: [aj.SectionModel, aj.Grid.common],
				methods: {
					gridCols(){
						return ['id', 'name', 'rate', data => new Date(data.createDate).format("yyyy-MM-dd")];
					}
				}
			}); 
			GRID.updateApi = '.';
		</script>
	</body>
</html>
