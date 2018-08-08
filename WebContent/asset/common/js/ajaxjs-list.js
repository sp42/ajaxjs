;(function(){
	var isWebkit = navigator.userAgent.toLowerCase().indexOf('webkit') != -1;
	// 推荐使用 百分比，px 的话要考虑滚动条，比较麻烦
	// 要使用 px 推荐指定 stepWidth
	// banner，要使用 px
	// 如果要跟手移动，要使用 px
	ajaxjs.Tab = {
		init : function() {
			// get mover 
			this.mover = this.el.querySelector(this.moverTagName || 'div');// 有可能是 ul 而不是 div
			this.len = this.mover.children.length;
			this.children = this.mover.children;
	
			if(!this.stepWidth)
				this.stepWidth = this.mover.parentNode.clientWidth || window.innerWidth; // 获取容器宽度作为 item 宽度
			
			var len = this.children.length, stepWidth = this.stepWidth, children = this.children;
			if(this.isMagic) {
				this.mover.style.width = this.isUsePx ? (stepWidth * 2) +'px' : '200%';
			}else{
				this.mover.style.width = this.isUsePx ? (stepWidth * len) +'px' : len + '00%';
			} 
			
			// 分配  tab 宽度
			this.tabWidth = this.isUsePx ? this.stepWidth + 'px' : (1 / len * 100).toFixed(5) + '%';
			
			for(var  i = 0, j = children.length; i < j; i++) {
				if(this.isMagic) 
					children[i].style.width = '50%';
				else
					children[i].style.width = this.tabWidth + '!important';
			}

			// 登记 resize 事件，以便 resize 容器的时候调整大小。
			// 使用 isUsePx = true 的好处是不用登记 resize 事件
			this.isUsePx && ajaxjs.throttle.init(onResize.bind(this));
			
			// tab button
			// 登记按钮事件
			this.tabHeader = this.el.querySelector('header');
			
			this.onTabHeaderPress = onTabHeaderPress;

			if(this.tabHeader && !this.disableTabHeaderJump)this.tabHeader.onclick = onTabHeaderPress.bind(this);
		},
		
		/**
		 * 跳到指定的那一帧 
		 * @param {Number} i
		 * @param {Boolean} isDirectShow
		 */
		go : true ? function(i) {
			// 控制高度 解决高度问题
			if(this.isGetCurrentHeight) {
				for(var p = 0, q = this.children.length; p < q; p++) {
					if(i == p) {
						this.children[p].style.height = 'initial';	
					}else{
						this.children[p].style.height = '1px';					
					}
				}
			}
		
			if(this.isMagic){
				// clear before
				for(var p = 0, q = this.children.length; p < q; p++) {
					// 当前的
//					debugger;
					if(this.currentIndex == p) {
						continue;
					}else if(i == p) {// 要显示的
						this.children[p].classList.remove('hide');
					}else {	
						this.children[p].classList.add('hide');
					}
				}
		
				var cssText = i > this.currentIndex
					? 'translate3d({0}, 0px, 0px)'.replace('{0}', '-50%')
					: 'translate3d({0}, 0px, 0px)'.replace('{0}', '0%');
					this.mover.style.webkitTransition = '-webkit-transform 400ms linear';
				this.mover.style.webkitTransform = cssText;
	 			
			}else{
				var leftValue =  this.isUsePx ? ('-' + (i * this.stepWidth) + 'px') : ('-' + (1 / this.len * 100  * i).toFixed(2) + '%');
				this.mover.style[isWebkit ? 'webkitTransform' : 'transform'] = 'translate3d({0}, 0px, 0px)'.replace('{0}', leftValue);
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
        goNext : function() {
            this.currentIndex++;
            if (this.currentIndex == this.len)this.currentIndex = 0; // 循环
            
            this.go(this.currentIndex);
        },
        // 是否无缝模式
        isMagic : false,
		isUsePx:false,
		autoHeight : false,
		disableTabHeaderJump : false, // 是否禁止通过 tab 候选栏来跳转。一般 wizzard 向导式的时候不能跳转
		isGetCurrentHeight:true,// 自动当前项最高，忽略其他高度，这个用在 tab很好，比 autoHeight 的好，可视作 autoHeight 2.-
		autoHeight_fn : autoHeight,
		
        // 当前在第几帧数
        currentIndex : 0,
        autoLoop : 4000,
        loop: function() {
        	if(this.isEnableLoop)this.loopTimer = window.setInterval(this.goNext.bind(this), this.autoLoop);
        },
    	// 是否支持手势（左右切换）
		isEnableTouch : false,
		onTabHeaderPress : onTabHeaderPress,
		
		/**
		 * 点击第一个 tab，否则不会高亮 tab 1 按钮和自动计算高度
		 */
		initFirstTab : function() {
			this.goTab(0);
		}

		/**
		 * 跳到指定的那个 tab
		 * @param {Number} index tab 索引，从 0 开始
		 */
		,goTab : function(index) {
			this.onTabHeaderPress({
				target : this.el.querySelectorAll('header ul li')[index]
			});
		}
	};
	
 

	 function onTabHeaderPress(e) {
		var target = e.target,
		li = target.tagName == 'LI' ? target : target.up('li');
	
		if(!li)return;
		
		// 获取 index
		var arr = this.tabHeader.querySelector('ul').children;
		var index;
		for(var i = 0, j = arr.length; i < j; i++) {
			if(li == arr[i]) {
				arr[i].classList.add('active');
				index = i;
			} else
				arr[i].classList.remove('active');
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
		 if(this.autoHeight) {
			 var tabHeaderHeight = 0;
			 if(this.tabHeader) 
				 tabHeaderHeight = this.tabHeader.scrollHeight;
			 
			 this.el.style.height = (nextItem.scrollHeight + tabHeaderHeight + 50) + 'px'; 
		 }
	 }
	 
	function goSide(e, data) {
		// data.disX 
	    if (data.direction == 'right') {
	        this.goNext();// right2left
	    } else if (data.direction == 'left') {
	    	this.goPrevious();// left2right
	    }
	    
	    this.loop();
	}
})();


   /**
*  登记 resize 事件，以便 resize 容器的时候调整大小。
*/
//	window.onresize = resize.bind(this);
//	function resize() {
//	    console.log('调整大小中……');
//	    this.containerWidth = this.el.clientWidth;
//	    render.call(this, this.mover.children);
//	}    
ajaxjs.Banner = Object.create(ajaxjs.Tab);
ajaxjs.Banner.isEnableLoop = true;
ajaxjs.Banner.isEnableTouch = true;
ajaxjs.Banner.isUsePx = true; //  banner，要使用 px
//	ajaxjs.Banner.stepWidth = window .innerWidth; // 默认 100% 视口
ajaxjs.Banner.isGetCurrentHeight = false;
ajaxjs.Banner.initIndicator = function() {
	var ol = this.el.querySelector('ol');
	ajaxjs.Banner.onItemSwitch = function(index) {
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



/*
 * --------------------------------------------------------
 * 复合 tab & list
 * --------------------------------------------------------
 */
ajaxjs.scrollViewer_list = function(url, scrollViewer_el, tab_el, loadingIndicatorTpl, itemTpl, renderItem, requestParams,cfg) {
	cfg = cfg || {};
	
	var _tab = Object.create(ajaxjs.Tab);
	_tab.el = tab_el;
//	_tab.isEnableTouch = true;
	
	var tabHeader = scrollViewer_el;
	var data = {	// 基于数据的状态管理
		activeId : null, //  选中 id
		sectionsIds : [] // 有什么栏目 id 放在这里
	};
	
	var _event = new ajaxjs.UserEvent();// 没有 Object.watch() ，只能用事件代替
	_event.addEvents('update');
	
	_event.on('update', function(activeId) { // activeId = 选中 id
		data.activeId = activeId;
		
		var _requestParams = {}; // 请求参数，附加上 id
		if(requestParams)
			for(var i in requestParams)_requestParams[i] = requestParams[i];
		
		_requestParams[cfg.id_fieldName || 'id'] = activeId;
		
		// tab hightlight
		tabHeader.$('li', function(li) {
			//debugger;
			if(li.className.indexOf(activeId) != -1)li.classList.add('selected');
			else li.classList.remove('selected');
		});
		//alert(i);
		
		// render list
		var listStyle = location.getUrlParam('style'); // ui 风格
		for(var i = 0, j = data.sectionsIds.length; i < j; i++) {
			if(data.sectionsIds[i].id == activeId) {
				// ui
				_tab.go(i);
				
				if(data.sectionsIds[i].loaded) {
					// 已加载
				} else {
					var tab = tab_el.querySelector('div');
					var el = tab.children[i];
					var ul = el.querySelector('ul');
					ul.innerHTML = loadingIndicatorTpl; // 显示 加载中……
					
					ajaxjs.List(url, ul, _requestParams,
						{
							isNoAutoHeight : listStyle == 'col2' || listStyle == 'col3' ? false : true, // 海报 col3 需要等高
							tpl : itemTpl,
							pager : cfg.pager !=  undefined ? cfg.pager : true, // 是否需要分页
							loadMoreBtn : el.querySelector('.loadMore'),
							pageSize : cfg.pageSize || 10, // 海报 col3 读9条
							renderer : renderItem,
							isAppend : true,
							cb : cfg.cb,
							isJSONP : cfg.isJSONP,
							dataKey : cfg.dataKey
						}
					);
					
					data.sectionsIds[i].loaded = true;
				}
				
				//location.href += '#id=' + activeId; // url
				break;
			}
		}
		
		// set hash
		if(cfg.isNoHash) {
		} else location.setUrl_hash('id', activeId);
	});
	
	// 先获取所有 section id
	tabHeader.$('li', function(li) {
		var id;
		id = li.className && li.className.match(/id_(\w+)/).pop();
//		if(cfg.isTextId) { // id 不是 数字，是 text
//		} else {
//			id = li.className && li.className.match(/\d+/).pop();
//		}
		
		if(id){
			data.sectionsIds.push({
				id : id,
				loaded : false // lazy load token
			});
			
		}
	});
	
	var isPager = cfg.pager !=  undefined ? cfg.pager : true;
	// create tab item
	var tpl = '<div>\
		<ul class="{0}"></ul>'+
		(isPager ? '<button class="loadMore">加载更多</button>' : '')+
		(cfg.extendHTML || '')+ // 额外的 HTML
	'</div>';
	
	tpl = tpl.replace('{0}', location.getUrlParam('style') ||  cfg.style || 'col1');
	_tab.el.querySelector('div').innerHTML = new Array(data.sectionsIds.length + 1).join(tpl);
	_tab.init();
	
	var load_id;
	if (location.hash.indexOf('id=') != -1) {// 有 hash id 读取
		
		load_id = location.hash.match(/id=(\w+)/).pop();
		
		if (!data.sectionsIds[0]) {
			data.sectionsIds[0] = {
				id : load_id,
				loaded : false
			};
			_tab.el.querySelector('div').innerHTML = tpl;
			_tab.init();
		}
		
		_event.fireEvent('update', load_id);
	} else {
		// 默认 选中第一 tab
		if (data.sectionsIds.length) {
			load_id = data.sectionsIds[0].id;
		} else {
			// 没有子栏目，读取父栏目
			load_id = location.search.match(/id=(\w+)/).pop();
			data.sectionsIds[0] = {
					id : load_id,
					loaded : false
			};
			_tab.el.querySelector('div').innerHTML = tpl;
			_tab.init();
		}
		_event.fireEvent('update', load_id);
	}
	
	// 点击导航事件选中用户指定 tab
	tabHeader.onclick = function(e) {
		var el = e.target;
		if (el.tagName != 'LI')
			el = el.parentNode;
		
		tabHeader.$('li', function(li, i) { 
			if (el == li) {
				var id = li.className.match(/id_(\w+)/).pop();
				_event.fireEvent('update', id);
				return;
			}
		});
	}
	
	return _event;
}