// base goes here...
ajaxjs = {}; // Setup a top namespace

	/*
	 * -------------------------------------------------------- 
	 * 函数委托 参见
	 * http://blog.csdn.net/zhangxin09/article/details/8508128 
	 * @return {Function}
	 * --------------------------------------------------------
	 */
	Function.prototype.delegate = function() {
		var self = this, scope = this.scope, args = arguments, aLength = arguments.length, fnToken = 'function';
	
		return function() {
			var bLength = arguments.length, Length = (aLength > bLength) ? aLength
					: bLength;
	
			// mission one:
			for (var i = 0; i < Length; i++)
				if (arguments[i])
					args[i] = arguments[i]; // 拷贝参数
	
			args.length = Length; // 在 MS
									// jscript下面，arguments作为数字来使用还是有问题，就是length不能自动更新。修正如左:
	
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
	
			return result && (typeof result.pop != 'undefined')
					&& (typeof result.pop != 'unknown') ? composeFn.apply(this,
					result) : composeFn.call(this, result);
		};
	}

/*
 * -------------------------------------------------------- 
 * String.format? Not
 * necessary, use this instead
 * --------------------------------------------------------
 */
/*
 * var html = '', tpl = '<li><img src="../../../images/{0}" /></li>';
 * for(var i = 0, j = arr.length; i < j; i++) html += tpl.replace('{0}',
 * arr[i]);
 */
// For more {1}/{2}...,just repeat replace

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
	 * tppl.js 极致性能的 JS 模板引擎
	 * Github：https://github.com/jojoin/tppl
	 * 作者：杨捷  
	 * 邮箱：yangjie@jojoin.com
	 *
	 * @param tpl {String}    模板字符串
	 * @param data {Object}   模板数据（不传或为null时返回渲染方法）
	 *
	 * @return  {String}    渲染结果
	 * @return  {Function}  渲染方法
	 *
	 */
	tppl = function(tpl, data){
	    var fn =  function(d) {
	        var i, k = [], v = [];
	        for (i in d) {
	            k.push(i);
	            v.push(d[i]);
	        };
	        return (new Function(k, fn.$)).apply(d, v);
	    };
	    if(!fn.$){
	        var tpls = tpl.split('[:');
	        fn.$ = "var $=''";
	        for(var t = 0;t < tpls.length;t++){
	            var p = tpls[t].split(':]');
	            if(t!=0){
	                fn.$ += '='==p[0].charAt(0)
	                  ? "+("+p[0].substr(1)+")"
	                  : ";"+p[0].replace(/\r\n/g, '')+"$=$"
	            }
	            // 支持 <pre> 和 [::] 包裹的 js 代码
	            fn.$ += "+'"+p[p.length-1].replace(/\'/g,"\\'").replace(/\r\n/g, '\\n').replace(/\n/g, '\\n').replace(/\r/g, '\\n')+"'";
	        }
	        fn.$ += ";return $;";
	        // log(fn.$);
	    }
	    return data ? fn(data) : fn;
	}

/**
 * 使用 Proxy 来说实现被废弃的 Object.observe()
 * 
 * @param {any} target 
 * @param {any} fnBind 
 * @returns 
 */
var bind = function (target, fnBind) {
	bind.targets = bind.targets || [];
	var targets = bind.targets, index = targets.indexOf(target);

	bind.fnBinds = bind.fnBinds || [];
	var fnBinds = bind.fnBinds;
	if (index == -1) {
		index = targets.length;
		targets.push(target);
		fnBinds.push([]);
	}
	var targetFnBinds = fnBinds[index];
	targetFnBinds.push(fnBind);

	bind.proxy = bind.proxy || new Proxy(target, {
		set : function(target, prop, value) {
			target[prop] = value;
			for (var i = 0; i < targetFnBinds.length; i++) {
				targetFnBinds[i].call(target);
			}
		}
	});
	
	return bind.proxy;
}

/*
 * -------------------------------------------------------- Query Selector
 * --------------------------------------------------------
 */
window.qs = function(cssSelector) {
	var el = document.querySelector(cssSelector);
	return el || {}; // 避免 null point
}

Element.prototype.qs = function(cssSelector) {
	var el = this.querySelector(cssSelector);
	return el || {};
}

/**
 * 遍历每个子元素
 * 
 * @param cssSelector
 *            CSS 选择符
 * @param fn
 *            函数，可选
 * @returns 返回子元素集合
 */
Element.prototype.every_child = Element.prototype.eachChild = function(cssSelector, fn) {
	var children = this.querySelectorAll(cssSelector);

	if (children && fn)
		Array.prototype.forEach.call(children, fn);
	// for(var i = 0, j = children.length; i < j; i++)
	// fn(children[i], children, i, j);

	return children;
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

/*
 * -------------------------------------------------------- Element Class Utils
 * For css class helper, use:
 * https://developer.mozilla.org/en-US/docs/Web/API/Element/classList eg:
 * el.classList.add/remove/toggl
 * --------------------------------------------------------
 */

/*
 * -------------------------------------------------------- 
 * 获取浏览器 url 参数
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
			params = ~appendUrl.indexOf('?') ? appendUrl + '&' + params
					: appendUrl + '?' + params;

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

	/*
	 * -------------------------------------------------------- 
	 * 封装 XHR，支持 GET/POST/PUT/DELETE/JSONP/FormData
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
			
			if(this.readyState === 4 && this.status == 500)
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
			var globalMethod_Token = 'globalMethod_'
					+ parseInt(Math.random() * (200000 - 10000 + 1) + 10000);
	
			if (!window.$$_jsonp)
				window.$$_jsonp = {};
			// Map<String, Function>
			window.$$_jsonp[globalMethod_Token] = cb;
	
			params = params || {};
			params[cfg && cfg.callBackField || 'callBack'] = '$$_jsonp.'+ globalMethod_Token;
	
			var scriptTag = document.createElement('script');
			scriptTag.src = this.json2url(params, url);
			document.getElementsByTagName('head')[0].appendChild(scriptTag);
		},
	
		form : function(form, cb, cfg) {
			if (!window.FormData) {
				var msg = 'The version of your browser is too old, please upgrade it.';
				throw msg;
			}
			cb = cb || function(json){alert(json.msg);}
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
				var json = {};
				var formData = new FormData(form);
				formData.forEach(function(value, key){
					if(cfg && cfg.ignoreField != key) // 忽略的字段
						json[key] = value;
				});
				
				if (cfg && cfg.beforeSubmit && cfg.beforeSubmit(form, json) === false) 
					return;
				
				if(cfg && cfg.method == 'put')
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

/*
 * -------------------------------------------------------- 
 * 模版 Template, thanks
 * to https://github.com/jojoin/tppl
 * --------------------------------------------------------
 */
ajaxjs.tppl = function(tpl, data) {
	var fn = function(d) {
		var i, k = [], v = [];
		for (i in d) {
			k.push(i);
			v.push(d[i]);
		}

		try {

			return (new Function(k, fn.$)).apply(d, v);
		} catch (e) {
			console.log(e);
		}
	}

	if (!fn.$) {
		var tpls = tpl.split('[:');
		fn.$ = "var $=''";

		for (var t = 0; t < tpls.length; t++) {
			var p = tpls[t].split(':]');

			if (t != 0) {
				fn.$ += '=' == p[0].charAt(0) ? "+(" + p[0].substr(1) + ")"
						: ";" + p[0].replace(/\r\n/g, '') + "$=$"
			}
			// 支持 <pre> 和 [::] 包裹的 js 代码
			fn.$ += "+'"
					+ p[p.length - 1].replace(/\'/g, "\\'").replace(/\r\n/g,
							'\\n').replace(/\n/g, '\\n').replace(/\r/g, '\\n')
					+ "'";
		}
		fn.$ += ";return $;";
		// log(fn.$);
	}
	return data ? fn(data) : fn;
}

/*
 * -------------------------------------------------------- 
 * 观察者模式
 * --------------------------------------------------------
 */
ajaxjs.UserEvent = function() {
	var events = {};

	this.addEvents = function() {
		for (var i = 0, j = arguments.length; i < j; i++)
			events[arguments[i].toLowerCase()] = [];
	}

	/**
	 * 添加一个事件侦听器。
	 * 
	 * @param {String}
	 *            name
	 * @param {Function}
	 *            fn
	 * @return {this}
	 */
	this.addListener = this.on = function(name, eventHandler) {
		var eventQueen = events[name.toLowerCase()];
		if (!eventQueen)
			throw '没有该事件！请使用addEvent()增加事件';

		eventQueen.push(eventHandler);
		return this;
	}

	/**
	 * 触发事件。
	 * 
	 * @param {String}
	 *            name
	 * @param {Array}
	 *            args
	 * @return {Boolean}
	 */
	this.fireEvent = function(name) {
		var eventQueen = events[name.toLowerCase()]; // listeners
		if (!eventQueen)
			throw 'No such event:' + name;
		var args = eventQueen.length
				&& Array.prototype.slice.call(arguments, 1);

		var result;
		var output = [];

		for (var i = 0, j = eventQueen.length; i < j; i++) {
			result = eventQueen[i].apply(this, args);

			if (result === false) {
				return false;
			} else {
				output.push(result);
			}
		}

		return output;
	}

	/**
	 * 移除事件侦听器。须传入原来的函数句柄。
	 * 
	 * @param {String}
	 *            name
	 * @param {Function}
	 *            fn
	 * @return {this}
	 */
	this.removeListener = function(name, fn) {
		if (events[name])
			Array_Remove(events[name], fn);

		return this;
	}

	/**
	 * 删掉某个元素。
	 * 
	 * @param {Array}
	 *            arr
	 * @param {Mixed}
	 *            el
	 * @return {Mixed}
	 */
	function Array_Remove(arr, el) {
		var index = -1;

		for (var i = 0, j = arr.length; i < j; i++) {
			if (arr[i] == el) {
				index = i;
				break;
			}
		}

		arr.splice(index, 1);
		return el;
	}
}

/**
 * 导入 js 文件，有缓存和回调功能
 * 
 * @param {String}
 *            src js 地址
 * @param {Function}
 *            fn 回调函数
 */
ajaxjs.loadScript = function(src, fn) {
	if (!ajaxjs.loadScript.loaded[src]) {
		var script = document.createElement('script');
		script.src = src;
		script.onload = fn;
		document.body.appendChild(script);

		ajaxjs.loadScript.loaded[src] = true;
	} else {
		fn();
	}
}

ajaxjs.loadScript.loaded = {};

ajaxjs.validatorReg = {
		require : /.+/,
		username : /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/,
		email : /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/,
		url : /^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*$/,
		integer : /^[-\+]?\d+$/,
		double : /^[-\+]?\d+(\.\d+)?$/,
		english : /^[A-Za-z]+$/,
		chinese : /^[\u0391-\uFFE5]+$/
	}