<%@page pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<%
	request.setAttribute("CAPTCHA_CODE", com.ajaxjs.web.captcha.CaptchaController.CAPTCHA_CODE);
%>
<tags:content bannerImg="${ctx}/images/memberBanner.jpg">
	<fieldset class="user">
		<legend>重置密码 </legend>
		<form class="aj-form" action="${ctx}/user/reset_password/" method="POST">
			<dl>
				<label>
					<dt>邮 &nbsp;&nbsp;箱</dt>
					<dd>
						<input type="text" name="email" placeholder="请填写注册的邮箱 " required="required" />
					</dd>
				</label>
			</dl><dl>
				<label>
					<dt>验证码</dt>
					<dd class="captcha" style="font-size: .8rem;">
						<aj-page-captcha field-name="${CAPTCHA_CODE}"></aj-page-captcha>
					</dd>
				</label>
			</dl>
			<dl>
				<dt></dt>
				<dd style="font-size: .8rem;">
					<button>确定</button>
					<br />
					<br />
					<a href="${ctx}/user/register/">没有账号？点击注册</a> | <a href="${ctx}/user/login/">登录</a>
				</dd>
			</dl>
		</form>
		
		<script>
			new Vue({
				el : 'form.aj-form',
				mounted() {
					// AJAX 表单提交
			 		ajaxjs.xhr.form(this.$el, json => {
			 			if (json && json.msg) {
			 				alert(json.msg)
			 			} else {
			 				alert("未知异常！");
						}
			 		});
				}
			});
		</script>
	</fieldset>
</tags:content>
