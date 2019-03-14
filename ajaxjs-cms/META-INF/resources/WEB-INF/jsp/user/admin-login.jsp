<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/jsp/common/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
	<jsp:param name="title" value="&#x540E;&#x53F0;&#x7BA1;&#x7406;" />
</jsp:include>
<style>
body {
	background:
		url('${ctx}/asset/common/images/admin-user-login/dynamic-style.png')
		repeat fixed;
}

.form {
	margin: 0 auto;
	background: rgba(0, 0, 0, 0)
		url('${ctx}/asset/common/images/admin-user-login/wrap.png') no-repeat;
	width: 436px;
	height: 950px;
	position: relative;
}

h2 {
	font-size: 1.4rem;
	padding: 30% 0 5% 10%;
}

.form .box-title-login {
	position: absolute;
	top: 55px;
	left: 7px;
	width: 126px;
	height: 49px;
	background: rgba(0, 0, 0, 0)
		url('${ctx}/asset/common/images/admin-user-login/login-top-title.png')
		no-repeat scroll 0% 0%;
}

footer {
	position: fixed;
	bottom: 0;
	left: 0; height：30px;
	text-align: center;
	line-height: 2.5;
	color: rgb(153, 153, 153);
	background-color: rgba(0, 0, 0, 0.8);
	width: 100%;
}

form>table>tbody>tr>td {
	padding: 5% 0;
}

button{
	width:80%;
	height:40px!important;
}
</style>
</head>
<body>
	<div class="form">
		<div class="box-title-login"></div>
		<h2>${aj_allConfig.site.titlePrefix} 后台管理系统</h2>
		<form class="form-1" action="${ctx}/user/login" method="POST">
			<table align="center">
				<tr>
					<td>用户名&nbsp;&nbsp; </td>
					<td><input type="text" name="name" placeholder="请输入用户名" /></td>
				</tr>
				<tr>
					<td>密 &nbsp;&nbsp;码&nbsp;&nbsp; </td>
					<td><input type="password" name="password" placeholder="请输入用户密码" /></td>
				</tr>
				<tr>
					<td>验证码&nbsp;&nbsp; </td>
					<td><aj-page-captcha field-name="captchaImgCode"></aj-page-captcha></td>
				</tr>
				<tr>
					<td></td>
					<td>
						<button>立即登录</button>
					</td>
				</tr>
			</table>
		</form>
		<script src="${ajaxjs_ui_output}/lib/md5.min.js"></script>
		<script>
			// 表单提交
			aj.xhr.form('form', function(json) {
				if (json && json.msg) {
					if(json.isOk)
						aj.alert.show(json.msg, {
							afterClose(){
								location.assign("../");
							}
						});
					else {
						aj.alert.show(json.msg);
						// 每次失败后要刷新验证码
						TABLE.$children[0].refreshCode();
					}
				}
			}, {
				beforeSubmit(form, json) {
					var el = aj('input[name=password]');
					json.password = md5(el.value);
				}
			});
			
			TABLE = new Vue({el:'table'});
		</script>
	</div>
	<footer>
		<div>版权所有：${aj_allConfig.clientFullName}</div>
	</footer>
</body>
</html>
