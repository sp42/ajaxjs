<%@tag pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<%@attribute name="isAdminLogin" required="false" type="Boolean" description="是否后台的登录"%>
<form class="aj-form" action="${ctx}/user/login/" method="POST" data-msg-newline="true">
	<dl>
		<label>
			<dt>
				<tags:i18n zh="用户帐号" eng="User Account" />
			</dt>
			<dd>
				<input type="text" name="userID" placeholder="请输入${userID}" required pattern="^[a-zA-Z0-9_-]{4,20}$" />
			</dd>
		</label>
	</dl>
	<dl>
		<label>
			<dt>
				<tags:i18n zh="登录密码" eng="Password" />
			</dt>
			<dd>
				<input type="password" name="password" placeholder="<tags:i18n zh="6~10位数字或英文字母" eng="6 to 10 digits or English letters" />"
					required pattern="[0-9A-Za-z]{6,10}" title="请输入6~10位数字或英文密码" />
			</dd>
		</label>
	</dl>
	<dl>
		<dt></dt>
		<dd style="font-size: .8rem; width: 40%; text-align: left;">
			<button>
				<tags:i18n zh="登录" eng="Login" />
			</button>
			<br />

			<c:if test="${aj_allConfig.user.login.thridOauth}">
				<br />
				<aj-oauth-login client-id="360568732"
					redirect-uri="http://www.palate-movement.com/user/oauth/weibo/callback/"></aj-oauth-login>
				<br />
			</c:if>

			<c:if test="${!isAdminLogin}">
				<a href="${ctx}/user/register/"><tags:i18n zh="没有账号？点击注册" eng="No account? Click to register" /></a> | 
				<a ref="${ctx}/user/reset_password/"><tags:i18n zh="忘记密码" eng="forget password" /></a>
			</c:if>
		</dd>
	</dl>
</form>
<script src="${ajaxjs_ui_output}/lib/md5.min.js"></script>
<script>
	new Vue({
		el: 'form.aj-form',
		mounted() {
			aj.xhr.form(this.$el, 
				j => {
					if (j && j.isOk) {
						aj.widget.modal.popup(j.msg || '操作成功！');
						setTimeout("location.assign('${ctx}/${isAdminLogin ? 'admin' : 'user'}/')", 3000);
					} else 
						aj.alert(j ? j.msg : '执行失败！原因未知！');
				},
				{
					beforeSubmit(f, json) {
						json.password =  md5(json.password);
					<c:if test="${isAdminLogin}">
						json.isAdminLogin = true;
					</c:if>
					},
					googleReCAPTCHA: '${aj_allConfig.security.disableCaptcha ? '' : aj_allConfig.security.GoogleReCAPTCHA.siteId}'
				}
			);
		}
	});
</script>