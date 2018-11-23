<%@page pageEncoding="UTF-8"%>
	<h4>Carousel Tab</h4>
	<ul class="list">
		<li>CSS3 动画加速</li>
		<li>可自动适应宽度（使用百分比单位），或者强制绝对单位（使用像素单位）</li>
		<li>自动计算 tab 内容高度</li>
		<li>TODO：SimpleTab 有 afterRender（只是在第一次触发）事件，但这个 tab 却没有</li>
	</ul>
	
	<p>属性说明：</p>
	<table class="ajaxjs-borderTable">
		<tr>
			<th>属性</th>
			<th>含义</th>
			<th>类型</th>
			<th>是否必填</th>
		</tr>
		<tr>
			<td>isMagic</td>
			<td>是否无缝模式</td>
			<td>Boolean</td>
			<td>N，默认 false</td>
		</tr>
		<tr>
			<td>isUsePx</td>
			<td>推荐使用 百分比，px 的话要考虑滚动条，比较麻烦；<br /> 要使用 px 推荐指定 stepWidth；banner，要使用 px</td>
			<td>Boolean</td>
			<td>N，默认 false</td>
		</tr>
		<tr>
			<td>isGetCurrentHeight</td>
			<td>自动当前项最高，忽略其他高度，这个用在 tab很好，<br /> 比 autoHeight 的好，可视作autoHeight 2</td>
			<td>Boolean</td>
			<td>N，默认 true</td>
		</tr>
		<tr>
			<td>initItems</td>
			<td>输入的数据，结构为 [{name:'foo', content:'bar'}]，<br />其中 content 可包含 HTML 标签</td>
			<td>Array</td>
			<td>N=演示数据</td>
		</tr>
	</table>
	
	<p>例子：</p>
	<pre class="prettyprint prettyprinted"><xmp><aj-carousel></aj-carousel></xmp></pre>
	
	<p>演示：</p>
	<div class="carousel-tab">
		<aj-carousel></aj-carousel>
	</div>
	
	<script>
		new Vue({el:'.carousel-tab'});
	</script>
	
	
	<h4>Banner</h4>
	<ul class="list">
		<li>继承自 Tab 控件</li>
		<li>可支持手势切换、点击小圆点可以切换</li>
		<li>可 resize 容器的时候调整大小</li>
		<li>可自定义 indicator 样式</li>
	</ul>
	
	<p>属性说明：</p>
	<table class="ajaxjs-borderTable">
		<tr>
			<th>属性</th>
			<th>含义</th>
			<th>类型</th>
			<th>是否必填</th>
		</tr>
		<tr>
			<td>isMagic</td>
			<td>是否无缝模式</td>
			<td>Boolean</td>
			<td>N，默认 false</td>
		</tr>
		<tr>
			<td>isUsePx</td>
			<td>推荐使用 百分比，px 的话要考虑滚动条，比较麻烦；<br /> 要使用 px 推荐指定 stepWidth；banner，要使用 px</td>
			<td>Boolean</td>
			<td>N，默认 true</td>
		</tr>
		<tr>
			<td>isGetCurrentHeight</td>
			<td>自动当前项最高，忽略其他高度，这个用在 tab很好，<br /> 比 autoHeight 的好，可视作autoHeight 2</td>
			<td>Boolean</td>
			<td>N，默认 false</td>
		</tr>
		<tr>
			<td>initItems</td>
			<td>输入的数据，结构为 [{name:'foo', content:'bar'}]，<br />其中 content 可包含 HTML 标签</td>
			<td>Array</td>
			<td>N=演示数据</td>
		</tr>
		<tr>
			<td>autoLoop</td>
			<td>切换 item 的时间间隔</td>
			<td>Number</td>
			<td>N=4000</td>
		</tr>
		<tr>
			<td>showTitle</td>
			<td>是否显示下方的标题</td>
			<td>Boolean</td>
			<td>N=true</td>
		</tr>
		<tr>
			<td>showDot</td>
			<td>是否显示一点点，一般显示了标题就不显示点</td>
			<td>Boolean</td>
			<td>N=true</td>
		</tr>
	</table>
	
	<p>例子：</p>
	<pre class="prettyprint prettyprinted"><xmp><aj-banner></aj-banner></xmp></pre>
	
	<p>演示：</p>
	<div class="carousel-banner">
		<aj-banner></aj-banner>
	</div>
	
	<script>
		new Vue({el:'.carousel-banner'});
	</script>
	

	<h4>LightBox</h4>
	<p>TODO</p>