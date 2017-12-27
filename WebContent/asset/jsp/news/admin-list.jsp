<%@page pageEncoding="UTF-8" import="com.ajaxjs.web.Constant"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="adminUI" tagdir="/WEB-INF/tags/common/admin"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<%
	request.setAttribute("commonIcon", Constant.commonIcon);
%>
<adminUI:list>
	<table class="ajaxjs-niceTable" align="center" width="90%">
		<colgroup>
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
				<th class="name">${uiName}名称</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>分 类</th>
				<th>是否上线</th>
				<th class="control">控 制</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="8"></td>
			</tr>
		</tfoot>
		<tbody>
			<c:foreach var="current" items="${PageResult.rows}">
				<tr>
					<td>${current.id}</td>
					<td title="${current.intro}">${current.name}</td>

					<td>
						<c:dateFormatter value="${current.createDate}" format="yyyy-MM-dd" />
					</td>
					<td>
						 <c:dateFormatter value="${current.updateDate}" format="yyyy-MM-dd" /> 
					</td>
					<td>${catalogsMap[current.catalog]['name']}</td>
					<td>
						<%-- 				${current.status == 1 ? '已上线(<a href="javascript:entity.setStatus(' + current.id + ', 0);void(0);">下线</a>)' : '已下线'}${current.catalog} --%>
					</td>
					<td>
						<a href="../../../${tableName}/${current.id}/info.do" target="_blank">浏览</a>
						<a href="../${current.id}/edit.do"><img src="${pageContext.request.contextPath}/${commonIcon}update.gif" style="vertical-align: sub;" />编辑</a>
						<a href="javascript:entity.del('${current.id}', '${current.name}');"><img src="${pageContext.request.contextPath}/${commonIcon}delete.gif" style="vertical-align: sub;" />删除</a>
					</td>
				</tr>
			</c:foreach>
		</tbody>
	</table>
</adminUI:list>
