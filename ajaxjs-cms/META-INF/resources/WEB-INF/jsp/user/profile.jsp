<%@ page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
<tags:user-center>
	<h3 class="aj-center-title">个人信息</h3>
	
	<div style="text-align: right;">
		<a href="avatar/">修改头像</a> |
		<a href="${ctx}/user/address/list/">设置支付信息</a> |
		<a href="../address/list/">管理收货地址</a>
	</div>
	
	<br />
	<p class="aj-note" style="text-align:left;">
		欲修改手机号码、电子邮件信息请移玉步至“<a href="../account/">帐号管理</a>”。
		<br />
		为提供更好的服务，请完善以上信息。我们保证不会外泄个人私隐信息。
	</p>
	
	<tags:user-center-pages type="profile" />
</tags:user-center>
