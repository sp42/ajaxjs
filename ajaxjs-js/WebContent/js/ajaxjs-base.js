/*
 * AJAXJS UI Base
 * Vue.js + LESS.js
 * Home page: https://framework.ajaxjs.com/
 */

/**
 * 查找元素
 * 
 * @param String CSS 选择器
 * @param Function  可选，当送入该参数的时候，表示使用 querySelectorAll 来查询多个 dom 元素，故 fn 是个遍历器函数，其参数列表如 item、index、array
 */
aj = ajaxjs = function(cssSelector, fn) {
	return Element.prototype.$.apply(document, arguments);
}

// shorthand
Element.prototype.$ = function(cssSelector, fn) {
	if (typeof fn == 'function') {
		var children = this.querySelectorAll(cssSelector);

		if (children && fn)
			Array.prototype.forEach.call(children, fn);
			
		return children;
	} else 
		return this.querySelector.apply(this, arguments);
}

/**
 * 查找父元素，支持 标签名称 或 样式名称，任选其一而不能同时传。
 * 
 * @param tagName	标签名称
 * @param className	样式名称
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
 * 复制 b 对象到 a 对象身上
 * 
 * @param {Object} 目标对象
 * @param {Object} 源对象
 */
aj.apply = function(a, b, c) {
	for ( var i in b)
		a[i] = b[i];
	
	return c ? aj.apply(a, c) : a;
}

// 加载脚本
aj.loadScript = (url, id) => {
	var script = document.createElement("script");
	script.src = url;
	
	if(id) 
		script.id = id;
	
	document.getElementsByTagName("head")[0].appendChild(script);	
}

/**
 * 函数委托 参见 http://blog.csdn.net/zhangxin09/article/details/8508128
 *
 * @return {Function}
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

/*
 * -------------------------------------------------------- 
 * 封装 XHR，支持
 * GET/POST/PUT/DELETE/JSONP/FormData
 * http://blog.csdn.net/zhangxin09/article/details/78879244
 * --------------------------------------------------------
 */
aj.xhr = {
	/**
	 * JSON 转换为 URL
	 * 
	 * @param {Object} JSON
	 * @param {String}  附加的地址
	 */
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

		if (method == 'POST' || method == 'PUT') 
			xhr.open(method, url);
		else
			xhr.open(method, url + (params ? '?' + params : ''));

		cb.url = url; // 保存 url 以便记录请求路径，可用于调试
		xhr.onreadystatechange = this.callback.delegate(null, cb, cfg && cfg.parseContentType);
		xhr.setRequestHeader('Accept', 'application/json');
		
		if (method == 'POST' || method == 'PUT') {
			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			xhr.send(params);
		} else 
			xhr.send(null);
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
		cb = cb || aj.xhr.defaultCallBack;
		cfg = cfg || {};

		if(!form) return;
		if (typeof form == 'string')
			form = aj(form);

		if (!form.action)
			throw 'Please fill the url in ACTION attribute.';
		
		!cfg.noFormValid && aj.formValidator(form);

		if(cfg.googleReCAPTCHA) {// 加载脚本
			var script = aj("#googleReCAPTCHA");
			
			if(!script) {
				var src = 'https://www.recaptcha.net/recaptcha/api.js?render=';
				src += 	cfg.googleReCAPTCHA;		
				aj.loadScript(src, 'googleReCAPTCHA');
			}
		}

		form.addEventListener('submit', function(e, cb, cfg) {
			e.preventDefault();// 禁止 form 默认提交
			var form = e.target;

			// form.method always GET, so form.getAttribute('method') instead
			var method;
			if(form.getAttribute('method'))
				method = form.getAttribute('method').toLowerCase();
			
			cfg.method = method || cfg.method || 'post';
			
			if(!cfg.noFormValid && !aj.formValidator.onSubmit(form))
				return;
			
			var json = aj.xhr.serializeForm(form, cfg);

			if (cfg && cfg.beforeSubmit && cfg.beforeSubmit(form, json) === false)
				return;
				
			if(cfg.googleReCAPTCHA) {
				grecaptcha.ready(() => {
		        	grecaptcha.execute(cfg.googleReCAPTCHA, {action: 'submit'}).then(token => {
			        	// Add your logic to submit to your backend server here.
						json.grecaptchaToken = token;

						if (cfg.method == 'put')
							aj.xhr.put(form.action, cb, json);
						else
							aj.xhr.post(form.action, cb, json);
		        	});
		        });
			} else {
				if (cfg && cfg.method == 'put')
					aj.xhr.put(form.action, cb, json);
				else
					aj.xhr.post(form.action, cb, json);
			}

		}.delegate(null, cb, cfg));
		
		var returnBtn = form.$('button.returnBtn'); // shorthand for back btn
		
		if (returnBtn)
			returnBtn.onclick = e => {
				e.preventDefault();
				history.back();
			}
	},
	
	/**
	 * 表单序列化，兼容旧浏览器和 H5 FormData，返回 JSON
	 * 
	 * @param {Element} form
	 * @param {Object}  cfg
	 */
	serializeForm(form, cfg) {
		var json = {}, formData = new FormData(form);
		formData.forEach((value, name) => {
			if (cfg && cfg.ignoreField != name) // 忽略的字段
				json[name] = encodeURIComponent(value);
		});
		
		return json;
	}
};

aj.xhr.get 	= (url, cb, args, cfg) => aj.xhr.request(url, cb, args, cfg, 'GET');
aj.xhr.post = (url, cb, args, cfg) => aj.xhr.request(url, cb, args, cfg, 'POST');
aj.xhr.put 	= (url, cb, args, cfg) => aj.xhr.request(url, cb, args, cfg, 'PUT');
aj.xhr.dele = (url, cb, args, cfg) => aj.xhr.request(url, cb, args, cfg, 'DELETE');

// 默认的回调，有专属的字段并呼叫专属的控件
aj.xhr.defaultCallBack_cb = function(j, xhr, onOK, onFail) {
	if (j) {
		if (j.isOk) {
			!!onOK && onOK(j);
			aj.msg.show(j.msg || '操作成功！');
		} else {
			!!onFail && onFail(j);
			aj.msg.show(j.msg || '执行失败！原因未知！');
		}
	} else {
		onFail && onFail(j);
		aj.msg.show('ServerSide Error!');
	}
}

aj.xhr.defaultCallBack = aj.xhr.defaultCallBack_cb.delegate(null);

// 加载 Vue 组件的 template
// 在 vue 组件中的 beforeCreate() 执行
aj.getTemplate = (type, id, comp) => {
	var el = aj('#vue-tag-' + type);
	
	if(!el) { // 加载模板
		var xhr = new XMLHttpRequest();
		xhr.open("GET", '/ajaxjs-js/html/' + type + '.html', false);// 同步方式请求
		xhr.send(null);
		
		var div = document.createElement('div');
		div.id = 'vue-tag-' + type;
		div.innerHTML = xhr.responseText;
		document.body.appendChild(div);
	}

	comp.$options.template = document.getElementById(id).innerHTML;
}

/**
 * 消息框、弹窗、对话框组件
 */
document.addEventListener("DOMContentLoaded", () => {
	document.body.appendChild(document.createElement('div')).className = 'alertHolder';
	
	// 全屏幕弹窗，居中显示文字。
	// 不应直接使用该组件，而是执行 aj.showOk
	aj.msgbox = new Vue({
		el: '.alertHolder',
		data: {
			showText: '', 		// 显示的内容
			afterClose: null,	// 关闭弹窗后的回调
			showOk: false,
			showYes: false,
			showNo: false,
			showSave: false
		},
		template: 
			'<div class="aj-modal hide" @click="close">\
				<div><div v-html="showText"></div>\
					<div class="aj-btnsHolder">\
						<button v-show="showOk"  @click="onBtnClk" class="ok">确定</button> \
						<button v-show="showYes" @click="onBtnClk" class="yes">{{showSave? \'保存\': \'是\'}}</button> \
						<button v-show="showNo"  @click="onBtnClk" class="no">{{showSave? \'否\': \'否\'}}</button>\
					</div>\
				</div>\
			</div>',
		methods: {
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
			}
		}
	});
	
	/**
	 * 顯示確定的對話框
	 * 
	 * @param {String} text 显示的文本
	 * @param {Function} callback 回调函数
	 */
	aj.showOk = (text, callback) => {
		var alertObj = aj.msgbox.show(text, {
			showYes: false,
			showNo: false,
			showOk: true,
			onOkClk(e) { // 在 box 里面触发关闭，不能直接用 msgbox.close(e);
				alertObj.$el.classList.add('hide');
				callback && callback();
			}
		});
	}
	
	/**
	 * 顯示“是否”選擇的對話框
	 * 
	 * @param {String} text 显示的文本
	 * @param {Function} callback 回调函数
	 */
	aj.showConfirm = (text, callback, showSave) => {
		var alertObj = aj.msgbox.show(text, {
			showYes: true,
			showNo: true,
			showOk: false,
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
			showYes: false,
			showNo: false,
			showOk: false,
			onOkClk(e) { // 在box里面触发关闭，不能直接用 msgbox.close(e);
				alertObj.$el.classList.add('hide');
				callback && callback();
			}
		});
	}
	
	aj.alert = aj.showOk;
	aj.alert.show = aj.simpleOk;

	// 顶部出现，用于后台提示信息多
	var msgEl = document.createElement('div');
	msgEl.className = 'aj-topMsg';
	msgEl.setAttribute('v-html', "showText");
	document.body.appendChild(msgEl);

	aj.msg = new Vue({
		el: msgEl,
		data: {
			showText: '' // 显示的内容
		},
		methods: {
			show(text, cfg) {
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
	template: '<div class="aj-modal hide" @click="close"><div><slot></slot></div></div>',
	props: {
		notCloseWhenTap: Boolean // 默认点击窗体关闭，当 notCloseWhenTap = true 时禁止关闭
	},
	methods: {
		show(cfg) {
			this.$el.classList.remove('hide');
			this.BUS.emit('aj-layer-closed', this);
			
			if(cfg && cfg.afterClose)
				this.afterClose = cfg.afterClose;
		},
		close(e) { // isForceClose = 强制关闭
			if(!e) {
				aj.msgbox.$options.methods.close.call(this, {
					target: aj('.aj-modal')
				});
			}else{
				if(e.isForceClose || !this.notCloseWhenTap)
					aj.msgbox.$options.methods.close.apply(this, arguments);
			}
		}
	}
});


aj.tabable = (()=>{	
	// 按次序选中目标
	var select = (_new) => {
		var oldSelected = _new.parentNode.$('.selected');
		
		if(_new === oldSelected) // 没变化
			return;
			
		oldSelected && oldSelected.classList.remove('selected');
		_new.classList.add('selected');
	}
	
	return {
		mounted() {
			var ul = this.$el.$('.aj-simple-tab-horizontal > ul');
			ul.onclick = e => {
				var el = e.target;
				select(el);
		
				var index = Array.prototype.indexOf.call(el.parentElement.children, el);
				var _new = this.$el.$('.aj-simple-tab-horizontal > div').children[index];
				select(_new);
			};
			
			ul.onclick({target: ul.children[0]});
			//this.$options.watch.selected.call(this, 0);
		}
	};
})();

// https://vuejs.org/v2/guide/components.html#Content-Distribution-with-Slots
Vue.component('aj-tab', {
   	template: 
   		'<div :class="isVertical ? \'aj-simple-tab-vertical\' : \'aj-tab\' ">\
	      <button v-for="tab in tabs" v-bind:key="tab.name"\
	        v-bind:class="[\'tab-button\', { active: currentTab.name === tab.name }]"\
	        v-on:click="currentTab = tab">{{tab.name}}\
	      </button>\
	      <component v-bind:is="currentTab.component" class="tab"></component>\
	    </div>',
	props: {
		isVertical: Boolean // 是否垂直方向的布局，默认 false,
	},
    data() {
    	return {
          tabs: [],
          currentTab: {}
        };
    },
    mounted() {
		var arr = this.$slots.default;
		
		for(var i = 0; i < arr.length; i++) {
			var el = arr[i];
			
			if(el.tag === 'textarea') {
				this.tabs.push({
					name: el.data.attrs['data-title'],
					component: {
		 	            template: '<div>' + el.children[0].text + "</div>"
		   	 	    }
	    		});
	    	}
    	}

    	this.currentTab = this.tabs[0];
    }
});

/**
 * 页面常见组件
 */
aj.widget = {};

// 回到顶部  <a href="###" @click="go">回到顶部</a>
aj.widget.back2top = () => {
	var timerId = aj.widget.back2top.timerId;
	var top = speed = 0;
	
	timerId && window.clearInterval(timerId);
	
	aj.back2top.timerId = window.setInterval(() => {
		top = document.documentElement.scrollTop || document.body.scrollTop;
		speed = Math.floor((0 - top) / 8);
	
		if (top === 0)
			clearInterval(aj.widget.back2top.timerId);
		else
			document.documentElement.scrollTop = document.body.scrollTop = top + speed;
	}, 30);
}

// 悬浮显示大图。工厂方法
aj.widget.imageEnlarger = function() {
	// 单例
	aj.widget.imageEnlarger.singleInstance = new Vue({
		el: document.body.appendChild(document.createElement('div')),
		template: 
			'<div class="aj-image-large-view"><div><img :src="imgUrl" /></div></div>',
		data: {
			imgUrl: null
		},
		mounted() {// 不能用 onmousemove 直接绑定事件
			document.addEventListener('mousemove', aj.throttle(e => {
				if(this.imgUrl) {
					var el = this.$el.$('div');
					var w = 0, imgWidth = this.$el.$('img').clientWidth;
					
					if(imgWidth > e.pageX) 
						w = imgWidth;
				
					el.style.top = (e.pageY + 20) + 'px';
					el.style.left = (e.pageX - el.clientWidth + w) + 'px';
				}
			}, 50, 5000), false);
		}
	});
	
	return aj.widget.imageEnlarger.singleInstance;
}

/*
 正体中文 
	<span>
		 <a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>
		/<a href="javascript:;" onclick="toChinese(this); class="Chinese"">正体中文</a>
	</span>
*/

// 折叠菜单
Vue.component('aj-accordion-menu', {
	template: '<ul class="aj-accordion-menu" @click="onClk"><slot></slot></ul>',
	methods: {
		onClk($event) {
			this.children = this.$el.children;
			this.highlightSubItem($event);
	        
			var _btn = $event.target;
	        if (_btn.tagName == 'H3' && _btn.parentNode.tagName == 'LI') {
	            _btn = _btn.parentNode;

	            for (var btn, i = 0, j = this.children.length; i < j; i++) {
	                btn = this.children[i];
	                var ul = btn.querySelector('ul');

	                if (btn == _btn) {
	                    if (btn.className.indexOf('pressed') != -1) {
	                        btn.classList.remove('pressed'); // 再次点击，隐藏！
	                        if (ul)
	                            ul.style.height = '0px';
	                    } else {
	                        if (ul)
	                            ul.style.height = ul.scrollHeight + 'px';
	                        btn.classList.add('pressed');
	                    }
	                } else {
	                    btn.classList.remove('pressed');
	                    if (ul)
	                        ul.style.height = '0px';
	                }
	            }
	        } else 
	            return;
	    },
	    
	    // 内部子菜单的高亮
	    highlightSubItem($event) {
	        var li, el = $event.target;
	        if (el.tagName == 'A' && el.getAttribute('target')) {
	            li = el.parentNode;
	            li.parentNode.$('li', _el => {
	                if (_el == li)
	                    _el.classList.add('selected');
	                else
	                    _el.classList.remove('selected');
	            });
	        }
	    }
	}
}); 

// 字体大小
Vue.component('aj-adjust-font-size', {
	props : {
		articleTarget: { // 正文所在的位置，通过 CSS Selector 定位
			type: String, default: 'article p'
		}
	},
	template: 
		'<div class="aj-adjust-font-size" @click="onClk">\
			<span>字体大小</span>\
			<ul>\
				<li><label><input type="radio" name="fontSize" /> 小</label></li>\
				<li><label><input type="radio" name="fontSize" /> 中</label></li>\
				<li><label><input type="radio" name="fontSize" /> 大</label></li>\
			</ul>\
		</div>',
	methods: {
		onClk(e) {
			var el = e.target, target = el.innerHTML;
			
			if(el.tagName != 'LABEL')
				el = el.up('label');
			
			if(el.innerHTML.indexOf('大') != -1) 
				this.setFontSize('12pt');
			else if(el.innerHTML.indexOf('中') != -1) 
				this.setFontSize('10.5pt');
			else if(el.innerHTML.indexOf('小') != -1) 
				this.setFontSize('9pt');
		},
		setFontSize(fontSize) {
			aj(this.$props.articleTarget, p => p.style.fontSize = fontSize);
		}
	}
});

// Baidu 自定义搜索
Vue.component('aj-baidu-search', {
	props: ['siteDomainName'],
	template: 
		'<div class="aj-baidu-search"><form method="GET" action="http://www.baidu.com/baidu" onsubmit="//return g(this);">\
		     <input type="text" name="word" placeholder="请输入搜索之关键字" />\
		     <input name="tn" value="bds" type="hidden" />\
		     <input name="cl" value="3" type="hidden" />\
		     <input name="ct" value="2097152" type="hidden" />\
		     <input name="si" :value="getSiteDomainName" type="hidden" />\
		 	<div class="searchBtn" onclick="this.parentNode.submit();"></div>\
		 </form></div>',
	computed: {
		getSiteDomainName() {
			return this.siteDomainName || location.host || document.domain;
		}
	}
});
 
aj.img = (function() {
	function dataURLtoBlob(dataurl) {
		var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1], bstr = atob(arr[1]), 
			len = bstr.length, u8arr = new Uint8Array(len);
			
		while (len--)
			u8arr[len] = bstr.charCodeAt(len);
		
		return new Blob([u8arr], {type: mime});
	}
	
	return {	
		/**
		 * image转canvas：图片地址
		 */
		imageToCanvas(imgUrl, cb, isCovernt2DataUrl) {
			var img = new Image();
			img.onload = () => {
				var canvas = document.createElement('canvas');
				canvas.width = img.width;
				canvas.height = img.height;
				canvas.getContext('2d').drawImage(img, 0, 0);
				
				if(isCovernt2DataUrl) 
					cb(canvas.toDataURL(isCovernt2DataUrl.format || 'image/jpeg', isCovernt2DataUrl.quality || .9));
				else 
					cb(canvas);
			}
			
			img.src = imgUrl;
		},
		
		imageElToBlob(imgUrl, cb) {
			this.imageToCanvas(imgUrl, canvas => cb(dataURLtoBlob(canvas)), true);
		},

		/**
		 * 改变blob图片的质量，为考虑兼容性
		 * 
		 * @param blob 图片对象
		 * @param callback 转换成功回调，接收一个新的blob对象作为参数
		 * @param format  目标格式，mime格式
		 * @param quality 介于0-1之间的数字，用于控制输出图片质量，仅当格式为jpg和webp时才支持质量，png时quality参数无效
		 */
		changeBlobImageQuality(blob, callback, format, quality) {
			format = format || 'image/jpeg';
			quality = quality || 0.9; // 经测试0.9最合适
			var fr = new FileReader();
			
			fr.onload = (e) => {
				var dataURL = e.target.result;
				var img = new Image();
				img.onload = () => {
					var canvas = document.createElement('canvas');
					var ctx = canvas.getContext('2d');
					canvas.width = img.width;
					canvas.height = img.height;
					ctx.drawImage(img, 0, 0);
					
					var newDataURL = canvas.toDataURL(format, quality);
					callback && callback(dataURLtoBlob(newDataURL));
					canvas = null;
				};
				
				img.src = dataURL;
			}
			
			fr.readAsDataURL(blob); // blob 转 dataURL
		}
	};
})();

/**
 * 并行和串行任务 作者 https://segmentfault.com/a/1190000013265925
 */
aj.parallel = function(arr, finnaly) {
  let fn, index = 0;
  let statusArr = Array(arr.length).fill().map(() => ({
      isActive: false,
      data: null
   }));
  
  let isFinished = function() {
    return statusArr.every(item => item.isActive === true);
  };
  
  let resolve = function(index) {
    return function(data) {
      statusArr[index].data = data;
      statusArr[index].isActive = true;
      let isFinish = isFinished();
      
      if (isFinish) {
        let datas = statusArr.map(item => item.data);
        
        finnaly(datas);
      }
    };
  };
  
  while((fn = arr.shift())) {
    fn(resolve(index));// 给 resolve 函数追加参数,可以使用 bind 函数实现,这里使用了柯里化
    index++;
  }
}

Date.prototype.format = function(fmt) { //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   

  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));  
 
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  		fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
   
  return fmt;   
}

/**
 * 函数节流 作者 https://www.cnblogs.com/moqiutao/p/6875955.html
 */
aj.throttle = function(fn, delay, mustRunDelay) {
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
 			timer = window.setTimeout(() => fn.apply(this, args), delay);
 		}
 	};
};
