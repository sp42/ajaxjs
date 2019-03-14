<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/jsp/common/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/main.less" />
</jsp:include>
</head>
<body class="userRegister">
	<%@include file="/WEB-INF/jsp/nav.jsp"%>
	<h3 class="jb">欢迎注册</h3>

 	<form class="user-form center" style="width:50%" action="../user/register" method="post" @submit="onSubmit">
		<dl>
			<label>
				<dt>用户 ID</dt>
				<dd>
					<input type="text" name="name" placeholder="请输入用户名/邮箱/中国大陆手机号码" @blur="checkUserId($event)" />
					<span v-if="phoneMsg">{{phoneMsg}}</span>
					<div class="note">输入手机后，若合法并可注册，则允许点击“发送验证码”</div>
				</dd>
			</label>
		</dl>
		<!-- 
		<dl class="hide">
			<label>
				<dt>短信验证码:</dt>
				<dd>
					<input type="text" name="randomSmsCode" required placeholder="请输入手机验证码"
						style="width: 50%;" data-erruileft="200" />
					<button style="margin: 0;" @click="sendSMScode($event);" :class="(!phoneNumberValid || !isAllowRegister) ?  'ajaxjs-disable' : ''">发送验证码</button>
					
					<div class="note">通过验证码识别你的手机，五分钟内有效</div>
				</dd>
			</label>
		</dl>
		 -->
		<dl>
			<label>
				<dt>密码:</dt>
				<dd>
					<input type="password" name="password" placeholder="密码" pattern="[0-9A-Za-z]{6,10}" required />
					<div class="note">请输入6位以上密码</div>
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>重复密码:</dt>
				<dd>
					<input type="password" name="password2" placeholder="重复密码" required />
				</dd>
			</label>

		</dl>
		<dl>
			<dt></dt>
			<dd class="bottomLink">
				<label style="font-size: .9rem;"> <input type="checkbox" class="privacy" /> 同意<a href="privacy.jsp">《用户注册协议》和《隐私政策》</a>
				</label>
			</dd>
		</dl>
		<dl>
			<dt></dt>
			<dd>
				<div style="color:red;"><span v-html="errMsg.join('<br >')"></span></div>
				<button>注册</button> <a href="login">已有账号，立刻登录</a>
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
			el: '.user-form',
			data: {
				phoneNumber: 123,
				phoneNumberValid : false, // true 表示合法手机号码
				phoneMsg : null,
				isAllowRegister : false, // 是否可以注册
				errMsg :[]
			},
			mounted : function() {
				var formEl = this.$el;
				var onItemVaild = this.onItemVaild;
				formEl.$('input[type=text], input[type=password], input[type=number]', function(itemEl) {
					//console.log(itemEl)
					itemEl.oninvalid = onItemVaild;	
				});
			},
			methods : {
				checkUserId (e){
					var inputEl = e.target, v = inputEl.value;
					
					if(validateEmail(v)) {
						inputEl.name = "email";
					} else if(phoneNumber(v)) {
						inputEl.name = "email";
					} else if(!v) {
					}
					
					this.phoneMsg = v ? '' : inputEl.placeholder;
				},
				onItemVaild : function(e) {
					e.preventDefault();
					var el = e.target;
					el.style.borderColor = 'red';
					
					var msg;
					if(el.validity.patternMismatch)
						msg = '该内容格式不正确';
					if(el.validity.valueMissing)
						msg = '该内容不可为空';
					this.errMsg.push(el.placeholder + "：" + msg);
				},
				
				checkPhoneValid: function(e) {
					var phone = e.target.value;
					this.phoneNumberValid = phoneNumber(phone);
					
					if(this.phoneNumberValid) {
						this.checkIfUserPhoneRepeat(phone);
					} 
				},
				
				checkPhoneValid_ShowMsg : function(e) {
					var phone = e.target.value;
					this.phoneNumberValid = phoneNumber(phone);
					this.phoneMsg = this.phoneNumberValid ? "" : "手机格式不正确";
				},
				
				checkIfUserPhoneRepeat : function(phone) {
					var self = this;
					aj.xhr.get('${ctx}/user/register/checkIfUserPhoneRepeat', function(json) {
						self.isAllowRegister = !json.result.isRepeat;
						
						if (json.result.isRepeat)
							self.phoneMsg = phone + "已经注册！";
						else 
							self.phoneMsg = phone + "可以注册！";
					}, {
						phone : phone
					});
				},
				
				sendSMScode : function (e) {
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
				
				onSubmit : function() {
					if (!this.$el.$('.privacy').checked) {
						ajaxjs.alert.show('请同意用户注册协议和隐私政策');
						return false;
					}

					var passowrd = this.$el.$('input[name=password]')
					if (passowrd.value != this.$el.$('input[name=password2]').value) {
						ajaxjs.alert.show('两次密码输入不一致！');
						return false;
					}
					
					if(this.errMsg && this.errMsg.length) {
						aj.alert.show(this.errMsg.join('<br />'));
						return false;
					}
						
					
					passowrd.value = md5(passowrd.value);
				}
			}
		});
	</script>
	<%@include file="/WEB-INF/jsp/footer.jsp"%>
</body>
</html>