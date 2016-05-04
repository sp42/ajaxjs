<%@page pageEncoding="UTF-8"%>	
<!-- 简单的 crud 页面 -->
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/public"%>
<%@taglib prefix="UI"  		 tagdir="/WEB-INF/tags/public/UI"%>
<%@taglib prefix="c"  		 uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/bigfoot/asset/less/user.less" title="${uiName}管理" />
<body>
	<UI:adminHeader pageTitle="${uiName}管理" />
	<div class="panel">
		<h4>${uiName}管理</h4>
	<br />
	<br />
	<table align="center" width="600">
		<tr>
			<td>
				请选择${uiName}：<br /><br />
				<select name="catalog" onchange="applyData(this);" multiple="multiple" style="height:300px;width:200px;">
				<c:foreach items="${PageResult.rows}" var="current">
					<option value="${current.id}">${current.name}</option>
				</c:foreach>
				</select>
			</td> 
			<td valign="top">
				<form class="aj">
					&nbsp;&nbsp;&nbsp;&nbsp;${uiName} id ： &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span class="id"></span>
					<label>
						${uiName}名称：<input type="text" name="name" />
						<input type="hidden" name="id" value="" class="currentId" />
					</label>
					<div align="center">
						<button onclick="update();return false;" class="my-btn-3"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/update.gif" /> 更新</button>
						<button onclick="del();return false;" class="my-btn-3"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/delete.gif" /> 删除</button>
					</div>
				</form>
				<form class="aj">
					<label>
						新建${uiName}：<input type="text" name="name" class="createName" />
					</label>
					<div align="center">
						<button onclick="create();return false;" class="my-btn-3"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/add.gif" /> 新建</button>
					</div>
				</form> 
			</td>
		</tr>
	</table>
	<br />
	<br />
	<center>
		<p>你可以在这里添加、修改、删除${uiName}数据。</p>
	</center>
	<br />
	<br />
	</div>
	<script>
		var baseUrl = '${pageContext.request.contextPath}';
		// 数据绑定
		function applyData(el){
			var id = document.querySelector('.id');
			var idField = document.querySelector('input.currentId');
			idField.value = id.innerHTML = el.selectedOptions[0].value;
			
			var name = document.querySelector('input[name=name]');
			name.value = el.selectedOptions[0].innerHTML;
		}
		
		function callback(json){
			alert(json.msg);
			if(json.isOk){
				location.reload();
			}
		}
		
		// 创建
		function create(){
			var nameField = document.querySelector('input.createName').value;
			if (nameField){
				XMLHttpRequest.post('../${tableName}', { // 自动上一级似乎不行
					name : nameField
					//belongsto : '${tableName}'.toLowerCase()
				}, callback);
			}
		}
		
		// 修改
		function update(){
			var idField = document.querySelector('input.currentId');
			if (idField.value){							
				XMLHttpRequest.post(idField.value, {
					name : document.querySelector('input[name=name]').value,
				}, callback);
			}
		}
		
		//  删除
		function del(){
			var idField = document.querySelector('input[name=id]');
			if (idField.value){
				if(confirm('确定删除该${uiName}？')){
					XMLHttpRequest.dele(idField.value, {
		
					}, callback);
				}
			}						
		}
	</script>
</body>
</html>
