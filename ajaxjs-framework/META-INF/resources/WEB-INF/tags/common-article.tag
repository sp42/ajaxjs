<%@tag pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<%@attribute name="left" required="false" fragment="true" description="插入左侧菜单内容"%>
<%@attribute name="type" required="true" type="String" description="标签类型，必填"%>
<%@attribute name="isOnlyCreateDate" required="false" type="Boolean" description="是否只显示创建日期"%>

<c:if test="${type == 'list'}">
<%@attribute name="listClass" required="fasle" type="String" description="列表样式类"%>
	<c:if test="${not empty PageResult}">
		<div class="${empty listClass ? 'aj-pc-list-1' : listClass}">
			<ul>
				<c:foreach items="${PageResult}" var="item">
					<li>
						<h3>
							<a href="${item.id}/">${item.name}</a>
						</h3>
						<p>${item.intro}……</p>
						<div>
							<c:dateFormatter value="${item.createDate}" />
							类别：<a href="?catelogId=${item.catelogId}">${catalogs[item.catelogId].name}</a>
							| <a href="${item.id}/">阅读更多»</a>
						</div>
					</li>
				</c:foreach>
			</ul>
			<%@include file="/WEB-INF/jsp/pager_tag.jsp"%>
		</div>
	</c:if> 
</c:if>

<c:if test="${type == 'info' && not empty info}">
	<div class="aj-article-info">
		<article>
			<h1>${info.name}</h1>
			<h4 class="createDate">
			<c:choose>
				<c:when test="${isOnlyCreateDate}">
					创建于：<c:dateFormatter value="${info.createDate}" />
				</c:when>
				<c:otherwise>
					文/${empty info.author ? '佚名' : info.author}  
					<c:if test="${not empty info.sourceUrl}"><a href="${info.sourceUrl}/" target="_blank">源网址</a></c:if>
					<c:if test="${not empty info.source}">出处： ${info.sourceUrl}</c:if>
					创建于：<c:dateFormatter value="${info.createDate}" /> 最后编辑 ：<c:dateFormatter value="${info.updateDate}" />
				</c:otherwise>
			</c:choose>
			</h4>
			${info.content.replaceAll('src="', 'src="../../')}
		</article>
		
		<!-- 相邻记录 -->
		<div class="neighborRecord">
			<c:if test="${not empty neighbor_pervInfo}">
				<a href="../${neighbor_pervInfo.url}/">上则记录：${neighbor_pervInfo.name}</a>
			</c:if>
				<br />
			<c:if test="${not empty neighbor_nextInfo}">
				<a href="../${neighbor_nextInfo.url}/">下则记录：${neighbor_nextInfo.name}</a>
			</c:if>
		</div>
		
	 	<!-- 版权声明 -->
		<div style="font-size: 10pt;color: gray;text-align: right;">
			<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/cn/">
				<img alt="知识共享许可协议" style="border-width:0;vertical-align: middle;" src="https://i.creativecommons.org/l/by-nc-sa/3.0/cn/88x31.png" />
			</a> 
			本作品采用<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/cn/">知识共享署名-非商业性使用-相同方式共享 3.0 中国大陆许可协议</a>进行许可。
		</div>	
		
		
		<table class="vue" style="float:right;margin:10px 0 20px 0;">
			<tr>
				<td>
					<!-- 百度分享按钮 http://share.baidu.com/ -->
					<div class="bdsharebuttonbox">
						<a href="#" class="bds_more" data-cmd="more">分享到：</a>
						<a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间">QQ空间</a>
						<a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博">新浪微博</a>
						<a href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博">腾讯微博</a>
						<a href="#" class="bds_renren" data-cmd="renren" title="分享到人人网">人人网</a>
						<a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信">微信</a>
					</div> 
					<!-- // -->
				</td>
				<td>
					<aj-adjust-font-size></aj-adjust-font-size>
				</td>
			</tr>
		</table>
		
		<!-- 来必力City版安装代码 -->
		<div id="lv-container" data-id="city" data-uid="MTAyMC80NDQ4OS8yMTAyMQ==">
			<script type="text/javascript">
		   (function(d, s) {
		       var j, e = d.getElementsByTagName(s)[0];
		
		       if (typeof LivereTower === 'function') { return; }
		
		       j = d.createElement(s);
		       j.src = 'https://cdn-city.livere.com/js/embed.dist.js';
		       j.async = true;
		
		       e.parentNode.insertBefore(j, e);
		   })(document, 'script');
			</script>
		<noscript> 为正常使用来必力评论功能请激活JavaScript</noscript>
		</div>
		<!-- City版安装代码已完成 -->
	</div>
	<script>
		new Vue({ el : '.vue' });
		window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"1","bdSize":"16"},"share":{"bdSize":16}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];
	</script>
</c:if>
