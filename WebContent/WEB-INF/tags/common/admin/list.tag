<%@tag pageEncoding="UTF-8" description="管理界面-列表界面"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/less/admin.less" title="${uiName}列表" />
<body class="">
	<header class="top">
		<div class="right">
			<a href="../create.do">新建${uiName}</a> | 
			<a href="#" target="_blank">新窗口打开</a>
		</div>
		<h3>${uiName}一览</h3>
	</header>

	<div class="filterPanel">
		<form action="?" method="GET" >
			<input type="hidden" name="searchField" value="content" /> 
			<input type="text" name="searchValue" placeholder="请输入正文之关键字" style="float: inherit;" class="ajaxjs-inputField" />
			<button style="margin-top: 0;" class="ajaxjs-btn">搜索</button>
		</form>
		分类： 
		<select onchange="onCatalogSelected(this);" class="ajaxjs-select" style="width: 100px;">
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
		<script>
			function onCatalogSelected(el) {
				var catalogId = el.selectedOptions[0].value;
				if (catalogId == '全部分类')
					location.assign(location.origin + location.pathname); // todo
				else
					location.assign('?filterField=catalog&filterValue=' + catalogId);
			}
		</script> 
	</div>

	<jsp:doBody />

	<div class="pager">
		<commonTag:page type="page" pageInfo="${PageResult}" />
	</div>
	
	<script>
		entity = {
			del : function(id, title) {
				if (confirm('请确定删除记录：\n' + title + ' ？')) {
					xhr.dele('../' + id + '/delete.do', {}, function(json) {
						if (json.isOk) {
							alert('删除成功！');
							location.reload();
						}
					});
				}
			},
			setStatus : function(id, status) {
				xhr.post('../setStatus/' + id + '/action.do', {
					status : status
				}, function(json) {
					if (json.isOk) {
	
					}
				});
			}
	
		};
	</script>
</body>
</html>