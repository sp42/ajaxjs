<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="/ajaxjs" prefix="c" %>
<%@attribute name="type" required="true" type="String" description="标签类型"%>
<%
	request.setAttribute("CAPTCHA_CODE", com.ajaxjs.web.captcha.CaptchaController.CAPTCHA_CODE);
%>
<c:if test="${type == 'login'}">
	

</c:if>

<c:if test="${type == 'register'}">
	
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
		
		aj.xhr.form('form', json => json && json.msg && aj.alert.show(json.msg));
	</script>
</c:if>