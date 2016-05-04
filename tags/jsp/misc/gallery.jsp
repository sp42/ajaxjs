<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/public"%>
<%@taglib prefix="commonUI" tagdir="/WEB-INF/tags/public/UI"%>
<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/bigfoot/asset/less/pages.less" />
<body>
	<commonUI:adminHeader pageTitle="相册" />
	<style>
		ul {
			margin-top: 5px;
			-webkit-column-count: 4;
			-moz-column-count: 4;
			-webkit-column-width: 222px;
			-moz-column-width: 222px;
			-o-colum-width: 222px;
			-webkit-column-gap: 1px;
			-moz-column-gap: 1px;
			-o-column-gap: 1px;
		}
		
		li:hover img {
			border-color: #FF4400;
		}
		
		img {
			margin: 5px;
			cursor: pointer;
			border: 1px white solid;
			width: 97%;
		}
	</style>

	<div>
		<ul>
				<c:foreach items="${imgs}" var="img">
			<li>
					<img src="../images/${img}" />
			</li>
				</c:foreach>
		</ul>
	</div>
</body>
</html>
