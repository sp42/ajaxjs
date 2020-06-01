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
						<input type="text" placeholder="请填写${uiName}名称" required name="name" value="${info.name}" />
					</label> 
					<label> 
						<div class="label">栏 目：</div>  
						<!-- 分类下拉 -->
						<aj-tree-catelog-select field-name="catalogId" :catalog-id="${domainCatalog_Id}" :selected-catalog-id="${empty info || empty info.catalogId? 0 : info.catalogId}">
						</aj-tree-catelog-select>
					</label>
		
 					<label>
 						<div class="label">创建日期：</div>  
						<aj-form-calendar-input field-name="createDate" field-value="<c:dateFormatter value="${info.createDate}" />"></aj-form-calendar-input> 
 					</label> 
				</div>
		
				<div>
					<label>
						<div class="label">作者：</div> 
						<input type="text" placeholder="本文作者" name="author" value="${info.author}" />
					</label> 
					<label> 
						<div class="label">出处：</div>  
						<input type="text" placeholder="文章的制作单位" name="source" value="${info.source}" />
					</label>
					<label> 
						<div class="label">源网址：</div>  
						<input type="text" placeholder="如果是转载，请输入原网址" name="sourceUrl" value="${info.sourceUrl}" />
					</label>
				</div>
				
				<div>
					<label>
						<div class="label">摘 要：</div> 
						<textarea style="width: 90%; height: 30px;" name="intro">${info.intro}</textarea>
					</label>
				</div>
		
				<div>
					<label>
						<div class="label">关键字：</div> 
						<input type="text" placeholder="逗号或空格隔开多个关键字" size="50" name="keywords" value="${info.keywords}" />
					</label> 
					<label>
						<div class="label">热度：</div> 
						<input type="text" placeholder="整数" size="20" name="keywords" value="${info.hot}" />
					</label> 
				</div>
				<div class="htmlEditor-row">
					<div class="label">正 文：</div>
					<div>
						<!-- HTML 在线编辑器，需要 textarea 包裹着内容 -->
						<aj-form-html-editor field-name="content" base-path="${ctx}" ref="htmleditor" upload-image-action-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=2">
							<textarea class="hide" name="content">${info.content}</textarea>
						</aj-form-html-editor>
					</div>
				</div>
				
				<aj-admin-state :checked="${empty info.stat ? 9 : info.stat}"></aj-admin-state>
				
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
											img-place="${empty info.cover ? commonAsset.concat('/images/imgBg.png') : aj_allConfig.uploadFile.imgPerfix.concat(info.cover)}">
										</aj-xhr-upload>
									</td>
									<td style="width：20px;"></td>
									<td> <a href="#">上传附件</a></td> 
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
			<aj-popup-upload ref="uploadLayer" upload-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=1"></aj-popup-upload>
		</div>
		<script>
			App = new Vue({el: '.admin-entry-form'});
			aj.xhr.form('.admin-entry-form form', aj.admin.defaultAfterCreate, {
				beforeSubmit(form, json) {
					json.content = App.$refs.htmleditor.getValue({cleanWord : eval('${aj_allConfig.article.cleanWordTag}'), encode : true});
				}
			});
			${isCreate ? 'window.isCreate = true;' : ''}
		</script>
	</body>
</html>