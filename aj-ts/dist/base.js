"use strict";
// VS Code 高亮 HTML 用
var html = String;
Element.prototype.$ = function (cssSelector, fn) {
    if (typeof fn == 'function') {
        var children = this.querySelectorAll(cssSelector);
        if (children && fn)
            // @ts-ignore
            Array.prototype.forEach.call(children, fn);
        return children;
    }
    else
        // @ts-ignore
        return this.querySelector.apply(this, arguments);
};
Element.prototype.up = function (tagName, className) {
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
};
Function.prototype.delegate = function () {
    var _args = [];
    for (var _i = 0; _i < arguments.length; _i++) {
        _args[_i] = arguments[_i];
    }
    var self = this, scope = this.scope, args = arguments, aLength = arguments.length, fnToken = 'function';
    return function () {
        var bLength = arguments.length, Length = (aLength > bLength) ? aLength : bLength;
        // mission one:
        for (var i = 0; i < Length; i++)
            if (arguments[i])
                args[i] = arguments[i]; // 拷贝参数
        args.length = Length; // 在 MS JScript 下面，arguments 作为数字来使用还是有问题，就是 length 不能自动更新。修正如左:
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
 * 日期格式化
 *
 * @author meizz
 */
Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,
        "d+": this.getDate(),
        "h+": this.getHours(),
        "m+": this.getMinutes(),
        "s+": this.getSeconds(),
        "q+": Math.floor((this.getMonth() + 3) / 3),
        "S": this.getMilliseconds() // 毫秒   
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) {
            var obj = (RegExp.$1.length == 1) ? o[k] : ("00" + o[k]).substr(("" + o[k]).length);
            // @ts-ignore
            fmt = fmt.replace(RegExp.$1, obj);
        }
    return fmt;
};
/*
    polyfill JavaScript-Canvas-to-Blob 解决了 HTMLCanvasElement.toBlob 的兼容性
    https://github.com/blueimp/JavaScript-Canvas-to-Blob
*/
if (!HTMLCanvasElement.prototype.toBlob) {
    Object.defineProperty(HTMLCanvasElement.prototype, 'toBlob', {
        value: function (callback, type, quality) {
            var binStr = atob(this.toDataURL(type, quality).split(',')[1]), len = binStr.length, arr = new Uint8Array(len);
            for (var i = 0; i < len; i++)
                arr[i] = binStr.charCodeAt(i);
            callback(new Blob([arr], { type: type || 'image/png' }));
        }
    });
}

"use strict";
var aj;
(function (aj) {
    /**
     * 复制 b 对象到 a 对象身上
     *
     * @param {Object} 目标对象
     * @param {Object} 源对象
     */
    function apply(a, b, c) {
        for (var i in b)
            a[i] = b[i];
        return c ? aj.apply(a, c) : a;
    }
    aj.apply = apply;
    /**
     * 加载脚本
     *
     * @param url   脚本地址
     * @param id    脚本元素 id
     * @param cb    回调函数
     */
    function loadScript(url, id, cb) {
        var script = document.createElement("script");
        script.src = url;
        if (cb)
            script.onload = cb;
        if (id)
            script.id = id;
        document.getElementsByTagName("head")[0].appendChild(script);
    }
    aj.loadScript = loadScript;
    /**
      * 并行和串行任务
      *
      * @author https://segmentfault.com/a/1190000013265925
      * @param arr
      * @param finnaly
      */
    function parallel(arr, _finally) {
        var fn, index = 0;
        // @ts-ignore
        var statusArr = Array(arr.length).fill().map(function () { return ({
            isActive: false,
            data: null
        }); });
        var isFinished = function () {
            return statusArr.every(function (item) { return item.isActive === true; });
        };
        var resolve = function (index) {
            return function (data) {
                statusArr[index].data = data;
                statusArr[index].isActive = true;
                var isFinish = isFinished();
                if (isFinish) {
                    var datas = statusArr.map(function (item) { return item.data; });
                    _finally(datas);
                }
            };
        };
        // @ts-ignore
        while ((fn = arr.shift())) {
            fn(resolve(index)); // 给 resolve 函数追加参数,可以使用 bind 函数实现,这里使用了柯里化
            index++;
        }
    }
    aj.parallel = parallel;
    /**
     * 函数节流
     *
     * @author https://www.cnblogs.com/moqiutao/p/6875955.html
     * @param fn
     * @param delay
     * @param mustRunDelay
     */
    function throttle(fn, delay, mustRunDelay) {
        var timer, t_start;
        return function () {
            var _this = this;
            var t_curr = +new Date();
            window.clearTimeout(timer);
            if (!t_start)
                t_start = t_curr;
            if (t_curr - t_start >= mustRunDelay) {
                // @ts-ignore
                fn.apply(this, arguments);
                t_start = t_curr;
            }
            else {
                var args = arguments;
                // @ts-ignore
                timer = window.setTimeout(function () { return fn.apply(_this, args); }, delay);
            }
        };
    }
    aj.throttle = throttle;
    ;
    aj.SELECTED = "selected";
    aj.SELECTED_CSS = "." + aj.SELECTED;
    /**
     * 为让 Vue 组件使用 Class 风格，通过一个类似语法糖的转换器
     * 这是实验性质的
     */
    var VueComponent = /** @class */ (function () {
        function VueComponent() {
        }
        /**
         * 转换为 ClassAPI
         */
        VueComponent.prototype.initComp = function () {
            Vue.component(this.name, this);
        };
        return VueComponent;
    }());
    aj.VueComponent = VueComponent;
})(aj || (aj = {}));

"use strict";
/*
* --------------------------------------------------------
* 封装 XHR，支持
* GET/POST/PUT/DELETE/FormData
* --------------------------------------------------------
*/
var aj;
(function (aj) {
    var xhr;
    (function (xhr_1) {
        /**
         * 执行请求，这是内部的函数
         *
         * @param url       注意 url 部分带有 # 的话则不能传参数过来
         * @param cb        回调函数
         * @param args      请求参数
         * @param method    请求 HTTP 方法
         * @param cfg       配置
         */
        function request(url, cb, args, method, cfg) {
            if (method === void 0) { method = "GET"; }
            var params = args ? json2url(args) : "";
            var xhr = new XMLHttpRequest();
            method = method.toUpperCase();
            if (method == 'POST' || method == 'PUT')
                xhr.open(method, url);
            else
                xhr.open(method, url + (params ? '?' + params : ''));
            cb.url = url; // 保存 url 以便记录请求路径，可用于调试
            // @ts-ignore
            xhr.onreadystatechange = requestHandler.delegate(null, cb, cfg && cfg.parseContentType);
            xhr.setRequestHeader('Accept', 'application/json');
            if (method == 'POST' || method == 'PUT') {
                xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
                xhr.send(params);
            }
            else
                xhr.send(null);
        }
        xhr_1.request = request;
        /**
         * JSON 转换为 URL。
         * 注意这个方法不会作任何编码处理。
         *
         * @param json      JSON
         * @param appendUrl 附加的地址
         * @returns 如 a=1&b=true&c=foo 的参数字符串
         */
        function json2url(json, appendUrl) {
            var params = new Array();
            for (var i in json)
                params.push(i + '=' + json[i]);
            var _params = params.join('&');
            if (appendUrl) // 如果有 ? 则追加，否则加入 ?
                _params = ~appendUrl.indexOf('?') ? appendUrl + '&' + params : appendUrl + '?' + params;
            return _params;
        }
        xhr_1.json2url = json2url;
        /**
         * XHR 前期执行的回调函数，进行一些初始化的工作
         *
         * @param this
         * @param ev                XHR 事件，不使用
         * @param cb                回调函数
         * @param parseContentType  解析响应数据的类型
         */
        function requestHandler(ev, cb, parseContentType) {
            if (this.readyState === 4 && this.status === 200) {
                var responseText = this.responseText.trim();
                var data = null;
                try {
                    if (!responseText)
                        throw '服务端返回空的字符串!';
                    switch (parseContentType) {
                        case 'text':
                            data = responseText;
                            break;
                        case 'xml':
                            data = this.responseXML;
                            break;
                        case 'json':
                        default:
                            try {
                                data = JSON.parse(responseText);
                            }
                            catch (e) {
                                try {
                                    data = eval("window.TEMP_VAR = " + responseText); // for {ok: true}
                                }
                                catch (e) {
                                    throw e;
                                }
                            }
                    }
                }
                catch (e) {
                    window.alert("XHR \u9519\u8BEF:" + e + "\n\u8BBF\u95EE\u5730\u5740\u662F: " + cb.url); // 提示用户异常
                }
                cb(data, this);
            }
            if (this.readyState === 4 && this.status == 500)
                window.alert('服务端 500 错误！');
        }
        xhr_1.requestHandler = requestHandler;
        xhr_1.get = function (url, cb, args, cfg) { return request(url, cb, args, 'GET', cfg); };
        xhr_1.post = function (url, cb, args, cfg) { return request(url, cb, args, 'POST', cfg); };
        xhr_1.put = function (url, cb, args, cfg) { return request(url, cb, args, 'PUT', cfg); };
        xhr_1.dele = function (url, cb, args, cfg) { return request(url, cb, args, 'DELETE', cfg); };
        /**
         *  默认的回调，有专属的字段并呼叫专属的控件
         *
         * @param j         响应结果 JSON
         * @param xhr       XHR 请求对象
         * @param onOK      当 isOk = true 时执行的回调函数
         * @param onFail    当 isOk = false 时执行的回调函数
         */
        xhr_1.defaultCallBack = function (j, xhr, onOK, onFail) {
            if (j) {
                if (j.isOk) {
                    onOK && onOK(j);
                    aj.msg.show(j.msg || '操作成功！');
                }
                else {
                    onFail && onFail(j);
                    aj.msg(j.msg || '执行失败！原因未知！');
                }
            }
            else {
                onFail && onFail(j);
                aj.msg.show('服务端执行错误，不是标准的消息体 ServerSide Error! ');
            }
        };
        /**
         * 初始化 AJAX 表单
         *
         * @param form  表单元素，可以是 CSS 选择符，或者是 HTML元素
         * @param cb    回调函数
         * @param cfg   表单请求的配置参数，可选的
         */
        function form(form, cb, cfg) {
            if (cb === void 0) { cb = xhr_1.defaultCallBack; }
            if (cfg === void 0) { cfg = {}; }
            if (!form)
                return;
            if (typeof form == 'string')
                form = document.body.$(form);
            if (!form.action)
                throw '请在 form 表单中指定 action 属性。Please fill the url in ACTION attribute.';
            !cfg.noFormValid && aj.form.Validator(form);
            if (cfg.googleReCAPTCHA) { // 加载脚本
                var script = document.body.$("#googleReCAPTCHA");
                if (!script)
                    aj.loadScript("https://www.recaptcha.net/recaptcha/api.js?render=" + cfg.googleReCAPTCHA, 'googleReCAPTCHA');
            }
            // @ts-ignore
            form.addEventListener('submit', formSubmit.delegate(null, cb, cfg));
            var returnBtn = form.$('button.returnBtn'); // shorthand for back btn
            if (returnBtn)
                returnBtn.onclick = goBack;
        }
        xhr_1.form = form;
        /**
         * 执行表单的 XHR 请求
         * 通过拦截表单的 submit 事件触发。
         *
         * @param ev    事件对象
         * @param cb    回调函数
         * @param cfg   表单请求的配置参数
         */
        function formSubmit(ev, cb, cfg) {
            ev.preventDefault(); // 禁止 form 默认提交
            var form = ev.target;
            var method = form.getAttribute('method'); // form.method always GET, so form.getAttribute('method') instead
            if (method)
                method = method.toLowerCase();
            cfg.method = method || cfg.method || 'post';
            if (!cfg.noFormValid && !aj.form.Validator.onSubmit(form))
                return;
            var json = serializeForm(form, cfg);
            if (cfg && cfg.beforeSubmit && cfg.beforeSubmit(form, json) === false)
                return;
            if (cfg.googleReCAPTCHA) {
                // @ts-ignore
                grecaptcha.ready(function () {
                    // @ts-ignore
                    grecaptcha.execute(cfg.googleReCAPTCHA, { action: 'submit' }).then(function (token) {
                        // Add your logic to submit to your backend server here.
                        json.grecaptchaToken = token;
                        if (cfg.method == 'put')
                            xhr_1.put(form.action, cb, json);
                        else
                            xhr_1.post(form.action, cb, json);
                    });
                });
            }
            else {
                if (cfg.method == 'put')
                    xhr_1.put(form.action, cb, json);
                else
                    xhr_1.post(form.action, cb, json);
            }
        }
        /**
         * 为表单里面的 返回按钮 添加后退的事件处理器
         *
         * @param ev
         */
        function goBack(ev) {
            ev.preventDefault();
            history.back();
        }
        /**
         * 表单序列化，兼容旧浏览器和 H5 FormData，返回 JSON
         *
         * @param form  表单元素
         * @param cfg   是否有忽略的字段
         * @returns Json 参数，已 encodeURIComponent 编码 value
         */
        function serializeForm(form, cfg) {
            var json = {}, formData = new FormData(form);
            formData.forEach(function (value, name) {
                if (cfg && cfg.ignoreField != name) // 忽略的字段
                    json[name] = encodeURIComponent(value.toString());
            });
            return json;
        }
    })(xhr = aj.xhr || (aj.xhr = {}));
})(aj || (aj = {}));
