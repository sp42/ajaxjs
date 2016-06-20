<%@tag pageEncoding="UTF-8" description="编辑器框架"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="commonUI"  tagdir="/WEB-INF/tags/common/UI"%>

<%@attribute name="type" type="String" required="true" description="该属性选择类型"%>
<%@attribute name="hasCatalog" type="Boolean" required="fasle" description="是否需要分类"%>

<c:if test="${type=='main'}">
	<!DOCTYPE html>
	<html>
		<commonTag:head lessFile="/asset/bigfoot/asset/less/pages.less" />
	<body>
		<commonUI:adminHeader maxWin="false" pageTitle="${isCreate ? '新建' : '编辑'}${uiName}►${not empty info.id ? '#': ''}${info.id}">
			<c:choose>
				<c:when test="${isCreate}">
				</c:when>
				<c:otherwise>
					<a href="new">新建${uiName}</a> | 
				</c:otherwise>
			</c:choose>
			<c:if test="${hasCatalog || empty hasCatalog}">
				<a href="edit">${uiName}列表</a> |  
			</c:if>
		</commonUI:adminHeader>
	
		<form class="form_style_2" action="${isCreate ? '../'.concat(tableName) : id}" method="POST">
			<jsp:doBody />
		<c:choose>
			<c:when test="${isCreate}">
			</c:when>
			<c:otherwise>
			<div class="row">
				<div>
					<label>
						<div class="label">创建日期：</div>
					</label> 
						<commonUI:calendar>
							<input type="text" value="${viewUtils.formatDateShorter(info.createDate)}" class="my-inputField" />
						</commonUI:calendar>
					<label><div class="label">修改日期：</div>${viewUtils.formatDateShorter(info.updateDate)}</label>
				</div>
	<%-- 				<input type="hidden" value="${info.id}" /> --%>
			</div>
			</c:otherwise>
		</c:choose>
			<div class="row">
				<div style="text-align:center;">
					<button class="my-btn-3" style="width:15%;">
						<img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/save.gif" /> 提 交</button> 
					<button class="my-btn-3" style="width:10%;" onclick="this.up('form').reset();return false;">复 位</button> 
					<c:if test="${!isCreate}">
					<button class="my-btn-3" style="width:10%;" onclick="this.up('form').reset();return false;">删 除</button> 
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
			form.action = '../${tableName}';
			form.on('afterSubmit', function(form, json){
				if(json.isOk && json.newlyId){
					location.assign(json.newlyId);
				}
			});
		</script>
		</c:when>
		<c:otherwise>
		<script>
			new bf_form(document.querySelector('form'), formConfig);
		</script>
		</c:otherwise>
	</c:choose>
	</body>
	</html>
 </c:if>

<c:if test="${type=='adminList_btns'}">
	<%@attribute name="current" type="Object" required="fasle" description="实体"%>
	<a href="${current.url}" target="_blank">浏览</a>
	<a href="${current.id}"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/update.gif" style="vertical-align: sub;" />编辑</a>
	<a href="javascript:entity.del('${current.id}', '${current.name}');"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/delete.gif" style="vertical-align: sub;" />删除</a>
</c:if>
