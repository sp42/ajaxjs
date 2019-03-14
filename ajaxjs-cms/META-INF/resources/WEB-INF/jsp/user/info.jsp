<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/jsp/common/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/main.less" />
</jsp:include>
</head>
<body>
	<%@include file="/WEB-INF/jsp/nav.jsp"%>
	
	<div class="center userCenter">
		<%@include file="user-center-menu.jsp"%>
		
		
		<div class="right">
			<a href="modifly" class="btn right">修改信息</a>
			<h3 class="jb">会员信息</h3>

			<form class="user-form center" action="${ctx}/user/add_information" method="post">
				<div class="formBox">
					<br /> <br />
					<div class="userAvatar">
						<img src="${empty userAvatar ? ctx.concat('/asset/images/avatar.png') : userAvatar}" />
						<br />
						<a href="avatar">修改头像</a>
					</div>
					<dl>
						<label>
							<dt>用户名</dt>
							<dd>${userName}</dd>
						</label>
						<label>
							<dt>用户姓名</dt>
							<dd>${user.name}</dd>
						</label>
						<label>
							<dt>用户性别</dt>
							<dd>${user.sex  == 1 ?'男':'女'}</dd>
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
	<%@include file="/WEB-INF/jsp/footer.jsp"%>
</body>
</html>