<%@ page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/jsp/common/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
	</jsp:include>
	<script src="${ajaxjs_ui_output}/lib/md5.min.js"></script>
</head>
<body>
	<div class="center userCenter">
		<%@include file="user-center-menu.jsp"%>
		
		<div class="right">
			<h3 class="jb">账户安全</h3>
			
			<c:choose>
				<c:when test="${param.action == 'resetPsw'}">
				
				<div class="test-pl">
					<aj-process-line ref="processLine" :items="['填写信息', '重置密码', '完成']"></aj-process-line>
				</div>
				
				<form class="user-form center" action="${ctx}/user/info/resetPassword" method="POST" style="margin-left: 15%;">
					<dl>
						<label>
							<dt>原密码</dt>
							<dd>
								<input type="password" name="password"  placeholder="请输入原来的密码" required pattern="[0-9A-Za-z]{6,10}" value="" />
								<div class="note">虽已登录了，但为安全起见，请先输入原密码方可修改之</div>
							</dd>
						</label>
						<label>
							<dt>新密码</dt>
							<dd>
								<input type="password" name="new_password"  placeholder="请输入新密码" required  pattern="[0-9A-Za-z]{6,10}" value="" />
								<div class="note">请输入您的由6~10位由数字和26个英文字母的登录密码</div>
							</dd>
						</label>
			
						<label>
							<dt>重复新密码</dt>
							<dd>
								<input type="password" name="new_password2" placeholder="请输入重复新密码" required pattern="[0-9A-Za-z]{6,10}" value="" />
								<div class="note">请您重复输入新的密码</div>
							</dd>
						</label>
					</dl>
					<dl>
						<dt></dt>
						<dd>
							<input type="submit" value="提交" /> <button onclick="location.assign('${ctx}/user/center/loginInfo/');">返回</button>
						</dd>
					</dl>
				</form>
				
				<script>
					ProcessLine = new Vue({el : '.test-pl'});
					
					aj.xhr.form('.user-form', function(json) {
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
			 			beforeSubmit : function(form, json) {
			 				ProcessLine.$refs.processLine.go(1);
		 					json.password = md5(json.password);
		 					json.new_password = md5(json.new_password);
		 					
		 					delete json.new_password2;
		 				}
					});
				</script>
				
				</c:when>
				<c:when test="${param.action == 'modiflyPhone'}">
				<div class="test-pl">
					<aj-process-line ref="processLine" :items="['输入新号码', '完成']"></aj-process-line>
				</div>
				
				<form class="user-form center" action="${ctx}/user/info/modiflyPhone" method="POST" style="margin-left: 15%;">
					<dl>
						<label>
							<dt>手机</dt>
							<dd>
								<input type="text" name="phone" value="" placeholder="请输入手机" required />
								<div class="note">请输入您手机</div>
							</dd>
						</label>
					</dl>
					<dl>
						<dt></dt>
						<dd>
							<input type="submit" value="提交" /> <button onclick="location.assign('${ctx}/user/center/loginInfo/');">返回</button>
						</dd>
					</dl>
				</form>
				
				<script>
					ProcessLine = new Vue({el : '.test-pl'});
					
					aj.xhr.form('.user-form', function(json) {
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
				</c:when>
				<c:when test="${param.action == 'modiflyEmail'}">
				<div class="test-pl">
					<aj-process-line ref="processLine" :items="['填写新邮箱', '完成']"></aj-process-line>
				</div>
				
				<form class="user-form center" action="${ctx}/user/info/modiflyEmail" method="POST" style="margin-left: 15%;">
					<dl>
						<label>
							<dt>邮箱</dt>
							<dd>
								<input type="text" name="email" title="email" value="${user.email}" placeholder="请输入邮箱" required pattern="[^@]+@[^@]+\.[a-zA-Z]{2,6}" /> 
								<div class="note">请输入您邮箱</div>
							</dd>
						</label>
					</dl>
					<dl>
						<dt></dt>
						<dd>
							<input type="submit" value="提交" /> <button onclick="location.assign('${ctx}/user/center/loginInfo/');">返回</button>
						</dd>
					</dl>
				</form>
				
				<script>
					ProcessLine = new Vue({el : '.test-pl'});
					
					aj.xhr.form('.user-form', function(json) {
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
				</c:when>
				
				<c:when test="${param.action == 'modiflyUserName'}">
				<div class="test-pl">
					<aj-process-line ref="processLine" :items="['填写登录名信息', '完成']"></aj-process-line>
				</div>
				
				<form class="user-form center" action="${ctx}/user/info/modiflyUserName" method="POST" style="margin-left: 15%;">
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
						<dd>
							<input type="submit" value="提交" /> <button onclick="location.assign('${ctx}/user/center/loginInfo/');">返回</button>
						</dd>
					</dl>
				</form>
				
				<script>
					ProcessLine = new Vue({el : '.test-pl'});
					
					aj.xhr.form('.user-form', function(json) {
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
				</c:when>
				<c:otherwise>
					<ul class="list2">
						<li>	
							<a href="?action=resetPsw">修改密码</a>
							<div class="ok">设置密码</div><div>已设置</div>
						</li>
						<li>	
							<a href="?action=modiflyPhone">更换手机</a>
							<div class="${empty userPhone ? 'fail' : 'ok' }">绑定手机</div><div>${empty userPhone ? '未绑定手机' : userPhone}</div>
						</li>
						<li>	
							<a href="?action=modiflyEmail">设置邮箱</a>
							<div class="${empty email ? 'fail' : 'ok' }">绑定邮箱</div><div>${empty email ? '未绑定邮箱' : email}</div>
						</li>
						<li>	
							<a href="?action=modiflyUserName">修改登录名</a>
							<div class="${empty userName ? 'fail' : 'ok' }">登录名</div><div>${empty userName ? '未绑定邮箱' : userName}</div>
						</li>
					</ul>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>