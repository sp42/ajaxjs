;(function (re) {
    bf_marquee = {
        init: function (oScroller) {
        	var oScrollMid = oScroller.querySelector('div');
        	
            this.heightScroller = oScroller.offsetHeight;
            this.heightList 	= oScrollMid.offsetHeight;

            // autoPauseHeight.call(this, oScroller);
            if(this.heightList <= this.heightScroller){
            	// 父容器有足够高度显示，无须滚动，有时可能因为数据源只有一条记录返回，也就是无须上下滚动了
            	this.oneLineRecord = true;
            	console.warn('父容器足够高度显示，无须滚动'); 
            	return;
            }
            
            if(getComputedStyle(oScroller).overflow != 'hidden'){ // 强制设置不溢出
            	oScroller.style.overflow = "hidden";
            }
            
            oScroller.appendChild(oScrollMid.cloneNode(true));

            this.oScroller = oScroller;
  
            oScrollMid.onmouseover = this.Stop.bind(this);
            oScrollMid.onmouseout = this.Scroll.bind(this);

            if (this.PauseStep <= 0 || this.PauseHeight <= 0) this.PauseStep = this.PauseHeight = 0;

            this.Pause = 0;
        },
        // 设置默认属性
        side: 1,
        // 滚动方向1是上 -1是下
        Step: 1,
        // 每次变化的px量
        Time: 20,
        // 速度(越大越慢)
        PauseHeight: 25,
        // 隔多高停一次
        PauseStep: 1000,
        // 停顿时间(PauseHeight大于0该参数才有效)
        // 开始滚动
        Scroll: function () {
        	if(this.oneLineRecord)return;
        	
            var iScroll = this.oScroller.scrollTop,
                iHeight = this.heightList,
                time = this.Time,
                iStep = this.Step * this.side;

            if(this.side > 0) {
                if (iScroll >= (iHeight * 2 - this.heightScroller)) {
                    iScroll -= iHeight;
                }
            }else{
                if (iScroll <= 0) {
                    iScroll += iHeight;
                }
            }

            if(this.PauseHeight > 0){
                if (this.Pause >= this.PauseHeight) {
                    time = this.PauseStep;
                    this.Pause = 0;
                } else {
                    this.Pause += Math.abs(iStep);
                    this.oScroller.scrollTop = iScroll + iStep;
                }
            }else{
                this.oScroller.scrollTop = iScroll + iStep;
            }

            this.timer = window.setTimeout(this.Scroll.bind(this), time);
        },

        //停止
        Stop: function(){
            window.clearTimeout(this.timer);
        }
    };

    function autoPauseHeight(ul){
        var li = ul.querySelector('li'), 
            lineHeight = getComputedStyle(li).lineHeight;
        
        // 有设置 line-height
        if(lineHeight && lineHeight != 'normal')lineHeight = parseInt(lineHeight);

        if(!isNaN(lineHeight))this.PauseHeight = lineHeight;
    }
})();