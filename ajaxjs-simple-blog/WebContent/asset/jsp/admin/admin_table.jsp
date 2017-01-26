<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/less/admin.less" title="${uiName} 列表" />
<body>
	<commonTag:adminUI type="header" pageTitle="${uiName}列表">
		<a href="create">新建${uiName}</a> | <a href="../edit/catalog/${tablename}">${uiName}管理</a> |
	</commonTag:adminUI>
	<h2 class="title">${uiName}列表</h2>
	<br />
	<div style="width:90%;margin: 0 auto;text-align: right;margin-bottom: 1%;">
		<commonTag:page type="catalog_dropdownlist" /> <a href="javascript:location.reload();">刷新</a> &nbsp;
	</div>
	<table class="niceTable" align="center" width="90%">
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
				<th>分 类</th>
				<th>是否上线</th>
				<th class="control">控 制</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="8">
					<div class="filter">
						<label>
					<form action="?" method="GET">
						<input type="hidden" name="ns" value="${uiName}" />
						<input type="text" name="keyword" placeholder="搜索标题/正文之关键字" style="float: inherit;" />
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
				<td title="${current.brief}">${current.name}</td>
	
				<td align="left">${current.createDate}</td>
				<td>${current.catalogName}</td>
				<td>${current.online ? '已上线' : '已上线'}</td>
				<td>
					<a href="../${current.id}.info">浏览</a>
					<a href="edit/${current.id}"><img src="${pageContext.request.contextPath}/asset/images/icon/update.gif" style="vertical-align: sub;" /> 编辑</a>
					<a href="javascript:entity.del('${current.id}', '${current.name}');"><img src="${pageContext.request.contextPath}/asset/images/icon/delete.gif" style="vertical-align: sub;" /> 删除</a>
				</td>
			</tr>
		</c:foreach>
 
	 	</tbody>
	</table>
	
	<script>
		entity = {
			del : function(id, title){
				if(confirm('请确定删除记录：\n' + title + ' ？')){
					xhr.dele(id, {}, function(json){
						if(json.isOk){
							alert('删除成功！');
							location.reload();
						}
					});
				}
			}
		};
	</script>
</body>
</html>
