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
				<template slot="btns"><a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | </template>
			</ajaxjs-admin-header>
			
			<!-- 搜索、分类下拉 -->
			<aj-admin-filter-panel :no-catalog="true">
				使用人 <select class="aj-select" style="width:130px;"
				 onchange="location.assign('?filterField=whoUse&filterValue=' + this.selectedOptions[0].value);">
						<option value="null">不指定使用人</option>
					<c:foreach items="${WhoUses}" var="item">
<option value="${item.key}" ${(param.filterValue != 'null') && (param.filterField == 'whoUse') && (param.filterValue == item.key)  ? 'selected' : ''}>
	${item.value.name}
</option>
					</c:foreach>
				</select>	
			</aj-admin-filter-panel>
		</div>
		
		<script>
			new Vue({el:'body > div'});
		</script>

		<!-- 列表渲染，采用传统后端 MVC 渲染 -->
		<table class="ajaxjs-niceTable listTable">
			<thead>
				<tr>
					<th>#</th>
					<th>银行</th>
					<th>名称</th>
					<th>用户名</th>
					<th>网银密码</th>
					<th>电话银行密码</th>
					<th>u盾密码</th>
					<th>备注</th>
					<th>使用人</th>
					<th class="control">控 制</th>
				</tr>
			</thead>
			<tfoot>
				<tr>
					<td colspan="11">
						<a href="?downloadXSL=true&<%=com.ajaxjs.web.ServletHelper.getAllQueryParameters(request) %>" target="_blank"><img src="${commonAssetIcon}/excel.png" width="16" style="vertical-align: middle;" /> 下载 Excel 格式</a>
					</td>
				</tr>
			</tfoot>
			<tbody>
				<c:foreach var="current" items="${PageResult}">
					<tr>
						<td>${current.id}</td>
						<td class="bankName" data-bank-id="${current.bankId}">${Banks[current.bankId].name}</td>
						<td>${current.name}</td>
						<td>${current.username}</td>
						<td>${current.psw}</td>
						<td>${current.phoneBankPsw}</td>
						<td>${current.uDsafePsw}</td>
						<td>${current.content}</td>
						<td>${WhoUses[current.whoUse].name}</td>
						<td><a href="../${current.id}/">详情</a></td>
					</tr>
				</c:foreach>
			</tbody>
		</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
		
		<script>
			// 合并单元格
			function megeCell(columnClass) {
				// 收集所有的列
				var arr = document.querySelectorAll(columnClass);
				
				var map = {};
				for(var i = 0, j = arr.length; i < j; i++) {
					var td = arr[i];
					var bankId = td.dataset.bankId;
					
					if(undefined === map[bankId]) map[bankId] = 1; // 统计跨行的有多少，用一个 map 装着
					else map[bankId] = ++map[bankId];
				} 
				
				var stack = [];
				for(var i = 0, j = arr.length; i < j; i++) {
					var td = arr[i];
					var bankId = td.dataset.bankId;
					var tds = map[bankId];
					
					if( tds > 1 && stack.length === 0) {// 标记
						for(var q = i; q < i + tds ; q++) {// 连续 tds 个都是要合并单元格的
							if(q == i) {
								arr[q].classList.add('firtstOne');
								arr[q].dataset.rowSpan = tds;							
							} else {
								arr[q].classList.add('die');
							}
							
							stack.push(arr[q]); // 入栈
						}
					}
				
				 	if(stack.length && td === stack[0]) {
						stack.shift();// 退栈  
						if(td.className.indexOf('firtstOne') != -1) {
						} else { 
							td.classList.add('die'); // 要删除的元素
						}
					} 
				}
				
				[].forEach.call(document.querySelectorAll('.firtstOne'), i => {
					i.setAttribute('rowspan', i.dataset.rowSpan);
				});
				[].forEach.call(document.querySelectorAll('.die'), i => {
					i.die();
				});
			}
			
			megeCell('.bankName');
		</script>
	</body>
</html>