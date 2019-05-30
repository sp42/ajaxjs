<%@tag pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="/ajaxjs"%>
<%@attribute name="left" required="false" fragment="true" description="插入左侧菜单内容"%>
<%@attribute name="type" required="false" type="String" description="标签类型"%>
<c:if test="${type == 'info'}">
<div class="article">
	<article>
		<h1>${info.name}</h1>
		<h4 class="createDate">文/${info.author } <br />
		出处：${empty info.sourceUrl ? info.source : '<a href="'.concat(info.sourceUrl).concat('">').concat(info.source).concat('</a>') }<br />
		创建于：<c:dateFormatter value="${info.createDate}" /><br />最后编辑 ：<c:dateFormatter value="${info.updateDate}" /></h4>
		${info.content.replaceAll('src="', 'src="../../images/')}
	</article>
	<table class="vue" style="float:right;margin:10px 0 20px 0;">
		<tr>
			<td>
				<aj-page-share></aj-page-share>
			</td>
			<td>
				<aj-adjust-font-size></aj-adjust-font-size>
			</td>
		</tr>
	</table>
</div>
<script>
	new Vue({ el : '.vue' });
</script>
</c:if>
