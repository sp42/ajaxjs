<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
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
				<template slot="title">${uiName}</template>
				<template slot="btns">
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
						<div class="label">用户：
							<!-- 读取用户标识 -->
							<c:if test="${empty info.userId}">
								匿名用户
							</c:if>
							<c:if test="${not empty info.userId}">
								
								<a href="${ctx}/user/center/info/${info.userId}/">
									${ empty info.userName ? info.userNickName : info.userName}
								</a>
							</c:if>
						</div>
						<div class="label">
							联系手机：${info.phone}</div>  <div class="label">联系邮箱：<a href="mailto:${info.email}">${info.email}</a>
							留言时间：<c:dateFormatter value="${info.createDate}" />
						</div>
					</label>
				</div>
		 
				<div>
					<label>
						<div class="label">留言内容：</div> 
						<textarea rows="5" cols="80" name="content" class="ajaxjs-input">${info.content}</textarea>
					</label>
				</div>
				
				<div>
					<label>
						<div class="label">回复内容：</div> 
						<textarea rows="5" cols="80" name="feedback" class="ajaxjs-input">${info.feedback}</textarea>
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