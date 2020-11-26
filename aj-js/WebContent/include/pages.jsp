<%@page pageEncoding="UTF-8"%>
	<h4>全屏幕加载指示器</h4>
	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-page-fullscreen-loading-indicator></aj-page-fullscreen-loading-indicator></xmp></pre>
	
	<p>演示：</p>
	<div class="center">
		<img src="https://images.gitee.com/uploads/images/2018/0814/150555_8e8601b5_784269.png" />
	</div>
	
	<p>该功能通过以下代码实现。</p>
	<pre class="prettyprint">document.onreadystatechange = function () {
    if(document.readyState === "complete") {
        aj(".aj-fullscreen-loading").classList.add('fadeOut');// 关闭遮罩
    }
}</pre>

	<h4>Baidu 自定义搜索</h4>
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
				<td>siteDomainName</td>
				<td>搜索网站之域名</td>
				<td>String</td>
				<td>n, 通过 js 返回域名</td>
			</tr>
		</tbody>
	</table>
	
	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-baidu-search site-domain-name="${all_config.site.domainName}"></aj-baidu-search></xmp></pre>
	
	<p>演示：</p>
	
	<div class="baidu center">
		<aj-baidu-search site-domain-name="${all_config.site.domainName}"></aj-baidu-search>
	</div>

	<script>
		new Vue({el:'.baidu'});
	</script>	
	
	
	<h4>分享页面的按钮</h4>
	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-page-share></aj-page-share></xmp></pre>
	
	<p>演示：</p>
	
	<div class="shareBtns center">
		<aj-page-share></aj-page-share>
	</div>

	<script>
		new Vue({el:'.shareBtns'});
	</script>	
	
	
	<h4>选择正文字体大小</h4>
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
				<td>articleTarget</td>
				<td>正文所在的位置，通过 CSS Selector 定位</td>
				<td>String</td>
				<td>n, article p</td>
			</tr>
		</tbody>
	</table>
	
	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-adjust-font-size></aj-adjust-font-size></xmp></pre>
	
	<p>演示：</p>
	<div class="font center">
		<aj-adjust-font-size></aj-adjust-font-size>
	</div>

	<script>
		new Vue({el:'.font'});
	</script>	
	
	
	<h4>切换简体、繁体中文</h4>
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
				<td>jsurl</td>
				<td>js字库文件较大，外部引入</td>
				<td>String</td>
				<td>y</td>
			</tr>
		</tbody>
	</table>
	
	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-chinese-switch jsurl="${ajaxjsui}/js/libs/chinese.js"></aj-chinese-switch></xmp></pre>
	
	<p>演示：</p>
	<div class="chinese center">
		<aj-chinese-switch jsurl="${ajaxjsui}/js/libs/chinese.js"></aj-chinese-switch>
	</div> 
	<p>暂不演示，代码高亮和 js 冲突。</p>
	<script>
		//new Vue({el:'.chinese'});
	</script>	
	
	<h4>分享页面的按钮</h4>
	<p>例子：</p>
	<pre class="prettyprint"><xmp><aj-back-top></aj-back-top></xmp></pre>
	
	<p>演示：</p>
	
	<div class="backTop center">
		<aj-back-top></aj-back-top>
	</div>

	<script>
		new Vue({el:'.backTop'});
	</script>	