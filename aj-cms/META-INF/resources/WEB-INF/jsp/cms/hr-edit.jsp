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
		<div class="admin-entry-form">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">${isCreate?'新建':'编辑'}${uiName}</template>
				<template slot="btns">
				<c:if test="${!isCreate}">
					<a href="..">新建</a> | 
				</c:if>
					<a :href="ajResources.ctx + '/admin/cms/hr/list/'">${uiName}列表</a> | 
				</template>
			</ajaxjs-admin-header>

			<form action="." method="${isCreate ? 'POST' : 'PUT'}">
				<c:if test="${!isCreate}">
					<input type="hidden" name="id" value="${info.id}" /><!-- 传送 id 参数 -->
				</c:if>
			
				<div>
					<label>
						<div class="label">名 称：</div> 
						<input placeholder="请填写${uiName}名称" size="60" required name="name" value="${info.name}" type="text" />
					</label> 
					<label>
 						<div class="label">创建日期：</div>  
						<aj-form-calendar-input field-name="createDate" field-value="<c:dateFormatter value="${info.createDate}" />" :show-time="true">
						</aj-form-calendar-input> 
 					</label> 
				</div>
				<div>
					<div class="label" style="vertical-align: top;">工作要求：</div>
					<textarea rows="15" cols="20" style="width: 50%; height: 230px;" name="content" required>${info.content}</textarea>
				</div>
				<aj-admin-state :checked="${empty info.stat ? 9 : info.stat}"></aj-admin-state>
				<div>
					<!--按钮 -->
					<ajaxjs-admin-info-btns :is-create="${isCreate}"></ajaxjs-admin-info-btns>
				</div>
			</form>
		</div>
		<script>
			new Vue({el: '.admin-entry-form'});
			aj.xhr.form('.admin-entry-form form', aj.admin.defaultAfterCreate);
			${isCreate ? 'window.isCreate = true;' : ''}
		</script>
	</body>
</html>