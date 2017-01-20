<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@taglib prefix="UI"   tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<tags:content isSecondLevel="true" extraTitle="${info.name}">
	<jsp:attribute name="body">
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
			<a href="list?filterField=portalId&filterValue=${info.portalId}">门户：${portal.name}</a>
			&nbsp;&nbsp;&nbsp;<a href="list?filterField=catalog&filterValue=${catalog.id}">分类：${catalog.name}</a> 
		</li>
		<li>
			抓取分类：${crawlerTypes[info.crawlerType]}
		</li>
		<li>
			上次更新：${empty info.lastUpdate ? 'n/a' : viewUtils.formatDateShorter(info.lastUpdate)}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			集数总计：${info.length} 
			播放量：${empty info.playHit ? 'n/a' : info.playHit} 
			订阅量：${empty info.subscriptionAmount ? 'n/a' : info.subscriptionAmount}  
			标签：${empty info.tags ? 'n/a' : info.tags} 
		</li>
		<li>
			来源地址：<a href="${info.url}"  target="_blank">${info.url}</a> 
		</li>
		<li>封面地址：<a href="${info.cover}" target="_blank" rel="noreferrer">${info.cover}</a></li>

		<li>
			创建日期：${viewUtils.formatDateShorter(info.createDate)}
			修改日期：${viewUtils.formatDateShorter(info.updateDate)}
		</li>
	</ul>
	<a href="../service/album/${info.id}.json">该实体之 JSON 格式</a>
		<br />
		<br />
	<table>
		<tr>
			<td>
				<img src="${info.cover}" width="300" />
			</td>
			<td width="20"></td>
			<td valign="top">
				<p>简介：${info.content} ${info.intro}</p>
				<hr />
				<a href="../show_${info.crawlerType == 0 ? 'video' : 'audio'}/list?filterField=albumId&filterValue=${info.id}">该专辑记录</a>
				<br />
				<a href="../show_${info.crawlerType == 0 ? 'video' : 'audio'}/list.json?filterField=albumId&filterValue=${info.id}">该专辑记录（JSON）</a>
			</td>
		</tr>
	</table>
		
		
		<div style="margin-top:5%;font-size:.8rem;padding-left:2%;">
		
		<c:if test="${not empty neighbor.perRecord.id}">
			<div>
				<a href="?id=${neighbor.perRecord.id}">上则记录：${neighbor.perRecord.name}</a>
			</div>
		</c:if>
		
		<c:if test="${not empty neighbor.nextRecord.id}">
			<div>
				<a href="?id=${neighbor.nextRecord.id}">下则记录：${neighbor.nextRecord.name}</a>
			</div>
		</c:if>
		</div>
		
		<div style="max-width: 600px;margin: 1% auto;overflow: hidden;">
			<table align="right">
				<tr>
					<td valign="middle">
						<UI:widget type="share" />
					</td>
					<td valign="middle">
						<UI:widget type="adjustFontSize" />
					</td>
				</tr>
			</table>
		</div>

	</jsp:attribute>
</tags:content>



