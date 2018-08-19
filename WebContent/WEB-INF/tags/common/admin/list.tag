<%@tag pageEncoding="UTF-8" description="管理界面-列表界面"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<%@attribute required="false" type="Boolean" name="isSimpleSection"   description="是否需要简易栏目"%>
<%@attribute required="false" type="Boolean" name="isCatalog"   description="是否需要分类"%>
<%@attribute required="false" type="Boolean" name="isNoCreateBtn" description="是否不新建按钮"%>
<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/ajaxjs-ui/less/admin.less" title="${uiName}列表" />
<body class="list-ui">
	<header class="top">
		<div>
		<c:if test="${empty isNoCreateBtn || !isNoCreateBtn}">
			<a href="../">新建${uiName}</a> | 
	    </c:if>
			<a href="#" target="_blank">
				<img width="12" src="data:image/gif;base64,R0lGODlhEAAQAIABAAAAAP///yH5BAEAAAEALAAAAAAQABAAAAImjG+gq+je3gOBWURrlvVEuWlcKE4T2Xkql6zshkLuOIO1mVj6VgAAOw==" />
			新窗口打开</a>
		</div>
		
		<fieldset style="border-bottom: 0; border-left: 0; border-right: 0; border-top: 1px solid lightgray; width: 94%;margin: 0 auto;letter-spacing: 5px;">
			<legend style="margin-left: 1em;font-size:1.2rem;">
				${uiName}一览
			</legend>
		</fieldset>
	</header>

	<div class="filterPanel">
		<form action="?" method="GET" >
			<input type="hidden" name="searchField" value="content" /> 
			<input type="text" name="searchValue" placeholder="请输入正文之关键字" style="float: inherit;" class="ajaxjs-inputField" />
			<button style="margin-top: 0;" class="ajaxjs-btn">搜索</button>
		</form>
	<c:if test="${not empty isCatalog && isCatalog}">
		分类： 
		<select onchange="onCatalogSelected(this);" class="ajaxjs-select" style="width: 100px;">
			<option>全部分类</option>
			<c:foreach items="${catalogMenu}" var="current">
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
					location.assign('?filterField=catalog&filterValue=' + encodeURIComponent(catalogId));
			}
		</script> 
	</c:if>
	<c:if test="${not empty isSimpleSection && isSimpleSection}">
		栏目： 
		<select onchange="onSimpleSectionSelected(this);" class="ajaxjs-select" style="width: 200px;">
		</select>
		<jsp:useBean id="json" class="com.ajaxjs.js.JsonHelper"></jsp:useBean>
		<script>
			var catalogArr = ${json.beans2json(catalogMenu)};
			
			var selectUI = new ajaxjs.tree.selectUI();
			
			var select = document.querySelector('select');
			selectUI.renderer(catalogArr, select, ${empty param.catalogId ? 'null' : param.catalogId}, {makeAllOption : true});
			
			function onSimpleSectionSelected(el) {
				var catalogId = el.selectedOptions[0].value;
				if (catalogId == '全部分类')
					location.assign(location.origin + location.pathname); // todo
				else
					location.assign('?catalogId=' + catalogId);
			}
		</script> 
	</c:if>
	</div>

	<jsp:doBody />

	<div class="pager">
		<commonTag:list type="pager" pageInfo="${PageResult}" />
	</div>
	
	<script>
		entity = {
			del : function(id, title) {
				if (confirm('请确定删除记录：\n' + title + ' ？')) {
					ajaxjs.xhr.dele('../' + id + '/', function(json) {
						if (json.isOk) {
							alert('删除成功！');
							location.reload();
						}
					});
				}
			},
			setStatus : function(id, status) {
				ajaxjs.xhr.post('../setStatus/' + id + '/', function(json) {
					if (json.isOk) {
	
					}
				}, {
					status : status
				});
			}
	
		};
	</script>
</body>
</html>

<%-- 页脚 --%>

<footer>
	<div class="sitemap">
		<div>
			<div class="btn" onclick="document.querySelector('.sitemap').toggleCls('open');"></div>
			${SITE_STRU.getSiteMap(pageContext.request)}
		</div>
	</div>

	<div class="copyright">
		<div>
			<jsp:doBody />
			<a href="#"><img src="${commonImage}gs.png" height="40" /></a> 
			<a href="#"><img src="${commonImage}kexin.png" hspace="20" width="90" style="margin-top:15px;" /></a> 
			<a href="#"><img src="${commonImage}360logo.gif" width="90" style="margin-top:15px;" /></a>
			<br />
			<span>
				<a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>
				/<a href="javascript:;" class="Chinese" onclick="toChinese(this);">正体中文</a>
			</span>
			<script src="${commonAsset}js/libs/chinese.js"></script>
			<br />
			${empty _config.site_icp ? '粤ICP备15007080号-2' :  _config.site_icp}
			Powered by <a target="_blank" href="http://framework.ajaxjs.com">AJAXJS</a>
			<br />
			<%
			
			if(request.getAttribute("requestTimeRecorder") != null ) {
				Long requestTimeRecorder = (Long)request.getAttribute("requestTimeRecorder");
				requestTimeRecorder = System.currentTimeMillis() - requestTimeRecorder;
				float _requestTimeRecorder = (float)requestTimeRecorder;
				_requestTimeRecorder = _requestTimeRecorder / 1000;
				// float seconds = (endTime - startTime) / 1000F;
				request.setAttribute("requestTimeRecorder", _requestTimeRecorder); 
			}
			%>
	 		©Copyright <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%> 版权所有， ${_config.clientFullName} ${empty requestTimeRecorder ? '' : '请求完成耗时：'.concat(requestTimeRecorder).concat('秒') }
		</div>
	</div>
</footer>