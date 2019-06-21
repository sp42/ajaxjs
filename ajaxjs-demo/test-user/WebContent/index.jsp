<%@ page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/jsp/head.jsp">
	<jsp:param name="lessFile" value="/asset/less/admin.less" />
</jsp:include>
</head>
<body>
	<input type="hidden" id="qq" value="">
	<a href="#" onClick="loginQQ();">QQ登录</a>

	<br>
	<br>
	<hr>
	<br>

	<input type="hidden" id="weibo" value="" />
	<a href="#" onClick="loginWeibo();"><img src="http://www.sinaimg.cn/blog/developer/wiki/240.png" /></a>

	<script type="text/javascript">
		var qqAuthWin, weiboAuthWin;

		/**
		 * 关闭QQ子窗口
		 */
		function closeQQWin() {
			var result = qq.value;
			if (result != "") {
				console.log(result);
				qqAuthWin.close();
			} else {
				console.log("值为空");
			}
		}

		/**
		 * QQ登录
		 * http://localhost:9090/logback/qq.jsp  QQ互联上设置的回调地址
		 */
		function loginQQ() {
			qqAuthWin = window.open(
				"https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101641370&state=register&redirect_uri=http://localhost:8080/test-user/qq/", 
				'QQ授权登录', 
				'width=770,height=600,menubar=0,scrollbars=1,resizable=1,status=1,titlebar=0,toolbar=0,location=1'
			);
		}

		/**
		 * 关闭微博子窗口
		 */
		function closeWeiboWin() {
			var result = weibo.value;
			if (result != "") {
				console.log(result);
				weiboAuthWin.close();
			} else {
				console.log("值为空");
			}
		}

		/**
		 * 微博登录
		 * http://localhost:9090/logback/weibo.jsp这个就是在微博上设置的回调地址
		 */
		function loginWeibo() {
			weiboAuthWin = window.open(
				"https://api.weibo.com/oauth2/authorize?client_id=360568732&response_type=code&state=register&redirect_uri=http://localhost:8080/shop/callback.jsp", 
				'微博授权登录', 
				'width=770,height=600,menubar=0,scrollbars=1,resizable=1,status=1,titlebar=0,toolbar=0,location=1'
			);
		}
	</script>
</body>
</html>