<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<adminUI:list isNoCatalog="true">
	<table class="ajaxjs-niceTable">
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
				<th>角色</th>
				<th>创建时间</th>
				<th class="control">控 制</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="5"></td>
			</tr>
		</tfoot>
		<tbody>
			<c:foreach var="current" items="${PageResult}">
				<tr>
					<td>${current.id}</td>
					<td>${current.name}</td>
					<td>${current.roleName}</td>
					<td><c:dateFormatter value="${current.createDate}" /></td>
					<td>
						<a href="../${current.id}/"><img src="${commonAssetIcon}update.gif" style="vertical-align: sub;" />编辑</a>
						<a href="javascript:entity.del('${current.id}', '${current.name}');"><img src="${commonAssetIcon}delete.gif" style="vertical-align: sub;" />删除</a>
					</td>
				</tr>
			</c:foreach>
		</tbody>
	</table>
</adminUI:list>
