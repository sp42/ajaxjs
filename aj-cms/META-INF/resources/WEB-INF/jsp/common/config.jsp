<%@page pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
	<!-- 全局统一的 HTML 文件头 -->
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="title" value="网站结构" />
	</jsp:include>
	
	<!-- Admin 公共前端资源 -->
	<link rel="stylesheet" href="${aj_static_resource}/dist/css/admin/admin.css" />
	<script src="${aj_static_resource}dist/admin/admin.js"></script>
</head>
<body>
	<div class="aj-json-form">
		<span>
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">配置参数</template>
			</ajaxjs-admin-header>
		</span>

		<div class="soft-container">
			<div class="box padding">
				<select class="aj-select right" style="width:180px;" onchange="changeTab(this);">
					<option>常规参数</option>
					<option>数据配置</option>
				</select>
				小白用户请谨慎修改配置参数
			</div> 
			<div>
<!-- 常规参数  -->
<div class="config-tab-0 show">
	<div class="aj-simple-tab-horizontal tab" style="padding: 1% 5%;">
		<ul>
			<li>文件上传配置</li><li>用户配置</li><li>邮件服务器配置</li>
			<li>加密配置</li><li>短信服务配置</li><li>网络安全防控</li><li>开发者参数</li>
		</ul>
		<div>
			<div>
				<aj-json-form :scheme="scheme" :config="config" path="uploadFile"></aj-json-form>
			</div>
			<div>
				<aj-json-form :scheme="scheme" :config="config" path="user"></aj-json-form>
			</div>
			<div>
				<aj-json-form :scheme="scheme" :config="config" path="mailServer"></aj-json-form>
			</div>
			<div>
				<aj-json-form :scheme="scheme" :config="config" path="Symmetric"></aj-json-form>
			</div>
			<div>
				<aj-json-form :scheme="scheme" :config="config" path="sms"></aj-json-form>
			</div>
			<div>
				<aj-json-form :scheme="scheme" :config="config" path="security"></aj-json-form>
			</div>
			<div>
				<aj-json-form :scheme="scheme" :config="config"
					path="forDelevelopers"></aj-json-form>
			</div>
		</div>
	</div>
</div>
<!-- // 常规参数  -->
			
<!-- 数据配置  -->
<div class="config-tab-1 hide">
	<div class="aj-simple-tab-horizontal tab" style="padding: 1% 5%;">
		<ul>
			<li>数据库连接</li>
			<li>数据配置</li>
			<li>通用实体配置</li>
			<li>图文配置</li>
			<li>商城配置</li>
		</ul>
		<div>
			<div>
				<form>
					<div>
						<table width="100%">
							<tr>
								<td width="50%">
									<div class="label">名称：</div><div class="input"><input type="text" value="${conn.name}" /></div>
								</td>
								<td></td>
							</tr>
						</table>
					</div>
					<div>
						<table width="100%">
							<tr>
								<td>
									<div class="label">账号：</div>
									<div class="input"><input type="text" value="${conn.username}" /></div>
								</td>
								<td>
									<div class="label">密码：</div>
									<div class="input"><input type="password" value="${conn.password}" /></div>
								
								</td>
							</tr>
						</table>
					</div>
					<div>
						<div class="label" style="width:16%;">URL：</div><div class="input"><input type="text" value="${conn.url}" size="68" /></div>
					</div>
					<div>
						&nbsp;&nbsp;&nbsp;Notes:数据库 url 注意相关字符串要转义，如：&amp;
					</div>
					<%-- <div>
						<section class="aj-btnsHolder">
							<button>
								<img src="${ctx}/asset/common/icon/save.gif" /> 修改
							</button>
							<button class="aj-btn" onclick="this.up(\'form\').reset();return false;">复 位</button>
						</section>
					</div> --%>
				</form>
			</div>
			<div><aj-json-form :scheme="scheme" :config="config" path="data"></aj-json-form></div>
			<div><aj-json-form :scheme="scheme" :config="config" path="entity"></aj-json-form></div>
			<div><aj-json-form :scheme="scheme" :config="config" path="domain.article"></aj-json-form></div>
			<div>
				<aj-json-form :scheme="scheme" :config="config" path="shop"></aj-json-form>
				<h3>微信公众号</h3>
				<aj-json-form :scheme="scheme" :config="config" path="wx_open"></aj-json-form>
				<h3>小程序</h3>
				<aj-json-form :scheme="scheme" :config="config" path="mini_program"></aj-json-form>
			</div>
		</div>
	</div>	
</div>
<!-- // 数据配置  -->
			</div>
		</div>
	</div>
	<script src="${aj_static_resource}dist/admin/system/config-parser.js"></script>
	<script>
		new Vue({el:'.aj-json-form > span'});
		
		new Vue({
			el:'.config-tab-0',
			data: {
				scheme: ${schemeJson},
				config: ${configJson}
			},
			mixins: [aj.widget.tab.tabable]
		});
		
		new Vue({
			el:'.config-tab-1',
			data: {
				scheme: ${schemeJson},
				config: ${configJson}
			},
			mixins: [aj.widget.tab.tabable]
		});
		
		changeTab = function(e) {
			var old = aj('.show');
			old.classList.remove('show');
			old.classList.add('hide');
			
			var el = aj('.config-tab-' + e.selectedIndex);
			el.classList.remove('hide');
			el.classList.add('show');
		}
	</script>
</body>
</html>
