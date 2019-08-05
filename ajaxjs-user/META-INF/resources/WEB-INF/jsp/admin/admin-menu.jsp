<%@page pageEncoding="UTF-8" import="com.ajaxjs.user.role.RightConstant, com.ajaxjs.user.role.RoleService"%>
<%
	long privilegeTotal = (long)request.getSession().getAttribute("privilegeTotal");
%>
<li>
	<h3>内容管理</h3>
	<ul>
	<%if(RoleService.check(privilegeTotal, RightConstant.ARTICLE_ONLINE)){ %>
		<li><a target="iframepage" href="${ctx}/admin/article/list/">文章管理</a></li> 
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
		<li><a target="iframepage" href="${ctx}/admin/feedback/list/">留言反馈管理</a></li>
	<%}%>
	<%if(RoleService.check(privilegeTotal, RightConstant.SECTION)){ %>
		<li><a target="iframepage" href="${ctx}/admin/section/">栏目管理</a></li>
	<%}%>
	</ul>
</li>

<%if(RoleService.check(privilegeTotal, RightConstant.SHOP) || RoleService.check(privilegeTotal, RightConstant.SHOP_SELLER)){ %>
<li>
	<h3>商城管理</h3>
	<ul>
	<%if(RoleService.check(privilegeTotal, RightConstant.SHOP)){ %>
		<li><a target="iframepage" href="${ctx}/admin/goods/list/">商品管理</a></li>
		<li><a target="iframepage" href="${ctx}/admin/simple-group/list/">团购管理</a></li>
		<li><a target="iframepage" href="${ctx}/admin/order/list/">用户订单</a></li>
	<%}%>
	<%if(RoleService.check(privilegeTotal, RightConstant.SHOP) || RoleService.check(privilegeTotal, RightConstant.SHOP_SELLER)){ %>
		<li><a target="iframepage" href="${ctx}/admin/orderItem/list/">订单明细</a></li>
	<%}%>
	<%if(RoleService.check(privilegeTotal, RightConstant.SHOP)){ %>
		<li><a target="iframepage" href="${ctx}/admin/cart/list/">购物车一览</a></li>
		<li><a target="iframepage" href="${ctx}/admin/bookmark/list/">收藏一览</a></li>
		<li><a target="iframepage" href="${ctx}/admin/address/list/">地址薄一览</a></li>
		<li><a target="iframepage" href="${ctx}/admin/seller/list/">商家管理</a></li>
	<%}%>
	</ul>
</li>
<%}%>

 
	<%if(RoleService.check(privilegeTotal, RightConstant.WEBSITE)){ %>
<li>
	<h3>网站管理</h3>
	<ul>
		<li><a target="iframepage" href="${ctx}/admin/config/siteStru/">网站结构</a></li>
		<li><a target="iframepage" href="${ctx}/admin/page_editor/">页面管理</a></li>
	</ul>
</li>
	<%}%>
	<%if(RoleService.check(privilegeTotal, RightConstant.GLOBAL_SETTING)){ %>

<li>
	<h3>全局数据</h3>
	<ul>
		<li><a target="iframepage" href="${ctx}/admin/config/site/">项目信息</a></li>
		<li><a target="iframepage" href="${ctx}/admin/attachmentPicture/list/">图片列表</a></li>
		<li><a target="iframepage" href="${ctx}/admin/config/">所有配置</a></li>
		<li><a target="iframepage" href="${ctx}/admin/catelog/">分类管理</a></li>
		<li><a target="iframepage" href="${ctx}/admin/DataDict/">数据字典管理</a></li>
	</ul>
</li>
	<%}%>
	<%if(RoleService.check(privilegeTotal, RightConstant.USER)){ %>
<li> 
	<h3>用户管理</h3>
	<ul>
		<li><a target="iframepage" href="${ctx}/admin/user/list/">用户管理</a></li>
		<li><a target="iframepage" href="${ctx}/admin/userLoginLog/">登录日志</a></li>
		<li><a target="iframepage" href="${ctx}/admin/userGlobalLog/">操作日志</a></li>
	<%if(RoleService.check(privilegeTotal, RightConstant.USER_PRIVILEGE)){ %>
		<li><a target="iframepage" href="${ctx}/admin/user/privilege/">用户-权限管理</a></li>
		<li><a target="iframepage" href="${ctx}/admin/user/user_group/">用户组管理</a></li>
		<li><a target="iframepage" href="${ctx}/user/register/">用户注册</a></li>
	<%}%>
	</ul>
</li> 
<%}%>
<%if(RoleService.check(privilegeTotal, RightConstant.DEVELOPER_TOOL)){ %>
<li>
	<h3>开发工具</h3>
	<ul>
		<li><a target="iframepage" href="${ctx}/admin/DataBase/">数据库管理</a></li>
		<li><a target="iframepage" href="${ctx}/admin/CodeGenerators/">代码生成器</a></li>
		<li><a target="iframepage" href="${ajaxjsui}/doc">前端文档</a></li>
		<li><a target="iframepage" href="${ctx}/admin/DataBaseShowStru">表字段浏览</a></li>
		<li><a target="iframepage" href="${ctx}/admin/GlobalLog/">操作日志浏览</a></li>
		<li><a target="iframepage" href="${ctx}/asset/admin/tomcat-log.jsp">后台日志浏览</a></li>
		<li><a target="iframepage" href="${ctx}/jsp/swagger-ui/">API 文档</a></li>
	</ul>
</li>
<%}%>
<li>
	<h3>账号中心</h3>
	<ul>
		<li><a target="iframepage" href="${ctx}/user/account-center/">账号中心</a></li>
		<li><a href="javascript:logout();">退出登录</a></li>
	</ul>
</li>