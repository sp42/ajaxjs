<%@tag pageEncoding="UTF-8" description="页脚" import="java.util.Date"%>
<footer>
		<div class="sitemap">
			<div>
				<div class="btn" onclick="document.querySelector('.sitemap').toggleCls('open');"></div>
				${PageNode.siteMap}
			</div>
		</div>

		<div class="copyright">
		<div>
			<a href="#">
				<img src="${pageContext.request.contextPath}/asset/bigfoot/asset/images/gs.png" />
			</a> 
			<a href="#">
				<img src="${pageContext.request.contextPath}/asset/bigfoot/asset/images/kexin.png" hspace="20" width="90" style="margin-top:15px;" />
			</a> 
			<a href="#">
				<img src="${pageContext.request.contextPath}/asset/bigfoot/asset/images/360logo.gif" width="90" style="margin-top:15px;" />
			</a>
			<br />
			<a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>
			/
			<a href="javascript:;" class="Chinese" onclick="toChinese(this);">正体中文</a>
			<script src="${pageContext.request.contextPath}/asset/bigfoot/js/libs/chinese.js"></script>
			<br />
			粤ICP备15007080号-1
			Powered by <a target="_blank" href="http://framework.ajaxjs.com">AJAXJS</a>
			<br />
			<%
			
			if(request.getAttribute("requestTimeRecorder") != null ){
				Long requestTimeRecorder = (Long)request.getAttribute("requestTimeRecorder");
				requestTimeRecorder = System.currentTimeMillis() - requestTimeRecorder;
				float _requestTimeRecorder = (float)requestTimeRecorder;
				_requestTimeRecorder = _requestTimeRecorder / 1000;
				// float seconds = (endTime - startTime) / 1000F;
				request.setAttribute("requestTimeRecorder", _requestTimeRecorder); 
			}
			%>
	 	©Copyright <%=java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)%> 版权所有， ${global_config.clientFullName} ${empty requestTimeRecorder ? '' : '请求完成耗时：'.concat(requestTimeRecorder).concat('秒') }
	
	</div>
		</div>
</footer>