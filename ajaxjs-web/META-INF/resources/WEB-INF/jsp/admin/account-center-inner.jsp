<%@ page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
	<div align="center">您的用户 ID：${userId}</div>
	<ul class="safe">
		<li>	
		<c:if test="${aj_allConfig.user.login.canModiflyUserName}">
			<a href="javascript:openPopupTpl('modiflyPhone');">修改登录名</a>
		</c:if>
			<div class="${empty userName ? 'fail' : 'ok' }">登录名</div>
			<div>${empty userName ? '未绑定邮箱' : userName} 登录名即用户名</div>
		</li>
		<li>	
			<a href="javascript:openPopupTpl('modiflyPhone');">更换手机</a>
			<div class="${empty userInfo.phone ? 'fail' : 'ok' }">绑定手机</div><div>${empty userPhone ? '未绑定手机' : userPhone}</div>
		</li>
		<li>	
			<a href="javascript:openPopupTpl('modiflyEmail');">设置邮箱</a>
			<div class="${empty userInfo.email ? 'fail' : 'ok' }">绑定邮箱</div><div>${empty userInfo.email ? '未绑定邮箱' : userInfo.email} 
				&nbsp;${empty userInfo.email ? '' : (isEmailVerified ? '已验证' :'未验证 &nbsp;<a href="javascript:emailVerify();">点击验证</a>')}</div>
				
			<div>
			
			
			</div>
			<script>
				function emailVerify() {
					alert(9)
				}
			</script>
		</li>
		<li>	
			<a href="javascript:openPopupTpl('resetPsw');">修改密码</a>
			<div class="ok">设置密码</div><div>已设置</div>
		</li>
		<li class="log-history">	
			<a href="log-history/">查看登录历史</a>
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
			<div style="padding-left:3%;">用户组</div><div>${UserGroups[userGroupId].name}</div>
		</li>
		<li>	
			<a href="javascript:openPopupTpl('deleteAccount');">账号注销</a>
			<div style="padding-left:3%;">删除帐号</div><div>删除该帐号以及所有该帐号关联的信息</div>
		</li>
	</ul>
	
	<style>
		.test-pl{
			width:600px;
		}
	</style>
	
	<!-- 更换id -->
	<div class="modiflyUserId-holder"></div>
	<textarea class="hide modiflyUserId">
		<aj-layer ref="layer">
			<h2>修改用户名</h2>
			<div class="test-pl">
				<aj-process-line ref="processLine" :items="['填写登录名信息', '检查是否可用', '完成']"></aj-process-line>
			</div>
			
			<form class="aj-form fixed-width" action="${ctx}/user/info/modiflyUserName" method="POST">
				<dl>
					<label>
						<dt>用户名</dt>
						<dd>
							<input type="text" name="userName" value="${userName}" placeholder="请输入用户名" required pattern=".{4,10}"/>
							<div class="note">请输入您的由4~10位由中文26个英文字母的用户名</div>
						</dd>
					</label>
				</dl>
				<dl>
					<dt></dt>
					<dd class="aj-btnsHolder" style="text-align: left;width:300px;">
						<button>提 交</button> <button @click.prevent="closePopup($event);return false;">关 闭</button>
					</dd>
				</dl>
			</form>
		</aj-layer>
	</textarea>
	
	
	<div class="modiflyPhone-holder"></div>
	<textarea class="hide modiflyPhone">
		<aj-layer ref="layer">
			<h2>更换手机</h2>
			<div class="test-pl">
				<aj-process-line ref="processLine" :items="['填写手机号码', '输入验证码', '完成']"></aj-process-line>
			</div>
			
			<form class="aj-form fixed-width" action="${ctx}/user/account/modiflyPhone" method="POST">
				<dl>
					<label>
						<dt>手机号码</dt>
						<dd>
							<input type="number" name="phone" value="${user.phone}" placeholder="请输入中国大陆手机号码" required pattern="" /> 
						</dd>
					</label>
				</dl>
				<dl>
					<dt></dt>
					<dd class="aj-btnsHolder" style="text-align: left;width:300px;"d>
						<button>提 交</button> <button @click.prevent="closePopup($event);return false;">关 闭</button>
					</dd>
				</dl>
			</form>
		</aj-layer>
	</textarea>
	
	
	<!-- 弹窗浮层 -->
	<div class="modiflyEmail-holder"></div>
	<textarea class="hide modiflyEmail">
		<aj-layer ref="layer">
			<h2>设置邮箱</h2>
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
					<dd class="aj-btnsHolder" style="text-align: left;width:300px;">
						<button>提 交</button>  <button @click.prevent="closePopup($event);">关 闭</button>
					</dd>
				</dl>
			</form>
		</aj-layer>
	</textarea>
	
	<div class="resetPsw-holder"></div>
	<textarea class="hide resetPsw">
		<aj-layer ref="layer">
			<h2>重置密码</h2>
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
					<dd class="aj-btnsHolder" style="text-align: left;width:300px;">
						<button>提 交</button> <button @click.prevent="closePopup($event);">返 回</button>
					</dd>
				</dl>
			</form>
		</aj-layer>
	</textarea>
	
	<!-- 账号注销 -->
	<div class="deleteAccount-holder"></div>
	<textarea class="hide deleteAccount">
		<aj-layer ref="layer">
			<h2>账号注销</h2>
			<div class="test-pl">
				<aj-process-line ref="processLine" :items="['确认删除', '注销帐号', '登出系统']"></aj-process-line>
			</div>
			
			<p class="aj-note">
				1、帐号删除后您将不能继续使用本系统提供的服务，并且您的帐号相关信息将永久删除；<br />
				2、帐号删除后将不能再次找回，请谨慎操作；<br />
				3、删除后系统立刻登出离开该系统。
			</p>
			
			<form class="aj-form fixed-width" action="." method="POST">
				<dl>
					<label>
						<dt>帐号密码</dt>
						<dd>
							<input type="password" name="password" placeholder="请输入原密码再按确定删除帐号" required pattern="[0-9A-Za-z]{6,10}" />
						</dd>
					</label>
				</dl>
				<dl>
					<dt></dt>
					<dd class="aj-btnsHolder" style="text-align: left;width:300px;">
						<button>确 定</button> <button @click.prevent="closePopup($event);">返 回</button>
					</dd>
				</dl>
			</form>
		</aj-layer>
	</textarea>
	
	<script>
		window.PopupTpl = {};
		
		function openPopupTpl(type) {
			var obj = PopupTpl[type];
			
			if (!obj) {
				obj = new Vue({
					el : '.' + type + '-holder',
					template : aj('.' + type).value,
					methods : {
						closePopup(e){
							this.$el.classList.add('hide');
						}
					}
				});
				
				PopupTpl[type] = obj;
			}
			
			obj.$refs.layer.show();
			
			switch(type) {
				case 'modiflyUserId':	
					aj.xhr.form(obj.$el.$('.aj-form'), json => {				
						if (json.isOk) {
							ajaxjs.alert.show(json.msg || '操作成功！');
							obj.$refs.processLine.go(1);
						} else {
							ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
						}
					});
				break;
				case 'modiflyPhone':					
					aj.xhr.form(obj.$el.$('.aj-form'), json => {				
						if (json.isOk) {
							ajaxjs.alert.show(json.msg || '操作成功！');
							obj.$refs.processLine.go(1);
						} else {
							ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
						}
					});
				break;
				case 'modiflyEmail':		
					aj.xhr.form(obj.$el.$('.aj-form'), json => {				
						if (json.isOk) {
							ajaxjs.alert.show(json.msg || '操作成功！');
							obj.$refs.processLine.go(1);
						} else {
							ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
						}
					});
				break;
				case 'resetPsw':					
					ProcessLine = new Vue({el : '.test-pl'});
					
					aj.xhr.form('.aj-form', json => {
						if (json.isOk) {
							ajaxjs.alert.show(json.msg || '操作成功！');
							ProcessLine.$refs.processLine.go(2);
						} else {
							ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
						}
					}, {
			 			beforeSubmit (form, json) {
			 				ProcessLine.$refs.processLine.go(1);
								json.password = md5(json.password);
								json.new_password = md5(json.new_password);
								
								delete json.new_password2;
							}
					});
				break;
				case 'deleteAccount':		
					aj.xhr.form('.aj-form', json => {
						if (json.isOk) {
							ajaxjs.alert.show(json.msg || '操作成功！');
							ProcessLine.$refs.processLine.go(1);
							
							// logout
							setTimeout(()=>{
								ProcessLine.$refs.processLine.go(2);
								aj.xhr.get('${ctx}/user/logout/');
							}, 2000);
						} else {
							ajaxjs.alert.show(json.msg || '执行失败！原因未知！');
						}
					}, {
			 			beforeSubmit (form, json) {
			 				debugger;
								json.password = md5(json.password);
							}
					});
				break;
			}
		}	
	</script>
	
	<br />
	<br />
	<br />