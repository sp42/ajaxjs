<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@ taglib uri="/ajaxjs" prefix="c" %>
<%
	String action = request.getParameter("action");
%>
<html>
	<head>
		<commonTag:head title="后台" lessFile="/asset/less/admin.less" />
		<style>
			.memberNav{
				display:none;
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
	<%if(action == null) {%>
		<h1>欢迎使用${global_config.clientFullName}后台</h1>
		<section class="login center">
			<form action="${pageContext.request.contextPath}/service/user/access/login" method="POST" class="aj">
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
					<img src="captchaImg/show.do?1" onclick="this.src=this.src + '?' + new Date;" />
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
		<%}else if(action.equals("forgotPassword")){ %>
		
			<section class="forgotPassword">
				<form action="index.jsp" method="POST">
					<label>
						Username：
						<input type="text" name="name" />
					</label>
					<!-- 验证码 -->
					<label class="captchaCode">
						Captcha Code：
						<img src="captchaImg/show.do?2" onclick="this.src=this.src + '?' + new Date;" />
					</label>
					<input type="submit" value="查找密码" />
				</form>
			</section>
		<%}else if(action.equals("register")){ %>
			<h1>Register</h1>
			<section class="register center">
				<form action="index.jsp" method="POST">
					<label>
						<span class="required-note">*</span>用户名：
						<input type="text" name="name" placeholder="用户名" 
							required
							data-regexp="Username"
							data-note="用户名等于账号名；不能与现有的账号名相同；注册后不能修改；"
							data-note2="用户名22等于账号名；不能与现有的账号名相同；<br >注册后不能修改；"
						 />
					</label>
					<label>
						<span class="required-note">*</span>电子邮件：
						<input type="text" name="name" placeholder="请输入电子邮件" 
							data-regexp="email"
							data-note="邮件可用于登录和查找密码等；不能与现有的邮件相同；注册后可以修改；" 
							data-note2="推荐使用国内邮箱，如 QQ、163 等的邮箱" />
					</label>
					<label>
						<span class="required-note">*</span>登录密码 :
						<input type="password" name="password" />
					</label>
					<label>
						<span class="required-note">*</span>确认密码 :
						<input type="password" name="password" />
					</label>
					<!-- 验证码 -->
					<label class="captchaCode">
						<span class="required-note">*</span>验证码：
						<img src="../public/Captcha.jsp" onclick="this.src=this.src + '?' + new Date;" />
						<input type="text" data-regexp="integer" required /> 
					</label>
					<div>
						<label>
    	                	<input id="agree" class="agree" name="agree" checked="checked" style="width:20px;height:12px" type="checkbox" /> 
	                    	我已仔细阅读并接受<a href="misc/registration-policy.jsp" target="_blank">注册条款</a>
						</label>
					</div>
					<div> 
						&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" value="注 册" /> &nbsp;&nbsp;&nbsp;&nbsp;<a href="?">现有账号登录</a>
					</div>
				</form>
			</section>
		<%} %>
		<div class="single_tips hide">
			<div class="arrow"></div>
			<span></span>
		</div>
		<script>
			FormData.initTips('form');
		</script>
	</body>
</html>