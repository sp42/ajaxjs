<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/jsp/common/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
		<style>
			.admin-entry-form > form > div {
			    text-align: center;
			}
		</style>
	</head>
	<body>
		<div class="admin-entry-form">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${isCreate?'新建':'编辑'}${uiName}</template>
				<template slot="btns">
				<c:if test="${!isCreate}">
					<a :href="ajResources.ctx + '/admin/${tableName}/'">新建</a> | 
				</c:if>
					<a :href="ajResources.ctx + '/admin/${tableName}/list/'">${uiName}列表</a> | 
				</template>
			</ajaxjs-admin-header>

			<form action="." method="${isCreate ? 'POST' : 'PUT'}" class="entityEdit">
			<c:if test="${!isCreate}">
				<input type="hidden" name="id" value="${info.id}" /><!-- 传送 id 参数 -->
			</c:if>
			
				<div>
					<label>
						<div class="label">名 称：</div> 
						<input placeholder="请填写${uiName}名称" size="60" required="required" name="name" value="${info.name}" type="text" />
					</label> 
				</div>
				<div>
					<label> 
						<div class="label">分类：</div>  
						<!-- 分类下拉 -->
						<aj-tree-catelog-select field-name="catelog" :catelog-id="${catelogId}" :selected-catelog-id="${empty info ? 0 : info.catelog}"></aj-tree-catelog-select>
					</label>
					<label>
						<div class="label">工作经验：</div> 
						<input type="text" name="expr" value="${info.expr}" />
					</label>
				</div>
		
				<div class="brief">
					<label>
						<div class="label">岗位要求：</div> 
						<textarea rows="5" cols="80" name="content" class="ajaxjs-input">${info.content}</textarea>
					</label>
				</div>
		
				<div>
					<!--按钮 -->
					<ajaxjs-admin-info-btns :is-create="${isCreate ? true : false}"></ajaxjs-admin-info-btns>
				</div>
			</form>
		</div>
		<script>
			App = new Vue({el: '.admin-entry-form'});
		
			// 表单提交
			aj.xhr.form('form.entityEdit', function(json) {
			 if(json && json.msg)
				 aj.alert.show(json.msg);
				${isCreate ? 'json && location.assign(json.newlyId + "/");' : ''}
			});
		</script>
	</body>
</html>