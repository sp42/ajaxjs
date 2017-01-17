<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "折叠菜单"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
   
    	<div class="p">
	    	<h3>折叠菜单</h3>
	    	<p>折叠菜单的特点是同一时间只展开一个区域，其他区域则处于闭合状态。</p>
	    	 <ul>
	    	 	<li>默认 JSONP 获取远程或本地数据，JSONP 可跨域，也可以支持 XHR。由 cfg.isJSONP 决定。</li>
	    	 </ul>
    	</div>
    <div class="p">
	    <h3>例子</h3>
	</div>
		<ul class="leftSidebar">
		    <li class="siteMgr">
		        <h3>网站管理</h3>
		        <ul>
		            <li>
		                <a target="iframepage" href="website/profile/changePassword.jsp">帐号信息</a>
		            </li>
		            <li>
		                <a target="iframepage" href="website/profile/companyInfo.jsp">公司信息</a>
		            </li>
		            <li>
		                <a target="iframepage" href="website/gallery/companyAlbum.jsp">相 册</a>
		            </li>
		            <li>
		                <a target="iframepage" href="website/pageEditor/index.jsp">页面维护</a>
		            </li>
		            <li>
		                <a target="iframepage" href="website/profile/global.jsp">全局信息</a>
		            </li>
		        </ul>
		    </li>
		
		    <li class="news">
		        <h3>新闻中心管理</h3>
		        <ul>
		            <li>
		                <a target="iframepage" href="entry/list/news">新闻管理</a>
		            </li>
		        </ul>
		    </li>
		    <li class="levelPoints">
		        <h3>积分系统</h3>
		        <ul>
		            <li>
		                <a target="iframepage" href="levelPoints/memberTree.jsp">会员下线一览</a>
		            </li>
		            <li>
		                <a target="iframepage" href="levelPoints/showUserPoints.jsp">显示会员积分</a>
		            </li>
		        </ul>
		    </li>
		    <li>
		        <h3>其他信息</h3>
		        <ul>
		            <li>
		                <a target="iframepage" href="../job/job.jsp">留言管理</a>
		            </li>
		            <li>
		                <a target="iframepage" href="../job/job.jsp">招聘管理</a>
		            </li>
		        </ul>
		    </li>
		</ul>
	 	<script src="${bigfoot}/js/widget/acMenu.js"></script>
	 	<script>
    	// 初始化菜单
    	new bf_acMenu(document.querySelector('.leftSidebar'));
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