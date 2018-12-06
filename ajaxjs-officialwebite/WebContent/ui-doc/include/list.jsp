<%@page pageEncoding="UTF-8"%>
<h4>简单异步列表</h4>	
<p>简单异步列表，可访问 JSON/JSONP 数据源，并提供大量配置选项。</p>
<ul class="list">
	<li>默认 JSONP 获取远程或本地数据，JSONP 可跨域，也可以支持 XHR。由 cfg.isJSONP 决定。</li>
	<li>可指定返回的 JSON 结果集是哪个 key。由 cfg.resultKey 决定。</li>
	<li>每行的结构由 tpl 决定</li>
	<li>可简单地设置 loading.在 HTML 直接设置，如 &lt;ul class=&quot;simpleList&quot;&gt; 加载中…… &lt;/ul&gt;</li>
	<li>该控件须引用 list.js</li>
	<li>方法签名：
<pre class="prettyprint" style="height: 160px; width: 390px;">/**
 * 渲染一个列表
 * @param {String} url 接口地址
 * @param {String|Element} el 渲染到那个元素的下面
 * @param {Object} args 请求参数
 * @param {Object} cfg 配置对象
 */
ajaxjs.List = function (url, el, args, cfg);</pre>
		</li>
	</ul>

	<ul class="simpleList" style="width: 98%;">加载中……</ul>
	<script>
		ajaxjs.List(
			'proxy.jsp',
			document.querySelector('ul.simpleList'),
			{
				objectId : '223946',
				objectType : 1,
				start : 0,
				limit : 10,
				action : 'http_proxy',
				url : 'http://u1.3gtv.net:2080/pms-service/common/search_related'
			},
			{
				isNoAutoHeight : true,
				tpl : '<li>\
						<a href="{url}?id={id}">[:=name:]<div class="createTime">[:=createTime:]</div></a>\
					   </li>',
				renderer : function rendererItem(data) {
					data.createTime = data.createTime.substring(0, 10);
					return data;
				}
			});
	</script>



	<h4>分页列表</h4>
	
	<p>分页列表是在前者“简易列表”上提供分页功能。</p>
	<ul class="list">
		<li>采用 MySQL 偏移分页的机制：start/limit</li>
		<li>内置简单分页功能（由 cfg.pager:Boolean 决定），提供手机端的 append
			方式（累加方式）或者传统方式（覆盖之前的）。由 cfg.isAppend 决定，默认为 true</li>
		<li>分页按钮，由 cfg.loadMoreBtn:(String|Element) 指定</li>
		<li>提供 cb:Function 回调功能，你可以在加载数据之后执行任意函数</li>
		<li>由 pageSize 设置分页大小。</li>
		<li>数据 EOF 时候会在按钮处提示，并且不允许继续按</li>
	</ul>

	<h4>注意事项：</h4>
	<ul class="list">
		<li>起始状态必须设置 start : 0。start 为查询起始数，即 数据库中 记录的 行数，第 N 行</li>
		<li>当 isAppend = true，不能设置 loading indicator</li>
		<li>当前值提供最简单的“下一页”，TODO:更复杂的分页状态</li>
		<li>元素可以是 String，即 CSS Selector 或者元素对象，即 Element</li>
		<li>另外凡涉及元素的配置是 CSS Selector，如 loadMoreBtn:'.nextPageBtn'，不能是
			loadMoreBtn:'nextPageBtn'</li>
	</ul>
	
	
	<p>累加方式分页例子，isAppend = true</p>
	<ul class="simpleList1"></ul>
	<button class="ajaxjs-btn nextPageBtn1 center" style="display: block; margin-top: 10px;">下一页</button>

	<script>
		ajaxjs.List(
			'../../public_service',
			document.querySelector('ul.simpleList1'),
			{
				id : 2268,
				portalId : 45,
				start : 0,
				action : 'http_proxy',
				url : 'http://u1.3gtv.net:2080/pms-service/section/content_list'
			},
			{
				pager : true,
				isAppend : true,
				loadMoreBtn : '.nextPageBtn1',
				isNoAutoHeight : true,
				tpl : '<li>\
	<a href="#">[:=name:]<div class="createTime">[:=createTime:]</div></a>\
</li>',
				renderer : function rendererItem(data) {
					data.createTime = data.createTime.substring(0,
							10);
					return data;
				}
			});
	</script>

	<p>覆盖方式分页例子，isAppend = false</p>
	<ul class="center simpleList2">加载中……</ul>
	<button class="ajaxjs-btn nextPageBtn2" style="display: block; margin-top: 10px;">下一页</button>
	<script>
		ajaxjs.List(
			'../../public_service',
			document.querySelector('ul.simpleList2'),
			{
				id : 2268,
				portalId : 45,
				start : 0,
				action : 'http_proxy',
				url : 'http://u1.3gtv.net:2080/pms-service/section/content_list'
			},
			{
				pager : true,
				isAppend : false,
				loadMoreBtn : '.nextPageBtn2',
				isNoAutoHeight : true,
				tpl : '<li>\
						<a href="#">[:=name:]<div class="createTime">[:=createTime:]</div></a>\
					   </li>',
				renderer : function rendererItem(data) {
					data.createTime = data.createTime.substring(0,
							10);
					return data;
				}
			});
	</script>