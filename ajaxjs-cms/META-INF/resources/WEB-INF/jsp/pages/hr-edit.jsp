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
		<div class="admin-entry-form">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${isCreate?'新建':'编辑'}${uiName}</template>
				<template slot="btns">
				<c:if test="${!isCreate}">
					<a :href="ajResources.ctx + '/admin/${shortName}/'">新建</a> | 
				</c:if>
					<a :href="ajResources.ctx + '/admin/${shortName}/list/'">${uiName}列表</a> | 
				</template>
			</ajaxjs-admin-header>

			<form action="." method="${isCreate ? 'POST' : 'PUT'}">
			<c:if test="${!isCreate}">
				<input type="hidden" name="id" value="${info.id}" /><!-- 传送 id 参数 -->
			</c:if>
			
				<div>
					<label>
						<div class="label">名 称：</div> 
						<input placeholder="请填写${uiName}名称" size="60" required="required" name="name" value="${info.name}" type="text" />
					</label> 
					<label> 
						<div class="label">栏 目：</div>  
						<!-- 分类下拉 -->
						<aj-tree-catelog-select field-name="catelogId" :catelog-id="${domainCatalog_Id}" :selected-catelog-id="${empty info || empty info.catelogId ? 0 : info.catelogId}">
						</aj-tree-catelog-select>
					</label>
				</div>
				<div>
					<label>
						<div class="label">工作经验：</div> 
						<input placeholder="请填写工作经验" size="60" name="expr" value="${info.expr}" type="text" />
					</label> 
				</div>
		
				<div>
					<div class="label" style="vertical-align: top;">工作要求：</div>
					<textarea rows="15" cols="20" style="width: 90%; height: 50px;" class="ajaxjs-inputField" name="content">${info.content}</textarea>
				</div>
 
				<div>
					<!--按钮 -->
					<ajaxjs-admin-info-btns :is-create="${isCreate}"></ajaxjs-admin-info-btns>
				</div>
			</form>
			
		</div>
		<script>
			App = new Vue({el: '.admin-entry-form'});
		
			// 表单提交
			aj.xhr.form('form.entityEdit', json => {
				if(json && json.msg)
					aj.alert.show(json.msg);
				${isCreate ? 'json && json.isOk && setTimeout(function(){location.assign(json.newlyId + "/");}, 2000);' : ''}
			});
		</script>
		<c:if test="${isCreate}">
			<script>
				window.isCreate = true;
			</script>
		</c:if>
	</body>
</html>