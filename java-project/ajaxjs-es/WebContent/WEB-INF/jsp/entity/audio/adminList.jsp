<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="commonUI"  tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/bigfoot/asset/less/user.less" />
<body>
	<commonUI:adminHeader pageTitle="${uiName}列表">
		<a href="new">新建${uiName}</a> | <a href="../edit/catalog/${tablename}">${uiName}管理</a> |
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
						<input type="text" name="keyword" placeholder="请输入标题或正文之关键字" style="float: inherit;" />
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
										location.assign('?catalogId=' + catalogId);
								}
							</script>
							<select onchange="onCatalogSelected(this);" class="select_1" style="width:50px;">
								<option>全部分类</option>
<%-- 						<commonTag:iterator array="${catalogs}" isOutterInclude="true" isOutterItemInclude="true"> --%>
<%-- 							<jsp:attribute name="itemTag"> --%>
<%-- 						<c:choose> --%>
<%-- 							<c:when test="${param.catalogId == current.id}"> --%>
<%-- 								<option value="${current.id}" selected>${current.name}</option> --%>
<%-- 							</c:when> --%>
<%-- 							<c:otherwise> --%>
<%-- 								<option value="${current.id}">${current.name}</option> --%>
<%-- 							</c:otherwise> --%>
<%-- 						</c:choose> --%>
<%-- 							</jsp:attribute> --%>
<%-- 						</commonTag:iterator> --%>
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
				<td title="${current.intro}">
					<a href="${current.id}">${current.name}</a>
				</td>
	
				<td>${viewUtils.formatDateShorter(current.createDate)}</td>
				<td>${viewUtils.formatDateShorter(current.updateDate)}</td>
				<td>${catalogs_map[current.catalog]['name']}</td>
				<td>${current.online ? '已上线' : '已下线'}</td>
				<td>
					<a href="../${current.id}">浏览</a>
					<a href="${current.id}"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/update.gif" style="vertical-align: sub;" /> 编辑</a>
					<a href="javascript:entity.del('${current.id}', '${current.name}');"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/delete.gif" style="vertical-align: sub;" /> 删除</a>
				</td>
			</tr>
		</c:foreach>
 
	 	</tbody>
	</table>
	
	<script>
		entity = {
			del : function(id, title){
				if(confirm('请确定删除记录：\n' + title + ' ？')){
					xhr.dele('../' + id, {}, function(json){
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
