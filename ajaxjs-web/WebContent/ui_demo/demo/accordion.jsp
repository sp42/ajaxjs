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
	 	<script>
	    	// 初始化菜单
	    	new ajaxjs.AccordionMenu(document.querySelector('.leftSidebar'));
	    </script>
		 
		<br />
		
 
		<div class="p">
			<h3>JS 代码如下：</h3>
		</div>
		<pre class="prettyprint" style="height:30px;">
new ajaxjs.AccordionMenu(document.querySelector('.leftSidebar'));
		</pre>	

<div class="p">
			<h3>HTML 代码如下：</h3>
		</div>
		<pre class="prettyprint">
&lt;ul class=&quot;leftSidebar&quot;&gt;
    &lt;li class=&quot;siteMgr&quot;&gt;
        &lt;h3&gt;网站管理&lt;/h3&gt;
        &lt;ul&gt;
   
            &lt;li&gt;
                &lt;a target=&quot;iframepage&quot; href=&quot;website/gallery/companyAlbum.jsp&quot;&gt;相 册&lt;/a&gt;
            &lt;/li&gt;
            &lt;li&gt;
                &lt;a target=&quot;iframepage&quot; href=&quot;website/pageEditor/index.jsp&quot;&gt;页面维护&lt;/a&gt;
            &lt;/li&gt;
            &lt;li&gt;
                &lt;a target=&quot;iframepage&quot; href=&quot;website/profile/global.jsp&quot;&gt;全局信息&lt;/a&gt;
            &lt;/li&gt;
        &lt;/ul&gt;
    &lt;/li&gt;

    &lt;li class=&quot;news&quot;&gt;
        &lt;h3&gt;新闻中心管理&lt;/h3&gt;
        &lt;ul&gt;
            &lt;li&gt;
                &lt;a target=&quot;iframepage&quot; href=&quot;entry/list/news&quot;&gt;新闻管理&lt;/a&gt;
            &lt;/li&gt;
        &lt;/ul&gt;
    &lt;/li&gt;
    &lt;li class=&quot;levelPoints&quot;&gt;
        &lt;h3&gt;积分系统&lt;/h3&gt;
        &lt;ul&gt;
            &lt;li&gt;
                &lt;a target=&quot;iframepage&quot; href=&quot;levelPoints/memberTree.jsp&quot;&gt;会员下线一览&lt;/a&gt;
            &lt;/li&gt;
            &lt;li&gt;
                &lt;a target=&quot;iframepage&quot; href=&quot;levelPoints/showUserPoints.jsp&quot;&gt;显示会员积分&lt;/a&gt;
            &lt;/li&gt;
        &lt;/ul&gt;
    &lt;/li&gt;
&lt;/ul&gt;
		</pre>			
		
		<div class="p">
			<h3>LESS 样式如下：</h3>
		</div>
		<pre class="prettyprint">
.leftSidebar {
	.acMenu ();
	width: 20%;
	margin: 3% auto;
	overflow: hidden;
	& > li {
		border-top: 1px solid white;
		border-bottom: 1px solid lightgray;
		&.pressed {
			border-top: 0;
			border-bottom: 1px solid lightgray;
			h3 {
				// .hozGradient_3 (transparent , darken(@mainColor, 10));
			}
		}

		ul {
			li {
				list-style-type: disc;
				padding-left: 10%;
				a {
					width: 100%;
					display: block;
				}

				&.selected {
					// background-color: lighten(@hightColor, 33);
					a {
						color: black;
					}
				}
			}
		}

		h3 , li {
			padding: 5px 0 5px 15px;
			letter-spacing: 2px;
			line-height: 20px;
			color: #939da8;
			font-size: 12px;
			&:hover , a:hover {
				color: black;
			}
		}
	}
}
	    </pre>
		
	    <%@include file="../public/footer.jsp" %>
    </body>
</html>