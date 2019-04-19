<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
</jsp:include>
</head>
<%!

%>
<body>
	<div class="center userCenter">
		<%@include file="user-center-menu.jsp"%>
		<div class="right">
			<h3 class="jb">会员信息</h3>
			<form class="user-form">
				<div class="formBox">
					<br /> <br />
					<div class="userAvatar">
						<img src="${empty userAvatar ? ctx.concat('/asset/common/images/avatar.png') : userAvatar}" />
						<br />
						<a href="avatar">修改头像</a>
						<br />
						<br />
						<button onclick="location.assign('modifly');return false;">修改信息</button>
						<br />
						<br />
						<button onclick="location.assign('modifly');return false;">绑定设置</button>
						<br />
						<br />
						<button onclick="location.assign('modifly');return false;">账号注销</button>
					</div>
					<dl>
						<label>
							<dt>用户 id</dt>
							<dd>${empty info ? userId : info.id}</dd>
						</label>
						<label>
							<dt>用户账户名</dt>
							<dd>${empty info ? name : info.name}</dd>
						</label>
						<label>
							<dt>用户姓名</dt>
							<dd>${empty info ? userName : info.username}</dd>
						</label>
						<label>
							<dt>用户性别</dt>
							<dd>${empty info ? (user.sex == 1 ?'男': (user.sex == 2 ?'女':'未知')) : info.getSexText()}</dd>
						</label>
						<label>
							<dt>出生日期</dt>
							<dd>${user.birthday}</dd>
						</label>

						<label>
							<dt>所在地区</dt>
							<dd>${user.name}</dd>
						</label>
						
						<label>
							<dt>用户手机</dt>
							<dd>${userPhone}</dd>
						</label>
						<label>
							<dt>电子邮件</dt>
							<dd>${user.email}</dd>
						</label>
					</dl>
				</div>
			</form>
		</div>
	</div>
	<script>
		new Vue({
			el : '.calendar'
		});
		var add_information = document.querySelector('form.add_information');
		// AJAX 表单提交
		ajaxjs.xhr.form(add_information, ajaxjs.xhr.defaultCallBack);
	</script>
</body>
</html>