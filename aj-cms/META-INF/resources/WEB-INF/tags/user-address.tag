<%@tag pageEncoding="UTF-8" %>

<script src="${ajaxjs_ui_output}/lib/China_AREA_full.js"></script>
<style>
	.aj-form select{
	    width: 130px!important;
	}
	button{
	    line-height: 100%;
	}
</style>
<form class="aj-form" action="." method="${isCreate ? 'post' : 'put' }" style="width:800px;">
	<dl style="width: 50%;">
		<label>
			<dt>收货人</dt>
			<dd>
				<input type="text" name="name" value="${info.name}" placeholder="请输入您的真实姓名" required />
			</dd>
		</label>
	</dl>
	<dl style="width: 50%;">
		<label>
			<dt>联系电话</dt>
			<dd>
				<input type="text" name="phone" value="${info.phone}" placeholder="请输入手机或固话" required />
			</dd>
		</label>
	</dl>
	<dl>
		<dt>地区</dt>
		<dd class="area">
			<aj-china-area province-code="${info.locationProvince}" city-code="${info.locationCity}" district-code="${info.locationDistrict}">
			</aj-china-area>
			<br /> 
			<input type="text" size="70" name="address" value="${info.address}" required placeholder="请输入详细地址" />
		</dd>
	</dl>
	<dl>
		<dt>默认地址</dt>
		<dd>
			<label><input type="radio" name="isDefault" checked="checked" /> 是</label> 
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<label><input type="radio" name="isDefault" /> 否</label> 
		</dd>
	</dl>
	<dl>
		<dt>备注</dt>
		<dd>
			<input type="text" name="content" size="70" />
		</dd>
	</dl>
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