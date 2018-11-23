ajaxjs.tab = function(cfg) {
	ajaxjs.apply(this, cfg);

	var mover = this.el.querySelector(this.moverTagName || 'div');// 有可能是 ul 而不是 div
	var children = mover.children, len = children.length;

	var stepWidth = this.stepWidth || mover.parentNode.clientWidth || window.innerWidth; // 获取容器宽度作为 item 宽度
	
	if(this.isMagic) 
		mover.style.width = this.isUsePx ? (stepWidth * 2) +'px' : '200%';
	else
		mover.style.width = this.isUsePx ? (stepWidth * len) +'px' : len + '00%';
	
	var tabWidth = this.isUsePx ? stepWidth + 'px' : (1 / len * 100).toFixed(5) + '%';// 分配  tab 宽度
	
	for(var  i = 0; i < len; i++) 
		children[i].style.width = this.isMagic ? '50%' : tabWidth;
	
	// 登记 resize 事件，以便 resize 容器的时候调整大小。
	// 使用 isUsePx = true 的好处是不用登记 resize 事件
	this.isUsePx && ajaxjs.throttle.init(onResize.bind(this));
	
	// 登记按钮事件
	this.tabHeader = this.el.$('header');
	
	if (this.tabHeader && !this.disableTabHeaderJump)
		this.tabHeader.onclick = onTabHeaderPress.bind(this);
	
	var isWebkit = navigator.userAgent.toLowerCase().indexOf('webkit') != -1;
	
	/**
	 *  跳到指定的那一帧 
	 *  @param {Number} i
	 */
	this.go = function(i) {
		// 控制高度 解决高度问题
		if(this.isGetCurrentHeight) {
			for(var p = 0; p < len; p++) {
				if(i == p) {
					this.children[p].style.height = 'initial';	
				}else{
					this.children[p].style.height = '1px';					
				}
			}
		}
	
		if(this.isMagic) {
			// clear before
			for(var p = 0; p < len; p++) {
				if(this.currentIndex == p) {// 当前的
					continue;
				}else if(i == p) {// 要显示的
					children[p].classList.remove('hide');
				}else {	
					children[p].classList.add('hide');
				}
			}
	
			var cssText = i > this.currentIndex
				? 'translate3d({0}, 0px, 0px)'.replace('{0}', '-50%')
				: 'translate3d({0}, 0px, 0px)'.replace('{0}', '0%');
				mover.style.webkitTransition = '-webkit-transform 400ms linear';
			
			mover.style.webkitTransform = cssText;
 			
		}else{
			var leftValue =  this.isUsePx ? ('-' + (i * stepWidth) + 'px') : ('-' + (1 / len * 100  * i).toFixed(2) + '%');
			mover.style[isWebkit ? 'webkitTransform' : 'transform'] = 'translate3d({0}, 0px, 0px)'.replace('{0}', leftValue);
			
			// 使用 left 移动！
//			mover.style.left = this.isUsePx ? ('-' + (i * this.stepWidth) + 'px') : ('-' + i + '00%');
		}
		
		this.currentIndex = i;
		this.onItemSwitch && this.onItemSwitch.call(this, i, children[i]);
	}
	
    // 跳到前一帧。
	 this.goPrevious = function() {
        this.currentIndex--;
        if (this.currentIndex < 0)this.currentIndex = len - 1;
        this.go(this.currentIndex); 
    }
    
    /**
     * 跳到下一帧。
     */
    this.goNext = function() {
        this.currentIndex++;
        if (this.currentIndex == len)this.currentIndex = 0; // 循环
        
        this.go(this.currentIndex);
    }
    
    this.loop = function() {
    	if(this.isEnableLoop)
    		this.loopTimer = window.setInterval(this.goNext.bind(this), this.autoLoop);
    }
    
	/**
	 * 跳到指定的那个 tab
	 * @param {Number} index tab 索引，从 0 开始
	 */
	this.goTab = function(index) {
		onTabHeaderPress.call(this, {
			target : this.el.querySelectorAll('header ul li')[index]
		});
	}
	
	function onResize() {
		var stepWidth   = mover.parentNode.clientWidth; // 获取容器宽度作为 item 宽度
		mover.style.width = this.isUsePx ? (stepWidth * len) +'px' : len + '00%';
		
		for(var i = 0; i < len; i++) 
			children[i].style.width = stepWidth + 'px';
	}
	
	function onTabHeaderPress(e) {
		var target = e.target,
		li = target.tagName == 'LI' ? target : target.up('li');
	
		if(!li)return;
	
		// 获取 index
		var arr = this.tabHeader.$('ul').children, index;
		for(var i = 0, j = arr.length; i < j; i++) {
			if(li == arr[i]) {
				arr[i].classList.add('active');
				index = i;
			} else
				arr[i].classList.remove('active');
		}
	
		// 移动！
		this.go(index);
		this.currentIndex = index;
		
		var nextItem = children[index]; // 下一个 item
		this.onTabHeaderSwitch && this.onTabHeaderSwitch.call(this, li, nextItem, target, index);
				
		autoHeight.call(this, nextItem);
	}
	
	 function autoHeight(nextItem) {
		 if(this.autoHeight) {
			 var tabHeaderHeight = 0;
			 if(this.tabHeader) 
				 tabHeaderHeight = this.tabHeader.scrollHeight;
			 
			 this.el.style.height = (nextItem.scrollHeight + tabHeaderHeight + 50) + 'px'; 
		 }
	 }
}

ajaxjs.tab.prototype = {
	// 是否无缝模式
	isMagic : false,
	
	// 推荐使用 百分比，px 的话要考虑滚动条，比较麻烦
	// 要使用 px 推荐指定 stepWidth
	// banner，要使用 px
	// 如果要跟手移动，要使用 px
	isUsePx : false,
	
	// 是否自动高度
	autoHeight : false,
	
	// 是否禁止通过 tab 候选栏来跳转。一般 wizzard 向导式的时候不能跳转
	disableTabHeaderJump : false, 
	
	// 自动当前项最高，忽略其他高度，这个用在 tab很好，比 autoHeight 的好，可视作autoHeight 2.-
	isGetCurrentHeigh : true,
	
	// 当前在第几帧数
	currentIndex : 0,
	
	autoLoop : 4000,
	
	/**
	 * 点击第一个 tab，否则不会高亮 tab 1 按钮和自动计算高度
	 */
	initFirstTab : function() {
		this.goTab(0);
	}
};

ajaxjs.banner = function(cfg) {
	cfg.isUsePx = true; //  banner，要使用 px
	cfg.isEnableLoop = true;
	cfg.isGetCurrentHeight = false;
	ajaxjs.tab.call(this, cfg);
	
	this.initIndicator = function() {
		var ol = this.el.$('ol');
		
		this.onItemSwitch = function(index) {
			// pressedStatable
			if (ol) ol.$('li', function(li, i) {
				if(index == i) 
					li.classList.add('active');
				else
					li.classList.remove('active');
			});
		}
		
		var self = this;
		ol.onclick =  function(e) {
			var el = e.target;
			if(el.tagName != 'LI')return;
			if (ol) ol.$('li', function(li, i) {
				if(el == li) {
					self.go(i);
					return;
				}
			});
		}
	}
	
	this.loop();
	this.initIndicator();
}

ajaxjs.banner.prototype = ajaxjs.tab.prototype;

/**
 * 采用 start/limit分页
 */
ajaxjs.list = function(cfg) {
	ajaxjs.apply(this, cfg);

	var args = {};
	args.start = 0;
	args.limit = 10;
	this.baseParam && ajaxjs.apply(args, this.baseParam);// 每次请求都附带的参数

	if (!this.jsonInit)
		this.jsonInit = function(json) { // json 指定字段，你可以重新指定一个 jsonInit
			if (json) {
				if (!json.isOk) 
					ajaxjs.alert(json.msg || '执行失败！原因未知！');
			} else {
				ajaxjs.alert('ServerSide Error!');
			}
			
			return {
				data : json.result,
				total : json.total
			};
		}

	// 加载数据
	this.load = function() {
		ajaxjs.xhr.get(this.url, function(json) {
			var result = this.jsonInit(json);
			var data = result.data;
			
			renderer.call(this, data);
			
			args.start += args.limit;
			
			this.afterDataLoad && typeof this.afterDataLoad == 'function' && this.afterDataLoad.call(this, result);
		}.bind(this), args);
	}

	function renderer(data) {
		if (!data || data.length <= 0)
			return; // 无记录
		
		var lis = [];

		for (var i = 0; i < data.length; i++) {
			var _data = this.renderer ? this.renderer(data[i]) : data[i]; // 很细的颗粒度控制记录
			if (_data === false)
				continue; // 返回 false 则跳过该记录，不会被渲染

			var li = tppl(this.tpl, _data);

			if (this.isAppend) { // 分页是累加的
				// 利用 div 添加到 tplEl
				var loadingIndicator = this.el.$('.loadingIndicator');
				if (loadingIndicator) // 如果有 loadingIndicator，則干掉
					this.el.removeChild(loadingIndicator);

				var temp = document.createElement('div');
				temp.innerHTML = li;
				this.el.appendChild(temp.$('li'));
				temp = null;
			} else {
				lis.push(li);// 之前的 tag 不要了
			}
		}

		if (!this.isAppend)
			this.el.innerHTML = lis.join('');
	}

	function getCellRequestWidth() {
		window.devicePixelRatio = window.devicePixelRatio || 1;
		
		var screenWidth = window.innerWidth; // 获取视口宽度
		var columns = 3; // 列数，可双列或三列（取值：2|3），假设三列
		
		var cellWidth = screenWidth * ( 1 / columns );// 求单元格实际宽度
		var cellHight = cellWidth / (4 / 3); // 实际高度。此为横图，竖图为 8/9
		var reqeustWidth = cellWidth * window.devicePixelRatio;
		reqeustWidth = Math.floor(reqeustWidth);
		var MaxWidth = 500;// 宽度上限
		
		return reqeustWidth;
	}
	
	function autoHeight() {
		var firstHeight = arguments.callee.firstHeight;
		
		for(var i = 0 , j = imgs.length; i < j; i++) {
			var imgObj = imgs[i];
			
			console.log(imgObj.el.complete);
			if(i == 0 && !firstHeight)// firstHeight 只设置一次
				firstHeight = arguments.callee.firstHeight = imgObj.el.height;
			else
				imgObj.el.height = firstHeight;
		}
	}
	
	/**
	 * 固定图片高度
	 */
	function fixImgHeigtBy() {
		window.innerWidth * 0.3 / 1.333
	}
}




ajaxjs.list.thumbFadeIn = function() {
	var imgs = [];
	// 获取图片列表
	this.el.$('img[data-src^="https://"]', function(img, index) {
		img.onload = function(){ this.classList.add('tran') };
		imgs.push({
			index : index,  // 序号
			el : img,		// img DOM 元素
			src : img.getAttribute('data-src') // 图片地址
		});
	});
	
	Step(function() {
		for(var i = 0 , j = imgs.length; i < j; i++) {
			if(imgs[i].src) {
				var img = new Image();
				img.onload  = this.parallel();	
				img.src = imgs[i].src;// 加载图片
			}
		}
	}, function() {
		// all images are local
		// 逐次显示
		// for(var i = 0, j = imgs.length; i < j; i++){
		// imgs[i].el.addEventListener('load', this.parallel());
		// }
		
		var i = 0;
		var nextStep = this;
		var id = setInterval(function() {
			var imgObj = imgs[i++];
			imgObj.el.src = imgObj.src;
			
			if(i == imgs.length) {
				clearInterval(id);
				// 同步高度
// if(config && config.isNoAutoHeight) {
// }else nextStep();
				
				nextStep();
			}
		}, 300);
		
		// for(var i = 0 , j = imgs.length; i < j; i++){
		// setTimeout(showImg.bind(imgs[i]), i * 200);
		// }
		// function showImg(){
		// this.el.src = this.src;
		// //this.el.classList.add('tran');// ios 不能这里处理动画，改而在 onload 事件中
		// }
	}, function() {
// autoHeight();
// if(config.isNotAutoHeight) {
// } else {
// //UserEvent.onWinResizeFree(autoHeight);
// }
	});
}