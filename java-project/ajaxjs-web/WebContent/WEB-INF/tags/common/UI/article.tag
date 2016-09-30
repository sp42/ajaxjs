<%@tag pageEncoding="UTF-8" description="文章功能模块" import="java.util.Map"%>
<%@taglib prefix="UI"        tagdir="/WEB-INF/tags/common/UI"%>
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib uri="/ajaxjs" prefix="c" %>
<%@attribute name="articleList" type="Map[]" required="false" description="文章列表"%>
<%if(articleList != null){ %>
	<div class="list">

				<a href="detail?sectionId=${current.seciotnId}&uid=${current.uid}">
					<div>
						<h3>${current.name}</h3>
						<p>${current.brief}</p>
					</div>
				</a>
				<div align="right" class="misc">
					${PageUtil.formatDate(current.createDate)} 分类：${current.seciotnName}
				</div>

	</div>
	<div class="list">
		<commonTag:page type="page" />
	</div>
<%}%>
<c:if test="${not empty info}">
	<style>
		article p {
		    color: #555;
		    padding: 0 2%;
		    line-height: 140%;
		    margin: 20px 0;
		    letter-spacing: 1px;
		    text-align: justify;
		    text-indent: 2em;
		}
	</style>
		<article>
			<h2>${info.name}</h2>
			<h3 class="createDate">
<%-- 			栏目： ${sectionInfo.name}  --%>
			创建日期：${PageUtil.formatDate(info.createDate)}</h3>
			<div>${info.content}</div>
			<div style="margin-top:5%;font-size:.8rem;padding-left:2%;">
			<c:if test="${not empty neighbor.perRecord.id}">
				<div>
					<a href="${neighbor.perRecord.id}.info">上则记录：${neighbor.perRecord.name}</a>
				</div>
			</c:if>
			<c:if test="${not empty neighbor.nextRecord.id}">
				<div>
					<a href="?id=${neighbor.nextRecord.id}">下则记录：${neighbor.nextRecord.name}</a>
				</div>
			</c:if>
			</div>
		</article>
		<div style="overflow: hidden;">
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
 
		<!-- UY BEGIN -->
		<div id="uyan_frame" class="list" style="padding:2%;"></div>
		<script type="text/javascript" src="http://v2.uyan.cc/code/uyan.js?uid=1952510"></script>
		<!-- UY END -->
</c:if>