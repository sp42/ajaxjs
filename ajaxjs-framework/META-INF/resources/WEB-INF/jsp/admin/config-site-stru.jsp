<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="网站结构" />
		</jsp:include>
		<style>
		
		</style>
	</head>
	
<body>
	<div>
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header>
			<template slot="title">网站结构</template>
		</ajaxjs-admin-header>
	</div>
	<script>
		new Vue({el:' body > div'});
	</script>

	<div class="tree" style="margin:0 auto;">
		<div class="tooltip tipsNote hide">
			<div class="aj-arrow toLeft"></div>
			<span>用户名等于账号名；不能与现有的账号名相同；注册后不能修改；</span>
		</div>
	</div>
	
	<textarea class="hide tpl">
		<span class="indicator">new</span> 
		<div class="valueHolder">
			<input type="text" size="10" name="id"    placeholder="id标识符，必填" />   <button class="saveId">   <img class="icon" src="${commonAssetIcon}/save.gif" /> 保存</button>
			<input type="text" size="10" name="name"  placeholder="显示的名称，必填"  />	<button class="saveName"> <img class="icon" src="${commonAssetIcon}/save.gif" /> 保存</button>
			<input type="text" size="10" name="param" placeholder="参数，选填" />		<button class="saveParam"><img class="icon" src="${commonAssetIcon}/save.gif" /> 保存</button>
			
			<button class="initJSP">初始化页面 </button>
		</div>
		<span class="name">new</span> 
		<span class="fa fa-plus" style="color:#00c3fa;"></span> 
		<span class="fa fa-minus" style="color:red;"></span>					 
		<span class="up">▲</span> 
		<span class="down">▼</span>
		<label><input type="checkbox" /> 隐藏？</label>
	</textarea> 
	<textarea class="hide addTpl">
		添加新节点
		<br />
		<br />
		&nbsp;&nbsp;id&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="id" class="aj-input" placeholder="id标识符，必填" />
		<br />
		<br />
		名称 <input type="text" class="aj-input" name="name" placeholder="显示的名称，必填" />
	</textarea>
	<br />
	<br />
	<script>
		jsonData = ${siteStruJson};
	</script>
	<center>
		<button class="aj-btn addTop">新增顶级节点</button>
	</center>
	<script src="${ctx}/asset/admin/config-site-stru.js"></script>
</body>
</html>