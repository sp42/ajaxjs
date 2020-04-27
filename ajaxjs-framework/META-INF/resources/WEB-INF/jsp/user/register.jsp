<%@page pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>

<tags:content bannerImg="${ctx}/images/banner.jpg">
	<br />
	<fieldset class="user">
		<legend>
			<tags:i18n zh="用户注册" eng="Register" />
		</legend>

		<tags:user type="register" />
	</fieldset>
</tags:content>