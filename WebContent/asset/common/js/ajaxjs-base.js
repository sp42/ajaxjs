// Setup a top namespace
//查找元素
ajaxjs = aj = function(cssSelector, fn) {
	return Element.prototype.$.apply(document, arguments);
}

/*
 * -------------------------------------------------------- 
 * 函数委托 参见 http://blog.csdn.net/zhangxin09/article/details/8508128 @return {Function}
 * --------------------------------------------------------
 */
Function.prototype.delegate = function() {
	var self = this, scope = this.scope, args = arguments, aLength = arguments.length, fnToken = 'function';

	return function() {
		var bLength = arguments.length, Length = (aLength > bLength) ? aLength : bLength;

		// mission one:
		for (var i = 0; i < Length; i++)
			if (arguments[i])
				args[i] = arguments[i]; // 拷贝参数

		args.length = Length; // 在
		// MSjscript下面，arguments作为数字来使用还是有问题，就是length不能自动更新。修正如左:

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
 * 
 * @param {Function}
 *            composeFn
 * @param {Boolean}
 *            isForceCall 是否强行执行 call 方法。设置为 true 在数组作为单独对象的时候有用。
 * @return {Function}
 */
Function.prototype.after = function(composeFn, isForceCall, scope) {
	var self = this;

	return function() {
		var result = self.apply(scope || this, arguments);

		if (isForceCall) {
			return composeFn.call(this, result);
		}

		return result && (typeof result.pop != 'undefined') && (typeof result.pop != 'unknown') ? composeFn.apply(this, result) : composeFn.call(this, result);
	};
}

/*
 * -------------------------------------------------------- 
 * User Agent 浏览器检测
 * --------------------------------------------------------
 */
ajaxjs.ua = (function() {
	var ua = navigator.userAgent.toLowerCase();
	if (/msie 8/.test(ua) || /msie 9/.test(ua))
		alert('ie 版本太低（要求 ie >= 10）或者你处于 360 的兼容模式下，请切换到极速模式 Not support this browser!');

	return {
		isWebkit : /webkit/.test(ua),
		isIOS : /ios/.test(ua),
		isAndroid : /android/.test(ua),
		isFirefox : /firefox/.test(ua)
	};
})();

/**
 * tppl.js 极致性能的 JS 模板引擎 Github：https://github.com/jojoin/tppl
 * 
 * @param tpl
 *            {String} 模板字符串
 * @param data
 *            {Object} 模板数据（不传或为null时返回渲染方法）
 * 
 * @return {String} 渲染结果
 * @return {Function} 渲染方法
 * 
 */
function tppl(tpl, data) {
	var fn = function(d) {
		var i, k = [], v = [];
		for (i in d) {
			k.push(i);
			v.push(d[i]);
		}
		;
		return (new Function(k, fn.$)).apply(d, v);
	};
	if (!fn.$) {
		var tpls = tpl.split('[:');
		fn.$ = "var $=''";
		for (var t = 0; t < tpls.length; t++) {
			var p = tpls[t].split(':]');
			if (t != 0) {
				fn.$ += '=' == p[0].charAt(0) ? "+(" + p[0].substr(1) + ")" : ";" + p[0].replace(/\r\n/g, '') + "$=$"
			}
			// 支持 <pre> 和 [::] 包裹的 js 代码
			fn.$ += "+'" + p[p.length - 1].replace(/\'/g, "\\'").replace(/\r\n/g, '\\n').replace(/\n/g, '\\n').replace(/\r/g, '\\n') + "'";
		}
		fn.$ += ";return $;";
		// log(fn.$);
	}
	return data ? fn(data) : fn;
}

// https://github.com/creationix/step
function Step() {
	var steps = Array.prototype.slice.call(arguments), pending, counter, results, lock;

	// Define the main callback that's given as `this` to the steps.
	function next() {
		counter = pending = 0;

		// Check if there are no steps left
		if (steps.length === 0) {
			// Throw uncaught errors
			if (arguments[0])
				throw arguments[0];
			
			return;
		}

		// Get the next step to execute
		var fn = steps.shift();
		results = [];

		// Run the step in a try..catch block so exceptions don't get out of
		// hand.
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
	next.parallel = function() {
		var index = 1 + counter++;
		pending++;

		return function() {
			pending--;
			
			if (arguments[0]) // Compress the error from any result to the first argument
				results[0] = arguments[0];
			
			results[index] = arguments[1];// Send the other results as arguments
			if (!lock && pending === 0) 
				next.apply(null, results);// When all parallel branches done, call the callback
			
		};
	};

	// Generates a callback generator for grouped results
	next.group = function() {
		var localCallback = next.parallel();
		var counter = 0;
		var pending = 0;
		var result = [];
		var error = undefined;

		function check() {
			if (pending === 0) 
				localCallback(error, result);// When group is done, call the callback
		}
		process.nextTick(check); // Ensures that check is called at least once

		// Generates a callback for the group
		return function() {
			var index = counter++;
			pending++;
			
			return function() {
				pending--;
				
				if (arguments[0]) // Compress the error from any result to the first argument
					error = arguments[0];
				
				result[index] = arguments[1];// Send the other results as arguments
				if (!lock) 
					check();
			};
		};
	};

	
	next();// Start the engine an pass nothing to the first step.
}

// Tack on leading and tailing steps for input and output and return the whole
// thing as a function. Basically turns step calls into function factories.
Step.fn = function StepFn() {
	var steps = Array.prototype.slice.call(arguments);
	return function() {
		var args = Array.prototype.slice.call(arguments);
		
		var toRun = [ function() {// Insert a first step that primes the data stream
			this.apply(null, args);
		} ].concat(steps);
		
		if (typeof args[args.length - 1] === 'function') // If the last arg is a function add it as a last step
			toRun.push(args.pop());

		Step.apply(null, toRun);
	}
}

// shorthand
Element.prototype.$ = function(cssSelector, fn) {
	if (typeof fn == 'function') {
		var children = this.querySelectorAll(cssSelector);

		if (children && fn)
			Array.prototype.forEach.call(children, fn);
		return children;
	} else {
		return this.querySelector.apply(this, arguments);
	}

}

/**
 * 删除自己
 */
Element.prototype.die = function() {
	this.parentNode.removeChild(this);
}

/**
 * 查找父元素，支持 标签名称 或 样式名称，任选其一而不能同时传。
 * 
 * @param tagName
 *            标签名称
 * @param className
 *            样式名称
 * @returns 父级元素，如果没有找到返回 null
 */
Element.prototype.up = function(tagName, className) {
	if (tagName && className)
		throw '只能任选一种参数，不能同时传';

	var el = this.parentNode;
	tagName = tagName && tagName.toUpperCase();

	while (el) {
		if (tagName && el.tagName == tagName)
			return el;
		if (className && el.className && ~el.className.indexOf(className))
			return el;
		el = el.parentNode;
	}

	return null;
}

/**
 * 在当前元素后面追加 newElement
 * 
 * @param newElement
 *            新的元素
 */
Element.prototype.insertAfter = function(newElement) {
	var targetElement = this, parent = targetElement.parentNode;

	if (parent.lastChild == targetElement)
		parent.insertBefore(newElement);
	else
		parent.insertBefore(newElement, targetElement.nextSibling);
}

ajaxjs.apply = function(a, b) {
	for ( var i in b)
		a[i] = b[i];
}

/*
 * -------------------------------------------------------- 获取浏览器 url 参数
 * --------------------------------------------------------
 */
ajaxjs.params = {
	/**
	 * 
	 */
	json2url : function(json, appendUrl) {
		var params = [];
		for ( var i in json)
			params.push(i + '=' + json[i]);

		params = params.join('&');

		if (appendUrl) // 如果有 ? 则追加，否则加入 ?
			params = ~appendUrl.indexOf('?') ? appendUrl + '&' + params : appendUrl + '?' + params;

		return params;
	},

	/**
	 * 读取 search 和 hash 的参数 location.params().hash['appinstall'];
	 * location.params().search['isappinstalled'];
	 */
	get : function(search, hash) {
		search = search || window.location.search;
		hash = hash || window.location.hash;

		var fn = function(str, reg) {
			if (str) {
				var data = {};
				str.replace(reg, function($0, $1, $2, $3) {
					data[$1] = $3;
				});

				return data;
			}
		};

		return {
			search : fn(search, new RegExp("([^?=&]+)(=([^&]*))?", "g")) || {},
			hash : fn(hash, new RegExp("([^#=&]+)(=([^&]*))?", "g")) || {}
		};
	}
};

if (!window.URLSearchParams) { // polyfill
	URLSearchParams = function() {

	}
}

/*
 * -------------------------------------------------------- 封装 XHR，支持
 * GET/POST/PUT/DELETE/JSONP/FormData
 * http://blog.csdn.net/zhangxin09/article/details/78879244
 * --------------------------------------------------------
 */
ajaxjs.xhr = {
	json2url : ajaxjs.params.json2url,

	// 注意 url 部分带有 # 的话则不能传参数过来
	request : function(url, cb, args, cfg, method) {
		var params = this.json2url(args), xhr = new XMLHttpRequest();

		method = method.toUpperCase();

		if (method == 'POST' || method == 'PUT') {
			xhr.open(method, url);
		} else
			xhr.open(method, url + (params ? '?' + params : ''));

		cb.url = url; // 保存 url 以便记录请求路径，可用于调试

		xhr.onreadystatechange = this.callback.delegate(null, cb, cfg && cfg.parseContentType);

		if (method == 'POST' || method == 'PUT') {
			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			xhr.send(params);
		} else {
			xhr.send(null);
		}
	},

	callback : function(event, cb, parseContentType) {
		if (this.readyState === 4 && this.status === 200) {
			var responseText = this.responseText.trim();

			try {
				if (!responseText)
					throw '服务端返回空的字符串!';

				var data = null;
				switch (parseContentType) {
				case 'text':
					data = this.responseText;
					break;
				case 'xml':
					data = this.responseXML;
					break;
				case 'json':
				default:
					data = JSON.parse(responseText);
				}
			} catch (e) {
				alert('AJAX 错误:\n' + e + '\nThe url is:' + cb.url); // 提示用户 异常
			}

			if (!cb)
				throw '你未提供回调函数';

			cb(data, this);
		}

		if (this.readyState === 4 && this.status == 500)
			alert('服务端 500 错误！');
	},

	/**
	 * e.g XMLHttpRequest.jsonp(
	 * 'http://u1.3gtv.net:2080/pms-service/section/content_list', { start:0,
	 * limit:10, sort:0, portalId:45, id:3290 }, function(json){
	 * console.log(json); } );
	 * 
	 * @param url
	 *            请求远程路径
	 * @param params
	 *            参数
	 * @param cb
	 *            回调函数
	 * @param cfg
	 *            该次请求的配置
	 */
	jsonp : function(url, params, cb, cfg) {
		var globalMethod_Token = 'globalMethod_' + parseInt(Math.random() * (200000 - 10000 + 1) + 10000);

		if (!window.$$_jsonp)
			window.$$_jsonp = {};
		// Map<String, Function>
		window.$$_jsonp[globalMethod_Token] = cb;

		params = params || {};
		params[cfg && cfg.callBackField || 'callBack'] = '$$_jsonp.' + globalMethod_Token;

		var scriptTag = document.createElement('script');
		scriptTag.src = this.json2url(params, url);
		document.getElementsByTagName('head')[0].appendChild(scriptTag);
	},

	form : function(form, cb, cfg) {
		if (!window.FormData) {
			var msg = 'The version of your browser is too old, please upgrade it.';
			throw msg;
		}

		cb = cb || ajaxjs.xhr.defaultCallBack;
		cfg = cfg || {};

		if (typeof form == 'string')
			form = document.querySelector(form);

		if (!form.action)
			throw 'Please fill the url in ACTION attribute.';

		// form.method always GET, so form.getAttribute('method') instead
		var method = form.getAttribute('method').toLowerCase();

		cfg.method = method || cfg.method || 'post';

		form.addEventListener('submit', function(e, cb, cfg) {
			e.preventDefault();// 禁止 form 默认提交
			var form = e.target;
			var json = ajaxjs.xhr.serializeForm(form, cfg);

			if (cfg && cfg.beforeSubmit && cfg.beforeSubmit(form, json) === false)
				return;

			if (cfg && cfg.method == 'put')
				ajaxjs.xhr.put(form.action, cb, json);
			else
				ajaxjs.xhr.post(form.action, cb, json);
		}.delegate(null, cb, cfg));
	}
};

// GET 请求
ajaxjs.xhr.get = ajaxjs.xhr.request.delegate(null, null, null, null, 'GET');
ajaxjs.xhr.post = ajaxjs.xhr.request.delegate(null, null, null, null, 'POST');
ajaxjs.xhr.put = ajaxjs.xhr.request.delegate(null, null, null, null, 'PUT');
ajaxjs.xhr.dele = ajaxjs.xhr.request.delegate(null, null, null, null, 'DELETE');

/**
 * 表单序列化，兼容旧浏览器和 H5 FormData，返回 JSON
 * 
 * @param {Element}
 *            form
 * @param {Object}
 *            cfg
 */
ajaxjs.xhr.serializeForm = function(form, cfg) {
	var json = {};

	if (window.FormData && FormData.prototype.forEach) { // 奇葩魅族浏览器，有
		// FormData 却只有
		// append 一个方法
		var formData = new FormData(form);
		formData.forEach(function(value, name) {
			if (cfg && cfg.ignoreField != name) // 忽略的字段
				json[name] = encodeURIComponent(value);
		});

	} else {
		for (var i = 0, len = form.elements.length; i < len; i++) {
			var formElement = form.elements[i], name = formElement.name, value = formElement.value;

			if (formElement.name === '' || formElement.disabled || (cfg && cfg.ignoreField != name))
				continue;

			switch (formElement.nodeName.toLowerCase()) {
			case 'input':
				switch (formElement.type) {
				case 'text':
				case 'hidden':
				case 'password':
					json[name] = encodeURIComponent(value);
					break;
				case 'checkbox':
				case 'radio':
					if (formElement.checked)
						json[name] = encodeURIComponent(value);
					break;
				}
				break;
			case 'textarea':
			case 'select':
				json[name] = encodeURIComponent(value);
				break;
			}
		}
	}

	return json;
}

// 默认的回调，有专属的字段并呼叫专属的控件
ajaxjs.xhr.defaultCallBack = function(json) {
	if (json) {
		if (json.isOk) {
			ajaxjs.alert(json.msg || '操作成功！');
		} else {
			ajaxjs.alert(json.msg || '执行失败！原因未知！');
		}
	} else {
		ajaxjs.alert('ServerSide Error!');
	}
}

// --------------------------------------------------------
// 拖放/触控 Drag&Drop
// 例子
// http://i.ifeng.com/ent/ylch/news?ch=ifengweb_2014&aid=91654101&mid=5e7Mzq&vt=5
// --------------------------------------------------------
ajaxjs.throttle = {
	event : {},
	handler : [],
	init : function(fn, eventType) {
		eventType = eventType || 'resize'; // resize|scroll

		this.handler.push(fn);

		if (!this.event[eventType])
			this.event[eventType] = true;

		(function() {
			var me = arguments.callee;

			window.addEventListener(eventType, function() {
				window.removeEventListener(eventType, arguments.callee); // 开始的触发事件的时候就只执行一次，之后的就让
				// setTimeout
				// 来接管，从而避免了多次调用

				window.setTimeout(function() {
					for (var i = 0, j = self.handler.length; i < j; i++) {
						var obj = self.handler[i];

						if (typeof obj == 'function') {
							obj();
						} else if (typeof obj.fn == 'function' && !obj.executeOnce) {
							obj.fn.call(obj);
							// obj.done = true;
						}
					}

					me(); // 回调 resize|scroll
					// 事件，过程：再登记、再撤销、再调用--》再登记、再撤销、再调用--》（如此反复）
				}, 300); // 300毫秒执行一次
			});
		})();

		// 先执行一次
		var obj = fn;
		if (typeof obj == 'function') {
			obj();
		} else if (typeof obj.fn == 'function' && !obj.done) {
			obj.fn.call(obj);
		}
	}
};

ajaxjs.throttle.onEl_in_viewport = function(el, fn) {
	setTimeout(function() { // 加入延时，让 dom pic 加载好，高度动态的
		UserEvent2.onWinResizeFree({
			executeOnce : false,
			fn : function() {
				var scrollTop = document.body.scrollTop, docHeight = window.innerHeight;

				var elTop = el.getBoundingClientRect().top, isFirstPage = /*
																			 * scrollTop ==
																			 * 0 &&
																			 */docHeight > elTop, isInPage = scrollTop > elTop;

				// console.log(isFirstPage || isInPage);

				if (isFirstPage || isInPage) {
					this.executeOnce = true;
					fn();
				}
			}
		}, 'scroll');
	}, 1500);
}

ajaxjs.throttle.onEl_in_viewport.actions = [];

// ;(function(){
// // 回到顶部
// var timer = null;
// var b = 0;//作为标志位，判断滚动事件的触发原因，是定时器触发还是其它人为操作
// UserEvent2.onWinResizeFree(function(e) {
// if (b != 1) clearInterval(timer);
// b = 2;
// }, 'scroll');
//	
// window.goTop = function () {
// clearInterval(timer);
// var iCur = speed = 0;
//		
// timer = setInterval(function() {
// iCur = document.documentElement.scrollTop || document.body.scrollTop;
// speed = Math.floor((0 - iCur) / 8);
//			
// if (iCur === 0)
// clearInterval(timer);
// else
// document.documentElement.scrollTop = document.body.scrollTop = iCur + speed;
// b = 1;
// }, 30);
// }
// })();
