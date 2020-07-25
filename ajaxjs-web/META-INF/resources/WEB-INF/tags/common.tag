<%@tag pageEncoding="UTF-8" import="com.ajaxjs.framework.config.ConfigService" trimDirectiveWhitespaces="true" %>
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
					<a href="#"> <img src="${ajaxjs_ui_output}/images/360logo.gif" width="90" style="margin-top: 15px;" /></a> 
				</div>
				<a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a> / <a href="javascript:;" class="Chinese" onclick="toChinese(this);">正体中文</a>
				<script src="${ajaxjs_ui_output}/lib/Chinese.js"></script>
				<br /> 
				
				<a target="_blank" href="http://beian.miit.gov.cn">${empty aj_allConfig.site.site_icp ? '粤ICP备15007080号-2' : aj_allConfig.site.site_icp}</a>
				<!-- Powered by <a target="_blank" href="http://framework.ajaxjs.com">AJAXJS</a> -->
				<br />
			
				©Copyright ${FOOTER_YEAR} 版权所有， ${aj_allConfig.clientFullName} &nbsp; ${empty requestTimeRecorder ? '' : '请求完成耗时：'.concat(requestTimeRecorder).concat('秒') }
			</div>
		</div>
		<c:if test="${not empty aj_allConfig.baidu_tongji.scriptId}">
			<!-- 百度网站统计 -->
			<script>
				var _hmt = _hmt || [];
				(function() {
				  var hm = document.createElement("script");
				  hm.src = "https://hm.baidu.com/hm.js?${aj_allConfig.baidu_tongji.scriptId}";
				  var s = document.getElementsByTagName("script")[0]; 
				  s.parentNode.insertBefore(hm, s);
				})();
			</script>
		</c:if>
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
								<div class="thumb">
									<img src="${item.cover.startsWith('http') ? item.cover : aj_allConfig.uploadFile.imgPerfix.concat(item.cover)}" />
								</div>
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
		</div>
	</c:if> 
</c:if>

<%-- 缩略图 --%>
<c:if test="${type == 'thumb'}">
	<%@attribute name="thumb" required="false" type="String" description="缩略图"%>
	<c:if test="${not empty thumb}">
		<a href="${aj_allConfig.uploadFile.imgPerfix}${thumb}" target="_blank">
			<img src="${thumb.startsWith('http') ? thumb : aj_allConfig.uploadFile.imgPerfix.concat(thumb)}" style="max-width:50px;max-height:60px;vertical-align: middle;" 
		 		onmouseenter="aj.widget.imageEnlarger.singleInstance.imgUrl = '${thumb.startsWith('http') ? thumb : aj_allConfig.uploadFile.imgPerfix.concat(thumb)}';" onmouseleave="aj.widget.imageEnlarger.singleInstance.imgUrl = null;" />
		</a>
	</c:if>
</c:if>

<c:if test="${type == 'info' && not empty info}">
	<%@attribute name="isOnlyCreateDate" required="false" type="Boolean" description="是否只显示创建日期"%>
	
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
					${not empty catalogs ? '<br />分类/': ''} ${not empty catalogs ? catalogs[info.catalogId].name : ''}
					<c:if test="${not empty info.source}">出处/${info.source}</c:if>
					<c:if test="${not empty info.sourceUrl}"><a href="${info.sourceUrl}" target="_blank"> 源网址 &#128279;</a></c:if>
					<br />
					创建于/<c:dateFormatter value="${info.createDate}" format="YYYY-MM-dd" /> 最后编辑/<c:dateFormatter value="${info.updateDate}" format="YYYY-MM-dd" />
				</c:otherwise>
			</c:choose>
			</h4>
			
			${info.content.replaceAll('src="[^http]', 'src="../../i')}
		</article>
		<jsp:doBody />
		
<c:if test="${ConfigService.getValueAsBool('domain.article.attachmentDownload') && not empty info.attachment}">
		<fieldset class="aj-fieldset">
			<legend>附件下载</legend>
			<ul>
				<c:foreach items="${info.attachment}">
					<li style="padding:1%;">
						<a href="${ctx}/images/${item.name}" download>${item.name}</a> | ${item.fileSize}kb
					</li>
				</c:foreach>
			</ul>
		</fieldset>		
</c:if>		

<c:if test="${ConfigService.getValueAsBool('domain.article.neighborRecord')}">
		<!-- 相邻记录 -->
		<div class="neighborRecord">
			<c:if test="${not empty neighbor_pervInfo}">
				<a href="../${neighbor_pervInfo.id}/">上则记录：${neighbor_pervInfo.name}</a>
			</c:if>
				<br />
			<c:if test="${not empty neighbor_nextInfo}">
				<a href="../${neighbor_nextInfo.id}/">下则记录：${neighbor_nextInfo.name}</a>
			</c:if>
		</div>
</c:if>
	 	<!-- 版权声明 -->
		<div style="font-size: 10pt;color: gray;text-align: right;">
			本作品采用<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/3.0/cn/">知识共享署名-非商业性使用-相同方式共享 3.0 中国大陆许可协议</a>进行许可。
		</div>	
		
		
<c:if test="${ConfigService.getValueAsBool('domain.article.pageHelper')}">		
			<table class="aj-pageHelper" style="float:right;margin:10px 0 20px 0;">
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
			<script> 
				new Vue({el : '.aj-pageHelper'});
				window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"1","bdSize":"16"},"share":{"bdSize":16}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='https://static-163yun.ajaxjs.com/bs/static/api/share.js?cdnversion='+~(-new Date()/36e5)];
			</script>
</c:if>

<c:if test="${ConfigService.getValueAsBool('domain.article.comment')}">
			<!-- 来必力City版安装代码 -->
			<div id="lv-container" data-id="city" data-uid="MTAyMC80NDQ4OS8yMTAyMQ==">
				<script>
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
</c:if>
