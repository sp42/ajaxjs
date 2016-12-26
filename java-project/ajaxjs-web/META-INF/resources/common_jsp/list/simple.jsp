<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<commonTag:head lessFile="/asset/less/user.less" title="列表" />
<body>
	<style>
html, body {
	height: 100%;
}
table{
	height: 60%;
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
	color: gray;
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

a {
	color: #333;
	text-decoration: none;
}

a:visited {
	color: gray;
}

a:hover {
	color: #d9534f;
	text-decoration: underline;
}

.box {
	width: 990px;
	margin: 0 auto;
}

.col_id {
	width: 10%;
}

.col_name {
	width: 25%;
	text-align: left;
	font-size:1rem;
}

.col_createDate, .col_updateDate {
	width: 18%;
	text-align: left;
	font-size:.9rem;
}

.col_action {
	width: 28%;
	text-align: left;
}

.col_id, .col_name .col_createDate {
	text-align: center;
}

button {
	border-radius: 3px;
	border: 1px solid;
	color: white;
	padding: 5px 10px;
	letter-spacing: 5px;
	font-size: 14px;
	cursor: pointer;
}

button.b1 {
	background-color: #5bc0de;
	border-color: #46b8da;
}

button.b2 {
	background-color: #2e6da4;
	border-color: #46b8da;
}

button.b3 {
	background-color: #d9534f;
	border-color: #d43f3a;
}
</style>
	<table width="100%" height="100%" align="center" valign="middle">
		<tr>
			<td align="center" valign="middle">

				<div class="box">

					<h1>列表</h1>
					<ul class="title">
						<li class="col_id">#ID</li>
						<li class="col_name">名称</li>
						<li class="col_createDate">创建時間</li>
						<li class="col_updateDate">门户|专辑</li>
						<li class="col_action">操作</li>
					</ul>
					<ul class="list">
						<c:forEach var="current" items="${PageResult.rows}">
							<li>
								<div class="col_id">${current.id}</div>
								<div class="col_name">
									<a
										href="${tableName}/${current.id}.info">${current.name}</a>
								</div>
								<div class="col_createDate">${viewUtils.formatDateShorter(current.createDate)}</div>
								<div class="col_updateDate">${viewUtils.formatDateShorter(current.updateDate)}</div>
								<div class="col_action">
									<button class="b1" onclick="location.assign('${tableName}/${current.id}.info');">详情</button>
<%-- 									<button class="b2" onclick="playAudio('${current.sourceUrl}');">播放</button> --%>
									<button class="b3" onmouseover="showCover(arguments[0], this);" onmouseout="hideCover(arguments[0]);" data-img="${current.cover}">来源</button>
								</div>
							</li>
						</c:forEach>
					</ul>
					<commonTag:page type="page" pageInfo="${PageResult}" />

					<a href="${tableName}/edit/list">进入编辑列表</a>
					
					<div class="cover_img">
						<img src="" />
					</div>
					<script>
						var cover_img = document.querySelector('.cover_img');
						function showCover(e, el){
							cover_img.addCls('show');
							if(el.getAttribute('data-img')){
								cover_img.querySelector('img').src = el.getAttribute('data-img');
							}
							cover_img.style.left = e.clientX + 'px';
							cover_img.style.top = e.clientY + 'px';
						}
						
						function hideCover(e){
							cover_img.querySelector('img').src = '';
							cover_img.removeCls('show');
						}
						 
						
						function playAudio(sourceUrl){
							var audio = document.querySelector('video');
							audio.src = sourceUrl;
							audio.play();
						}
					</script>
					<style>
						.cover_img{
							display:none;
							position: fixed;
							width:300px;
							height:150px;
						}
						.cover_img img {
							width:100%;
						}
						.show{
							display:block;
						}
						video{
							max-width:500px;
						}
					</style>
					<br />
					<video src="" controls></video>
				</div>
			</td>
		</tr>
	</table>
	
</body>
</html>
