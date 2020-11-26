<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="网站结构" />
		</jsp:include>
	</head>
<body>
	<div>
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header>
			<template slot="title">网站信息</template>
		</ajaxjs-admin-header>
	</div>
	<script>
		new Vue({el:' body > div'});
	</script>
	<div class="admin-entry-form">
		<form method="POST" action="." class="configForm">
			<div>
				<label>
					<div class="label">公司全称<span class="required-note">*</span></div> 
					<input type="text" placeholder="公司全称" name="clientFullName" data-note2="请输入你公司的全称" value="${aj_allConfig.clientFullName}" />
				</label> 
			</div>
			<div>
				<label>
					<div class="label">公司简称<span class="required-note">*</span></div> 
					<input type="text" placeholder="公司简称" name="clientShortName" data-note2="请输入你公司的简称" value="${aj_allConfig.clientShortName}" />
				</label>
			</div>
			<div>
				<label>
					<div class="label">网站标题前缀<span class="required-note">*</span></div> 
					<input type="text" placeholder="用户名" name="site.titlePrefix" required data-note="网站全局的标题，在 title 标签中显示" value="${aj_allConfig.site.titlePrefix}" />
				</label>
			</div>
			<div>
				<label>
					<div class="label">搜索关键字<span class="required-note">*</span></div> 
					<textarea name="site.keywords" rows="10" cols="50"
						class="textarea" data-note2="用于 SEO 关键字优化，以便于搜索引擎机器人查找、分类。<br />多个关键字之间用英文 , 逗号分开">${aj_allConfig.site.keywords}</textarea>
					<div class="sub">用于 SEO 关键字优化，以便于搜索引擎机器人查找、分类。多个关键字之间用英文 , 逗号分开</div>
				</label>
			</div>
			
			<div>
				<label>
					<div class="label">网站描述<span class="required-note">*</span></div> 
					<textarea name="site.description" rows="10">${aj_allConfig.site.description}</textarea>
					<div class="sub">网站的说明文本。通常是一段话介绍网站即可。</div>
				</label>
			</div>
	
			<section class="aj-btnsHolder">
				<button class="aj-btn">
					<img src="${commonAssetIcon}/save.gif" /> 修改
				</button>
				<button class="aj-btn" onclick="this.up('form').reset();return false;">复 位</button>
			</section>
		</form>
	</div>
	<script>
		ajaxjs.xhr.form('form');
	</script>
</body>
</html>