<%@tag pageEncoding="UTF-8" description="文章功能模块"%>
<%@attribute name="body"  required="false" fragment="true" description="插入正文内容"%>
<%@attribute name="isNoMenu"  required="false" type="Boolean" description="是否需要  menu 内容"%>		
<%@attribute name="extraTitle"  required="false" type="String" description="副标题"%>		
<%@attribute name="isSecondLevel"  required="false" type="Boolean" description="是否二级栏目，否则为三级"%>		
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<!DOCTYPE html>
<html>
	<commonTag:head lessFile="/asset/less/company.less" />
<body>
	<%@include file="/WEB-INF/jsp/public/nav.jsp"%>
	<div class="body">
	<%if(isNoMenu == null || isNoMenu == false){ %>
		<menu>
			<fieldset>
				<legend>
					${PageNode.node.name}
				</legend>
				<commonTag:page type="secondLevelMenu" />
			</fieldset>
		</menu>
	<%} %>

		<commonTag:page type="anchor" />
		<article>
			<h3>${PageNode.node.name}</h3>
			<jsp:invoke fragment="body" />
		</article>
	</div>
	 
	<%@ include file="/WEB-INF/jsp/public/footer.jsp" %>
</body>
</html>		