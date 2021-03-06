<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/jsp/common/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
		<jsp:param name="title" value="${uiName}管理" />
	</jsp:include>
</head>
<body class="pid">	
	<div class="panel">
		<!-- 后台头部导航 -->
		<ajaxjs-admin-header>
			<template slot="title">${uiName}一览：<span>你可以在这里添加、修改、删除${uiName}</template>
		</ajaxjs-admin-header>

		<form class="createTopNode" action="." method="post">
			<input type="hidden" name="pid" value="-1" />
			
			<label>新增顶级${uiName}：
			<input type="text" name="name" class="ajaxjs-inputField" required="required" /> </label>
			&nbsp;&nbsp;
			<label> 描述： <input type="text" name="content" class="ajaxjs-inputField" style="width: 300px;" />  </label>
			<button class="ajaxjs-btn">
				<img src="${commonAssetIcon}/add.gif" /> 新建${uiName}
			</button>
		</form>
		<br />
		<table class="ajaxjs-borderTable">
			<thead>
				<tr>
					<th>id</th>
					<th>名称</th>
					<th>描述</th>
					<th>创建日期</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
	</div>
	
	<script>
		// 新增顶级节点
		ajaxjs.xhr.form(".createTopNode", function(json) {
			if (json.isOk) {
				aj.alert.show('创建成功！');
				render();
				aj(".createTopNode").$('input[name=name]').value = '';
			} else {
				alert(json.msg);
			}
		});
		
	
		var tdHtml = '<td class="id">[:=id:]</td><td class="name">[:=nameTd:]</td> <td class="content">[:=content||\'\':]</td><td class="createDate">[:=createDate:]</td>\
		  <td class="action"><a href="#" onclick="showCreate(this);"><img src="${commonAssetIcon}/add.gif" /> 新 建</a> | <a href="#" onclick="showUpdate(this);"><img src="${commonAssetIcon}/update.gif" /> 修 改</a> | <a href="#" onclick="showDelete(this);"><img src="${commonAssetIcon}/delete.gif" /> 删 除</a> </td>';
	</script>
	<script src="${commonJsp}/tools/pid.js"></script>
</body>
</html>
