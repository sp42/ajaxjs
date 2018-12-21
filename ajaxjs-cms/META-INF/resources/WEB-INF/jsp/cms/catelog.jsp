<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/jsp/common/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
		<jsp:param name="title" value="${uiName}管理" />
	</jsp:include>
</head>

<body class="pid">
	<div class="vue">
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header>
			<template slot="title">${uiName}一览：<span>你可以在这里添加、修改、删除${uiName}</span></template>
		</ajaxjs-admin-header>
	</div>
	
	<div class="panel">
		<form class="createTopNode" action="." method="post">
			<input type="hidden" name="pid" value="-1" />
			
			<label>新增顶级${uiName}：<input type="text" name="name" class="ajaxjs-inputField" required="required" /> </label>
			<button class="ajaxjs-btn">
				<img src="${commonAssetIcon}/add.gif" /> 新建${uiName}
			</button>
		</form>
		<br />
		
		<select multiple="multiple" style="width: 100%; margin: 2% 0;min-height:430px;"></select>
		<br />
		
		<form style="background-color: #e3e3e3;" class="modifly_Name" action="." method="put">
			名称：
			<input type="text" class="modiflyName ajaxjs-inputField" name="name" required="required" /> 
			<input type="hidden" name="id" /> 

			<button class="ajaxjs-btn">
				<img src="${commonAssetIcon}/update.gif" /> 更新名称
			</button>
			<button class="ajaxjs-btn" onclick="sendDelete(this);return false;">
				<img src="${commonAssetIcon}/delete.gif" /> 删除${uiName}
			</button>
		</form>
		
		<form class="createUnderNode" action="." method="post">
			<span id="addNewText"></span>下添加${uiName}：
			<input type="text" name="name" class="ajaxjs-inputField" required="required" /> 
			<input type="hidden" name="pid" />
			<button class="ajaxjs-btn">
				<img src="${commonAssetIcon}/add.gif" /> 新建${uiName}
			</button>
		</form>
	</div>

	<script>
		new Vue({el : ".vue"});
		
		// 新增顶级${uiName}
		ajaxjs.xhr.form(".createTopNode", function(json) {
			if (json.isOk) {
				aj.alert.show('创建成功！');
				render();
				aj(".createTopNode").$('input[name=name]').value = '';
			} else {
				aj.alert.show(json.msg);
			}
		});

		// 在${uiName}下添加子${uiName}
		ajaxjs.xhr.form(".createUnderNode", 
			function(json) {
				if (json.isOk) {
					aj.alert.show('创建成功！');
					render();
					aj(".createUnderNode").$('input[name=name]').value = '';
				} else {
					aj.alert.show(json.msg);
				}
		});
		// 修改该${uiName}名称
		ajaxjs.xhr.form(".modifly_Name", function(json) {
			if (json.isOk) {
				aj.alert.show(json.msg);
				render();
				aj(".modifly_Name input[name=name]").value =  '';
			} else {
				aj.alert.show(json.msg);
			}
		});

		
		// 删除
		function sendDelete(btn) {
			var modiflyName = aj('.modiflyName').value;
			var formEl = btn.up('form');

			if (window.confirm('确定删除该${uiName}{0}？'.replace('{0}', modiflyName))) {
				ajaxjs.xhr.dele(formEl.action,
					function callback(json) {
						if (json.isOk) {
							aj.alert.show(json.msg);
							render();
						} else {
							aj.alert.show(json.msg);
						}
				});
			}
		}
		
		function clearName(str) {
			return str.replace(/&nbsp;|└─/g, '');
		}

		function onSelectChange(e) {
			var selectEl = e.target;
			var option = selectEl.selectedOptions[0], id = option.value, pid = option.dataset['pid'], name = clearName(option.innerHTML);

			// 送入修改框
			var action = aj('.modifly_Name').action.replace(/\/\d+\/?$/, '');
			aj('.modifly_Name').action = id + '/';
			aj('.modifly_Name').$('input[name=id]').value = id;// 送入 id 以便 bean 组装数据
			aj('.modiflyName').value = name;
			
			// 使得可以新加入子节点
			var createUnderNode = aj(".createUnderNode");
			createUnderNode.$('#addNewText').innerHTML = '<b>' + id + '#' + name + '</b>';
			createUnderNode.$('input[name=pid]').value = id;
		}

		var selectUI = new ajaxjs.tree.selectUI();
		
		function render() {
			selectUI.initData();
			
			var select = aj('select');
			select.innerHTML = '';
			select.onchange = onSelectChange;
			
			ajaxjs.xhr.get('list/?limit=99', function(json) {
				if(!json.isOk) {
					aj.alert.show(json.msg); // 有错误就提示
					return;
				}

				selectUI.renderer(json.result, select);
			});
		}
		
		render();
	</script>
</body>
</html>
