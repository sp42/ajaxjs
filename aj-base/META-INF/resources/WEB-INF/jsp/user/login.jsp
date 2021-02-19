<%@page pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<%@taglib tagdir="/WEB-INF/tags/user" prefix="user-tag"%>

<tags:content bannerImg="${ctx}/images/banner.jpg">
	<br />
	<fieldset class="aj-fieldset" style="width:500px; margin:3% auto;">
		<legend style="width: initial; border-bottom:0;">
			<tags:i18n zh="用户登录" eng="User Login" />
		</legend>

		<user-tag:login />
	</fieldset>
</tags:content>