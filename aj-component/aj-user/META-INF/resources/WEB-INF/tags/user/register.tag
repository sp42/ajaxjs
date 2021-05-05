<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<form class="aj-form" action="${ctx}/user/register/" method="post">
	<dl>
		<label>
			<dt>用户名</dt>
			<dd>
				<input type="text" name="name" placeholder="4-15位英文/数字/下划线/中划线" @blur="checkUserId" autofocus="false" pattern="^[a-zA-Z0-9_-]{4,20}$" required />
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
			<button>注册</button> &nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/user/login/" style="font-size: .9rem;">我已经注册，点此登录</a>
		</dd>
	</dl>
	<!-- 注册用户组 -->
	<input type="hidden" name="roleId" value="30" />
</form>
<script src="${ajaxjs_ui_output}/lib/md5.min.js"></script>
<script src="${aj_static_resource}/dist/user/register.js"></script>

