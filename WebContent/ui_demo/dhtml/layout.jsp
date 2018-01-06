<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "常见布局");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>

	<h4>单行浮动</h4>
	
	<p>浮动元素要写在前面。</p>
	<div class="box centerLimitWidth" style="width: 310px;text-align:center;">
		<div style="float:left;">&lt;</div>
		<div style="float:right;">⌂</div>
		Title
	</div>

	<h4>相对/绝对布局</h4>
	
	<p>外面为 position: relative；里面构建坐标系，position: absolute。</p>
	<div class="box centerLimitWidth"
		style="position: relative; width: 300px; height: 150px;">
		position: relative
		<div class="box"
			style="position: absolute; background-color: yellow; width: 60px; height: 50px; bottom: 20px; left: 30px;">position:
			absolute</div>
	</div>

	<h4>全屏幕背景</h4>
	
	<p>一般该 div 放在 document.body下一层，且 body 有高度 100%。</p>
	<p>
		<button class="ajaxjs-btn"
			onclick="document.querySelector('.fullScreenMask').classList.toggle('hide');">点击演示</button>
	</p>
	<div class="fullScreenMask hide"
		style="width: 100%; height: 100%; position: fixed; top: 0; left: 0; z-index: 9999998; background-color: rgba(0, 0, 0, .5);"
		onclick="document.querySelector('.fullScreenMask').classList.toggle('hide');"></div>

	<h4>移动端页面宽度</h4>
    
    <p>	
    &lt;meta name="viewport" content="width=320,user-scalable=0,initial-scale=1.0,maximum-scale=1.0, minimum-scale=1.0" /&gt;</p>

	<h4>手机区域内滚动</h4>
		
	<p>
		使用样式 -webkit-overflow-scrolling : touch;，iOS 有滚动回弹效果。
	</p>
	<p>
		自定义滚动条样式（只对 webkit 有效）：因为大量使用 -webkit-overflow-scrolling: touch; 区域内局部滚动，所以在 pc 上应该要让滚动条变细，不然会遮住内容。手机端上冇此问题 
	</p>
	<div class="box centerLimitWidth" style="width:300px;height:150px;-webkit-overflow-scrolling: touch;overflow: auto;">
	 昨晚闲暇时翻看了下之前加的一些QQ群准备整理下，突然发现之前加的几个seo讨论群已经不知不觉间被遗忘了。与前几年seo行业的火热相比，现在的seo行业已经显得有些落寞了。身边不少做seo的朋友都已经转行做其他的了，本人也已经转行php1年多了，只是业余时间优化下自己的一个工作室网站了。seo从业者的减少已经看出很多企业已经放弃自然优化了，有的选择了更快捷的付费推广，也有的直接退出互联网专门做线下去了。搜seo招聘，岗位也是少的几乎没有了。是seo这个行业真的没有效果了吗?
	</div>	
 <style>
			/* 自定义滚动条样式（只对 webkit 有效） 这是全局的，要针对某一元素可以 div::-webkit-scrollbar */
			::-webkit-scrollbar {
			    width : 7px; /* 对垂直流动条有效 */
			    height: 5px; /* 对水平流动条有效 */
			}
			
			::-webkit-scrollbar-thumb {
			    background-color: lightgray;
			    //border: 1px solid gray;
			    border-radius:3px;
			}
			
			::-webkit-scrollbar-thumb:window-inactive {
			    background-color: rgba(0, 0, 0, 0);
			}
		</style>
	
<p>如果结构如下 div.areaScroll > ul > li，则不能 float:left，必须 float: none;display: inline-block;</p>
	
		<div class="box centerLimitWidth" style="width:300px;height:20px;
			overflow-x: auto;white-space: nowrap; auto;-webkit-overflow-scrolling: touch;overflow: hidden;">
			TODO
		</div>	

	<style>
ul.tab {
	overflow: hidden;
	width: 30%;
}

ul.tab li {
	float: left;
	text-align: center;
	cursor: pointer;
	border-bottom: 3px solid lightgray;
	padding-bottom: 5px;
	width: 33.3%;
}

ul.tab li.active {
	border-bottom-color: #0084C9;
	color: #0084C9;
}
</style>

	<h4>高亮 item 效果</h4>
	<p>简单设置底边颜色不同即可。注意这里要使用 js 类：new
		FocusHandler(document.querySelector('.tab'));</p>
	<ul class="tab" style="width:80%;">
		<li class="active">验证</li>
		<li>核对</li>
		<li>支付</li>
	</ul>

	<br />
	<br />

	<p>上例为各个 item 等分宽度，如果不是等分（如下例子），则要设置 ul 底部边框， item 的底边要
		margin-bottom: -Xpx; X 是边框宽度，而且 ul、li 的高度要设置。</p>
	<style>
ul.tab2 {
	border-bottom: 3px solid lightgray;
	width: 80%;
	height: 30px;
}

ul.tab2 li {
	width: 20%;
	margin-bottom: -3px;
	float: left;
	text-align: center;
	cursor: pointer;
	height: 30px;
	line-height: 30px;
	letter-spacing: 6px;
}

ul.tab2 li.active {
	border-bottom: 3px solid #0084C9;
	color: #0084C9;
}
</style>


	<ul class="center tab2" style="width:96%;">
		<li class="active">公开课</li>
		<li>直播课</li>
	</ul>

	<script>
		// 用户点击 UI，UI 得到一个状态，以表示与其他组件不同，例如 tab 下面多个 items 点击状态
		function FocusHandler(el) {
			el.onclick = this.click.bind(this);
			this.items = el.querySelectorAll('li');
		}

		FocusHandler.prototype = {
			itemTag : 'li',
			activeClassName : 'active',
			click : function(e) {
				var li = e.target, items = this.items;
				var index;
				for (var i = 0, j = items.length; i < j; i++) {
					if (items[i] == li) {
						index = i;
						items[i].classList.add(this.activeClassName);
					} else
						items[i].classList.remove(this.activeClassName);
				}

				this.onClick && this.onClick(li, index);// 点击的 item 和索引
			}
		}

		new FocusHandler(document.querySelector('.tab'));
		new FocusHandler(document.querySelector('.tab2'));
	</script>
	<br />

<h4>细线表格 Table</h4>
	
	<p>快速制作 1px 表格边框，为需要设置的 table 元素 class 即可。 class="ajaxjs-borderTable"</p>
	<div class="centerLimitWidth">
		<table class="ajaxjs-borderTable"  width="80%" align="center">
			<tr>
				<th>姓名</th>
				<td>Bill Gates</td>
			</tr>
			<tr>
				<th rowspan="2">电话</th>
				<td>555 77 854</td>
			</tr>
			<tr>
				<td>555 77 855</td>
			</tr>
		</table>
	</div>
	<p>class="ajaxjs-niceTable"</p>
	<div class="centerLimitWidth">
		<table class="ajaxjs-niceTable" width="80%" align="center">
			<thead>
				<tr>
					<th>Month</th>
					<th>Savings</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<td>January</td>
					<td>$100</td>
				</tr>
				<tr>
					<td>February</td>
					<td>$80</td>
				</tr>
			</tbody>
			<tfoot>
				<tr>
					<td>Sum</td>
					<td>$180</td>
				</tr>
			</tfoot>
		</table>
	</div>
	
<h4>提示文字</h4>
	
	<p class="usingClass">上下方向（toBottom、toTop）不需要 JS；提示 div 需指定宽度和 position: relative。</p>
	<div style="position: relative; height: 150px; line-height: 150px;">

		<div class="tipsNote tipsNote-3" style="width: 300px;">
			<div class="aj-arrow toBottom"></div>
			<span>用户名等于账号名；不能与现有的账号名相同；注册后不能修改；</span>
		</div>

		<p class="foo" style="width: 600px; margin-left: 20%;">
			<i>北上广深同比涨幅高达20%，26个主要城市涨幅超10%，69个城市房价上涨……</i>
		</p>

		<div class="tipsNote tipsNote-2" style="width: 450px;">
			<div class="aj-arrow toTop"></div>
			<span>用户名等于账号名；不能与现有的账号名相同；注册后不能修改；</span>
		</div>
		<div class="tipsNote tipsNote-1" style="width: 380px;">
			<div class="aj-arrow toLeft"></div>
			<span>用户名等于账号名；不能与现有的账号名相同；注册后不能修改；</span>
		</div>
		<div class="tipsNote tipsNote-0" style="width: 300px;">
			<div class="aj-arrow toRight"></div>
			<span>用户名等于账号名；不能与现有的账号名相同；注册后不能修改；</span>
		</div>
	</div>
	<style>
.tipsNote {
	position: absolute;
}
/* 重写修改边框颜色 */
.tipsNote-3 {
	background-color: #f5faff;
	border-color: green;
	color: green;
	left: 30%;
	top: -10%;
}

.tipsNote-3 .aj-arrow:after {
	border-top-color: #f5faff; /* 内容颜色 */
}

.tipsNote-3 .aj-arrow:before {
	border-top-color: green; /* 边框颜色 */
}

.tipsNote-2 {
	background-color: #f5faff;
	border-color: #0066cc;
	color: #0066cc;
	left: 30%;
	bottom: 10%;
}

.tipsNote-2 .aj-arrow:after {
	border-bottom-color: #f5faff; /* 内容颜色 */
}

.tipsNote-2 .aj-arrow:before {
	border-bottom-color: #0066cc; /* 边框颜色 */
}

.tipsNote-1 {
	top: 30%;
}

.tipsNote-0 {
	top: 30%;
	left: -20px;
}
</style>
	<script>
		var tipsNoteStyle = document.querySelector('.tipsNote-1').style;
		setTimeout(function() {

			with (document.querySelector('.foo i')) {
				//alert(offsetLeft + offsetWidth)
				tipsNoteStyle.left = offsetLeft + offsetWidth + 20 + 'px';
				//tipsNoteStyle.top  = (-(offsetTop  + offsetHeight) ) + 'px';
			}
		}, 500);
	</script>	

	<h4>分隔样式</h4>
	<p>利用 &lt;fieldset&gt; 制作如下样式。</p>
	<div class="centerLimitWidth" style="width: 98%; padding-left: 1%;">
		<fieldset
			style="border-bottom: 0; border-left: 0; border-right: 0; border-top: 1px solid lightgray; width: 90%;">
			<legend align="center" style="text-align: center;">
				<img src="../../asset/common/images/gs.png" />
			</legend>
		</fieldset>
	</div>

	<%@include file="../public/footer.jsp"%>
</body>
</html>