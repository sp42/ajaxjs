/*
 * AJAXJS UI Base
 * Vue.js + LESS.js
 * Homepage: https://framework.ajaxjs.com/
 */

// 查找元素
ajaxjs = aj = function(cssSelector, fn) {
	return Element.prototype.$.apply(document, arguments);
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
 * @param tagName 标签名称
 * @param className 样式名称
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
 * @param newElement 新的元素
 */
Element.prototype.insertAfter = function(newElement) {
	var targetElement = this, parent = targetElement.parentNode;

	if (parent.lastChild == targetElement)
		parent.insertBefore(newElement);
	else
		parent.insertBefore(newElement, targetElement.nextSibling);
}

/*
 * @Dep
 * -------------------------------------------------------- 
 *  函数委托 参见 http://blog.csdn.net/zhangxin09/article/details/8508128 
 *  @return {Function}
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
}

/**
 * 设置一个后置函数。
 * 
 * @param {Function} composeFn
 * @param {Boolean} isForceCall 是否强行执行 call 方法。设置为 true 在数组作为单独对象的时候有用。
 * @return {Function}
 */
Function.prototype.after = function(composeFn, isForceCall, scope) {
    var self = this;

    return function() {
        var result = self.apply(scope || this, arguments);

        if (isForceCall) {
            return composeFn.call(this, result);
        }

        return result && (typeof result.pop != 'undefined')&& (typeof result.pop != 'unknown') ? composeFn.apply(this, result) : composeFn.call(this, result);
    };
}

aj.apply = function(a, b) {
	for ( var i in b)
		a[i] = b[i];
	
	return a;
}

/*
 * -------------------------------------------------------- 
 * 封装 XHR，支持
 * GET/POST/PUT/DELETE/JSONP/FormData
 * http://blog.csdn.net/zhangxin09/article/details/78879244
 * --------------------------------------------------------
 */
ajaxjs.xhr = {
	json2url(json, appendUrl) {
		var params = [];
		for ( var i in json)
			params.push(i + '=' + json[i]);

		params = params.join('&');

		if (appendUrl) // 如果有 ? 则追加，否则加入 ?
			params = ~appendUrl.indexOf('?') ? appendUrl + '&' + params : appendUrl + '?' + params;

		return params;
	},

	// 注意 url 部分带有 # 的话则不能传参数过来
	request(url, cb, args, cfg, method) {
		var params = this.json2url(args), xhr = new XMLHttpRequest();

		method = method.toUpperCase();

		if (method == 'POST' || method == 'PUT') {
			xhr.open(method, url);
		} else
			xhr.open(method, url + (params ? '?' + params : ''));

		cb.url = url; // 保存 url 以便记录请求路径，可用于调试
		xhr.onreadystatechange = this.callback.delegate(null, cb, cfg && cfg.parseContentType);
		xhr.setRequestHeader('Accept', 'application/json');
		
		if (method == 'POST' || method == 'PUT') {
			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			xhr.send(params);
		} else {
			xhr.send(null);
		}
	},

	callback(event, cb, parseContentType) {
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
					try{
						data = JSON.parse(responseText);						
					} catch(e) {
						try{
							data = eval("TEMP_VAR = " + responseText);  // for {ok: true}
						} catch(e) {
							throw e;
						}
					}
				}
			} catch (e) {
				alert('XHR 错误:\n' + e + '\nUrl is:' + cb.url); // 提示用户 异常
			}

			if (!cb)
				throw '你未提供回调函数';

			cb(data, this);
		}

		if (this.readyState === 4 && this.status == 500)
			alert('服务端 500 错误！');
	},

	form(form, cb, cfg) {
		cb = cb || ajaxjs.xhr.defaultCallBack;
		cfg = cfg || {};

		if(!form) return;
		if (typeof form == 'string')
			form = aj(form);

		if (!form.action)
			throw 'Please fill the url in ACTION attribute.';
		
		!cfg.noFormValid && aj.formValidator(form);

		// form.method always GET, so form.getAttribute('method') instead
		var method;
		if(form.getAttribute('method'))
			method = form.getAttribute('method').toLowerCase();
		
		cfg.method = method || cfg.method || 'post';

		form.addEventListener('submit', function(e, cb, cfg) {
			e.preventDefault();// 禁止 form 默认提交
			var form = e.target;
			
			if(!cfg.noFormValid && !aj.formValidator.onSubmit(form))
				return;
			
			var json = ajaxjs.xhr.serializeForm(form, cfg);

			if (cfg && cfg.beforeSubmit && cfg.beforeSubmit(form, json) === false)
				return;

			if (cfg && cfg.method == 'put')
				ajaxjs.xhr.put(form.action, cb, json);
			else
				ajaxjs.xhr.post(form.action, cb, json);
		}.delegate(null, cb, cfg));
		
		var returnBtn = form.$('button.returnBtn'); // shorthand for back btn
		if (returnBtn)
			returnBtn.onclick = e => {
				e.preventDefault();
				history.back();
			};
	}
};

ajaxjs.xhr.get = (url, cb, args, cfg) => {
	ajaxjs.xhr.request(url, cb, args, cfg, 'GET');
}
ajaxjs.xhr.post = (url, cb, args, cfg) => {
	ajaxjs.xhr.request(url, cb, args, cfg, 'POST');
}
ajaxjs.xhr.put = (url, cb, args, cfg) => {
	ajaxjs.xhr.request(url, cb, args, cfg, 'PUT');
}
ajaxjs.xhr.dele = (url, cb, args, cfg) => {
	ajaxjs.xhr.request(url, cb, args, cfg, 'DELETE');
}

/**
 * 表单序列化，兼容旧浏览器和 H5 FormData，返回 JSON
 * 
 * @param {Element} form
 * @param {Object}  cfg
 */
ajaxjs.xhr.serializeForm = function(form, cfg) {
	var json = {}, formData = new FormData(form);
	formData.forEach((value, name) => {
		if (cfg && cfg.ignoreField != name) // 忽略的字段
			json[name] = encodeURIComponent(value);
	});
	
	return json;
}

// 默认的回调，有专属的字段并呼叫专属的控件
ajaxjs.xhr.defaultCallBack_cb = function(json, xhr, onOK, onFail) {
	if (json) {
		if (json.isOk) {
			!!onOK && onOK(json);
			aj.msg.show(json.msg || '操作成功！');
		} else {
			!!onFail && onFail(json);
			aj.msg.show(json.msg || '执行失败！原因未知！');
		}
	} else {
		onFail && onFail(json);
		aj.msg.show('ServerSide Error!');
	}
}

ajaxjs.xhr.defaultCallBack = ajaxjs.xhr.defaultCallBack_cb.delegate(null);

// --------------------------------------------------------
// 拖放/触控 Drag&Drop
// 例子
// http://i.ifeng.com/ent/ylch/news?ch=ifengweb_2014&aid=91654101&mid=5e7Mzq&vt=5
// --------------------------------------------------------
ajaxjs.throttle = {
	event: {},
	handler: [],
	init (fn, eventType) {
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
				var args = arguments;
				window.setTimeout(function() {
					for (var i = 0, j = ajaxjs.throttle.handler.length; i < j; i++) {
						var obj = ajaxjs.throttle.handler[i];

						if (typeof obj == 'function') {
							obj.apply(this, args);
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

//函数节流 https://www.cnblogs.com/moqiutao/p/6875955.html
aj.throttleV2 = function(fn, delay, mustRunDelay) {
 	var timer = null;
 	var t_start;
 	
 	return function() {
 		var t_curr = +new Date();
 		window.clearTimeout(timer);
 		
 		if(!t_start) 
 			t_start = t_curr;
 		
 		if(t_curr - t_start >= mustRunDelay) {
 			fn.apply(this, arguments);
 			t_start = t_curr;
 		} else {
 			var args = arguments;
 			timer = window.setTimeout(() => {
 				fn.apply(this, args);
 			}, delay);
 		}
 	};
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

//并行和串行任务 https://segmentfault.com/a/1190000013265925
aj.parallel = function(arr, finnaly) {
  let fn, index = 0;
  let statusArr = Array(arr.length).fill().map(() => ({
      isActive: false,
      data: null
   }));
  
  let isFinished = function() {
    return statusArr.every(item => {
      return item.isActive === true;
    });
  };
  
  let resolve = function(index) {
    return function(data) {
      statusArr[index].data = data;
      statusArr[index].isActive = true;
      let isFinish = isFinished();
      
      if (isFinish) {
        let datas = statusArr.map(item => {
          return item.data;
        });
        
        finnaly(datas);
      }
    };
  };
  
  while ((fn = arr.shift())) {
    // 给resolve函数追加参数,可以使用bind函数实现,这里使用了柯里化
    fn(resolve(index));
    index++;
  }
};

// https://github.com/jojoin/tppl/blob/gh-pages/tppl.js
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
 * 消息框、弹窗、对话框组件
 */
document.addEventListener("DOMContentLoaded", () => {
	document.body.appendChild(document.createElement('div')).className = 'alertHolder';
	
	// 全屏幕弹窗，居中显示文字。
	// 不应直接使用该组件，而是执行 aj.showOk
	aj.msgbox = new Vue({
		el : '.alertHolder',
		data : {
			showText : '', 		// 显示的内容
			afterClose : null,	// 关闭弹窗后的回调
			showOk : false,
			showYes : false,
			showNo : false,
			showSave : false
		},
		template : 
			'<div class="aj-modal hide" @click="close($event);">\
				<div><div v-html="showText"></div>\
					<div class="aj-btnsHolder">\
						<button v-show="showOk"  @click="onBtnClk($event)" class="ok">确定</button> \
						<button v-show="showYes" @click="onBtnClk($event)" class="yes">{{showSave? \'保存\': \'是\'}}</button> \
						<button v-show="showNo"  @click="onBtnClk($event)" class="no">{{showSave? \'否\': \'否\'}}</button>\
					</div>\
				</div>\
			</div>',
		methods : {
			show(text, cfg) {
				this.showText = text;
				this.$el.classList.remove('hide');
				aj.apply(this, cfg);
				
				return this;
			},
			close(e) {
				var div = e.target; // check if in the box
				if (div && div.className.indexOf('modal') != -1) {
					this.$el.classList.add('hide');
					
					this.afterClose && this.afterClose(div, this);
				}
			},
			onBtnClk(e) {
				var el = e.target;
				switch(el.className) {
					case 'ok':
						this.onOkClk && this.onOkClk(e, this);
					break;
					case 'no':
						this.onNoClk && this.onNoClk(e, this);
						break;
					case 'yes':
						this.onYesClk && this.onYesClk(e, this);
						break;
				}
			},
			
		}
		
	});
	
	/**
	 * 顯示確定的對話框
	 * @param {String} text 显示的文本
	 * @param {Function} callback 回调函数
	 */
	aj.showOk = (text, callback) => {
		var alertObj = aj.msgbox.show(text, {
			showYes : false,
			showNo : false,
			showOk : true,
			onOkClk(e) { // 在box里面触发关闭，不能直接用 msgbox.close(e);
				alertObj.$el.classList.add('hide');
				callback && callback();
			}
		});
	}
	
	/**
	 * 顯示“是否”選擇的對話框
	 * @param {String} text 显示的文本
	 * @param {Function} callback 回调函数
	 */
	aj.showConfirm = (text, callback, showSave) => {
		var alertObj = aj.msgbox.show(text, {
			showYes : true,
			showNo : true,
			showOk : false,
			showSave: showSave,
			onYesClk(e) {
				alertObj.$el.classList.add('hide');
				callback && callback(alertObj.$el, e);
			},
			onNoClk(e) { // 在box里面触发关闭，不能直接用 msgbox.close(e);
				alertObj.$el.classList.add('hide');
			}
		});
	}
	
	aj.simpleOk = (text, callback) => {
		var alertObj = aj.msgbox.show(text, {
			showYes : false,
			showNo : false,
			showOk : false,
			onOkClk(e) { // 在box里面触发关闭，不能直接用 msgbox.close(e);
				alertObj.$el.classList.add('hide');
				callback && callback();
			}
		});
	}
	
	aj.alert = aj.showOk;
	aj.alert.show = aj.simpleOk;

	document.body.appendChild(document.createElement('div')).className = 'msgHolder';

	// 顶部出现，用于后台提示信息多
	aj.msg = new Vue({
		el : '.msgHolder',
		data : {
			showText : '' // 显示的内容
		},
		template : '<div class="aj-topMsg" v-html="showText"></div>',
		methods : {
			show (text, cfg) {
				this.showText = text;
				var el = this.$el;
				
				setTimeout(()=> {
					el.classList.remove('fadeOut');
					el.classList.add('fadeIn');
				}, 0);
				
				setTimeout(() => { // 自动隐藏，无须 close
					el.classList.remove('fadeIn');
					el.classList.add('fadeOut');
					cfg && cfg.afterClose && cfg.afterClose(div, this);
				}, cfg && cfg.showTime || 3000);
			}
		}
	});
});

// 浮層組件，通常要復用這個組件
Vue.component('aj-layer', {
	template: '<div class="aj-modal hide" @click="close($event);"><div><slot></slot></div></div>',
	props: {
		// 默认点击窗体关闭，当 notCloseWhenTap = true 时禁止关闭
		notCloseWhenTap: Boolean
	},
	methods: {
		show (cfg) {
			this.$el.classList.remove('hide');
//			this.BUS.emit('aj-layer-closed', this);
			if(cfg && cfg.afterClose)
				this.afterClose = cfg.afterClose;
		},
		close(e) { // isForceClose = 强制关闭
			if(!e) {
				aj.msgbox.$options.methods.close.call(this, {
					target : aj('.aj-modal')
				});
			}else{
				if(e.isForceClose || !this.notCloseWhenTap)
					aj.msgbox.$options.methods.close.apply(this, arguments);
			}
		}
	}
});

