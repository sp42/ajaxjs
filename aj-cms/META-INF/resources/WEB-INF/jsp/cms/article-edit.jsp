<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
		
		<!-- Admin 公共前端资源 -->
		<link rel="stylesheet" href="${aj_static_resource}/dist/css/admin/admin.css" />
		<script src="${aj_static_resource}/dist/admin/admin.js"></script>
	</head>
	<body>
		<div class="admin-entry-form">
			<!-- 后台头部导航 -->
			<aj-admin-header>
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
			</aj-admin-header>

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
						<aj-form-calendar-input field-name="createDate" field-value="<c:dateFormatter value="${info.createDate}" />" :show-time="true"></aj-form-calendar-input> 
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
						<aj-form-html-editor field-name="content" base-path="${ctx}" ref="htmleditor" upload-image-action-url="uploadContentImg/">
							<textarea class="hide" name="content">${info.content}</textarea>
						</aj-form-html-editor>
					</div>
				</div>
				
				<aj-admin-state :checked="${empty info.stat ? 9 : info.stat}"></aj-admin-state>
				
				<!-- 图片上传 -->
				<div>
					<table>
						<tr>
							<td>
								<div class="label" style="float:left;">封面图：</div> 
							</td>
							<td>
								<c:choose>
									<c:when test="${isCreate}">
										<span>请保存记录后再上传图片。</span>
									</c:when>
									<c:otherwise>
										<!-- 图片上传 --> 
										<aj-xhr-upload action="uploadCover/" :is-img-upload="true" hidden-field="cover" hidden-field-value="${info.cover}" 
											img-place="${empty info.cover ? commonAsset.concat('/images/imgBg.png') : aj_allConfig.uploadFile.imgPerfix.concat(info.cover)}">
										</aj-xhr-upload>
									</c:otherwise>
								</c:choose>
							</td>
							<td style="width:50px;"></td>
							<td> <a href="javascript:attchementMgr.$refs.layer.show();">点击上传附件</a></td> 
						</tr>
					</table>
				</div>
				
				<div>
					<!--按钮 -->
					<aj-admin-info-btns :is-create="${isCreate}"></aj-admin-info-btns> 
				</div>
			</form>
			
			<!-- 弹出层上传对话框 -->
			<aj-popup-upload ref="uploadLayer" upload-url="uploadContentImg/"></aj-popup-upload>
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
		
		<!-- 弹窗浮层 -->
		<div class="attchementMgrHolder">
			<aj-layer ref="layer">
				<table class="attchementMgr">
					<tr>
					<td class="aj-tableList">
						<h3>已上传附件列表<span> 点击文件名下载</span></h3>
						<header style="width:600px;">
							<div style="width:5%">id</div><div style="width:30%">文件名</div>
							<div style="width:20%">文件大小</div>
							<div style="width:30%">上传时间</div>
							<div style="width:10%">控制</div>
						</header>
						<aj-page-list ref="list" :is-page="false" api-url="${ctx}/admin/attachment/getListByOwnerUid/${info.uid}/" :is-show-footer="false">
							<template slot-scope="item" :onclick="'location.assign(\'' + item.id + '/\');'">
								<div style="width: 5%">{{item.id}}</div>
								<div style="width:35%"><a :href="'${aj_allConfig.uploadFile.perfix}' + item.name" target="_blank" download="w3logo">{{item.name}}</a></div>
								<div style="width:15%">{{item.fileSize}}kb</div>
								<div style="width:30%">{{item.createDate}}</div>
								<div style="width:10%;"><a href="javascript:void(0);" @click="del(item.id)">删除</a>
									
								</div>
							</template>
						</aj-page-list>
					</td>
					<td>
						<!-- 图片上传 --> 
						<aj-xhr-upload ref="upload" action="${ctx}/admin/attachment/upload/${info.uid}/?catalog=1" :is-img-upload="false" 
							hidden-field="cover" hidden-field-value="${info.cover}" :button-bottom="true">
						</aj-xhr-upload>
					</td>
					</tr>
					<tr>
						<td>
							<h3>上传注意事项</h3>
							<ol>
								<li>附件不仅文件格式上传</li>
								<li>单个文件最大为 20000kb</li>
								<li>查看所有上传附件请到“<a href="${ctx}/admin/attachment/">全部附件列表</a>”</li>
							</ol>
						</td><td></td>
					</tr>
				</table>
			</aj-layer>
		</div>
		
		<script>
			attchementMgr = new Vue({
				el: '.attchementMgrHolder',
				methods: {
					del(id) {
						aj.xhr.dele('${ctx}/admin/attachment/' + id + '/', j => {
							if(j.result)
								j = j.result;
							aj.msg.show(j.msg);
							
							this.$refs.list.ajaxGet();
						});
					}
				}
			});
			attchementMgr.$refs.upload.uploadOk_callback = j => {
				if(j.result)
					j = j.result;
				aj.msg.show(j.msg)
				attchementMgr.$refs.list.ajaxGet();
			}
		</script>
	</body>
</html>