<%@page pageEncoding="UTF-8"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tags"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/admin.less" />
		<jsp:param name="title" value="&#x540E;&#x53F0;&#x7BA1;&#x7406;" />
	</jsp:include>
	<style>
		body {
			background: url('https://static-163yun.ajaxjs.com/images/admin-user-login/dynamic-style.png') repeat fixed;
		}
		
		.form {
			margin: 0 auto;
			background: rgba(0, 0, 0, 0) url('https://static-163yun.ajaxjs.com/images/admin-user-login/wrap.png') no-repeat;
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
			background: rgba(0, 0, 0, 0) url('https://static-163yun.ajaxjs.com/images/admin-user-login/login-top-title.png') no-repeat scroll 0% 0%;
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
		<h2>${aj_allConfig.site.titlePrefix}<br/> ${aj_allConfig.System.name}</h2>

		<tags:user type="login" isAdminLogin="true" />
		
	</div>
	<footer>
		<div>版权所有：${aj_allConfig.clientFullName}</div>
	</footer>
</body>
</html>
