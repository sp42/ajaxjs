<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<title>登录后台管理</title>
		<%@ include file="pages/common.jsp" %>
		<style>
            html,
            body {
               height: 100%;
                overflow: hidden;
            }
		</style>
	</head>

	<body>
		<table width="100%" style="height:100%;">
		    <tr>
		        <td align="center" valign="middle">
                    <fieldset class="fieldset-box" style="width:350px;">
                        <legend>用户登录</legend>

                        <input class="text-input-1" type="text" name="userName" placeholder="用户名" />
                        <br />
                        <br />
                        <input class="text-input-1" type="password" name="password" placeholder="密码" />
                        <br />
                        <br />
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
	// http://www.ajaxjs.com/public/temp.js
	function login() {
        let userName = document.querySelector('*[name=userName]').value;
        let password = document.querySelector('*[name=password]').value;

        aj.xhr.postForm('user', { userName: userName, password: password }, json => {
            if (json.status) {
                localStorage.setItem("accessToken", json.data.accessToken);
                localStorage.setItem("userInfo", JSON.stringify(json.data.userInfo));

                location.assign('admin.jsp');
            }
        });
    }
	</script>
</html>