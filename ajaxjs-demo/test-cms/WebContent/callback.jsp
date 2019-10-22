<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
</jsp:include>
</head>
<body>

	登录成功。
	<script>
        var code = "<%=request.getParameter("code")%>";
        var state = "<%=request.getParameter("state")%>";
        console.log(code)
        console.log(state)
        aj.xhr.get('http://localhost:8080/shop/user/thirdpartrybinding/weixin', json => {
    		var result = JSON.stringify(json);
			console.log(result);
			window.opener.document.getElementById("weibo").value = result;// 把返回的数据传给父窗口的隐藏域中
			//window.opener.closeWeiboWin();// 授权完成后，关闭子窗口
        }, {
			code : code,
			state : state
		});

	</script>
</body>
</html>