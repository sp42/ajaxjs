/**
 * 简单选项卡控件
 */
(function() {
	SimpleTab = function(container) {
		this.el = container;
		var ul = container.querySelector('ul');
		this.buttons = ul.children, // tab候选栏strip
		this.tabs = container.querySelector('.content').children;
		ul.onclick = onTabChooserPressHandler.bind(this);
	}
	
	/**
	 * 跳到指定的 tab，仿佛好象点击那样
	 * @param {int} index
	 */
	SimpleTab.prototype.jump = function(index){
		var btn = this.buttons[index];
		onTabChooserPressHandler.call(this, {
			target : btn,
			currentTarget : this.el.querySelector('ul')
		});
	}
	
	var onPressed_ClassName = 'selected';
	// 登记的单击事件是整个 tan panel
	function onTabChooserPressHandler(e) {
		// 搜索 el 下的 li 元素，到容器为止
		var el = e.target;
		if (el.tagName != 'LI') return;

		var buttons = e.currentTarget.children, // tab候选栏strip
			tabs = e.currentTarget.parentNode.querySelector('.content').children;
		!buttons.length && console.log('该控件未发现任何 strip。');
		
		for (var nextIndex = 0, j = buttons.length; nextIndex < j; nextIndex++)
			if (buttons[nextIndex] == el)
				break; // 获取 nextIndex
				
		// 拦截事件
		if (
			this.beforeSwitch && typeof this.beforeSwitch == 'function' && 
			this.beforeSwitch(this.currentIndex, nextIndex, j, buttons[nextIndex], tabs[nextIndex]) === false
		) {
			return;
		}
				
		// 查找与 index 相等的 item 设置其高亮，否则移除样式。
		var btn, showTab;
		for (var i = 0, j = buttons.length; i < j; i++) {
			btn = buttons[i], showTab = tabs[i];
			// debugger;
			if (nextIndex == i && btn.className.indexOf(onPressed_ClassName) == -1) { // 找到目标项
				btn.addCls(onPressed_ClassName);
				showTab.addCls(onPressed_ClassName);
				this.currentIndex = i; // 保存当前游标

				if (this.afterSwitch && typeof this.afterSwitch == 'function')
					this.afterSwitch(i, btn, showTab);

				// 是否已经渲染
				var isRendered_marked = ~showTab.className.indexOf('rendered');
				if (!isRendered_marked)
					showTab.addCls('rendered');
				if (!isRendered_marked && this.afterRender && typeof this.afterRender == 'function')
					this.afterRender(i, btn, showTab);
			} else if (btn == el && btn.className.indexOf(onPressed_ClassName) != -1) {
				// 已在当前项
			} else if (btn.className.indexOf(onPressed_ClassName) != -1) {
				btn.removeCls(onPressed_ClassName);
				showTab.removeCls(onPressed_ClassName);
			}
		}
	}
})();

;(function(){
	var isWebkit = navigator.userAgent.toLowerCase().indexOf('webkit') != -1;
	
	// 推荐使用 百分比，px 的话要考虑滚动条，比较麻烦
	// 要使用 px 推荐指定 stepWidth
	// banner，要使用 px
	// 如果要跟手移动，要使用 px
	window.bf_tab = {
		init : function(){
			// get mover 
			this.mover = this.el.querySelector(this.moverTagName || 'div');// 有可能是 ul 而不是 div
			this.len = this.mover.children.length;
			this.children = this.mover.children;
	
			if(!this.stepWidth)
				this.stepWidth = this.mover.parentNode.clientWidth; // 获取容器宽度作为 item 宽度
			
			var len = this.children.length, stepWidth = this.stepWidth, children = this.children;
			if(this.isMagic){
				this.mover.style.width = this.isUsePx ? (stepWidth * 2) +'px' : '200%';
			}else{
				this.mover.style.width = this.isUsePx ? (stepWidth * len) +'px' : len + '00%';
			}
			
			// 分配  tab 宽度
			this.tabWidth = this.isUsePx ? stepWidth + 'px' : (1 / len * 100).toFixed(5) + '%';
			
			for(var  i = 0, j = children.length; i < j; i++){
				if(this.isMagic){
					children[i].style.width = '50%';
				}else{
					children[i].style.width = this.tabWidth;
				}
			}

			// 登记 resize 事件，以便 resize 容器的时候调整大小。
			// 使用 isUsePx = true 的好处是不用登记 resize 事件
			this.isUsePx && UserEvent.onWinResizeFree(onResize.bind(this));
			
			// tab button
			// 登记按钮事件
			this.tabHeader = this.el.querySelector('header');
			
			this.onTabHeaderPress = onTabHeaderPress;

			if(this.tabHeader)this.tabHeader.onclick = onTabHeaderPress.bind(this);
			
			if(this.isEnableTouch && typeof window.bf_touch != 'undefined'){
				var self = this;
				var touch = Object.create(bf_touch);
				touch.el = this.el;
				touch.onBeforeMove = function(){
					if(this.loopTimer)
						window.clearInterval(this.loopTimer);
				}.bind(this);
//				touch.onMoving = function(e, direction, x, lastX){
//					if((direction == 'right' || direction == 'left')){
//						// get left
//						var el_Left = this.mover.style.webkitTransform;
//						if(el_Left){
//							el_Left = el_Left.match(/(-?\d+)px,/);
//							el_Left = el_Left[1];
//							el_Left = Number(el_Left);
//						}else{
//							el_Left = 0;
//						}
//						
//					//	alert(el_Left);
//						var leftValue = el_Left + (x - lastX);
//						if(leftValue <= 0)
//							this.mover.style.webkitTransform  = 'translate3d({0}, 0px, 0px)'.format(leftValue + 'px');
//					}
//				}.bind(this);
				touch.onAfterMove = goSide.bind(this);
				touch.init();
			}
		},
		
		/**
		 * 跳到指定的那一帧 
		 * @param {Number} i
		 * @param {Boolean} isDirectShow
		 */
		go : true ? function(i){
			// 控制高度 解决高度问题
			if(this.isGetCurrentHeight) {
				for(var p = 0, q = this.children.length; p < q; p++){
					if(i == p){
						this.children[p].style.height = 'initial';	
					}else{
						this.children[p].style.height = '1px';					
					}
				}
			}
		
			if(this.isMagic){
				// clear before
				for(var p = 0, q = this.children.length; p < q; p++){
					// 当前的
//					debugger;
					if(this.currentIndex == p){
						continue;
					}else if(i == p){// 要显示的
						this.children[p].removeCls('hide');
					}else{	
						this.children[p].addCls('hide');
					}
				}
		
				var cssText = i > this.currentIndex
					? 'translate3d({0}, 0px, 0px)'.format('-50%')
					: 'translate3d({0}, 0px, 0px)'.format('0%');
					this.mover.style.webkitTransition = '-webkit-transform 400ms linear';
				this.mover.style.webkitTransform = cssText;
	 			
			}else{
				var leftValue =  this.isUsePx ? ('-' + (i * this.stepWidth) + 'px') : ('-' + (1 / this.len * 100  * i).toFixed(2) + '%');
				this.mover.style[isWebkit ? 'webkitTransform' : 'transform'] = 'translate3d({0}, 0px, 0px)'.format(leftValue);
			}
			
			this.currentIndex = i;
			this.onItemSwitch && this.onItemSwitch.call(this, i, this.children[i]);
		} : function(i){
			// 移动！
			this.mover.style.left = this.isUsePx ? ('-' + (i * this.stepWidth) + 'px') : ('-' + i + '00%');
			
			this.onItemSwitch && this.onItemSwitch.call(this, i, this.children[i]);
		},
		
        // 跳到前一帧。
        goPrevious : function(){
            this.currentIndex--;
            if (this.currentIndex < 0)this.currentIndex = this.len - 1;
            this.go(this.currentIndex); 
        },
        
        /**
         * 跳到下一帧。
         */
        goNext : function(){
            this.currentIndex++;
            if (this.currentIndex == this.len)this.currentIndex = 0; // 循环
            
            this.go(this.currentIndex);
        },
        // 是否无缝模式
        isMagic : false,
		isUsePx:false,
		autoHeight : false,
		isGetCurrentHeight:true,// 自动当前项最高，忽略其他高度，这个用在 tab很好，比 autoHeight 的好，可视作 autoHeight 2.-
		autoHeight_fn : autoHeight,
		
        // 当前在第几帧数
        currentIndex : 0,
        loop: function(){
        	if(this.isEnableLoop)this.loopTimer = window.setInterval(this.goNext.bind(this), this.autoLoop);
        },
        autoLoop : 4000,
    	// 是否支持手势（左右切换）
		isEnableTouch : false,
		onTabHeaderPress : onTabHeaderPress,
		
		/**
		 * 点击第一个 tab，否则不会高亮 tab 1 按钮和自动计算高度
		 */
		initFirstTab : function(){
			this.onTabHeaderPress({
				target : this.el.querySelector('header ul li')
			});
		}
	};
	
	function onResize(){
		var children 	= this.mover.children, len = children.length;
		this.stepWidth  = this.mover.parentNode.clientWidth; // 获取容器宽度作为 item 宽度
		var stepWidth   = this.stepWidth + 'px';
		this.mover.style.width = this.isUsePx ? (this.stepWidth * len) +'px' : this.len + '00%';
		
		for(var i = 0; i < len; i++){
			children[i].style.width = stepWidth;
		}
	}

	 function onTabHeaderPress(e){
		var target = e.target,
		li = target.tagName == 'LI' ? target : target.up('li');
	
		if(!li)return;
		
		// 获取 index
		var arr = this.tabHeader.querySelector('ul').children;
		var index;
		for(var i = 0, j = arr.length; i < j; i++){
			if(li == arr[i]){
				arr[i].addCls('active');
				index = i;
			}else{
				arr[i].removeCls('active');
			}
		}

//		// todo 结合 库
//		if(~li.className.indexOf('id_')){
//			var id = li.className.match(/id_(\d+)/).pop();
//			window.location.hash = '#id=' + id;
//		}
		// 移动！
		this.go(index);
		this.currentIndex = index;
		
		var nextItem = this.children[index]; // 下一个 item
		this.onTabHeaderSwitch && this.onTabHeaderSwitch.call(this, li, nextItem, target, index);
		
		autoHeight.call(this, nextItem);
	}
	 
	 function autoHeight(nextItem) {
		 if(this.autoHeight){
			 var tabHeaderHeight = 0;
			 if(this.tabHeader){
				 tabHeaderHeight = this.tabHeader.scrollHeight;
			 }
			 this.el.style.height = (nextItem.scrollHeight + tabHeaderHeight + 50) + 'px'; 
		 }
	 }
	 
	function goSide(e, data){
		// data.disX 
	    if (data.direction == 'right') {
	        // right2left
	        this.goNext();
	    } else if (data.direction == 'left') {
	        // left2right
	    	this.goPrevious();
	    }
	    
	    this.loop();
	}
})();

;(function(){
	
	   /**
	    *  登记 resize 事件，以便 resize 容器的时候调整大小。
	    */
	// window.onresize = resize.bind(this);
//	    function resize() {
//	        console.log('调整大小中……');
//	        this.containerWidth = this.el.clientWidth;
//	        render.call(this, this.mover.children);
//	    }    
	bf_banner = Object.create(window.bf_tab);
	bf_banner.isEnableLoop = true;
	bf_banner.isEnableTouch = true;
	bf_banner.isUsePx = true; //  banner，要使用 px
//	bf_banner.stepWidth = window .innerWidth; // 默认 100% 视口
	bf_banner.isGetCurrentHeight = false;
	bf_banner.initIndicator = function(){
		var ol = this.el.querySelector('ol');
		bf_banner.onItemSwitch = function(index){
			// pressedStatable
			if (ol) ol.eachChild('li', function(li, i){
				if(index == i){
					li.addCls('active');
				}else{
					li.removeCls('active');
				}
			});
		}
		var self = this;
		ol.onclick =  function(e){
			var el = e.target;
			if(el.tagName != 'LI')return;
			if (ol) ol.eachChild('li', function(li, i){
				if(el == li){
					self.go(i);
					return;
				}
			});
		}
	}
})();