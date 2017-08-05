<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "菜单");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<h4>一般的下拉菜单</h4>
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
	<ul class="centerLimitWidth menu2">
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


	<h4>注意事项：</h4>
	<ul class="centerLimitWidth list">
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

	<p>HTML 代码如下：</p>

	<pre class="prettyprint">
&lt;ul class=&quot;menu&quot;&gt;
	&lt;li&gt;
		&lt;a href=&quot;../index.htm&quot;&gt;首页&lt;/a&gt;
	&lt;/li&gt;
	&lt;li&gt;
		&lt;a href=&quot;#&quot;&gt;DHTML&lt;/a&gt;
		&lt;menu&gt;
			&lt;div&gt;DHTML，即动态 HTML，为详见的页面效果。&lt;/div&gt;
			&lt;ul&gt;
				&lt;li&gt;
					&lt;a href=&quot;msgbox.jsp&quot;&gt;对话框&lt;/a&gt;
				&lt;/li&gt;
				&lt;li&gt;
					&lt;a href=&quot;menu.jsp&quot;&gt;菜单&lt;/a&gt;
				&lt;/li&gt;
				&lt;li&gt;
					&lt;a href=&quot;scrollText.jsp&quot;&gt;无缝上下滚动文字&lt;/a&gt;
				&lt;/li&gt;
			&lt;/ul&gt;
		&lt;/menu&gt;
	&lt;/li&gt;
	&lt;li&gt;
		&lt;a href=&quot;#&quot;&gt;切换类&lt;/a&gt;
		&lt;menu&gt;
			&lt;div&gt;切换类多数为滚动内容的控件&lt;/div&gt;
			&lt;ul&gt;
				&lt;li&gt;
					&lt;a href=&quot;tab.jsp&quot;&gt;Tab&lt;/a&gt;
				&lt;/li&gt;
				&lt;li&gt;
					&lt;a href=&quot;tab.jsp&quot;&gt;垂直 Tab&lt;/a&gt;
				&lt;/li&gt;
				&lt;li&gt;
					&lt;a href=&quot;banner.jsp&quot;&gt;广告轮播图 Banner&lt;/a&gt;
				&lt;/li&gt;
				&lt;li&gt;
					&lt;a href=&quot;lightbox.jsp&quot;&gt;Lightbox&lt;/a&gt;
				&lt;/li&gt;
			&lt;/ul&gt;
		&lt;/menu&gt;
	&lt;/li&gt;
	&lt;li&gt;
		&lt;a href=&quot;#&quot;&gt;其他控件类&lt;/a&gt;
		&lt;menu&gt;
			&lt;div&gt;指日历、HTML 编辑器这类控件&lt;/div&gt;
			&lt;ul&gt;
				&lt;li&gt;
					&lt;a href=&quot;tab.jsp&quot;&gt;Tab&lt;/a&gt;
				&lt;/li&gt;
				&lt;li&gt;
					&lt;a href=&quot;banner.jsp&quot;&gt;Banner&lt;/a&gt;
				&lt;/li&gt;
				&lt;li&gt;
					&lt;a href=&quot;lightbox.jsp&quot;&gt;lightbox&lt;/a&gt;
				&lt;/li&gt;
			&lt;/ul&gt;
		&lt;/menu&gt;
	&lt;/li&gt;
&lt;/ul&gt; 
		</pre>
	<p>样式如下：</p>

	<pre class="prettyprint">
.menu{
	max-width:@maxWidth;
	margin:0 auto;
	&>li{
		float:left;
		padding:0 2%;
		box-sizing: border-box;	
		position: relative;
		&:hover menu{// 展开状态
			height:10rem; // 手动设置内容高度
			z-index:999999999; // 
		}
		menu{
			line-height: 25px;
			z-index:99999999;
			padding: 0 5px;
			box-sizing: border-box;
			width:200px;// 手动设置内容宽度
			background-color:#eee;
			overflow: hidden;
			margin:0;
			position: absolute;
			.heightFx; 	// css3 动画效果
			height:0;	// 初始高度 0
			div{
				font-size:.8rem;
				color:gray;
				line-height: 15px;
				margin:10px 0;
			}
			&:hover{	// 展开状态
				height:10rem;
				z-index:999999999;
			}
		}
	}
}

// display:none/block 版
.menu2{
	max-width:@maxWidth;
	margin:0 auto;
	&>li{
		float:left;
		padding:0 2%;
		box-sizing: border-box;	
		position: relative;
		&:hover menu{// 展开状态
			display:block;
		}
		menu{
			line-height: 25px;
			display:none;
			z-index:99999999;
			padding: 0 5px;
			box-sizing: border-box;
			width:200px;// 手动设置内容宽度
			background-color:#eee;
			overflow: hidden;
			margin:0;
			position: absolute;
			div{
				font-size:.8rem;
				color:gray;
				line-height: 15px;
				margin:10px 0;
			}
			&:hover{	// 展开状态
				display:block;
			}
		}
	}
}
	    </pre>
</body>
</html>