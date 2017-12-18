<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="commonUI"  tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/less/admin.less" title="${uiName}列表" />
<body class="editUI-list">
	<commonTag:adminUI type="header" pageTitle="${uiName}列表">
		<a href="../create.do">新建${uiName}</a> | 
	</commonTag:adminUI>
	<h2>${uiName}列表</h2>
	<br />
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
				<th>修改时间</th>
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
						<input type="hidden" name="searchField" value="content" />
						<input type="text" name="searchValue" placeholder="请输入正文之关键字" style="float: inherit;" />
						<button style="margin-top:0;" class="my-btn">搜索</button>
					</form>
						</label>
						
						<label>分类：
							<script>
								function onCatalogSelected(el){
									var catalogId = el.selectedOptions[0].value;
									if(catalogId == '全部分类')
										location.assign(location.origin + location.pathname); // todo
									else
										location.assign('?filterField=catalog&filterValue=' + catalogId);
								}
							</script>
							<select onchange="onCatalogSelected(this);" class="select_1" style="width:100px;">
								<option>全部分类</option>
					<c:foreach items="${catalogs}" var="current">
						<c:choose>
							<c:when test="${param.filterValue == current.id}">
								<option value="${current.id}" selected>${current.name}</option>
							</c:when>
							<c:otherwise>
								<option value="${current.id}">${current.name}</option>
							</c:otherwise>
						</c:choose>
					</c:foreach>
							</select>
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
				<td title="${current.intro}">${current.name}</td>
	
				<td><c:dateFormatter date="${current.createDate}" format="yyyy-MM-dd" /></td>
				<td><c:dateFormatter date="${current.updateDate}" format="yyyy-MM-dd" /></td>
				<td>${catalogsMap[current.catalog]['name']}</td>
				<td>
<%-- 				${current.status == 1 ? '已上线(<a href="javascript:entity.setStatus(' + current.id + ', 0);void(0);">下线</a>)' : '已下线'}${current.catalog} --%>
				</td>
				<td>
					<commonTag:adminUI type="adminList_btns" current="${current}"  viewLink="../../../${tableName}" />
				</td>
			</tr>
		</c:foreach>
 
	 	</tbody>
	</table>
	
	<script>
		entity = {
			del : function(id, title){
				if(confirm('请确定删除记录：\n' + title + ' ？')){
					xhr.dele('../' + id + '/delete.do', {}, function(json){
						if(json.isOk){
							alert('删除成功！');
							location.reload();
						}
					});
				}
			},
			setStatus : function(id, status) {
				xhr.post('../setStatus/' + id + '/action.do', {
					status : status
				}, function(json){
					if(json.isOk){
						
					}
				});
			}
		
		};
	</script>
</body>
</html>
