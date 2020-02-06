<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@taglib uri="/ajaxjs" 		prefix="c" %>
<%@taglib uri="/ajaxjs_config"	prefix="config"%>
<%@taglib tagdir="/WEB-INF/tags/" prefix="tagfile"%>

<%@attribute name="type" required="true" type="String" description="标签类型"%>


<%

	// 要写 java 所以不用 jstl tag
	if("footer".equals(jspContext.getAttribute("type"))) {
		
		if (request.getAttribute("requestTimeRecorder") != null) {
			long requestTimeRecorder = (long) request.getAttribute("requestTimeRecorder");
			long end = System.currentTimeMillis() - requestTimeRecorder;
			float _end = (float) end;
			request.setAttribute("requestTimeRecorder", _end / 1000);
		}
		
		request.setAttribute("FOOTER_YEAR", java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
%>

	<!-- 通用页面底部 -->
	<footer>
		<div class="top">
			<div>
				<div class="btn" onclick="aj('footer .top').classList.toggle('close');"></div>
			</div>
			${SITE_STRU.getSiteMap(pageContext.request)}
		</div>
	
		<div class="copyright">
		<div>
			<div class="right">
<%-- 				<a href="#"> <img src="${commonAsset}/images/gs.png" height="40" /></a> 
				<a href="#"> <img src="${commonAsset}/images/kexin.png" hspace="20" width="90" style="margin-top: 15px;" /></a>  --%>
				<a href="#"> <img src="${commonAsset}/images/360logo.gif" width="90" style="margin-top: 15px;" /></a> 
			</div>
			<a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a> / <a href="javascript:;" class="Chinese" onclick="toChinese(this);">正体中文</a>
			<script src="${ajaxjs_ui_output}/lib/Chinese.js"></script>
			<br /> 
			
			${empty aj_allConfig.site.site_icp ? '粤ICP备15007080号-2' : aj_allConfig.site.site_icp}
			Powered by <a target="_blank" href="http://framework.ajaxjs.com">AJAXJS</a>
			<br />
		
			©Copyright ${FOOTER_YEAR} 版权所有， ${aj_allConfig.clientFullName} &nbsp; ${empty requestTimeRecorder ? '' : '请求完成耗时：'.concat(requestTimeRecorder).concat('秒') }
		</div>
		</div>
	</footer>
<%}%>


<c:if test="${type == 'list'}">
	<%@attribute name="listClass" required="fasle" type="String" description="列表样式类"%>
	<%@attribute name="catalogs"  required="fasle" type="java.util.Map" description="类别"%>
	<c:if test="${not empty PageResult}">
		<div class="${empty listClass ? 'aj-pc-list-1' : listClass}">
			<ul>
				<c:foreach items="${PageResult}" var="item">
					<li>
						<h3>
							<a href="${item.id}/">${item.name}</a>
						</h3>
						<c:if test="${not empty item.cover}">
							<a href="${item.id}/">
								<img src="${item.cover.startsWith('http') ? item.cover : ctx.concat('/images/').concat(item.cover)}" />
							</a>
						</c:if>
						<p>${item.intro}……</p>
						<div>
							<c:dateFormatter value="${item.createDate}" format="YYYY-MM-dd" />
							类别：<a href="?catalogId=${item.catalogId}">${catalogs[item.catalogId].name}</a>
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
	<%@attribute name="isOnlyCreateDate" required="false" type="Boolean" description="是否只显示创建日期"%>
	<%@attribute name="showPageHelper" required="false" type="Boolean" fragment="false" description="是否显示分享、调整字体大小"%>
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
					<c:if test="${not empty info.source}">出处/${info.source}</c:if>
					<c:if test="${not empty info.sourceUrl}"><a href="${info.sourceUrl}" target="_blank">源网址 &#128279;</a></c:if>
					<br />
					创建于/<c:dateFormatter value="${info.createDate}" format="YYYY-MM-dd" /> 最后编辑/<c:dateFormatter value="${info.updateDate}" format="YYYY-MM-dd" />
				</c:otherwise>
			</c:choose>
			</h4>
			${info.content.replaceAll('src="[^http]', 'src="../../')}
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
		
		<c:if test="${showPageHelper}">	
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
		</c:if>
	</div>
	<script>
		new Vue({ el : '.vue' });
		window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"1","bdSize":"16"},"share":{"bdSize":16}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];
	</script>
</c:if>
