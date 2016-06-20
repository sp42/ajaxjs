<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="c" uri="/ajaxjs"%>
<%@taglib prefix="UI"   tagdir="/WEB-INF/tags/common/UI"%>
		<c:if test="${not empty album.name}">
		<fieldset>
			<legend><h4>专辑信息：${album.name}</h4></legend>
			<p>
				<img src="${album.cover}" style="float:left;margin-right:2%;" />${album.intro}
				<hr />
				<a href="../album/info.jsp?id=${album.id}">该专辑详细信息</a> | <a href="list/?filterField=albumId&filterValue=${album.id}">该专辑其他记录</a>
			</p>
		</fieldset>
		</c:if>
		<div style="margin-top:5%;font-size:.8rem;padding-left:2%;">
		
		<c:if test="${not empty neighbor[0]}">
			<div>
				<a href="?id=${neighbor[0].id}">上则记录：${neighbor[0].name}</a>
			</div>
		</c:if>
		
		<c:if test="${not empty neighbor[1]}">
			<div>
				<a href="?id=${neighbor[1].id}">下则记录：${neighbor[1].name}</a>
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