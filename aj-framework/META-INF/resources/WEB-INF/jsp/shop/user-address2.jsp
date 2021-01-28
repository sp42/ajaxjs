 <%@page import="com.ajaxjs.sql.JdbcConnection"%>
<%@page pageEncoding="UTF-8"
	import="com.ajaxjs.sql.orm.Repository,com.ajaxjs.cms.service.UserAddressService.UserAddressDao,com.ajaxjs.framework.filter.DataBaseFilter"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%
	DataBaseFilter.initDb();
	boolean byUser = request.getAttribute("userId") != null;
	UserAddressDao dao = new Repository().bind(UserAddressDao.class);
	// request.setAttribute("PageResult", byUser ? dao.findListByUserId((long) request.getAttribute("userId")) : dao.findList(null));
	
	JdbcConnection.closeDb();
%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
	<jsp:param name="title" value="${uiName}管理" />
</jsp:include>
</head>
<body>
	<div class="vue">
		<!-- 后台头部导航 -->
		<aj-admin-header> <template slot="title">用户地址簿一览</template></aj-admin-header>
		<!-- 搜索、分类下拉 -->
		<aj-admin-filter-panel :no-catalog="true"></aj-admin-filter-panel>
	</div>

	<script>
		new Vue({
			el : 'body > div'
		});
	</script>

	<!-- 列表渲染，采用传统后端 MVC 渲染 -->
	<table class="aj-niceTable listTable">
		<colgroup>
			<col />
			<col />
			<col />
			<col />
			<col />
			<col />
			<col />
			<col style="text-align: center;" align="center" />
		</colgroup>
		<thead>
			<tr>
				<th>#</th>
				<th>收货人姓名</th>
				<th>手机/固话</th>
				<th>省市区</th>
				<th>收货地址</th>
				<th>是否默认</th>
				<th>用户</th>
				<th>控制</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="8"></td>
			</tr>
		</tfoot>
		<tbody>
			<c:foreach var="current" items="${PageResult}">
				<tr>
					<td>${current.id}</td>
					<td>${current.name}</td>
					<td>${current.mobile}<br />${current.phone}</td>
					<td>${current.province}${current.city}${current.district}</td>
					<td>${current.address}</td>
					<td>${current.isDefault ? '是' : '否'}</td>
					<td>${not empty current.username ? current.username : (not empty current.userIdName ? current.userIdName : '#'.concat(current.userId))}</td>
					<td>
						<a href="${ctx}/user/center/info/${current.userId}/">
							<img src="${commonAssetIcon}/user.png" style="width:16px;vertical-align: sub;" />用户详情
						</a> |
						<a href="javascript:aj.admin.del('${current.id}', '${current.name}');">
							<img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" /> 删 除
						</a>
					</td>
				</tr>
			</c:foreach>
		</tbody>
	</table>
</body>
</html>