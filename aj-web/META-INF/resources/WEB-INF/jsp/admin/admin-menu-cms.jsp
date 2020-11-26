<%@page pageEncoding="UTF-8" import="com.ajaxjs.user.role.RightConstant, com.ajaxjs.user.role.RoleService"%>
 
<%
	long privilegeTotal = request.getSession().getAttribute("privilegeTotal") == null ? 0 : (long)request.getSession().getAttribute("privilegeTotal");
%>


<%if(RoleService.check(privilegeTotal, RightConstant.SHOP) || RoleService.check(privilegeTotal, RightConstant.SHOP_SELLER)){ %>
<li>
	<h3 class="shop"><i></i> 商城管理</h3>
	<ul>
	<%if(RoleService.check(privilegeTotal, RightConstant.SHOP)){ %>
		<li><a target="iframepage" href="${ctx}/admin/goods/list/">商品管理</a></li>
	<%-- 	<li><a target="iframepage" href="${ctx}/admin/simple-group/list/">团购管理</a></li> --%>
		<li><a target="iframepage" href="${ctx}/admin/order/list/">订单一览</a></li> 
	<%}%>
	<%if(RoleService.check(privilegeTotal, RightConstant.SHOP) || RoleService.check(privilegeTotal, RightConstant.SHOP_SELLER)){ %>
		<li><a target="iframepage" href="${ctx}/admin/orderItem/list/">订单明细</a></li>
	<%}%>
	<%if(RoleService.check(privilegeTotal, RightConstant.SHOP)){ %>
		<li><a target="iframepage" href="${ctx}/admin/cart/list/">购物车一览</a></li>
		<li><a target="iframepage" href="${ctx}/admin/shop-user/">商城用户管理</a></li>
		<li><a target="iframepage" href="${ctx}/admin/seller/list/">商家管理</a></li>
	<%}%>
	</ul>
</li>
<%}%>
<li>
	<h3 class="content"><i></i> 内容管理</h3>
	<ul>
		<%if(RoleService.check(privilegeTotal, RightConstant.ARTICLE_ONLINE)){ %>
			<li><a target="iframepage" href="${ctx}/admin/article/list/">图文管理</a></li> 
		<%}%>
		<%if(RoleService.check(privilegeTotal, RightConstant.ADS)){ %>
			<li><a target="iframepage" href="${ctx}/admin/ads/list/">广告管理</a></li>
		<%}%>
		<%if(RoleService.check(privilegeTotal, RightConstant.TOPIC)){ %>
	 		<li><a target="iframepage" href="${ctx}/admin/hr/list/">招聘管理</a></li> 
		<%}%>
		<%if(RoleService.check(privilegeTotal, RightConstant.ARTICLE_ONLINE)){ %>
			<li><a target="iframepage" href="${ctx}/admin/topic/list/">专题管理</a></li>
		<%}%>
		<%if(RoleService.check(privilegeTotal, RightConstant.FEEDBACK)){ %>
			<li><a target="iframepage" href="${ctx}/admin/hr/list/">招聘管理</a></li>
			<li><a target="iframepage" href="${ctx}/admin/feedback/list/">留言管理</a></li>
		<%}%>
		<%if(RoleService.check(privilegeTotal, RightConstant.SECTION)){ %>
			<li><a target="iframepage" href="${ctx}/admin/section/">栏目管理</a></li>
		<%}%>
	</ul>
</li>