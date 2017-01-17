<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "Banner"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
    	<script src="${bigfoot}/js/libs/touch.js"></script>
    	<script src="${bigfoot}/js/widget/tab.js"></script>
    	<br />

    	<div class="p">
    	 Banner 控件, 支持以下特性：
    	 <ul>
    	 	<li>继承自 Tab 控件</li>
    	 	<li>可支持手势切换、点击小圆点可以切换</li>
    	 	<li>提供服务端版（基于 JSP 标签），使用更方便</li>
    	 </ul>
    	</div>
    	<br />
    	
		<section class="banner">
			<div>
				<div>
					<a href="topic/?id=283">
						<img src="http://imgu.3gtv.net:9090/_file/section/20150906113629683.jpg">
					</a>
				</div>
			
				<div>
					<a href="topic/?id=341">
						<img src="http://imgu.3gtv.net:9090/_file/section/20150828090747132.jpg">
					</a>
				</div>
			
				<div>
					<a href="live/?id=3">
						<img src="http://imgu.3gtv.net:9090/_file/section/20150906125934797.png">
					</a>
				</div>
			
				<div>
					<a href="album/?id=4805">
						<img src="http://imgu.3gtv.net:9090/_file/section/20150826162352938.jpg">
					</a>
				</div>
			</div>
		    <ol class="indicator"> 
				<li>0</li>
				<li>1</li>
				<li>2</li>
				<li>3</li>
		    </ol>
		</section>

		<script>
			var banner = Object.create(bf_banner);
			banner.el = document.querySelector('.banner');
			banner.init();
			banner.loop();
			banner.initIndicator();
		</script>
    	<br />
	<div class="p">
		<h3>依赖 js：</h3>
		<ul>
			<li>/js/widget/tab.js</li>
			<li>/js/libs/touch.js 如果需要触控的话</li>
		</ul>
	</div>
	<div class="p">
		<h3>注意事项：</h3>
		<ul>
			<li>可自定义 indicator 样式</li>
		</ul>
	</div>
	<div class="p">
		<h3>代码如下：</h3>
	</div>
 
	<pre class="prettyprint">
&lt;section class=&quot;banner&quot;&gt;
	&lt;div&gt;
		&lt;div&gt;
			&lt;a href=&quot;topic/?id=283&quot;&gt;
				&lt;img src=&quot;http://imgu.3gtv.net:9090/_file/section/20150906113629683.jpg&quot;&gt;
			&lt;/a&gt;
		&lt;/div&gt;
	
		&lt;div&gt;
			&lt;a href=&quot;topic/?id=341&quot;&gt;
				&lt;img src=&quot;http://imgu.3gtv.net:9090/_file/section/20150828090747132.jpg&quot;&gt;
			&lt;/a&gt;
		&lt;/div&gt;
	
		&lt;div&gt;
			&lt;a href=&quot;live/?id=3&quot;&gt;
				&lt;img src=&quot;http://imgu.3gtv.net:9090/_file/section/20150906125934797.png&quot;&gt;
			&lt;/a&gt;
		&lt;/div&gt;
	
		&lt;div&gt;
			&lt;a href=&quot;album/?id=4805&quot;&gt;
				&lt;img src=&quot;http://imgu.3gtv.net:9090/_file/section/20150826162352938.jpg&quot;&gt;
			&lt;/a&gt;
		&lt;/div&gt;
	&lt;/div&gt;
    &lt;ol class=&quot;indicator&quot;&gt; 
		&lt;li&gt;0&lt;/li&gt;
		&lt;li&gt;1&lt;/li&gt;
		&lt;li&gt;2&lt;/li&gt;
		&lt;li&gt;3&lt;/li&gt;
    &lt;/ol&gt;
&lt;/section&gt;

&lt;script&gt;
	var banner = Object.create(bf_banner);
	banner.el = document.querySelector(&#x27;.banner&#x27;);
	banner.init();
	banner.loop();
	banner.initIndicator();
&lt;/script&gt;
	   </pre>
<div class="p">
			<h3>样式如下：</h3>
		</div>
		<pre class="prettyprint">
.banner{
	.tab;
	position:relative;
	margin-bottom: 15px;
	min-height: 140px;
	width:98%;
	.centerLimitWidth();
	max-height:280px; // 防止加载中扯高容器
	&>div>div img{
		width:100%;
	}
	ol{
		background:rgba(0, 0, 0, .5);
		width:100%;
		margin:0;
		padding:0;
		padding-right:60px;
		height:18px;
		line-height:25px;
		position:absolute;
		bottom:0;
		left:0;
		text-align:right;
		li{
			list-style-type:none;
			height:5px;
			width:5px;
			display:inline-block;
			margin:6px 3px;
			color:white;
			text-indent:99em;
			background:rgba(255, 255, 255, .5);
			cursor: pointer;
			&:last-child{
				margin-right: 20px;
			}
			&.active{
				background:white;
			}
		}
	}
}
	    </pre>
    </body>
</html>