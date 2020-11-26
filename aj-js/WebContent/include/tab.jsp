<%@page pageEncoding="UTF-8"%>
	<h4>简易选项卡 Tab</h4>
	
	<p>设有水平（常见）和垂直 Tab</p>
	<ul class="center list">
		<li>切换原理为控制元素的 display:block/none;，比较简单，附带的好处是高度自适应</li>
		<li>要将 Tab 边框样式无缝结合，需要 CSS 的一定技巧性</li>
		<li>提供 beforeSwitch、afterSwitch、afterRender(i, btn, showTab)（只是在第一次触发）事件</li>
	</ul>
	
	<p>属性说明：</p>
	<table class="ajaxjs-borderTable">
		<tr>
			<td>属性</td>
			<td>含义</td>
			<td>类型</td>
			<td>是否必填</td>
			<td>默认值</td>
		</tr>
		<tr>
			<td>initItems</td>
			<td>输入的数据，结构为 [{name:'foo', content:'bar'}]，<br />其中 content 可包含 HTML 标签</td>
			<td>Array</td>
			<td>N</td>
			<td>N=演示数据</td>
		</tr>
		<tr>
			<td>isVertical</td>
			<td>是否垂直方向的布局</td>
			<td>Boolean</td>
			<td>N</td>
			<td>false=水平 Tab</td>
		</tr>
	</table>
	
	<p>例子：</p>
	<pre class="prettyprint prettyprinted"><xmp><aj-simple-tab></aj-simple-tab></xmp></pre>
	
	<p>演示：</p>
	<div class="tab">
		<aj-simple-tab></aj-simple-tab>
	</div>
	
	
	<h4>垂直 Tab</h4>
	<p>垂直 Tab 的内容区域应该设置 min-height；或者固定高。</p>
	
	<p>例子：</p>
	<pre class="prettyprint prettyprinted"><xmp><aj-simple-tab :is-vertical="true"></aj-simple-tab></xmp></pre>
	
	<p>演示：</p>
	
	<div class="tab_hoz">
		<aj-simple-tab :is-vertical="true"></aj-simple-tab>
	</div>

	<script>
		new Vue({el:'.tab'});
		new Vue({el:'.tab_hoz'});
	</script>	