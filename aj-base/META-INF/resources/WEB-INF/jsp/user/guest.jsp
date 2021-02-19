<%@page pageEncoding="UTF-8"%>
<fieldset class="aj-fieldset onNoLogin" style="margin: 5% auto; max-width: 500px;">
	<legend>尚未登录，没有权限操作 </legend>
	<p>
		抱歉，您尚未登录，无法进行下一步操作。<br />请先<a href="${ctx}/user/login/">【登录】</a>，五秒钟之后自动跳转。
	</p>
	<script>
		setTimeout(() =>location.assign("${ctx}/user/login/"), 5000);
	</script>
</fieldset>