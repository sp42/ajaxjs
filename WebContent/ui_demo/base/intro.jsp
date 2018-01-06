<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "概述");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<h4>CSS RESET</h4>
	<p>CSS 重设就是由于各种浏览器解释 CSS 样式的初始值有所不同，导致在没有定义某个 CSS
		属性时，不同的浏览器会按照自己的默认值来为没有定义的样式赋值，所以我们要先定义好一些 CSS 样式，来让所有浏览器都按照同样的规则解释
		CSS，这样就能避免发生这种问题。</p>

	<pre class="prettyprint">
body,dl,dt,dd,ul,li,pre,form,fieldset,input,p,blockquote,th,td,h1,h2,h3,h4,h5{margin:0;padding:0;} /* 消除内外边距 */
h1,h2,h3,h4,h5{font-weight: normal;}  /* 不希望标题粗体 */
img{border:0;}	/* 图片没有边距 */
ul li{list-style-type:none}	/* 列表消除标记 */
.hide{display:none}	/* 提供隐藏的样式类 */
body{
	-webkit-font-smoothing:antialiased; /* 抗锯齿字体 Mac Safari Only */
	-moz-osx-font-smoothing: grayscale; /* 抗锯齿字体 Firefox Only */
	font-family: "Lantinghei SC", "Open Sans", Arial, "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", "STHeiti", "WenQuanYi Micro Hei", SimSun, sans-serif;
}

/* 以下部分可以不加入 */
a{
	text-decoration:none;
	color:#999;
	transition : color 400ms ease-in-out; /* 使链接有渐显动画效果 */
}

a:hover{
	color:black;
	text-decoration:underline;
}

/* 复位按钮 */
button {
	border:none;
	outline: none;
	cursor: pointer;
	letter-spacing: 2px;
	text-align: center;
	-webkit-user-select: none; /* 不可选择文本 */
	-moz-user-select: none;
	user-select: none;
}
select, input[type=text], input[type=password], textarea {
	outline:none;
	-moz-appearance:none;/* for mac */
}
	</pre>
	
	<h4>LESS.js</h4>
	
	<p>LESS.js 非常好用！推荐给大家！我们几乎所有样式都不是直接书写 CSS，而是采用
		LESS.js。当然简单的样式就没必要使用 LESS。</p>
	<p>当前笔者封装了两个 LESS 库</p>
	<ul class="centerLimitWidth list">
		<li>common_style.less 基础样式库</li>
		<li>common_component.less 组件库，须依赖 common_style</li>
	</ul>
	<p>
		LESS 可以服务端生成，服务端方法参见笔者<a
			href="http://blog.csdn.net/zhangxin09/article/details/53889045">博文</a>；也可以客户端生成。客户端引入方法如下。
	</p>
	<pre class="prettyprint">&lt;link rel=&quot;stylesheet/less&quot; type=&quot;text/css&quot; href=&quot;../less/common_component.less&quot; /&gt;
&lt;script src=&quot;../less.min.js&quot;&gt;&lt;/script&gt;
</pre>
 
	<h4>文章段落</h4>
	<p>class="ajaxjs-text-p" 文章段落 例子如下</p>
	<p>“北上广深同比涨幅高达20%，26个主要城市涨幅超10%，69个城市房价上涨……”2013年全国房价涨声一片，这与年初各地制定的房价控制目标相差甚远。经济学者马光远[微博]表示，房价调控目标是政府对于百姓的“承诺”，没有完成目标而“爽约”，蒙混过关只会丧失公信力。</p>

	<p>class="ajaxjs-text-quotation" 文章段落（引用）例子如下</p>
	<p class="ajaxjs-text-quotation">
		北上广深同比涨幅高达20%，26个主要城市涨幅超10%，69个城市房价上涨……”2013年全国房价涨声一片，这与年初各地制定的房价控制目标相差甚远。经济学者马光远[微博]表示，房价调控目标是政府对于百姓的“承诺”，没有完成目标而“爽约”，蒙混过关只会丧失公信力。</p>
	
	<h4>标题过长省略号</h4>
	
	<p>需要设置文本宽度。</p>
	<style>
.noWarp li {
	white-space: nowrap;
	text-overflow: ellipsis;
	overflow: hidden;
}
</style>
	<div class="centerLimitWidth">
		<ul class="list noWarp">
			<li style="width: 150px;">国土部严查小产权房拆除一批查处一批追究一批</li>
			<li>学规定 强素质 促发展——XXXX集团员工认真学习【员工手册】</li>
		</ul>
	</div>
	<p>标题过长省略号（多行） 目前仅支持 webkit 浏览器。</p>
	<div class="centerLimitWidth box"
		style="width: 90%; display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden; padding: 0;">
		【大众点评与美团网合并
		王兴张涛担任联席CEO】大众点评网与美团网今天联合发布声明，宣布达成战略合作，双方已共同成立一家新公司。新公司将实施Co-CEO制度，美团CEO王兴和大众点评CEO张涛将同时担任联席CEO和联席董事长，重大决策将在联席CEO和董事会层面完成。
	</div>

	<h4>常见字符</h4>
	
	<p>&amp; &lt; &gt; &quot; » → ⇒ ⇄ ↑ ↩ ⤢ ↱ ↷ ↻ ► ☛ © ® ™ • · » § ×</p>

	<%@include file="../public/footer.jsp"%>
</body>
</html>