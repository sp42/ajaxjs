<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="c" uri="/ajaxjs"%>
	<style>
		h2{
			padding:2% 0;
		}
		h3{
			display:none;
		}
		fieldset{
			width:80%;
			border-radius:10px;
			padding:1%;
			margin:2% 0;
		}
		legend{
			margin-left:10px;
		}
		.info li:nth-child(2n) {
		    background-color: #f5f5f5;
		}
		.info li {
		    border-top: 1px solid lightgray;
		    color: gray;
		    line-height: 40px;
		    overflow: hidden;
		}
	</style>
	<h2>${info.name}</h2>
	<ul class="info">
		<li>
			<a href="list/?filterField=portalId&filterValue=${info.portalId}">门户：${portal.name}</a>
			&nbsp;&nbsp;&nbsp;<a href="list/?filterField=catalog&filterValue=${catalog.id}">分类：${catalog.name}</a> 
			&nbsp;&nbsp;&nbsp;专辑：<a href="../album/info.jsp?id=${album.id}">${empty album.name ? 'n/a' : album.name}</a>
		</li>
		<li>
			推出日期：${empty info.publishDate ? 'n/a' : info.publishDate}&nbsp;&nbsp;&nbsp;时长：${empty info.duration ? 'n/a' : info.duration} 秒&nbsp;&nbsp;
			播放量：${info.playHit} 标签：${empty info.tags ? 'n/a' : info.tags} 
		</li>
		<li>
			来源地址：<a href="${info.ownerUrl}"  target="_blank">${info.ownerUrl}</a> 
		</li>
		<li>封面地址：<a href="${info.cover}" target="_blank" rel="noreferrer">${info.cover}</a></li>
		<li>
			播放地址：
			<c:if test="${empty info.sourceUrl}">
				<ul class="sourceUrl"></ul>
			</c:if>
			<c:if test="${not empty info.sourceUrl}">
				<a href="${info.sourceUrl}" target="_blank" rel="noreferrer" class="sourceUrl">${info.sourceUrl}</a>
			</c:if>
		</li>

		<li>
			创建日期：${viewUtils.formatDateShorter(info.createDate)}
			修改日期：${viewUtils.formatDateShorter(info.updateDate)}
		</li>
	</ul>
