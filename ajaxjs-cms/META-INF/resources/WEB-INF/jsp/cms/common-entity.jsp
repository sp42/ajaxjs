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
						<div class="label">名 称：</div> 
						<input placeholder="请填写${uiName}名称" size="60" required="required" name="name" value="${info.name}" type="text" />
					</label> 
					<label> 
						<div class="label">栏 目：</div>  
						<!-- 分类下拉 -->
						<aj-tree-catelog-select field-name="catelog" :catelog-id="${domainCatalog_Id}" :selected-catelog-id="${empty info || empty info.catelog? 0 : info.catelog}">
						</aj-tree-catelog-select>
					</label>
			<%-- 	<c:if test="${!isCreate}"> --%>
<!-- 					<label> -->
<!-- 						<div class="label">创建日期：</div>  -->
<!-- 						日历控件 -->
<%-- 						<aj-form-calendar-input field-name="date" field-value="<c:dateFormatter value="${info.createDate}" format="yyyy-MM-dd" />"></aj-form-calendar-input> --%>
<!-- 					</label> -->
				<%-- </c:if> --%>
				</div>
				<div>
					<label>
						<div class="label">副标题：</div> 
						<input placeholder="请填写${uiName}副标题" size="60" name="subTitle" value="${info.subTitle}" type="text" />
					</label> 

				</div>
		
				<div>
					<label>
						<div class="label">摘 要：</div> 
						<textarea rows="15" cols="20" style="width: 90%; height: 50px;" class="ajaxjs-inputField" name="intro">${info.intro}</textarea>
					</label>
				</div>
		
				<div>
					<div class="label" style="vertical-align: top;">正 文：</div>
						<div style="display: inline-block; width: 90%;">
							<!-- HTML 在线编辑器，需要 textarea 包裹着内容 -->
							<aj-form-html-editor field-name="content" base-path="${ctx}" ref="htmleditor">
								<textarea class="hide" name="content">${info.content}</textarea>
							</aj-form-html-editor>
						</div>
				</div>
				
				
				<div>
					<div class="label" style="float:left;">封面图：</div> 
			<c:choose>
				<c:when test="${isCreate}">
					<span>请保存记录后再上传图片。</span>
				</c:when>
				<c:otherwise>
					<table>
						<tr>
							<td>
<!-- 图片上传 -->
<ajaxjs-img-upload-perview ref="uploadControl" img-place="${(empty info.cover || isCreate) ? commonAsset.concat('/images/imgBg.png'): ctx.concat(info.cover)}" />
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
	<!-- 图片上传（iframe 辅助） -->
	<ajaxjs-fileupload-iframe upload-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=2" ref="uploadIframe"></ajaxjs-fileupload-iframe>
			
	<!-- 弹出层上传对话框 -->
	<aj-popup-upload ref="uploadLayer" upload-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=1" img-place="${commonAsset.concat('/images/imgBg.png')}"></aj-popup-upload>
		</div>
		<script>
			App = new Vue({el: '.admin-entry-form'});
		
			// 表单提交
			aj.xhr.form('form.entityEdit', function(json) {
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