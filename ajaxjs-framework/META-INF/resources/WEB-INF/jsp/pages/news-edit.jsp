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
						<div class="label">名 称：</div> 
						<input placeholder="请填写${uiName}名称" required name="name" value="${info.name}" type="text" />
					</label> 
					<label> 
						<div class="label">栏 目：</div>  
						<!-- 分类下拉 -->
						<aj-tree-catelog-select field-name="catalogId" :catalog-id="${domainCatalog_Id}" :selected-catalog-id="${empty info || empty info.catalogId? 0 : info.catalogId}">
						</aj-tree-catelog-select>
					</label>
		
 					<label>
 						<div class="label">创建日期：</div>  
						<aj-form-calendar-input field-name="createDate" field-value="<c:dateFormatter value="${info.createDate}" />">
						</aj-form-calendar-input> 
 					</label> 

				</div>
		
				<div>
					<label>
						<div class="label">作者：</div> 
						<input placeholder="" name="author" value="${info.author}" type="text" />
					</label> 
					<label> 
						<div class="label">出处：</div>  
						<input placeholder="" name="source" value="${info.source}" type="text" />
					</label>
					<label> 
						<div class="label">源网址：</div>  
						<input placeholder="" name="sourceUrl" value="${info.sourceUrl}" type="text" />
					</label>
				</div>
				
				<div>
					<label>
						<div class="label">摘 要：</div> 
						<textarea rows="10" cols="20" style="width: 90%; height: 30px;" class="ajaxjs-inputField" name="intro">${info.intro}</textarea>
					</label>
				</div>
		
				<div>
					<label>
						<div class="label">关键字：</div> 
						<input placeholder="逗号或空格隔开多个关键字" size="50" name="keywords" value="${info.keywords}" type="text" />
					</label> 
					<label>
						<div class="label">热度：</div> 
						<input placeholder="整数" size="20" name="keywords" value="${info.hot}" type="text" />
					</label> 
				</div>
				<div>
					<div class="label" style="vertical-align: top;">正 文：</div>
						<div style="display: inline-block; width: 90%;">
							<!-- HTML 在线编辑器，需要 textarea 包裹着内容 -->
							<aj-form-html-editor field-name="content" base-path="${ctx}" ref="htmleditor" upload-image-action-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=2">
								<textarea class="hide" name="content">${info.content}</textarea>
							</aj-form-html-editor>
						</div>
				</div>
				<div> 
						<div class="label">状态：</div> 
					<label>
					
						<input name="stat" value="1" type="radio" ${info.stat == 1 ? 'checked' : ''} /> 上线中 
					</label> 
					<label>
						<input name="stat" value="0" type="radio" ${info.stat == 0 ? 'checked' : ''} /> 已下线 
					</label> 
					<label>
						<input name="stat" value="2" type="radio" ${info.stat == 2 ? 'checked' : ''}  /> 已删除
					</label> 
			
				</div>
				
<!-- 图片上传 -->
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
						<aj-xhr-upload action="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catalog=1" :is-img-upload="true" 
							hidden-field="cover" 
							hidden-field-value="${info.cover}" 
							img-place="${empty info.cover ? commonAsset.concat('/images/imgBg.png') : ctx.concat('/').concat(info.cover)}">
						</aj-xhr-upload>
							
							</td>
						</tr>
					</table>
				</c:otherwise>
			</c:choose>
				</div>
				
<!-- 附件上传 -->
				<div>
					<div class="label" style="float:left;">附件：</div> 
			<c:choose>
				<c:when test="${isCreate}">
					<span>请保存记录后再上传附件。</span>
				</c:when>
				<c:otherwise>
					<table>
						<tr>
							<td>
								<!-- <ajaxjs-file-upload :limit-size="30000" field-name="kkk" label-id="attachmentUpload"></ajaxjs-file-upload> -->
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
			
	<!-- 弹出层上传对话框 -->
	<aj-popup-upload ref="uploadLayer" upload-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=1" img-place="${commonAsset.concat('/images/imgBg.png')}"></aj-popup-upload>
		</div>
		<script>
			App = new Vue({el: '.admin-entry-form'});
		
			// 表单提交
			aj.xhr.form('.admin-entry-form form', function(json) {
					 if(json && json.msg)
						 aj.alert.show(json.msg);
						${isCreate ? 'json && json.isOk && setTimeout(function(){location.assign(json.newlyId + "/");}, 2000);' : ''}
				}, {beforeSubmit(form, json) {
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