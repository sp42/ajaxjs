<%@page pageEncoding="UTF-8"%>
	<h4>Carousel Tab</h4>
	<ul class="list">
		<li>CSS3 动画加速</li>
		<li>自动适应宽度（使用百分比单位）</li>
		<li>可强制绝对单位（使用像素单位）</li>
		<li>自动计算 tab 内容高度</li>
		<li>TODO：SimpleTab 有 afterRender（只是在第一次触发）事件，但这个 tab 却没有</li>
	</ul>

	
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
	<div class="carousel-banner">
		<aj-banner></aj-banner>
	</div>
	
	<script>
		new Vue({el:'.carousel-banner'});
	</script>
	

	<h4>LightBox</h4>
	<p>TODO</p>