<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "图文列表"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
   
    	<div class="p">
	    	<h3>图文列表</h3>
	    	<p>图文列表，更多是在样式上的控制，以及围绕图片如何处理和优化的 JS。</p>
	    	 <ul>
	    	 	<li></li>
	    	 </ul>
    	</div>
    	
	    <div class="p">
			<h3>注意事项：</h3>
			<ul>
				<li></li>
			</ul>
		</div>
		
		<textarea class="itemTpl hide">
			<li>
				<a href="{href}">
					<div class="imgHolder noImg">
						<img data-src="<%= request.getParameter("style") != null  && request.getParameter("style").equals("col3")?"{verticalPic}":"{horizontalPic}"%>?w={0}"
						 height="{height}" 
						 onload="this.addCls('tran')" />
						<div class="icon {icon}"></div>
					</div>
					<h4>{name}</h4>
					<p>{createTime}</p>
				</a>
			</li>
		</textarea> 
	    <div class="p">
		    <h3>累加方式分页例子，isAppend = true</h3>
		</div>
	 	<ul class="simpleList1"></ul>
	 	<button class="nextPageBtn1 center" style="display:block;margin-top:10px;">下一页</button>
	 	
	 	<script src="${bigfoot}/js/widget/list.js"></script>
	
	 	<script>
	bf_list(
		'http://u1.3gtv.net:2080/pms-service/section/content_list', 
		document.querySelector('ul.simpleList1'), 
		{
			id : 2268,
			portalId : 45,
			start:0
		},
		{
	 		pager : true,
	 		isAppend: true,
	 		loadMoreBtn:'.nextPageBtn1',
			isNoAutoHeight : true,
			pageSize:3,
			tpl : document.querySelector('.itemTpl').value,
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
 	
 	
 	<script src="${bigfoot}/js/widget/list.js"></script>
 	
 	<script>
bf_list(
	'http://u1.3gtv.net:2080/pms-service/section/content_list', 
	document.querySelector('ul.simpleList2'), 
	{
		id : 2268,
		portalId : 45,
		start:0
	},
	{
 		pager : true,
 		isAppend: false,
 		loadMoreBtn:'.nextPageBtn2',
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