<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>统一用户管理</title>
		<%@ include file="../pages/common.jsp" %>
		<script src="https://challenges.cloudflare.com/turnstile/v0/api.js?onload=onloadTurnstileCallback" defer></script>
		<style>
            html,
            body {
               height: 100%;
               overflow: hidden;
            }
		</style>
		<script>
            window.onloadTurnstileCallback = function () {
                turnstile.render('#captcha-el', {
                    sitekey: '0x4AAAAAAAA2NqTaXauncxkr',
                    callback: function(token) {
                        console.log(`Challenge Success ${token}`);
                    },
                });
            };
        </script>
	</head>

	<body>
		<table width="100%" style="height:100%;">
		    <tr>
		        <td align="center" valign="middle">
                    <fieldset class="fieldset-box" style="width:350px;">
                        <legend>统一用户管理</legend>

                        <input class="text-input-1" type="text" name="userName" placeholder="用户名" />
                        <br />
                        <br />
                        <input class="text-input-1" type="password" name="password" placeholder="密码" />
                        <br />
                        <br /><div id="captcha-el"></div>
                        <br />
                        <button class="button-1" onclick="login();return false;">登录</button>
                    </fieldset>
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
                    <br />
		        </td>
		    </tr>
		</table>
	</body>
	<script>
        function login() {
            let userName = document.querySelector('*[name=userName]').value;
            let password = document.querySelector('*[name=password]').value;

            aj.xhr.postForm('../user/login', { loginId: userName, password: password }, json => {
                if (json.status) {
                    location.assign('../oidc/authorization' + location.search);
                }
            });
        }
	</script>
</html>