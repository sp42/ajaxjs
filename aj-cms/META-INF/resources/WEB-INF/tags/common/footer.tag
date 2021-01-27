<%@tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%
// 要写 java 所以不用 jstl tag
// "footer".equals(jspContext.getAttribute("type"))

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
			<div class="btn" onclick="document.body.$('footer > .top').classList.toggle('close');"></div>
		</div>
		${SITE_STRU.getSiteMap(pageContext.request)}
	</div>

	<div class="copyright">
		<div>
			<div class="right">
				<%-- 			<a href="#"> <img src="${commonAsset}/images/gs.png" height="40" /></a> 
				<a href="#"> <img src="${commonAsset}/images/kexin.png" hspace="20" width="90" style="margin-top: 15px;" /></a>  --%>
				<a href="#"> <img src="${ajaxjs_ui_output}/images/360logo.gif" width="90" style="margin-top: 15px;" /></a>
			</div>
			<span> 
				<a href="javascript:;" onclick="aj.widget.page.TraditionalChinese.toSimpleChinese(this);" class="simpleChinese selected">简体中文</a> /
				<a href="javascript:;" onclick="aj.widget.page.TraditionalChinese.toChinese(this);" class="Chinese">正体中文</a>
			</span>
			<script>
				aj.widget.page.TraditionalChinese.init();
			</script>
			<br /> 
			<a target="_blank" href="http://beian.miit.gov.cn">${empty aj_allConfig.site.site_icp ? '粤ICP备15007080号-2' : aj_allConfig.site.site_icp}</a>
			<c:if test="${not empty aj_allConfig.site.site_police_icp}">
				&nbsp; &nbsp;&nbsp;
				<a target="_blank" href="${aj_allConfig.site.site_police_icp_link}">${aj_allConfig.site.site_police_icp}</a>
			</c:if>
			<!-- Powered by <a target="_blank" href="http://framework.ajaxjs.com">AJAXJS</a> -->
			<br /> ©Copyright ${FOOTER_YEAR} 版权所有，
			${aj_allConfig.clientFullName} &nbsp; ${empty requestTimeRecorder ? '' : '请求完成耗时：'.concat(requestTimeRecorder).concat('秒') }
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