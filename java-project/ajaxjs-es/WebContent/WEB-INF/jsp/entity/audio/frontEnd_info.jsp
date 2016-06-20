<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags/"%>
 
<tags:content isSecondLevel="true">
	<jsp:attribute name="body">
	<div>
		<%@ include file="/WEB-INF/jsp/entity.jsp" %>
		<a href="../service/audio/${info.id}">该实体之 JSON 格式</a>
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
					<audio src="${info.sourceUrl}" controls></audio>
				</td>
			</tr>
		</table>
		<%@include file="/WEB-INF/jsp/entity_albumInfo.jsp"%>
	</div>
	</jsp:attribute>
</tags:content>



