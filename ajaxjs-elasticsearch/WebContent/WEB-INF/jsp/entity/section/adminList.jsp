<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="c" uri="/ajaxjs"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="adminUI" tagdir="/WEB-INF/tags/common/admin"%>
<%
	request.setAttribute("crawlerTypes", new String[]{"视频","音频","文章"});
%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/bigfoot/asset/less/user.less" />
<body>
	<commonUI:adminHeader pageTitle="${uiName}列表" maxWin="false">
		<a href="../edit">新建${uiName}</a> | <a href="../edit/list">${uiName}管理</a> |
	</commonUI:adminHeader>
	<h2>${uiName}列表</h2>
	<br />
	<fmt:formatDate value="<%=new java.util.Date()%>" type="time" timeStyle="full" /> 
	<table class="niceTable" align="center">
		<colgroup>  
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
				<th>抓取类型</th>
				<th>门户+分类</th>
				<th>更新情况</th>
	
				<th class="control">控 制</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="9">
					<div class="filter">
						<label>
					<form action="?" method="GET">
						<input type="hidden" name="ns" value="${uiName}" />
						<input type="text" name="keyword" placeholder="请输入标题之关键字" style="float: inherit;" />
						<button style="margin:0;" class="my-btn"> <img src="../../../asset/bigfoot/skin/icon/search.gif" style="vertical-align: sub;" />搜 索 </button>
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
				<td title="${current.intro}">${current.name}</td>
				<td>${viewUtils.formatDateShorter(current.createDate)}</td>
				<td>${crawlerTypes[current.crawlerType]}</td>
				<td>
					${portals_map[current.portalId]['name']}
					<a href="${current.url}" target="_blank">
						${catalogs_map[current.catalog]['name']}
					</a>
				</td>
				<td>
					${viewUtils.formatDateShorter(current.lastUpdate)}
				</td>
				<td> 
					<adminUI:edit type="adminList_btns" current="${current}" />
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
	<br>

</body>
</html>
