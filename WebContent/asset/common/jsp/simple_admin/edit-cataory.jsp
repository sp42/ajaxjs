<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/asset/common/jsp/head.jsp">
			<jsp:param name="lessFile" value="/asset/ajaxjs-ui/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
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
				width:250px;
				text-align: right;
			}
			
			.holder {
				margin: 3%;
				text-align: center;
			}
			h3{
				text-align: center;
			}
			form {
				padding: 1% 2%;
			}
		</style>
	</head>
<body>
	<header class="top">
		<div class="right">
			<a href="#" target="_blank">新窗口打开</a>
		</div>
		<h3>${uiName}管理</h3>
	</header>
		
	<div class="panel">
		<div class="holder" style="width:70%;margin:0 auto;overflow:hidden;">
			${uiName}管理:你可以在这里添加、修改、删除${uiName}。 <br /> <br /> 
			<select multiple="multiple" style="float:left;width:35%;"></select>
			<form style="width:50%;float:right;margin: 0; background-color: #e3e3e3;" class="modifly_Name" action="." method="put">
				<label style="width: 300px;">
					<div class="label">名称：</div> 
					<input type="text" class="modiflyName ajaxjs-inputField" name="name" /> 
					<input type="hidden" name="id" /> 

				</label>
				<button class="ajaxjs-btn">
					<img src="${commonAssetIcon}update.gif" /> 更新名称
				</button>
				<button class="ajaxjs-btn" onclick="sendDelete(this);return false;">
					<img src="${commonAssetIcon}delete.gif" /> 删除${uiName}
				</button>
			</form>
		</div>

		<div style="margin: 0 auto; width: 69%; border: 1px solid #e3e3e3;">
			<form style="margin: 0; background-color: #e3e3e3" class="createTopNode" action="create.do" method="post">
				<div class="label">新增顶级${uiName}：</div>
				<input type="text" name="name" class="ajaxjs-inputField" /> 
				<input type="hidden" name="parentId" value="-1" />
				<button class="ajaxjs-btn">
					<img src="${commonAssetIcon}add.gif" /> 新建${uiName}
				</button>
			</form>

			<form style="margin: 0; background-color: #f1f1f1;" class="createUnderNode" action="create.do" method="post">
				<div class="label" >
					<span id="addNewText"></span>下添加${uiName}：
				</div>
				<input type="text" name="name" class="ajaxjs-inputField" /> 
				<input type="hidden" name="parentId" />
				<button class="ajaxjs-btn">
					<img src="${commonAssetIcon}add.gif" /> 新建${uiName}
				</button>
			</form>
		</div>

	</div>

	<script>
		// 新增顶级${uiName}
		ajaxjs.xhr.form(".createTopNode", function(json) {
			if (json.isOk) {
				alert('创建成功！');
				render();
				document.querySelector(".createTopNode").querySelector('input[name=name]').value = '';
			} else {
				alert(json.msg);
			}
		});

		// 在${uiName}下添加子${uiName}
		ajaxjs.xhr.form(".createUnderNode", 
			function(json) {
				if (json.isOk) {
					alert('创建成功！');
					render();
					document.querySelector(".createUnderNode").querySelector('input[name=name]').value = '';
				} else {
					alert(json.msg);
				}
		});

		// 修改该${uiName}名称
		ajaxjs.xhr.form(".modifly_Name", function(json) {
			if (json.isOk) {
				alert(json.msg);
				render();
				document.querySelector(".modifly_Name input[name=name]").value =  '';
			} else {
				alert(json.msg);
			}
		});

		// 删除
		function sendDelete(btn) {
			var modiflyName = document.querySelector('.modiflyName').value;
			var formEl = btn.up('form');

			if (window.confirm('确定删除该${uiName}{0}？'.replace('{0}', modiflyName))) {
				ajaxjs.xhr.dele(formEl.action,
					function callback(json) {
						if (json.isOk) {
							render();
						} else {
							alert(json.msg);
						}
				});
			}
		}
		
		function clearName(str) {
			return str.replace(/&nbsp;|└─/g, '');
		}

		function onSelectChange(e) {
			var selectEl = e.target;
			var option = selectEl.selectedOptions[0], id = option.value, parentId = option.dataset['parentId'], name = clearName(option.innerHTML);

			// 送入修改框
			var action = document.querySelector('.modifly_Name').action.replace(/\/\d+\/?$/, '');
			document.querySelector('.modifly_Name').action = id + '/update.do';
			document.querySelector('.modifly_Name').querySelector('input[name=id]').value = id;// 送入 id 以便 bean 组装数据
			document.querySelector('.modiflyName').value = name;
			
			// 使得可以新加入子节点
			var createUnderNode = document.querySelector(".createUnderNode");
			createUnderNode.querySelector('#addNewText').innerHTML = '<b>' + id + '#' + name + '</b>';
			createUnderNode.querySelector('input[name=parentId]').value = id;
		}

		var stack = [];

		var map = {
			a : 1,
			b : 2,
			c : {
				children : [ {
					d : 3
				} ]
			}
		};

		function findParent(map, id) {
			for ( var i in map) {
				if (i == id)
					return map[i];
				var c = map[i].children;
				if (c) {
					for (var q = 0, p = c.length; q < p; q++) {
						var result = findParent(c[q], id);
						if (result != null)
							return result;
					}

				}
			}

			return null;
		}
		
		var stack = [];
		function output(map, cb) {
			stack.push(map);
			for ( var i in map) {
				map[i].level = stack.length;
				cb(map[i], i);
				
				var c = map[i].children;
				if (c) {
					for (var q = 0, p = c.length; q < p; q++) 
						output(c[q], cb);
				}
			}
			stack.pop();
		}
		
		//findParent(map, 'd')
		var m = {};

		function render() {
			var select = document.querySelector('select');
			select.innerHTML = '';
			select.onchange = onSelectChange;
			
			ajaxjs.xhr.get('list/list.do?limit=99', function(json) {
				// 生成 option
				var temp = document.createDocumentFragment();
				
				for (var i = 0, j = json.result.length; i < j; i++) {
	
					var n = json.result[i];
	
					var parentNode = findParent(m, n.parentId);
					if (parentNode == null) {
						m[n.id] = 	{
							name : n.name,
							parentId : n.parentId
						};				
					} else {
						var obj = {};
						obj[n.id] = {
							name : n.name,
							parentId : n.parentId
						};
						if (!parentNode.children)
							parentNode.children = [];
		
						parentNode.children.push(obj);
					}
	
					
				}
	
				output(m, function(node, nodeId){
					var option = document.createElement('option'); // 节点
					option.value = nodeId;
					option.dataset['parentId'] = node.parentId;
					//option.style= "padding-left:" + (node.level - 1) +"rem;";
					option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
					temp.appendChild(option);
					//console.log(node);
				});
				console.log(m)
				select.appendChild(temp)
			});
		}
		
		render();
	</script>
</body>
</html>
