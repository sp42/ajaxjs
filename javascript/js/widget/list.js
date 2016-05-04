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
		 * 
		 */
		var cb = (function (json, xhr, dataKey, tplEl, tpl, renderer){
			// 数据为 array 还有数据行数
			var data = json[dataKey], j = data ? data.length : 0;
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
					var li = tpl.format(_data);
					
					if(isAppend_DOM){ // 分页是累加的
						// 利用 div 添加到 tplEl
						var temp = document.createElement('div');
						temp.innerHTML = li;
						var loadingIndicator = tplEl.querySelector('.loadingIndicator');
						if (loadingIndicator) { // 如果有 loadingIndicator，則干掉
							tplEl.removeChild(loadingIndicator);
						}
				
						tplEl.appendChild(temp.querySelector('li'));
					}else{
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
		if(typeof isJSONP == 'undefined' || isJSONP == true){
			XMLHttpRequest.jsonp(url, args, cb);
		}else XMLHttpRequest.get(url, args, cb);
	}
	
	function getCellRequestWidth(){
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
	bf_list = function (url, el, args, config){
		if(!url)throw '未指定 url 参数！服务端地址是神马？';
	    if(!el) throw '未指定 ui 控件元素，通常这是一个 ul，里面有item 也就是 <li>...</li> 元素';
	    
	    config = config || {};
	    config.lastQueryLength = null;
		
		var tpl = config.tpl; // just tpl string
		if (typeof tpl == 'string' && tpl[0] == '.'){
			tpl = document.querySelector(tpl); // pass CSS Selector
			tpl = tpl.value;
		}else if(tpl && tpl.value) {
			tpl = tpl.value // pass element object
		}else if(!tpl){
			tpl = '<li>\
				<a href="javascript:play({id}, {contentType}, {feeFlag}, _g_feeCode_RawString);" >\
				<img data-src="{horizontalPic}?w={0}" onload="this.addCls(\'tran\')" />\
				<h3>{name}</h3>\
				</a>\
				<div class="black_mask"></div>\
			</li>'; 
		}
		tpl = tpl.format(getCellRequestWidth());
		
		var _args = {};
		if(window['_g_baseParams']){	
			for(var i in _g_baseParams)_args[i] = _g_baseParams[i];
		}
		
		if(args){ // 需要分页
			for(var i in args)_args[i] = args[i];
		}
		
		if(config.pager) {
			var pageSize = config.pageSize || 10, // 每页显示多少笔记录，默认十笔
				pageNo = 1;// 已加载第一页，所以从第二页开始
			_args.limit = pageSize;
			
			var loadMoreBtn = typeof config.loadMoreBtn == 'string' ? document.querySelector(config.loadMoreBtn) : config.loadMoreBtn;
			// 这里不要用  addEventListener(),否则会形成一个堆栈，
			if (loadMoreBtn){
				loadMoreBtn.onclick = (function(e) {
					e.preventDefault();
					if(pageNo < 1)pageNo = 1;// 不能向前
					pageNo++;
					
					var start = (pageNo - 1) * pageSize; 
					if(config.lastQueryLength != null && start >= config.lastQueryLength){
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
					var cb = config && config.cb ? _imageHandler.after(config.cb) : _imageHandler;
					config.afterLoad_Fn = cb;
					binding(url, _args, el, tpl, config);
				});
			}
			var perBtn = typeof config.perBtn == 'string' ? document.querySelector(config.perBtn) : config.perBtn;
			if (perBtn){
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
		config.adjustArgs = function(){
			if (this.baseParam) {
				for ( var i in this.baseParam) {
					args[i] = this.baseParam[i];
				}
			}
			bf_list(url, el, args, config);
		}
		return config;
	}
	
	function imageHandler(data, tplEl, config){
		// 分页
		if(config.pageSize){
			var loadMoreBtn = typeof config.loadMoreBtn == 'string' ? 
					document.querySelector(config.loadMoreBtn)
					: config.loadMoreBtn;
			if(config.lastQueryLength != null && (config.lastQueryLength <= config.pageSize && loadMoreBtn)){ // 足够容纳，无须分页
				// 不能超出更多
				loadMoreBtn.innerHTML = '最后一页';
			}else{
				loadMoreBtn.innerHTML = '下一页';
			}
		}
		var imgs = [];
		// 获取图片列表
		tplEl.eachChild('img[data-src^="http://"]', function(img, index){
			imgs.push({
				index : index,  // 序号
				el : img,		// img DOM 元素
				src : img.getAttribute('data-src') // 图片地址
			});
		});
		Step(function(){
			for(var i = 0 , j = imgs.length; i < j; i++){
				if(imgs[i].src){
					var img = new Image();
					img.onload  = this.parallel();	
					img.src = imgs[i].src;// 加载图片
				}
			}
		}, function(){
			// all images are local
			// 逐次显示
//			for(var i = 0, j = imgs.length; i < j; i++){
//				imgs[i].el.addEventListener('load', this.parallel());
//			}
			
			var i = 0;
			var nextStep = this;
			var id = setInterval(function(){
				var imgObj = imgs[i++];
				imgObj.el.src = imgObj.src;
				if(i == imgs.length){
					clearInterval(id);
					// 同步高度
					if(config && config.isNoAutoHeight){
					}else nextStep();
				}
			}, 300);
			
//			for(var i = 0 , j = imgs.length; i < j; i++){
//				setTimeout(showImg.bind(imgs[i]), i * 200);
//			}
//			function showImg(){
//				this.el.src = this.src;
//				//this.el.addCls('tran');// ios 不能这里处理动画，改而在 onload 事件中
//			}
		}, function(){
			autoHeight();
			if(config.isNotAutoHeight){
			}else{
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
	function fixImgHeigtBy(){
		window.innerWidth * 0.3 / 1.333
	}
})();

bf_scrollViewer_list = function(
	url, scrollViewer_el, tab_el, loadingIndicatorTpl, itemTpl, renderItem, requestParams,
	cfg
){
	cfg = cfg || {};
	
	var _tab = Object.create(bf_tab);
	_tab.el = tab_el;
//	_tab.isEnableTouch = true;
	
	var tabHeader = scrollViewer_el;
	var data = {	// 基于数据的状态管理
		activeId : null, //  选中 id
		sectionsIds : [] // 有什么栏目 id 放在这里
	};
	
	var _event = new UserEvent();// 没有 Object.watch() ，只能用事件代替
	_event.addEvents('update');
	
	_event.on('update', function(activeId){// activeId = 选中 id
		data.activeId = activeId;
		
		var _requestParams = {}; // 请求参数，附加上 id
		if(requestParams){
			for(var i in requestParams)_requestParams[i] = requestParams[i];
		}
		_requestParams.id = activeId;
		
		// tab hightlight
		tabHeader.eachChild('li', function(li){
			//debugger;
			if(li.className.indexOf(activeId) != -1)li.addCls('selected');
			else li.removeCls('selected');
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
					
					bf_list(url, ul, _requestParams,
						{
							isNoAutoHeight : listStyle == 'col2' || listStyle == 'col3' ? false : true, // 海报 col3 需要等高
							tpl : itemTpl,
							pager : cfg.pager !=  undefined ? cfg.pager : true, // 是否需要分页
							loadMoreBtn : el.querySelector('.loadMore'),
							pageSize : cfg.pageSize || 10, // 海报 col3 读9条
							renderer : renderItem,
							isAppend : true,
							cb : cfg.cb
						}
					);
					data.sectionsIds[i].loaded = true;
				}
				
				//location.href += '#id=' + activeId; // url
				
				break;
			}
		}
		
		// set hash
		if(cfg.isNoHash){
		}else location.setUrl_hash('id', activeId);
	});
	
	// 先获取所有 section id
	tabHeader.eachChild('li', function(li){
		var id = li.className && li.className.match(/\d+/).pop();
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
	
	tpl = tpl.format(location.getUrlParam('style') ||  cfg.style || 'col1');
	_tab.el.querySelector('div').innerHTML = new Array(data.sectionsIds.length + 1).join(tpl);
	_tab.init();
	
	
	var load_id;
	if(location.hash.indexOf('id=') != -1){// 有 hash id 读取
		load_id = location.hash.match(/id=(\d+)/).pop();
		
		if(!data.sectionsIds[0]){
			data.sectionsIds[0] = {
					id : load_id,
					loaded : false
			};	
			_tab.el.querySelector('div').innerHTML = tpl;
			_tab.init();
		}

		_event.fireEvent('update', load_id);
	}else{
		// 默认 选中第一 tab
		if(data.sectionsIds.length){
			load_id = data.sectionsIds[0].id;
		}else{
			// 没有子栏目，读取父栏目
			load_id = location.search.match(/id=(\d+)/).pop(); 
			data.sectionsIds[0] = {
				id : load_id,
				loaded : false
			};
			_tab.el.querySelector('div').innerHTML = tpl;
			_tab.init();
		}
		_event.fireEvent('update', load_id);
	}
	
	tabHeader.onclick =  function(e){
		var el = e.target;
		if(el.tagName != 'LI')el = el.parentNode;
		tabHeader.eachChild('li', function(li, i){
			if(el == li){
				var id = li.className.match(/\d+/).pop();
				_event.fireEvent('update', id);
				return;
			}
		});
	}
	
	return _event;
}