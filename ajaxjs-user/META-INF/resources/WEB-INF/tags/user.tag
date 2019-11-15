<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="/ajaxjs" prefix="c" %>
<%@attribute name="type" required="true" type="String" description="标签类型"%>

<%
	request.setAttribute("CAPTCHA_CODE", com.ajaxjs.web.captcha.CaptchaController.CAPTCHA_CODE);
%>

<c:if test="${type == 'login'}">
	<%@attribute name="isAdminLogin" required="false" type="Boolean" description="是否后台的登录"%>
	
	<form class="aj-form" action="${ctx}/user/login/" method="POST">
		<dl>
			<label>
				<dt>用户帐号</dt>
				<dd>
					<input type="text" name="userID" placeholder="请输入${userID}" required pattern="^[a-zA-Z0-9_-]{4,10}$" />
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>登录密码</dt>
				<dd>
					<input type="password" name="password" placeholder="6~10位数字或英文字母" required
					 pattern="[0-9A-Za-z]{6,10}" title="请输入您的由6~10位由数字和26个英文字母的登录密码" />
					
				</dd>
			</label>
		</dl>
		<dl>
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
				<button>登录</button>
				<br />
				<br />
				
		
			<c:if test="${aj_allConfig.user.login.thridOauth}">
				<a href="#" onClick="loginWeibo();"><img src="http://www.sinaimg.cn/blog/developer/wiki/240.png" /></a>
				<br />
			</c:if>
			
			<c:if test="${!isAdminLogin}">
				<a href="${ctx}/user/register/">没有账号？点击注册</a> | <a href="${ctx}/user/reset_password/">忘记密码</a>
			</c:if>
			</dd>
		</dl> 
	</form>		
	<script src="${ajaxjs_ui_output}/lib/md5.min.js"></script>
	<script>
		new Vue({
			el : 'form.aj-form',
			mounted() {
				ajaxjs.xhr.form(
					this.$el, 
					ajaxjs.xhr.defaultCallBack_cb.delegate(null, null, 
						j => setTimeout("location.assign('${ctx}/${isAdminLogin ? 'admin' : 'user/user-center'}/')", 3000), j => this.$children[0].refreshCode()) ,
					{
						beforeSubmit(f, json) {
							json.password =  md5(json.password);
						<c:if test="${isAdminLogin}">
							json.isAdminLogin = true;
						</c:if>
						}
					}
				);
			}
		});
	
	</script>

</c:if>

<c:if test="${type == 'register'}">
	
	
</c:if>