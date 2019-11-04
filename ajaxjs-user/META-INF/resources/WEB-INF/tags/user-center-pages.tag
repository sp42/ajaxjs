<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="/ajaxjs" prefix="c" %>
<%@attribute name="type" required="true" type="String" description="标签类型"%>

<c:if test="${type == 'account'}">	
	<ul class="safe">
		<li>	
		<c:if test="${aj_allConfig.user.login.canModiflyUserName}">
			<a href="${ctx}/user/user-center/account/safe/?action=modiflyUserName">修改登录名</a>
		</c:if>
			<div class="${empty userName ? 'fail' : 'ok' }">登录名</div>
			<div>${empty userName ? '未绑定邮箱' : userName} 登录名即用户名</div>
		</li>
		<li>	
			<a href="${ctx}/user/user-center/account/safe/?action=modiflyPhone">更换手机</a>
			<div class="${empty userPhone ? 'fail' : 'ok' }">绑定手机</div><div>${empty userPhone ? '未绑定手机' : userPhone}</div>
		</li>
		<li>	
			<a href="${ctx}/user/user-center/account/safe/?action=modiflyEmail">设置邮箱</a>
			<div class="${empty email ? 'fail' : 'ok' }">绑定邮箱</div><div>${empty email ? '未绑定邮箱' : email}</div>
		</li>
		<li>	
			<a href="${ctx}/user/user-center/account/safe/?action=resetPsw">修改密码</a>
			<div class="ok">设置密码</div><div>已设置</div>
		</li>
		<li>	
			<a href="${ctx}/user/user-center/account/log-history/">查看登录历史</a>
			<div style="padding-left:3%;">最近登录</div><div>
				<c:dateFormatter value="${lastUserLoginedInfo.createDate}"></c:dateFormatter>
			</div>
		</li>
	<c:if test="${aj_allConfig.user.login.thridOauth}">
		<li>	
			<a href="oauth/">管理绑定</a>
			<div style="padding-left:3%;">第三方登录</div><div>通过微博、微信、QQ等第三方登录的绑定</div>
		</li>
	</c:if>
		<li>	
			<a href="delete-account/">账号注销</a>
			<div style="padding-left:3%;">删除帐号</div><div>删除该帐号以及所有该帐号关联的信息</div>
		</li>
	</ul>
</c:if>

<c:if test="${type == 'modiflyPhone'}">
	<div class="test-pl">
		<aj-process-line ref="processLine" :items="['输入新号码', '审核手机号码','完成']"></aj-process-line>
	</div>
	
	<form class="aj-form fixed-width" action="${ctx}/user/info/modiflyPhone" method="POST">
		<dl>
			<label>
				<dt>手机</dt>
				<dd>
					<input type="text" name="phone" placeholder="请输入手机" required />
				</dd>
			</label>
		</dl>
		<dl>
			<dt></dt>
			<dd>
				<button>提交</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <button class="returnBtn">返回</button>
			</dd>
		</dl>
	</form>
	
	<script>
		ProcessLine = new Vue({el : '.test-pl'});
		
		aj.xhr.form('.aj-form', json => {
			if (json) {
				if (json.isOk) {
					ajaxjs.alert.show(json.msg || '操作成功！');
					ProcessLine.$refs.processLine.go(1);
				} else {
					ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
				}
			} else {
				ajaxjs.alert.show('ServerSide Error!');
			}
		});
	</script>
</c:if>

<c:if test="${type == 'modiflyEmail'}">
	<div class="test-pl">
		<aj-process-line ref="processLine" :items="['填写新邮箱', '邮箱审核', '完成']"></aj-process-line>
	</div>
	
	<form class="aj-form fixed-width" action="${ctx}/user/info/modiflyEmail" method="POST">
		<dl>
			<label>
				<dt>邮箱</dt>
				<dd>
					<input type="email" name="email" value="${user.email}" placeholder="请输入邮箱" required pattern="[^@]+@[^@]+\.[a-zA-Z]{2,6}" /> 
				</dd>
			</label>
		</dl>
		<dl>
			<dt></dt>
			<dd>
				<button>提交</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <button class="returnBtn">返回</button>
			</dd>
		</dl>
	</form>
	
	<script>
		ProcessLine = new Vue({el : '.test-pl'});
		
		aj.xhr.form('.aj-form', json => {
			if (json) {
				if (json.isOk) {
					ajaxjs.alert.show(json.msg || '操作成功！');
					ProcessLine.$refs.processLine.go(1);
				} else {
					ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
				}
			} else {
				ajaxjs.alert.show('ServerSide Error!');
			}
		});
	</script>
</c:if>

<c:if test="${type == 'resetPsw'}">
	<div class="test-pl">
		<aj-process-line ref="processLine" :items="['填写信息', '重置密码', '完成']"></aj-process-line>
	</div>
	
	<form class="aj-form fixed-width" action="resetPassword" method="POST">
		<dl>
			<label>
				<dt>原密码</dt>
				<dd>
					<input type="password" name="password"  placeholder="请输入原来的密码" required pattern="[0-9A-Za-z]{6,10}" />
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>新密码</dt>
				<dd>
					<input type="password" name="new_password" placeholder="6~10位的数字或英文字母" required  pattern="[0-9A-Za-z]{6,10}"  />
				</dd>
			</label>
		</dl>
		<dl>
			<label>
				<dt>重复新密码</dt>
				<dd>
					<input type="password" name="new_password2" placeholder="请输入重复新密码" required pattern="[0-9A-Za-z]{6,10}" />
				</dd>
			</label>
		</dl>
		<dl>
			<dt></dt>
			<dd>
				<br />
				<button>提交</button> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <button class="returnBtn">返回</button>
			</dd>
		</dl>
	</form>
	
	<script>
		ProcessLine = new Vue({el : '.test-pl'});
		
		aj.xhr.form('.aj-form', json => {
			if (json) {
				if (json.isOk) {
					ajaxjs.alert.show(json.msg || '操作成功！');
					ProcessLine.$refs.processLine.go(2);
				} else {
					ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
				}
			} else {
				ajaxjs.alert.show('ServerSide Error!');
			}
		}, {
 			beforeSubmit (form, json) {
 				ProcessLine.$refs.processLine.go(1);
					json.password = md5(json.password);
					json.new_password = md5(json.new_password);
					
					delete json.new_password2;
				}
		});
	</script>
</c:if>	