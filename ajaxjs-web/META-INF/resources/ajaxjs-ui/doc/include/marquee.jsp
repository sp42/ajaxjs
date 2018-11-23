<%@page pageEncoding="UTF-8"%>
<h3>跑马灯 Marquee</h3>
<h4>超简单跑马灯 Super Simple Marquee</h4>

<p>这是最简单的跑马灯，原理是把前面的字符放到后面去。鼠标移入时候可暂停动画，移出恢复。支持插槽，但插槽中不能包含 tag。</p>
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
		<td>interval</td>
		<td>动画时间间隔，定时器的参数</td>
		<td>Number</td>
		<td>N</td>
		<td>500</td>
	</tr>
	<tr>
		<td>canstop</td>
		<td>是否可以鼠标移入时候暂停动画</td>
		<td>Boolean</td>
		<td>N</td>
		<td>true，可以</td>
	</tr>
</table>

<p>例子：</p>
<pre class="prettyprint"><xmp><aj-super-simple-marquee-text></aj-super-simple-marquee-text></xmp></pre>

<p>演示：</p>
<section class="marquee">
	<aj-super-simple-marquee-text></aj-super-simple-marquee-text>
</section>
<script>
	new Vue({
		el : '.marquee'
	});
</script>



<h4>上下字幕跑马灯 Marquee</h4>

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
		<td>interval</td>
		<td>动画时间间隔，定时器的参数</td>
		<td>Number</td>
		<td>N</td>
		<td>500</td>
	</tr>
	<tr>
		<td>canstop</td>
		<td>是否可以鼠标移入时候暂停动画</td>
		<td>Boolean</td>
		<td>N</td>
		<td>true，可以</td>
	</tr>
</table> 

<p>例子：</p>
<pre class="prettyprint"><xmp><aj-simple-marquee-text></aj-simple-marquee-text></xmp></pre>
<p>演示：</p>
<section class="marquee2">
	<aj-simple-marquee-text></aj-simple-marquee-text>
</section>
<script>
	new Vue({
		el : '.marquee2'
	});
</script>



