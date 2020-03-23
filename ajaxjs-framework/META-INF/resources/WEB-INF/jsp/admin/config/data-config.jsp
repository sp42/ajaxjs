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
						<div class="aj-json-form">
							<form style="padding:2%;box-sizing: border-box;">
								当前数据库连接：
								<select class="ajaxjs-select" style="width: 200px;">
									<c:foreach items="${list}" var="current">
										<c:choose>
											<c:when test="${param.filterValue == current.id}">
												<option value="${current.name}" selected>${current.name}</option>
											</c:when>
											<c:otherwise>
												<option value="${current.name}">${current.name}</option>
											</c:otherwise>
										</c:choose>
									</c:foreach>
								</select>
								
								<button class="ajaxjs-btn">保存</button>
							
							</form>
						</div>
		<ul>
			<c:foreach items="${list}" var="item">
				<li class="aj-json-form">
					<form>
						<div>
							<table width="100%">
								<tr>
									<td width="50%">
							<div class="label">名称：</div><div class="input"><input type="text" value="${item.name}" /></div>
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
										<div class="input"><input type="text" value="${item.username}" /></div>
									</td>
									<td>
										<div class="label">密码：</div>
										<div class="input"><input type="text" value="${item.password}" /></div>
									
									</td>
								</tr>
							</table>
						</div>
						<div>
							<div class="label" style="width:16%;">URL：</div><div class="input"><input type="text" value="${item.url}" size="68" /></div>
						</div>
						<div>
							&nbsp;&nbsp;&nbsp;Notes:数据库 url 注意相关字符串要转义，如：&amp;
						</div>
						<div>
							<section class="aj-btnsHolder">
								<button>
									<img src="${ctx}/asset/common/icon/save.gif" /> 修改
								</button>
								<button class="ajaxjs-btn" onclick="this.up(\'form\').reset();return false;">复 位</button>
							</section>
						</div>
					</form>
				</li>
			</c:foreach>
		</ul>
		
		
					</div>
					<div><aj-json-form :scheme="scheme" :config="config" path="data"></aj-json-form></div>
					<div><aj-json-form :scheme="scheme" :config="config" path="entity"></aj-json-form></div>
					<div><aj-json-form :scheme="scheme" :config="config" path="domain.article"></aj-json-form></div>
					<div><aj-json-form :scheme="scheme" :config="config" path="domain.shop"></aj-json-form></div>
				</div>
			</div>	
		</div>
		
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
