<%@page pageEncoding="UTF-8"%>
	<h4>Nav Menu 导航菜单</h4>
	<p>下拉菜单控件, 纯 CSS 打造，无须 JavaScript。例子如下</p>

	<ul class="menu">
		<li><a href="../index.htm">首页</a></li>
		<li><a href="#">DHTML</a>
			<menu>
				<div>DHTML，即动态 HTML，为详见的页面效果。</div>
				<ul>
					<li><a href="msgbox.jsp">对话框</a></li>
					<li><a href="menu.jsp">菜单</a></li>
					<li><a href="scrollText.jsp">无缝上下滚动文字</a></li>
				</ul>
			</menu></li>
		<li><a href="#">切换类</a>
			<menu>
				<div>切换类多数为滚动内容的控件</div>
				<ul>
					<li><a href="tab.jsp">Tab</a></li>
					<li><a href="tab.jsp">垂直 Tab</a></li>
					<li><a href="banner.jsp">广告轮播图 Banner</a></li>
					<li><a href="lightbox.jsp">Lightbox</a></li>
				</ul>
			</menu></li>
		<li><a href="#">其他控件类</a>
			<menu>
				<div>指日历、HTML 编辑器这类控件</div>
				<ul>
					<li><a href="tab.jsp">Tab</a></li>
					<li><a href="banner.jsp">Banner</a></li>
					<li><a href="lightbox.jsp">lightbox</a></li>
				</ul>
			</menu></li>
	</ul>
	<br />
	<p>另有简单一点的 display:none/block 版</p>
	<ul class="menu2">
		<li><a href="../index.htm">首页</a></li>
		<li><a href="#">DHTML</a>
			<menu>
				<div>DHTML，即动态 HTML，为详见的页面效果。</div>
				<ul>
					<li><a href="msgbox.jsp">对话框</a></li>
					<li><a href="menu.jsp">菜单</a></li>
					<li><a href="scrollText.jsp">无缝上下滚动文字</a></li>
				</ul>
			</menu></li>
		<li><a href="#">切换类</a>
			<menu>
				<div>切换类多数为滚动内容的控件</div>
				<ul>
					<li><a href="tab.jsp">Tab</a></li>
					<li><a href="tab.jsp">垂直 Tab</a></li>
					<li><a href="banner.jsp">广告轮播图 Banner</a></li>
					<li><a href="lightbox.jsp">Lightbox</a></li>
				</ul>
			</menu></li>
		<li><a href="#">其他控件类</a>
			<menu>
				<div>指日历、HTML 编辑器这类控件</div>
				<ul>
					<li><a href="tab.jsp">Tab</a></li>
					<li><a href="banner.jsp">Banner</a></li>
					<li><a href="lightbox.jsp">lightbox</a></li>
				</ul>
			</menu></li>
	</ul>

	<br />


	<p>注意事项：</p>
	<ul class="list">
		<li>按照样式要求慢慢修正按钮与显示内容之间的界面融合</li>
		<li>需要手动设置内容高度、宽度</li>
		<li>内容层叠要调整 z-index 优先级</li>
		<li>相关文章<a
			href="http://blog.csdn.net/zhangxin09/article/details/17659883">《利用
				hover 伪类创建纯 CSS 收缩面板 》</a></li>
		<li>如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover
			伪类的方法，故所以必须使用以下技巧：<br /> <pre class="prettyprint" >
var menuPanel = el.parentNode;
menuPanel.style.display = 'none';
setTimeout(function(){
  menuPanel.style.display = '';
}, 500);</pre>
		</li>
	</ul>
	
	<h4>Accordion Menu 折叠菜单</h4>
	<p>折叠菜单的特点是同一时间只展开一个区域，其他区域则处于闭合状态。</p>
 
	<ul class="centerLimitWidth leftSidebar" style="width:80%;">
		<li class="siteMgr">
			<h3>网站管理</h3>
			<ul>
				<li><a target="iframepage"
					href="website/profile/changePassword.jsp">帐号信息</a></li>
				<li><a target="iframepage"
					href="website/profile/companyInfo.jsp">公司信息</a></li>
				<li><a target="iframepage"
					href="website/gallery/companyAlbum.jsp">相 册</a></li>
				<li><a target="iframepage" href="website/pageEditor/index.jsp">页面维护</a>
				</li>
				<li><a target="iframepage" href="website/profile/global.jsp">全局信息</a>
				</li>
			</ul>
		</li>

		<li class="news">
			<h3>新闻中心管理</h3>
			<ul>
				<li><a target="iframepage" href="entry/list/news">新闻管理</a></li>
			</ul>
		</li>
		<li class="levelPoints">
			<h3>积分系统</h3>
			<ul>
				<li><a target="iframepage" href="levelPoints/memberTree.jsp">会员下线一览</a>
				</li>
				<li><a target="iframepage"
					href="levelPoints/showUserPoints.jsp">显示会员积分</a></li>
			</ul>
		</li>
		<li>
			<h3>其他信息</h3>
			<ul>
				<li><a target="iframepage" href="../job/job.jsp">留言管理</a></li>
				<li><a target="iframepage" href="../job/job.jsp">招聘管理</a></li>
			</ul>
		</li>
	</ul>
	<script>
		// 初始化菜单
		new ajaxjs.AccordionMenu(document.querySelector('.leftSidebar'));
	</script>
	