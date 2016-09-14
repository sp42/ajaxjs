/**
 * 格式化字符串。
 * alert('sdsdsd{[(value.foo);]}sffsd{["rtertre"]}lsds'.format({foo:888888}));
 * @param  {String} string 带标记的字符串，或者可变长度的字符串参数。
 * @param  {String} value1 第一个值，替换{0}。The value to replace token {0}
 * @param  {String} value2 第二个值，替换{1}...等等（可以有任意多个）。Etc...
// * @return {String} 转化过的字符串。The formatted string
 */
String.prototype.format = (function(){
	var execJS = /{\[([^\]]*)\]}/;

	/**
	 * 捕获 js 段并执行返回。
	 * @param {String} key
	 * @return {String}
	 */
	function parseJS(str, value) {
		var match, js, fnBody = 'with(value){ return {0};} ';
		var exeResult;
		while ((match = execJS.exec(str)) != null) {
		    js = match[1];
		    js = fnBody.format(js);
		    js = new Function('value', js);
		    
		    exeResult = '';
		    
		    try{
		    	exeResult = js(value);
		    }catch(e){
		    	exeResult = '[[err]]';
		    	console.warn('parse tpl err:' + e.toString());
		    }
		    
		    str = str.replace(match[0], exeResult);
		}
		
		return str;
	}
	
	return function () {
		var str = this; 
		if(typeof(arguments[0]) == 'string' || typeof(arguments[0]) == 'number'){
			for(var i = 0, j = arguments.length; i < j; i++)
				str = str.replace(new RegExp('\\{' + i +'\\}', 'g'), arguments[i]);
		}else{
			//str = parseJS(str, arguments[0]);
			// 传入  obj 参数才可以 exe
			for(var i in arguments[0])
				str = str.replace(new RegExp('\\{' + i +'\\}', 'g'), arguments[0][i]); // 大小写敏感
		}
		
		return str.toString();
	};
})();

Date.prototype.format = function(format) // author: meizz
{
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}

// IE8 不支持，IE9开始支持。 安卓 2.2 不支持
if(!Function.prototype.bind){
    Function.prototype.bind = function () {
        if (!arguments.length) return this;
        var _ = this, p = Array.prototype.slice.call(arguments), A = p.shift();
        return function () {
            return _.apply(A, p.concat(Array.prototype.slice.call(arguments)));
        }
    }
}

/**
 * 函数委托 参见 http://blog.csdn.net/zhangxin09/article/details/8508128
 * @return {Function}
*/
Function.prototype.delegate = function () {
    var self = this, scope = this.scope, args = arguments, aLength = arguments.length, fnToken = 'function';

    return function(){
        var bLength = arguments.length, Length = (aLength > bLength) ? aLength : bLength;

        // mission one:
        for (var i = 0; i < Length; i++)
            if (arguments[i])args[i] = arguments[i]; // 拷贝参数

        args.length = Length; // 在 MS jscript下面，arguments作为数字来使用还是有问题，就是length不能自动更新。修正如左:

        // mission two:
        for (var i = 0, j = args.length; i < j; i++) {
            var _arg = args[i];
            if (_arg && typeof _arg == fnToken && _arg.late == true)
                args[i] = _arg.apply(scope || this, args);
        }

        return self.apply(scope || this, args);
    };
};

/**
 * 设置一个后置函数。
 * @param {Function} composeFn
 * @param {Boolean} isForceCall 是否强行执行 call 方法。设置为 true 在数组作为单独对象的时候有用。
 * @param {*} scope
 * @return {Function}
 */
Function.prototype.after = function (composeFn, isForceCall, scope) {
    var self = this;

    return function () {
        var result = self.apply(scope || this, arguments);

        if (isForceCall)return composeFn.call(this, result);
        
        var isApply = result && (typeof result.pop != 'undefined') && (typeof result.pop != 'unknown');
        return isApply ? composeFn.apply(this, result) : composeFn.call(this, result);
    };
}


function UserEvent(){  
    var events = {};  
      
    this.addEvents = function(){  
        for(var i = 0, j = arguments.length; i < j; i++){  
            events[arguments[i].toLowerCase()] = [];  
        }  
    }  
      
    /** 
      * 添加一个事件侦听器。 
      * @param  {String}   name 
      * @param  {Function} fn 
      * @return {this} 
      */  
    this.addListener = this.on = function(name, eventHandler) {  
        var eventQueen = events[name.toLowerCase()];  
        if(!eventQueen) throw '没有该事件！请使用addEvent()增加事件';  
  
        eventQueen.push(eventHandler);            
        return this;  
    }
      
    /** 
      * 触发事件。 
      * @param {String} name 
      * @param {Array}  args 
      * @return {Boolean} 
      */  
    this.fireEvent = function(name) {  
        var eventQueen = events[name.toLowerCase()]; // listeners  
        if(!eventQueen)throw 'No such event:' + name;
        var args = eventQueen.length && Array.prototype.slice.call(arguments, 1);   
                      
        var result;  
        var output = [];  
          
        for (var i = 0, j = eventQueen.length; i < j; i++) {  
            result = eventQueen[i].apply(this, args);  
              
            if(result === false){  
            	return false;
            }else{  
                output.push(result);  
            }  
        }  
      
        return output;  
    }  
      
    /** 
      * 移除事件侦听器。须传入原来的函数句柄。 
      * @param {String}   name 
      * @param {Function} fn 
      * @return {this} 
      */  
    this.removeListener = function(name, fn) {  
        if (events[name])
            Array_Remove(events[name], fn);  
         
        return this;  
    }  

    /** 
     * 删掉某个元素。 
     * @param   {Array} arr 
     * @param   {Mixed} el 
     * @return  {Mixed} 
     */  
    function Array_Remove(arr, el){  
    	var index = -1;  
    	for(var i = 0, j = arr.length; i < j; i++){  
    		if(arr[i] == el){  
    			index = i;  
    			break;  
    		}  
    	}  
    	arr.splice(index, 1);  
    	return el;  
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
//----------------------lang.js------------------------------------------
// XHR 异步请求
;(function(){
	// 注意 url 部分带有 # 的话则不能传参数过来
	function request(url, args, cb, cfg, method) {	
		var params = json2url(args);
		var xhr = new XMLHttpRequest();
		
		if(method == 'POST' || method == 'PUT'){
			xhr.open(method, url);
		}else xhr.open(method, url + (params ? '?' + params : ''));
		
		xhr.onreadystatechange = function (event, cb, parseContentType){
			if(this.readyState === 4 && this.status === 200) {				
				var data = null;
				try{
					var responseText = this.responseText.trim();
					if(!responseText)throw 'Server-side return EMPTY String!';
					
					switch(parseContentType){
						case 'text':
							data = this.responseText;
							break;
						case 'xml':
							break;
							data = this.responseXML;
						case 'json':
						default:
							data = JSON.parse(responseText);
					}
				}catch(e){
					alert('ajax error:\n' + e); // 提示用户 异常
				}
				if(!cb)throw '你未提供回调函数';
				cb(data, this);
			}
		}.delegate(null, cb, cfg && cfg.parseContentType);
		
		if(method == 'POST' || method == 'PUT'){
			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			xhr.send(params);
		}else{
			xhr.send(null);
		}
	}
	// GET 请求
	XMLHttpRequest.get	= request.delegate(null, null, null, null, 'GET');
	XMLHttpRequest.post = request.delegate(null, null, null, null, 'POST');
	XMLHttpRequest.put 	= request.delegate(null, null, null, null, 'PUT');
	XMLHttpRequest.dele	= request.delegate(null, null, null, null, 'DELETE');

	/**
	 * e.g
	 * XMLHttpRequest.jsonp(
			'http://u1.3gtv.net:2080/pms-service/section/content_list', 
			{
				start:0,
				limit:10,
				sort:0,
				portalId:45,
				id:3290
			},
			function(json){
				console.log(json);
			}	
		);
	 * @param url 请求远程路径
	 * @param args 参数
	 * @param cb 回调函数
	 * @param cfg 该次请求的配置
	 */
	XMLHttpRequest.jsonp = function (url, args, cb, cfg){
		var globalMethod_Token	= 'globalMethod_' + getRandom();
		
		if(!window.$$_jsonp)window.$$_jsonp = {};
		// Map<String, Function>
		window.$$_jsonp[globalMethod_Token] = cb;
		
		var scriptTag = document.createElement('script');
	
		args = args || {};
		args[cfg && cfg.callBackField || 'callBack'] = '$$_jsonp.' + globalMethod_Token; 
		
		scriptTag.src = json2url(args, url);
		document.getElementsByTagName('head')[0].appendChild(scriptTag);
		
		function getRandom(){
			return parseInt(Math.random()*(200000-10000 + 1) + 10000); 
		}
	};
	
	// shorthand
	window.xhr = XMLHttpRequest;
	
	function json2url(json, appendUrl) {
//		var params = JSON.stringify(json);
//		params = params.replace(/,/g, '&').replace(/:/g,'=').replace(/\{|}|"/g, '');
		var params = [];
		for(var i in json)params.push(i  + '=' + json[i]);
		
		params = params.join('&');
		
		if(appendUrl) // 如果有 ? 则追加，否则加入 ?
			params = ~appendUrl.indexOf('?') ?  appendUrl + '&' + params :  appendUrl + '?' + params;
		
		return params;
	}
})();

;(function () {
	// gb2312，Chrome和Opera会返回gbk，当前页面非UTF8编码的。推荐使用 UTF-8。
	(document.charset || document.characterSet).toLowerCase() != 'utf-8' && console.log('Pls use UTF8 encoding!非 UTF-8 页面编码。推荐使用 UTF-8');
	
	var ua = this.userAgent.toLowerCase();
	
	this.isWebkit = /webkit/.test(ua);
	if(this.isWebkit)this.webkitVersion = Number(ua.match(/webkit\/(\d+\.\d+)/)[1]);
	
	this.isIE = [, ] != 0;
	if(this.isIE){
		this.isIE6 = /msie 6/.test(ua);// ie6.test(ua) && alert('不支持' +  ie6 + '浏览器');
		this.isIE7 = /msie 7/.test(ua);
		this.isIE8 = /msie 8/.test(ua);
		this.isIE9 = /msie 9/.test(ua);
	}
	
	this.isFF = /firefox/i.test(ua);
	
	// Androdi & iPhone Detect...
	this.isAndroid = ua.indexOf("android") != -1;
	this.is_iOS    = ua.indexOf("iphone") != -1 || ua.indexOf("ipod") != -1 || ua.indexOf("ipad") != -1;
	
	var regexp, osVer;
	if(this.isAndroid === true) {
		regexp = /(?!android\s+)[24]/i;
		osVer  = regexp.exec(ua);
		if(osVer === null){
			//alert('虽为安卓机型，但不被支持，ua:' + ua);
		}else {
			osVer = osVer[0];
			this.isAndroid_2 = (osVer == 2);
			this.isAndroid_4 = (osVer == 4);
		}
	}else if (this.is_iOS) {
		regexp = /(?!(?!iphone|ipod|iPad)\s+os\s+)\d(?!_\d_\d)/i;
		osVer  = regexp.exec(ua);
		if(osVer === null)alert('虽为iOS机型，但不被支持，ua:' + ua);
		else{
			osVer = osVer[0];
			
			this.is_iOS_4 = (osVer == 4);
			this.is_iOS_5 = (osVer == 5);
			this.is_iOS_6 = (osVer == 6);
		}
	}
	
}).call(window.navigator);

;(function(){	

	// Get a element
	$$ = function(cssSelector) {
		var el = document.querySelector(cssSelector);// class name if matched
		if (!el)
			el = document.querySelector('input[name="' + cssSelector + '"]'); // form
																				// input
		return el;
	}
	
	/**
	 * 增加元素样式。
	 * @param {String} cls
	 */
	Element.prototype.addCls = function (cls) {
		cls = removeFirstDot(cls);

		var _cls = this.className;
		if (_cls.indexOf(cls) === -1) {
			// not found, so add it
			if (_cls === '')this.className = cls;
			else this.className += ' ' + cls;
		}
		
		return this;
	}
	
	/**
	 * 移除元素样式。
	 * @param {String} cls
	 */
	Element.prototype.removeCls = function (cls) {
		cls = removeFirstDot(cls);
		var _cls = this.className;
		var reg = new RegExp('\\s?\\b' + cls + '\\b', 'ig');
		if (reg.test(_cls)) 
			this.className = _cls.replace(reg, '');// remove it
		
		return this;
	}
	
	/**
	* 轮回样式。
	* @param {String} cls 样式
	*/
	Element.prototype.toggleCls = function (cls) {
		cls = removeFirstDot(cls);
		
		var _cls = this.className;
		var reg = new RegExp('\\s?\\b' + cls + '\\b');
		if (reg.test(_cls))
			this.className = _cls.replace(reg, '');// remove it
		else {
			// add it
			if (_cls === '') this.className = cls;
			else this.className += ' ' + cls;
		}
		
		return this;
	}
	
	var emptyStr = '';
	// 允许用户输入 .abc/abc 的 CSS Selector，推荐 abc 即可
	function removeFirstDot(str){
		if(str && str.charAt(0) === '.') {
			var arr = str.split(emptyStr);
			arr.shift();
			return arr.join(emptyStr);
		}
		
		return str;
	}
	
	/**
	 * 对 querySelectorAll 的改进，不用写循环
	 * @param arg
	 * @returns
	 */
	Element.prototype.eachChild = function (arg, eachFn) {
		if(arguments.length > 1 && typeof eachFn == 'function') { // all
		}else throw 'args not good!';
		
		var els = this.querySelectorAll.call(this, arg);
		Array.prototype.forEach.call(els, eachFn);
		
		return els;
	}
	
	Element.prototype.up = function(tagName, className) {
		if(tagName && className)throw '只能任选一种参数，不能同时传';
		var el = this.parentNode;
		tagName = tagName && tagName.toUpperCase();
		while(el){
			if(tagName && el.tagName == tagName)return el;
			if(className && el.className && ~el.className.indexOf(className))return el;
			el = el.parentNode;
		}
		return null;
	}	
	
	Element.prototype.insertAfter = function (newElement) {  
	    var targetElement = this, 
	    	parent = targetElement.parentNode; 
	    
	    if (parent.lastChild == targetElement)
	    	parent.insertBefore(newElement);  
	    else 
	    	parent.insertBefore(newElement,targetElement.nextSibling);
	}
	
	window.aj = document.querySelector;
	Element.prototype.$  = Element.prototype.querySelector;
	Element.prototype.$$ = Element.prototype.querySelectorAll;
})();

/**
 * 优先查询 parent win 中的元素
 * 有时后台中需要获取 顶部 元素，即 parent.xx
 */
Element.getParentEl = (function(){
	var empty = {}; // 空对象，为避免空指针
	return function(cssSelector){
		return parent.document.querySelector(cssSelector) 
			|| document.querySelector(cssSelector) 
			|| empty;
	}
})();

location.setUrl_hash = function (key, value){
	var hash = window.location.hash;
	if(hash.indexOf(key) != -1){
		var reg = new RegExp(key + '=[\\w-]+');
		window.location.hash = hash.replace(reg, key + '=' + value);
	}else{			// append
		if(!hash)window.location.hash = key + '=' + value;
		else window.location.hash += '&' + key + '=' + value;
	}
}

location.setUrl_param = function (key, value){
	var hash = window.location.href;
	if(hash.indexOf(key) != -1){
		var reg = new RegExp(key + '=[\\w-]+');
		window.location.href = hash.replace(reg, key + '=' + value);
	}else{			// append
		if(!hash)window.location.href = key + '=' + value;
		else window.location.href += '&' + key + '=' + value;
	}
}

// location.getUrlParam('id', location.hash)
location.getUrlParam = function (name, url) {
	var search;
	if(url){
		search = (url.indexOf('?') != -1) ? url.substr(url.indexOf('?')) : url.substr(url.indexOf('#'));
	}else search = window.location.search;
		
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return decodeURI( r[ 2 ] ); 
    return null; //返回参数值
}

// 导入 js 文件，有缓存和回调功能
location.loadScript = function(src, fn){
	if(!location.loadScript.loaded[src]){
		var script = document.createElement('script');
		script.src = src;
		script.onload = fn;
		document.body.appendChild(script);
		
		location.loadScript.loaded[src] = true;
	}else{
		fn();
	}
}

location.loadScript.loaded = {};

/**  
 * 读取 search 和 hash 的参数  
 * location.params().hash['appinstall'];
 * location.params().search['isappinstalled'];
 */  
location.params = function (search, hash){  
    search  = search    || window.location.search;  
    hash    = hash      || window.location.hash;  
      
    var fn = function(str, reg){  
        if(str){  
            var data = {};  
            str.replace(reg,function( $0, $1, $2, $3 ){  
                data[ $1 ] = $3;  
            });  
            return data;  
        }  
    };
      
    return {  
        search: fn(search,  new RegExp( "([^?=&]+)(=([^&]*))?", "g" ))||{},  
        hash:   fn(hash,    new RegExp( "([^#=&]+)(=([^&]*))?", "g" ))||{}  
    };  
}

// 优化 
UserEvent.onWinResizeFree = function(fn, eventType){
	eventType = eventType || 'resize'; // resize|scroll
	var self = UserEvent.onWinResizeFree;
	self.handler.push(fn);
	
	if(!self.init[eventType]){		
		
		UserEvent.onWinResizeFree.init[eventType] = true;
	}
	(function(){  
		var me = arguments.callee;  
		window.addEventListener(eventType, function(){  
			window.removeEventListener(eventType, arguments.callee);  
			window.setTimeout(function(){  
				for(var i = 0, j = self.handler.length; i < j; i++){
					var obj = self.handler[i];
					if(typeof obj == 'function'){
						obj();
					}else if(typeof obj.fn == 'function' && !obj.executeOnce){
						obj.fn.call(obj);
						//obj.done = true;
					}
				}
				me();  // 回调  resize|scroll  事件 
			}, 300); //300毫秒执行一次  
		});  
	})();
	
	// 先执行一次
	var obj = fn;
	if(typeof obj == 'function'){
		obj();
	}else if(typeof obj.fn == 'function' && !obj.done){
		obj.fn.call(obj);
	}
}

UserEvent.onWinResizeFree.init = {};
UserEvent.onWinResizeFree.handler = [];

UserEvent.onEl_in_viewport = function(el, fn){
	setTimeout(function(){ // 加入延时，让 dom pic 加载好，高度动态的
		UserEvent.onWinResizeFree({
			executeOnce : false,
			fn: function(){
				var scrollTop = document.body.scrollTop, docHeight = window.innerHeight;
				var elTop = el.getBoundingClientRect().top, 
					isFirstPage = /*scrollTop == 0 &&*/ docHeight > elTop, 
					isInPage = scrollTop > elTop;
					
				console.log(isFirstPage || isInPage);
				
				if (isFirstPage || isInPage) {
					this.executeOnce = true;
					fn();
	//				alert('saw it!');
				}
			}
		}, 'scroll');
	}, 1500);
}

UserEvent.onEl_in_viewport.actions = [];
;(function(){
	bf_Fx = {};
	/**
	 * @type {Boolean} 是否支持 CSS3 的 transitions。
	 */
	var isSupportsTransitions = (function () {
		var b = (document.body || document.documentElement).style;
		var p = 'transition';
		if(typeof b[p] == 'string') {return true; }
		
		// Tests for vendor specific prop
		v = ['Moz', 'Webkit', 'Khtml', 'O', 'ms'],
		p = p.charAt(0).toUpperCase() + p.substr(1);
		for(var i = 0, j = v.length; i < j; i++) {
			if(typeof b[v[i] + p] == 'string') {
				return true; 
			}
		}
		return false;
	})();
	
	/**
	 * 淡淡显示的效果。
	 * @param elem {Element} 需要淡入的元素
	 * @param speed {Number} 淡入速度,正整数(可选)
	 * @param opacity {Number} 淡入到指定的透明度,0~100(可选)
	 * @return {Element}
	 */
	bf_Fx.fadeIn = function(elem, speed, opacity){
		 // 若使用 Transition，则参数 speed、opacity 无效，必须通过 CSS 设置动画参数
		 if(isSupportsTransitions){
		     elem.style.display = 'block';
		     elem.style.opacity = .6;
		     setTimeout(function(){ // 不能同步进行，依赖 setTimeout 延时一下
		    	 elem.style.opacity = 1;
		     }, 100);
		 }else{
		 speed   = speed   || 80;
		 opacity = opacity || 100;
		
		 //初始化透明度变化值为0
		 var val = 0;
		 var elStyle = elem.style;
		
		 // 显示元素,并将元素值为0透明度(不可见)
		     elStyle.opacity = val;
		     elStyle.display = 'block';
		
		     //循环将透明值以5递增,即淡入效果
		             (function(){
		                     elStyle.opacity = val / 100;
		                     val += 20;
		                     if (val <= opacity) {
		                             window.setTimeout(arguments.callee, speed);
		                     }
		             })();
		     }
		
		 return elem;
	 }

	 /**
	 * 淡淡消失的效果。
	 * @param elem {Element} 需要淡入的元素
	 * @param speed {Number} 淡入速度,正整数(可选)
	 * @param opacity {Number} 淡入到指定的透明度,0~100(可选)
	 */
	bf_Fx.fadeOut = function(elem, speed, opacity){
	     if(isSupportsTransitions){
	             elem.style.opacity = 0;
	             setTimeout(function(){
	                     elem.style.display = 'none';
	             }, 300);
	     }else{
	             speed   = speed     || 90;
	             opacity = opacity   || 0;

	             var val = 100;// 初始化透明度变化值为0
	     var elStyle = elem.style;

	             (function(){
	         elStyle.opacity = val / 100;
	                     val -= 20;// 循环将透明值以 20 递减,即淡出效果
	                     if (val >= opacity) {
	                             window.setTimeout(arguments.callee, speed);
	                     }else if (val < 0) {
	                             elStyle.display = 'none';// 元素透明度为0后隐藏元素
	                         }
	                 })();
	         }
	     return elem;
	}
})();

//******----------------------------******
//              WEB
//******----------------------------******

//img
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

//移除浏览器地址栏 
window.scrollTo(0, 1);  

/**
 * 页面呼叫客户端
 * http://blog.csdn.net/zhangxin09/article/details/38011051
 * 依赖标签：
 * <iframe class="openApp hide" src="about:blank"></iframe>
 * @param el
 * @param downloadUrl
 */
function openClient(el, downloadUrl){
	var iframe = arguments.callee.iframe;
	
	if(!iframe){
		iframe = document.querySelector('.openApp')
		arguments.callee.iframe = iframe;
	}
	var startTime = +new Date();
	iframe.src = el.getAttribute(navigator.is_iOS ? 'data-scheme-ios' : 'data-scheme-android');
	
	// 如果已知安装的了，则无须跳转下载页
//	setTimeout(function() { // 如果不能打开客户端，跳到下载页面
//        if (Date.now() - startTime < 500)
//        	window.location.href = downloadUrl;
//    }, 400);
}


;(function(){
	// 回到顶部
	var timer = null;
	var b = 0;//作为标志位，判断滚动事件的触发原因，是定时器触发还是其它人为操作
	UserEvent.onWinResizeFree(function(e) {
		if (b != 1) clearInterval(timer);
		b = 2;
	}, 'scroll');
	
	window.goTop = function () {
		clearInterval(timer);
		var iCur = speed = 0;
		timer = setInterval(function() {
			iCur = document.documentElement.scrollTop || document.body.scrollTop;
			speed = Math.floor((0 - iCur) / 8);
			
			if (iCur === 0) clearInterval(timer);
			else document.documentElement.scrollTop = document.body.scrollTop = iCur + speed;
			b = 1;
		}, 30);
	}
})();

//******----------------------------******
//              表单
//******----------------------------******
;(function(){
	
	/**
	 * new bf_form(document.querySelector('form.addUser')).on('afterSubmit', bf_form.commonAfterSubmit); 
	 * @param formEl
	 * @returns {bf_form}
	 */
	bf_form = function(formEl, cfg){
		this.cfg = cfg || {};
		this.formEl = formEl;
		if(!this.formEl)throw '未设置表单元素！';
		if(!this.formEl.action)throw '未设置接口地址！';
		
		// this.formEl.method always GET, so this.formEl.getAttribute('method') instead
		var method = this.formEl.getAttribute('method').toLowerCase();
		if(method== 'post') this.PUT = false;
		if(method == 'put') this.PUT = true;

		UserEvent.call(this);
		
		this.addEvents('beforeSubmit', 'submit', 'afterSubmit');
		this.formEl.addEventListener('submit', fireSubmitEvent.bind(this));
		this.on('submit', onSubmit.bind(this));
		
		this.submit = function(){
			var formEl = this.formEl;
			if (formEl.fireEvent) { // ie
				formEl.fireEvent('onsubmit'); 
				formEl.submit(); 
			}else if(document.createEvent){ 
				var ev = document.createEvent('HTMLEvents'); 
				ev.initEvent('submit', false, true); 
				formEl.dispatchEvent(ev); 
			}else throw '触发提交事件失败，什么浏览器？？';
		}
		
		if(cfg && cfg.isCommonAfterSubmit)this.on('afterSubmit', bf_form.commonAfterSubmit);
//		autoInit.initCalendar(this.formEl);
//		autoInit.initImgPerview(this.formEl);
//		autoInit.whenpresscheckCharLength(this.formEl);
	}
	
	function fireSubmitEvent(e){
		e.preventDefault();
		if(this.fireEvent('beforeSubmit', this.formEl, e) !== false){
			this.fireEvent('submit', this.formEl, e);
		}
	}

	function onSubmit(){
		this.formData = serializeForm(this.formEl); // 保存表单数据引用
		
		// htmlEditor 没有标识自己不存在，@todo
		if(this.cfg.htmlEditor_hook)
			this.formData[this.cfg.htmlEditor_hook.fieldName || 'content'] = encodeURIComponent(this.cfg.htmlEditor_hook.getValue());			
		if(this.PUT){
			XMLHttpRequest.put(this.formEl.action, this.formData, fireAfterSubmitEvent.bind(this));
		}else{
			XMLHttpRequest.post(this.formEl.action, this.formData, fireAfterSubmitEvent.bind(this));
		}
	}
	
	function serializeForm(formEl, isStringOutput, isIgnroEmpty /* 是否忽略空字符串的字段 */){
		var formData = {};
		
		eachChild4form(formEl, function(el){
			var elType = el.type;
			if(elType == "text" || elType == "hidden" || elType == "password" || elType == "textarea"){
				formData[el.name] = getPrimitives(el.value);
			}else if(elType == "radio" || elType == "checkbox"){
				if(el.checked)// 选中才会加入数据
					formData[el.name] = getPrimitives(el.value);
			}else if(elType == "select-one" || elType == "select-multiple"){
				for(var opt, optValue, p = 0, q = el.options.length; p < q; p++){
					opt = el.options[p];
					if (opt.selected) {
						optValue = opt.hasAttribute ? opt.hasAttribute('value') : opt.getAttribute('value') !== null
						optValue = optValue ? opt.value : opt.text;			
						
						formData[el.name] = getPrimitives(optValue);
					}
				}
			}
		});

		if(isIgnroEmpty){
			for(var i in formData){
				if(formData[i] === "")delete formData[i];
			}
		}

		return isStringOutput ? utils.json2url(formData) : formData;
	}
	
	var url_regexp = /(https?:\/\/(?:www\.|(?!www))[^\s\.]+\.[^\s]{2,}|www\.[^\s]+\.[^\s]{2,})/;
	/**
	 * 输入参数，还原它的primitive值。有点类似 eval() 的作用，却不使用 eval()。
	 * @param  {Mixed} v
	 * @return {Mixed}
	 */
	function getPrimitives(v){
		if(v){
			if(v == 'true' ) return true;
			if(v == 'false') return false;
			if(v.toString() == (new Date(v)).toString()) return new Date(v); // v is a date but in Stirng Type
	      	if(v == String(Number(v))) return Number(v);
		}
		
		// 检查 url 编码
		if(url_regexp.test(v)){ v = encodeURIComponent(v);}
	  	return v;
	}

	// 判断form的内容是否有改变
	function isFormChanged(formEl){
		var i = 0;
		eachChild4form(formEl, function(el){
			switch(el.type){
	            case "text":
	            case "hidden":
	            case "password":
	            case "textarea":
	                if (el.defaultValue != el.value) return true;
	            break;
	            case "radio":
	            case "checkbox":
	                if (el.defaultChecked != el.checked) return true;
	            break;
	            case "select-one":
	                i = 1;
	            case "select-multiple":
	                opts = el.options;
	                for (;i < opts.length; i++)
	                	if (opts[i].defaultSelected != opts[i].selected)return true;
	            break;
			}
		});

		return false;
	}

	// 遍历表单
	function eachChild4form(formEl, fn){
		// 豁免：没有 name 属性的不要；禁止了的不要；特定的字段不要
		// !el.checked // 未选择的要来干嘛？?
		var ignore = /file|undefined|reset|button|submit/i;
		
		for(var el, i = 0, j = formEl.elements.length; i < j; i++){
			el = formEl.elements[i];
			if(!el.name || el.disabled || ignore.test(el.type))continue;
			fn(el, i);
		}
	}

	function fireAfterSubmitEvent(json){
		this.fireEvent('afterSubmit', this.formEl, json);
	}
	
	// 最简单的表单应答
	bf_form.commonAfterSubmit = function(f, j){
		alert(!j.isOk ? '操作失败，原因：\n' + j.msg :  j.msg);
	}
})();