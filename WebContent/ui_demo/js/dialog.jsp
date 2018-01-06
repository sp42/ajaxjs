<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "基本对话框（弹出层）");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<script>
		function showWarningDlg(innerText) {
			new ajaxjs.Popup(
					{
						innerText : '<div class="leftIcon warning">!</div>'
								+ innerText,
						title : '警告',
						hideYES_NO : true
					}).show();
		}
		function showSuccessDlg(innerText) {
			// ✓ &#10003; &#x2713; ✔&#10004;&#x2714;
			new ajaxjs.Popup(
					{
						innerText : '<div class="leftIcon success">✔</div>'
								+ innerText,
						title : '完成',
						closeAsConfirm : true,
						hideYES_NO : true
					}).show();
		}
		function showQueryDlg(innerText) {
			// ✓ &#10003; &#x2713; ✔&#10004;&#x2714;
			new ajaxjs.Popup({
				innerText : '<div class="leftIcon query">?</div>' + innerText,
				title : '询问',
				hideClose : true,
				yesHandler : function() {
					alert('yes');
				},
				noHandler : function() {
					alert('no');
				}
			}).show();
		}
	</script>
	<h4>对话框</h4>
	<p>制作对话框很简单，要点如下。</p>
	<ul class="centerLimitWidth list">
		<li>全屏幕的遮罩 Mask（fixed 定位，100%宽，高度用 js 获取页面内容高度然后赋值，设置透明背景然后注意
			z-index）</li>
		<li>如果不需要 Mask，不加入 &lt;div
			class=&quot;msgbox_mask&quot;&gt;&lt;/div&gt; 元素即可</li>

		<li>窗体居中（fixed 定位，js 计算页面内容高度、宽度减半然后再减去窗体之高度、宽度一半，即为 top、left）</li>
		<li>窗体不会随着页面滚动而定位位置，而是根据浏览器视口（viewport）来定位，因而采用 fixed 定位，计算
			top、left 读取 windows.innerWidth/innerHeight</li>
		<li>关闭窗体很简单，要连同 Mask 一起关闭（关闭动作是多个的，js 中使用了 [].forEach.call 遍历的技巧）</li>
		<li>fadeIn 效果为纯 CSS3 做的，可能一些低版本的浏览器就没有动画效果了</li>
		<li>可以方便地自定义按钮</li>
		<li>键盘 ESC 键关闭窗体</li>
		<li>如果需要拖放的话 dd.js</li>
	</ul>

	<p>更多例子：</p>
	<div class="centerLimitWidth">
		<button class="ajaxjs-btn" onclick="(new ajaxjs.Popup()).show();">点击演示</button>
		<button class="ajaxjs-btn" onclick="showWarningDlg('警告');">警告对话框</button>
		<button class="ajaxjs-btn" onclick="showSuccessDlg('成功或接受');">成功或接受对话框</button>
		<button class="ajaxjs-btn" onclick="showQueryDlg('成功或接受');">询问对话框</button>
	</div>
	<textarea class="hide msgboxTpl">
		<div class="msgbox">
			<h1>title</h1>
			<div class="topCloseBtn closeAction">×</div>
			<div class="inner">
			  人们普遍认为，随着浦东新区和苏州工业园在上世纪90年代的崛起，长三角已超越珠三角成为中国经济增长的第一级，但是本文通过大量的数据分析与调研认为：
			  近几年来，珠三角悄然实现了弯道超车，无论是自主创新能力、民间创富能力，还是可持续发展能力，都比长三角的表现更优。
			  为什么?长三角不是国家钦定的老大哥吗，无论是政策优势、人才优势，还是地缘优势，都比之珠三角更优。珠三角反超的秘密是什么?
			</div>
			<div class="btn">
		 		<div class="yesAction">是</div>
		 		<div class="noAction">否</div>
		 		<div class="closeAction">关闭</div>
			</div>
		</div>
		<div class="msgbox_mask"></div>
   	</textarea>

	<p>HTML 代码如下：</p>

	<pre class="prettyprint">
&lt;div class=&quot;msgbox&quot;&gt;
	&lt;h1&gt;title&lt;/h1&gt;
	&lt;div class=&quot;topCloseBtn closeAction&quot;&gt;×&lt;/div&gt;
	&lt;div class=&quot;inner&quot;&gt;
	  人们普遍认为，随着浦东新区和苏州工业园在上世纪90年代的崛起，长三角已超越珠三角成为中国经济增长的第一级，但是本文通过大量的数据分析与调研认为：
	  近几年来，珠三角悄然实现了弯道超车，无论是自主创新能力、民间创富能力，还是可持续发展能力，都比长三角的表现更优。
	  为什么?长三角不是国家钦定的老大哥吗，无论是政策优势、人才优势，还是地缘优势，都比之珠三角更优。珠三角反超的秘密是什么?
	&lt;/div&gt;
	&lt;div class=&quot;btn&quot;&gt;
 		&lt;div class=&quot;yesAction&quot;&gt;是&lt;/div&gt;
 		&lt;div class=&quot;noAction&quot;&gt;否&lt;/div&gt;
 		&lt;div class=&quot;closeAction&quot;&gt;关闭&lt;/div&gt;
	&lt;/div&gt;
&lt;/div&gt;
&lt;div class=&quot;msgbox_mask&quot;&gt;&lt;/div&gt;
		</pre>
	<p>核心 JS：</p>

	<pre class="prettyprint">
function showWarningDlg(innerText) {
	new ajaxjs.Popup({
		innerText : &#x27;&lt;div class=&quot;leftIcon warning&quot;&gt;!&lt;/div&gt;&#x27; + innerText,
		title : &#x27;警告&#x27;,
		hideYES_NO : true
	}).show();
}

function showSuccessDlg(innerText) {
	// ✓ &amp;#10003; &amp;#x2713; ✔&amp;#10004;&amp;#x2714;
	new ajaxjs.Popup({
		innerText : &#x27;&lt;div class=&quot;leftIcon success&quot;&gt;✔&lt;/div&gt;&#x27; + innerText,
		title : &#x27;完成&#x27;,
		closeAsConfirm : true,
		hideYES_NO : true
	}).show();
}

function showQueryDlg(innerText) {
	// ✓ &amp;#10003; &amp;#x2713; ✔&amp;#10004;&amp;#x2714;
	new ajaxjs.Popup({
		innerText : &#x27;&lt;div class=&quot;leftIcon query&quot;&gt;?&lt;/div&gt;&#x27; + innerText,
		title : &#x27;询问&#x27;,
		hideClose : true,
		yesHandler : function() {
			alert(&#x27;yes&#x27;);
		},
		noHandler : function() {
			alert(&#x27;no&#x27;);
		}
	}).show();
}
		</pre>
	<p>样式如下：</p>
	<pre class="prettyprint">
.msgbox_style_1(){
	background-color:#ebebeb;
	border:1px solid #d3d3d3;
	
	h1{
		font-size:.9rem;
		text-align: center;
		padding:5px 0;
		letter-spacing: 3px;
	}
	.inner{
		background-color:#f7f7f7;
		border:1px solid #dadada;

	}
	.bottomCloseBtn{
		
	}
	.btn{
		&>div{
			background-color:#f7f7f7;
			font-size:.9rem;
			&:hover{
				background-color:lighten(#f7f7f7, 90%);
				border-color:lightgray;
				color:black;
			}
		}
	}
}
.msgbox{
	.fadeIn(.8s);
	position: fixed;
	top:50%;
	.msgbox_style_1;
	width: 50%;
	min-width:300px;
	max-width:400px;
	z-index:99999999;
	margin:auto;
	//border-radius:10px;
	min-height:100px;

	.inner{
		margin:5px;
		margin-top:0;
		padding:10px;
		overflow: hidden;
	}
	
	.topCloseBtn{
		position: absolute;
		text-align: center;
		top:0;
		right:10px;
		width:16px;
		line-height:16px;
		height:16px;
		background-color:gray;
		color:white;
		cursor: pointer;
	}
	.closeAction{
	}
	.btn{
		margin:5px auto;
		width:70%;
		overflow: hidden;
		&>div{
			float:left;
			cursor: pointer;
			text-align: center;
			border:1px solid #d3d3d3;
			padding:3px 10px;
			letter-spacing:3px;
			//margin-right:10px;
			box-sizing: border-box;	
		}
	}
	
	.leftIcon{
		float:left;
		width:50px;
		height:50px;
		margin:0 20px;
		line-height:50px;
		font-size:40px;
		text-shadow: 1px 1px 0px #FFF;
		text-align:center;
		border-radius:32px;
		font-weight:bold;
		border:1px solid lightgray;
		box-shadow: 0px 2px 3px rgba(0, 0, 0, 0.5);
		background: #E3DFE3 url("data:image/gif;base64,R0lGODlhBQAfAMQAAOPf5OPf4+Xh5e7s7vf39/b29ubj5+vo6/Tz9Ozq7e/t7/f29/T09OTh5enm6fX09efl6PDv8fLx8uTg5Pj4+Ojl6ern6uXi5ufk5/Du8Ovp7O3r7fb19vHw8vPy8wAAACH/C1hNUCBEYXRhWE1QPD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iQWRvYmUgWE1QIENvcmUgNS4wLWMwNjAgNjEuMTM0Nzc3LCAyMDEwLzAyLzEyLTE3OjMyOjAwICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1IFdpbmRvd3MiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NzM0NThGMjI1RDUwMTFFMUFDQzg5NjYwRDlCQjA4QzMiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NzM0NThGMjM1RDUwMTFFMUFDQzg5NjYwRDlCQjA4QzMiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo3MzQ1OEYyMDVENTAxMUUxQUNDODk2NjBEOUJCMDhDMyIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo3MzQ1OEYyMTVENTAxMUUxQUNDODk2NjBEOUJCMDhDMyIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PgH//v38+/r5+Pf29fTz8vHw7+7t7Ovq6ejn5uXk4+Lh4N/e3dzb2tnY19bV1NPS0dDPzs3My8rJyMfGxcTDwsHAv769vLu6ubi3trW0s7KxsK+urayrqqmop6alpKOioaCfnp2cm5qZmJeWlZSTkpGQj46NjIuKiYiHhoWEg4KBgH9+fXx7enl4d3Z1dHNycXBvbm1sa2ppaGdmZWRjYmFgX15dXFtaWVhXVlVUU1JRUE9OTUxLSklIR0ZFRENCQUA/Pj08Ozo5ODc2NTQzMjEwLy4tLCsqKSgnJiUkIyIhIB8eHRwbGhkYFxYVFBMSERAPDg0MCwoJCAcGBQQDAgEAACH5BAAAAAAALAAAAAAFAB8AAAVQICWKRFkuKFqsK+e6TxwzNI3ct6frUt93QGBkOMwYjYpkcsBkbp7PhFSqqVYPWKxlu3V4vZVwGEImY85ng1p9abcFcHhjPp/Y7YB8PsDnhwAAOw==") repeat-x scroll center top;
		&.warning{
			color:red;
		}
		&.success{
			color:green;
		}
		&.query{
			color:#cac42e;
		}
	}
}

 
.msgbox_mask{
	.fadeIn(.5s);
	position: fixed;
	top:0;
	left:0;
	width:100%;
	z-index:9999999;
	background-color:rgba(0,0,0, .5);
	
}
	    </pre>
	<%@include file="../public/footer.jsp"%>
</body>
</html>