<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" description="默认的用户登录、注册标签"%>
<%@attribute name="type" required="true" type="String" description="标签类型"%>
<%@taglib uri="/ajaxjs" prefix="c" %>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>

<c:if test="${type == 'login'}">
	<%@attribute name="isAdminLogin" required="false" type="Boolean" description="是否后台的登录"%>
	
	<form class="aj-form" action="${ctx}/user/login/" method="POST" data-msg-newline="true">
		<dl>
			<label>
				<dt><tags:i18n zh="用户帐号" eng="User Account" /></dt>
				<dd>
					<input type="text" name="userID" placeholder="请输入${userID}" required pattern="^[a-zA-Z0-9_-]{4,20}$" />
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt><tags:i18n zh="登录密码" eng="Password" /></dt>
				<dd>
					<input type="password" name="password" placeholder="<tags:i18n zh="6~10位数字或英文字母" eng="6 to 10 digits or English letters" />" required
					 pattern="[0-9A-Za-z]{6,10}" title="请输入6~10位数字或英文密码" />
					
				</dd>
			</label>
		</dl>
		<dl>
			<dt></dt>
			<dd style="font-size: .8rem;;text-align: left">
				<button><tags:i18n zh="登录" eng="Login" /></button>
				<br />

		
			<c:if test="${aj_allConfig.user.login.thridOauth}">
				<br />
				<aj-oauth-login client-id="360568732" redirect-uri="http://www.palate-movement.com/user/oauth/weibo/callback/"></aj-oauth-login>
				<br />
			</c:if>
			
			<c:if test="${!isAdminLogin}">
				<a href="${ctx}/user/register/"><tags:i18n zh="没有账号？点击注册" eng="No account? Click to register" /></a> | <a href="${ctx}/user/reset_password/"><tags:i18n zh="忘记密码" eng="forget password" /></a>
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
							aj.alert.show(json.msg || '操作成功！');
							setTimeout("location.assign('${ctx}/${isAdminLogin ? 'admin' : 'user'}/')", 3000);
						} else 
							aj.alert.show(j ? j.msg : '执行失败！原因未知！');
					},
					{
						beforeSubmit(f, json) {
							json.password =  md5(json.password);
						<c:if test="${isAdminLogin}">
							json.isAdminLogin = true;
						</c:if>
						},
						googleReCAPTCHA: '${aj_allConfig.security.GoogleReCAPTCHA.siteId}'
					}
				);
			}
		});
	</script>
</c:if>

<c:if test="${type == 'register'}">
<form class="aj-form" action="${ctx}/user/register/" method="post">
		<dl>
			<label>
				<dt>用户名</dt>
				<dd>
					<input type="text" name="name" placeholder="4-15位英文/数字/下划线/中划线" @blur="checkUserId($event)" pattern="^[a-zA-Z0-9_-]{4,20}$" required />
					<span>{{checkUserIdMsg}}</span>
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>用户邮箱</dt>
				<dd>
					<input type="email" name="email" placeholder="请输入邮箱" required @blur="checkEmailValid($event)" />
					<span>{{checkUserEmailMsg}}</span>
				</dd>
			</label>
		</dl>
		<!-- 
		<dl class="hide">
			<label>
				<dt>短信验证码:</dt>
				<dd>
					<input type="text" name="randomSmsCode" required placeholder="请输入手机验证码" style="width: 50%;" data-erruileft="200" />
					<button style="margin: 0;" @click="sendSMScode($event);" :class="(!phoneNumberValid || !isAllowRegister) ?  'aj-disable' : ''">发送验证码</button>
					
					<div class="note">通过验证码识别你的手机，五分钟内有效</div>
				</dd>
			</label>
		</dl>
		 -->
		<dl>
			<label>
				<dt>密 码</dt>
				<dd>
					<input type="password" name="password" placeholder="6~10位数字或英文字母" pattern="[0-9A-Za-z]{6,10}" required />
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>重复密码</dt>
				<dd>
					<input type="password" name="password2" placeholder="重复密码" required />
				</dd>
			</label>
		</dl>
		<dl>
			<dt style="text-align: right;padding-right: 4%;"><input type="checkbox" class="privacy" id="agreement" /></dt>
			<dd>
				<label style="font-size: .9rem;" for="agreement">  同意《<a href="${ctx}/about/service">用户注册协议</a>、<a href="${ctx}/about/privacy">隐私政策》</a></label>
				<br />
				<br />
				<button>注册</button> &nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/user/login/" style="font-size: .9rem;">我已是会员，点此登录</a>
			</dd>
		</dl>
		<!-- 注册用户组 -->
		<input type="hidden" name="roleId" value="30" />
	</form>
	
	<script src="${ajaxjs_ui_output}/lib/md5.min.js"></script>
	<script>
		function phoneNumber(p) {
			return /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/.test(p);
		}
		
		function validateEmail(email) {
		    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		    return re.test(String(email).toLowerCase());
		}
		
		new Vue({
			el: '.aj-form',
			data: {
				checkUserIdMsg: '',
				checkUserPhoneMsg : '',
				checkUserEmailMsg : '',
				isAllowRegister : false // 是否可以注册
			},
			
			mounted() {
 				aj.xhr.form(this.$el, 
					aj.xhr.defaultCallBack_cb.delegate(null, null, j => setTimeout("location.assign('${ctx}/user/login/')", 2000), 
						j => this.$children[0].refreshCode()),
					{
						beforeSubmit: (f, json) => {
							if (!this.$el.$('.privacy').checked) {
								aj.alert.show('请同意用户注册协议和隐私政策');
								return false;
							}

							if (json.passowrd != json.passowrd2) {
								aj.alert.show('两次密码输入不一致！');
								return false;
							}
							
							json.password = md5(json.password);
							delete json.password2;
						},
						googleReCAPTCHA: '${aj_allConfig.security.GoogleReCAPTCHA.siteId}'
					}
				); 
			},
		 
			methods: {
				checkUserId(e) {
					var el = e.target, userId = el.value;
				
					if(aj.formValidator.hasError(el) === undefined)
						aj.xhr.get('${ctx}/user/checkIfUserNameRepeat/', j => {
							this.isAllowRegister = !j.result.isRepeat;
							
							if (j.result.isRepeat)
								this.checkUserIdMsg = userId + "已经注册";
							else 
								this.checkUserIdMsg = userId + "可以注册";
						}, {
							name : userId
						});
				},
				checkEmailValid(e) {
					var el = e.target, email = el.value;
					
					if(aj.formValidator.hasError(el) === undefined) {
						aj.xhr.get('${ctx}/user/checkIfUserEmailRepeat', json => {
							this.isAllowRegister = !json.result.isRepeat;
							
							if (json.result.isRepeat)
								this.checkUserEmailMsg = email + "已经注册";
							else 
								this.checkUserEmailMsg = email + "可以注册";
						}, {
							email : email
						});
					} 
				},
				checkPhoneValid (e) {
					var phone = e.target.value;
					this.phoneNumberValid = phoneNumber(phone);
					
					if(this.phoneNumberValid) {
						aj.xhr.get('${ctx}/user/register/checkIfUserPhoneRepeat', json => {
							this.isAllowRegister = !json.result.isRepeat;
							
							if (json.result.isRepeat)
								this.phoneMsg = phone + "已经注册！";
							else 
								this.phoneMsg = phone + "可以注册！";
						}, {
							phone : phone
						});
					} 
				},

				sendSMScode(e) {
					e.preventDefault();
					if(this.phoneNumberValid && this.isAllowRegister) {
						var value = aj('form input[name=phone]').value;
						if (phoneNumber(value)) {
							aj.xhr.post('${ctx}/user/register/sendSMScode', function(json) {
								if (json && json.msg)
									ajaxjs.alert.show(json.msg);
							}, {
								phoneNo : value
							});
						} else {
							ajaxjs.alert.show('手机格式不正确！');
						}
					}
				},
				
				onSubmit() {
					if (!this.$el.$('.privacy').checked) {
						ajaxjs.alert.show('请同意用户注册协议和隐私政策');
						return false;
					}

					var passowrd = this.$el.$('input[name=password]')
					if (passowrd.value != this.$el.$('input[name=password2]').value) {
						ajaxjs.alert.show('两次密码输入不一致！');
						return false;
					}
					
					passowrd.value = md5(passowrd.value);
				}
			}
		});
	</script>
</c:if>

<c:if test="${type == 'onNoLogin'}">
	<fieldset class="aj-fieldset onNoLogin" style="margin:5% auto;max-width:500px;">
		<legend>尚未登录，没有权限操作 </legend>
		<p>抱歉，您尚未登录，无法进行下一步操作。<br />请先<a href="${ctx}/user/login/">【登录】</a>，五秒钟之后自动跳转。</p>
		<script>
			setTimeout(function() {
				location.assign("${ctx}/user/login/");
			}, 5000);
		</script>
	</fieldset>
</c:if>