<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
		<style>
			select {
				min-width:120px;
			}
		</style>
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
						<span style="color:red;">*</span>
					</label> 
					<label> 
						<div class="label">栏 目：</div>  
						<!-- 分类下拉 -->
			 			<aj-tree-catelog-select field-name="catalogId" :catalog-id="${domainCatalog_Id}" :selected-catalog-id="${empty info || empty info.catalogId? 0 : info.catalogId}">
						</aj-tree-catelog-select>
						<span style="color:red;">*</span>
					</label>

				</div>
				<div>
					<label>
						<div class="label">副标题：</div> 
						<input placeholder="请填写${uiName}副标题" size="60" name="subTitle" value="${info.subTitle}" type="text" />
					</label> 
					<label>
						<div class="label">品牌：</div> 
						<input placeholder="品牌" size="10" name="brand" value="${info.brand}" type="text" />
					</label> 
					<label>
						<div class="label">品牌：</div>
						<!-- 选择商家 -->
						<select class="ajaxjs-select" name="sellerId">
							<c:foreach items="${sellers}" var="item">
								<option value="${item.key}" ${item.key == info.sellerId ? 'selected' : ''}>${item.value.name}</option>
							</c:foreach>
						</select>
					</label> 

				</div>
				<div>	
					<label>
						<div class="label">封面价格：</div> 
						<input placeholder="通常为最低价" title="吸引用户显示，通常为最低价" size="10" name="coverPrice" value="${info.coverPrice}" type="text" required="required" />
						<span style="color:red;">*</span>
					</label> 
					<label>
						<div class="label">标题价格：</div> 
						<input placeholder="通常为较高的价格" title="通常为较高的价格" size="10" name="titlePrice" value="${info.titlePrice}" type="text" />
					</label> 
				</div>
				<div class="goodsFormat">
 			<c:choose>
				<c:when test="${isCreate}">
					<span>请保存记录后再编辑规格。</span>
				</c:when>
				<c:otherwise> 
					<table>
						<tr>
							<td valign="top"><br/>规格/分类：</td>
							
							<td>
				<ul>
					<li v-for="item in goodsFormatItemList">
<span :title="'#'+item.id">{{item.name}}</span>
<span style="color:red;">￥{{item.price}}</span> {{item.content}} <a href="javascript:void;" @click="delGoodsFormat(item.id)">删除</a>
					</li>
				</ul>			
				<ul>
					<li v-for="item in goodsFormatItems">
						<shop-goods-format-item></shop-goods-format-item>
					</li>
				</ul>
							</td>
							<td width="10"></td>
							<td valign="bottom">
								<button @click="addGoodsFormatItems($event)" class="ajaxjs-btn">+</button>
							</td>
						</tr>
					</table>
 				</c:otherwise>
			</c:choose>
				
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
					<table width="90%">
						<tr>
							<td width="350">
						<!-- 图片上传 --> 
						<aj-xhr-upload action="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catalog=1" :is-img-upload="true" 
							hidden-field="cover" 
							hidden-field-value="${info.cover}" 
							img-place="${isCreate || empty info.cover ? commonAsset.concat('/images/imgBg.png') : ctx.concat('/').concat(info.cover)}">
						</aj-xhr-upload>
							</td>
							
							<td width="50"></td>
							<td>
								<attachment-picture-list pic-ctx="${ctx}"
									upload-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catalogId=4" 
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
	<!-- 图片上传（iframe 辅助） -->
<%-- 	<ajaxjs-fileupload-iframe upload-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=2" ref="uploadIframe"></ajaxjs-fileupload-iframe>
			 --%>
	<!-- 弹出层上传对话框 -->
	<aj-popup-upload ref="uploadLayer" upload-url="${ctx}/admin/attachmentPicture/upload/${info.uid}/?catelog=1" img-place="${commonAsset.concat('/images/imgBg.png')}"></aj-popup-upload>
		</div>
		
		<form id="goodsFormat" action="${ctx}/admin/goodsFormat" method="POST">
			<input type="hidden" name="goodsId" value="${info.id}" />
		</form>
		
		<script>
			Vue.component('shop-goods-format-item', {
				template : 
					'<div>\
						分类 <input type="text" form="goodsFormat" size="15" name="name"  required placeholder="规格/分类/型号" />\
						价格 <input type="text" form="goodsFormat" size="10" name="price" required placeholder="最终实际交易金额" /> 元\
					    规格详情 <input type="text" form="goodsFormat" size="40" name="content" />\
						   <button form="goodsFormat" class="ajaxjs-btn">保存</button> </div>'
			});
			
			App = new Vue({
				el: '.admin-entry-form',
				data : {
					goodsFormatItemList:[],
					goodsFormatItems:[],
					attachmentPictures:[]
				},
				methods: {
					loadGoodsFormatItemList(){
			 			${!isCreate} && aj.xhr.get('${ctx}/admin/goodsFormat', json => {
							this.goodsFormatItemList = json.result;
						}, {
							goodsId: ${isCreate ? 0 : info.id} 
						}); 
					},
					addGoodsFormatItems(e) {
						e.preventDefault();
						this.goodsFormatItems.push({
						});

						return false;
					},
					delGoodsFormat (id) {
						aj.xhr.dele('${ctx}/admin/goodsFormat/' +id, ajaxjs.xhr.defaultCallBack.after(() => {
							this.loadGoodsFormatItemList();
						}));
					}
				}
			});
			
			${!isCreate ? 'App.loadGoodsFormatItemList();' : '' }
		
			// 表单提交
			aj.xhr.form('form.entityEdit', json => {
					 if(json && json.msg)
						 aj.alert.show(json.msg);
						${isCreate ? 'json && json.isOk && setTimeout(function(){location.assign(json.newlyId + "/");}, 2000);' : ''}
				}, {beforeSubmit (form, json) {
					json.content = App.$refs.htmleditor.getValue({cleanWord : eval('${aj_allConfig.article.cleanWordTag}'), encode : true});
				}
			});
			
			aj.xhr.form('#goodsFormat', ajaxjs.xhr.defaultCallBack.after(App.loadGoodsFormatItemList), {noFormValid:true});
		</script> 
		<c:if test="${isCreate}">
			<script>
				window.isCreate = true;
			</script>
		</c:if>
	</body>
</html>