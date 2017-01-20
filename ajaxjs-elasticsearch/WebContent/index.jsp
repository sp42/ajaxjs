<%@page pageEncoding="UTF-8" import="com.ajaxjs.framework.model.JspModel"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/less/company.less" />
<body>
	<%@include file="/WEB-INF/jsp/public/nav.jsp"%>
	<div class="index">
		<div class="banner">
			<img src="asset/images/index.jpg" />
		</div>
		<ul>
			<li>
				<h3>最新视频</h3>
<%
	JspModel model = new JspModel(request);
	new com.egdtv.crawler.controller.VideoController().list(0, 5, request, model);
%>
				<ul>
				<c:foreach var="current" items="${PageResult.rows}">
					<li>
						<a href="dsfdsf">${current.name}</a>
					</li>
				</c:foreach>
				</ul>
			</li>
			<li>
				<h3>最新音频</h3>
<%
	//new com.egdtv.crawler.controller.AudioController().list(0, 5, request, model);
%>
				<ul>
				<c:foreach var="current" items="${PageResult.rows}">
					<li>
						<a href="dsfdsf">${current.name}</a>
					</li>
				</c:foreach>
				</ul>
			</li>
			<li><h3>最新文章</h3>
			<p>暂无</p></li>
		</ul>
	</div>
	<%@ include file="/WEB-INF/jsp/public/footer.jsp" %>
</body>
</html>