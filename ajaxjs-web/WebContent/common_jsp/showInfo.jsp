<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="UI"        tagdir="/WEB-INF/tags/common/UI"%>
<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/less/user.less" title="列表" />
<body>
	<style>
html, body, table {
	height: 100%;
}

h1 {
	text-align: left;
	letter-spacing: 3px;
	margin: 10px 0;
}

.title {
	font-weight: bold;
	border-bottom: 1px solid lightgray;
	clear: both;
	overflow: hidden;
}

.title li {
	float: left;
	letter-spacing: 3px;
	line-height: 40px;
}

.list li {
	border-top: 1px solid lightgray;
	overflow: hidden;
	line-height: 40px;
	color:gray;	
}

.list li:nth-child(even) {
	background-color: #f5f5f5;
}

.list li div {
	float: left;
	white-space: nowrap;
	text-overflow: ellipsis;
	overflow: hidden;
}

.box {
	width: 800px;
	margin: 0 auto;
}
 
</style>
	<table width="100%" height="100%" align="center" valign="middle">
		<tr>
			<td align="center" valign="middle">

				<div class="box">
					 <UI:article />

					<a href="javascript:history.go(-1);">返回列表</a>
					<a href="edit/${info.id}">编辑记录</a>
					<a href="../edit/list">进入编辑列表</a>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>
