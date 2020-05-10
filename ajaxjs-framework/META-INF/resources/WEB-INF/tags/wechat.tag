<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" description="微信公众号用标签"%>
<%@taglib uri="/ajaxjs" 		prefix="c"%>
<%@attribute name="type" required="true" type="String" description="标签类型"%>

<%if("share".equals(type)){ %>
	<%@tag import="com.ajaxjs.weixin.open_account.JsSdkApi"%>
	<script src="https://res.wx.qq.com/open/js/jweixin-1.6.0.js"></script>
<%
	com.ajaxjs.weixin.open_account.JsSdkApi.init(request);
%>
	${JsSdkApi.init(pageContext.request)}
	
	<script>
		wx.config({
		  debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		  appId: '${aj_allConfig.wx_open.appId}', // 必填，公众号的唯一标识
		  timestamp: ${map.timestamp}, // 必填，生成签名的时间戳
		  nonceStr: '${map.noncestr}', // 必填，生成签名的随机串
		  signature: '${map.signature}',// 必填，签名
		  jsApiList: ['updateAppMessageShareData', 'updateTimelineShareData'] // 必填，需要使用的JS接口列表
		});
		
		wx.ready(function() {
			console.log("ok");
	
			wx.updateAppMessageShareData({ 
			    title: 'test', // 分享标题
			    desc: 'hihi', // 分享描述
			    link: '${map.url}', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
			    imgUrl: 'http://www.palate-movement.com/asset/images/logo.png', // 分享图标
			    success: function () {
			    	console.log("分享成功");
			    }
			  });
			wx.updateTimelineShareData({ 
			    title: 'test', // 分享标题
			    link: '${map.url}', // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
			    imgUrl: 'http://www.palate-movement.com/asset/images/logo.png', // 分享图标
			    success: function () {
			    	console.log("分享成功");
			    }
			  });
		});
	
		wx.error(function(res) {
			console.log(res);
		});
	</script>
<%}%>