<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
 
<tags:content isSecondLevel="true">
	<jsp:attribute name="body">
	<div>
		<%@include file="/WEB-INF/jsp/entity.jsp"%>
		<a href="../service/video/${info.id}">该实体之 JSON 格式</a> 
		<c:if test="${portal.liveSource}">
			<button class="my-btn" onclick="getLiveSource();">获取实时源</button>
			<script>
				function getLiveSource() {
					xhr.get('liveSourceById', {
						site 	: '${portal.id}',
						miscId 	: encodeURIComponent('${info.misc}')
					}, function(json) {
						var sourceUrl = document.querySelector('.sourceUrl');
						sourceUrl.innerHTML = '';
						var a = '<a href="{0}" target="_blank" rel="noreferrer">{0}</a>'
						for(var i in json) {
							var div = document.createElement('li');
							div.innerHTML = i + ' : ' + a.format(json[i]);
							sourceUrl.appendChild(div);
						}
						document.querySelector('video').src = json[i]; // 播放器
					});
				}
			</script>
		</c:if>
		<br />
		<br />
		<table>
			<tr>
				<td>
					<video src="${info.sourceUrl}" controls poster="${info.cover}" width="450"></video>
				</td>
				<td width="20"></td>
				<td valign="top">
					<p>简介：${info.content} ${info.intro}</p>
					<br />
					注意：m3u8视频格式不能在 PC 浏览器播放
				</td>
			</tr>
		</table>
		<%@include file="/WEB-INF/jsp/entity_albumInfo.jsp"%>
	</div>
	</jsp:attribute>
</tags:content>



