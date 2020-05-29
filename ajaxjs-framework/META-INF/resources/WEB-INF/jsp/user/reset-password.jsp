﻿<%@page pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<%
	request.setAttribute("CAPTCHA_CODE", com.ajaxjs.web.captcha.CaptchaController.CAPTCHA_CODE);
%>
<tags:content bannerImg="${ctx}/images/memberBanner.jpg">
	<fieldset class="user">
		<legend>重置密码 </legend>
		
	 	<label style="cursor: pointer;"><input type="radio" value="1" v-model="mode" /> 用电子邮箱重置密码</label> 
    	<label style="cursor: pointer;"><input type="radio" value="2" v-model="mode" /> 用手机短信重置密码</label>
    	
    	
		<form v-show="mode == 1" class="aj-form f1" action="${ctx}/user/reset_password/findByEmail/" method="POST">
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
						<aj-page-captcha ref="c1"  field-name="${CAPTCHA_CODE}"></aj-page-captcha>
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
		
		<form v-show="mode == 2" class="aj-form" action="${ctx}/user/reset_password/findBySms/" method="POST">
			<dl>
				<label>
					<dt>手机号码</dt>
					<dd>
						<input type="text" name="phone" placeholder="请填写注册的手机号码" required="required" />
					</dd>
				</label>
			</dl><dl>
				<label>
					<dt>验证码</dt>
					<dd class="captcha" style="font-size: .8rem;">
						<aj-page-captcha ref="c2" field-name="${CAPTCHA_CODE}"></aj-page-captcha>
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
	</fieldset>
	<script>
		new Vue({
			el: 'fieldset.user',
			data: {
				mode: 1,
			},
			mounted() {
				// AJAX 表单提交
				this.$el.$('form', f => {
					console.log(f)
			 		ajaxjs.xhr.form(f, json => {
			 			if (json && json.msg) {
			 				alert(json.msg)
			 			} else {
			 				alert("未知异常！");
						}
			 		});
				});
			},
			watch(n) {
				if(n === 1)
					this.$refs.c1;
				if(n === 2)
					this.$refs.c2;
				debugger
			}
		});
	</script>
</tags:content>
