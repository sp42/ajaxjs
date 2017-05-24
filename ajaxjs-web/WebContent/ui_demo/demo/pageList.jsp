<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "分页列表"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
   
    	<div class="p">
	    	<h3>分页列表</h3>
	    	<p>分页列表是在前者“简易列表”上提供分页功能。</p>
	    	 <ul>
	    	 	<li>采用 MySQL 偏移分页的机制：start/limit</li>
	    	 	<li>内置简单分页功能（由 cfg.pager:Boolean 决定），提供手机端的 append 方式（累加方式）或者传统方式（覆盖之前的）。由 cfg.isAppend 决定，默认为 true</li>
	    	 	<li>分页按钮，由 cfg.loadMoreBtn:(String|Element) 指定</li>
	    	 	<li>提供 cb:Function 回调功能，你可以在加载数据之后执行任意函数</li>
	    	 	<li>由 pageSize 设置分页大小。</li>
	    	 	<li>数据 EOF 时候会在按钮处提示，并且不允许继续按</li>
	    	 </ul>
    	</div>
    	
	    <div class="p">
			<h3>注意事项：</h3>
			<ul>
				<li>起始状态必须设置 start : 0。start 为查询起始数，即 数据库中 记录的 行数，第 N 行</li>
				<li>当 isAppend = true，不能设置 loading indicator</li>
				<li>当前值提供最简单的“下一页”，TODO:更复杂的分页状态</li>
				<li>元素可以是 String，即 CSS Selector 或者元素对象，即 Element</li>
				<li>另外凡涉及元素的配置是 CSS Selector，如 loadMoreBtn:'.nextPageBtn'，不能是  loadMoreBtn:'nextPageBtn'</li>
			</ul>
		</div>
    <div class="p">
	    <h3>累加方式分页例子，isAppend = true</h3>
	</div>
 	<ul class="simpleList1"></ul>
 	<button class="nextPageBtn1 center" style="display:block;margin-top:10px;">下一页</button>
 	
 	
 	
 	<script>
ajaxjs.List(
	'../../public_service', 
	document.querySelector('ul.simpleList1'), 
	{
		id : 2268,
		portalId : 45,
		start:0,
		action:'http_proxy',
		url : 'http://u1.3gtv.net:2080/pms-service/section/content_list'
	},
	{
 		pager : true,
 		isAppend: true,
 		loadMoreBtn:'.nextPageBtn1',
		isNoAutoHeight : true,
		tpl : '<li>\
				<a href="#">[:=name:]<div class="createTime">[:=createTime:]</div></a>\
			</li>',
		renderer : function rendererItem(data){
			data.createTime = data.createTime.substring(0, 10);
			return data;
		}
	}
);
    	</script>
		 
		<br />
		
 <div class="p">
	    <h3>覆盖方式分页例子，isAppend = false</h3>
	</div>
 	<ul class="simpleList2"> 加载中…… </ul>
 	<button class="nextPageBtn2 center" style="display:block;margin-top:10px;">下一页</button>
 	
 	
 	
 	<script>
 	
ajaxjs.List(
	'../../public_service', 
	document.querySelector('ul.simpleList2'), 
	{
		id : 2268,
		portalId : 45,
		start:0,
		action:'http_proxy',
		url : 'http://u1.3gtv.net:2080/pms-service/section/content_list'
	},
	{
 		pager : true,
 		isAppend: false,
 		loadMoreBtn:'.nextPageBtn2',
		isNoAutoHeight : true,
		tpl : '<li>\
			<a href="#">[:=name:]<div class="createTime">[:=createTime:]</div></a>\
			</li>',
		renderer : function rendererItem(data){
			data.createTime = data.createTime.substring(0, 10);
			return data;
		}
	}
);
 
    	</script>
		

 
		<div class="p">
			<h3>代码如下：</h3>
		</div>
		<pre class="prettyprint">
&lt;ul class=&quot;simpleList1&quot;&gt;&lt;/ul&gt;
&lt;button class=&quot;nextPageBtn1 center&quot; style=&quot;display:block;margin-top:10px;&quot;&gt;下一页&lt;/button&gt;
&lt;script&gt;
bf_list(
	&#x27;http://u1.3gtv.net:2080/pms-service/section/content_list&#x27;, 
	document.querySelector(&#x27;ul.simpleList1&#x27;), 
	{
		id : 2268,
		portalId : 45,
		start:0
	},
	{
 		pager : true,
 		isAppend: true,
 		loadMoreBtn:&#x27;.nextPageBtn1&#x27;,
		isNoAutoHeight : true,
		tpl : &#x27;&lt;li&gt;\
				&lt;a href=&quot;{url}?id={id}&quot;&gt;{name}&lt;div class=&quot;createTime&quot;&gt;{createTime}&lt;/div&gt;&lt;/a&gt;\
			&lt;/li&gt;&#x27;,
		renderer : function rendererItem(data){
			data.createTime = data.createTime.substring(0, 10);
			return data;
		}
	}
);
&lt;/script&gt;
		</pre>	
		
	    <%@include file="../public/footer.jsp" %>
    </body>
</html>