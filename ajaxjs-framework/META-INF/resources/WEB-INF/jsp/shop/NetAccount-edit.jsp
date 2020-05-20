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

			<form action="." method="${isCreate ? 'POST' : 'PUT'}" class="entityEdit">
			<c:if test="${!isCreate}">
				<input type="hidden" name="id" value="${info.id}" /><!-- 传送 id 参数 -->
			</c:if>
			
				<div>
					<label>
						<div class="label">名称：</div> 
						<input placeholder="请填写卡名称" size="10" required="required" name="name" value="${info.name}" type="text" />
						<span style="color:red;">*</span>
					</label> 
					<label>
						<div class="label">用户名：</div> 
						<input placeholder="请填写卡名称" size="10" required="required" name="username" value="${info.username}" type="text" />
						<span style="color:red;">*</span>
					</label> 

				</div>
				<div>
					<label>
						<div class="label">选择银行：</div>
						<!-- 选择商家 -->
						<select class="aj-select" name="bankId" style="width:200px;">
							<c:foreach items="${Banks}" var="item">
								<option value="${item.key}" ${item.key == info.bankId ? 'selected' : ''}>${item.value.name}</option>
							</c:foreach>
						</select>
						<span style="color:red;">*</span>
					</label> 
				</div>
				<div>
					<label>
						<div class="label">使用人：</div>
						<!-- 选择商家 -->
						<select class="aj-select" name="whoUse" style="width:100px;">
							<c:foreach items="${WhoUses}" var="item">
								<option value="${item.key}" ${item.key == info.whoUse ? 'selected' : ''}>${item.value.name}</option>
							</c:foreach>
						</select>
						<span style="color:red;">*</span>
					</label> 
				</div>
				<div>
					<label>
						<div class="label">网银密码：</div> 
						<input size="10" required="required" name="psw" value="${info.psw}" type="text" />
						<span style="color:red;">*</span>
					</label> 
					<label>
						<div class="label">电话银行密码：</div> 
						<input size="10" name="phoneBankPsw" value="${info.phoneBankPsw}" type="text" />
					</label> 
					<label>
						<div class="label">u盾密码：</div> 
						<input size="10" name="uDsafePsw" value="${info.uDsafePsw}" type="text" />
					</label> 
				</div>

				<div>
					<label>
						<div class="label">备注：</div> 
						<textarea rows="5" cols="80" name="content" class="aj-input">${info.content}</textarea>
					</label>
				</div>
				<div>
					<!--按钮 -->
					<ajaxjs-admin-info-btns :is-create="${isCreate}"></ajaxjs-admin-info-btns>
				</div>
			</form>
		</div>

		<script>
			App = new Vue({
				el: '.admin-entry-form',
				data : {
	
				},
				
				mounted(){
					// 表单提交
					aj.xhr.form('form.entityEdit', json => {
						if(json && json.msg){
							aj.alert.show(json.msg);
							${isCreate} && json && json.isOk && setTimeout(()=>location.assign(json.newlyId + "/"), 2000);
						}
					});
				},
				methods: {

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