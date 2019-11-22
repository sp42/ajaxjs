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
						<div class="label">账号名称：</div> 
						<input placeholder="请填写账号名称" size="20" required="required" name="name" value="${info.name}" type="text" />
						<span style="color:red;">*</span>
					</label> 
					<label>
						<div class="label">用户姓名：</div> 
						<input placeholder="请填写用户姓名" size="20" name="username" value="${info.username}" type="text" />
						<span style="color:red;">*</span>
					</label> 
				</div>
				
				<div>
					<label>用户组
						<select name="roleId" class="ajaxjs-select" style="width: 200px;"></select>
					
					</label>
				</div>
				<div>
					<div class="label" style="float:left;">头像：</div> 
			<c:choose>
				<c:when test="${isCreate}">
					<span>请保存记录后再上传图片。</span>
				</c:when>
				<c:otherwise>
					<table width="90%">
						<tr>
							<td width="350">
							</td>
							
							<td width="50"></td>
							<td>
								<attachment-picture-list pic-ctx="${ctx}"
									upload-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=3"
									blank-bg="${commonAsset.concat('/images/imgBg.png')}" 
									load-list-url="${ctx}/admin/attachmentPicture/getAttachmentPictureByOwner/${info.uid}" 
									del-img-url="${ctx}/admin/attachmentPicture/"></attachment-picture-list>
							</td>
						</tr>
					</table>
				</c:otherwise>
			</c:choose>
				</div>
				<div>
					<!--按钮 -->
					<ajaxjs-admin-info-btns :is-create="${isCreate}"></ajaxjs-admin-info-btns>
				</div>
			</form>
		</div>
		
		<script>

		</script>

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
							isCreate && json && json.isOk && setTimeout(()=>location.assign(json.newlyId + "/"), 2000);
						}
					});
				},
				methods: {

				}
			});		
			
			setTimeout(()=>{
				var el = document.querySelector('select[name=roleId]');
				aj.xhr.get('${ctx}/admin/user/user_group/list/?limit=99', json => {
					var selectUI = new ajaxjs.tree.selectUI();
					selectUI.renderer(json.result, el, ${empty info.roleId ? 0 : info.roleId}, {makeAllOption : false});
				});
			}, 300);
		</script>
		<c:if test="${isCreate}">
			<script>
				window.isCreate = true;
			</script>
		</c:if>
	</body>
</html>