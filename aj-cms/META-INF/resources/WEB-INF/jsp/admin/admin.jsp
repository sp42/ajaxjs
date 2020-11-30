<%@page pageEncoding="UTF-8" import="com.ajaxjs.user.role.RoleService"%>
<%@taglib uri="/ajaxjs" prefix="c"%>
<!DOCTYPE html>
<html style="height: 100%;">
	<head>
		<jsp:include page="/WEB-INF/jsp/head.jsp" flush="true">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
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
							<li><a target="iframepage" href="${ctx}/admin/article/list/">图文管理</a></li>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.ADS)}">
							<li><a target="iframepage" href="${ctx}/admin/cms/ads/list/">广告管理</a></li>
					</c:if>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.ADS)}">
						 	<li><a target="iframepage" href="${ctx}/admin/cms/hr/list/">招聘管理</a></li> 
					</c:if>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.TOPIC)}">
							<li><a target="iframepage" href="${ctx}/admin/topic/list/">专题管理</a></li>
					</c:if>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.FEEDBACK)}">
							<li><a target="iframepage" href="${ctx}/admin/cms/feedback/list/">留言管理</a></li>
					</c:if>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.WEBSITE)}">
							<li><a target="iframepage" href="${ctx}/admin/page_editor/">页面内容</a></li>
					</c:if>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.SECTION)}">
							<li><a target="iframepage" href="${ctx}/admin/cms/section/">栏目管理</a></li>
					</c:if>
						</ul>
					</li>	    
					<li>
						<h3 class="tools"><i></i> 通用模块</h3>
						<ul>
							<li><a target="iframepage" href="${ctx}/admin/tree-like/">分类管理</a></li>
							<li><a target="iframepage" href="${ctx}/admin/datadict/">数据字典</a></li>
							<li><a target="iframepage" href="${ctx}/admin/attachment/">附件列表</a></li>
							
					<c:if test="${RoleService.check(privilegeTotal, RoleService.USER_PRIVILEGE)}">
							<li><a target="iframepage" href="${ctx}/admin/user/list/">用户管理</a></li>
					</c:if>
						</ul>
					</li>
					
					<li>
						<h3 class="system"><i></i> 系统维护</h3>
						<ul>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.GLOBAL_SETTING)}">
							<li><a target="iframepage" href="${ctx}/admin/config/">配置参数</a></li>
							
					</c:if>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.WEBSITE)}">
						<li><a target="iframepage" href="${ctx}/admin/config/site/">网站信息</a></li>
						<li><a target="iframepage" href="${ctx}/admin/config/siteStru/">网站结构</a></li>
					</c:if>
							<li><a target="iframepage" href="${ctx}/admin/userLoginLog/">登录日志</a></li>
							<li><a target="iframepage" href="${ctx}/admin/userGlobalLog/">操作日志</a></li>
					<c:if test="${RoleService.check(privilegeTotal, RoleService.DEVELOPER_TOOL)}">
							<li><a target="iframepage" href="${ctx}/admin/developer-tool/">实用工具</a></li>
							<li><a target="iframepage" href="${ctx}/admin/developer-tool/docs">文档</a></li>
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
		
	    <script>
	    	function logout() {
	    		confirm('确定退出吗？') && aj.xhr.get('${ctx}/user/login/logout/', j => {
	  				if(j.isOk) {
	  					aj.msg.show(j.msg);
	  					setTimeout(()=>location.assign('${ctx}/admin/login/'), 1000);
	  				}
	    		});
	    	}
	    	
		 	// 判断当前页面是否在iframe中 
		    if (self != top)    
		    	parent.window.location.reload();
		 	
		    // 收缩菜单
		    function hideSider(el) {
		    	if(hideSider.isHidden) {
		    		aj('.side').style.width   = '20%';
		    		aj('.iframe').style.width = '80%';
		    		hideSider.isHidden = false;
		    		el.style.right = '0'; 
		    	} else {
		    		aj('.side').style.width   = '0';
		    		aj('.iframe').style.width = '100%';
		    		hideSider.isHidden = true;
		    		el.style.right = '-'+ el.clientWidth +'px';
		    	}				
		    }
	
		    // 初始化菜单
		    new Vue({el: '.menu'});
	
		    // 需要把链接配置属性  target="iframepage"
	    	// 获取 # target=abc 参数
	    	function getTarget() {
	    		var target = window.location.hash.match(/target=([^$]+)/);
	    		return target && target.pop();
	    	}
	    	
	    	// 展开菜单
	    	function highlightMenu(target) {
	    		var a = aj('a[href="' + target + '"]');
	    		if (a) {        	
	    			var li = a.up('li'), ul = li.up('ul');
	    			li.classList.add('selected');
	    			ul.style.height = 'auto';
	    			ul.up('li').classList.add('pressed');
	    		}
	    	}
	    	
	    	var prefix = location.origin + "", iframepageName = 'iframepage';
	    	
	        var target = getTarget();
	        var iframeEl = aj('iframe[name=' + iframepageName + ']');
	        
	        if(target) {
	        	iframeEl.src = target; // 跳转 iframe
	        	highlightMenu(target);
	        }

	        // 点击菜单时保存按钮
	    	aj('a[target=' + iframepageName + ']', a => a.onclick = add_Hash);
	        
	        function add_Hash(e) {
	        	var target = new String(iframeEl.contentWindow.location);
	        	var target = e.target.getAttribute('href');
	        	window.location.assign('#target=' + target); // 为主窗体添加描点记录，以便 F5 刷新可以回到这里
	            
	        	return false; // onhashchange() 里面已经跳转了，这里避免 a 再次跳转
	        }
	        
	        window.onhashchange = e => {
	        	var target = getTarget();
	        	iframeEl.src = target;
//	        	highlightMenu(target);
	        }
	    </script>
	</body>
</html>