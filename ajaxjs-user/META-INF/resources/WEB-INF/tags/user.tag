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
<form class="aj-form" action="${ctx}/user/register/" method="post">
		<dl>
			<label>
				<dt>用户名</dt>
				<dd>
					<input type="text" name="name" placeholder="4-15位英文/数字/下划线/中划线" @blur="checkUserId($event)" pattern="^[a-zA-Z0-9_-]{4,15}$" required />
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
					<button style="margin: 0;" @click="sendSMScode($event);" :class="(!phoneNumberValid || !isAllowRegister) ?  'ajaxjs-disable' : ''">发送验证码</button>
					
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
			<label>
				<dt>验证码</dt>
				<dd class="captcha">
					<aj-page-captcha field-name="captchaImgCode"></aj-page-captcha>
				</dd>
			</label>
		</dl>
		<dl>
			<dt style="text-align: right;padding-right: 4%;"><input type="checkbox" class="privacy" id="agreement" /></dt>
			<dd>
				<label style="font-size: .9rem;" for="agreement">  同意《<a href="${ctx}/about/service">用户注册协议</a>、<a href="${ctx}/about/privacy">隐私政策》</a></label>
				<br />
				<br />
				<button>注册</button> &nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/user/login/" style="font-size: .9rem;">登录</a>
			</dd>
		</dl>
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
				ajaxjs.xhr.form(
					this.$el, 
					ajaxjs.xhr.defaultCallBack_cb.delegate(null, null, 
						j => {
							setTimeout("location.assign('${ctx}/user/login/')", 2000)
						}, 
						j => this.$children[0].refreshCode()) ,
					{
						beforeSubmit: (f, json) => {
							if (!this.$el.$('.privacy').checked) {
								ajaxjs.alert.show('请同意用户注册协议和隐私政策');
								return false;
							}

							if (json.passowrd != json.passowrd2) {
								ajaxjs.alert.show('两次密码输入不一致！');
								return false;
							}
							
							json.password = md5(json.password);
							delete json.password2;
						}
					}
				);
			},
		 
			methods : {
				checkUserId(e) {
					var el = e.target, userId = el.value;
				
					if(aj.formValidator.hasError(el) === undefined)
						aj.xhr.get('${ctx}/user/checkIfUserNameRepeat/', json => {
							this.isAllowRegister = !json.result.isRepeat;
							
							if (json.result.isRepeat)
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
		
		aj.xhr.form('form');
	</script>
	
</c:if>