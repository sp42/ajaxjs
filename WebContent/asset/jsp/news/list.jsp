<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/less/company.less" title="${uiName}列表" />
<body class="newsList">
	<div class="top">
		<commonTag:page type="navMenu2" />
	</div>

	<div class="centerWidth">
		<img src="../images/news.jpg" width="100%" style="margin-top: 5px;" />
		<h2>${uiName}列表</h2>
		${(not empty errMsg.cause ) ? (errMsg.cause.message) : ''}

		<div class="body">
			<div class="left">
				<ul>
				dd
				</ul>
			</div>
			<div class="right">
			<commonTag:list type="list-thumb" classList="list" />
			</div>
		</div>
	</div>
	<div class="bottom">
		<commonTag:page type="footer" />
	</div>
</body>

</html>
