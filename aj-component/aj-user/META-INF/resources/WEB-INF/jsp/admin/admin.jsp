<%@page pageEncoding="UTF-8" import="com.ajaxjs.user.role.RoleService"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html style="height: 100%;">
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp" flush="true">
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
		<!-- Admin 公共前端资源 -->
		<link rel="stylesheet" href="${aj_static_resource}/dist/css/admin/shell.css" />
	</head>
	<body class="admin-shell">
		<header>
			<h1>${aj_allConfig.site.titlePrefix} ${aj_allConfig.System.name}</h1>
			<menu>
				${userName} 已登录 &nbsp;&nbsp;
				|&nbsp;&nbsp;<a href="${ctx}/" target="_blank">首页</a>&nbsp;&nbsp; 
				|&nbsp;&nbsp;<a href="${ctx}/admin/home/" target="iframepage">后台首页</a>&nbsp;&nbsp;
				|&nbsp;&nbsp;<a href="javascript:logout();">退出</a> 
			</menu>
		</header>
		
		<section class="side">
			<div class="rightTop_title">Welcome</div>
			<div class="rightTop"></div>
			<div class="closeBtn" onclick="hideSider(this);"></div>
			<span class="menu">
				<aj-accordion-menu>
			    	<%@include file="/WEB-INF/jsp/admin/admin-menu.jsp" %>
					<!-- 通用菜单	 -->		
					<li>
						<h3><i class="fa fa-sitemap"></i> 内容管理</h3>
						<ul>
							<li><a target="iframepage" href="${ctx}/admin/cms/article/list/">图文管理</a></li>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.ADS)}">
							<li><a target="iframepage" href="${ctx}/admin/cms/ads/list/">广告管理</a></li>
					</c:if>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.HR_ONLINE)}">
						 	<li><a target="iframepage" href="${ctx}/admin/cms/hr/list/">招聘管理</a></li> 
					</c:if>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.TOPIC)}">
							<li><a target="iframepage" href="${ctx}/admin/cms/topic/list/">专题管理</a></li>
					</c:if>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.SECTION)}">
							<li><a target="iframepage" href="${ctx}/admin/cms/section/">栏目管理</a></li>
					</c:if>
						</ul>
					</li>	    
					<c:if test="${RoleService.check(privilegeTotal, RoleService.WEBSITE)}">
					<li>
						<h3 class="tools"><i></i> 网站配置</h3>
						<ul>
							<li><a target="iframepage" href="${ctx}/admin/website/site/">网站信息</a></li>
							<li><a target="iframepage" href="${ctx}/admin/website/siteStru/">网站结构</a></li>
							<li><a target="iframepage" href="${ctx}/admin/website/page_editor/">页面内容</a></li>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.FEEDBACK)}">
							<li><a target="iframepage" href="${ctx}/admin/website/feedback/list/">留言管理</a></li>
					</c:if>
							<li><a target="iframepage" href="${ctx}/admin/website/tongji/">网站统计</a></li>
						</ul>
					</li>
					</c:if>
					
					<li>
						<h3 class="system"><i></i> 系统维护</h3>
						<ul>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.GLOBAL_SETTING)}">
							<li><a target="iframepage" href="${ctx}/admin/common/config/">配置参数</a></li>
							<li><a target="iframepage" href="${ctx}/admin/common/tree-like/">分类管理</a></li>
							<li><a target="iframepage" href="${ctx}/admin/common/datadict/">数据字典</a></li>
							<li><a target="iframepage" href="${ctx}/admin/common/attachment/">附件列表</a></li>
							
					</c:if>
					
					<c:if test="${RoleService.check(privilegeTotal, RoleService.USER_PRIVILEGE)}">
							<li><a target="iframepage" href="${ctx}/admin/user/list/">用户管理</a></li>
							<li><a target="iframepage" href="${ctx}/admin/user/userLoginLog/">登录日志</a></li>
							<li><a target="iframepage" href="${ctx}/admin/user/userGlobalLog/">操作日志</a></li>
					</c:if>
					
					<c:if test="${RoleService.check(privilegeTotal, RoleService.DEVELOPER_TOOL)}">
							<li><a target="iframepage" href="${ctx}/admin/common/developer-tool/">实用工具</a></li>
							<li><a target="iframepage" href="${ctx}/admin/common/developer-tool/docs">文档</a></li>
					</c:if>
						</ul>
					</li>
					
					<li>
						<h3 class="accountCenter"><i></i> 账号中心</h3>
						<ul>
							<li><a target="iframepage" href="${ctx}/admin/user/account-center/">账号中心</a></li>
							<li><a target="iframepage" href="${ctx}/admin/user/profile">个人信息</a></li>
							<li><a href="javascript:logout();">退出登录</a></li>
						</ul>
					</li>
					<!-- // 通用菜单	 -->
			    </aj-accordion-menu>
			</span>
		</section>
		
		<section class="iframe">
			<iframe src="${ctx}/admin/home/" name="iframepage"></iframe>
		</section>
		
	    <script src="${aj_static_resource}/dist/admin/shell.js"></script>
	</body>
</html>