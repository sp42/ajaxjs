<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
</jsp:include>
</head>
<body class="userLogin">
	<h3 class="jb">欢迎登录</h3>
	<div class="tab">
		<form class="user-form center" action="${ctx}/user/login/" @submit="onSubmit" method="POST" style="width: 50%;">
			<dl>
				<label>
					<dt>手机号码:</dt>
					<dd>
						<input type="text" name="phone" placeholder="请输入手机号 " value="13711228150" />
						<div class="note">请输入中国大陆手机号码</div>
					</dd>
				</label>
				<label>
					<dt>
						<br />登录密码:
					</dt>
					<dd>
						<input type="password" name="password" placeholder="请输入登录密码 "
							style="width: 50%;" data-erruileft="200" value="123456"
							pattern="[0-9A-Za-z]{6,10}" />
						<div class="note">
							请输入您的由6~10位由数字和<br />26个英文字母的登录密码
						</div>
					</dd>
				</label>
				<label>
					<dt>验证码:</dt>
					<dd class="captcha">
						<aj-page-captcha field-name="captchaImgCode"></aj-page-captcha>
					</dd>
				</label>
			</dl>
			<dl>
				<dt></dt>
				<dd>
					<button>登录</button>
				</dd>
			</dl>
			<dl>
				<dt></dt>
				<dd class="bottomLink">
					<a href="../register/">没有账号？点击注册</a>
				</dd>
			</dl>
		</form>
	</div>
 
	<script src="${ajaxjs_ui_output}/lib/md5.min.js"></script>
	<script>
		var form = aj('form.user-form');

		new Vue({
			el : '.tab',
			data : {
				selected : 0
			},
			methods : {
				onSubmit : function() {
					var el = aj('input[name=password]');
					el.value = md5(el.value);
				}
			}
		});
		// 表单验证
		// 		new ajaxjs.formValid(form, {
		// 			alignRight : true
		// 		});

		// AJAX 表单提交
		// 		ajaxjs.xhr.form(form, function(json) {
		// 			if (json && json.isOk) {
		// 				ajaxjs.alert('登录成功！', {
		// 					afterClose : function() {
		// 						location.reload();
		// 					}
		// 				});
		// 			} else {
		// 				ajaxjs.alert(json.msg);
		// 			}
		// 		}, {
		// 			beforeSubmit : function(form, json) {
		// 				json.password = md5(json.password);
		// 			}
		// 		});
	</script>
</body>
</html>