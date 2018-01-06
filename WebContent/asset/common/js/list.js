;(function(){
	var isWebkit = navigator.userAgent.toLowerCase().indexOf('webkit') != -1;
	// 推荐使用 百分比，px 的话要考虑滚动条，比较麻烦
	// 要使用 px 推荐指定 stepWidth
	// banner，要使用 px
	// 如果要跟手移动，要使用 px
	ajaxjs.Tab = {
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
			this.isUsePx && ajaxjs.throttle.init(onResize.bind(this));
			
			// tab button
			// 登记按钮事件
			this.tabHeader = this.el.querySelector('header');
			
			this.onTabHeaderPress = onTabHeaderPress;

			if(this.tabHeader && !this.disableTabHeaderJump)this.tabHeader.onclick = onTabHeaderPress.bind(this);
			
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
						this.children[p].classList.remove('hide');
					}else{	
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
        goNext : function(){
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
			this.goTab(0);
		}

		/**
		 * 跳到指定的那个 tab
		 * @param {Number} index tab 索引，从 0 开始
		 */
		,goTab : function(index){
			this.onTabHeaderPress({
				target : this.el.querySelectorAll('header ul li')[index]
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
				arr[i].classList.add('active');
				index = i;
			}else{
				arr[i].classList.remove('active');
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
	ajaxjs.Banner.initIndicator = function(){
		var ol = this.el.querySelector('ol');
		ajaxjs.Banner.onItemSwitch = function(index){
			// pressedStatable
			if (ol) ol.eachChild('li', function(li, i) {
				if(index == i){
					li.classList.add('active');
				}else{
					li.classList.remove('active');
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



function Step() {
    var steps = Array.prototype.slice.call(arguments),
        pending, counter, results, lock;

    // Define the main callback that's given as `this` to the steps.
    function next() {
      counter = pending = 0;

      // Check if there are no steps left
      if (steps.length === 0) {
        // Throw uncaught errors
        if (arguments[0]) {
          throw arguments[0];
        }
        return;
      }

      // Get the next step to execute
      var fn = steps.shift();
      results = [];

      // Run the step in a try..catch block so exceptions don't get out of hand.
      try {
        lock = true;
        var result = fn.apply(next, arguments);
      } catch (e) {
        // Pass any exceptions on through the next callback
        next(e);
      }

      if (counter > 0 && pending == 0) {
        // If parallel() was called, and all parallel branches executed
        // synchronously, go on to the next step immediately.
        next.apply(null, results);
      } else if (result !== undefined) {
        // If a synchronous return is used, pass it to the callback
        next(undefined, result);
      }
      lock = false;
    }

    // Add a special callback generator `this.parallel()` that groups stuff.
    next.parallel = function () {
      var index = 1 + counter++;
      pending++;

      return function () {
        pending--;
        // Compress the error from any result to the first argument
        if (arguments[0]) {
          results[0] = arguments[0];
        }
        // Send the other results as arguments
        results[index] = arguments[1];
        if (!lock && pending === 0) {
          // When all parallel branches done, call the callback
          next.apply(null, results);
        }
      };
    };

    // Generates a callback generator for grouped results
    next.group = function () {
      var localCallback = next.parallel();
      var counter = 0;
      var pending = 0;
      var result = [];
      var error = undefined;

      function check() {
        if (pending === 0) {
          // When group is done, call the callback
          localCallback(error, result);
        }
      }
      process.nextTick(check); // Ensures that check is called at least once

      // Generates a callback for the group
      return function () {
        var index = counter++;
        pending++;
        return function () {
          pending--;
          // Compress the error from any result to the first argument
          if (arguments[0]) {
            error = arguments[0];
          }
          // Send the other results as arguments
          result[index] = arguments[1];
          if (!lock) { check(); }
        };
      };
    };

    // Start the engine an pass nothing to the first step.
    next();
  }

  // Tack on leading and tailing steps for input and output and return
  // the whole thing as a function.  Basically turns step calls into function
  // factories.
  Step.fn = function StepFn() {
    var steps = Array.prototype.slice.call(arguments);
    return function () {
      var args = Array.prototype.slice.call(arguments);

      // Insert a first step that primes the data stream
      var toRun = [function () {
        this.apply(null, args);
      }].concat(steps);

      // If the last arg is a function add it as a last step
      if (typeof args[args.length-1] === 'function') {
        toRun.push(args.pop());
      }


      Step.apply(null, toRun);
    }
  }

;(function(){
//	function binding(url, args, dataKey, el, tpl, renderer, isJSONP, afterLoad_Fn, isAppend_DOM){
	function binding(url, args, el, tpl, config) {
		var dataKey = config.dataKey || 'results',
			isJSONP = config.isJSONP,
			afterLoad_Fn = config.afterLoad_Fn,
			isAppend_DOM = config.isAppend,
			renderer = config.renderer || binding.renderer;
		
		/**
		 * 请求完毕之后的回调函数。可以先行对改函数进行配置
		 * @param {JSON} json 服务端返回 JSON
		 */
		var cb = (function (json, xhr, dataKey, tplEl, tpl, renderer) { 	
			// 数据为 array 还有数据行数
			var data;
			if(dataKey.indexOf('.') != -1) {
				var arr = dataKey.split('.');
				data = json[arr[0]][arr[1]]; // 一般只有两层
				json = json[arr[0]];
			}else data = json[dataKey];
			var j = data ? data.length : 0;
			// 符合记录的总数，不过该字段的 key 不支持配置，写死了
			config.lastQueryLength = json['totalCount'] || json['total'];
			
			// 如果是 string，那么获取元素先
			if(typeof tplEl == 'string')tplEl = document.querySelector(el);
			
			if (config.lastQueryLength == 0) {
				tplEl.innerHTML = '没有数据哦~';
			}else if (j > 0) { // 有记录
				var lis = [];
				for (var i = 0 ; i < j; i++) {
					var _data = renderer ? renderer(data[i]) : data[i]; // 很细的颗粒度控制记录
					
					if(_data === false) continue; // 返回 false 则跳过该记录，不会被渲染
					var li = ajaxjs.tppl(tpl, _data);
					
					if (isAppend_DOM) { // 分页是累加的
						// 利用 div 添加到 tplEl
						var temp = document.createElement('div');
						temp.innerHTML = li;
						var loadingIndicator = tplEl.querySelector('.loadingIndicator');
						if (loadingIndicator) { // 如果有 loadingIndicator，則干掉
							tplEl.removeChild(loadingIndicator);
						}

						tplEl.appendChild(temp.querySelector('li'));
					} else {
						lis.push(li);// 之前的 tag 不要了
					}
				}
				
				if(!isAppend_DOM) tplEl.innerHTML = lis.join('');
			}else {
				throw '数据异常';
			}
			// AOP after
			afterLoad_Fn && typeof afterLoad_Fn == 'function' && afterLoad_Fn(data, tplEl);
		}).delegate(null, null, dataKey, el, tpl, renderer);

		// 发起请求
		if (typeof isJSONP == 'undefined' || isJSONP == true) {
			ajaxjs.xhr.jsonp(url, args, cb);
		} else
			ajaxjs.xhr.get(url, args, cb);
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
	
	/**
	 * 渲染一个列表
	 * @param url 接口地址
	 * @param el 渲染到那个元素的下面
	 * @param args 请求参数
	 * @param config 配置
	 */
	ajaxjs.List = function (url, el, args, config) {
		if(!url)throw '未指定 url 参数！服务端地址是神马？';
	    if(!el) throw '未指定 ui 控件元素，通常这是一个 ul，里面有item 也就是 <li>...</li> 元素';
	    
	    config = config || {};
	    config.lastQueryLength = null;
		
		var tpl = config.tpl; // just tpl string
		if (typeof tpl == 'string' && tpl[0] == '.') {
			tpl = document.querySelector(tpl); // passed CSS Selector
			tpl = tpl.value;
		}else if(tpl && tpl.value) {
			tpl = tpl.value // passed textarea element object
		}else if(!tpl) {
			tpl = '<li>\
				<a href="javascript:play({id}, {contentType}, {feeFlag}, _g_feeCode_RawString);" >\
				<img data-src="{horizontalPic}?w={0}" onload="this.classList.add(\'tran\')" />\
				<h3>{name}</h3>\
				</a>\
				<div class="black_mask"></div>\
			</li>'; 
		}
		
		tpl = tpl.replace('{0}', getCellRequestWidth());
		
		var _args = {};
		if(window['_g_baseParams']) 
			for(var i in _g_baseParams)_args[i] = _g_baseParams[i];
		
		if(args)  // 需要分页
			for(var i in args)_args[i] = args[i];
		
		if(config.pager) {
			var pageSize = config.pageSize || 10, // 每页显示多少笔记录，默认十笔
				pageNo = 1;// 已加载第一页，所以从第二页开始
			_args.limit = _args.pageSize =pageSize; // limit 和 pageSize 两种方式都传
			
			var loadMoreBtn = typeof config.loadMoreBtn == 'string' ? document.querySelector(config.loadMoreBtn) : config.loadMoreBtn;
			
			// 这里不要用  addEventListener(),否则会形成一个堆栈，
			if (loadMoreBtn) {
				loadMoreBtn.onclick = (function(e) {
					e.preventDefault();
					if(pageNo < 1)pageNo = 1;// 不能向前
					pageNo++;
					
					var start = (pageNo - 1) * pageSize; 
					if(config.lastQueryLength != null && start >= config.lastQueryLength) {
						// 不能超出更多
						//loadMoreBtn.removeEventListener('click', arguments.callee);
						//loadMoreBtn.innerHTML = '最后一页';
						return;
					} else {
						//loadMoreBtn.innerHTML = '下一页';
						//loadMoreBtn.onclick = arguments.callee;
					}
					//var offset = start + pageSize; // 
					_args.start = start;
					_args.pageNo = pageNo;
					
					var cb = config && config.cb ? _imageHandler.after(config.cb) : _imageHandler;
					config.afterLoad_Fn = cb;
					binding(url, _args, el, tpl, config);
				});
			}
			
			var perBtn = typeof config.perBtn == 'string' ? document.querySelector(config.perBtn) : config.perBtn;
			if (perBtn) {
				perBtn.onclick = (function(e) {
					e.preventDefault();
					pageNo--;
					if(pageNo < 0)pageNo = 1;// 不能向前
					
					var start = (pageNo - 1) * pageSize; 
					if(config.lastQueryLength != null && start < 0){
						// 不能超出更多
						//perBtn.removeEventListener('click', arguments.callee);
						//perBtn.innerHTML = '没有前一页';
						return;
					} else{
						//perBtn.innerHTML = '前一页';
						//perBtn.onclick = arguments.callee;
					}
					//var offset = start + pageSize; // 
					_args.start = start;
					
					var cb = config && config.cb ? _imageHandler.after(config.cb) : _imageHandler;
					config.afterLoad_Fn = cb;
					binding(url, _args, el, tpl, config);
				});
			}
		}
		var _imageHandler = imageHandler.delegate(null, null, config); // 定义 imageHandler
		var cb =  config && config.cb ? _imageHandler.after(config.cb) : _imageHandler;// 连接两个函数
		config.afterLoad_Fn = cb;
		
		binding(url, _args, el, tpl, config);
		
		// 每次请求都附带的参数
		config.adjustArgs = function() {
			if (this.baseParam) 
				for ( var i in this.baseParam) 
					args[i] = this.baseParam[i];
				
			ajaxjs.List(url, el, args, config);
		}
		return config;
	}
	
	function imageHandler(data, tplEl, config) {
		// 分页
		if(config.pageSize) {
			var loadMoreBtn = typeof config.loadMoreBtn == 'string' ? document.querySelector(config.loadMoreBtn) : config.loadMoreBtn;
			if (loadMoreBtn) {	
				if(config.lastQueryLength != null && (config.lastQueryLength <= config.pageSize && loadMoreBtn)) { // 足够容纳，无须分页
					// 不能超出更多
					loadMoreBtn.innerHTML = '最后一页';
				}else{
					loadMoreBtn.innerHTML = '下一页';
				}
			}
		}
		
		var imgs = [];
		// 获取图片列表
		tplEl.eachChild('img[data-src^="http://"]', function(img, index) {
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
//			for(var i = 0, j = imgs.length; i < j; i++){
//				imgs[i].el.addEventListener('load', this.parallel());
//			}
			
			var i = 0;
			var nextStep = this;
			var id = setInterval(function() {
				var imgObj = imgs[i++];
				imgObj.el.src = imgObj.src;
				
				if(i == imgs.length) {
					clearInterval(id);
					// 同步高度
					if(config && config.isNoAutoHeight) {
					}else nextStep();
				}
			}, 300);
			
//			for(var i = 0 , j = imgs.length; i < j; i++){
//				setTimeout(showImg.bind(imgs[i]), i * 200);
//			}
//			function showImg(){
//				this.el.src = this.src;
//				//this.el.classList.add('tran');// ios 不能这里处理动画，改而在 onload 事件中
//			}
		}, function() {
			autoHeight();
			if(config.isNotAutoHeight) {
			} else {
				//UserEvent.onWinResizeFree(autoHeight);
			}
		});
		
		function autoHeight(){
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
		
		return [data, tplEl, config]; // AOP 需要的参数
	}
	
	/**
	 * 固定图片高度
	 */
	function fixImgHeigtBy() {
		window.innerWidth * 0.3 / 1.333
	}
})();


/*
 * --------------------------------------------------------
 * 复合 tab & list
 * --------------------------------------------------------
 */
bf_scrollViewer_list = function(url, scrollViewer_el, tab_el, loadingIndicatorTpl, itemTpl, renderItem, requestParams,cfg) {
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
	
	_event.on('update', function(activeId){ // activeId = 选中 id
		data.activeId = activeId;
		
		var _requestParams = {}; // 请求参数，附加上 id
		if(requestParams){
			for(var i in requestParams)_requestParams[i] = requestParams[i];
		}
		_requestParams[cfg.id_fieldName || 'id'] = activeId;
		
		// tab hightlight
		tabHeader.eachChild('li', function(li){
			//debugger;
			if(li.className.indexOf(activeId) != -1)li.classList.add('selected');
			else li.classList.remove('selected');
		});
		//alert(i);
		
		// render list
		var listStyle = location.getUrlParam('style'); // ui 风格
		for(var i = 0, j = data.sectionsIds.length; i < j; i++){
			if(data.sectionsIds[i].id == activeId){
				// ui
				_tab.go(i);
				
				if(data.sectionsIds[i].loaded){// 已加载
				}else{
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
		}else {
			location.setUrl_hash('id', activeId);
		} 
	});
	
	// 先获取所有 section id
	tabHeader.eachChild('li', function(li) {
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
		
		tabHeader.eachChild('li', function(li, i) { 
			if (el == li) {
				var id = li.className.match(/id_(\w+)/).pop();
				_event.fireEvent('update', id);
				return;
			}
		});
	}
	
	return _event;
}