<%@page pageEncoding="utf-8"%>
<div class="lockWidth">

	<section class="banner">
		<div>
			<div>
				<a href="topic/?id=283"> <img
					src="http://imgu.3gtv.net:9090/_file/section/20150906113629683.jpg">
				</a>
			</div>

			<div>
				<a href="topic/?id=341"> <img
					src="http://imgu.3gtv.net:9090/_file/section/20150828090747132.jpg">
				</a>
			</div>

			<div>
				<a href="live/?id=3"> <img
					src="http://imgu.3gtv.net:9090/_file/section/20150906125934797.png">
				</a>
			</div>

			<div>
				<a href="album/?id=4805"> <img
					src="http://imgu.3gtv.net:9090/_file/section/20150826162352938.jpg">
				</a>
			</div>
		</div>
		<ol class="indicator">
			<li>0</li>
			<li>1</li>
			<li>2</li>
			<li>3</li>
		</ol>
	</section>

	<script>
		setTimeout(function() {
			var banner = Object.create(ajaxjs.Banner);
			banner.el = document.querySelector('.banner');
			banner.init();
			banner.loop();
			banner.initIndicator();
		}, 100);
	</script>


	<section class="idScroller">
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
	</section>
	
	<script>
	/*
	 * --------------------------------------------------------
	 * 无缝上下滚动
	 * 鸣谢原作者： cloudgamer
	 * --------------------------------------------------------
	 */
	ajaxjs_MarqueeText = {
		init : function(el) {
			this.el = el;
			this.boxHeight = el.offsetHeight;
			
			if (getComputedStyle(el).overflow != 'hidden') // 强制设置不溢出
				el.style.overflow = "hidden";

			var listEl = el.querySelector('div');
			listEl.onmouseover = this.stop.bind(this);
			listEl.onmouseout  = this.scroll.bind(this);

			this.listHeight = listEl.offsetHeight;

			// autoPauseHeight.call(this, oScroller);
			if (this.listHeight <= this.boxHeight) {
				console.warn('父容器有足够高度显示，无须滚动，有时可能因为数据源只有一条记录返回，也就是无须上下滚动了');
				return;
			}

			el.appendChild(listEl.cloneNode(true)); // 复制多一份

			this.pause = 0;
		},
		// 设置默认属性
		side : 1,
		// 滚动方向1是上 -1是下
		step : 1,
		// 每次变化的px量
		time : 20,
		// 速度(越大越慢)
		pauseHeight : 25,
		// 隔多高停一次
		pauseStep : 1000,
		// 停顿时间(PauseHeight大于0该参数才有效)

		// 开始滚动
		scroll : function() {
			var iScroll = this.el.scrollTop, time = this.time, iStep = this.step * this.side;

			if (this.side > 0) { 
				if (iScroll >= (this.listHeight * 2 - this.boxHeight))
					iScroll -= this.listHeight;
			} else {
				if (iScroll <= 0)
					iScroll += this.listHeight;
			}

			if (this.pauseHeight > 0) {
				if (this.pause >= this.pauseHeight) {
					time = this.pauseStep;
					this.pause = 0;

				} else {
					this.pause += Math.abs(iStep);
					this.el.scrollTop = iScroll + iStep;
				}
			} else {
				this.el.scrollTop = iScroll + iStep;
				console.log('never?');
			}
			if(this.el.scrollTop ==51)
			console.log(this.el.scrollTop);
			this.timer = window.setTimeout(this.scroll.bind(this), time);
		},

		// 停止
		stop : function() {
			window.clearTimeout(this.timer);
		}
	};

	function autoPauseHeight(ul) {
		var li = ul.qs('li'), lineHeight = getComputedStyle(ul.qs('li')).lineHeight;

		// 有设置 line-height
		if (lineHeight && lineHeight != 'normal')
			lineHeight = parseInt(lineHeight);

		if (!isNaN(lineHeight))
			this.pauseHeight = lineHeight;
	}
		// 无缝上下滚动
		setTimeout(function(){
				
			var scroller = Object.create(ajaxjs_MarqueeText);
			scroller.init(document.querySelector(".idScroller"));
			scroller.PauseStep = 3000; // 停留时间
			scroller.PauseHeight = 25; // 单行高度，必须与 CSS 中的 li.height 相等
			scroller.scroll();
		}, 500)
	</script>

	<section>
		<ul class="btn">
			<li>上班族</li>
			<li>企业族</li>
			<li>个体户</li>
			<li>自由职业</li>
		</ul>
		<form>
			<label> 贷款金额：<input type="text" name="" />
			</label> <label> 贷款期限：<input type="text" name="" />
			</label>
			<button>量身定制方案</button>
		</form>
	</section>

	<img src="images/banner.jpg" width="100%" />

	<section class="s">
		<div class="left">
			<div>方案资讯定制更新</div>
			<div>热门贷款</div>
		</div>
		<div class="right">
			<div>常用工具</div>
			<div>资 讯</div>
		</div>
	</section>
</div>