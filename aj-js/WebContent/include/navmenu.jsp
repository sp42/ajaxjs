<%@page pageEncoding="UTF-8"%>
<h4>一般的下拉菜单</h4>
<p>下拉菜单控件, 纯 CSS 打造，无须 JavaScript。例子如下：</p>

<p>演示：</p>
<ul class="aj-css-menu center" style="width:400px;">
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
		</menu>
	</li>
</ul>

<div style="clear:both;"></div> 
<p>另有简单一点的 display:none/block 版，没有 CSS3 动画。</p>
<ul class="aj-css-menu-2 center" style="width:400px;">
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
	
<div style="clear:both;"></div> 
<p>
按照样式要求慢慢修正按钮与显示内容之间的界面融合；需要手动设置内容高度、宽度；内容层叠要调整 z-index 优先级；相关原理文章<a href="http://blog.csdn.net/zhangxin09/article/details/17659883">《利用 hover 伪类创建纯 CSS 收缩面板 》</a>。
</p>

<p>如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：</p>


<pre class="prettyprint">var menuPanel = el.parentNode;
menuPanel.style.display = 'none';
setTimeout(function(){
  menuPanel.style.display = '';
}, 500);</pre>

<h4>展开闭合器</h4>
<p>常用于展开正文详情。提供过渡效果的图层，如果不需要把 div class="mask" 去掉即可。注意加入容器的 padding 会导致关闭时高度异常。</p>

<p>属性说明：</p>
<table class="ajaxjs-borderTable">
	<thead>
		<tr>
			<th>属性</th>
			<th>含义</th>
			<th>类型</th>
			<th>是否必填，默认值</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>openHeight</td>
			<td>展开状态的高度</td>
			<td>Number</td>
			<td>n, 200</td>
		</tr>
		<tr>
			<td>closeHeight</td>
			<td>闭合状态的高度</td>
			<td>Number</td>
			<td>n, 50</td>
		</tr>
		<tr>
			<td><code>&lt;slot</code>&gt;</td>
			<td>正文内容扩展</td>
			<td>String</td>
			<td>n</td>
		</tr>
	</tbody>
</table>

<p>例子：</p>
<pre class="prettyprint prettyprinted"><xmp><aj-expander :close-height="50">……</aj-expander></xmp></pre>


<p>演示：</p>
<div class="expander">
	<aj-expander :close-height="50">
1、什么是WebpackWebPack可以看做是模块打包机：它做的事情是，分析你的项目结构，找到JavaScript模块以及其它的一些浏览器不能直接运行的拓展语言（Scss，TypeScript等），并将其打包为合适的格式以供浏览器使用。
2、为什要使用WebPack今的很多网页其实可以看做是功能丰富的应用，它们拥有着复杂的JavaScript代码和一大堆依赖包。为了简化开发的复杂度，前端社区涌现出了很多好的实践方法
a:模块化，让我们可以把复杂的程序细化为小的文件;
b:类似于TypeScript这种在JavaScript基础上拓展的开发语言：使我们能够实现目前版本的JavaScript不能直接使用的特性，并且之后还能能装换为JavaScript文件使浏览器可以识别；
c:scss，less等CSS预处理器
<div class="mask-layer"></div>
	</aj-expander>
</div>
<script>
	new Vue({el:'.expander'});
</script>



<div style="clear:both;"></div> 
<h4>折叠菜单 Accordion</h4>
<p>折叠菜单的特点是同一时间只展开一个区域，其他区域则处于闭合状态。</p>

<p>属性说明：</p>
<table class="ajaxjs-borderTable">
	<thead>
		<tr>
			<th>属性</th>
			<th>含义</th>
			<th>类型</th>
			<th>是否必填，默认值</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td><code>&lt;slot&gt;</code></td>
			<td>菜单内容</td>
			<td>String</td>
			<td>y</td>
		</tr>
	</tbody>
</table>

<p>例子：</p>
<pre class="prettyprint prettyprinted"><xmp><aj-accordion-menu>……</aj-accordion-menu></xmp></pre>


<p>演示：</p>
<ul class="center menu" style="width:80%;">
<aj-accordion-menu>
	<li class="siteMgr">
		<h3>网站管理</h3>
		<ul>
			<li><a target="iframepage" href="website/profile/changePassword.jsp">帐号信息</a></li>
			<li><a target="iframepage" href="website/profile/companyInfo.jsp">公司信息</a></li>
			<li><a target="iframepage" href="website/gallery/companyAlbum.jsp">相 册</a></li>
			<li><a target="iframepage" href="website/pageEditor/index.jsp">页面维护</a></li>
			<li><a target="iframepage" href="website/profile/global.jsp">全局信息</a></li>
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
			<li><a target="iframepage" href="levelPoints/showUserPoints.jsp">显示会员积分</a></li>
		</ul>
	</li>
	<li>
		<h3>其他信息</h3>
		<ul>
			<li><a target="iframepage" href="../job/job.jsp">留言管理</a></li>
			<li><a target="iframepage" href="../job/job.jsp">招聘管理</a></li>
		</ul>
	</li>
</aj-accordion-menu>
</ul>

<script>
	new Vue({el:'.menu'});
</script>