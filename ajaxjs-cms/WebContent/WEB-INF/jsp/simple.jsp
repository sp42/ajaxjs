<%@page pageEncoding="UTF-8"%>	
<!-- ${uiName}页面 -->
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="UI"  		 tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib prefix="c"  		 uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/less/admin.less" title="${uiName}管理" />
<body>
<%-- 	<UI:adminHeader pageTitle="${uiName}管理" /> --%>
	<style>
		select {
			height:300px;
			width:200px;
			outline: none;
			margin:10px;
		}
		.label{
			display:inline-block;
			width: 150px;
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
				<p align="center">你可以在这里添加、修改、删除${uiName}。
				<br />
				<select onchange="onSelectChange(this);" data-level="1" multiple="multiple"></select></p>
			</td>
		</tr>
		<tr>
			<td>
				
				<br />
				<form style="margin:0;" class="createTopNode" action="create.do" method="post">
					<div class="label">
						新增${uiName}：
					</div>
					<input type="text" name="name" class="" />
					<button class="my-btn-3"><img src="${pageContext.request.contextPath}/asset/images/icon/add.gif" /> 新建${uiName}</button>
				</form>
			
				
				<form style="margin:0;" class="modifly_Name" action="." method="put">
					<label style="width:300px;">
						<div class="label">
							修改${uiName}名称：
						</div>
						<input type="text" class="modiflyName" name="name" />
						<input type="hidden" class="modiflyName_level" />
						<input type="hidden" class="noPassServerParentId" />
						
						<button class="my-btn-3"><img src="${pageContext.request.contextPath}/asset/images/icon/update.gif" /> 更新名称</button>
						<button onclick="sendDelete(this);return false;" class="my-btn-3"><img src="${pageContext.request.contextPath}/asset/images/icon/delete.gif" /> 删除${uiName}</button>
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
		// 新增顶级${uiName}
		var createTopNode = new bf_form(document.querySelector('.createTopNode'));
		createTopNode.on('afterSubmit', function callback(el, json){
			if(json.isOk){
				alert('创建成功！');
				render(1, -1);
			} else {
				alert(json.msg);
			}
		});
		
		// 修改该${uiName}名称
		var modifly_Name = new bf_form(document.querySelector('.modifly_Name'));
		modifly_Name.on('afterSubmit', function callback(el, json){
			if(json.isOk){
				alert(json.msg);
				location.reload();
				var level = el.querySelector('.modiflyName_level').value;
				level = Number(level);
				//render(level, el.querySelector('.noPassServerParentId').value);
				el.querySelector('input[name=name]').value = el.querySelector('.noPassServerParentId').value = '';
			} else {
				alert(json.msg);
			}
		});
		
		// 删除
		function sendDelete(btn){
			var modiflyName = document.querySelector('.modiflyName').value;
			var formEl = btn.up('form');
			if(window.confirm('确定删除该${uiName}：{0}？'.format(modiflyName))){
				XMLHttpRequest.dele(formEl.action, {}, function callback(json){
					if(json.isOk){
						var level = formEl.querySelector('.modiflyName_level').value;
						level = Number(level);
						alert('删除成功！');
						location.reload();
						//render(level, formEl.querySelector('.noPassServerParentId').value);
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
			
			// 送入修改框
			var action = document.querySelector('.modifly_Name').action.replace(/\/\d+\/?$/, '');
			document.querySelector('.modifly_Name').action = '' + selectedOptions.value + '/update.do';
			var arr = selectedOptions.innerHTML.split(' ');
			arr.shift();
			
			document.querySelector('.modiflyName').value = arr.join('');
		}
		
		function render(level, id) {
			xhr.get('list/list.do', {
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
						option.innerHTML = '#' + option.value + '  ' + json.result[i].name;
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
