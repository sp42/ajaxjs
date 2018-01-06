<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<%
	request.setAttribute("title", "跑马灯");
%>
<%@include file="../public/common.jsp"%>
<body>
	<%@include file="../public/nav.jsp"%>
	<h4>跑马灯 Marquee</h4>
	<p>最简单的跑马灯</p> 
	
	<div class="m center" style="width:100%;overflow:hidden;font-size:.8rem;">
		提示：本例代码一般可以通过“鼠标右键查看源码”的方式了解源码。
	</div>
	<script>
		window.setInterval(function(){ // 跑马灯
			var m = document.querySelector('.m');
			var queen = m.innerHTML.split('');
			queen.push(queen.shift());
			m.innerHTML = queen.join('');
		}, 500);
	</script>
	
	<br />
	<p>最简单的跑马灯</p>
	
	<div class="center" style="padding-left: 10Px;font-size:.8rem;">
		<div class="content1">这是一段滚动的文字11111111</div>
		<div class="content2">这是一段滚动的文字22222222</div>
	</div>
	<script>
		/**
		 * @param {Element} el 列表元素
		 * @param {Number} interval 动画时间间隔
		 * @param {Boolean} canstop 是否可以鼠标移入时候暂停动画
		 */
		function ScrollContent_Hoz(el, interval, canstop) {
			interval = interval || 500;
			canstop = canstop || false;
			var arr = el.innerHTML.split("");
			function scroll() {
				arr.push(arr.shift());
				el.innerHTML = arr.join("");
			}

			var t = window.setInterval(scroll, interval);

			if (canstop) {
				el.onmouseover = function() {
					if (t)
						window.clearInterval(t);
				}
				el.onmouseout = function() {
					t = window.setInterval(scroll, interval);
				}
			}
		}

		new ScrollContent_Hoz(document.querySelector('.content1'));
		new ScrollContent_Hoz(document.querySelector('.content2'), null, true);
	</script>

	<p>可以停止的</p>
	
	<div class="center" style="padding-left: 10Px;font-size:.8rem;">
		<input id="startBtn" type="button" value="开始滚动" /> <input
			id="stopBtn" type="button" value="停止滚动" />
		<div id="content">这是一段滚动的文字</div>
	</div>

	<script>
		var Util = {
			$ : function(e) {
				return typeof e == "string" ? document.getElementById(e) : e;
			}
		};

		function ScrollText(s, fn, t) {
			this.s = s.split("");
			this.fn = fn;
			this.t = t || 300;
		}

		ScrollText.prototype = {
			start : function() {
				var fn = this.fn;
				var s = this.s;
				clearInterval(this.interval);
				this.interval = setInterval(function() {
					s.push(s.shift());
					fn(s.join(""));
				}, this.t);
			},
			stop : function() {
				clearInterval(this.interval);
			}
		}
		var scroll = new ScrollText("这是一段滚动的文字", function(s) {
			Util.$("content").innerHTML = s;
		})
		var startBtn = Util.$("startBtn"), stopBtn = Util.$("stopBtn");
		startBtn.onclick = function() {
			scroll.start();
		}
		stopBtn.onclick = function() {
			scroll.stop();
		}
	</script>

	<h4>上下字幕</h4>
	
	<p>鼠标移入时候可暂停动画</p>
	<ol class="center" id="scrollA">
		<li>11111111111</li>
		<li>22222222222</li>
		<li>33333333333</li>
		<li>44444444444</li>
		<li>55555555555</li>
	</ol>

	<script>
		/**
		 * @param {Element} el 列表元素
		 * @param {Number} interval 动画时间间隔
		 * @param {Boolean} canstop 是否可以鼠标移入时候暂停动画
		 */
		function ScrollContent(el, interval, canstop) {
			interval = interval || 3000;
			canstop = canstop || false;

			function scroll() {
				var lastEl = el.firstChild;
				while (lastEl.nodeType != 1)
					// 找到最后一个元素
					lastEl = lastEl.nextSibling;

				el.appendChild(el.removeChild(lastEl)); // 把最后一个元素放到前头
			}

			var t = window.setInterval(scroll, interval);

			if (canstop) {
				el.onmouseover = function() {
					if (t)
						window.clearInterval(t);
				}
				el.onmouseout = function() {
					t = window.setInterval(scroll, interval);
				}
			}
		}

		new ScrollContent(document.getElementById('scrollA'), 500, true);
	</script>

	<h4>无缝上下滚动</h4>
	
	<p>PauseHeight 必须与 CSS 中的 li.height 相等</p>
	<style>
/* 无缝上下滚动 */
.idScroller {
	overflow: hidden;
	height: 25px;
	padding-left: 15px;
}

.idScroller	li {
	height: 25px; /* 单行高度 */
	line-height: 25px;
	white-space: nowrap;
}

.idScroller	li a {
	font-size: 1rem;
	color: #666;
}
</style>

	<div class="center idScroller">
		<div>
			<ul>
				<li><a href="vod/?id=223397">
						<div class="icon colorful video"></div> 抗战胜利70周年纪念大会流程
				</a></li>
				<li><a href="vod/?id=223389">
						<div class="icon colorful video"></div> 公积金购二套房首付最低2成
				</a></li>
				<li><a href="vod/?id=223269">
						<div class="icon colorful video"></div> 我国首款喷气式客机将商运
				</a></li>
			</ul>
		</div>
	</div>

	<script src="MarqueeText.js"></script>
	<script>
		// 无缝上下滚动
		var scroller = Object.create(ajaxjs_MarqueeText);
		scroller.init(document.querySelector(".idScroller"));
		scroller.PauseStep = 3000; // 停留时间
		scroller.PauseHeight = 25; // 单行高度，必须与 CSS 中的 li.height 相等
		scroller.scroll();
	</script>







	<h4>联动 Select 下拉</h4>
	
	<p>年历</p>
	<div class="center">
		<select id="year">
			<option value="0">--请选择--</option>
		</select> 年 <select id="month">
			<option value="0">--请选择--</option>
		</select> 月 <select id="day">
			<option value="0">--请选择--</option>
		</select> 日
	</div>

	<script>
		function initSelect(selectEl, i, end) {
			selectEl.length = 1;
			for (; i <= end; i++) {
				try {
					selectEl.add(new Option(i, i), null);
				} catch (e) {
					selectEl.add(new Option(i, i));
				}
			}
		}
		var year = document.getElementById("year");
		var month = document.getElementById("month");
		var day = document.getElementById("day");

		initSelect(year, 1970, 2012);

		year.onchange = function() {
			if (this.value != 0) {
				initSelect(month, 1, 12);
			} else {
				month.length = 1;
				day.length = 1;
			}
		}
		month.onchange = function() {
			if (this.value != 0) {
				var m30 = {
					2 : 1,
					4 : 1,
					6 : 1,
					9 : 1,
					11 : 1
				};
				if (this.value == 2) { // 二月份特别处理
					if (isLeapYear(year.value))
						initSelect(day, 1, 29);
					else
						initSelect(day, 1, 28);
				} else if (this.value in m30) // 三十天的月份
					/*
					因为2、4、6、9、11月份都是30天，如果把它们放在一个数组中，那么还要遍历来判断是否相等，则比较麻烦了，于是在这里把这些都当成对象来处理，使用 in 判断即可
					 */
					initSelect(day, 1, 30);
				else
					initSelect(day, 1, 31);
			} else
				day.length = 1;
		}
		// 判断闰年的条件：能被4整除且不能被100整除 或 能被100整除且能被400整除
		function isLeapYear(y) {
			return (y % 4 == 0 && y % 100 != 0)
					|| (y % 100 == 0 && y % 400 == 0);
		}
	</script>

	<h4>回到顶部</h4>
	
	<p>TODO</p>
	<%@include file="../public/footer.jsp"%>
</body>
</html>
