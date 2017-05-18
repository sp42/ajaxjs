<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "tab"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
    	<br />
    	<div class="p">
	    	<h3>无缝上下滚动</h3>
	    	<p>鸣谢原作者： cloudgamer</p>
    	</div>
		<!-- 公告 -->
		<div class="idScroller center">
		<div>
			<ul>
				<li><a href="vod/?id=223397">
						<div class="icon colorful video"></div> 抗战胜利70周年纪念大会流程
				</a></li>
				<li><a href="vod/?id=223389">
						<div class="icon colorful video"></div> 公积金购二套房首付最低2成
				</a></li>
				<li><a href="vod/?id=223269">
						<div class="icon colorful video"></div> 我国首款喷气式客机将商运
				</a></li>
			</ul>
		</div>
		</div>
		<script>
			// 无缝上下滚动
			var scroller = Object.create(ajaxjs.MarqueeText);
			scroller.init(document.querySelector(".idScroller"));
			scroller.pauseStep = 3000; // 停留时间
			scroller.pauseHeight = 25; // 单行高度，必须与 CSS 中的 li.height 相等
			scroller.scroll();
		</script>
		<!-- // 公告 -->

 
    	<br />
	<div class="p">
		<h3>注意事项：</h3>
		<ul>
			<li>PauseHeight 必须与 CSS 中的 li.height 相等</li>
		</ul>
	</div>
	<div class="p">
		<h3>代码如下：</h3>
	</div>
	<pre class="prettyprint">
&lt;div class=&quot;idScroller&quot;&gt;
 &lt;div&gt;
	&lt;ul&gt;
		&lt;li&gt;&lt;a href=&quot;vod/?id=223397&quot;&gt;
				&lt;div class=&quot;icon colorful video&quot;&gt;&lt;/div&gt; 抗战胜利70周年纪念大会流程
		&lt;/a&gt;&lt;/li&gt;
		&lt;li&gt;&lt;a href=&quot;vod/?id=223389&quot;&gt;
				&lt;div class=&quot;icon colorful video&quot;&gt;&lt;/div&gt; 公积金购二套房首付最低2成
		&lt;/a&gt;&lt;/li&gt;
		&lt;li&gt;&lt;a href=&quot;vod/?id=223269&quot;&gt;
				&lt;div class=&quot;icon colorful video&quot;&gt;&lt;/div&gt; 我国首款喷气式客机将商运
		&lt;/a&gt;&lt;/li&gt;
	&lt;/ul&gt;
 &lt;/div&gt;
&lt;/div&gt;
&lt;script src=&quot;${bigfoot}/js/dhtml/marquee.js&quot;&gt;&lt;/script&gt;
&lt;script&gt;
	// 无缝上下滚动
	var scroller = Object.create(bf_marquee);
	scroller.init(document.querySelector(&quot;.idScroller&quot;));
	scroller.PauseStep = 3000; // 停留时间
	scroller.PauseHeight = 25; // 单行高度，必须与 CSS 中的 li.height 相等
	scroller.Scroll();
&lt;/script&gt;
	</pre>	
	<div class="p">
		<h3>样式如下：</h3>
	</div>
	<pre class="prettyprint">
// 无缝上下滚动
// 注：该组件样式基本要自定义的，所以没有集成到类库中
.idScroller {
	overflow:hidden;
	height:25px;
	padding-left:15px;
	li{
		height:25px; // 单行高度
		line-height:25px;
		white-space: nowrap;
		a{
			font-size:1rem;
			color:#666;
		}
	}
}</pre>
    </body>
</html>