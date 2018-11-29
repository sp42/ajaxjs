<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/less/admin.less" title="${actionName}${uiName}${info.name}" />
<body class="admin-entry-form">
	<header class="top">
		<div>
		<c:if test="${empty isCreate}">
			<a href="../">新建${uiName}</a> | 
		</c:if>
			<a href="${isCreate ? '' : '../'}list/?start=0&limit=9">${uiName}列表</a> | 
			<a href="#" target="_blank"><img width="12" src="data:image/gif;base64,R0lGODlhEAAQAIABAAAAAP///yH5BAEAAAEALAAAAAAQABAAAAImjG+gq+je3gOBWURrlvVEuWlcKE4T2Xkql6zshkLuOIO1mVj6VgAAOw==" /> 新窗口打开</a>
		</div>
		
		<fieldset>
			<legend>
				${actionName}${uiName}：<span>${not empty info.id ? '#': ''}${info.id}</span>
			</legend>
		</fieldset>
	</header>

	<form action="." method="${isCreate ? 'POST' : 'PUT'}">
		<c:if test="${!isCreate}">
			<input type="hidden" name="id" value="${info.id}" /><!-- 传送 id 参数 -->
		</c:if>
		
		<div>
			<label>
				<div class="label">用户名：</div> 
				<input placeholder="请填写${uiName}名称" size="60" required="required" name="name" value="${info.name}" type="text" class="ajaxjs-inputField" />
			</label> 
			<label> 
				<div class="label">分 类：</div>  <select name="catalog" class="ajaxjs-select" style="width: 200px;">
					<c:foreach items="${catalogMenu}" var="current">
						<c:choose>
							<c:when test="${info.catalog == current.id}">
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

		<div class="brief">
				<label>
					<div class="label">用户简介：</div> 
					<textarea rows="15" cols="20" style="width: 90%;" class="ajaxjs-inputField" name="intro">${info.intro}</textarea>
				</label>
		</div>


		<div class="buttons">
			<button class="ajaxjs-btn"><img src="${commonAssetIcon}save.gif" /> ${actionName}</button>
			<button class="ajaxjs-btn" onclick="this.up('form').reset();return false;">复 位</button>
			 
		<c:if test="${not empty isCreate}">
			<button class="ajaxjs-btn" onclick="del();return false;">
				<img src="${commonAssetIcon}delete.gif" /> 删 除
			</button>
			
			<script type="text/javascript">
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
		</c:if>
		</div>
	</form>
</body>
</html>
