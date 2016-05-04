<%@page pageEncoding="UTF-8"%>	
<!-- 分类页面 -->
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/public"%>
<%@taglib prefix="UI"  		 tagdir="/WEB-INF/tags/public/UI"%>
<%@taglib prefix="c"  		 uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/bigfoot/asset/less/user.less" title="${uiName}管理" />
<body>
	<UI:adminHeader pageTitle="${uiName}管理" />
	<style>
		select {
			height:300px;
			width:200px;
			outline: none;
			margin:10px;
		}
		.label{
			display:inline-block;
			width: 170px;
			text-align:right;
		}
	</style>
	<div class="panel">
		<h4>${uiName}管理</h4>
	<br />
	<br />
	<table align="center" width="95%">

		<tr>
			<td class="holder">
				<p align="center">你可以在这里添加、修改、删除${uiName}。</p>
				<br />
				<select onchange="onSelectChange(this);" data-level="1" multiple="multiple"></select>
			</td>
		</tr>
		<tr>
			<td>
				
				<br />
				<form style="margin:0;" class="createTopNode" action="${pageContext.request.contextPath}/service/${tableName}">
					<div class="label">
						新增顶级分类：
					</div>
					<input type="text" name="name" class="" />
					<input type="hidden" name="parentId" value="-1" />
					<button class="my-btn-3"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/add.gif" /> 新建</button>
					
				</form>
				<form style="margin:0;" class="createUnderNode" action="${pageContext.request.contextPath}/service/${tableName}">
					<div class="label">
					<span id="addNewText"></span>下添加分类：
					</div>
					<input type="text" name="name" class="" />
					<input type="hidden" name="parentId" />
					<input type="hidden" name="level" />
					<button class="my-btn-3"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/add.gif" /> 新建</button>
					
				</form>
				<form style="margin:0;" class="modifly_Name" action="${pageContext.request.contextPath}/service/${tableName}">
					<label style="width:300px;">
					<div class="label">
						修改该分类名称：
					</div>
					<input type="text" class="modiflyName" name="name" />
					<input type="hidden" class="modiflyName_level" />
					<input type="hidden" class="noPassServerParentId" />
					
					<button class="my-btn-3"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/update.gif" /> 更新</button>
					<button onclick="sendDelete(this);return false;" class="my-btn-3"><img src="${pageContext.request.contextPath}/asset/bigfoot/skin/icon/delete.gif" /> 删除</button>
					</label>
				</form>
			</td>
		</tr>
	</table>
	<br />

	<br />
	<br />
	</div>
	<script>
		// 新增顶级分类
		var createTopNode = new bf_form(document.querySelector('.createTopNode'));
		createTopNode.on('afterSubmit', function callback(el, json){
			if(json.isOk){
				alert('创建成功！');
				render(1, -1);
			} else {
				alert(json.msg);
			}
		});
		
		// 在分类下添加子分类
		var createUnderNode = new bf_form(document.querySelector('.createUnderNode'));
		createUnderNode.on('afterSubmit', function callback(el, json){
			if(json.isOk){
				alert('创建成功！');
				//debugger;
				var level = el.querySelector('input[name=level]').value;
				level = Number(level);
				render(++level, el.querySelector('input[name=parentId]').value);
				el.querySelector('input[name=name]').value = '';
			} else {
				alert(json.msg);
			}
		});
		
		// 修改该分类名称
		var modifly_Name = new bf_form(document.querySelector('.modifly_Name'));
		modifly_Name.on('afterSubmit', function callback(el, json){
			if(json.isOk){
				alert(json.msg);
				var level = el.querySelector('.modiflyName_level').value;
				level = Number(level);
				render(level, el.querySelector('.noPassServerParentId').value);
				el.querySelector('input[name=name]').value = el.querySelector('.noPassServerParentId').value = '';
			} else {
				alert(json.msg);
			}
		});
		
		// 删除
		function sendDelete(btn){
			var modiflyName = document.querySelector('.modiflyName').value;
			var formEl = btn.up('form');
			if(window.confirm('确定删除该分类{0}？'.format(modiflyName))){
				XMLHttpRequest.dele(formEl.action, {}, function callback(json){
					if(json.isOk){
						var level = formEl.querySelector('.modiflyName_level').value;
						level = Number(level);
						render(level, formEl.querySelector('.noPassServerParentId').value);
						formEl.querySelector('input[name=name]').value = formEl.querySelector('.noPassServerParentId').value = '';
					}else {
						alert(json.msg);
					}
				});
			}
		}
		
		function onSelectChange(selectEl) {
			var level = selectEl.getAttribute('data-level');
			level = Number(level);
			
			var selectedOptions = selectEl.selectedOptions[0], parentId = selectedOptions.value;
			render(++level, parentId);
			
			// 送入修改框
			var action = document.querySelector('.modifly_Name').action.replace(/\/\d+\/?$/, '');
			document.querySelector('.modifly_Name').action = action + '/' + selectedOptions.value;
			document.querySelector('.modiflyName').value		 = selectedOptions.innerHTML;
			// 用于刷新节目数据用，不用传给服务端
			document.querySelector('.modiflyName_level').value   = selectEl.getAttribute('data-level');
			document.querySelector('.noPassServerParentId').value= selectEl.getAttribute('data-parentId');
			
			// 使得可以新加入子节点
			var createUnderNode = document.querySelector(".createUnderNode");
			createUnderNode.querySelector('#addNewText').innerHTML = '<b>' + parentId + '#' + selectedOptions.innerHTML + '</b>';
			createUnderNode.querySelector('input[name=parentId]').value = selectedOptions.value;
			createUnderNode.querySelector('input[name=level]').value = selectEl.getAttribute('data-level');
		}
		
		function render(level, id) {
			xhr.get('list.json', {
				filterField : 'parentId', filterValue: id, limit: 99
			}, function(json){
				var selectEl = document.querySelector('*[data-level="{0}"]'.format(level));
				
				if (!selectEl && json.result  && json.result.length) {
					var selectEl = document.createElement('select');
					selectEl.multiple = 'multiple';
					selectEl.setAttribute('data-level', level);
					selectEl.setAttribute('data-parentId', json.result[0].parentId);
					selectEl.onchange = function(e) {
						onSelectChange(e.target);
					};
					
					var holder = document.querySelector('.holder');
					holder.appendChild(selectEl);
				} else {
					selectEl && selectEl.setAttribute('data-parentId', '');
				}
				
				if(selectEl)
					selectEl.innerHTML = '';
				
				if(!json.result || !json.result.length) {
					// 如果有下下一级，清除内容，当前只能清除下一级，不能多级
					var nextLevel = Number(level);
					nextLevel++;
					var nextSelectEl = document.querySelector('*[data-level="{0}"]'.format(nextLevel));
					if (nextSelectEl)
						nextSelectEl.innerHTML = '';
					return; // 叶子结点，没有下一级
				} else {
					// 记录 parentId，这是已经创建了 select element 的情况
					selectEl.setAttribute('data-parentId', json.result[0].parentId);
					// 生成 option
					var temp = document.createDocumentFragment();
					for(var i = 0, j = json.result.length; i < j; i++) {
						var option = document.createElement('option'); // 节点
						option.value = json.result[i].id;
						option.innerHTML = json.result[i].name;
						temp.appendChild(option);
					}
					
					selectEl.appendChild(temp);
				}
				
			});
			
		}
		render(1, -1);
		
		
	</script>
</body>
</html>
