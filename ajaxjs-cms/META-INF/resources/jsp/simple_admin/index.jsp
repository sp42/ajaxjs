<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height: 100%;">
	<head>
		<jsp:include page="/jsp/common/head.jsp"  flush="true">
			<jsp:param name="lessFile" value="/asset/less/admin.less" />
			<jsp:param name="title" value="${uiName}管理" />
		</jsp:include>
	</head>
	<body class="admin-shell">
		<header>
			<h1>我的控制面板</h1>
			<menu>
				${userName} 已登录 | <a href="${pageContext.request.contextPath}/" target="_blank">首页</a> | <a href="?action=logout">退出</a> 
			</menu>
		</header>
		<section class="side">
			<div class="rightTop_title">Welcome</div>
			<div class="rightTop"></div>
			<div class="closeBtn" onclick="hideSider(this);"></div>
			<span class="menu">
				<aj-accordion-menu>
			    	<%@include file="/WEB-INF/jsp/admin/admin-menu.jsp" %>
			    </aj-accordion-menu>
			</span>
		</section>
		<section class="iframe">
			<iframe src="admin/workbench/" name="iframepage"></iframe>
		</section>
	    <script>
	 	// 判断当前页面是否在iframe中 
	    if (self != top) {    
	    	parent.window.location.reload();
	    }  
	 	
	    // 收缩菜单
	    function hideSider(el) {
	    	if(hideSider.isHidden) {
	    		aj('.side').style.width   = '20%';
	    		aj('.iframe').style.width = '80%';
	    		hideSider.isHidden = false;
	    		el.style.right = '0'; 
	    	}else {
	    		aj('.side').style.width   = '0';
	    		aj('.iframe').style.width = '100%';
	    		hideSider.isHidden = true;
	    		el.style.right = '-'+ el.clientWidth +'px';
	    	}				
	    }

	    // 初始化菜单
	    new Vue({el: '.menu'});

	    // 需要把链接配置属性  target="iframepage"
	    ;(function () {
	    	// 获取 # target=abc 参数
	    	function getTarget() {
	    		var target = window.location.hash.match(/target=([^$]+)/);
	    		return target && target.pop();
	    	}
	    	
	    	// 展开菜单
	    	function highlightMenu(target) {
	    		var a = document.querySelector('a[href="' + target + '"]');
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
	    	aj('a[target=' + iframepageName + ']', function(a) {
	            a.onclick = add_Hash;
	        });
	        
	        function add_Hash(e) {
	        	var target = new String(iframeEl.contentWindow.location);
	        	var target = e.target.getAttribute('href');

	        	window.location.assign('#target=' + target); // 为主窗体添加描点记录，以便 F5 刷新可以回到这里
	            //alert(99)
	        	return false; // onhashchange() 里面已经跳转了，这里避免 a 再次跳转
	        }
	        
	        window.onhashchange = function(e) {
	        	var target = getTarget();
	        	iframeEl.src = target;
//	        	highlightMenu(target);
	        }
	    })();
	    		    
	    </script>
	</body>
</html>