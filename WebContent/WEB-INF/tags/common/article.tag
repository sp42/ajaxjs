<%@tag pageEncoding="UTF-8" description="页面小功能" import="java.util.HashMap"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@attribute name="type" type="String" required="false" description="哪一个模块"%>  
<%
 	if("share".equals(type)) {
 		String title = "";
 		int serverPort = request.getServerPort();
 		String currentPage_url = request.getScheme() + "://" + request.getServerName();
 		if (serverPort != 80) {
 			currentPage_url += ":" + serverPort;
 		}
 		currentPage_url += request.getRequestURI() + "?" + request.getQueryString();
 %>
		<div class="share" style="margin-top:10px;margin-right:5px;font-size: .8rem;">
		分享到 &nbsp;&nbsp;
			<a title="转发至QQ空间" id="s_qq"
				href="http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=<%=currentPage_url%>"
				target="_blank"><img
				src="http://static.youku.com/v1.0.0691/v/img/ico_Qzone.gif" /></a>
			<a title="转发至新浪微博" id="s_sina"
				href="http://v.t.sina.com.cn/share/share.php?appkey=2684493555&url=<%=currentPage_url%>&title=<%=title%>&Uid=&source=&sourceUrl="
				target="_blank"><img
				src="http://static.youku.com/v1.0.0691/v/img/ico_sina.gif" /></a>
			<a title="分享到腾讯朋友" id="s_pengyou"
				href="http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?to=pengyou&url=<%=currentPage_url%>&title=%<%=title%>" 
				target="_blank"><img
				src="http://static.youku.com/v1.0.0691/v/img/ico_pengyou.png" /></a>
			<a title="推荐到豆瓣" id="s_douban"
				href="http://www.douban.com/recommend/?url=<%=currentPage_url%>&title=<%=title%>" 
				target="_blank"><img
				src="http://static.youku.com/v1.0.0691/v/img/ico_dou_16x16.png" /></a>
		</div>	
<%
	}else if("adjustFontSize".equals(type)){
%>
	<div class="adjustFontSize">
		<span>字体大小</span>
		<ul>
			<li>
				<label>
					<input type="radio" name="fontSize" class="小" /> 小
				</label>
			</li>
			<li>
				<label>
					<input type="radio" name="fontSize" class="中" /> 中
				</label>
			</li>
			<li>
				<label>
					<input type="radio" name="fontSize" class="大" /> 大
				</label>
			</li>
			<li></li>
		</ul>

	</div>
	<style type="text/css">
		.adjustFontSize{
			width:210px; 
			font-size:.8rem;  
			padding:2px 0;
		}
		.adjustFontSize span{
			float:left;
			width:35%;
			display:block;
		}
		.adjustFontSize ul{
			width:65%;
			float:right;
		}
		.adjustFontSize ul li{
			cursor: pointer!important;
			display:block;
			float:right;
			width:33%; 
		}
		.adjustFontSize label{
		}
	</style>

	<script>
		// 打印页面
		function printContent() {
      		var printHTML = "<html><head><title></title><style>body{padding:2%};</style></head><body>";
      		printHTML +=  document.querySelector('article').innerHTML;
      		printHTML += "</body></html>";
	        var oldstr = document.body.innerHTML;
	        document.body.innerHTML = printHTML;
	        window.print();
	        document.body.innerHTML = oldstr;
		}
		/**
		 * 调整字体大小
		 */
		document.querySelector('.adjustFontSize ul').onclick = function (e){
			var el = e.target, target = el.innerHTML || el.className;
			if(target.indexOf('大') != -1){
				setFontSize('12pt');
			}else if(target.indexOf('中') != -1){
				setFontSize('10.5pt');
			}else if(target.indexOf('小') != -1){
				setFontSize('9pt');
			}
		};
	
		function setFontSize(fontSize){
			var p = document.querySelectorAll('article p');
			if (!p || !p.length)throw '未发现目标段落！';
			
			for (var i = 0, j = p.length; i < j; i++)
				p[i].style.fontSize = fontSize;
		}
	
		/**
		 * 发送邮件
		 * @TODO base64 encode
		 */
		function sendMail_onClick() {
			location.href = 'mailto:xxx@tagzine.com?subject=\u63A8\u8350\u65B0\u95FB: '
					+ document.title
					+ '&body=\u6211\u5411\u4F60\u63A8\u8350\u8FD9\u6761\u6587\u7AE0\uFF0C\u5E0C\u671B\u4F60\u559C\u6B22\uFF01\u6587\u7AE0\u6807\u9898\uFF1A'
					+ document.title
					+ '\u3002\u8BF7\u70B9\u51FB\u67E5\u770B\uFF1A ' + location.href;
		}
	</script>
<%
	}else if("function".equals(type)) {
%>
		<a href="javascript:printContent();"><span style="font-size:1rem;">&#12958;</span>打 印</a>
		<a href="javascript:sendMail_onClick();"><span style="font-size:1rem;">&#9993;</span>发送邮件</a>
		<a href="javascript:;"><span style="font-size:1.2rem;">★ </span>收 藏</a>
<!-- 	&#8984;	❂ -->
<%
	}else if("toggleBtn".equals(type)) {
%>
		<div class="closeBtn"></div>
		<script>
			/**
			 * 初始化展开闭合器
			 * @return {[type]} [description]
			 */
			initExpander = function (cfg){
				var container = cfg.container, moreBtn = cfg.btn;
				var contentHeight = container.scrollHeight;
	
				var state = {
					expended : true
				}
				moreBtn.onclick = function(){
					// debugger;
					if(!state.expended){
						// moreBtn.innerHTML = '收起';
						if(cfg.beforeExpand && cfg.beforeExpand() != false)
							container.style.height = (contentHeight + 45) + 'px';
					}else{
						// moreBtn.innerHTML = '更多';
						if(cfg.beforeClose && cfg.beforeClose() != false)
							container.style.height = '32px';
					}
	
					state.expended = !state.expended;
				}
			}
	
		</script>
		<jsp:doBody />

<%
	} else {
%>

	<article> 
		<h3>${info.name}</h3>
		<h4>${info.createDate}</h4>
		${info.content}
	</article> 
	
	<div style="padding: 20px 0;" align="right">
		<!-- 分享功能 -->
		<commonTag:article type="share" />
		<!-- 调整页面字号 -->
		<commonTag:article type="adjustFontSize" />
		<!-- 页面功能 -->
		<commonTag:article type="function" />
	</div>
<%
	}
%>
