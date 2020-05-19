<%@page pageEncoding="UTF-8" import="com.ajaxjs.user.role.RoleService, com.ajaxjs.user.role.RightConstantDummy"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
		<script src="${ajaxjs_ui_output}/lib/China_AREA_full.js"></script>
		<style>
			.aj-china-area select{
				width: 100px;
			}
		</style>
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
						<input placeholder="请填写${uiName}名称" required name="name" value="${info.name}" type="text" /> 
						<div class="sub-2">帐号 id、登录 id</div>
					</label> 
		
 					<label>
 						<div class="label">注册日期</div>  
						<aj-form-calendar-input field-name="createDate" field-value="<c:dateFormatter value="${info.createDate}" />">
						</aj-form-calendar-input> 
 					</label> 

				</div>
				<div>
					<label>
						<div class="label">手机</div> 
						<input type="text" name="phone" value="${info.phone}" />
					</label> 
					<label>
						<div class="label">邮箱</div> 
						<input type="email" name="email" value="${info.email}"  />
					</label> 
				</div>
		
				<div>
					<label>
						<div class="label">姓 名</div> 
						<input type="text" name="username" value="${info.username}" placeholder="请输入您的真实姓名" />
						<div class="sub-2">请输入您的真实姓名</div>
					</label> 
					
					<div class="label">性别</div> 
					<label><input type="radio" name="sex" value="1" ${info.sex == 1 ? 'checked' : '' }> 男</label> 
					<label><input type="radio" name="sex" value="2" ${info.sex == 2 ? 'checked' : '' }> 女</label> 
					<label><input type="radio" name="sex" value="0" ${info.sex == 0 ? 'checked' : '' }> 未知</label>

				</div>
				
				<div>
					<label>
						<div class="label">工作职务</div> 
						<input type="text" name="jobTitle" value="${info.jobTitle}" placeholder="工作职务" />
					</label> 
		
 					<label>
 						<div class="label">出生日期</div>  
						<aj-form-calendar-input field-name="birthday" field-value="${info.birthday}" :position-fixed="true"></aj-form-calendar-input>
 					</label> 
				</div>
				
				<div>
					<div class="label">居住地</div> 
					<aj-china-area style="display: inline-block;" province-code="${info.locationProvince}" city-code="${info.locationCity}" district-code="${info.locationDistrict}">
					</aj-china-area>
					<input type="text" name="address" placeholder="填写地址，不用再填省市区" pattern="^[\u4e00-\u9fa5\w]{4,15}$" value="${info.address}" size="32" />
				</div>
				
				<div>
					<label>
						<div class="label">身份证号码</div> 
						<input type="text" name="idCardNo" value="${info.idCardNo}" placeholder="中国大陆身份证号码" pattern="^[1-9]\d{5}(18|19|20|(3\d))\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$" />
					</label>
				</div>
				<div>
					<label>
						<div class="label">个人简介</div> 
						<textarea rows="6" cols="85" name="content">${info.content}</textarea>
					</label>
				</div>
				<div> 
					<div class="label" style="vertical-align: top;">头像</div> 
					<div style="display:inline-block;">
						<!-- 图片上传 -->
						<aj-xhr-upload action="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catalog=1" 
							:is-img-upload="true" hidden-field="avatar" hidden-field-value="${info.avatar}"
							img-place="${empty info.avatar ? commonAsset.concat('/images/imgBg.png') : (info.avatar.startsWith('http') ? info.avatar : aj_allConfig.uploadFile.imgPerfix.concat(info.avatar))}">
						</aj-xhr-upload>
					</div>
				</div>
				<c:if test="${RoleService.check(RightConstantDummy.USER_PRIVILEGE)}">
				<div>
					<div class="label">用户组</div> 
					<!-- 分类下拉 -->
					<aj-tree-user-role-select :no-jump="true" :value="${empty info.roleId ? 0 : info.roleId}" :json="${UserGroupsJSON}" style="width: 300px;"></aj-tree-user-role-select>
					<div class="sub-2">用户组对应着权限，请谨慎设置。</div>
				</div>
				</c:if>
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
			aj.xhr.form('.admin-entry-form form', json => {
				 if(json && json.msg)
					 aj.alert.show(json.msg);
			});
		</script>
	</body>
</html>