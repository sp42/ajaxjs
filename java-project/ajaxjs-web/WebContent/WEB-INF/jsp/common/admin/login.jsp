<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<html>
	<head>
		<commonTag:head title="后台" lessFile="/asset/bigfoot/asset/less/user.less" />
		<style>
			.memberNav {
				display: none;
			}
		</style>
	</head>
	<body>
		<header>
			<div>
				<div class="memberNav">
					你好！
					<a href="memberCenter.jsp">${userName}</a>
					<a href="?action=logout">消息</a>
					<a href="?action=logout">积分</a>
					<a href="?action=logout">好友</a>
					<a href="?action=logout">收藏</a>
					<a href="?action=logout">退出</a>
				</div>
				<a href="../../../">${global_config.clientFullName}</a>
				
			</div>
		</header>
		<h1>欢迎使用${global_config.clientFullName}后台</h1>
		<section class="login center">
			<form action="?" method="POST" class="aj">
			<c:if test="${User.允许错误尝试次数超过则锁定() > 0 && 允许错误尝试次数超过则锁定 != null}">
				<span class="warning">登入超过尝试次数，请在十分钟之后再试 ${允许错误尝试次数超过则锁定}</span>
			</c:if>
				<label>
					用户名：
					<input type="text" name="name" data-singleTips="请输入用户名" value="admin" />
				</label>
				<label>
					密码：
					<input type="password" name="password" value="admin" />
				</label>
<%-- 			<c:if test="${User.showCaptchaWhenLoginFailed() == 0 || showCaptchaWhenLoginFailed}"> --%>
				<!-- 验证码 -->
				<label class="captchaCode">验证码：
					<img src="../captchaImg/do.do" onclick="this.src=this.src + '?' + new Date;" />
					<input type="text" name="captchaImgCode" /> 
				</label>
<%-- 			</c:if> --%>
				<div>
					&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="登录" /> &nbsp;&nbsp;
				</div>
				<div>
					&nbsp;&nbsp;&nbsp;&nbsp;<a href="">用微博账号登入</a> &nbsp;&nbsp;<a href="">用 QQ 账号登入</a>
				</div>
				<div>
					&nbsp;&nbsp;&nbsp;&nbsp;<a href="?action=forgotPassword">忘记密码？</a> 还没有帐号？<a href="?action=register">立即注册</a>
				</div>
			</form>
		</section>
		<br />
		<br />
		<br />
		
		<script>
			var form = new bf_form(document.querySelector('form'), {
				isCommonAfterSubmit : true
			});
			// 成功登录后的跳转
			form.on('afterSubmit', function(el, json){
				if(json.isOk) {
					location.assign('${pageContext.request.contextPath}/admin/');
				}
			});
		</script>
	</body>
</html>