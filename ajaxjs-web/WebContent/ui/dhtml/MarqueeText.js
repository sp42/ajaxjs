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