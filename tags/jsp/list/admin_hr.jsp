<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="commonUI"  tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/bigfoot/asset/less/user.less" />
<body>
	<commonUI:adminHeader pageTitle="${uiName}列表">
		<a href="../edit">新建${uiName}</a> | <a href="../edit/catalog/${tablename}">${uiName}管理</a> |
	</commonUI:adminHeader>
	<h2>${uiName}列表</h2>
	<br />
	<table class="niceTable" align="center">
		<colgroup>  
            <col />     
            <col />     
            <col />     
            <col />     
            <col />     
            <col />     
            <col style="text-align:center;" align="center" />     
     	</colgroup> 
		<thead>
			<tr>
				<th>#</th>
				<th class="name">${uiName}名称</th>
				<th>创建时间</th>
				<th>修改时间</th>
				<th>工作年限</th>
				<th>类型</th>
				<th class="control">控 制</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="8">
					<div class="filter">
						<label>
					<form action="?" method="GET">
						<input type="hidden" name="searchField" value="name" />
						<input type="text" name="searchValue" placeholder="请输入标题或正文之关键字" style="float: inherit;" />
						<button style="margin-top:0;" class="my-btn">搜索</button>
					</form>
						</label>
					</div>
					<div class="pager">
					<c:if test="${not empty PageResult}">
						<commonTag:page type="page" pageInfo="${PageResult}" />
					</c:if>
					</div>
				</td>
			</tr>
		</tfoot>
	 	<tbody>
		<c:foreach var="current" items="${PageResult.rows}">
			<tr> 
				<td>${current.id}</td>
				<td>${current.name}</td>
	
				<td>${viewUtils.formatDateShorter(current.createDate)}</td>
				<td>${viewUtils.formatDateShorter(current.updateDate)}</td>
				<td>${current.expr}</td>
				<td>${current.jobType}</td>
				<td>
					<a href="../${current.id}.info">浏览</a>
					<a href="../edit/${current.id}"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/update.gif" style="vertical-align: sub;" /> 编辑</a>
					<a href="javascript:entity.del('${current.id}', '${current.name}');"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/delete.gif" style="vertical-align: sub;" /> 删除</a>
				</td>
			</tr>
		</c:foreach>
 
	 	</tbody>
	</table>
</body>
</html>
