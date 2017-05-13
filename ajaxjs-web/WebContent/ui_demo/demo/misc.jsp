<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<%request.setAttribute("title", "杂项"); %>
	<%@include file="../public/common.jsp" %>
    <body>
		<%@include file="../public/nav.jsp" %>
   
    	<div class="p">
	    	<h3>md5</h3>
	    	<p>
		    	字符串的“abc”的 md5 加密是 <span class="result1">900150983cd24fb0d6963f7d28e17f72</span>
		    	<button onclick="test();">Test</button>
	    	</p>
			<div class="result2"></div>
			<div class="result"></div>
    	</div>
	 	<script src="${bigfoot}/js/libs/md5.js"></script>
	 	<script>
	    	function test(){
	    		document.querySelector('.result2').innerHTML = 'md5("abc")结果：<span class="md5Result">{0}</span>'.format(md5('abc'));
	    		if(document.querySelector('.result1').innerHTML == document.querySelector('.md5Result').innerHTML){
	    			document.querySelector('.result').innerHTML = '<span style="color:green;">测试通过<span>';
	    		} else document.querySelector('.result').innerHTML = '<span style="color:red;">测试不通过<span>';
	    	}
	    </script>
		 
		<div class="p">
			<h3>依赖 js：</h3>
			<ul>
				<li>/js/libs/md5.js</li>
			</ul>
		</div>
 
		<div class="p">
			<h3>代码如下：</h3>
		</div>
		<pre class="prettyprint" style="height:30px;">var md5Str = md5('abc');
		</pre>	
		
		
    	<div class="p">
	    	<h3>Base64</h3>
	    	<p>
		    	字符串的“abc”的 base64 编码是 <span class="result1_1">YWJj</span> (UTF-8)
		    	<button onclick="test2();">Test</button>
	    	</p>
			<div class="result2_1"></div>
			<div class="result_1"></div>
    	</div>
	 	<script src="${bigfoot}/js/libs/base64.js"></script>
	 	<script>
	    	function test2(){
	    		document.querySelector('.result2_1').innerHTML = 'base("abc")结果：<span class="base64Result">{0}</span>'.format(base64.encode('abc'));
	    		if(document.querySelector('.result1_1').innerHTML == document.querySelector('.base64Result').innerHTML){
	    			document.querySelector('.result_1').innerHTML = '<span style="color:green;">测试通过<span>';
	    		} else document.querySelector('.result_1').innerHTML = '<span style="color:red;">测试不通过<span>';
	    	}
	    </script>
		 
		<div class="p">
			<h3>依赖 js：</h3>
			<ul>
				<li>/js/libs/base64.js</li>
			</ul>
		</div>
 
		<div class="p">
			<h3>代码如下：</h3>
		</div>
		<pre class="prettyprint" style="height:50px;">var base64Str = base64.encode('abc');
var decodeBase64 = base64.decode('base64Str'); // 对应有解码方法
		</pre>	
		
		<div class="p">
			<h3>手机端增强</h3>
			<ul>
				<li>
					区域内局部滚动:控制元素在移动设备上是否使用滚动回弹效果。
					看上去和原生app的效率都有得一拼，要实现这个效果很简单，原理是：-webkit-overflow-scrolling : touch;
				</li>
				<li>.areaScroll(v); 垂直方向滚动；.areaScroll(hoz); 水平方向滚动；</li>
				<li>对横向的 list 有优化。</li>
				<li>
					自定义滚动条样式（只对 webkit 有效）：因为大量使用 -webkit-overflow-scrolling: touch; 区域内局部滚动，所以在 pc 上应该要让滚动条变细，不然会遮住内容。手机端上冇此问题 
				</li>
			</ul>
		</div>
		
		<div class="my-area">
		 昨晚闲暇时翻看了下之前加的一些QQ群准备整理下，突然发现之前加的几个seo讨论群已经不知不觉间被遗忘了。与前几年seo行业的火热相比，现在的seo行业已经显得有些落寞了。身边不少做seo的朋友都已经转行做其他的了，本人也已经转行php1年多了，只是业余时间优化下自己的一个工作室网站了。seo从业者的减少已经看出很多企业已经放弃自然优化了，有的选择了更快捷的付费推广，也有的直接退出互联网专门做线下去了。搜seo招聘，岗位也是少的几乎没有了。是seo这个行业真的没有效果了吗?
		</div>
		<div class="p">
			<h3>样式如下：</h3>
		</div>
		<pre class="prettyprint">

// 区域滚动
.my-area{
	.centerLimitWidth;
	width: 300px;
	height:150px;
	.areaScroll(v);
		::-webkit-scrollbar {
	    width : 7px; /* 对垂直流动条有效 */
	    height: 5px; /* 对水平流动条有效 */
	}
	
	&::-webkit-scrollbar {
	    width : 7px; /* 对垂直流动条有效 */
	    height: 5px; /* 对水平流动条有效 */
	}
	
	&::-webkit-scrollbar-thumb {
	    background-color: lightgray;
	    //border: 1px solid gray;
	    border-radius:3px;
		&:window-inactive {
		    background-color: rgba(0, 0, 0, 0);
		}
	}
}
	    </pre>	
	    <%@include file="../public/footer.jsp" %>
    </body>
</html>