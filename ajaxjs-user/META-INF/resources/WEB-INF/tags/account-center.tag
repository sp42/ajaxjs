<%@tag pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/main.less" />
	</jsp:include>
</head>
<body class="account-center">
	<div class="center">
		<div class="left">
			<div class="avatar">
				<img src="https://static001.geekbang.org/account/avatar/00/13/d3/bd/c7f292ed.jpg" />
				<h3>${empty info ? userName : info.username}</h3>
			</div>
			<menu>
				<ul>
					<li><a href="${ctx}/user/center/home/">概 览</a></li>
					<li><a href="${ctx}/user/center/info/">用户信息</a></li>
					<li><a href="${ctx}/user/center/loginInfo/">账户安全</a></li>
					<li><a href="${ctx}/user/center/feedback/">账户绑定</a></li>
					<li><a href="${ctx}/user/login/logout/">登录历史</a></li>
				</ul>
			</menu>
			<script>
				// find current hightlight
				var arr = document.location.href.split('/');
				if('' === arr[arr.length -1]) {
					arr.pop();
				}
				
				var last = arr.pop(), path = arr.pop() + '/' + last; 
				var a = document.querySelectorAll('menu ul li a');
				
				for(var i = 0, j = a.length; i < j; i++) {
					if(document.location.href.indexOf(a[i].href) != -1) {
						a[i].parentNode.classList.add('selected');
						break;
					}
				}
			</script>
		</div>
		<div class="right">
			<jsp:doBody/>
		</div>
	</div>
</body>
</html>