<footer>
	<div class="top">
		<div class="btn" onclick="aj('footer .top').classList.toggle('close');"></div>
		${SITE_STRU.getSiteMap(pageContext.request)}
	</div>

	<div class="copyright">
		<div class="right">
			<a href="#"> <img src="${commonAsset}/images/gs.png" height="40" /></a> 
			<a href="#"> <img src="${commonAsset}/images/kexin.png" hspace="20" width="90" style="margin-top: 15px;" /></a> 
			<a href="#"> <img src="${commonAsset}/images/360logo.gif" width="90" style="margin-top: 15px;" /></a> 
		</div>
		<a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a> / <a href="javascript:;"
			class="Chinese" onclick="toChinese(this);">正体中文</a>
		<script src="${ajaxjsui}/js/libs/chinese.js"></script>
		<br /> 
		
		${empty aj_allConfig.site.site_icp ? '粤ICP备15007080号-2' : aj_allConfig.site.site_icp}
		Powered by <a target="_blank" href="http://framework.ajaxjs.com">AJAXJS</a>
		<br />
		<%
			if (request.getAttribute("requestTimeRecorder") != null) {
				Long requestTimeRecorder = (Long) request.getAttribute("requestTimeRecorder");
				requestTimeRecorder = System.currentTimeMillis() - requestTimeRecorder;
				float _requestTimeRecorder = (float) requestTimeRecorder;
				_requestTimeRecorder = _requestTimeRecorder / 1000;
				// float seconds = (endTime - startTime) / 1000F;
				request.setAttribute("requestTimeRecorder", _requestTimeRecorder);
			}
		%>
		©Copyright <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%>
		版权所有， ${aj_allConfig.clientFullName} &nbsp; ${empty requestTimeRecorder ? '' : '请求完成耗时：'.concat(requestTimeRecorder).concat('秒') }
	</div>
</footer>