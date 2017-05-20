<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "简单列表"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
   
    	<div class="p">
	    	<h3>简单异步列表</h3>
	    	<p>简单异步列表，可访问 JSON/JSONP 数据源，并提供大量配置选项。</p>
	    	 <ul>
	    	 	<li>默认 JSONP 获取远程或本地数据，JSONP 可跨域，也可以支持 XHR。由 cfg.isJSONP 决定。</li>
	    	 	<li>可指定返回的 JSON 结果集是哪个 key。由 cfg.resultKey 决定。</li>
	    	 	<li>每行的结构由 tpl 决定</li>
	    	 	<li>可简单地设置 loading.在 HTML 直接设置，如 &lt;ul class=&quot;simpleList&quot;&gt; 加载中…… &lt;/ul&gt;</li>
	    	 	
	    	 	<li> 方法签名：<pre class="prettyprint" style="height:140px;width:390px;">
/**
 * 渲染一个列表
 * @param {String} url 接口地址
 * @param {String|Element} el 渲染到那个元素的下面
 * @param {Object} args 请求参数
 * @param {Object} cfg 配置对象
 */
bf_list = function (url, el, args, cfg);</pre>
	    	 	</li>
	    	 </ul>
    	</div>
    <div class="p">
	    <h3>例子</h3>
	</div>
 	<ul class="simpleList"> 加载中…… </ul>
 	<script>
bf_list('http://u1.3gtv.net:2080/pms-service/common/search_related', document.querySelector('ul.simpleList'), {
		objectId : '223946',
		objectType : 1,
		start : 0,
		limit : 10
	},
	{
		isNoAutoHeight : true,
		tpl : '<li>\
				<a href="{url}?id={id}">{name}<div class="createTime">{createTime}</div></a>\
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
			<h3>依赖 js：</h3>
			<ul>
				<li>/js/widget/list.js</li>
			</ul>
		</div>
 
		<div class="p">
			<h3>代码如下：</h3>
		</div>
		<pre class="prettyprint">
&lt;ul class=&quot;simpleList&quot;&gt; 加载中…… &lt;/ul&gt;
&lt;script src=&quot;${bigfoot}/js/widget/list.js&quot;&gt;&lt;/script&gt;
&lt;script&gt;
bf_list(&#x27;http://u1.3gtv.net:2080/pms-service/common/search_related&#x27;, document.querySelector(&#x27;ul.simpleList&#x27;), {
		objectId : &#x27;223946&#x27;,
		objectType : 1,
		start : 0,
		limit : 10
	},
	{
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