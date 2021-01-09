"use strict";
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
     * @param url
     * @param id
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
    function parallel(arr, finnaly) {
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
                    finnaly(datas);
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
})(aj || (aj = {}));

"use strict";
/*
* --------------------------------------------------------
* 封装 XHR，支持
* GET/POST/PUT/DELETE/JSONP/FormData
* http://blog.csdn.net/zhangxin09/article/details/78879244
* --------------------------------------------------------
*/
var aj;
(function (aj) {
    var xhr;
    (function (xhr_1) {
        /**
         * JSON 转换为 URL
         *
         * @param json      JSON
         * @param appendUrl 附加的地址
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
         *
         *
         * @param url   注意 url 部分带有 # 的话则不能传参数过来
         * @param cb
         * @param args
         * @param method
         * @param cfg
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
            xhr.onreadystatechange = requestCallback.delegate(null, cb, cfg && cfg.parseContentType);
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
         * 表单序列化，兼容旧浏览器和 H5 FormData，返回 JSON
         *
         * @param form
         * @param cfg
         */
        function serializeForm(form, cfg) {
            var json = {};
            var formData = new FormData(form);
            formData.forEach(function (value, name) {
                if (cfg && cfg.ignoreField != name) // 忽略的字段
                    json[name] = encodeURIComponent(value.toString());
            });
            return json;
        }
        xhr_1.serializeForm = serializeForm;
        /**
         *
         * @param form
         * @param cb
         * @param cfg
         */
        function form(form, cb, cfg) {
            cb = cb || xhr_1.defaultCallBack;
            cfg = cfg || {};
            if (!form)
                return;
            if (typeof form == 'string')
                form = document.body.$(form);
            if (!form.action)
                throw 'Please fill the url in ACTION attribute.';
            !cfg.noFormValid && aj.form.Validator(form);
            if (cfg.googleReCAPTCHA) { // 加载脚本
                var script = document.body.$("#googleReCAPTCHA");
                if (!script) {
                    var src = 'https://www.recaptcha.net/recaptcha/api.js?render=';
                    src += cfg.googleReCAPTCHA;
                    aj.loadScript(src, 'googleReCAPTCHA');
                }
            }
            // @ts-ignore
            form.addEventListener('submit', formSubmit.delegate(null, cb, cfg));
            var returnBtn = form.$('button.returnBtn'); // shorthand for back btn
            if (returnBtn)
                returnBtn.onclick = goBack;
        }
        xhr_1.form = form;
        function formSubmit(e, cb, cfg) {
            e.preventDefault(); // 禁止 form 默认提交
            var form = e.target;
            // form.method always GET, so form.getAttribute('method') instead
            var method = form.getAttribute('method');
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
                if (cfg && cfg.method == 'put')
                    xhr_1.put(form.action, cb, json);
                else
                    xhr_1.post(form.action, cb, json);
            }
        }
        function goBack(e) {
            e.preventDefault();
            history.back();
        }
        xhr_1.get = function (url, cb, args, cfg) { return aj.xhr.request(url, cb, args, 'GET', cfg); };
        xhr_1.post = function (url, cb, args, cfg) { return aj.xhr.request(url, cb, args, 'POST', cfg); };
        xhr_1.put = function (url, cb, args, cfg) { return aj.xhr.request(url, cb, args, 'PUT', cfg); };
        xhr_1.dele = function (url, cb, args, cfg) { return aj.xhr.request(url, cb, args, 'DELETE', cfg); };
        /**
         *  默认的回调，有专属的字段并呼叫专属的控件
         *
         * @param j
         * @param xhr
         * @param onOK
         * @param onFail
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
                aj.msg.show('ServerSide Error!');
            }
        };
        // export var defaultCallBack = defaultCallBack_cb.delegate(null);
        /**
         *
         * @param event
         * @param cb
         * @param parseContentType
         */
        function requestCallback(event, cb, parseContentType) {
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
                                    data = eval("TEMP_VAR = " + responseText); // for {ok: true}
                                }
                                catch (e) {
                                    throw e;
                                }
                            }
                    }
                }
                catch (e) {
                    aj.alert('XHR 错误:\n' + e + '\nUrl is:' + cb.url); // 提示用户 异常
                }
                if (cb)
                    cb(data, this);
                else
                    throw '你未提供回调函数';
            }
            if (this.readyState === 4 && this.status == 500)
                aj.alert('服务端 500 错误！');
        }
    })(xhr = aj.xhr || (aj.xhr = {}));
})(aj || (aj = {}));
