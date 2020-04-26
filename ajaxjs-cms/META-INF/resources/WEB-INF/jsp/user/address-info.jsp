<%@ page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:user-center>
	<script src="${ajaxjs_ui_output}/lib/China_AREA_full.js"></script>

	<h3 class="aj-center-title">编辑收货地址</h3> 

	<style>
		.aj-form td{
			padding:2% 0;
		}
	</style>
	<form class="aj-form" action="." method="post">
	
		<table width="100%">
			<tr>
				<td>收货人</td><td><input type="text" name="name" value="${info.name}" placeholder="请输入您的真实姓名" required="required" /></td>
				<td>联系电话</td><td><input type="text" name="phone" value="${info.phone}" placeholder="请输入手机或固话" required="required" /></td>
			</tr>
			<tr>
				<td>地区</td><td colspan="3" class="area"><aj-china-area></aj-china-area></td>
			</tr>
			<tr>
				<td></td>
				<td colspan="3">
					<input type="text" name="name" value="${info.address}" placeholder="请输入地址，注意不要再输入省市区" required size="50" />
				</td>
			</tr>
			<tr>
				<td>默认地址</td><td>
					<label><input type="radio" name="isDefault" checked="checked" /> 是</label> 
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<label><input type="radio" name="isDefault" /> 否</label> 
				</td>
				<td>备注</td><td><input type="text" name="content" /></td>
			</tr>
		</table>
	
		<dl>
			<dt></dt>
			<dd class="btns">
				<button>确定保存</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <button class="returnBtn">返回</button>
			</dd>
		</dl>
	</form>
	<script>
		new Vue({
			el : '.area'
		});
		
		ajaxjs.xhr.form('.aj-form');
	</script>
</tags:user-center>
