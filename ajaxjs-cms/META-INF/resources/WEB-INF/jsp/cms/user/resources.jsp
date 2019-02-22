<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<adminUI:list isNoCatalog="true" isNoCreateBtn="true">
	<table class="ajaxjs-niceTable" align="center" width="90%">
		<colgroup>
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
				<th>${uiName}内容</th>
				<th>锁值</th>
				<th>创建时间</th>
		
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="7"></td>
			</tr>
		</tfoot>
		<tbody>
			<c:foreach var="current" items="${PageResult}">
				<tr>
					<td>${current.id}</td>
					<td>${current.name}</td>
					<td title="${current.content}">${current.content}</td>
					<td>${current.lockValue}</td>
					<td><c:dateFormatter value="${current.createDate}" /></td>
				</tr>
			</c:foreach>
		</tbody>
	</table>
</adminUI:list>
