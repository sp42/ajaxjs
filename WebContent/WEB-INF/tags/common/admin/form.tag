<%@tag pageEncoding="UTF-8" import="com.ajaxjs.web.Constant" description="编辑器框架"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<%@attribute name="moreBtn" required="false" fragment="true" description="是否需要按钮"%>
<%@attribute name="f" required="false" fragment="true" description="是否需要按钮"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/less/admin.less" title="${isCreate ? '新建' : '编辑:'}${uiName}${info.name}" />
	<body class="admin-entry-form">
		<header class="top">
			<div class="right">
				<c:if test="${empty isCreate}">
					<a href="../create.do">新建${uiName}</a> | 
				</c:if>
				
				<a href="${isCreate ? '' : '../'}list/do.do?start=0&limit=9">${uiName}列表</a> |  
				<a href="#" target="_blank">新窗口打开</a>
			</div>
			<h3>${isCreate ? '新建' : '编辑'}${uiName} ${not empty info.id ? '#': ''}${info.id}</h3>
		</header>

		<form action="action.do" method="${isCreate ? 'POST' : 'PUT'}">
			<!-- 传送 id 参数 -->
			<input type="hidden" name="id" value="${info.id}" />
			<jsp:doBody />
		
			
			<div>
				<div class="label" style="vertical-align: top;">正 文：</div>
				<commonUI:htmlEditor name="content" basePath="../">${info.content}</commonUI:htmlEditor>
			</div>
		
			<div style="text-align: center;">
				<jsp:invoke fragment="moreBtn" />
				<button class="ajaxjs-btn">
					<img src="${pageContext.request.contextPath}/<%=Constant.commonIcon%>save.gif" />
					${isCreate ? '新建' : '修改'}
				</button>
				<button class="ajaxjs-btn" onclick="this.up('form').reset();return false;">复 位</button>
			<%if(request.getAttribute("isCreate") == null) {%>
				<button class="ajaxjs-btn" onclick="del();return false;">
					<img src="${pageContext.request.contextPath}/<%=Constant.commonIcon%>delete.gif" /> 删 除
				</button>
			<%} %>
			</div>
		</form>
		
		<script>
			var formConfig = {
				isCommonAfterSubmit : true
			};
		
			if (this.htmlEditor) {
				formConfig.htmlEditor_hook = htmlEditor;
			}
		</script>
		<c:choose>
			<c:when test="${isCreate}">
				<script>
					form = new bf_form(document.querySelector('form'), formConfig);
					form.on('afterSubmit', function(form, json) {
						if (json.isOk) {
							//location.assign(json.newlyId); // 跳转编辑模式
							//location.assign('create.do?' + (still_params ? still_params() : ''));// 提交后回到新建
						}
					});
				</script>
			</c:when>
			<c:otherwise>
				<script>
					new bf_form(document.querySelector('form'), formConfig);
		
					function del() {
						if (confirm('确定删除 \n${info.name}？'))
							XMLHttpRequest.dele('delete.do', {}, function(json) {
								if (json && json.isOk) {
									alert(json.msg);
									location.assign('../list/list.do');
								}
							});
					}
				</script>
			</c:otherwise>
		</c:choose>
	</body>
</html>
