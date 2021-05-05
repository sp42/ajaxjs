<%@page pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<tags:content bannerImg="${ctx}/images/memberBanner.jpg">
	<fieldset class="user">
		<legend>重置密码 </legend>
		<center style="margin:5% 0;">
		 	<label style="cursor: pointer;"><input type="radio" :value="1" v-model="mode" /> 用电子邮箱重置密码</label> 
	    	<label style="cursor: pointer;"><input type="radio" :value="2" v-model="mode" /> 用手机短信重置密码</label>
		</center>
    	
    	
		<form v-show="mode == 1" class="aj-form f1" action="${ctx}/user/reset_password/findByEmail/" method="POST">
			<dl>
				<label>
					<dt>邮 &nbsp;&nbsp;箱</dt>
					<dd>
						<input type="text" name="email" placeholder="请填写注册的邮箱 " required="required" />
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
		
		<form v-show="mode == 2" class="aj-form f2" action="${ctx}/user/reset_password/findBySms/" method="POST">
			<dl>
				<label>
					<dt>手机号码</dt>
					<dd>
						<input type="text" name="phone" placeholder="请填写注册的手机号码" required="required" />
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
		 		aj.xhr.form('.f1', json => {
		 			if (json && json.msg) {
		 				alert(json.msg)
		 			} else {
		 				alert("未知异常！");
					}
		 		}, {
		 			googleReCAPTCHA: '${aj_allConfig.security.GoogleReCAPTCHA.siteId}'
		 		});
/* 		 		ajaxjs.xhr.form('.f2', json => {
		 			if (json && json.msg) {
		 				alert(json.msg);
		 				location.assign('findBySms/');
		 			} else {
		 				alert("未知异常！");
					}
		 		}); */
				
			},
			watch: {
				mode(n) {					
					if(n === 1)
						this.$refs.c2.refreshCode();
					if(n === 2)
						this.$refs.c2.refreshCode();
					//debugger
				}
			}
		});
	</script>
</tags:content>
