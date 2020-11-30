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
						<div class="label">开始时间：</div> 
						<aj-form-calendar-input :show-time="true" field-name="beginTime" field-value="<c:dateFormatter value="${info.beginTime}" />" :position-fixed="true"></aj-form-calendar-input>
						<span class="needed" style="color:red;">*</span>
					</label> 
					<label>
						<div class="label">结束时间：</div> 
						<aj-form-calendar-input field-name="endTime" field-value="<c:dateFormatter value="${info.endTime}" />" :position-fixed="true"></aj-form-calendar-input>
						<span class="needed" style="color:red;">*</span>
					</label> 
				</div>
				<div>
					<label>
						<div class="label">当前参团人数：</div> 
						<input placeholder="" size="10" required="required" name="currentPerson" value="${info.currentPerson}" type="text" />
						<span class="needed" style="color:red;">*</span>
					</label> 
					<label>
						<div class="label">成团人数：</div> 
						<input placeholder="" size="10" required="required" name="minimumPerson" value="${info.minimumPerson}" type="text" />
					</label> 
				</div>
				<div>
					<label>
						<div class="label">最大商品供应数：</div> 
						<input placeholder="" size="10" required="required" name="maxGoodsNumber" value="${info.maxGoodsNumber}" type="text" />
						<span class="needed" style="color:red;">*</span>
					</label> 
					<label>
						<div class="label">已经出售商品份数：</div> 
						<input placeholder="" size="10" required="required" name="soldNumber" value="${info.soldNumber}" type="text" />
					</label> 
				</div>
				<div>
					<label>
						<div class="label">预计配送时间：</div> 
						<input placeholder="请填写${uiName}最低开团金额" size="20" required="required" name="deliveryTime" value="<c:dateFormatter value="${info.deliveryTime}" />" type="text" />
						<span class="needed" style="color:red;">*</span>
					</label> 
					<label>
						<div class="label">当前状态：</div> 
						<aj-select field-name="status" :json="${statusJSON}" default-selected="${statusJSON_defaultSelected}"></aj-select>
					</label> 
				</div>				
				
				<div>
				
					<table>
						<template v-for="item in goodsSelectFormat">
							<goods-select-format :data="item"></goods-select-format>
						</template>
					</table>
					<div align="center" v-if="!goodsSelectFormat.length">
						<button class="aj-btn" onclick="openGoodsSelect();return false;">
							<img class="icon" src="${commonAssetIcon}/add.gif" />选择商品
						</button>
					</div>
					
					<input type="hidden" name="goodsId" value="${info.goodsId}" required="required" />
				</div>
				
				<div>
					<!--按钮 -->
					<ajaxjs-admin-info-btns :is-create="${isCreate}"></ajaxjs-admin-info-btns>
				</div>
			</form>
 	</div>


	<!-- 选择商品 弹窗 -->
	<div class="goodsSelect"></div>
	<textarea class="hide GoodsSelectTpl">
		<aj-layer ref="layer">
			<div class="aj-tableList">
				<header style="width:950px;">
					<div style="width:5%">id</div><div style="width:30%">商品名称</div><div style="width:10%">品牌</div><div style="width:10%">封面价格</div>
					<div style="width:10%">商品分类</div><div style="width:30%">选择规格</div>
				</header>
				<aj-page-list api-url="${ctx}/admin/goods/listJson_format">
					<template slot-scope="item" :onclick="'location.assign(\'' + item.id + '/\');'">
						<div style="width: 5%">{{item.id}}</div>
						<div style="width:30%"><img :src="'${ctx}/' + item.cover" style="max-width:50px;" />{{item.name}}</div>
						<div style="width:10%">{{item.brand}}</div>
						<div style="width:10%">{{item.coverPrice}}</div>
						<div style="width:10%">{{item.catelogName}}</div>
						<div style="width:30%;">
							<div style="display: flex;justify-content: center;align-items: center;">
								<table v-html="renderFormat(item, item.formats)"></table>
								<a href="javascript:void(0);" @click="selectGoodsId(item.id)">选择</a>
							</div>
						</div>
					</template>
				</aj-page-list>
			</div>
		</aj-layer>
	</textarea>

<%
	request.setAttribute("BeanUtil", new com.ajaxjs.util.map.JsonHelper());
%>
	<script src="${ctx}/asset/js/simple-group-edit.js"></script>
	
	<script> 
		window.isCreate = ${isCreate};

		init('${ctx}', '${commonAssetIcon}', isCreate, ${empty info ? '{}' : BeanUtil.beanToJson(info)});
	</script>
</body>
</html>