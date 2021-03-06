<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/jsp/common/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
	<jsp:param name="title" value="${uiName}管理" />
</jsp:include>
		 
</head>
<body>
	<div class="admin-entry-form">
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header> <template slot="title">${isCreate?'新建':'编辑'}${uiName}</template>
		<template slot="btns"> <c:if test="${!isCreate}">
			<a :href="ajResources.ctx + '/admin/ads/'">新建</a> | 
				</c:if> <a :href="ajResources.ctx + '/admin/ads/list/'">${uiName}列表</a> | </template>
		</ajaxjs-admin-header>

		<form action="." method="${isCreate ? 'POST' : 'PUT'}"
			class="entityEdit">
			<c:if test="${!isCreate}">
				<input type="hidden" name="id" value="${info.id}" />
				<!-- 传送 id 参数 -->
			</c:if>

			<div>
				<label>
					<div class="label">广告标题：</div> <input placeholder="请填写${uiName}名称" size="40" required="required" name="name" type="text" value="${info.name}" />
				</label>
			</div>
			
			<div>
				<label> 
					<div class="label">分类：</div>  
					<!-- 分类下拉 -->
					<aj-tree-catelog-select field-name="catelog" :catelog-id="${domainCatalog_Id}" :selected-catelog-id="${empty info.catelog ? 0 : info.catelog}"></aj-tree-catelog-select>
				</label>
				&nbsp; &nbsp; &nbsp; 
				<label>
					<div class="label">广告目标链接：</div> 
					<input type="text" placeholder="请填入${uiName}链接" size="60" required="required" name="link" value="${info.link}" v-model="adsLink" /> 
					<a :href="adsLink" target="_blank">访问</a>
				</label>
			</div>
			<div>
				<table >
					<tr>
						<td><div class="label" style="float: left;">封面图：</div></td>
						<td>
				<c:choose>
					<c:when test="${isCreate}">
							<span>请保存记录后再上传图片。</span>
					</c:when>
					<c:otherwise>
							<!-- 图片上传 -->
							<aj-xhr-upload action="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=2" 
								:is-img-upload="true" img-place="${empty info.cover ? commonAsset.concat('/images/imgBg.png') : ctx.concat(info.cover)}"></aj-xhr-upload>
					</c:otherwise>
				</c:choose>
						</td>
						<td width="50"></td>
						<td>
							<div class="label" style="float: left;">展示图：</div>
						</td>
						<td>
				<c:choose>
					<c:when test="${isCreate}">
							<span>请保存记录后再上传图片。</span>
					</c:when>
					<c:otherwise>
							<!-- 图片上传 --> 
							<aj-xhr-upload action="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=1" 
								:is-img-upload="true" img-place="${empty info.img ? commonAsset.concat('/images/imgBg.png') : ctx.concat(info.img)}"></aj-xhr-upload>
					</c:otherwise>
				</c:choose>
						</td>
						
					</tr>
				</table>
			</div>
			<div>
				<!--按钮 -->
				<ajaxjs-admin-info-btns :is-create="${isCreate ? true : false}"></ajaxjs-admin-info-btns>
			</div>
		</form>
	</div>
	
	
	<script src="${ctx}/test/test.js"></script>
	<script>
		App = new Vue({
			el : '.admin-entry-form',
			data : {
				adsLink : '${info.link}'
			}
		});

		// 表单提交
		aj.xhr.form('form.entityEdit', function(json) {
			if (json && json.msg)
				aj.alert.show(json.msg);
			${isCreate ? 'json && json.isOk && setTimeout(function(){location.assign(json.newlyId + "/");}, 2000);': ''}
		});
	</script>
	<c:if test="${isCreate}">
		<script>
			window.isCreate = true;
		</script>
	</c:if>
</body>
</html>