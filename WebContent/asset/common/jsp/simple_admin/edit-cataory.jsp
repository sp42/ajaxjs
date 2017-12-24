<%@page import="com.ajaxjs.web.Constant"%>
<%@page pageEncoding="UTF-8"%>
<!-- ${uiName}页面 -->
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="UI" tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/less/admin.less" title="${uiName}管理" />
<body>
	<%-- 	<UI:adminHeader pageTitle="${uiName}管理" /> --%>
	<style>
select {
	height: 300px;
	width: 200px;
	outline: none;
	margin: 10px;
	border: 1px solid gray;
border-radius: 5px;
}

.label {
	display: inline-block;
	width: 250px;
	text-align: right;
}
.holder{
	margin:3%;
	text-align: center;
}
form{
	padding:1% 2%;
}
</style>
	<div class="panel">
		<div class="holder">
			分类管理:你可以在这里添加、修改、删除分类。
			 <br /> 
			 <br /> 
			<select onchange="onSelectChange(this);" data-level="1" multiple="multiple"></select>
		</div>
		
		<div style="margin:0 auto;width:960px;border:1px solid #e3e3e3;">
					<form style="margin: 0;background-color:#e3e3e3" class="createTopNode" action="create.do"
						method="post">
						<div class="label">新增顶级分类：</div>
						<input type="text" name="name" class="ajaxjs-inputField" /> <input type="hidden"
							name="parentId" value="-1" />
						<button class="ajaxjs-btn">
							<img src="${pageContext.request.contextPath}/<%=Constant.commonIcon%>add.gif" /> 新建分类
						</button>

					</form>

					<form style="margin: 0;background-color: #f1f1f1;" class="createUnderNode" action="create.do"
						method="post">
						<div class="label">
							<span id="addNewText"></span>下添加分类：
						</div>
						<input type="text" name="name" class="ajaxjs-inputField" /> <input type="hidden"
							name="parentId" />  <input type="hidden" name="level" /> 
						<button class="ajaxjs-btn">
							<img src="${pageContext.request.contextPath}/<%=Constant.commonIcon%>add.gif" /> 新建分类
						</button>
					</form>

					<form style="margin: 0;background-color:#e3e3e3;" class="modifly_Name" action="." method="put">
						<label style="width: 300px;">
							<div class="label">修改该分类名称：</div> <input type="text"
							class="modiflyName ajaxjs-inputField" name="name" />
							<input type="hidden" name="id" /> 
							<input type="hidden" class="modiflyName_level" /> 
							<input type="hidden" class="noPassServerParentId" />

							<button class="ajaxjs-btn">
								<img src="${pageContext.request.contextPath}/<%=Constant.commonIcon%>update.gif" /> 更新名称
							</button>
							<button class="ajaxjs-btn" onclick="sendDelete(this);return false;">
								<img src="${pageContext.request.contextPath}/<%=Constant.commonIcon%>delete.gif" /> 删除分类
							</button>
						</label>
					</form>
		</div>
		
	</div>
	</div>
	<script>
		var stack = [document.querySelector('select[data-level="1"]')];
		
		// 新增顶级分类
		ajaxjs.xhr.form(".createTopNode", function(json) {
			if (json.isOk) {
				alert('创建成功！');
				render(1, -1);
			} else {
				alert(json.msg);
			}
		});
		

		
		// 在分类下添加子分类
		ajaxjs.xhr.form(".createUnderNode", function(json) {
			 if(json.isOk){
			 	alert('创建成功！');
			 	var el = document.querySelector(".createUnderNode");
			 	var level = el.querySelector('input[name=level]').value;
			 	level = Number(level);
			 	render(++level, el.querySelector('input[name=parentId]').value);
			 	el.querySelector('input[name=name]').value = '';
			 } else {
			 	alert(json.msg);
			 }
		});
		
		// 修改该分类名称
		ajaxjs.xhr.form(".modifly_Name", function(json) {
			if(json.isOk){
				alert(json.msg);
				var el = document.querySelector(".modifly_Name");
				var level = el.querySelector('.modiflyName_level').value;
				level = Number(level);
				render(level, el.querySelector('.noPassServerParentId').value);
				el.querySelector('input[name=name]').value = el.querySelector('.noPassServerParentId').value = '';
			} else {
				alert(json.msg);
			}
		});

		// 删除
		function sendDelete(btn) {
			var modiflyName = document.querySelector('.modiflyName').value;
			var formEl = btn.up('form');
			
			if (window.confirm('确定删除该分类{0}？'.replace('{0}', modiflyName))) {
				debugger;
				ajaxjs.xhr.dele(formEl.action,
				function callback(json) {
					if (json.isOk) {
						var level = formEl.querySelector('.modiflyName_level').value;
						level = Number(level);
						render(level, formEl.querySelector('.noPassServerParentId').value);
						formEl.querySelector('input[name=name]').value = formEl.querySelector('.noPassServerParentId').value = '';
					} else {
						alert(json.msg);
					}
				});
			}
		}

		function onSelectChange(selectEl) {

			var current;
			for(var i = 0, j = stack.length; i < j; i++) {
				if(stack[i] == selectEl) {
					current = i;
					break;
				}
			}
			
			for(var i =current+1; i < j; i++) {
				stack.pop().die();
			}
			
			var level = selectEl.getAttribute('data-level');
			level = Number(level);

			var selectedOptions = selectEl.selectedOptions[0], parentId = selectedOptions.value;
			render(++level, parentId);

			// 送入修改框
			var action = document.querySelector('.modifly_Name').action.replace(/\/\d+\/?$/, '');
			document.querySelector('.modifly_Name').action = '' + selectedOptions.value + '/update.do';
			document.querySelector('.modifly_Name').querySelector('input[name=id]').value = selectedOptions.value;// 送入 id 以便 bean 组装数据
			document.querySelector('.modiflyName').value = selectedOptions.innerHTML;
			// 用于刷新节目数据用，不用传给服务端
			document.querySelector('.modiflyName_level').value = selectEl.getAttribute('data-level');
			document.querySelector('.noPassServerParentId').value = selectEl.getAttribute('data-parentId');

			// 使得可以新加入子节点
			var createUnderNode = document.querySelector(".createUnderNode");
			createUnderNode.querySelector('#addNewText').innerHTML = '<b>' + parentId + '#' + selectedOptions.innerHTML + '</b>';
			createUnderNode.querySelector('input[name=parentId]').value = selectedOptions.value;
			createUnderNode.querySelector('input[name=level]').value = selectEl.getAttribute('data-level');
		}

		function render(level, id) {
			ajaxjs.xhr.get('list/list.do', function(json) {
				var selectEl = document.querySelector('*[data-level="{0}"]'.replace('{0}', level));

				if (!selectEl && json.result && json.result.length) {
					var selectEl = document.createElement('select');
					selectEl.multiple = 'multiple';
					selectEl.setAttribute('data-level', level);
					selectEl.setAttribute('data-parentId', json.result[0].parentId);
					selectEl.onchange = function(e) {
						onSelectChange(e.target);
					};
					
					stack.push(selectEl);

					var holder = document.querySelector('.holder');
					holder.appendChild(selectEl);
				} else {
					selectEl && selectEl.setAttribute('data-parentId', '');
				}

				if (selectEl)
					selectEl.innerHTML = '';

				if (!json.result || !json.result.length) {
					// 如果有下下一级，清除内容，当前只能清除下一级，不能多级
					var nextLevel = Number(level);
					nextLevel++;
					var nextSelectEl = document.querySelector('*[data-level="{0}"]'.replace('{0}', nextLevel));
					if (nextSelectEl)
						nextSelectEl.innerHTML = '';
					return; // 叶子结点，没有下一级
				} else {
					// 记录 parentId，这是已经创建了 select element 的情况
					selectEl.setAttribute('data-parentId', json.result[0].parentId);
					// 生成 option
					var temp = document.createDocumentFragment();
					for (var i = 0, j = json.result.length; i < j; i++) {
						var option = document.createElement('option'); // 节点
						option.value = json.result[i].id;
						option.innerHTML = json.result[i].name;
						temp.appendChild(option);
					}

					selectEl.appendChild(temp);
				}

			}, {
				filterField : 'parentId',
				filterValue : id
			});

		}

		render(1, -1);
	</script>
</body>
</html>
