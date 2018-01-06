<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "Tab 混合 List"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
		<br />
		<div class="p">
	    	<h3>Tab 混合 List</h3>
    	</div>
    	<div class="p">
    	 <ul>
    	 	<li></li>
    	 </ul>
    	</div>
		<div class="p">
	    	<h3>垂直 Tab</h3>
    	</div>
    	<div class="center" style="padding:5px;">
			<div class="tab_group_1 tab2">
				<ul>
					<li class="id_2266">热点新闻</li>
					<li class="id_2267">全国新闻</li>
					<li class="id_2268">本地新闻</li>
				</ul>
				<div class="content">
					<div>
						<ul>加载中……</ul>
					</div>
					<div>
						<ul>加载中……</ul>
					</div>
					<div>
						<ul>加载中……</ul>
					</div>
				</div>
			</div>
			<div class="p">
		    	<h3>更多样式的列表</h3>
	    	</div>
			<div class="tab_group_2 tab2">
				<ul>
					<li class="id_2266">热点新闻</li>
					<li class="id_2267">全国新闻</li>
					<li class="id_2268">本地新闻</li>
				</ul>
				<div class="content">
					<div>
						<ul>加载中……</ul>
					</div>
					<div>
						<ul>加载中……</ul>
					</div>
					<div>
						<ul>加载中……</ul>
						<center>
							<button>下一页</button>
						</center>
					</div>
				</div>
			</div>
    	</div>
 
<script>
	var tab_group_1 = new ajaxjs.SimpleTab(document.querySelector('.tab_group_1'));
	tab_group_1.afterRender = function(i, btn, tab){
		ajaxjs.List('../../public_service', tab.querySelector('ul'), {
				id : btn.className.match(/\d+/)[0],
				portalId : 45,
				start:0,
				limit:4,
				action:'http_proxy',
				url : 'http://u1.3gtv.net:2080/pms-service/section/content_list'
			},
			{
				isNoAutoHeight : true,
				tpl : '<li>\
					<a href="#">[:=name:]<div class="createTime" style="float:right;">[:=createTime:]</div></a>\
					</li>',
				renderer : function rendererItem(data){
					data.createTime = data.createTime.substring(0, 10);
					return data;
				}
			}
		);
	}
	tab_group_1.jump(0);
	
	var tab_group_2 =  new ajaxjs.SimpleTab(document.querySelector('.tab_group_2'));
	tab_group_2.afterRender = function(i, btn, tab) {
		var id = btn.className.match(/\d+/)[0];
		var ul = tab.querySelector('ul');
		switch(Number(id)) {
			case 2266:
				ul.classList.add('simpleList1');
				ajaxjs.List('../../public_service', ul, {
					id : id,
					portalId : 45,
					start:0,
					limit:4,
					action:'http_proxy',
					url : 'http://u1.3gtv.net:2080/pms-service/section/content_list'
				},
				{
					isNoAutoHeight : true,
					tpl : '<li>\
							<a href="#">[:=name:]<div class="createTime">[:=createTime:]</div></a>\
						</li>',
					renderer : function rendererItem(data){
						data.createTime = data.createTime.substring(0, 10);
						return data;
					}
				});
			break;
			case 2267:
				ul.classList.add('simpleList2');
				ajaxjs.List('../../public_service', ul, {
					id : id,
					portalId : 45,
					start:0,
					limit:4,
					action:'http_proxy',
					url : 'http://u1.3gtv.net:2080/pms-service/section/content_list'
				},
				{
					isNoAutoHeight : true,
					tpl : '<li>\
						<div class="box">\
							<div class="imgHolder">\
								<img data-src="[:=horizontalPic:]?w=150" />\
							</div>\
						</div>\
					</li>',
					renderer : function rendererItem(data){
						data.createTime = data.createTime.substring(0, 10);
						return data;
					}
				});
			break;
			case 2268:
				ul.classList.add('simpleList3');
				ajaxjs.List('../../public_service', ul, {
					id : id,
					portalId : 45,
					start:0,
					action:'http_proxy',
					url : 'http://u1.3gtv.net:2080/pms-service/section/content_list'
				},
				{
					isNoAutoHeight : true,
					pager:true,
					pageSize:6,
					loadMoreBtn: tab.querySelector('button'),
					tpl : '<li>\
						<div class="box">\
							<div class="imgHolder">\
								<img data-src="[:=horizontalPic:]?w=150" />\
							</div>\
						</div>\
					</li>',
					renderer : function rendererItem(data){
						data.createTime = data.createTime.substring(0, 10);
						return data;
					}
				});
			break;
		}
	}
	tab_group_2.jump(0);
</script>
    	<br />

	<div style="clear:both;"></div>
 
	<div class="p">
		<h3>注意事项：</h3>
		<ul>
			<li>垂直 Tab 的内容区域应该设置 min-height；或者固定高度</li>
		</ul>
	</div>
	<div class="p">
		<h3>代码如下：</h3>
	</div>
 
	<pre class="prettyprint">
&lt;div class=&quot;tab2&quot;&gt;
	&lt;ul&gt;
		&lt;li class=&quot;selected&quot;&gt;公司简介&lt;/li&gt;
		&lt;li&gt;资质证照&lt;/li&gt;
		&lt;li&gt;荣誉证书&lt;/li&gt;
	&lt;/ul&gt;
	&lt;div class=&quot;content&quot;&gt;
		&lt;div&gt;
			自古以来，……
		&lt;/div&gt;
		&lt;div&gt;
			之所以如此，……
		&lt;/div&gt;
		&lt;div&gt;
			笔者出于个人兴趣，……
		&lt;/div&gt;
	&lt;/div&gt;
&lt;/div&gt;

&lt;script src=&quot;${bigfoot}/js/widget/tab.js&quot;&gt;&lt;/script&gt;
&lt;script&gt;
	new SimpleTab(document.querySelector(&#x27;.tab2&#x27;));
&lt;/script&gt;
	   </pre>
	    <%@include file="../public/footer.jsp" %>
    </body>
</html>