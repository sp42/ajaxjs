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
				<template slot="btns"><a :href="ajResources.ctx + '/admin/${tableName}/'">新建</a> | </template>
			</ajaxjs-admin-header>
		</div>
		
		<script>
			new Vue({el:'.vue'});
		</script>

		 
		<!-- demo root element -->
		<div class="treeGrid">
			<div class="left">
				<aj-tree url="/admin/catelog/list/?limit=99" top-node-name="栏目"></aj-tree>
			</div>
			<div class="right">
				<form class="inline-search">列表内查找  <input name="query" v-model="searchQuery" class="ajaxjs-inputField" /></form>
				<aj-grid :data="gridData" :columns="gridColumns" :filter-key="searchQuery"></aj-grid>
			</div>
		</div>
		
		<div class="aj-dhtml-border">hgfgfgfh</div>
		
		
		<script src="${ctx}/test.js"></script>
		<script>	
			new Vue({
			  el: '.treeGrid',
			  data: {
			    searchQuery: '',
			    gridColumns: ['name', 'power'],
			    gridData: [
			      { name: 'Chuck Norris', power: Infinity },
			      { name: 'Bruce Lee', power: 9000 },
			      { name: 'Jackie Chan', power: 7000 },
			      { name: 'Jet Li', power: 8000 }
			    ]
			  }
			});
		</script>
	</body>
</html>