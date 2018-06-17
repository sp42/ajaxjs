<%@page pageEncoding="UTF-8"%>
<!-- 简单的 crud 页面 -->
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="UI" tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/common/less/admin.less" title="${uiName}管理" />
<body>
	<div class="panel">
		<h4>${uiName}管理</h4>
		<table align="center" width="800">
			<tr>
				<td>请选择${uiName}：<br /> <br /> <select name="catalog"
					onchange="applyData(this);" multiple="multiple"
					style="height: 300px; width: 200px;">
						<c:foreach items="${PageResult.rows}" var="current">
							<option value="${current.id}">${current.name}</option>
						</c:foreach>
				</select>
				</td>
				<td width="20"></td>
				<td valign="top">
					<form>
						${uiName} id ： <span class="id"></span> <br /> <label>
							${uiName}名称：<input type="text" name="name"
							class="ajaxjs-inputField" /> <input type="hidden" name="id"
							value="" class="currentId" />
						</label>


						<button onclick="update();return false;" class="ajaxjs-btn">
							<img
								src="${pageContext.request.contextPath}/asset/common/images/icon/update.gif" />
							更新
						</button>
						<button onclick="del();return false;" class="ajaxjs-btn">
							<img
								src="${pageContext.request.contextPath}/asset/common/images/icon/delete.gif" />
							删除
						</button>
					</form>
					<form>
						<label> 新建${uiName}：<input type="text" name="name"
							class="createName ajaxjs-inputField" />
						</label>

						<button onclick="create();return false;" class="ajaxjs-btn">
							<img
								src="${pageContext.request.contextPath}/asset/common/images/icon/add.gif" />
							新建
						</button>
					</form>
					<p>你可以在这里添加、修改、删除${uiName}数据。</p>
				</td>
			</tr>
		</table>

	</div>
	<script>
		var baseUrl = '${pageContext.request.contextPath}';
		// 数据绑定
		function applyData(el) {
			var id = document.querySelector('.id');
			var idField = document.querySelector('input.currentId');
			idField.value = id.innerHTML = el.selectedOptions[0].value;

			var name = document.querySelector('input[name=name]');
			name.value = el.selectedOptions[0].innerHTML;
		}

		function callback(json) {
			alert(json.msg);
			if (json.isOk) {
				location.reload();
			}
		}

		// 创建
		function create() {
			var nameField = document.querySelector('input.createName').value;
			if (nameField) {
				XMLHttpRequest.post('create.do', { // 自动上一级似乎不行
					name : nameField
				//belongsto : '${tableName}'.toLowerCase()
				}, callback);
			}
		}

		// 修改
		function update() {
			var idField = document.querySelector('input.currentId');
			if (idField.value) {
				XMLHttpRequest.put(idField.value + '/update.do', {
					name : document.querySelector('input[name=name]').value,
				}, callback);
			}
		}

		//  删除
		function del() {
			var idField = document.querySelector('input[name=id]');
			if (idField.value) {
				if (confirm('确定删除该${uiName}？')) {
					XMLHttpRequest.dele(idField.value + '/delete.do', {

					}, callback);
				}
			}
		}
	</script>
</body>
</html>
