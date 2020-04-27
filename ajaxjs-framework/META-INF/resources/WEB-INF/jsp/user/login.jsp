<%@page pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>

<tags:content bannerImg="${ctx}/images/banner.jpg">
	<br />
	<fieldset class="user">
		<legend>
			<tags:i18n zh="用户登录" eng="User Login" />
		</legend>

		<tags:user type="login" />
	</fieldset>
</tags:content>