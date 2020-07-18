<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" 	uri="/ajaxjs"%>
<!doctype html>
<html>
	<head>
		<!-- 全局统一的 HTML 文件头 -->
		<jsp:include page="/WEB-INF/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="数据配置" />
		</jsp:include>
	</head>
	<body>
		<div class="aj-json-form">
			<!-- 后台头部导航 -->
			<ajaxjs-admin-header>
				<template slot="title">数据配置</template>
			</ajaxjs-admin-header>
			
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
		
		<style>
			h3{
			    margin: 2% auto;
    			width: 80%;
			}
		</style>
		<script src="${ctx}/asset/admin/config-parser.js"></script>
		<script>
			new Vue({
				el:'.aj-json-form',
				data: {
					scheme : ${schemeJson},
					config : ${configJson}
				},
				mixins: [aj.tabable]
			});
		</script>
	</body>
</html>
