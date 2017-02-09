<%@page pageEncoding="UTF-8"%>	
<%@taglib prefix="commonTag" tagdir="/WEB-INF/tags/common"%>
<%@taglib prefix="c"  		 uri="/ajaxjs"%>
<%
	//request.setAttribute("moreDocument", "/album/document.jsp");
%>
<!DOCTYPE html>
<html>
<head>
	<commonTag:head lessFile="/asset/bigfoot/asset/less/pages.less" title="文档" />
  	<style>
  		a{
  			text-decoration: underline!important;
  		}
		h2, h3, p {
			margin: 15px 0;
			margin-left: 30px;
		}
		
		h4, p, pre, iframe, ul.list {
			margin-left: 50px !important;
		}
		
		ul.list li{
			list-style-type: disc;
			margin-left: 1%;
			margin-right: 20%;
		}
		
		td {
			background-color: white;
			padding: 6px 20px !important;
		}
		
		h3 {
			clear: both;
		}
		
		.longBtn {
			display: block;
			clear: both;
			margin-top: 15px !important;
			width: 95%;
		}
		
		.tab2 {
			width: 95%;
		}
		
		.tab2 li {
			/*       	list-style-position: inside; */
			list-style-type: none;
		}
		
		body {
			padding-top: 60px;
		}
		
		header {
			width: 100%;
			background-color: rgba(239, 239, 239, 0.6);
			border-bottom: 2px solid gray;
			position: fixed;
			top: 0;
			left: 0;
			height: 50px;
			z-index: 9999;
		}
		
		header h1 {
			line-height: 50px;
			font-size: 130%;
		}
		
		header menu {
			position: absolute;
			width: 15%;
			top: 50px;
			right: 0;
			background-color: #f4f4f4;
			border: 2px solid gray;
			border-top: 0;
			border-right: 0;
			margin: 0;
			padding: 1% 2%;
		}
		
		header menu ol {
			margin: 0;
		}
		
		header menu ol li {
			
		}
		
		header menu ol ul li a {
			font-size: 80%;
		}
		
		//
		code pretty
			.pln {
			color: #000
		}
		
		@media screen {
			.str {
				color: #080
			}
			.kwd {
				color: #008
			}
			.com {
				color: #800
			}
			.typ {
				color: #606
			}
			.lit {
				color: #066
			}
			.pun, .opn, .clo {
				color: #660
			}
			.tag {
				color: #008
			}
			.atn {
				color: #606
			}
			.atv {
				color: #080
			}
			.dec, .var {
				color: #606
			}
			.fun {
				color: red
			}
		}
		
		@media print , projection {
			.str {
				color: #060
			}
			.kwd {
				color: #006;
				font-weight: bold
			}
			.com {
				color: #600;
				font-style: italic
			}
			.typ {
				color: #404;
				font-weight: bold
			}
			.lit {
				color: #044
			}
			.pun, .opn, .clo {
				color: #440
			}
			.tag {
				color: #006;
				font-weight: bold
			}
			.atn {
				color: #404
			}
			.atv {
				color: #060
			}
		}
		
		pre.prettyprint {
			padding: 2px;
			border: 1px solid #888
		}
		
		ol.linenums {
			margin-top: 0;
			margin-bottom: 0
		}
		
		li.L0, li.L1, li.L2, li.L3, li.L5, li.L6, li.L7, li.L8 {
			list-style-type: none
		}
		
		li.L1, li.L3, li.L5, li.L7, li.L9 {
			background: #eee
		}
		
		pre.prettyprint {
			border: 0;
			border-left: 3px solid lightgray;
			padding: .5%;
		}
		
		table.aj-borderTable {
			margin-left: 2%;
		}
		
		iframe {
			width: 60%;
			max-height: 300px;
			border: 1px solid lightgray;
			padding-left: 2%;
		}
		
		p {
			max-width: 78%;
		}
		
		table.aj-borderTable {
			margin-left: 7%;
		}
</style>
	<script src="../../asset/bigfoot/js/libs/run_prettify.js"></script> 
</head>
  <body>
	  <header>
	    <h1 style="text-align: center;">实体：${uiName} 文档</h1>
	    <menu>
			目 录：
			<ol>
				<li>
					<a href="#intro">概述</a>
				</li>
				<li>
					<a href="#list">列表组件</a>
					<ul>
						<li>
							<a href="#list_col_0">纯文字列表</a>
						</li>
						<li>
							<a href="#list_col_1">单列图文例子,分页并指定读取分页数</a>
						</li>
						<li>
							<a href="#list_col_2">双列图文例子，分页，覆盖方式</a>
						</li>
						<li>
							<a href="#list_col_3">三列图文例子，分页，追加方式</a>
						</li>
					</ul>
				</li>
				<li><a href="#tab">Tab 组件</a></li>
				<li><a href="#gallery">相册</a></li>
				<li><a href="#combo">多种组合的样式</a></li>
			</ol>
		</menu>
	  </header>
    <a name="intro"></a>
 
    <h2>概述</h2>
    <p>为了介绍接口用法特设定该文档。API 接口只提供纯数据输出或者写行为，不包括样式或者模板呈现。</p>
    <p>默认地，POJO 字段与数据库字段的命名是一致的；在前台表单或者后台接口的命名也是一致的。</p>
    <p>特别地，该文档为${uiName}&nbsp; ${tableName} 而设。${entityInfo}。</p>
    <table class="aj-borderTable" align="center">
    	<tr>
    		<th>名称</th>
    		<th>说明</th>
    		<th>数据库类型</th>
    		<th>允许 null？</th>
    		<th>长度</th>
    		<th>验证：必填？/格式</th>
    	</tr>
    	<c:foreach items="${meta}" var="current">
    	<tr>
			<td>${current.name}</td>    	
			<td>${current.description}</td>    	
			<td title="Java 类型：${current.returnType}">${current.dbMetaInfo.typeName}</td>    	
			<td align="center">${current.dbMetaInfo.nullable}</td>    	  	
			<td align="center">${current.dbMetaInfo.size}</td>    	
			<td align="center">${current.nullable}/${current.pattern}</td>    	
    	</tr>
    	</c:foreach>
    </table>
    <p>辨別 Null 与 Not Null ：Null 为允许储存空值(Null)。255 字节可储存 128 个汉字。</p>

    <a name="list"></a>
    <h2>接口说明</h2>
    <p>请求方式：HTTP GET；返回结果：JSON格式（响应 ContentType 为“application/json”）；字符编码：UTF-8。</p>
    <h3>全局参数</h3>
    <p>不管什么接口总可以参数以下参数，如表格所示。</p>
    <table class="aj-borderTable" align="center">
	    <tr>
	    	<th>参数名</th><th>说明</th><th>参数类型</th><th>是否必填</th><th>默认值</th>
	    </tr>
	    <tr>
	    	<td>portalId</td><td>门户 id</td><td>int</td><td>false</td><td>n/a</td>
	    </tr>
	    <tr>
	    	<td>cache</td><td>是否使用缓存，true=使用，false=不使用</td><td>boolean</td><td>false</td><td>true</td>
	    </tr>
	    <tr>
	    	<td>testMode</td><td>是否使用测试版本的接口，用于开发阶段时候用，true=使用，false=不使用</td><td>boolean</td><td>false</td><td>false</td>
	    </tr>
    </table>
    <p>以上参数应以 HTTP GET 请求附加在 URL 中（即使 POST 数据）。</p>
    
    <h3>${uiName}接口一览表</h3>
	<table class="aj-borderTable" align="center">
		<tr>
			<td>POST /service/${tableName}</td>
			<td>创建一个${uiName}</td>
		</tr>
		<tr>
			<td>POST /service/${tableName}/1</td>
			<td>修改 id 为 2 的${uiName}</td>
		</tr>
		<tr>
			<td>DELETE /service/${tableName}/1</td>
			<td>删除 id 为 2 的${uiName}</td>
		</tr>
		<tr>
			<td>GET /service/${tableName}/1</td>
			<td>读取单个${uiName}，id 为 2</td>
		</tr>
		<tr>
			<td>GET /service/${tableName}</td>
			<td>读取${uiName}列表，默认第一页分页（0-9）</td>
		</tr>
		<tr>
			<td>GET /service/${tableName}?start=10&amp;limit=5</td>
			<td>读取${uiName}列表，并指定分页参数start=10&amp;limit=5</td>
		</tr>
		<tr>
			<td>GET /service/${tableName}?catalog=company_news</td>
			<td>读取${uiName}列表，指定分类为 catalog=company_news，默认第一页分页（0-9）</td>
		</tr>
		<tr>
			<td>GET
				/service/${tableName}?catalog=company_news&amp;start=10&amp;limit=5</td>
			<td>读取${uiName}列表，指定分类为 catalog=company_news，并指定分页参数start=10&amp;limit=5</td>
		</tr>
		<tr>
			<td>GET/service/${tableName}/img</td>
			<td>读取${uiName}下面所有的关联对象的列表</td>
		</tr>
	</table>

	<p>下面是${uiName}各个接口详解。</p>
    <h3>获取多个${uiName}实体接口（列表）</h3>
    <p>获取多个${uiName}实体，如 GET /${tableName}，表示获取批量${uiName}数据。</p>
    <p>请求实例：http://{serverUrl}/service/${tableName}。</p>
	<p>JSON 响应内容如下（<a href="../${tableName}/list.json">点击这里查看例子</a>）：</p>
	<iframe src="../${tableName}/list.json"></iframe>
	
	<p>列表响应内容的 JSON 结构如下所示：</p>
<pre class="prettyprint">{
	total :  1020, // 总记录数
	result: [ // 记录集合，包含一个或多个实体的数组。
	   {
        	// 丰富的实体内容
	   },
	   ……
	]
}</pre>
	<p>如果查询结果为零，那么返回 JSON 如下：</p>
	<pre class="prettyprint">{
    total : 0,
    result : null 
}</pre>
    <p>该接口是只读接口，没有任何写操作。list 接口有分页参数、查询参数、搜索参数和排序参数这四种。</p>
 
    
    <h4>分页参数</h4>
    <p>分页采用 LIMIT # OFFSET # 支持句法，如下分页参数说明表格所示。 </p>
    <table class="aj-borderTable" align="center">
	    <tr>
	    	<th>参数名</th><th>说明</th><th>参数类型</th><th>是否必填</th><th>默认值</th>
	    </tr>
	    <tr>
	    	<td>start</td><td>分页参数之起始行数</td><td>int</td><td>false</td><td>0</td>
	    </tr>
	    <tr>
	    	<td>limit</td><td>分页参数之读取记录数</td><td>int</td><td>false</td><td>8</td>
	    </tr>
    </table>

	<p>请求实例：<a href="../${tableName}?start=5&limit=3">http://{serverUrl}/service/${tableName}?start=5&amp;limit=3</a>，其中 start=5&amp;limit=3 表示读取列表 5-8 条记录。</p>
	
	

	<h4>查询参数</h4>
	<p>采用 key/value 结对方式对应欲查询的“字段名”和“条件值”。可支持多个条件的输入，此时相当于传入数组，构成 SQL WHERE 语句的 AND 关系。参数说明如查询参数说明表格所示。 </p>
    <table class="aj-borderTable"> 
	    <tr>
	    	<th>参数名</th><th>说明</th><th>参数类型</th><th>是否必填</th><th>默认值</th>
	    </tr>
	    <tr>
	    	<td>filterField</td><td>查询的字段名。可以传入多个 fields</td><td>string</td><td>false</td><td>n/a</td>
	    </tr>
	    <tr>
	    	<td>filterValue</td><td>查询的条件。可传入多个 keywords。</td><td>string</td><td>false</td><td>n/a</td>
	    </tr>
    </table>
    
	<p>请求实例：<a href="../${tableName}?filterField=catalog&filterValue=16">http://{serverUrl}/service/${tableName}?filterField=catalog&amp;filterValue=16</a>，表示查询实体“分类”为 2 的条件输入。注意 fields 允许出现 “.（点号）”的写法，如 catelog.id。
	</p>
	<p> 其他例子如:  <a href="../${tableName}?filterField=catalog&filterValue=16&filterField=portalId&filterValue=17">http://{serverUrl}/service/${tableName}?filterField=catalog&amp;filterValue=16&amp;filterField=portalId&amp;filterValue=17</a>，表示同时查询符合 catelog = 3 并且 portalId = 17 条件的记录。多个 fields 与 keywords 按照出现的次序一一对应着。</p>
	
	<br />
	
	<h4>搜索参数</h4>
	<p>搜索一个或多个字段（使用 SQL LIKE 匹配），如搜索参数说明表格所示。</p>
    <table class="aj-borderTable">
	    <tr>
	    	<th>参数名</th><th>说明</th><th>参数类型</th><th>是否必填</th><th>默认值</th>
	    </tr>
	    <tr>
	    	<td>searchField</td><td>欲搜索的字段名</td><td>string</td><td>false</td><td>n/a</td>
	    </tr>
	    <tr>
	    	<td>searchValue</td><td>搜索的关键字</td><td>string</td><td>false</td><td>n/a</td>
	    </tr>
    </table>
	<p>请求实例：<a href="../${tableName}?searchField=name&searchValue=nba">http://{serverUrl}/service/${tableName}?searchField=name&amp;searchValue=nba</a>，表示对 name 字段关键字搜索 nba。</p>


	<h4>排序参数</h4>
	<p>指定某一列字段名和方向进行排序，如排序参数说明表格所示。</p>
    <table class="aj-borderTable">
	    <tr>
	    	<th>参数名</th><th>说明</th><th>参数类型</th><th>是否必填</th><th>默认值</th>
	    </tr>
	    <tr>
	    	<td>sortField</td><td>排序的字段名</td><td>string</td><td>false</td><td>n/a</td>
	    </tr>
	    <tr>
	    	<td>sortValue</td><td>排序的方向，只允许传入 ASC（升序）| DESC（降序）</td><td>string</td><td>false</td><td>n/a</td>
	    </tr>
    </table>
	<p>请求实例：<a href="../${tableName}?sortField=name&sortValue=ASC">http://{serverUrl}/service/${tableName}?sortField=hotLevel&amp;sortValue=ASC</a>，在 hotLevel （热度）的字段上按照 ASC 升序排序。</p>
	

    
    <h3>获取单个${uiName}实体接口</h3>
    <p>获取单个${uiName}实体，如 GET /${tableName}/100，表示获取${uiName} id 为 100 的记录，最后一位 int 是${uiName}的 id。</p>
    <p>请求实例：http://{serverUrl}/service/${tableName}/100。</p>
    <p>JSON 响应内容如下（<a href="100.json">点击这里查看例子</a>）：</p>
    <iframe src="100.json"></iframe>
	<p>如果查询结果为零，那么返回 JSON 如下：</p> 
	<pre class="prettyprint">{
    total : 0,
    result : null // or result : []
}</pre>

	<h3>新增${uiName}实体接口</h3>
		<p>新增${uiText}实体操作：POST /${tableName}。</p>
	    <p>使用标准 POST 表单结构，参数为实体各个的：字段名+值。注意 ? &amp; 等的符号需要转义。</p>
	    <p>JSON 响应内容如下：</p>
	    <pre class="prettyprint">{
	isOk : true|false, // 是否新增成功，true 表示为新增操作成功，false 为新增操作失败
	newlyId : 1000 // 新增实体的 id
}</pre>

	<h3>修改${uiName}实体接口</h3>
		<p>修改${uiText}实体操作：POST /${tableName}/1000，最后一位 int 是${uiName}的 id。</p>
	    <p>使用标准 POST 表单结构，参数为实体各个的：字段名+值。注意 ? &amp; 等的符号需要转义。</p>
	    <p>JSON 响应内容如下：</p>
	    <pre class="prettyprint">{
	isOk : true|false // 是否修改成功，true 表示为修改操作成功，false 为修改操作失败
}</pre>


	<h3>删除单个${uiName}实体接口</h3>
	<p>删除${uiText}实体操作：DELETE /${tableName}/1000，最后一位 int 是${uiName}的 id。后端暂不支持批量删除。批量删除由客户端通过多次删除实现。</p>
    <p>必须使用 HTTP DELETE 方法。</p>
    <p>JSON 响应内容如下：</p>
    <pre class="prettyprint">{
	isOk : true|false // 是否删除成功，true 表示为删除成功，false 为删除失败
}</pre>
<c:if test="${not empty moreDocument}">
	<jsp:include page="${moreDocument}" flush="true" />
</c:if>
<br />
<br />
<br />
</body></html>