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
		<div class="aj-form-row-holder infoForm">
			<!-- 后台头部导航 -->
			<aj-admin-header>
				<template slot="title">
					编辑${uiName} #${info.id} <!-- 外显 id -->
				</template>
				<template slot="btns">
					<a href="..">${uiName}列表</a> | 
				</template>
			</aj-admin-header>

			<form action="." method="PUT">
				<input type="hidden" name="id" value="${info.id}" /><!-- 传送 id 参数 -->
				
				<div>
					<label>
						<div class="label">id：</div> ${info.name}
					</label> 
					&nbsp;&nbsp;&nbsp;
					<label>
						<div class="label">版本号：</div> ${info.version}
					</label> 
					&nbsp;&nbsp;&nbsp;
 					<label>
 						<div class="label">创建日期：</div>  <c:dateFormatter value="${info.createDate}" />
 					</label> 
				</div>
		
				<div>
					<label>
						<div class="label">名称：</div> 
						<input type="text" placeholder="说明" size="60" name="displayName" value="${info.displayName}" />
					</label> 
					<label> 
						<div class="label">实例 URL：</div>  
						<input type="text" placeholder="实例 URL" name="instanceUrl" value="${info.instanceUrl}" />
					</label>
				</div>
				
				<div>
					<label>
						<div class="label">流程定义：</div> 
						<textarea style="width: 90%; height: 300px;" name="intro">${info.content}</textarea>
					</label>
				</div>
				<aj-admin-state :checked="${info.stat}"></aj-admin-state>
				<div>
					<!--按钮 -->
					<aj-admin-info-btns :is-create="false" list-url="../"></aj-admin-info-btns> 
				</div>
			</form>
		</div>
		<script>
			App = new Vue({el: '.infoForm'});
			aj.xhr.form('.infoForm form');
		</script>
	</body>
</html>