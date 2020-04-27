<%@page import="com.ajaxjs.orm.JdbcConnection"%>
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
	<div class="vue">
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header> <template slot="title">用户收藏一览</template></ajaxjs-admin-header>
	</div>

	<script>
		new Vue({
			el : 'body > div'
		});
	</script>

	<!-- 列表渲染，采用传统后端 MVC 渲染 -->
	<table class="ajaxjs-niceTable listTable">
		<colgroup>
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
				<th>实体 id</th>
				<th>实体名称</th>
				<th>实体类型</th>
				<th>收藏时间</th>
				<th>操 作</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="6"></td>
			</tr>
		</tfoot>
		<tbody>
			<c:foreach var="current" items="${PageResult}">
				<tr>
					<td>${current.id}</td>
					<td>#${current.entryId}</td>
					<td style="text-align:left;"><c:if test="${not empty current.cover}">
							<img src="${ctx}${current.cover}"
								style="max-width: 50px; max-height: 60px; vertical-align: middle;"
								onmouseenter="aj.imageEnlarger.singleInstance.imgUrl = '${ctx}${current.cover}';"
								onmouseleave="aj.imageEnlarger.singleInstance.imgUrl = null;" />
						</c:if> <b>${current.name}</b>
					</td>
					<td>${entryTypeIdNameMap[current.entryTypeId]}</td>
					<td><c:dateFormatter value="${current.createDate}"  /></td>
					<td>
						<a href="${current.entryId}">实体详情</a> 
						<a href="${ctx}/user/center/info/${current.userId}/"><img src="${commonAssetIcon}/user.png" style="width:16px;vertical-align: sub;" />用户详情</a> 
						<a href="javascript:aj.admin.del('${current.id}', '${current.name}');">
							<img src="${commonAssetIcon}/delete.gif" style="vertical-align: sub;" /> 删 除
						</a>
					</td>
				</tr>
			</c:foreach>
		</tbody>
	</table>
		<div class="listTable pager">
			<%@include file="/WEB-INF/jsp/pager.jsp" %>
		</div>
	<script>
		aj.imageEnlarger();// 鼠标移动大图
	</script>
</body>
</html>