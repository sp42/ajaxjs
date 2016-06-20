<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="c" uri="/ajaxjs"%>
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
				<th>类型</th>
				<th>门户</th>
				<th>分类</th>
				<th>原地址</th>
				<th>更新情况+集数</th>
	
				<th class="control">控 制</th>
			</tr>
		</thead>
		<tfoot>
			<tr>
				<td colspan="9">
					<div class="filter">
						<label>
					<form action="?" method="GET">
						<input type="hidden" name="searchField" value="name" />
						<input type="text" name="searchValue" placeholder="请输入标题之关键字" style="float: inherit;" />
						<button style="margin:0;" class="my-btn"> <img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/search.gif" style="vertical-align: sub;" />搜 索 </button>
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
				<td>${all_portals_map[current.portalId]['name']}</td>
				<td>
					<a href="?filterField=catalog&filterValue=${current.catalog}">
						${all_catalogs_map[current.catalog]['name']}
					</a>
				</td>
				<td>
					<a href="${current.url}" target="_blank">来源</a> |
					<a href="#" onmouseover="showCover(arguments[0], this);" onmouseout="hideCover(arguments[0]);" data-img="${current.cover}">封面</a>
				</td>
				<td style="font-family:Courier;font-size:.7rem;">${viewUtils.formatDateShorter(current.lastUpdate)}<br/>${current.length}</td>
				<td> 
					<adminUI:edit type="adminList_btns" current="${current}" />
				</td>
			</tr>
		</c:foreach>
 
	 	</tbody>
	</table>
	
			<div class="cover_img">
						<img src="" />
					</div>
					<script>
						var cover_img = document.querySelector('.cover_img');
						function showCover(e, el){
							cover_img.addCls('show');
							if(el.getAttribute('data-img')){
								cover_img.querySelector('img').src = el.getAttribute('data-img');
							}
							cover_img.style.left = e.clientX + 'px';
							cover_img.style.top = e.clientY + 'px';
						}
						
						function hideCover(e){
							cover_img.querySelector('img').src = '';
							cover_img.removeCls('show');
						}
					</script>
					<style>
						.cover_img{
							display:none;
							position: fixed;
							width:300px;
							height:150px;
						}
						.cover_img img {
							width:100%;
						}
						.show{
							display:block;
						}
					</style>
	<script>
		entity = {
				setOnline : function(el){
					if(true){
						el.innerHTML = "已上线";
					}
				},
				setOffline : function(el){
					if(true){
						el.innerHTML = "已下线";
					}
				},
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
	<br>

</body>
</html>
