<%@tag pageEncoding="UTF-8" import="com.ajaxjs.framework.config.ConfigService" trimDirectiveWhitespaces="true" %>
<%@taglib uri="/ajaxjs" 		prefix="c" %>
<c:if test="${not empty info}">
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
			
			 ${info.content} 
		</article>
		
		
<c:if test="${ConfigService.getValueAsBool('domain.article.attachmentDownload') && not empty info.attachment}">
		<fieldset class="aj-fieldset">
			<legend>附件下载</legend>
			<ul>
				<c:foreach items="${info.attachment}">
					<li style="padding:1%;">
						<a href="${aj_allConfig.uploadFile.perfix}${item.name}" download>${item.name}</a> | ${item.fileSize}kb
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
				window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdMiniList":false,"bdPic":"","bdStyle":"1","bdSize":"16"},"share":{"bdSize":16}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='https://ajaxjs.nos-eastchina1.126.net/bs/static/api/share.js?cdnversion='+~(-new Date()/36e5)];
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
