<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="title" value="${uiName}管理" />
	</jsp:include>
	
	<!-- Admin 公共前端资源 -->
	<link rel="stylesheet" href="${aj_static_resource}/dist/css/admin/admin.css" />
	<script src="${aj_static_resource}dist/admin/admin.js"></script>
	
	<style>
		.tree-like select{
			width:100%;
			height:500px;
			border:0;
		}
		
		.tree-like.main-panel {
			height:500px;
		}
	</style>
</head>

<body>
	<div class="tree-like">
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header>
			<template slot="title">${uiName}一览</template>
		</ajaxjs-admin-header>
		
		<div class="soft-container">
			<div class="box padding">你可以在这里添加、修改、删除${uiName}。请于分类中点选目标的节点，成为选中的状态后，再进行下面的编辑。</div>
			<div class="box">
				<!-- 菜单工具栏-->
				<div class="toolbar">
					<ul>
						<li @click="rename"><i class="fa fa-pencil-square-o" style="color:#0a90f0;"></i> 修改分类名称</li>
						<li @click="dele"><i class="fa fa-trash-o" aria-hidden="true" style="color:red;"></i> 删除</li>
						<li>
							<form action="." method="post" class="createUnderNode">
								<input type="hidden" name="pid" :value="selectedId" />
								<input type="text" name="name" required size="12" /> 
								<button><i class="fa fa-plus" aria-hidden="true" style="color:#ffaf0a;"></i> 新建子分类</button>	
							</form>
						</li>
						<li>
							<form action="." method="post" class="createTopNode">
								<input type="hidden" name="pid" value="-1" />
								<input type="text" name="name" size="12" required /> 
								<button><i class="fa fa-plus" aria-hidden="true" style="color:#ffaf0a;"></i> 新增顶级${uiName}</button>	
							</form>
						</li>
					</ul>
				</div>
			</div>
			
			<div class="main-panel">
				<select multiple @change="onChange"></select>
			</div>
		
			<div class="box bottom-bar padding">
				<b v-show="selectedId != 0">已选择# {{selectedId}}-{{selectedName}}</b>
			</div> 
		</div>
		<aj-layer ref="layer">
			<form class="rename" :action="selectedId + '/'" method="put" style="text-align:center;">
				<h5>修改${uiName}名称</h5>
				<br />
				名称： <input type="text" class="aj-input" name="name" :value="selectedName" required /> 
				<br />
				<br />
				<button class="aj-btn">
					<img src="${commonAssetIcon}/update.gif" /> 更新名称
				</button>
				<br />
				<br />
			</form>
		</aj-layer>
	</div>

	<script src="${aj_static_resource}dist/admin/system/tree-like.js"></script>
</body>
</html>
