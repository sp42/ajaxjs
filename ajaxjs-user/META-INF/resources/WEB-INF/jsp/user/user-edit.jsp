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
				<template slot="title">
					${isCreate?'新建':'编辑'}${uiName}
					
					<c:if test="${!isCreate}">
						#${info.id} <!-- 外显 id -->
					</c:if>
				</template>
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
						<div class="label">名 称</div> 
						<input placeholder="请填写${uiName}名称" required name="name" value="${info.name}" type="text" /> 帐号 id、登录 id
					</label> 
		
 					<label>
 						<div class="label">注册日期</div>  
						<aj-form-calendar-input field-name="createDate" field-value="<c:dateFormatter value="${info.createDate}" />">
						</aj-form-calendar-input> 
 					</label> 

				</div>
		
				<div>

				</div>
				
				<div>
					<label>
						<div class="label">个人简介</div> 
						<textarea rows="10" cols="20" style="width: 90%; height: 30px;" class="ajaxjs-inputField" name="content">${info.content}</textarea>
					</label>
				</div>
		

				<div> 
					<div class="label">状态：</div> 
					<label>
						<input name="stat" value="1" type="radio" ${info.stat == 1 ? 'checked' : ''} /> 正常 
					</label> 
					<label>
						<input name="stat" value="0" type="radio" ${info.stat == 0 ? 'checked' : ''} /> 异常
					</label> 
					<label>
						<input name="stat" value="2" type="radio" ${info.stat == 2 ? 'checked' : ''}  /> 已删除
					</label> 
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
			aj.xhr.form('.admin-entry-form form', function(json) {
					 if(json && json.msg)
						 aj.alert.show(json.msg);
						${isCreate ? 'json && json.isOk && setTimeout(function(){location.assign(json.newlyId + "/");}, 2000);' : ''}
				}, {beforeSubmit : function(form, json) {
					json.content = App.$refs.htmleditor.getValue({cleanWord : eval('${aj_allConfig.article.cleanWordTag}'), encode : true});
				}
			});
			
		</script>
		<c:if test="${isCreate}">
			<script>
				window.isCreate = true;
			</script>
		</c:if>
	</body>
</html>