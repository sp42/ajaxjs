<%@tag pageEncoding="UTF-8" description="公共的管理界面"%>	
<%@taglib prefix="c" uri="/ajaxjs"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@attribute name="type" type="String" required="true" description="该属性选择类型"%>

<%-- 头部 --%>
<c:if test="${type == 'header'}">
	<%@attribute name="pageTitle" 	fragment="false" required="false" description="页面标题"%> 
	<%@attribute name="maxWin" 		fragment="false" required="false" type="Boolean" description="是否需要'最大化窗体'"%> 
		<header>
			<div>
				<div class="memberNav">
					<jsp:doBody />
					  
				 	<c:if test="${not empty maxWin && maxWin == true}">
				 		<a href="javascript:maxWin();" >最大化窗体</a> |
				 	</c:if>
				 	
				 	<a href="#" target="_blank">新窗口打开</a>
				</div>
				<span>${pageTitle}</span>
			</div>
		</header>
		<script>
			function maxWin() {
				var panel = document.querySelector('.panel');
				if (panel) {
					panel.style.width = '95%';
				}
			}
		</script>
</c:if>

<%-- 编辑器框架 --%>
<%@attribute name="hasCatalog" type="Boolean" required="fasle" description="是否需要分类"%>
<%@attribute name="moreBtn"  required="false" fragment="true"  description="是否需要按钮"%>
<c:if test="${type=='editor'}">
		<commonTag:adminUI type="header" maxWin="false" pageTitle="${isCreate ? '新建' : '编辑'}${uiName} ${not empty info.id ? '#': ''}${info.id}">
			<c:choose>
				<c:when test="${isCreate}">
				
				</c:when>
				<c:otherwise>
					<a href="../create.do">新建${uiName}</a> | 
				</c:otherwise>
			</c:choose>
			<c:if test="${hasCatalog || empty hasCatalog}">
				<a href="${isCreate ? '' : '../'}list/do.do?start=0&limit=9">${uiName}列表</a> |  
			</c:if>
		</commonTag:adminUI>
	
		<form class="form_style_2" action="action.do" method="${isCreate ? 'POST' : 'PUT'}">
			<!-- 传送 id 参数 -->
			<input type="hidden" name="id" value="${info.id}" />
			<jsp:doBody />
		
		<c:choose>
			<c:when test="${isCreate}">
			</c:when>
			<c:otherwise>

			</c:otherwise>
		</c:choose>
		
			<div class="row">
				<div style="text-align:center;">
					<jsp:invoke fragment="moreBtn" />
						<button class="my-btn-3" style="width:15%;">
							<img src="${pageContext.request.contextPath}/asset/images/icon/save.gif" /> ${isCreate ? '新建' : '修改'}
						</button> 
						<button class="my-btn-3" style="width:10%;" onclick="this.up('form').reset();return false;">复 位</button> 
					<c:if test="${!isCreate}">
						<button class="my-btn-3" style="width:10%;" onclick="del();return false;">
							<img src="${pageContext.request.contextPath}/asset/images/icon/delete.gif" /> 删 除
						</button> 
					</c:if>
				</div> 
			</div>
		</form>
		
		<script>
			var formConfig = {
				isCommonAfterSubmit : true
			};
			
			if(this.htmlEditor) {
				formConfig.htmlEditor_hook = htmlEditor;
			}
		</script>
	<c:choose>
		<c:when test="${isCreate}">
		<script>
			form = new bf_form(document.querySelector('form'), formConfig);
			form.on('afterSubmit', function(form, json){
				if(json.isOk){
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
				if(confirm('确定删除 \n${info.name}？'))
					XMLHttpRequest.dele('delete.do', {}, function(json) {
						if(json && json.isOk){
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
 </c:if>

<c:if test="${type=='adminList_btns'}">
	<%@attribute name="current"  type="Object" required="fasle" description="实体"%>
	<%@attribute name="viewLink" type="String" required="fasle" description="浏览外部连接"%>
	
	<a href="${viewLink}/${current.id}/info.do" target="_blank">浏览</a>
	<a href="../${current.id}/edit.do"><img src="${pageContext.request.contextPath}/asset/images/icon/update.gif" style="vertical-align: sub;" />编辑</a>
	<a href="javascript:entity.del('${current.id}', '${current.name}');"><img src="${pageContext.request.contextPath}/asset/images/icon/delete.gif" style="vertical-align: sub;" />删除</a>
</c:if>