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
     * 判断是否 Vue 配置字段
     *
     * @param name
     */
    function isVueCfg(name) {
        return name == 'template' || name == 'data' || name == 'mixins'
            || name == 'computed' || name == 'mounted' || name == 'beforeCreate' || name == "watch";
    }
    /**
     *
     * @param name
     * @param props
     */
    function isPropsField(name, props) {
        if (props && props[name])
            return true;
        else
            return false;
    }
    /**
     * 判断是否 props 字段
     *
     * @param value
     */
    function isSimplePropsField(value) {
        // console.log(value)
        if (value === String || value === Boolean || value === Number || value && value.type)
            return true;
        else
            return false;
    }
    /**
     * 为让 Vue 组件使用 Class 风格，通过一个类似语法糖的转换器
     * 这是实验性质的
     */
    var VueComponent = /** @class */ (function () {
        function VueComponent() {
            this.$el = document.body;
            this.props = {};
            this.$children = [];
        }
        VueComponent.prototype.$watch = function (a, b) { };
        VueComponent.prototype.$set = function (a, b, c) { };
        // public propsFactory: any;
        // public propsFactory(): { [key: string]: any };
        VueComponent.prototype.$destroy = function () { };
        VueComponent.prototype.$emit = function (e) {
            var obj = [];
            for (var _i = 1; _i < arguments.length; _i++) {
                obj[_i - 1] = arguments[_i];
            }
        };
        /**
         * 转换为 ClassAPI
         */
        VueComponent.prototype.register = function (instanceFields) {
            var cfg = {
                props: {},
                methods: {}
            };
            var dataFields = {};
            var props;
            //@ts-ignore
            if (typeof this.propsFactory == 'function') // fn 用于继承时候的复制
                //@ts-ignore
                props = this.propsFactory();
            else
                props = this.props;
            for (var i in this) {
                if (i == 'constructor' || i == 'name' || i == 'register' || i == 'propsFactory' || i == 'watchFactory' ||
                    i == 'props' || i == '$destroy' || i == '$watch' || i == '$set' || i == "$el" || i == "$emit" || i == "$options")
                    continue;
                var value = this[i];
                if (isVueCfg(i))
                    cfg[i] = value;
                else if (isSimplePropsField(value))
                    cfg.props[i] = value;
                else if (typeof value == 'function')
                    cfg.methods[i] = value;
                else if (isPropsField(i, props))
                    cfg.props[i] = props[i];
                else if (i[0] != '$') // 如果不是 $ 开头的，就是 data fields，$xxx 表示实例变量，不参与数据驱动，节省资源
                    dataFields[i] = value;
            }
            for (var i_1 in props) { // 补充缺少的 prop。这些 prop 只在 markup 中使用，故不需要在 class 中列出
                if (!(i_1 in cfg.props))
                    cfg.props[i_1] = props[i_1];
            }
            // 注意如果 类有了 data(){}，那么 data 属性将会失效（仅作提示用），改读取 data() {} 的
            if (!cfg.data)
                cfg.data = function () {
                    return JSON.parse(JSON.stringify(dataFields)); // 深度 Clone 对象
                };
            // console.log(cfg)
            // @ts-ignore
            if (cfg.watch && this.watchFactory)
                // @ts-ignore
                aj.apply(cfg.watch, this.watchFactory());
            Vue.component(this.name, cfg);
        };
        return VueComponent;
    }());
    aj.VueComponent = VueComponent;
})(aj || (aj = {}));
// VS Code 高亮 HTML 用
var html = String;

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
                if (j.result)
                    // @ts-ignore
                    j = j.result;
                if (j.isOk) {
                    onOK && onOK(j);
                    aj.msg.show(j.msg || '操作成功！');
                }
                else {
                    onFail && onFail(j);
                    aj.msg.show(j.msg || '执行失败！原因未知！');
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
         * @param form  表单元素，可以是 CSS 选择符，或者是 HTML 元素
         * @param cb    回调函数，可选的
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
            if (!cfg.noFormValid)
                new aj.form.Validator(form);
            if (cfg.googleReCAPTCHA && !document.body.$("#googleReCAPTCHA")) // 加载脚本
                aj.loadScript("https://www.recaptcha.net/recaptcha/api.js?render=" + cfg.googleReCAPTCHA, 'googleReCAPTCHA');
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
         * @param ev    事件对象
         * @param cb    回调函数
         * @param cfg   表单请求的配置参数
         */
        function formSubmit(ev, cb, cfg) {
            ev.preventDefault(); // 禁止 form 默认提交
            var form = ev.target, method = form.getAttribute('method'); // form.method always GET, so form.getAttribute('method') instead
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
         * 表单序列化，返回 JSON
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

var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();

var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
"use strict";

/**
 * 折叠菜单
 */
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var AccordionMenu = /** @class */ (function (_super) {
            __extends(AccordionMenu, _super);
            function AccordionMenu() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-accordion-menu";
                _this.template = '<ul class="aj-accordion-menu" @click="onClk"><slot></slot></ul>';
                return _this;
            }
            AccordionMenu.prototype.onClk = function (ev) {
                var children = this.$el.children;
                highlightSubItem(ev);
                var _btn = ev.target;
                if (_btn && _btn.tagName == 'H3' && _btn.parentNode.tagName == 'LI') {
                    _btn = _btn.parentNode;
                    for (var btn = void 0, i = 0, j = children.length; i < j; i++) {
                        btn = children[i];
                        var ul = btn.querySelector('ul');
                        if (btn == _btn) {
                            if (btn.className.indexOf('pressed') != -1) {
                                btn.classList.remove('pressed'); // 再次点击，隐藏！
                                if (ul)
                                    ul.style.height = '0px';
                            }
                            else {
                                if (ul)
                                    ul.style.height = ul.scrollHeight + 'px';
                                btn.classList.add('pressed');
                            }
                        }
                        else {
                            btn.classList.remove('pressed');
                            if (ul)
                                ul.style.height = '0px';
                        }
                    }
                }
                else
                    return;
            };
            return AccordionMenu;
        }(aj.VueComponent));
        widget.AccordionMenu = AccordionMenu;
        new AccordionMenu().register();
        /**
         * 内部子菜单的高亮
         *
         * @param ev
         */
        function highlightSubItem(ev) {
            var _a;
            var li, el = ev.target;
            if (el.tagName == 'A' && el.getAttribute('target')) {
                li = el.parentNode;
                (_a = li.parentNode) === null || _a === void 0 ? void 0 : _a.$('li', function (_el) {
                    if (_el == li)
                        _el.classList.add('selected');
                    else
                        _el.classList.remove('selected');
                });
            }
        }
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var widget;
    (function (widget) {
        /**
         * Baidu 自定义搜索
         */
        var BaiduSearch = /** @class */ (function (_super) {
            __extends(BaiduSearch, _super);
            function BaiduSearch() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = 'aj-widget-baidu-search';
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-widget-baidu-search\">\n                <form method=\"GET\" action=\"http://www.baidu.com/baidu\" onsubmit=\"//return g(this);\">\n                    <input type=\"text\" name=\"word\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" />\n                    <input name=\"tn\" value=\"bds\" type=\"hidden\" />\n                    <input name=\"cl\" value=\"3\" type=\"hidden\" />\n                    <input name=\"ct\" value=\"2097152\" type=\"hidden\" />\n                    <input name=\"si\" :value=\"getSiteDomainName\" type=\"hidden\" />\n                    <div class=\"searchBtn\" onclick=\"this.parentNode.submit();\"></div>\n                </form>\n            </div>\n        "], ["\n            <div class=\"aj-widget-baidu-search\">\n                <form method=\"GET\" action=\"http://www.baidu.com/baidu\" onsubmit=\"//return g(this);\">\n                    <input type=\"text\" name=\"word\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" />\n                    <input name=\"tn\" value=\"bds\" type=\"hidden\" />\n                    <input name=\"cl\" value=\"3\" type=\"hidden\" />\n                    <input name=\"ct\" value=\"2097152\" type=\"hidden\" />\n                    <input name=\"si\" :value=\"getSiteDomainName\" type=\"hidden\" />\n                    <div class=\"searchBtn\" onclick=\"this.parentNode.submit();\"></div>\n                </form>\n            </div>\n        "]));
                _this.siteDomainName = String;
                _this.computed = {
                    getSiteDomainName: function () {
                        //@ts-ignore
                        return this.siteDomainName || location.host || document.domain;
                    }
                };
                return _this;
            }
            return BaiduSearch;
        }(aj.VueComponent));
        widget.BaiduSearch = BaiduSearch;
        new BaiduSearch().register();
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var widget;
    (function (widget) {
        /**
         * 展开闭合器
         */
        var Expander = /** @class */ (function (_super) {
            __extends(Expander, _super);
            function Expander() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-expander";
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-expander\" :style=\"'height:' + (expended ? openHeight : closeHeight) + 'px;'\">\n                <div :class=\"expended ? 'closeBtn' : 'openBtn'\" @click=\"expended = !expended;\"></div>\n                <slot></slot>\n            </div>\n        "], ["\n            <div class=\"aj-expander\" :style=\"'height:' + (expended ? openHeight : closeHeight) + 'px;'\">\n                <div :class=\"expended ? 'closeBtn' : 'openBtn'\" @click=\"expended = !expended;\"></div>\n                <slot></slot>\n            </div>\n        "]));
                _this.expended = false;
                _this.openHeight = { type: Number, "default": 200 };
                _this.closeHeight = { type: Number, "default": 50 };
                return _this;
            }
            return Expander;
        }(aj.VueComponent));
        widget.Expander = Expander;
        new Expander().register();
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var widget;
    (function (widget) {
        /**
         * 进度条
         */
        var ProcessLine = /** @class */ (function (_super) {
            __extends(ProcessLine, _super);
            function ProcessLine() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = 'aj-process-line';
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-process-line\">\n                <div class=\"process-line\">\n                    <div v-for=\"(item, index) in items\" :class=\"{current: index == current, done: index < current}\">\n                        <span>{{index + 1}}</span>\n                        <p>{{item}}</p>\n                    </div>\n                </div>\n            </div>\n        "], ["\n            <div class=\"aj-process-line\">\n                <div class=\"process-line\">\n                    <div v-for=\"(item, index) in items\" :class=\"{current: index == current, done: index < current}\">\n                        <span>{{index + 1}}</span>\n                        <p>{{item}}</p>\n                    </div>\n                </div>\n            </div>\n        "]));
                _this.props = {
                    items: {
                        type: Array,
                        default: function () {
                            return ['Step 1', 'Step 2', 'Step 3'];
                        }
                    }
                };
                _this.items = [];
                _this.current = 0;
                return _this;
            }
            /**
             *
             * @param i
             */
            ProcessLine.prototype.go = function (i) {
                this.current = i;
            };
            /**
             *
             */
            ProcessLine.prototype.perv = function () {
                var perv = this.current - 1;
                if (perv < 0)
                    perv = this.items.length - 1;
                this.go(perv);
            };
            /**
             *
             */
            ProcessLine.prototype.next = function () {
                var next = this.current + 1;
                if (this.items.length == next)
                    next = 0; // 循环
                this.go(next);
            };
            return ProcessLine;
        }(aj.VueComponent));
        widget.ProcessLine = ProcessLine;
        new ProcessLine().register();
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";
/**
 * Tab 组件
 *
 * 参考：https://vuejs.org/v2/guide/components.html#Content-Distribution-with-Slots
 */
Vue.component('aj-tab', {
    template: "\n        <div :class=\"isVertical ? 'aj-simple-tab-vertical' : 'aj-tab' \">\n\t      <button v-for=\"tab in tabs\" v-bind:key=\"tab.name\"\n\t        v-bind:class=\"['tab-button', { active: currentTab.name === tab.name }]\"\n\t        v-on:click=\"currentTab = tab\">{{tab.name}}\n\t      </button>\n\t      <component v-bind:is=\"currentTab.component\" class=\"tab\"></component>\n        </div>\n    ",
    props: {
        isVertical: Boolean // 是否垂直方向的布局，默认 false,
    },
    data: function () {
        return {
            tabs: [],
            currentTab: {}
        };
    },
    mounted: function () {
        var arr = this.$slots.default;
        for (var i = 0; i < arr.length; i++) {
            var el = arr[i];
            if (el.tag === 'textarea') {
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
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var tab;
        (function (tab) {
            tab.tabable = {
                mounted: function () {
                    var _this = this;
                    var ul = this.$el.$('.aj-simple-tab-horizontal > ul');
                    ul.onclick = function (e) {
                        var _a, _b;
                        var el = e.target;
                        select(el);
                        var index = Array.prototype.indexOf.call((_a = el.parentElement) === null || _a === void 0 ? void 0 : _a.children, el);
                        // @ts-ignore
                        var _new = (_b = _this.$el.$('.aj-simple-tab-horizontal > div')) === null || _b === void 0 ? void 0 : _b.children[index];
                        select(_new);
                    };
                    // @ts-ignore
                    ul.onclick({ target: ul.children[0] });
                    //this.$options.watch.selected.call(this, 0);
                }
            };
            // 按次序选中目标
            var select = function (_new) {
                var oldSelected = _new.parentNode.$('.selected');
                if (_new === oldSelected) // 没变化
                    return;
                oldSelected && oldSelected.classList.remove('selected');
                _new.classList.add('selected');
            };
        })(tab = widget.tab || (widget.tab = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var img;
        (function (img) {
            /**
             * 显示头像
             */
            var Avatar = /** @class */ (function (_super) {
                __extends(Avatar, _super);
                function Avatar() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = 'aj-avatar';
                    _this.template = html(__makeTemplateObject(["\n            <a :href=\"avatar\" target=\"_blank\">\n                <img :src=\"avatar\" style=\"max-width:50px;max-height:60px;vertical-align: middle;\" \n                    @mouseenter=\"mouseEnter\"\n                    @mouseleave=\"mouseLeave\" />\n            </a>\n        "], ["\n            <a :href=\"avatar\" target=\"_blank\">\n                <img :src=\"avatar\" style=\"max-width:50px;max-height:60px;vertical-align: middle;\" \n                    @mouseenter=\"mouseEnter\"\n                    @mouseleave=\"mouseLeave\" />\n            </a>\n        "]));
                    _this.props = {
                        avatar: { type: String, required: true }
                    };
                    /**
                     * 头像图片地址
                     */
                    _this.avatar = "";
                    return _this;
                }
                Avatar.prototype.mouseEnter = function () {
                    if (img.imageEnlarger)
                        img.imageEnlarger.imgUrl = this.avatar;
                };
                Avatar.prototype.mouseLeave = function () {
                    if (img.imageEnlarger)
                        img.imageEnlarger.imgUrl = null;
                };
                return Avatar;
            }(aj.VueComponent));
            img.Avatar = Avatar;
            new Avatar().register();
        })(img = widget.img || (widget.img = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var img;
        (function (img) {
            function initImageEnlarger() {
                img.imageEnlarger = new Vue({
                    el: document.body.appendChild(document.createElement('div')),
                    template: '<div class="aj-image-large-view"><div><img :src="imgUrl" /></div></div>',
                    data: {
                        imgUrl: null
                    },
                    mounted: function () {
                        var _this = this;
                        // @ts-ignore
                        document.addEventListener('mousemove', aj.throttle(function (e) {
                            if (_this.imgUrl) {
                                var w = 0, imgWidth = _this.$el.$('img').clientWidth;
                                if (imgWidth > e.pageX)
                                    w = imgWidth;
                                var el = _this.$el.$('div');
                                el.style.top = (e.pageY + 20) + 'px';
                                el.style.left = (e.pageX - el.clientWidth + w) + 'px';
                            }
                        }, 50, 5000), false);
                    }
                });
                return img.imageEnlarger;
            }
            img.initImageEnlarger = initImageEnlarger;
            Vue.component('aj-img-thumb', {
                template: "<a class=\"aj-img-thumb\" :href=\"getImgUrl\" v-if=\"imgUrl\" target=\"_blank\">\n                <img :src=\"getImgUrl\"\n                    :onmouseenter=\"'aj.widget.img.imageEnlarger.imgUrl = \\'' + getImgUrl + '\\';'\" \n                    onmouseleave=\"aj.widget.img.imageEnlarger.imgUrl = null;\" />\n            </a>",
                props: {
                    imgUrl: String
                },
                computed: {
                    getImgUrl: function () {
                        if (!this.imgUrl)
                            return "";
                        if (this.imgUrl.indexOf('http') != -1) // 图片地址已经是完整的 http 地址，直接返回
                            return this.imgUrl;
                        // uploadFile.imgPerfix
                        if (!this.ajResources.imgPerfix)
                            throw "未提供图片前缀地址";
                        return this.ajResources.imgPerfix + this.imgUrl;
                    }
                }
            });
        })(img = widget.img || (widget.img = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";
/**
 * 图片工具库
 */
var aj;
(function (aj) {
    var img;
    (function (img_1) {
        /**
         * 为了避免压缩图片变形，一般采用等比缩放，
         * 首先要计算出原始图片宽高比 aspectRatio，
         * 用户设置的高乘以 aspectRatio，得出等比缩放后的宽，
         * 若比用户设置宽的小，则用户设置的高为为基准缩放，否则以宽为基准缩放。
         *
         * @param originWidth
         * @param originHeight
         * @param maxWidth
         * @param maxHeight
         */
        function fitSize(originWidth, originHeight, maxWidth, maxHeight) {
            // 目标尺寸
            var targetWidth = originWidth, targetHeight = originHeight;
            // 图片尺寸超过的限制
            if (originWidth > maxWidth || originHeight > maxHeight) {
                if (originWidth / originHeight > maxWidth / maxHeight) {
                    // 更宽，按照宽度限定尺寸
                    targetWidth = maxWidth;
                    targetHeight = Math.round(maxWidth * (originHeight / originWidth));
                }
                else {
                    targetHeight = maxHeight;
                    targetWidth = Math.round(maxHeight * (originWidth / originHeight));
                }
            }
            return { targetWidth: targetWidth, targetHeight: targetHeight };
        }
        img_1.fitSize = fitSize;
        /**
         * 图片对象转换为 Canvas
         *
         * @param img
         * @param targetWidth
         * @param targetHeigh
         */
        function imgObj2Canvas(img, targetWidth, targetHeigh) {
            var _a;
            var canvas = document.createElement('canvas');
            canvas.width = targetWidth || img.width;
            canvas.height = targetHeigh || img.height;
            (_a = canvas.getContext('2d')) === null || _a === void 0 ? void 0 : _a.drawImage(img, 0, 0);
            return canvas;
        }
        img_1.imgObj2Canvas = imgObj2Canvas;
        /**
        * Canvas.toDataURL() 也能调整图片大小（压缩图片），但不推荐
        * 参见：https://www.zhihu.com/question/59267048
        * 除了作为转换图片的 base64 编码的时候
        *
        * 推荐使用 reszieAsBlob()
        *
        * @param img
        * @param targetWidth
        * @param targetHeight
        * @param format         目标格式，mime 格式
        * @param quality        介于 0-1 之间的数字，用于控制输出图片质量，仅当格式为 jpg 和 webp 时才支持质量，png 时 quality 参数无效
        */
        function reszieCompressCompressAsDataURL(img, targetWidth, targetHeight, format, quality) {
            if (format === void 0) { format = 'image/jpeg'; }
            if (quality === void 0) { quality = .9; }
            return imgObj2Canvas(img, targetWidth, targetHeight).toDataURL(format, quality);
        }
        img_1.reszieCompressCompressAsDataURL = reszieCompressCompressAsDataURL;
        /**
         * 转换图片为 base64 编码
         * 如果你要展示 blob 图片，更推荐用 img.src = URL.createObjectURL(newBlob); 方法，这个 base64 性能耗损比较大
         *
         * @param img
         */
        function img2base64(img) {
            return reszieCompressCompressAsDataURL(img, img.width, img.height);
        }
        img_1.img2base64 = img2base64;
        /**
         * 调整图片大小（压缩图片）异步方法
         *
         * @param img
         * @param targetWidth
         * @param targetHeight
         * @param cb
         * @param format         目标格式，mime 格式
         * @param quality        介于 0-1 之间的数字，用于控制输出图片质量，仅当格式为 jpg 和 webp 时才支持质量，png 时 quality 参数无效
         */
        function reszieCompressAsBlob(img, targetWidth, targetHeight, cb, format, quality) {
            if (format === void 0) { format = 'image/jpeg'; }
            if (quality === void 0) { quality = .9; }
            imgObj2Canvas(img, targetWidth, targetHeight).toBlob(function (blob) { return blob && cb(blob); }, format, quality);
        }
        img_1.reszieCompressAsBlob = reszieCompressAsBlob;
        function compressAsBlob(img, cb, format, quality) {
            if (format === void 0) { format = 'image/jpeg'; }
            if (quality === void 0) { quality = .9; }
            return reszieCompressAsBlob(img, img.width, img.height, cb, format, quality);
        }
        img_1.compressAsBlob = compressAsBlob;
        /**
         * 对粘贴板的图片，都是原始 bitmap 比较大，压缩一下
         *
         * @param blob  图片对象
         * @param cb    转换成功回调，接收一个新的 blob 对象作为参数
         */
        function changeBlobImageQuality(blob, cb) {
            var img = new Image();
            img.onload = function () { return compressAsBlob(img, cb); };
            // 也可以用 FileReader 转 base64，但 img 实际支持 blob 加载的
            img.src = URL.createObjectURL(blob);
        }
        img_1.changeBlobImageQuality = changeBlobImageQuality;
        // EXIF {
        //     getTag(ob: any, ori: string):number;
        // }
        /**
         * 获取图片的方向
         *
         * @param img
         */
        function getPhotoOrientation(img) {
            var orient = 0;
            EXIF.getData(img, function () {
                //@ts-ignore
                orient = EXIF.getTag(this, 'Orientation');
            });
            return orient;
        }
        img_1.getPhotoOrientation = getPhotoOrientation;
        /**
         * 调整图片方向，返回新图片的 Base64 编码
         *
         * @param img
         * @param orient
         */
        function rotate(img, orient) {
            var width = img.width, height = img.height, canvas = document.createElement('canvas'), ctx = canvas.getContext("2d");
            // set proper canvas dimensions before transform & export
            if ([5, 6, 7, 8].indexOf(orient) > -1) {
                canvas.width = height;
                canvas.height = width;
            }
            else {
                canvas.width = width;
                canvas.height = height;
            }
            switch (orient) { // transform context before drawing image
                case 2:
                    ctx.transform(-1, 0, 0, 1, width, 0);
                    break;
                case 3:
                    ctx.transform(-1, 0, 0, -1, width, height);
                    break;
                case 4:
                    ctx.transform(1, 0, 0, -1, 0, height);
                    break;
                case 5:
                    ctx.transform(0, 1, 1, 0, 0, 0);
                    break;
                case 6:
                    ctx.transform(0, 1, -1, 0, height, 0);
                    break;
                case 7:
                    ctx.transform(0, -1, -1, 0, height, width);
                    break;
                case 8:
                    ctx.transform(0, -1, 1, 0, 0, width);
                    break;
                default:
                    ctx.transform(1, 0, 0, 1, 0, 0);
            }
            ctx.drawImage(img, 0, 0);
            return canvas.toDataURL('image/jpeg');
        }
        img_1.rotate = rotate;
        /**
         * 图片压缩
         *
         * @param imgObj
         */
        function compress(imgObj, vueCmp) {
            var maxWidth = 1000, maxHeight = 1500, fitSizeObj = fitSize(imgObj.width, imgObj.height, maxWidth, maxHeight), targetWidth = fitSizeObj.targetWidth, targetHeight = fitSizeObj.targetHeight;
            var orient = getPhotoOrientation(imgObj); // 获取照片的拍摄方向
            if (orient == 6) {
                targetWidth = fitSizeObj.targetHeight;
                targetHeight = fitSizeObj.targetWidth;
            }
            var comp = new Image();
            comp.onload = function () {
                var _a;
                var canvas = document.createElement('canvas');
                canvas.width = targetWidth;
                canvas.height = targetHeight;
                (_a = canvas.getContext('2d')) === null || _a === void 0 ? void 0 : _a.drawImage(comp, 0, 0, targetWidth, targetHeight); // 图片压缩
                canvas.toBlob(function (blob) {
                    vueCmp.$blob = blob;
                }, vueCmp.$fileType || 'image/jpeg');
            };
            comp.src = rotate(imgObj, orient);
        }
        img_1.compress = compress;
        /**
         *
         * @param dataurl
         */
        function dataURLtoBlob(dataurl) {
            // @ts-ignore
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1], bstr = atob(arr[1]), len = bstr.length, u8arr = new Uint8Array(len);
            while (len--)
                u8arr[len] = bstr.charCodeAt(len);
            return new Blob([u8arr], { type: mime });
        }
        img_1.dataURLtoBlob = dataURLtoBlob;
    })(img = aj.img || (aj.img = {}));
})(aj || (aj = {}));

"use strict";

var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var modal;
        (function (modal) {
            /**
             * 浮層組件，通常要復用這個組件
             */
            var Layer = /** @class */ (function (_super) {
                __extends(Layer, _super);
                function Layer() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = "aj-layer";
                    _this.template = '<div class="aj-modal hide" @click="close"><div><slot></slot></div></div>';
                    _this.props = {
                        notCloseWhenTap: Boolean,
                        cleanAfterClose: Boolean // 关闭是否清除
                    };
                    _this.afterClose = function () { };
                    _this.notCloseWhenTap = false;
                    _this.cleanAfterClose = false;
                    return _this;
                }
                /**
                 * 显示浮层
                 *
                 * @param cfg
                 */
                Layer.prototype.show = function (cfg) {
                    var _this = this;
                    var my = Number(getComputedStyle(this.$el).zIndex); // 保证最后显示的总在最前面
                    document.body.$('.aj-modal', function (i) {
                        if (i != _this.$el) {
                            var o = Number(getComputedStyle(i).zIndex);
                            if (o >= my)
                                _this.$el.style.zIndex = String(o + 1);
                        }
                    });
                    this.$el.classList.remove('hide');
                    this.BUS && this.BUS.$emit('aj-layer-closed', this);
                    if (cfg && cfg.afterClose)
                        this.afterClose = cfg && cfg.afterClose;
                };
                /**
                 * 关闭浮层
                 *
                 * @param ev
                 */
                Layer.prototype.close = function (ev) {
                    var isClosed = false;
                    if (!ev) {
                        isClosed = modal.msgbox.$options.methods.close.call(this, {
                            target: document.body.$('.aj-modal')
                        });
                    }
                    else {
                        // @ts-ignore
                        if (ev.isForceClose || !this.notCloseWhenTap)
                            isClosed = modal.msgbox.$options.methods.close.apply(this, arguments);
                    }
                    if (isClosed && this.cleanAfterClose) {
                        this.$el.parentNode && this.$el.parentNode.removeChild(this.$el);
                        this.$destroy();
                    }
                };
                return Layer;
            }(aj.VueComponent));
            modal.Layer = Layer;
            new Layer().register();
        })(modal = widget.modal || (widget.modal = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";

/**
 * 消息框、弹窗、对话框组件
 */
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var modal;
        (function (modal) {
            document.addEventListener("DOMContentLoaded", function () {
                document.body.appendChild(document.createElement('div')).className = 'alertHolder';
                // 全屏幕弹窗，居中显示文字。
                // 不应直接使用该组件，而是执行 aj.showOk
                modal.msgbox = new Vue({
                    el: '.alertHolder',
                    template: html(__makeTemplateObject(["\n                <div class=\"aj-modal hide\" @click=\"close\">\n                    <div>\n                        <div v-html=\"showText\"></div>\n                        <div class=\"aj-btnsHolder\">\n                            <button v-show=\"showOk\"  @click=\"onBtnClk\" class=\"ok\">\u786E\u5B9A</button>\n                            <button v-show=\"showYes\" @click=\"onBtnClk\" class=\"yes\">{{showSave? '\u4FDD\u5B58': '\u662F'}}</button>\n                            <button v-show=\"showNo\"  @click=\"onBtnClk\" class=\"no\">{{showSave? '\u5426': '\u5426'}}</button>\n                        </div>\n                    </div>\n                </div>\n            "], ["\n                <div class=\"aj-modal hide\" @click=\"close\">\n                    <div>\n                        <div v-html=\"showText\"></div>\n                        <div class=\"aj-btnsHolder\">\n                            <button v-show=\"showOk\"  @click=\"onBtnClk\" class=\"ok\">\u786E\u5B9A</button>\n                            <button v-show=\"showYes\" @click=\"onBtnClk\" class=\"yes\">{{showSave? '\u4FDD\u5B58': '\u662F'}}</button>\n                            <button v-show=\"showNo\"  @click=\"onBtnClk\" class=\"no\">{{showSave? '\u5426': '\u5426'}}</button>\n                        </div>\n                    </div>\n                </div>\n            "])),
                    data: {
                        showText: '',
                        afterClose: null,
                        showOk: false,
                        showYes: false,
                        showNo: false,
                        showSave: false // 是否显示“保存”按钮
                    },
                    methods: {
                        /**
                         * 显示
                         *
                         * @param this
                         * @param text  显示文字，支持 HTML 标签
                         * @param cfg   配置项，可选的
                         */
                        show: function (text, cfg) {
                            this.showText = text;
                            this.$el.classList.remove('hide');
                            cfg && aj.apply(this, cfg);
                            return this;
                        },
                        /**
                         * 关闭窗体
                         *
                         * @param this
                         * @param ev    事件对象，可选的
                         */
                        close: function (ev) {
                            if (!ev) { // 直接关闭
                                this.$el.classList.add('hide');
                                this.afterClose && this.afterClose(null, this);
                                return true;
                            }
                            var div = ev.target; // check if in the box
                            if (div && div.className.indexOf('modal') != -1) {
                                this.$el.classList.add('hide');
                                this.afterClose && this.afterClose(div, this);
                                return true;
                            }
                            return false;
                        },
                        onBtnClk: function (ev) {
                            switch (ev.target.className) {
                                case 'ok':
                                    this.onOkClk && this.onOkClk(ev, this);
                                    break;
                                case 'no':
                                    this.onNoClk && this.onNoClk(ev, this);
                                    break;
                                case 'yes':
                                    this.onYesClk && this.onYesClk(ev, this);
                                    break;
                            }
                        }
                    }
                });
            });
            /**
             * 顯示確定的對話框
             *
             * @param {String} text         显示的文本
             * @param {Function} callback   回调函数
             */
            aj.alert = function (text, callback) {
                var alertObj = modal.msgbox.show(text, {
                    showYes: false,
                    showNo: false,
                    showOk: true,
                    onOkClk: function () {
                        alertObj.$el.classList.add('hide');
                        callback && callback();
                    }
                });
            };
            /**
             * 顯示“是否”選擇的對話框
             *
             * @param {String} text         显示的文本
             * @param {Function} callback   回调函数
             */
            aj.showConfirm = function (text, callback, showSave) {
                var alertObj = modal.msgbox.show(text, {
                    showYes: true,
                    showNo: true,
                    showOk: false,
                    showSave: false,
                    onYesClk: function (ev) {
                        alertObj.$el.classList.add('hide');
                        callback && callback(alertObj.$el, ev);
                    },
                    onNoClk: function () {
                        alertObj.$el.classList.add('hide');
                    }
                });
            };
            modal.popup = function (text, callback) {
                modal.msgbox.show(text, {
                    showYes: false,
                    showNo: false,
                    showOk: false,
                    showSave: false
                });
            };
            //----------------------------------------------------------------------------------------
            /**
            * 顶部出现，用于后台提示信息多
            */
            document.addEventListener("DOMContentLoaded", function () {
                var msgEl = document.createElement('div');
                msgEl.className = 'aj-topMsg';
                msgEl.setAttribute('v-html', "showText");
                document.body.appendChild(msgEl);
                aj.msg = new Vue({
                    el: msgEl,
                    data: { showText: '' },
                    methods: {
                        show: function (text, cfg) {
                            var _this = this;
                            this.showText = text;
                            var el = this.$el;
                            setTimeout(function () {
                                el.classList.remove('fadeOut');
                                el.classList.add('fadeIn');
                            }, 0);
                            setTimeout(function () {
                                el.classList.remove('fadeIn');
                                el.classList.add('fadeOut');
                                cfg && cfg.afterClose && cfg.afterClose(el, _this);
                            }, cfg && cfg.showTime || 3000);
                        }
                    }
                });
            });
        })(modal = widget.modal || (widget.modal = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";


/**
 * 调整正文字体大小
 */
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var page;
        (function (page) {
            var AdjustFontSize = /** @class */ (function (_super) {
                __extends(AdjustFontSize, _super);
                function AdjustFontSize() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = 'aj-adjust-font-size';
                    _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-adjust-font-size\">\n                <span>\u5B57\u4F53\u5927\u5C0F</span>\n                <ul @click=\"onClk\">\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5C0F</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u4E2D</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5927</label></li>\n                </ul>\n            </div>\n        "], ["\n            <div class=\"aj-adjust-font-size\">\n                <span>\u5B57\u4F53\u5927\u5C0F</span>\n                <ul @click=\"onClk\">\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5C0F</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u4E2D</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5927</label></li>\n                </ul>\n            </div>\n        "]));
                    _this.articleTarget = { type: String, default: 'article p' }; // 正文所在的位置，通过 CSS Selector 定位
                    return _this;
                }
                AdjustFontSize.prototype.onClk = function (ev) {
                    var _this = this;
                    var el = ev.target;
                    var setFontSize = function (fontSize) {
                        document.body.$(_this.$props.articleTarget, function (p) { return p.style.fontSize = fontSize; });
                    };
                    if (el.tagName == 'LABEL' || el.tagName == 'input') {
                        if (el.tagName != 'LABEL')
                            el = el.up('label');
                        if (el.innerHTML.indexOf('大') != -1)
                            setFontSize('12pt');
                        else if (el.innerHTML.indexOf('中') != -1)
                            setFontSize('10.5pt');
                        else if (el.innerHTML.indexOf('小') != -1)
                            setFontSize('9pt');
                    }
                };
                return AdjustFontSize;
            }(aj.VueComponent));
            page.AdjustFontSize = AdjustFontSize;
            new AdjustFontSize().register();
        })(page = widget.page || (widget.page = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var back2topTimerId = 0;
        /**
         *  回到顶部  <a href="###" @click="go">回到顶部</a>
         */
        function back2top() {
            var top = 0, speed = 0;
            back2topTimerId && window.clearInterval(back2topTimerId);
            back2topTimerId = window.setInterval(function () {
                top = document.documentElement.scrollTop || document.body.scrollTop;
                speed = Math.floor((0 - top) / 8);
                if (top === 0)
                    clearInterval(back2topTimerId);
                else
                    document.documentElement.scrollTop = document.body.scrollTop = top + speed;
            }, 20);
        }
        widget.back2top = back2top;
        /**
         * 渲染浮动的按钮
         */
        function initBack2top() {
            var vue = new Vue({
                el: document.body.appendChild(document.createElement('div')),
                template: '<div @click="clk" class="aj-widget-back2top" title="回到顶部"><i class="fa fa-arrow-up" aria-hidden="true"></i> </div>',
                methods: {
                    clk: back2top
                }
            });
            var handler = aj.throttle(function () {
                vue.$el.style.top = (document.body.scrollTop + 100) + "px";
            }, 2000, 0);
            // @ts-ignore
            window.addEventListener('scroll', handler);
            handler();
        }
        widget.initBack2top = initBack2top;
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var page;
        (function (page) {
            var TraditionalChinese;
            (function (TraditionalChinese_1) {
                /*
                    正体中文
                    <span>
                         <a href="javascript:;" onclick="aj.widget.page.TraditionalChinese.toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>
                        /<a href="javascript:;" onclick="aj.widget.page.TraditionalChinese.toChinese(this);" class="Chinese">正体中文</a>
                    </span>
                */
                var TraditionalChinese = 1, SimpleChinese = 2;
                /**
                 *
                 * 默认是否正体中文：true为正体；false简体。 HTTP
                 * 头读取：Request.ServerVariables("http_accept_language")
                 *
                 * @return {Boolean}
                 */
                function getClientLanguage() {
                    // @ts-ignore
                    var s = navigator.userLanguage || navigator.language;
                    switch (s.toLowerCase()) {
                        case 'zh-tw':
                            return TraditionalChinese;
                        case 'zh-cn':
                            return SimpleChinese;
                        default:
                            return 0;
                    }
                }
                var Cookie = {
                    set: function (name, val) {
                        var exp = new Date();
                        exp.setDate(exp.getDate() + 600 * 1000);
                        document.cookie = name + "=" + escape(val) + ";expires=" + exp.toUTCString();
                    },
                    del: function (name) {
                        document.cookie = name + "=;expires=" + (new Date(0)).toUTCString();
                    },
                    get: function (name) {
                        var cookieArray = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
                        return cookieArray != null ? unescape(cookieArray[2]) : "";
                    }
                };
                /**
                 * 转换对象，使用递归，逐层剥到文本
                 *
                 * @param {HTMLElement} obj 从document.body开始，
                 */
                function walk(el, coverntFn) {
                    var chidlNodes = el.childNodes;
                    var node;
                    for (var i = 0, j = chidlNodes.length; i < j; i++) {
                        node = chidlNodes.item(i);
                        // || (node == $$.big5.el)
                        if (("||BR|HR|TEXTAREA|".indexOf("|" + node.tagName + "|")) > 0)
                            continue;
                        if (node.title)
                            node.title = coverntFn(node.title);
                        // @ts-ignore
                        else if (node.alt)
                            // @ts-ignore
                            node.alt = coverntFn(node.alt);
                        // @ts-ignore
                        else if (node.tagName == "INPUT" && node.value != "" && node.type != "text" && node.type != "hidden")
                            // @ts-ignore
                            node.value = coverntFn(node.value);
                        else if (node.nodeType == 3)
                            // @ts-ignore
                            node.data = coverntFn(node.data);
                        else
                            walk(node, coverntFn);
                    }
                }
                /**
                 * 一对一替换字符
                 *
                 * @param text      输入的文本
                 * @param oldChars  原来所在的字符集
                 * @param newChars  要替换到的新字符集
                 */
                function translateText(text, oldChars, newChars) {
                    var str = [];
                    var char, charIndex, result;
                    for (var i = 0, j = text.length; i < j; i++) {
                        char = text.charAt(i);
                        charIndex = oldChars.indexOf(char);
                        result = newChars.charAt(charIndex);
                        str.push(charIndex != -1 ? result : char); // 匹配不到，用原来的字符
                    }
                    return str.join('');
                }
                var isLoaded = false;
                function loadChars(cb, el) {
                    var _a;
                    if (isLoaded)
                        cb();
                    else
                        aj.loadScript("https://framework.ajaxjs.com/src/widget/page/ChineseChars.js", "", function () {
                            isLoaded = true;
                            cb();
                        });
                    ((_a = el.up('span')) === null || _a === void 0 ? void 0 : _a.$(aj.SELECTED_CSS)).classList.remove(aj.SELECTED);
                    el.classList.add(aj.SELECTED);
                }
                var self = aj.widget.page.TraditionalChinese;
                var currentLanguageState; // 当前语言选择
                var cookieName = 'ChineseType';
                currentLanguageState = getClientLanguage();
                function toSimpleChinese(el) {
                    if (currentLanguageState === SimpleChinese) // 已经是，无须进行
                        return;
                    loadChars(function () {
                        //@ts-ignore
                        walk(document.body, translateText.delegate(null, self.正体中文, self.简化中文));
                        currentLanguageState = SimpleChinese;
                        Cookie.set(cookieName, currentLanguageState + "");
                    }, el);
                }
                TraditionalChinese_1.toSimpleChinese = toSimpleChinese;
                function toChinese(el) {
                    if (currentLanguageState === TraditionalChinese) // 已经是，无须进行
                        return;
                    loadChars(function () {
                        //@ts-ignore
                        walk(document.body, translateText.delegate(null, self.简化中文, self.正体中文));
                        currentLanguageState = TraditionalChinese;
                        Cookie.set(cookieName, currentLanguageState + "");
                    }, el);
                }
                TraditionalChinese_1.toChinese = toChinese;
                /**
                 * 初始化
                 */
                function init() {
                    var valueInCookie = Cookie.get(cookieName);
                    if (valueInCookie)
                        valueInCookie = Number(valueInCookie);
                    // 浏览器是繁体中文的，或者 Cookie 设置了是正体的，进行转换（当然默认文本是简体的）
                    if (currentLanguageState == TraditionalChinese || valueInCookie == TraditionalChinese)
                        toChinese(document.querySelector(".Chinese"));
                }
                TraditionalChinese_1.init = init;
            })(TraditionalChinese = page.TraditionalChinese || (page.TraditionalChinese = {}));
        })(page = widget.page || (widget.page = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";

/**
 * 起始时间、截止时间的范围选择
 */
Vue.component('aj-form-between-date', {
    template: html(__makeTemplateObject(["\n        <form action=\".\" method=\"GET\" class=\"dateRange\" @submit=\"valid\">\n            <aj-form-calendar-input :date-only=\"true\" :position-fixed=\"true\" placeholder=\"\u8D77\u59CB\u65F6\u95F4\" field-name=\"startDate\" ></aj-form-calendar-input>\n            - <aj-form-calendar-input :date-only=\"true\" :position-fixed=\"true\" placeholder=\"\u622A\u81F3\u65F6\u95F4\" field-name=\"endDate\"></aj-form-calendar-input>\n            <button class=\"aj-btn\">\u6309\u65F6\u95F4\u7B5B\u9009</button>\n        </form>    \n    "], ["\n        <form action=\".\" method=\"GET\" class=\"dateRange\" @submit=\"valid\">\n            <aj-form-calendar-input :date-only=\"true\" :position-fixed=\"true\" placeholder=\"\u8D77\u59CB\u65F6\u95F4\" field-name=\"startDate\" ></aj-form-calendar-input>\n            - <aj-form-calendar-input :date-only=\"true\" :position-fixed=\"true\" placeholder=\"\u622A\u81F3\u65F6\u95F4\" field-name=\"endDate\"></aj-form-calendar-input>\n            <button class=\"aj-btn\">\u6309\u65F6\u95F4\u7B5B\u9009</button>\n        </form>    \n    "])),
    props: {
        isAjax: { type: Boolean, default: true } // 是否 AJAX 模式
    },
    methods: {
        valid: function (ev) {
            var start = this.$el.$('input[name=startDate]').value, end = this.$el.$('input[name=endDate]').value;
            if (!start || !end) {
                aj.alert("输入数据不能为空");
                ev.preventDefault();
                return;
            }
            if (new Date(start) > new Date(end)) {
                aj.alert("起始日期不能晚于结束日期");
                ev.preventDefault();
                return;
            }
            //@ts-ignore
            if (this.isAjax) {
                ev.preventDefault();
                var grid = this.$parent.$parent;
                aj.apply(grid.$refs.pager.extraParam, {
                    startDate: start, endDate: end
                });
                grid.reload();
            }
        }
    }
});

"use strict";

Vue.component('aj-form-calendar-input', {
    template: html(__makeTemplateObject(["\n        <div class=\"aj-form-calendar-input\" :class=\"{'show-time': showTime}\" @mouseover=\"onMouseOver\">\n            <div class=\"icon fa fa-calendar\"></div>\n            <input :placeholder=\"placeholder\" size=\"12\" :name=\"fieldName\" :value=\"date + (dateOnly ? '' : ' ' + time)\"\n                type=\"text\" autocomplete=\"off\" />\n            <aj-form-calendar ref=\"calendar\" :show-time=\"showTime\" @pick-date=\"recEvent\" @pick-time=\"recTimeEvent\">\n            </aj-form-calendar>\n        </div>\n    "], ["\n        <div class=\"aj-form-calendar-input\" :class=\"{'show-time': showTime}\" @mouseover=\"onMouseOver\">\n            <div class=\"icon fa fa-calendar\"></div>\n            <input :placeholder=\"placeholder\" size=\"12\" :name=\"fieldName\" :value=\"date + (dateOnly ? '' : ' ' + time)\"\n                type=\"text\" autocomplete=\"off\" />\n            <aj-form-calendar ref=\"calendar\" :show-time=\"showTime\" @pick-date=\"recEvent\" @pick-time=\"recTimeEvent\">\n            </aj-form-calendar>\n        </div>\n    "])),
    data: function () {
        return {
            date: this.fieldValue,
            time: ''
        };
    },
    props: {
        fieldName: { type: String, required: true },
        fieldValue: { type: String, required: false, default: '' },
        dateOnly: { type: Boolean, required: false, default: true },
        showTime: false,
        positionFixed: Boolean,
        placeholder: { type: String, default: '请输入日期' } // 提示文字
    },
    watch: {
        fieldValue: function (n) {
            this.date = n;
        }
    },
    mounted: function () {
        if (this.positionFixed) {
            var el = this.$el.$('.aj-form-calendar');
            el.classList.add('positionFixed');
        }
        // 2012-07-08
        // firefox中解析 new Date('2012/12-23') 不兼容，提示invalid date 无效的日期
        // Chrome下可以直接将其格式化成日期格式，且时分秒默认为零
        // var arr = date.split('-'), now = new Date(arr[0], arr[1] - 1, arr[2],
        // " ", "", " ");
        if (this.fieldValue) {
            var arr = this.fieldValue.split(' ')[0], _arr = arr.split('-');
            // @ts-ignore
            this.$refs.calendar.date = new Date(arr[0], arr[1] - 1, arr[2], " ", "", " ");
        }
    },
    methods: {
        recTimeEvent: function (time) {
            this.time = time;
        },
        recEvent: function (date) {
            this.date = date.trim();
        },
        onMouseOver: function (ev) {
            if (this.positionFixed) {
                var el = ev.currentTarget, b = el.getBoundingClientRect(), c = this.$el.$('.aj-form-calendar');
                c.style.top = (b.top + el.clientHeight - 0) + 'px';
                c.style.left = ((b.left - 0) + 0) + 'px';
            }
        }
    }
});

"use strict";


var aj;
(function (aj) {
    var form;
    (function (form) {
        /**
         * 日期选择器
         */
        var Calendar = /** @class */ (function (_super) {
            __extends(Calendar, _super);
            function Calendar() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-form-calendar";
                _this.template = html(__makeTemplateObject(["\n        <div class=\"aj-form-calendar\">\n            <div class=\"selectYearMonth\">\n                <a href=\"###\" @click=\"getDate('preYear')\" class=\"preYear\" title=\"\u4E0A\u4E00\u5E74\">&lt;</a>\n                <select @change=\"setMonth\" v-model=\"month\">\n                    <option value=\"1\">\u4E00\u6708</option>\n                    <option value=\"2\">\u4E8C\u6708</option>\n                    <option value=\"3\">\u4E09\u6708</option>\n                    <option value=\"4\">\u56DB\u6708</option>\n                    <option value=\"5\">\u4E94\u6708</option>\n                    <option value=\"6\">\u516D\u6708</option>\n                    <option value=\"7\">\u4E03\u6708</option>\n                    <option value=\"8\">\u516B\u6708</option>\n                    <option value=\"9\">\u4E5D\u6708</option>\n                    <option value=\"10\">\u5341\u6708</option>\n                    <option value=\"11\">\u5341\u4E00\u6708</option>\n                    <option value=\"12\">\u5341\u4E8C\u6708</option>\n                </select>\n                <a href=\"###\" @click=\"getDate('nextYear')\" class=\"nextYear\" title=\"\u4E0B\u4E00\u5E74\">&gt;</a>\n            </div>\n            <div class=\"showCurrentYearMonth\">\n                <span class=\"showYear\">{{year}}</span>/<span class=\"showMonth\">{{month}}</span>\n            </div>\n            <table>\n                <thead>\n                    <tr>\n                        <td>\u65E5</td>\n                        <td>\u4E00</td>\n                        <td>\u4E8C</td>\n                        <td>\u4E09</td>\n                        <td>\u56DB</td>\n                        <td>\u4E94</td>\n                        <td>\u516D</td>\n                    </tr>\n                </thead>\n                <tbody @click=\"pickDay\"></tbody>\n            </table>\n            <div v-if=\"showTime\" class=\"showTime\">\n                \u65F6 <select class=\"hour aj-select\">\n                    <option v-for=\"n in 24\">{{n}}</option>\n                </select>\n                \u5206 <select class=\"minute aj-select\">\n                    <option v-for=\"n in 61\">{{n - 1}}</option>\n                </select>\n                <a href=\"#\" @click=\"pickupTime\">\u9009\u62E9\u65F6\u95F4</a>\n            </div>\n        </div>"], ["\n        <div class=\"aj-form-calendar\">\n            <div class=\"selectYearMonth\">\n                <a href=\"###\" @click=\"getDate('preYear')\" class=\"preYear\" title=\"\u4E0A\u4E00\u5E74\">&lt;</a>\n                <select @change=\"setMonth\" v-model=\"month\">\n                    <option value=\"1\">\u4E00\u6708</option>\n                    <option value=\"2\">\u4E8C\u6708</option>\n                    <option value=\"3\">\u4E09\u6708</option>\n                    <option value=\"4\">\u56DB\u6708</option>\n                    <option value=\"5\">\u4E94\u6708</option>\n                    <option value=\"6\">\u516D\u6708</option>\n                    <option value=\"7\">\u4E03\u6708</option>\n                    <option value=\"8\">\u516B\u6708</option>\n                    <option value=\"9\">\u4E5D\u6708</option>\n                    <option value=\"10\">\u5341\u6708</option>\n                    <option value=\"11\">\u5341\u4E00\u6708</option>\n                    <option value=\"12\">\u5341\u4E8C\u6708</option>\n                </select>\n                <a href=\"###\" @click=\"getDate('nextYear')\" class=\"nextYear\" title=\"\u4E0B\u4E00\u5E74\">&gt;</a>\n            </div>\n            <div class=\"showCurrentYearMonth\">\n                <span class=\"showYear\">{{year}}</span>/<span class=\"showMonth\">{{month}}</span>\n            </div>\n            <table>\n                <thead>\n                    <tr>\n                        <td>\u65E5</td>\n                        <td>\u4E00</td>\n                        <td>\u4E8C</td>\n                        <td>\u4E09</td>\n                        <td>\u56DB</td>\n                        <td>\u4E94</td>\n                        <td>\u516D</td>\n                    </tr>\n                </thead>\n                <tbody @click=\"pickDay\"></tbody>\n            </table>\n            <div v-if=\"showTime\" class=\"showTime\">\n                \u65F6 <select class=\"hour aj-select\">\n                    <option v-for=\"n in 24\">{{n}}</option>\n                </select>\n                \u5206 <select class=\"minute aj-select\">\n                    <option v-for=\"n in 61\">{{n - 1}}</option>\n                </select>\n                <a href=\"#\" @click=\"pickupTime\">\u9009\u62E9\u65F6\u95F4</a>\n            </div>\n        </div>"]));
                _this.watch = {
                    date: function () {
                        this.year = this.date.getFullYear();
                        this.month = this.date.getMonth() + 1;
                        this.render();
                    }
                };
                _this.showTime = Boolean; // 是否显示时间，即“时分秒”
                _this.date = new Date();
                _this.year = new Date().getFullYear();
                _this.month = new Date().getMonth() + 1;
                _this.day = 1;
                return _this;
            }
            Calendar.prototype.data = function () {
                var date = new Date;
                return {
                    date: date,
                    year: date.getFullYear(),
                    month: date.getMonth() + 1,
                    day: 1
                };
            };
            Calendar.prototype.mounted = function () {
                this.$options.watch.date.call(this);
            };
            /**
             * 画日历
             */
            Calendar.prototype.render = function () {
                var arr = this.getDateArr(), // 用来保存日期列表
                frag = document.createDocumentFragment(); // 插入日期
                while (arr.length) {
                    var row = document.createElement("tr"); // 每个星期插入一个 tr
                    for (var i = 1; i <= 7; i++) { // 每个星期有7天
                        var cell = document.createElement("td");
                        if (arr.length) {
                            var d = arr.shift();
                            if (d) {
                                var text = this.year + '-' + this.month + '-' + d;
                                cell.title = text; // 保存日期在 title 属性
                                cell.className = 'day day_' + text;
                                cell.innerHTML = d + "";
                                var on = new Date(this.year, this.month - 1, d);
                                // 判断是否今日
                                if (isSameDay(on, this.date)) {
                                    cell.classList.add('onToday');
                                    // this.onToday && this.onToday(cell);// 点击 今天 时候触发的事件
                                }
                                // 判断是否选择日期
                                // this.selectDay && this.onSelectDay && this.isSameDay(on, this.selectDay) && this.onSelectDay(cell);
                            }
                        }
                        row.appendChild(cell);
                    }
                    frag.appendChild(row);
                }
                // 先清空内容再插入
                var tbody = this.$el.$("table tbody");
                tbody.innerHTML = '';
                tbody.appendChild(frag);
            };
            /**
             * 获取指定的日期
             *
             * @param dateType
             * @param month
             */
            Calendar.prototype.getDate = function (dateType, month) {
                var nowYear = this.date.getFullYear(), nowMonth = this.date.getMonth() + 1; // 当前日期
                switch (dateType) {
                    case 'preMonth': // 上一月
                        this.date = new Date(nowYear, nowMonth - 2, 1);
                        break;
                    case 'nextMonth': // 下一月
                        this.date = new Date(nowYear, nowMonth, 1);
                        break;
                    case 'setMonth': // 指定月份
                        this.date = new Date(nowYear, month - 1, 1);
                        break;
                    case 'preYear': // 上一年
                        this.date = new Date(nowYear - 1, nowMonth - 1, 1);
                        break;
                    case 'nextYear': // 下一年
                        this.date = new Date(nowYear + 1, nowMonth - 1, 1);
                        break;
                }
            };
            /**
             *
             *
             * @param $even
             */
            Calendar.prototype.setMonth = function (ev) {
                var el = ev.target;
                this.getDate('setMonth', Number(el.selectedOptions[0].value));
            };
            /**
             * 获取空白的非上月天数 + 当月天数
             *
             * @param this
             */
            Calendar.prototype.getDateArr = function () {
                var arr = [];
                // 算出这个月1号距离前面的星期天有多少天
                for (var i = 1, firstDay = new Date(this.year, this.month - 1, 1).getDay(); i <= firstDay; i++)
                    arr.push(0);
                // 这个月有多少天。用上个月然后设置日子参数为 0，就可以得到本月有多天
                for (var i = 1, monthDay = new Date(this.year, this.month, 0).getDate(); i <= monthDay; i++)
                    arr.push(i);
                return arr;
            };
            /**
             * 获取日期
             *
             * @param $event
             */
            Calendar.prototype.pickDay = function (ev) {
                var el = ev.target, date = el.title;
                this.$emit('pick-date', date);
                return date;
            };
            /**
             *
             * @param $event
             */
            Calendar.prototype.pickupTime = function ($event) {
                var hour = this.$el.$('.hour'), minute = this.$el.$('.minute'), time = hour.selectedOptions[0].value + ':' + minute.selectedOptions[0].value;
                this.$emit('pick-time', time);
            };
            return Calendar;
        }(aj.VueComponent));
        form.Calendar = Calendar;
        /**
         * 判断两个日期是否同一日
         *
         * @param d1
         * @param d2
         */
        function isSameDay(d1, d2) {
            return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
        }
        new Calendar().register();
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var form;
    (function (form) {
        /**
         * 全国省市区
         * 写死属性
         */
        var ChinaArea = /** @class */ (function (_super) {
            __extends(ChinaArea, _super);
            function ChinaArea() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-china-area";
                _this.template = html(__makeTemplateObject(["\n\t\t\t<div class=\"aj-china-area\">\n\t\t\t\t<span>\u7701</span>\n\t\t\t\t<select v-model=\"province\" class=\"aj-select\" style=\"width:120px;\" :name=\"provinceName || 'locationProvince'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in addressData[86]\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u5E02 </span>\n\t\t\t\t<select v-model=\"city\" class=\"aj-select\" style=\"width:120px;\" :name=\"cityName || 'locationCity'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in citys\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u533A</span>\n\t\t\t\t<select v-model=\"district\" class=\"aj-select\" style=\"width:120px;\" :name=\"districtName || 'locationDistrict'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in districts\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t</div>\n\t\t"], ["\n\t\t\t<div class=\"aj-china-area\">\n\t\t\t\t<span>\u7701</span>\n\t\t\t\t<select v-model=\"province\" class=\"aj-select\" style=\"width:120px;\" :name=\"provinceName || 'locationProvince'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in addressData[86]\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u5E02 </span>\n\t\t\t\t<select v-model=\"city\" class=\"aj-select\" style=\"width:120px;\" :name=\"cityName || 'locationCity'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in citys\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t\t<span>\u533A</span>\n\t\t\t\t<select v-model=\"district\" class=\"aj-select\" style=\"width:120px;\" :name=\"districtName || 'locationDistrict'\">\n\t\t\t\t\t<option value=\"\">\u8BF7\u9009\u62E9</option>\n\t\t\t\t\t<option v-for=\"(v, k) in districts\" :value=\"k\">{{v}}</option>\n\t\t\t\t</select>\n\t\t\t</div>\n\t\t"]));
                _this.provinceCode = String;
                _this.cityCode = String;
                _this.districtCode = String;
                _this.provinceName = String;
                _this.cityName = String;
                _this.districtName = String;
                _this.computed = {
                    citys: function () {
                        if (!this.province)
                            return {};
                        return this.addressData[this.province];
                    },
                    districts: function () {
                        if (!this.city)
                            return {};
                        return this.addressData[this.city];
                    }
                };
                return _this;
            }
            ChinaArea.prototype.data = function () {
                return {
                    province: this.provinceCode || '',
                    city: this.cityCode || '',
                    district: this.districtCode || '',
                    //@ts-ignore
                    addressData: window.China_AREA
                };
            };
            // watch = { // 令下一级修改
            // 	province(val: any, oldval: any) {
            // 		if (val !== oldval)
            // 			this.city = '';
            // 	},
            // 	city(val: any, oldval: any) {
            // 		if (val !== oldval)
            // 			this.district = '';
            // 	}
            // };
            ChinaArea.prototype.mounted = function () {
                //@ts-ignore
                if (!window.China_AREA)
                    throw '中国行政区域数据 脚本没导入';
            };
            return ChinaArea;
        }(aj.VueComponent));
        form.ChinaArea = ChinaArea;
        new ChinaArea().register();
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";

var aj;
(function (aj) {
    var form;
    (function (form) {
        Vue.component('aj-edit-form', {
            template: html(__makeTemplateObject(["\n            <form class=\"aj-table-form\" :action=\"getInfoApi + (isCreate ? '' : info.id + '/')\" :method=\"isCreate ? 'POST' : 'PUT'\">\n                <h3>{{isCreate ? \"\u65B0\u5EFA\" : \"\u7F16\u8F91\" }}{{uiName}}</h3>\n                <!-- \u4F20\u9001 id \u53C2\u6570 -->\n                <input v-if=\"!isCreate\" type=\"hidden\" name=\"id\" :value=\"info.id\" />\n                <slot v-bind:info=\"info\"></slot>\n                <div class=\"aj-btnsHolder\">\n                    <button><img :src=\"ajResources.commonAsset + '/icon/save.gif'\" /> {{isCreate ? \"\u65B0\u5EFA\":\"\u4FDD\u5B58\"}}</button>\n                    <button onclick=\"this.up('form').reset();return false;\">\u590D \u4F4D</button>\n                    <button v-if=\"!isCreate\" v-on:click.prevent=\"del()\">\n                        <img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" /> \u5220 \u9664\n                    </button>\n                    <button @click.prevent=\"close\">\u5173\u95ED</button>\n                </div>\n            </form>\n        "], ["\n            <form class=\"aj-table-form\" :action=\"getInfoApi + (isCreate ? '' : info.id + '/')\" :method=\"isCreate ? 'POST' : 'PUT'\">\n                <h3>{{isCreate ? \"\u65B0\u5EFA\" : \"\u7F16\u8F91\" }}{{uiName}}</h3>\n                <!-- \u4F20\u9001 id \u53C2\u6570 -->\n                <input v-if=\"!isCreate\" type=\"hidden\" name=\"id\" :value=\"info.id\" />\n                <slot v-bind:info=\"info\"></slot>\n                <div class=\"aj-btnsHolder\">\n                    <button><img :src=\"ajResources.commonAsset + '/icon/save.gif'\" /> {{isCreate ? \"\u65B0\u5EFA\":\"\u4FDD\u5B58\"}}</button>\n                    <button onclick=\"this.up(\\'form\\').reset();return false;\">\u590D \u4F4D</button>\n                    <button v-if=\"!isCreate\" v-on:click.prevent=\"del()\">\n                        <img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" /> \u5220 \u9664\n                    </button>\n                    <button @click.prevent=\"close\">\u5173\u95ED</button>\n                </div>\n            </form>\n        "])),
            props: {
                isCreate: Boolean,
                uiName: String,
                apiUrl: { type: String, required: true } // 获取实体详情的接口地址 
            },
            data: function () {
                return {
                    id: 0,
                    info: {},
                };
            },
            mounted: function () {
                var _this = this;
                aj.xhr.form(this.$el, function (j) {
                    if (j) {
                        if (j.isOk) {
                            var msg_1 = (_this.isCreate ? "新建" : "保存") + _this.uiName + "成功";
                            aj.msg.show(msg_1);
                            _this.$parent.close();
                        }
                        else
                            aj.msg.show(j.msg);
                    }
                });
            },
            methods: {
                load: function (id, cb) {
                    var _this = this;
                    this.id = id;
                    aj.xhr.get(this.apiUrl + id + "/", function (j) {
                        _this.info = j.result;
                        cb && cb(j);
                    });
                },
                close: function () {
                    if (this.$parent.$options._componentTag === 'aj-layer')
                        //@ts-ignore
                        this.$parent.close();
                    else
                        history.back();
                },
                /**
                 * 执行删除
                 *
                 * @param this
                 */
                del: function () {
                    var id = form.utils.getFormFieldValue(this.$el, 'input[name=id]'), title = form.utils.getFormFieldValue(this.$el, 'input[name=name]');
                    aj.showConfirm("\u8BF7\u786E\u5B9A\u5220\u9664\u8BB0\u5F55\uFF1A\n" + title + "\uFF1F", function () {
                        return aj.xhr.dele("../" + id + "/", function (j) {
                            if (j.isOk) {
                                aj.msg.show('删除成功！');
                                //setTimeout(() => location.reload(), 1500);
                            }
                            else
                                aj.alert('删除失败！');
                        });
                    });
                }
            }
        });
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var xhr_upload;
    (function (xhr_upload) {
        /**
         * 属性较多，设一个抽象类
         */
        var BaseFileUploader = /** @class */ (function (_super) {
            __extends(BaseFileUploader, _super);
            function BaseFileUploader() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.fieldName = "";
                _this.fieldValue = "";
                /**
                 * 不重复的 id，用关于关联 label 与 input[type=file]
                 */
                _this.radomId = 0;
                /**
                 * 上传路径，必填
                 */
                _this.action = "";
                /**
                 * 允许文件选择器列出的文件类型
                 */
                _this.accpectFileType = "";
                /**
                 * 限制的文件扩展名，这是一个正则。如无限制，不设置或者空字符串
                 * 正则如：txt|pdf|doc
                 */
                _this.limitFileType = "";
                /**
                 * 文件大小
                 */
                _this.fileSize = 0;
                /**
                 * 获取文件名称，只能是名称，不能获取完整的文件目录
                 */
                _this.fileName = '';
                /**
                 * 文件对象，实例属性
                 */
                _this.$fileObj = null;
                /**
                 * 二进制数据，用于图片预览
                 */
                _this.$blob = null;
                /**
                 * 上传按钮是否位于下方
                 */
                _this.buttonBottom = false;
                /**
                 * 文件大小限制，单位：KB。
                 * 若为 0 则不限制
                 */
                _this.limitSize = 0;
                /**
                 * 上传进度百分比
                 */
                _this.progress = 0;
                /**
                 * 错误信息。约定：只有为空字符串，才表示允许上传。
                 */
                _this.errMsg = "init";
                /**
                 * 固定的错误结构，元素[0]为文件大小，[1]为文件类型。
                 * 如果元素非 true，表示不允许上传。
                 */
                _this.errStatus = [false, false];
                /**
                 * 成功上传之后的文件 id
                 */
                _this.newlyId = "";
                /**
                 * 上传之后的回调函数
                 */
                _this.$uploadOk_callback = function (json) {
                    if (json.isOk)
                        this.fieldValue = json.imgUrl;
                    aj.xhr.defaultCallBack(json);
                };
                return _this;
            }
            return BaseFileUploader;
        }(aj.VueComponent));
        /**
         * 文件上传器
         */
        var FileUploader = /** @class */ (function (_super) {
            __extends(FileUploader, _super);
            function FileUploader() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-file-uploader";
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-file-uploader\">\n                <input type=\"hidden\" :name=\"fieldName\" :value=\"fieldValue\" />\n                <input type=\"file\" :id=\"'uploadInput_' + radomId\" @change=\"onUploadInputChange\" :accept=\"accpectFileType\" />\n            \n                <label class=\"pseudoFilePicker\" :for=\"'uploadInput_' + radomId\">\n                    <div @drop=\"onDrop\" ondragover=\"event.preventDefault();\">\n                        <div>+</div>\u70B9\u51FB\u9009\u62E9\u6587\u4EF6<br />\u6216\u62D6\u653E\u5230\u6B64\n                    </div>\n                </label>\n            \n                <div class=\"msg\" v-if=\"errMsg != ''\">\n                    \u5141\u8BB8\u7C7B\u578B\uFF1A{{limitFileType || '\u65E0\u9650\u5236'}}\n                    <br />\n                    \u5141\u8BB8\u5927\u5C0F\uFF1A{{limitSize ? changeByte(limitSize * 1024) : '\u65E0\u9650\u5236'}}\n                    <span class=\"slot\"></span>\n                </div>\n                <div class=\"msg\" v-if=\"errMsg == ''\">\n                    {{fileName}}<div v-if=\"fileSize\">{{changeByte(fileSize)}}</div>\n                    <button @click.prevent=\"doUpload\">{{progress && progress !== 100 ? '\u4E0A\u4F20\u4E2D ' + progress + '%': '\u4E0A\u4F20'}}</button>\n                </div>\n            </div>\n        "], ["\n            <div class=\"aj-file-uploader\">\n                <input type=\"hidden\" :name=\"fieldName\" :value=\"fieldValue\" />\n                <input type=\"file\" :id=\"'uploadInput_' + radomId\" @change=\"onUploadInputChange\" :accept=\"accpectFileType\" />\n            \n                <label class=\"pseudoFilePicker\" :for=\"'uploadInput_' + radomId\">\n                    <div @drop=\"onDrop\" ondragover=\"event.preventDefault();\">\n                        <div>+</div>\u70B9\u51FB\u9009\u62E9\u6587\u4EF6<br />\u6216\u62D6\u653E\u5230\u6B64\n                    </div>\n                </label>\n            \n                <div class=\"msg\" v-if=\"errMsg != ''\">\n                    \u5141\u8BB8\u7C7B\u578B\uFF1A{{limitFileType || '\u65E0\u9650\u5236'}}\n                    <br />\n                    \u5141\u8BB8\u5927\u5C0F\uFF1A{{limitSize ? changeByte(limitSize * 1024) : '\u65E0\u9650\u5236'}}\n                    <span class=\"slot\"></span>\n                </div>\n                <div class=\"msg\" v-if=\"errMsg == ''\">\n                    {{fileName}}<div v-if=\"fileSize\">{{changeByte(fileSize)}}</div>\n                    <button @click.prevent=\"doUpload\">{{progress && progress !== 100 ? '\u4E0A\u4F20\u4E2D ' + progress + '%': '\u4E0A\u4F20'}}</button>\n                </div>\n            </div>\n        "]));
                _this.props = {
                    action: { type: String, required: true },
                    fieldName: String,
                    limitSize: { type: Number, default: 20000 },
                    limitFileType: String,
                    accpectFileType: String,
                    buttonBottom: Boolean,
                    radomId: { type: Number, default: function () { return Math.round(Math.random() * 1000); } }
                };
                _this.watch = {
                    fileName: function (newV) {
                        if (!this.limitFileType) { // 无限制，也不用检查，永远是 true
                            Vue.set(this.errStatus, 0, true);
                            return;
                        }
                        if (newV && this.limitFileType) {
                            var ext = newV.split('.').pop(); // 扩展名，fileInput.value.split('.').pop(); 也可以获取
                            if (!new RegExp(this.limitFileType, 'i').test(ext)) {
                                var msg_1 = "\u4E0A\u4F20\u6587\u4EF6\u4E3A " + newV + "\uFF0C<br />\u62B1\u6B49\uFF0C\u4E0D\u652F\u6301\u4E0A\u4F20 *." + ext + " \u7C7B\u578B\u6587\u4EF6";
                                Vue.set(this.errStatus, 0, msg_1);
                            }
                            else
                                Vue.set(this.errStatus, 0, true); // 检查通过
                        }
                    },
                    fileSize: function (newV) {
                        if (!this.limitSize) { // 无限制，也不用检查，永远是 true
                            Vue.set(this.errStatus, 1, true);
                            return;
                        }
                        if (this.limitSize && newV > this.limitSize * 1024) {
                            var msg_2 = "\u8981\u4E0A\u4F20\u7684\u6587\u4EF6\u5BB9\u91CF\u8FC7\u5927(" + this.changeByte(newV) + ")\uFF0C\u8BF7\u538B\u7F29\u5230 " + this.changeByte(this.limitSize * 1024) + " \u4EE5\u4E0B";
                            Vue.set(this.errStatus, 1, msg_2);
                        }
                        else
                            Vue.set(this.errStatus, 1, true);
                    },
                    errStatus: function (newV) {
                        var j = newV.length;
                        if (!j)
                            return;
                        var msg = "";
                        for (var i = 0; i < j; i++) {
                            var err = newV[i];
                            if (err === false)
                                return; // 未检查完，退出
                            if (err && typeof err == 'string')
                                msg += err + '；<br/>';
                        }
                        // 到这步，所有检查完毕
                        if (msg) { // 有错误
                            aj.alert(msg);
                            this.errMsg = msg;
                        }
                        else { // 全部通过，复位
                            this.errMsg = "";
                            this.errStatus = [false, false];
                        }
                    }
                };
                return _this;
            }
            /**
             * 选择文件后触发的事件
             *
             * @param ev
             */
            FileUploader.prototype.onUploadInputChange = function (ev) {
                var fileInput = ev.target;
                if (!fileInput.files || !fileInput.files[0])
                    return;
                // this.errStatus = [false, false, false];
                this.onFileGet(fileInput.files);
            };
            FileUploader.prototype.onDrop = function (ev) {
                var _a;
                ev.preventDefault(); // 阻止进行拖拽时浏览器的默认行为，即自动打开图片
                if ((_a = ev.dataTransfer) === null || _a === void 0 ? void 0 : _a.files)
                    this.onFileGet(ev.dataTransfer.files);
            };
            FileUploader.prototype.onFileGet = function (files) {
                var file = files[0], fileType = file.type;
                this.$fileObj = file;
                this.fileName = file.name;
                this.fileSize = file.size;
            };
            /**
             * 字节 Byte 转化成 KB，MB，GB
             *
             * @param limit
             */
            FileUploader.prototype.changeByte = function (limit) {
                var size = "";
                if (limit < 0.1 * 1024) // 小于 0.1KB，则转化成 B
                    size = limit.toFixed(2) + "B";
                else if (limit < 0.1 * 1024 * 1024) // 小于 0.1MB，则转化成 KB
                    size = (limit / 1024).toFixed(2) + "KB";
                else if (limit < 0.1 * 1024 * 1024 * 1024) // 小于 0.1GB，则转化成 MB
                    size = (limit / (1024 * 1024)).toFixed(2) + "MB";
                else // 其他转化成 GB
                    size = (limit / (1024 * 1024 * 1024)).toFixed(2) + "GB";
                var index = size.indexOf("."); // 获取小数点处的索引
                if (size.substr(index + 1, 2) == "00") // 获取小数点后两位的值，判断后两位是否为 00，如果是则删除 00                
                    return size.substring(0, index) + size.substr(index + 3, 2);
                return size;
            };
            /**
             * 执行上传
             *
             * @param this
             */
            FileUploader.prototype.doUpload = function () {
                // this.$uploadOk_callback({ isOk: true, msg: "ok!", imgUrl: "fdfdf" });
                // return;
                var _this = this;
                var fd = new FormData();
                if (this.$blob)
                    fd.append("file", this.$blob, this.fileName);
                else if (this.$fileObj)
                    fd.append("file", this.$fileObj);
                var xhr = new XMLHttpRequest();
                //@ts-ignore
                xhr.onreadystatechange = aj.xhr.requestHandler.delegate(null, this.$uploadOk_callback, 'json');
                xhr.open("POST", this.action, true);
                xhr.onprogress = function (ev) {
                    var progress = 0, p = ~~(ev.loaded * 1000 / ev.total);
                    p = p / 10;
                    if (progress !== p)
                        progress = p;
                    _this.progress = progress;
                };
                xhr.send(fd);
            };
            return FileUploader;
        }(BaseFileUploader));
        xhr_upload.FileUploader = FileUploader;
        /**
         * 用于继承，获取方法句柄
         */
        xhr_upload.fileUploader = new FileUploader();
        xhr_upload.fileUploader.register();
    })(xhr_upload = aj.xhr_upload || (aj.xhr_upload = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var form;
    (function (form) {
        /**
         * HTML 在綫編輯器
         *
         * 注意：必须提供一个 <slot> 包含有 <textarea class="hide" name="content">${info.content}</textarea>
         */
        var HtmlEditor = /** @class */ (function (_super) {
            __extends(HtmlEditor, _super);
            function HtmlEditor() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-form-html-editor";
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-form-html-editor\">\n                <ul class=\"toolbar\" @click=\"onToolBarClk\">\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\" class=\"fa-font\"></i>\n                        <div class=\"fontfamilyChoser\" @click=\"onFontfamilyChoserClk\">\n                            <a style=\"font-family: '\u5B8B\u4F53'\">\u5B8B\u4F53</a>\n                            <a style=\"font-family: '\u9ED1\u4F53'\">\u9ED1\u4F53</a>\n                            <a style=\"font-family: '\u6977\u4F53'\">\u6977\u4F53</a>\n                            <a style=\"font-family: '\u96B6\u4E66'\">\u96B6\u4E66</a>\n                            <a style=\"font-family: '\u5E7C\u5706'\">\u5E7C\u5706</a>\n                            <a style=\"font-family: 'Microsoft YaHei'\">Microsoft YaHei</a>\n                            <a style=\"font-family: Arial\">Arial</a>\n                            <a style=\"font-family: 'Arial Narrow'\">Arial Narrow</a>\n                            <a style=\"font-family: 'Arial Black'\">Arial Black</a>\n                            <a style=\"font-family: 'Comic Sans MS'\">Comic Sans MS</a>\n                            <a style=\"font-family: Courier\">Courier</a>\n                            <a style=\"font-family: System\">System</a>\n                            <a style=\"font-family: 'Times New Roman'\">Times New Roman</a>\n                            <a style=\"font-family: Verdana\">Verdana</a>\n                        </div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u53F7\" class=\"fa-header\"></i>\n                        <div class=\"fontsizeChoser\" @click=\"onFontsizeChoserClk\">\n                            <a style=\"font-size: xx-small; \">\u6781\u5C0F</a>\n                            <a style=\"font-size: x-small;  \">\u7279\u5C0F</a>\n                            <a style=\"font-size: small;    \">\u5C0F</a>\n                            <a style=\"font-size: medium;   \">\u4E2D</a>\n                            <a style=\"font-size: large;    \">\u5927</a>\n                            <a style=\"font-size: x-large;  \">\u7279\u5927</a>\n                            <a style=\"font-size: xx-large; line-height: 140%\">\u6781\u5927</a>\n                        </div>\n                    </li>\n                    <li><i title=\"\u52A0\u7C97\" class=\"bold fa-bold\"></i></li>\n                    <li><i title=\"\u659C\u4F53\" class=\"italic fa-italic\"></i></li>\n                    <li><i title=\"\u4E0B\u5212\u7EBF\" class=\"underline fa-underline\"></i></li>\n                    <li><i title=\"\u5DE6\u5BF9\u9F50\" class=\"justifyleft fa-align-left\"></i></li>\n                    <li><i title=\"\u4E2D\u95F4\u5BF9\u9F50\" class=\"justifycenter fa-align-center\"></i></li>\n                    <li><i title=\"\u53F3\u5BF9\u9F50\" class=\"justifyright fa-align-right\"></i></li>\n                    <li><i title=\"\u6570\u5B57\u7F16\u53F7\" class=\"insertorderedlist fa-list-ol\"></i></li>\n                    <li><i title=\"\u9879\u76EE\u7F16\u53F7\" class=\"insertunorderedlist fa-list-ul\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u7F29\u8FDB\" class=\"outdent fa-outdent\"></i></li>\n                    <li><i title=\"\u51CF\u5C11\u7F29\u8FDB\" class=\"indent fa-indent\"></i></li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\u989C\u8272\" class=\"fa-paint-brush\"></i>\n                        <div class=\"fontColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontColorPicker\"></div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u80CC\u666F\u989C\u8272\" class=\"fa-pencil\"></i>\n                        <div class=\"bgColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontBgColorPicker\"></div>\n                    </li>\n                    <li><i title=\"\u589E\u52A0\u94FE\u63A5\" class=\"createLink fa-link\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u56FE\u7247\" class=\"insertImage fa-file-image-o\"></i></li>\n                    <li><i title=\"\u4E00\u952E\u5B58\u56FE\" class=\"saveRemoteImage2Local fa-hdd-o\"></i></li>\n                    <li><i title=\"\u6E05\u7406 HTML\" class=\"cleanHTML fa-eraser\"></i></li>\n                    <li><i title=\"\u5207\u6362\u5230\u4EE3\u7801\" class=\"switchMode fa-code\"></i></li>\n                </ul>\n            \n                <div class=\"editorBody\">\n                    <iframe srcdoc=\"<html><body></body></html>\"></iframe>\n                    <slot></slot>\n                </div>\n                <aj-form-popup-upload ref=\"uploadLayer\" :upload-url=\"uploadImageActionUrl\"></aj-form-popup-upload>\n            </div>\n        "], ["\n            <div class=\"aj-form-html-editor\">\n                <ul class=\"toolbar\" @click=\"onToolBarClk\">\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\" class=\"fa-font\"></i>\n                        <div class=\"fontfamilyChoser\" @click=\"onFontfamilyChoserClk\">\n                            <a style=\"font-family: '\u5B8B\u4F53'\">\u5B8B\u4F53</a>\n                            <a style=\"font-family: '\u9ED1\u4F53'\">\u9ED1\u4F53</a>\n                            <a style=\"font-family: '\u6977\u4F53'\">\u6977\u4F53</a>\n                            <a style=\"font-family: '\u96B6\u4E66'\">\u96B6\u4E66</a>\n                            <a style=\"font-family: '\u5E7C\u5706'\">\u5E7C\u5706</a>\n                            <a style=\"font-family: 'Microsoft YaHei'\">Microsoft YaHei</a>\n                            <a style=\"font-family: Arial\">Arial</a>\n                            <a style=\"font-family: 'Arial Narrow'\">Arial Narrow</a>\n                            <a style=\"font-family: 'Arial Black'\">Arial Black</a>\n                            <a style=\"font-family: 'Comic Sans MS'\">Comic Sans MS</a>\n                            <a style=\"font-family: Courier\">Courier</a>\n                            <a style=\"font-family: System\">System</a>\n                            <a style=\"font-family: 'Times New Roman'\">Times New Roman</a>\n                            <a style=\"font-family: Verdana\">Verdana</a>\n                        </div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u53F7\" class=\"fa-header\"></i>\n                        <div class=\"fontsizeChoser\" @click=\"onFontsizeChoserClk\">\n                            <a style=\"font-size: xx-small; \">\u6781\u5C0F</a>\n                            <a style=\"font-size: x-small;  \">\u7279\u5C0F</a>\n                            <a style=\"font-size: small;    \">\u5C0F</a>\n                            <a style=\"font-size: medium;   \">\u4E2D</a>\n                            <a style=\"font-size: large;    \">\u5927</a>\n                            <a style=\"font-size: x-large;  \">\u7279\u5927</a>\n                            <a style=\"font-size: xx-large; line-height: 140%\">\u6781\u5927</a>\n                        </div>\n                    </li>\n                    <li><i title=\"\u52A0\u7C97\" class=\"bold fa-bold\"></i></li>\n                    <li><i title=\"\u659C\u4F53\" class=\"italic fa-italic\"></i></li>\n                    <li><i title=\"\u4E0B\u5212\u7EBF\" class=\"underline fa-underline\"></i></li>\n                    <li><i title=\"\u5DE6\u5BF9\u9F50\" class=\"justifyleft fa-align-left\"></i></li>\n                    <li><i title=\"\u4E2D\u95F4\u5BF9\u9F50\" class=\"justifycenter fa-align-center\"></i></li>\n                    <li><i title=\"\u53F3\u5BF9\u9F50\" class=\"justifyright fa-align-right\"></i></li>\n                    <li><i title=\"\u6570\u5B57\u7F16\u53F7\" class=\"insertorderedlist fa-list-ol\"></i></li>\n                    <li><i title=\"\u9879\u76EE\u7F16\u53F7\" class=\"insertunorderedlist fa-list-ul\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u7F29\u8FDB\" class=\"outdent fa-outdent\"></i></li>\n                    <li><i title=\"\u51CF\u5C11\u7F29\u8FDB\" class=\"indent fa-indent\"></i></li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u5B57\u4F53\u989C\u8272\" class=\"fa-paint-brush\"></i>\n                        <div class=\"fontColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontColorPicker\"></div>\n                    </li>\n                    <li class=\"dorpdown\">\n                        <i title=\"\u80CC\u666F\u989C\u8272\" class=\"fa-pencil\"></i>\n                        <div class=\"bgColor colorPicker\" v-html=\"createColorPickerHTML()\" @click=\"onFontBgColorPicker\"></div>\n                    </li>\n                    <li><i title=\"\u589E\u52A0\u94FE\u63A5\" class=\"createLink fa-link\"></i></li>\n                    <li><i title=\"\u589E\u52A0\u56FE\u7247\" class=\"insertImage fa-file-image-o\"></i></li>\n                    <li><i title=\"\u4E00\u952E\u5B58\u56FE\" class=\"saveRemoteImage2Local fa-hdd-o\"></i></li>\n                    <li><i title=\"\u6E05\u7406 HTML\" class=\"cleanHTML fa-eraser\"></i></li>\n                    <li><i title=\"\u5207\u6362\u5230\u4EE3\u7801\" class=\"switchMode fa-code\"></i></li>\n                </ul>\n            \n                <div class=\"editorBody\">\n                    <iframe srcdoc=\"<html><body></body></html>\"></iframe>\n                    <slot></slot>\n                </div>\n                <aj-form-popup-upload ref=\"uploadLayer\" :upload-url=\"uploadImageActionUrl\"></aj-form-popup-upload>\n            </div>\n        "]));
                _this.props = {
                    fieldName: { type: String, required: true },
                    uploadImageActionUrl: String // 图片上传路径
                };
                _this.fieldName = "";
                _this.fieldValue = "";
                /**
                 * 图片上传路径
                 */
                _this.uploadImageActionUrl = "";
                _this.toolbarEl = document.body;
                _this.iframeEl = document.body;
                _this.iframeDoc = document;
                _this.sourceEditor = document.body;
                _this.mode = "iframe";
                return _this;
            }
            HtmlEditor.prototype.mounted = function () {
                var _this = this;
                var el = this.$el;
                this.mode = 'iframe'; // 当前可视化编辑 iframe|textarea
                this.toolbarEl = el.$('.toolbar');
                this.iframeEl = el.$('iframe');
                // 这个方法只能写在 onload 事件里面， 不写 onload 里还不执行
                this.iframeEl.contentWindow.onload = function (ev) {
                    var iframeDoc = _this.iframeEl.contentWindow.document;
                    iframeDoc.designMode = 'on';
                    iframeDoc.addEventListener('paste', onImagePaste.bind(_this)); // 直接剪切板粘贴上传图片
                    _this.iframeDoc = iframeDoc;
                    new MutationObserver(function (mutationsList, observer) {
                        if (_this.mode === 'iframe')
                            _this.sourceEditor.value = _this.iframeDoc.body.innerHTML;
                    }).observe(iframeDoc.body, { attributes: true, childList: true, subtree: true, characterData: true });
                    _this.sourceEditor.value && _this.setIframeBody(_this.sourceEditor.value); // 有内容
                };
                this.sourceEditor = el.$('textarea');
                this.sourceEditor.classList.add("hide");
                this.sourceEditor.name = this.fieldName;
                this.sourceEditor.oninput = function (ev) {
                    if (_this.mode === 'textarea')
                        _this.setIframeBody(_this.sourceEditor.value);
                };
                this.uploadImgMgr = this.$refs.uploadLayer;
            };
            /**
            * 输入 HTML 内容
            *
            * @param html
            */
            HtmlEditor.prototype.setIframeBody = function (html) {
                this.iframeDoc.body.innerHTML = html;
            };
            /**
             * 获取内容的 HTML
             *
             * @param cleanWord
             * @param encode
             */
            HtmlEditor.prototype.getValue = function (cleanWord, encode) {
                var result = this.iframeDoc.body.innerHTML;
                if (cleanWord)
                    result = cleanPaste(result);
                if (encode)
                    result = encodeURIComponent(result);
                return result;
            };
            /**
             * 切換 HTML 編輯 or 可視化編輯
             *
             */
            HtmlEditor.prototype.setMode = function () {
                if (this.mode == 'iframe') {
                    this.iframeEl.classList.add('hide');
                    this.sourceEditor.classList.remove('hide');
                    this.mode = 'textarea';
                    grayImg.call(this, true);
                }
                else {
                    this.iframeEl.classList.remove('hide');
                    this.sourceEditor.classList.add('hide');
                    this.mode = 'iframe';
                    grayImg.call(this, false);
                }
            };
            /**
             * 当工具条点击的时候触发
             *
             * @param ev
             */
            HtmlEditor.prototype.onToolBarClk = function (ev) {
                var _this = this;
                var el = ev.target, clsName = el.className.split(' ').shift();
                switch (clsName) {
                    case 'createLink':
                        var result = prompt("请输入 URL 地址");
                        if (result)
                            this.format("createLink", result);
                        break;
                    case 'insertImage':
                        // @ts-ignore
                        if (window.isCreate)
                            aj.alert('请保存记录后再上传图片。');
                        else {
                            this.uploadImgMgr.show(function (json) {
                                if (json && json.isOk)
                                    _this.format("insertImage", json.fullUrl);
                            });
                        }
                        break;
                    case 'switchMode':
                        this.setMode();
                        break;
                    case 'cleanHTML':
                        // @ts-ignore
                        this.setIframeBody(HtmlSanitizer.SanitizeHtml(this.iframeDoc.body.innerHTML)); // 清理冗余 HTML
                        break;
                    case 'saveRemoteImage2Local':
                        saveRemoteImage2Local.call(this);
                        break;
                    default:
                        this.format(clsName);
                }
            };
            /**
             * 通过 document.execCommand() 来操纵可编辑内容区域的元素
             *
             * @param type 命令的名称
             * @param para 一些命令（例如 insertImage）需要额外的参数（insertImage 需要提供插入 image 的 url），默认为 null
             */
            HtmlEditor.prototype.format = function (type, para) {
                if (para)
                    this.iframeDoc.execCommand(type, false, para);
                else
                    this.iframeDoc.execCommand(type, false);
                this.iframeEl.contentWindow.focus();
            };
            /**
             * 选择字号大小
             *
             * @param ev
             */
            HtmlEditor.prototype.onFontsizeChoserClk = function (ev) {
                var el = ev.target, els = ev.currentTarget.children;
                for (var i = 0, j = els.length; i < j; i++)
                    if (el == els[i])
                        break;
                this.format('fontsize', i + "");
            };
            HtmlEditor.prototype.onFontColorPicker = function (ev) {
                this.format('foreColor', ev.target.title);
            };
            HtmlEditor.prototype.onFontBgColorPicker = function (ev) {
                this.format('backColor', ev.target.title);
            };
            /**
             * 选择字体
             *
             * @param ev
             */
            HtmlEditor.prototype.onFontfamilyChoserClk = function (ev) {
                var el = ev.target;
                this.format('fontname', el.innerHTML);
                /* 如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：*/
                var menuPanel = el.parentNode;
                menuPanel.style.display = 'none';
                setTimeout(function () { return menuPanel.style.display = ''; }, 300);
            };
            /**
             * 创建颜色选择器
             */
            HtmlEditor.prototype.createColorPickerHTML = function () {
                var cl = ['00', '33', '66', '99', 'CC', 'FF'], b, d, e, f, h = ['<div class="colorhead"><span class="colortitle">颜色选择</span></div><div class="colorbody"><table cellspaci="0" cellpadding="0"><tr>'];
                // 创建 body  [6 x 6的色盘]
                for (var i = 0; i < 6; ++i) {
                    h.push('<td><table class="colorpanel" cellspacing="0" cellpadding="0">');
                    for (var j = 0, a = cl[i]; j < 6; ++j) {
                        h.push('<tr>');
                        for (var k = 0, c = cl[j]; k < 6; ++k) {
                            b = cl[k];
                            e = (k == 5 && i != 2 && i != 5) ? ';border-right:none;' : '';
                            f = (j == 5 && i < 3) ? ';border-bottom:none' : '';
                            d = '#' + a + b + c;
                            // T = document.all ? '&nbsp;' : '';
                            h.push('<td unselectable="on" style="background-color: ' + d + e + f + '" title="' + d + '"></td>');
                        }
                        h.push('</tr>');
                    }
                    h.push('</table></td>');
                    if (cl[i] == '66')
                        h.push('</tr><tr>');
                }
                h.push('</tr></table></div>');
                return h.join('');
            };
            return HtmlEditor;
        }(aj.VueComponent));
        form.HtmlEditor = HtmlEditor;
        /**
         * 使图标变灰色
         *
         * @param this
         * @param isGray
         */
        function grayImg(isGray) {
            this.toolbarEl.$('i', function (item) {
                if (item.className.indexOf('switchMode') != -1)
                    return;
                item.style.color = isGray ? 'lightgray' : '';
            });
        }
        /**
         * 一键存图
         *
         * @param this
         */
        function saveRemoteImage2Local() {
            var arr = this.iframeDoc.querySelectorAll('img'), remotePicArr = new Array(), srcs = [];
            for (var i = 0, j = arr.length; i < j; i++) {
                var imgEl = arr[i], src = imgEl.getAttribute('src');
                if (/^http/.test(src)) {
                    remotePicArr.push(imgEl);
                    srcs.push(src);
                }
            }
            if (srcs.length)
                aj.xhr.post('../downAllPics/', function (json) {
                    var _arr = json.pics;
                    for (var i = 0, j = _arr.length; i < j; i++)
                        remotePicArr[i].src = "images/" + _arr[i]; // 改变 DOM 的旧图片地址为新的
                    aj.alert('所有图片下载完成。');
                }, { pics: srcs.join('|') });
            else
                aj.alert('未发现有远程图片');
        }
        /**
         * 粘贴图片
         *
         * @param this
         * @param ev
         */
        function onImagePaste(ev) {
            var _this = this;
            if (!this.uploadImageActionUrl) {
                aj.alert('未提供图片上传地址');
                return;
            }
            var items = ev.clipboardData && ev.clipboardData.items, file = null; // file 就是剪切板中的图片文件
            if (items && items.length) { // 检索剪切板 items
                for (var i = 0; i < items.length; i++) {
                    var item = items[i];
                    if (item.type.indexOf('image') !== -1) {
                        // @ts-ignore
                        if (window.isCreate) { // 有图片
                            aj.alert('请保存记录后再上传图片。');
                            return;
                        }
                        file = item.getAsFile();
                        break;
                    }
                }
            }
            if (file) {
                ev.preventDefault();
                aj.img.changeBlobImageQuality(file, function (newBlob) {
                    // 复用上传的方法
                    Vue.options.components["aj-xhr-upload"].extendOptions.methods.doUpload.call({
                        action: _this.uploadImageActionUrl,
                        progress: 0,
                        uploadOk_callback: function (j) {
                            if (j.isOk)
                                this.format("insertImage", this.ajResources.imgPerfix + j.imgUrl);
                        },
                        $blob: newBlob,
                        $fileName: 'foo.jpg' // 文件名不重要，反正上传到云空间会重命名
                    });
                });
            }
        }
        /**
         * Remove additional MS Word content
         * MSWordHtmlCleaners.js https://gist.github.com/ronanguilloux/2915995
         *
         * @param html
         */
        function cleanPaste(html) {
            html = html.replace(/<(\/)*(\\?xml:|meta|link|span|font|del|ins|st1:|[ovwxp]:)((.|\s)*?)>/gi, ''); // Unwanted tags
            html = html.replace(/(class|style|type|start)=("(.*?)"|(\w*))/gi, ''); // Unwanted sttributes
            html = html.replace(/<style(.*?)style>/gi, ''); // Style tags
            html = html.replace(/<script(.*?)script>/gi, ''); // Script tags
            html = html.replace(/<!--(.*?)-->/gi, ''); // HTML comments
            return html;
        }
        new HtmlEditor().register();
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";

var aj;
(function (aj) {
    var xhr_upload;
    (function (xhr_upload) {
        /**
         * 图片占位符，用户没有选定图片时候使用的图片
         */
        var emptyImg = "data:image/svg+xml,%3Csvg class='icon' viewBox='0 0 1024 1024' xmlns='http://www.w3.org/2000/svg' width='200' height='200'%3E%3Cpath d='M304.128 456.192c48.64 0 88.064-39.424 88.064-88.064s-39.424-88.064-88.064-88.064-88.064 39.424-88.064 88.064 39.424 88.064 88.064 88.064zm0-116.224c15.36 0 28.16 12.288 28.16 28.16s-12.288 28.16-28.16 28.16-28.16-12.288-28.16-28.16 12.288-28.16 28.16-28.16z' " +
            "fill='%23e6e6e6'/%3E%3Cpath d='M887.296 159.744H136.704C96.768 159.744 64 192 64 232.448v559.104c0 39.936 32.256 72.704 72.704 72.704h198.144L500.224 688.64l-36.352-222.72 162.304-130.56-61.44 143.872 92.672 214.016-105.472 171.008h335.36C927.232 864.256 960 832 960 791.552V232.448c0-39.936-32.256-72.704-72.704-72.704zm-138.752 71.68v.512H857.6c16.384 0 30.208 13.312 30.208 30.208v399.872L673.28 408.064l75.264-176.64zM304.64 " +
            "792.064H165.888c-16.384 0-30.208-13.312-30.208-30.208v-9.728l138.752-164.352 104.96 124.416-74.752 79.872zm81.92-355.84l37.376 228.864-.512.512-142.848-169.984c-3.072-3.584-9.216-3.584-12.288 0L135.68 652.8V262.144c0-16.384 13.312-30.208 30.208-30.208h474.624L386.56 436.224zm501.248 325.632c0 16.896-13.312 30.208-29.696 30.208H680.96l57.344-93.184-87.552-202.24 7.168-7.68 229.888 272.896z' fill='%23e6e6e6'/%3E%3C/svg%3E";
        var emptyImg2 = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAALMAAAB2CAYAAACDMaL0AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA1xpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuMC1jMDYwIDYxLjEzNDM0MiwgMjAxMC8wMS8xMC0xODowNjo0MyAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo5RkM5NjQxRDQxQUNFNDExOTcwMkU4MjNEOTc3MDU5RiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDo4QTc3NDNFQkFDNDIxMUU0QTEzMkQyOUQzOTFFQkMzRCIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDo4QTc3NDNFQUFDNDIxMUU0QTEzMkQyOUQzOTFFQkMzRCIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgQ1M1Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6OUZDOTY0MUQ0MUFDRTQxMTk3MDJFODIzRDk3NzA1OUYiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6OUZDOTY0MUQ0MUFDRTQxMTk3MDJFODIzRDk3NzA1OUYiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz77Tka5AAADFElEQVR42uzd0W3aUBiAUVNFlTqBO4VRn/scT1FvQaaAKWCK0hX6FKZghb44Rr2uLGqCL+AY7HMkqyitSHC+XP5riDoryzIZs91ulzAJ5SfngJGYiZnREDNiBjGDmEHMiBnEDGIGMYOYETOIGcQMYkbMIGYQM4gZxIyYQcwgZhAzYgYxg5hBzPDP0wQeYzmR7+Wf6vhdHd+tzDy6z9Xxzco8HZvq2I/sMaXVUYTbX8Q8DasQ8+vIHlcWfkAXNoDTsR9hyMlIH5OYETPjeNYRs1OAmEHM0I8np+BuHK4Xz8PtrdNhZX5UeXUsq+NndfwIt3Onxcr8iCvyIeD6Vbyi8XErtJX54Vbl4sTfFU6PlTlWFv58HfBzt4Xs+rGYo9XheGlYzA9tEebTe2RlNjNHbb6yls3XR/qV/H0337GNDaCYY1flonH7eYCvYRtW4GbQqzDyGHuMGZ3n5PTE1YWPXhFfGnP7vhE0Yu50BeG5Zayogxri6X0jR2PGpaty8c4cfYtX3/LEb3+IuecrA3mH0K+ZnbPG/J0mXvgwZvQ8XpxbMesIY5766xU9O7r/1UBzuJhHbB05huxDoF2u9ebv/JAsGlct+n7WMWbQatFxREg7rvbGDTEPqstmMO0Q8iKMH5lTKuYhV+dzm8E84r68R1nMgyvOrMwx+gg69S0Sc9cV9dSIEBvmMtzPreOzARRzVNBt4WZJ/Asjyws3g0YUMd/UrYJKIzeD9fXrwiZSzLewPArpmqgOq/k84t9n4fOvQ9hWaTHfdDM4D4H1vdLnJ36o7vmXCgbhN03iV+f67ZrXhrQO93WwPTOSLFu+jnrjZ/MXzMpy3P9Lwm63u/cH+JL8/+b841k55hlgZsxgyNW+nqOzDqsyxoy71nwT1NcwdvRxPVrMfPhMnlwwXojZKbjbsQMzM2IGMYOYQcwgZsQMYgYxg5gRMzy+0b+fGTGDmEHMIGYQM2IGMYOYQcyIGcQMYgYxg5gRM4gZxAxiRswgZhAziBnEjJhBzCBmEDNiBjGDmEHMIGbEDGIGMYOYmaY3AQYAXojZOq5J9RcAAAAASUVORK5CYII=";
        // 文件头判别，看看是否为图片
        var imgHeader = { "jpeg": "/9j/4", "gif": "R0lGOD", "png": "iVBORw" };
        var ImgFileUploader = /** @class */ (function (_super) {
            __extends(ImgFileUploader, _super);
            function ImgFileUploader() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-img-uploder";
                _this.template = "<div class=\"aj-img-uploader\">\n                <img :src=\"imgSrc\" />" + xhr_upload.fileUploader.template + "\n            </div>";
                /**
                 * 预览图像显示的内容，可以是图片的 url 也可以是图片的 base64 形式
                 */
                _this.imgSrc = emptyImg2;
                _this.imgMaxWidth = 0;
                _this.imgMaxHeight = 0;
                /**
                 * 图片前缀
                 */
                _this.imgPerfix = "";
                return _this;
            }
            ImgFileUploader.prototype.propsFactory = function () {
                var p = aj.apply({
                    imgMaxWidth: { type: Number, default: 1920 },
                    imgMaxHeight: { type: Number, default: 1680 },
                    imgPerfix: { type: String }
                }, this.props);
                p.accpectFileType = { type: String, default: "image/*" };
                p.limitFileType = { type: String, default: 'jpg|png|gif|jpeg' };
                return p;
            };
            // onUploadInputChange(ev: Event): void {
            //     super.onUploadInputChange(ev);
            //     // fileUploader.onUploadInputChange.call(this, ev);
            //     this.readBase64();
            // }
            ImgFileUploader.prototype.beforeCreate = function () {
                this.$options.template = this.$options.template.replace('<span class="slot"></span>', '<br />最大尺寸：{{imgMaxWidth}}x{{imgMaxHeight}}');
            };
            ImgFileUploader.prototype.mounted = function () {
                var _this = this;
                if (this.fieldValue)
                    this.imgSrc = this.imgPerfix + this.fieldValue;
                var imgEl = this.$el.$('img');
                imgEl.onload = function () {
                    if (imgEl.width > _this.imgMaxWidth || imgEl.height > _this.imgMaxHeight) {
                        _this.errMsg = '图片大小尺寸不符合要求哦，请裁剪图片重新上传吧~';
                    }
                    // if (this.fileSize > 300 * 1024)  // 大于 300k 才压缩
                    //     img.compressAsBlob(imgEl, (blob: Blob): void => {
                    //         this.$blob = blob;
                    //     });
                };
            };
            ImgFileUploader.prototype.watchFactory = function () {
                return {
                    imgBase64Str: function (newV) {
                        // 文件头判别，看看是否为图片
                        var isPic = false;
                        for (var i in imgHeader) {
                            if (~newV.indexOf(imgHeader[i])) {
                                isPic = true;
                                break;
                            }
                        }
                        if (!isPic) {
                            var msg_1 = '亲，改了扩展名我还能认得你不是图片哦';
                            Vue.set(this.errStatus, 2, msg_1);
                            aj.alert(msg_1);
                        }
                        else
                            Vue.set(this.errStatus, 2, "");
                    },
                    errMsg: function (newV) {
                        if (!newV) // 没有任何错误才显示图片
                            this.readBase64();
                    }
                };
            };
            ImgFileUploader.prototype.readBase64 = function () {
                var _this = this;
                var reader = new FileReader();
                reader.onload = function (ev) {
                    var fileReader = ev.target;
                    _this.imgSrc = fileReader.result;
                };
                this.$fileObj && reader.readAsDataURL(this.$fileObj);
            };
            return ImgFileUploader;
        }(xhr_upload.FileUploader));
        xhr_upload.ImgFileUploader = ImgFileUploader;
        new ImgFileUploader().register();
    })(xhr_upload = aj.xhr_upload || (aj.xhr_upload = {}));
})(aj || (aj = {}));

"use strict";

/**
* 将上传控件嵌入到一个浮出层中
*/
Vue.component('aj-form-popup-upload', {
    template: html(__makeTemplateObject(["\n        <aj-layer>\n            <h3>\u56FE\u7247\u4E0A\u4F20</h3>\n            <p>\u4E0A\u4F20\u6210\u529F\u540E\u81EA\u52A8\u63D2\u5165\u5230\u6B63\u6587</p>\n            <aj-img-uploder ref=\"uploadControl\" :action=\"uploadUrl\"></aj-img-uploder>\n        </aj-layer>\n    "], ["\n        <aj-layer>\n            <h3>\u56FE\u7247\u4E0A\u4F20</h3>\n            <p>\u4E0A\u4F20\u6210\u529F\u540E\u81EA\u52A8\u63D2\u5165\u5230\u6B63\u6587</p>\n            <aj-img-uploder ref=\"uploadControl\" :action=\"uploadUrl\"></aj-img-uploder>\n        </aj-layer>\n    "])),
    data: function () {
        return {
            text: {}
        };
    },
    props: {
        uploadUrl: { type: String, required: true },
        imgName: { type: String, required: false },
        imgId: { type: Number, required: false },
        imgPlace: String // 图片占位符，用户没有选定图片时候使用的图片
    },
    mounted: function () {
        var obj = this.$refs.uploadControl;
        this.text = { maxSize: obj.limitSize || 600, maxHeight: obj.imgMaxHeight, maxWidth: obj.imgMaxWidth };
    },
    methods: {
        /**
         * 显示上传控件
         *
         * @param {Function} callback 上传成功之后的回调函数
         */
        show: function (callback) {
            if (callback)
                this.$refs.uploadControl.uploadOk_callback = callback;
            this.$children[0].show();
        }
    }
});

"use strict";
// aj.searchPanel = {
//     methods: {
//         searchBy(field, clz) {
//             var param = {};
//             param[field] = aj(clz).value;
//             if (!param[field]) {
//                 aj.alert('请输入搜索的关键字');
//                 return;
//             }
//             aj.apply(this.$refs.pager.extraParam, param);
//             this.reload();
//         },
//         // 选择任意字段
//         searchAny(field, clz) {
//             var param = {};
//             param.filterField = field;
//             param.filterValue = aj(clz).value;
//             if (!param.filterValue) {
//                 aj.alert('请输入搜索的关键字');
//                 return;
//             }
//             aj.apply(this.$refs.pager.extraParam, param);
//             this.reload();
//         },
//         // 清空查询参数
//         clearSearch() {
//             this.$refs.pager.extraParam = {};
//             this.reload();
//         }
//     }
// };

"use strict";

/**
 * 下拉列表
 */
Vue.component('aj-form-select', {
    template: html(__makeTemplateObject(["\n        <select :name=\"name\" class=\"aj-form-select\">\n            <template v-for=\"value, key, index in options\">\n                <option v-if=\"index === selectedIndex\" selected :value=\"value\" >{{key}}</option>\n                <option v-else :value=\"value\" >{{key}}</option>\n            </template>\n        </select>\n    "], ["\n        <select :name=\"name\" class=\"aj-form-select\">\n            <template v-for=\"value, key, index in options\">\n                <option v-if=\"index === selectedIndex\" selected :value=\"value\" >{{key}}</option>\n                <option v-else :value=\"value\" >{{key}}</option>\n            </template>\n        </select>\n    "])),
    props: {
        name: { type: String, required: true },
        options: { type: Object, required: true },
        selectedIndex: { type: Number } // starts form 0
    }
});

"use strict";
/**
 * 表单工具函数
 */
var aj;
(function (aj) {
    var form;
    (function (form) {
        var utils;
        (function (utils) {
            /**
             * 获取表单控件的值
             *
             * @param el
             * @param cssSelector
             */
            function getFormFieldValue(_el, cssSelector) {
                var el = _el.$(cssSelector);
                if (el)
                    return el.value;
                else
                    throw "\u627E\u4E0D\u5230" + cssSelector + "\u5143\u7D20";
            }
            utils.getFormFieldValue = getFormFieldValue;
            /**
             * 指定 id 的那个 option 选中
             *
             * @param this
             * @param id
             */
            function selectOption(id) {
                this.$el.$('option', function (i) {
                    if (i.value == id)
                        i.selected = true;
                });
            }
            utils.selectOption = selectOption;
        })(utils = form.utils || (form.utils = {}));
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var form;
    (function (form_1) {
        if (!('validity' in document.createElement('input')))
            window.alert("不支持 HTML5 表单验证");
        /**
         * 表单验证器
         */
        var Validator = /** @class */ (function () {
            function Validator(el) {
                this.errorElements = [];
                // let isMsgNewLine: boolean = el.dataset.msgNewline === "true";
                el.setAttribute('novalidate', 'true'); // 禁止浏览器原生的错误提示
                this.el = el;
                this.checkEveryField();
            }
            /**
             * 对每一个表单元素监听事件，一失去焦点就触发表单验证
             */
            Validator.prototype.checkEveryField = function () {
                var _this = this;
                this.el.$('input', function (input) {
                    input.addEventListener('blur', function (ev) {
                        var el = ev.target;
                        if (el.tagName == "A" || Validator.isIgnoreEl(el)) // 忽略部分元素；a 元素也有 blur 事件，忽略之
                            return;
                        var result = Validator.check(el);
                        if (result) { // 如果有错误,就把它显示出来
                            _this.errorElements.push(result);
                            _this.showError(result);
                        }
                        else
                            _this.removeError(el); // 否则, 移除所有存在的错误信息
                    }, true);
                });
            };
            /**
             *
             * @param err
             */
            Validator.prototype.showError = function (err) {
                var _a;
                var el = err.el, id = el.id || el.name; // 获取字段 id 或者 name
                if (!id)
                    return;
                err.el.classList.add('error'); // 将错误类添加到字段
                // 检查错误消息字段是否已经存在 如果没有, 就创建一个
                var message = err.el.form.$(".error-message#error-for-" + id);
                if (!message) {
                    message = document.createElement('div');
                    message.className = 'error-message';
                    message.id = 'error-for-' + id;
                    (_a = el.parentNode) === null || _a === void 0 ? void 0 : _a.insertBefore(message, el.nextSibling);
                }
                el.setAttribute('aria-describedby', 'error-for-' + id); // 添加 ARIA role 到字段
                message.innerHTML = err.msg; // 更新错误信息
                // if (!isNewLine)// 显示错误信息
                //     message.style.display = 'inline-block';
                message.classList.remove('hide');
            };
            /**
             * 移除所有的错误信息
             *
             * @param el
             */
            Validator.prototype.removeError = function (el) {
                var id = el.id || el.name; // 获取字段的 id 或者 name
                if (!id)
                    return;
                el.classList.remove('error'); // 删除字段的错误类
                el.removeAttribute('aria-describedby'); // 移除字段的 ARIA role
                var message = el.form.$(".error-message#error-for-" + id); // 检查 DOM 中是否有错误消息
                if (message) {
                    message.innerHTML = ''; // 如果有错误消息就隐藏它
                    message.classList.add('hide');
                }
            };
            /**
             * 是否忽略的表单元素
             *
             * @param el
             */
            Validator.isIgnoreEl = function (el) {
                return el.disabled || el.type === 'file' || el.type === 'reset' || el.type === 'submit' || el.type === 'button';
            };
            /**
             * 验证字段
             *
             * @param field 表单字段元素
             * @returns 若验证通过返回 null，否则返回 ErrorElement
             */
            Validator.check = function (field) {
                // if (!field || !field.getAttribute)
                //     console.log(field);
                var validity = field.validity; // 获取 validity
                if (!validity)
                    throw '浏览器不支持 HTML5 表单验证';
                if (validity.valid) // 通过验证
                    return null;
                else {
                    var result = {
                        el: field,
                        msg: "无效输入" // 通用错误讯息 The value you entered for this field is invalid.
                    };
                    if (validity.valueMissing) // 如果是必填字段但是字段为空时
                        result.msg = '该项是必填项';
                    if (validity.typeMismatch) { // 如果类型不正确
                        if (field.type === 'email')
                            result.msg = '请输入有效的邮件地址';
                        else if (field.type === 'url')
                            result.msg = '请输入一个有效的网址';
                        else
                            result.msg = '请输入正确的类型';
                    }
                    if (validity.tooShort)
                        result.msg = "\u8BF7\u8F93\u5165\u81F3\u5C11" + field.getAttribute('minLength') + "\u4E2A\u5B57\u7B26\u3002\u5F53\u524D\u4F60\u8F93\u5165\u4E86" + field.value.length + "\u4E2A\u5B57\u7B26\u3002";
                    // 'Please lengthen this text to ' + field.getAttribute('minLength') + ' characters or more. You are currently using ' + field.value.length + ' characters.'
                    if (validity.tooLong)
                        result.msg = "\u4F60\u53EA\u80FD\u8F93\u5165\u6700\u591A" + field.getAttribute('maxLength') + "\u4E2A\u5B57\u7B26\u3002\u5F53\u524D\u4F60\u8F93\u5165\u4E86" + field.value.length + "\u4E2A\u5B57\u7B26\u3002";
                    // 'Please shorten this text to no more than ' + field.getAttribute('maxLength') + ' characters. You are currently using ' + field.value.length + ' characters.';
                    if (validity.badInput)
                        result.msg = '请输入数字';
                    if (validity.stepMismatch) // 如果数字值与步进间隔不匹配
                        result.msg = '请选择一个有效值';
                    if (validity.rangeOverflow) // 如果数字字段的值大于 max 的值
                        result.msg = "\u8BF7\u9009\u62E9\u5C0F\u4E8E " + field.getAttribute('max') + " \u7684\u503C";
                    // return 'Please select a value that is no more than ' + field.getAttribute('max') + '.';
                    if (validity.rangeUnderflow)
                        result.msg = "\u8BF7\u9009\u62E9\u5927\u4E8E " + field.getAttribute('min') + " \u7684\u503C";
                    // return 'Please select a value that is no less than ' + field.getAttribute('min') + '.';
                    if (validity.patternMismatch)
                        result.msg = field.getAttribute('title') || '格式要求不正确';
                    return result;
                }
            };
            /**
             * 是否通过验证
             *
             * @param form
             */
            Validator.onSubmit = function (form) {
                var fields = form.elements; // 获取所有的表单元素
                // 验证每一个字段
                // 将具有错误的第一个字段存储到变量中以便稍后我们将其默认获得焦点
                var error, hasErrors = null;
                for (var i = 0, j = fields.length; i < j; i++) {
                    var el = fields[i];
                    error = this.check(el);
                    if (error) {
                        // showError(el);
                        if (!hasErrors) // 如果有错误,停止提交表单并使出现错误的第一个元素获得焦点
                            hasErrors = el;
                    }
                }
                if (hasErrors) {
                    hasErrors.focus();
                    return false;
                }
                return true;
            };
            return Validator;
        }());
        form_1.Validator = Validator;
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));
// ; (function (window, document, undefined) {  // 确保 ValidityState 全部被支持 (所有的功能) 
//     var supported = function () {
//         var input = document.createElement('input');
//         return ('validity' in input && 'badInput' in input.validity && 'patternMismatch' in input.validity && 'rangeOverflow' in input.validity && 'rangeUnderflow' in input.validity && 'stepMismatch' in input.validity && 'tooLong' in input.validity && 'tooShort' in input.validity && 'typeMismatch' in input.validity && 'valid' in input.validity && 'valueMissing' in input.validity);
//     };
//     /** 
//      *  Generate the field validity object
//      *  @param {Node]} field The field to validate 
//      *  @return {Object} The validity object 
//      **/
//     var getValidityState = function (field) {
//         // 变量 
//         var type = field.getAttribute('type') || input.nodeName.toLowerCase(); 
//         var isNum = type === 'number' || type === 'range'; 
//         var length = field.value.length; var valid = true; 
//         //检测支持性
//         var checkValidity = {
//             badInput: (isNum && length > 0 && !/[-+]?[0-9]/.test(field.value)),
//             // 数字字段的值不是数字 
//             patternMismatch: (field.hasAttribute('pattern') && length > 0 && new RegExp(field.getAttribute('pattern')).test(field.value) === false),
//             // 输入的值不符合模式 
//             rangeOverflow: (field.hasAttribute('max') && isNum && field.value > 1 && parseInt(field.value, 10) > parseInt(field.getAttribute('max'), 10)),
//             // 数字字段的值大于max属性值 
//             rangeUnderflow: (field.hasAttribute('min') && isNum && field.value > 1 && parseInt(field.value, 10) < parseInt(field.getAttribute('min'), 10)),
//             // 数字字段的值小于min属性值
//             stepMismatch: (field.hasAttribute('step') && field.getAttribute('step') !== 'any' && isNum && Number(field.value) % parseFloat(field.getAttribute('step')) !== 0),
//             // 数字字段的值不符合 stepattribute 
//             tooLong: (field.hasAttribute('maxLength') && field.getAttribute('maxLength') > 0 && length > parseInt(field.getAttribute('maxLength'), 10)),
//             // 用户在具有maxLength属性的字段中输入的值的长度大于属性值
//             tooShort: (field.hasAttribute('minLength') && field.getAttribute('minLength') > 0 && length > 0 && length < parseInt(field.getAttribute('minLength'), 10)),
//             // 用户在具有minLength属性的字段中输入的值的长度小于属性值 
//             typeMismatch: (length > 0 && ((type === 'email' && !/^([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x22([^\x0d\x22\x5c\x80-\xff]|\x5c[\x00-\x7f])*\x22))*\x40([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d)(\x2e([^\x00-\x20\x22\x28\x29\x2c\x2e\x3a-\x3c\x3e\x40\x5b-\x5d\x7f-\xff]+|\x5b([^\x0d\x5b-\x5d\x80-\xff]|\x5c[\x00-\x7f])*\x5d))*$/.test(field.value)) || (type === 'url' && !/^(?:(?:https?|HTTPS?|ftp|FTP):\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-zA-Z\u00a1-\uffff0-9]-*)*[a-zA-Z\u00a1-\uffff0-9]+)(?:\.(?:[a-zA-Z\u00a1-\uffff0-9]-*)*[a-zA-Z\u00a1-\uffff0-9]+)*)(?::\d{2,5})?(?:[\/?#]\S*)?$/.test(field.value)))),
//             // email 或者 URL 字段的值不是一个 email地址或者 URL 
//             valueMissing: (field.hasAttribute('required') && (((type === 'checkbox' || type === 'radio') && !field.checked) || (type === 'select' && field.options[field.selectedIndex].value < 1) || (type !== 'checkbox' && type !== 'radio' && type !== 'select' && length < 1))) // 必填字段没有值 
//         };
//         // 检查是否有错误 
//         for (var key in checkValidity) {
//             if (checkValidity.hasOwnProperty(key)) {
//                 // If there's an error, change valid value 
//                 if (checkValidity[key]) { valid = false; break; }
//             }
//         }
//         // 给 validity 对象添加 valid 属性
//         checkValidity.valid = valid;
//         // 返回对象 
//         return checkValidity;
//     };
//     // 如果不支持完整的 ValidityState 功能，则可以使用polyfill 
//     if (!supported()) {
//         Object.defineProperty(HTMLInputElement.prototype, 'validity', {
//             get: function ValidityState() {
//                 return getValidityState(this);
//             },
//             configurable: true,
//         });
//     }
// })(window, document);

"use strict";

/**
 * 相册列表
 */
Vue.component('aj-attachment-picture-list', {
    template: html(__makeTemplateObject(["\n        <table>\n            <tr>\n                <td>\n                    <div class=\"label\">\u76F8\u518C\u56FE\uFF1A</div>\n                    <ul>\n                        <li v-for=\"pic in pics\" style=\"float:left;margin-right:1%;text-align:center;\">\n                            <a :href=\"picCtx + pic.path\" target=\"_blank\"><img :src=\"picCtx + pic.path\" style=\"max-width: 100px;max-height: 100px;\" /></a><br />\n                            <a href=\"###\" @click=\"delPic(pic.id);\">\u5220 \u9664</a>\n                        </li>\n                    </ul>\n                </td> \n                <td>\n                    <aj-xhr-upload ref=\"attachmentPictureUpload\" :action=\"uploadUrl\" :is-img-upload=\"true\" :img-place=\"blankBg\"></aj-xhr-upload>\n                </td>\n            </tr>\n        </table>\n    "], ["\n        <table>\n            <tr>\n                <td>\n                    <div class=\"label\">\u76F8\u518C\u56FE\uFF1A</div>\n                    <ul>\n                        <li v-for=\"pic in pics\" style=\"float:left;margin-right:1%;text-align:center;\">\n                            <a :href=\"picCtx + pic.path\" target=\"_blank\"><img :src=\"picCtx + pic.path\" style=\"max-width: 100px;max-height: 100px;\" /></a><br />\n                            <a href=\"###\" @click=\"delPic(pic.id);\">\u5220 \u9664</a>\n                        </li>\n                    </ul>\n                </td> \n                <td>\n                    <aj-xhr-upload ref=\"attachmentPictureUpload\" :action=\"uploadUrl\" :is-img-upload=\"true\" :img-place=\"blankBg\"></aj-xhr-upload>\n                </td>\n            </tr>\n        </table>\n    "])),
    props: {
        picCtx: String,
        uploadUrl: String,
        blankBg: String,
        delImgUrl: String,
        apiUrl: String
    },
    data: function () {
        return {
            pics: []
        };
    },
    mounted: function () {
        this.getData();
        this.$refs.attachmentPictureUpload.uploadOk_callback = this.getData;
    },
    methods: {
        getData: function () {
            var _this = this;
            aj.xhr.get(this.apiUrl, function (j) { return _this.pics = j.result; });
        },
        delPic: function (picId) {
            var _this = this;
            aj.showConfirm("确定删除相册图片？", function () {
                aj.xhr.dele(_this.delImgUrl + picId + "/", function (j) {
                    if (j.isOk)
                        _this.getData(); // 刷新
                });
            });
        }
    }
});

"use strict";

var aj;
(function (aj) {
    var grid;
    (function (grid) {
        Vue.component('aj-simple-grid', {
            template: html(__makeTemplateObject(["\n            <div>\n                <form action=\"?\" method=\"GET\" style=\"float:right;\">\n                    <input type=\"hidden\" name=\"searchField\" value=\"content\" />\n                    <input type=\"text\" name=\"searchValue\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" class=\"aj-input\" />\n                    <button style=\"margin-top: 0;\" class=\"aj-btn\">\u641C\u7D22</button>\n                </form>\n                <table class=\"aj-grid ajaxjs-borderTable\">\n                    <thead>\n                        <tr>\n                            <th v-for=\"key in columns\" @click=\"sortBy(key)\" :class=\"{ active: sortKey == key }\">\n                                {{ key | capitalize }}\n                                <span class=\"arrow\" :class=\"sortOrders[key] > 0 ? 'asc' : 'dsc'\"></span>\n                            </th>\n                        </tr>\n                    </thead>\n                    <tbody>\n                        <tr v-for=\"entry in filteredData\">\n                            <td v-for=\"key in columns\" v-html=\"entry[key]\"></td>\n                        </tr>\n                    </tbody>\n                </table>\n            </div>\n        "], ["\n            <div>\n                <form action=\"?\" method=\"GET\" style=\"float:right;\">\n                    <input type=\"hidden\" name=\"searchField\" value=\"content\" />\n                    <input type=\"text\" name=\"searchValue\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" class=\"aj-input\" />\n                    <button style=\"margin-top: 0;\" class=\"aj-btn\">\u641C\u7D22</button>\n                </form>\n                <table class=\"aj-grid ajaxjs-borderTable\">\n                    <thead>\n                        <tr>\n                            <th v-for=\"key in columns\" @click=\"sortBy(key)\" :class=\"{ active: sortKey == key }\">\n                                {{ key | capitalize }}\n                                <span class=\"arrow\" :class=\"sortOrders[key] > 0 ? 'asc' : 'dsc'\"></span>\n                            </th>\n                        </tr>\n                    </thead>\n                    <tbody>\n                        <tr v-for=\"entry in filteredData\">\n                            <td v-for=\"key in columns\" v-html=\"entry[key]\"></td>\n                        </tr>\n                    </tbody>\n                </table>\n            </div>\n        "])),
            props: {
                data: Array,
                columns: Array,
                filterKey: String
            },
            data: function () {
                var sortOrders = {};
                this.columns.forEach(function (key) { return sortOrders[key] = 1; });
                return {
                    sortKey: '',
                    sortOrders: sortOrders
                };
            },
            computed: {
                filteredData: function () {
                    var sortKey = this.sortKey, filterKey = this.filterKey && this.filterKey.toLowerCase(), order = this.sortOrders[sortKey] || 1, data = this.data;
                    if (filterKey) {
                        data = data.filter(function (row) {
                            return Object.keys(row).some(function (key) { return String(row[key]).toLowerCase().indexOf(filterKey) > -1; });
                        });
                    }
                    if (sortKey) {
                        data = data.slice().sort(function (a, b) {
                            a = a[sortKey];
                            b = b[sortKey];
                            return (a === b ? 0 : a > b ? 1 : -1) * order;
                        });
                    }
                    return data;
                }
            },
            filters: {
                capitalize: function (str) {
                    return str.charAt(0).toUpperCase() + str.slice(1);
                }
            },
            methods: {
                sortBy: function (key) {
                    this.sortKey = key;
                    this.sortOrders[key] = this.sortOrders[key] * -1;
                }
            }
        });
    })(grid = aj.grid || (aj.grid = {}));
})(aj || (aj = {}));

"use strict";

var aj;
(function (aj) {
    var list;
    (function (list) {
        /**
         * 本地数据仓库
         * 一般情况下不会单独使用这个组件
         */
        list.datastore = {
            props: {
                apiUrl: { type: String, required: true },
                isPage: { type: Boolean, default: true },
                initPageSize: { type: Number, required: false, default: 9 },
                isAutoLoad: { type: Boolean, default: true },
                baseParam: { type: Object, default: function () { return {}; } },
                pageParamNames: { type: Array, default: function () { return ['start', 'limit']; } },
                onLoad: Function
            },
            data: function () {
                return {
                    result: [],
                    extraParam: {},
                    pageSize: this.initPageSize,
                    total: 0,
                    totalPage: 0,
                    pageStart: 0,
                    currentPage: 0
                };
            },
            methods: {
                /**
                 * 分页，跳到第几页，下拉控件传入指定的页码
                 *
                 * @param this
                 * @param ev
                 */
                jumpPageBySelect: function (ev) {
                    var selectEl = ev.target;
                    var currentPage = selectEl.options[selectEl.selectedIndex].value;
                    this.pageStart = (Number(currentPage) - 1) * this.pageSize;
                    this.getData();
                },
                /**
                 * PageSize 改变时候重新分页
                 *
                 * @param this
                 * @param ev
                 */
                onPageSizeChange: function (ev) {
                    this.pageSize = Number(ev.target.value);
                    this.count();
                    this.getData();
                },
                count: function () {
                    var totalPage = this.total / this.pageSize, yushu = this.total % this.pageSize;
                    this.totalPage = parseInt(String(yushu == 0 ? totalPage : totalPage + 1));
                    //@ts-ignore
                    this.currentPage = parseInt((this.pageStart / this.pageSize) + 1);
                },
                /**
                 * 前一页
                 *
                 * @param this
                 */
                previousPage: function () {
                    this.pageStart -= this.pageSize;
                    //@ts-ignore
                    this.currentPage = parseInt((this.pageStart / this.pageSize) + 1);
                    this.getData();
                },
                /**
                 * 下一页
                 *
                 * @param this
                 */
                nextPage: function () {
                    this.pageStart += this.pageSize;
                    this.currentPage = (this.pageStart / this.pageSize) + 1;
                    this.getData();
                }
            }
        };
        Vue.component('aj-list', {
            mixins: [list.datastore],
            template: html(__makeTemplateObject(["\n\t\t\t<div class=\"aj-list\">\n\t\t\t\t<slot name=\"header\" v-if=\"total != 0\"></slot>\n\t\t\t\t<ul v-if=\"showDefaultUi && (total != 0)\">\n\t\t\t\t\t<li v-for=\"(item, index) in result\">\n\t\t\t\t\t\t<slot v-bind=\"item\">\n\t\t\t\t\t\t\t<a href=\"#\" @click=\"show(item.id, index, $event)\" :id=\"item.id\">{{item.name}}</a>\n\t\t\t\t\t\t</slot>\n\t\t\t\t\t</li>\n\t\t\t\t</ul>\n\t\t\t\t<div class=\"no-data\" v-show=\"isPage && total == 0\">\u672A\u6709\u4EFB\u4F55\u6570\u636E</div>\n\t\t\t\t<footer v-if=\"isPage\" class=\"pager\">\n\t\t\t\t\t<a v-if=\"pageStart > 0\" href=\"#\" @click=\"previousPage\">\u4E0A\u4E00\u9875</a>\n\t\t\t\t\t<a v-if=\"(pageStart > 0 ) && (pageStart + pageSize < total)\" style=\"text-decoration: none;\">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>\n\t\t\t\t\t<a v-if=\"pageStart + pageSize < total\" href=\"#\" @click=\"nextPage\">\u4E0B\u4E00\u9875</a>\n\t\t\t\t\t<a href=\"javascript:;\" @click=\"getData\"><i class=\"fa fa-refresh\" aria-hidden=\"true\"></i> \u5237\u65B0</a>\n\t\t\t\t\t<input type=\"hidden\" name=\"start\" :value=\"pageStart\" />\n\t\t\t\t\t\u9875\u6570\uFF1A{{currentPage}}/{{totalPage}} \u8BB0\u5F55\u6570\uFF1A{{pageStart}}/{{total}}\n\t\t\t\t\t\u6BCF\u9875\u8BB0\u5F55\u6570\uFF1A <input size=\"2\" title=\"\u8F93\u5165\u4E00\u4E2A\u6570\u5B57\u786E\u5B9A\u6BCF\u9875\u8BB0\u5F55\u6570\" type=\"text\" :value=\"pageSize\" @change=\"onPageSizeChange\" />\n\t\t\t\t\t\u8DF3\u8F6C\uFF1A\n\t\t\t\t\t<select @change=\"jumpPageBySelect\">\n\t\t\t\t\t\t<option :value=\"n\" v-for=\"n in totalPage\">{{n}}</option>\n\t\t\t\t\t</select>\n\t\t\t\t</footer>\n\t\t\t\t<div v-show=\"!!autoLoadWhenReachedBottom\" class=\"buttom\"></div>\n\t\t\t</div>\n\t\t"], ["\n\t\t\t<div class=\"aj-list\">\n\t\t\t\t<slot name=\"header\" v-if=\"total != 0\"></slot>\n\t\t\t\t<ul v-if=\"showDefaultUi && (total != 0)\">\n\t\t\t\t\t<li v-for=\"(item, index) in result\">\n\t\t\t\t\t\t<slot v-bind=\"item\">\n\t\t\t\t\t\t\t<a href=\"#\" @click=\"show(item.id, index, $event)\" :id=\"item.id\">{{item.name}}</a>\n\t\t\t\t\t\t</slot>\n\t\t\t\t\t</li>\n\t\t\t\t</ul>\n\t\t\t\t<div class=\"no-data\" v-show=\"isPage && total == 0\">\u672A\u6709\u4EFB\u4F55\u6570\u636E</div>\n\t\t\t\t<footer v-if=\"isPage\" class=\"pager\">\n\t\t\t\t\t<a v-if=\"pageStart > 0\" href=\"#\" @click=\"previousPage\">\u4E0A\u4E00\u9875</a>\n\t\t\t\t\t<a v-if=\"(pageStart > 0 ) && (pageStart + pageSize < total)\" style=\"text-decoration: none;\">&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;</a>\n\t\t\t\t\t<a v-if=\"pageStart + pageSize < total\" href=\"#\" @click=\"nextPage\">\u4E0B\u4E00\u9875</a>\n\t\t\t\t\t<a href=\"javascript:;\" @click=\"getData\"><i class=\"fa fa-refresh\" aria-hidden=\"true\"></i> \u5237\u65B0</a>\n\t\t\t\t\t<input type=\"hidden\" name=\"start\" :value=\"pageStart\" />\n\t\t\t\t\t\u9875\u6570\uFF1A{{currentPage}}/{{totalPage}} \u8BB0\u5F55\u6570\uFF1A{{pageStart}}/{{total}}\n\t\t\t\t\t\u6BCF\u9875\u8BB0\u5F55\u6570\uFF1A <input size=\"2\" title=\"\u8F93\u5165\u4E00\u4E2A\u6570\u5B57\u786E\u5B9A\u6BCF\u9875\u8BB0\u5F55\u6570\" type=\"text\" :value=\"pageSize\" @change=\"onPageSizeChange\" />\n\t\t\t\t\t\u8DF3\u8F6C\uFF1A\n\t\t\t\t\t<select @change=\"jumpPageBySelect\">\n\t\t\t\t\t\t<option :value=\"n\" v-for=\"n in totalPage\">{{n}}</option>\n\t\t\t\t\t</select>\n\t\t\t\t</footer>\n\t\t\t\t<div v-show=\"!!autoLoadWhenReachedBottom\" class=\"buttom\"></div>\n\t\t\t</div>\n\t\t"])),
            props: {
                showDefaultUi: { type: Boolean, default: true },
                isShowFooter: { type: Boolean, default: true },
                hrefStr: { type: String, required: false },
                autoLoadWhenReachedBottom: { type: String, default: '' },
                isDataAppend: { type: Boolean, default: false }
            },
            mounted: function () {
                this.isAutoLoad && this.getData();
                // this.BUS.$on('base-param-change', this.onExtraParamChange.bind(this));
                if (!!this.autoLoadWhenReachedBottom) {
                    // var scrollSpy = new aj.scrollSpy({ scrollInElement: aj(this.autoLoadWhenReachedBottom), spyOn: thish.$el.$('.buttom') });
                    // scrollSpy.onScrollSpyBackInSight = e => this.nextPage();
                }
            },
            methods: {
                getData: function () {
                    var _this = this;
                    this.lastRequestParam = {};
                    aj.apply(this.lastRequestParam, this.baseParam);
                    aj.apply(this.lastRequestParam, this.extraParam);
                    initPageParams.call(this);
                    aj.xhr.get(this.apiUrl, this.onLoad || (function (j) {
                        if (j.result) {
                            if (_this.isPage && j.total === undefined)
                                aj.alert('JSON 缺少 total 字段');
                            if (j.total == 0 || j.result.length == 0)
                                aj.alert('没有找到任何记录');
                            _this.result = j.result;
                            if (_this.isPage) {
                                _this.total = j.total;
                                _this.count();
                            }
                        }
                        _this.$emit('pager-result', _this.result);
                    }), this.lastRequestParam);
                },
                /**
                 * 复位
                 *
                 * @param this
                 */
                reset: function () {
                    this.total = this.totalPage = this.pageStart = this.currentPage = 0;
                    this.pageSize = this.initPageSize;
                },
                onExtraParamChange: function (params) {
                    aj.apply(this.extraParam, params);
                    this.pageStart = 0; // 每次 extraParam 被改变，都是从第一笔开始
                    this.getData();
                }
            },
            watch: {
                extraParam: function () {
                    this.getData();
                }
            }
        });
        /**
         * 生成分页参数的名字
         *
         * @param this
         */
        function initPageParams() {
            var params = {};
            params[this.pageParamNames[0]] = this.pageStart;
            params[this.pageParamNames[1]] = this.pageSize;
            this.isPage && aj.apply(this.lastRequestParam, params);
        }
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";

var aj;
(function (aj) {
    var grid;
    (function (grid) {
        Vue.component('aj-simple-grid', {
            template: html(__makeTemplateObject(["\n            <div>\n                <form action=\"?\" method=\"GET\" style=\"float:right;\">\n                    <input type=\"hidden\" name=\"searchField\" value=\"content\" />\n                    <input type=\"text\" name=\"searchclassValue\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" class=\"aj-input\" />\n                    <button style=\"margin-top: 0;\" class=\"aj-btn\">\u641C\u7D22</button>\n                </form>\n                <table class=\"aj-grid aj-table\" style=\"clear: both;width:100%\">\n                    <thead>\n                        <tr>\n                            <th v-for=\"key in columns\" @click=\"sortBy(key)\" :class=\"{ active: sortKey == key }\">\n                                {{ key | capitalize }}\n                                <span class=\"arrow\" :class=\"sortOrders[key] > 0 ? 'asc' : 'dsc'\"></span>\n                            </th>\n                        </tr>\n                    </thead>\n                    <tbody>\n                        <tr v-for=\"entry in filteredData\">\n                            <td v-for=\"key in columns\" v-html=\"entry[key]\"></td>\n                        </tr>\n                    </tbody>\n                </table>\n            </div>\n        "], ["\n            <div>\n                <form action=\"?\" method=\"GET\" style=\"float:right;\">\n                    <input type=\"hidden\" name=\"searchField\" value=\"content\" />\n                    <input type=\"text\" name=\"searchclassValue\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" class=\"aj-input\" />\n                    <button style=\"margin-top: 0;\" class=\"aj-btn\">\u641C\u7D22</button>\n                </form>\n                <table class=\"aj-grid aj-table\" style=\"clear: both;width:100%\">\n                    <thead>\n                        <tr>\n                            <th v-for=\"key in columns\" @click=\"sortBy(key)\" :class=\"{ active: sortKey == key }\">\n                                {{ key | capitalize }}\n                                <span class=\"arrow\" :class=\"sortOrders[key] > 0 ? 'asc' : 'dsc'\"></span>\n                            </th>\n                        </tr>\n                    </thead>\n                    <tbody>\n                        <tr v-for=\"entry in filteredData\">\n                            <td v-for=\"key in columns\" v-html=\"entry[key]\"></td>\n                        </tr>\n                    </tbody>\n                </table>\n            </div>\n        "])),
            props: {
                data: Array,
                columns: Array,
                filterKey: String
            },
            data: function () {
                var sortOrders = {};
                this.columns.forEach(function (key) { return sortOrders[key] = 1; });
                return {
                    sortKey: '',
                    sortOrders: sortOrders
                };
            },
            computed: {
                filteredData: function () {
                    var sortKey = this.sortKey, filterKey = this.filterKey && this.filterKey.toLowerCase(), order = this.sortOrders[sortKey] || 1, data = this.data;
                    if (filterKey) {
                        data = data.filter(function (row) {
                            return Object.keys(row).some(function (key) { return String(row[key]).toLowerCase().indexOf(filterKey) > -1; });
                        });
                    }
                    if (sortKey) {
                        data = data.slice().sort(function (a, b) {
                            a = a[sortKey];
                            b = b[sortKey];
                            return (a === b ? 0 : a > b ? 1 : -1) * order;
                        });
                    }
                    return data;
                }
            },
            filters: {
                capitalize: function (str) {
                    return str.charAt(0).toUpperCase() + str.slice(1);
                }
            },
            methods: {
                sortBy: function (key) {
                    this.sortKey = key;
                    this.sortOrders[key] = this.sortOrders[key] * -1;
                }
            }
        });
    })(grid = aj.grid || (aj.grid = {}));
})(aj || (aj = {}));

"use strict";
Vue.component('aj-grid-select-row', {
    template: '<a href="#" @click="fireSelect">选择</a>',
    props: { type: { type: String, required: true } },
    methods: {
        fireSelect: function () {
            //@ts-ignore
            this.BUS.$emit('on-' + this.type + '-select', this.$parent.form);
        }
    }
});
Vue.component('aj-grid-open-link', {
    template: '<a href="#" @click="fireSelect"><i class="fa fa-external-link"></i> 详情</a>',
    methods: {
        fireSelect: function () {
            //@ts-ignore
            this.BUS.$emit('on-open-link-clk', this.$parent.form);
        }
    }
});

"use strict";

"use strict";

var aj;
(function (aj) {
    var list;
    (function (list) {
        var grid;
        (function (grid) {
            grid.SectionModel = {
                data: function () {
                    return {
                        isSelectAll: false,
                        selected: {},
                        selectedTotal: 0,
                        maxRows: 0
                    };
                },
                mounted: function () {
                    this.BUS && this.BUS.$on('on-delete-btn-clk', this.batchDelete);
                },
                methods: {
                    /**
                     * 批量删除
                     */
                    batchDelete: function () {
                        var _this = this;
                        if (this.selectedTotal > 0) {
                            aj.showConfirm('确定批量删除记录？', function () {
                                for (var id in _this.selected) {
                                    // @ts-ignore
                                    aj.xhr.dele(_this.apiUrl + "/" + id + "/", function (j) {
                                        console.log(j);
                                    });
                                }
                            });
                        }
                        else
                            aj.alert('未选择记录');
                    },
                    /**
                     * 全选
                     */
                    selectAll: function () {
                        var _this = this;
                        var checkAll = function (item) {
                            item.checked = true;
                            var id = item.dataset.id;
                            if (!id)
                                throw '需要提供 id 在 DOM 属性中';
                            _this.$set(_this.selected, Number(id), true);
                        }, diskCheckAll = function (item) {
                            item.checked = false;
                            var id = item.dataset.id;
                            if (!id)
                                throw '需要提供 id 在 DOM 属性中';
                            _this.$set(_this.selected, Number(id), false);
                        };
                        this.$el.$('table .selectCheckbox input[type=checkbox]', this.selectedTotal === this.maxRows ? diskCheckAll : checkAll);
                    }
                },
                watch: {
                    selected: {
                        handler: function (_new) {
                            var j = 0;
                            // clear falses
                            for (var i in this.selected) {
                                if (this.selected[i] === false)
                                    delete this.selected[i];
                                else
                                    j++;
                            }
                            this.selectedTotal = j;
                            if (j === this.maxRows)
                                this.$el.$('.top-checkbox').checked = true;
                            else
                                this.$el.$('.top-checkbox').checked = false;
                        },
                        deep: true
                    }
                }
            };
            /**
             * 标准表格
             */
            var Grid = /** @class */ (function (_super) {
                __extends(Grid, _super);
                function Grid() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = "aj-grid";
                    _this.template = '<div class="aj-grid"><slot v-bind:grid="this"></slot></div>';
                    _this.mixins = [grid.SectionModel];
                    _this.props = {
                        apiUrl: { type: String, required: true }
                    };
                    /**
                     * 数据层，控制分页
                     */
                    _this.$store = null;
                    /**
                     * 工具条 UI
                     */
                    _this.$toolbar = null;
                    /**
                     * 行 UI
                     */
                    _this.$row = null;
                    /**
                     *
                     */
                    _this.showAddNew = false;
                    /**
                     *
                     */
                    _this.list = [];
                    _this.apiUrl = "";
                    _this.maxRows = 0;
                    _this.selected = {};
                    return _this;
                }
                Grid.prototype.data = function () {
                    return {
                        list: [],
                        updateApi: null,
                        showAddNew: false
                    };
                };
                Grid.prototype.mounted = function () {
                    var _this = this;
                    this.$children.forEach(function (child) {
                        switch (child.$options._componentTag) {
                            case 'aj-entity-toolbar':
                                _this.$toolbar = child;
                                break;
                            case 'aj-grid-inline-edit-row':
                                _this.$row = child;
                                break;
                            case 'aj-list':
                                _this.$store = child;
                                break;
                        }
                    });
                    this.$store.$on("pager-result", function (result) {
                        _this.list = result;
                        _this.maxRows = result.length;
                    });
                    // this.$store.autoLoad && this.$store.getDataData();
                };
                /**
                 * 按下【新建】按钮时候触发的事件，你可以覆盖这个方法提供新的事件
                 */
                Grid.prototype.onCreateClk = function () {
                    this.showAddNew = true;
                };
                /**
                 * 重新加载数据
                 */
                Grid.prototype.reload = function () {
                    this.$store.getData();
                };
                /**
                 * 保存脏数据
                 */
                Grid.prototype.onDirtySaveClk = function () {
                    var _this = this;
                    var dirties = getDirty.call(this);
                    if (!dirties.length) {
                        aj.msg.show('没有修改过的记录');
                        return;
                    }
                    dirties.forEach(function (item) {
                        aj.xhr.put(_this.apiUrl + "/" + item.id + "/", function (j) {
                            if (j.isOk) {
                                _this.list.forEach(function (item) {
                                    if (item.dirty)
                                        delete item.dirty;
                                });
                                aj.msg.show('修改记录成功');
                            }
                        }, item.dirty);
                    });
                };
                return Grid;
            }(aj.VueComponent));
            grid.Grid = Grid;
            new Grid().register();
            /**
             * 获取修改过的数据
             *
             * @param this
             */
            function getDirty() {
                var dirties = [];
                this.list.forEach(function (item) {
                    if (item.dirty) { // 有这个 dirty 就表示修改过的
                        // item.dirty.id = item.id;
                        dirties.push(item);
                    }
                });
                return dirties;
            }
        })(grid = list.grid || (list.grid = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var list;
    (function (list) {
        var grid;
        (function (grid) {
            /**
             * 新建记录的行
             */
            var EditRowCreate = /** @class */ (function (_super) {
                __extends(EditRowCreate, _super);
                function EditRowCreate() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = "aj-grid-inline-edit-row-create";
                    _this.template = html(__makeTemplateObject(["\n            <tr class=\"aj-grid-inline-edit-row isEditMode\">\n                <td></td>\n                <td></td>\n                <td v-for=\"key in columns\" style=\"padding:0\" class=\"cell\">\n                    <aj-select v-if=\"key != null && key.type == 'select'\" :name=\"key.name\" :options=\"key.data\"\n                        style=\"width: 200px;\"></aj-select>\n                    <input v-if=\"key != null && !key.type\" type=\"text\" size=\"0\" :name=\"key\" />\n                </td>\n                <td class=\"control\">\n                    <span @click=\"addNew\"><i class=\"fa fa-plus\" style=\"color:#080;\"></i> \u65B0\u589E</span>\n                    <span @click=\"$parent.showAddNew = false\"><i class=\"fa fa-undo\" style=\"color:#bc49eb;\"></i> \u64A4\u9500</span>\n                </td>\n            </tr>\n        "], ["\n            <tr class=\"aj-grid-inline-edit-row isEditMode\">\n                <td></td>\n                <td></td>\n                <td v-for=\"key in columns\" style=\"padding:0\" class=\"cell\">\n                    <aj-select v-if=\"key != null && key.type == 'select'\" :name=\"key.name\" :options=\"key.data\"\n                        style=\"width: 200px;\"></aj-select>\n                    <input v-if=\"key != null && !key.type\" type=\"text\" size=\"0\" :name=\"key\" />\n                </td>\n                <td class=\"control\">\n                    <span @click=\"addNew\"><i class=\"fa fa-plus\" style=\"color:#080;\"></i> \u65B0\u589E</span>\n                    <span @click=\"$parent.showAddNew = false\"><i class=\"fa fa-undo\" style=\"color:#bc49eb;\"></i> \u64A4\u9500</span>\n                </td>\n            </tr>\n        "]));
                    _this.props = {
                        columns: { type: Array, required: true },
                        createApi: { type: String, required: false, default: '.' }
                    };
                    /**
                     * 创建的 API 地址
                     */
                    _this.createApi = "";
                    _this.$parent = null;
                    return _this;
                }
                /**
                 * 新增按钮事件
                 */
                EditRowCreate.prototype.addNew = function () {
                    var _this = this;
                    var map = {}; // 创建动作的表单数据
                    this.$el.$('*[name]', function (i) { return map[i.name] = i.value; });
                    this.BUS.$emit('before-add-new', map);
                    aj.xhr.post(this.createApi, function (j) {
                        if (j && j.isOk) {
                            aj.msg.show('新建实体成功');
                            _this.$el.$('input[name]', function (i) {
                                i.value = '';
                            });
                            if (_this.$parent) {
                                _this.$parent.reload();
                                _this.$parent.showAddNew = false;
                            }
                        }
                        else if (j && j.msg) {
                            aj.msg.show(j.msg);
                        }
                    }, map);
                };
                return EditRowCreate;
            }(aj.VueComponent));
            new EditRowCreate().register();
        })(grid = list.grid || (list.grid = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var list;
    (function (list) {
        var grid;
        (function (grid) {
            Vue.component('foo', {
                template: '<div>foo</div>'
            });
            /**
             * 行的 UI
             */
            var GridEditRow = /** @class */ (function (_super) {
                __extends(GridEditRow, _super);
                function GridEditRow() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = "aj-grid-inline-edit-row";
                    _this.template = html(__makeTemplateObject(["\n            <tr class=\"aj-grid-inline-edit-row\" :class=\"{editing: isEditMode}\">\n                <td v-if=\"showCheckboxCol\" class=\"selectCheckbox\">\n                    <input type=\"checkbox\" @change=\"selectCheckboxChange\" :data-id=\"id\" />\n                </td>\n                <td v-if=\"showIdCol\">{{id}}</td>\n                <td v-for=\"cellRenderer in columns\" :style=\"styleModifly\" class=\"cell\" @dblclick=\"dbEdit\">\n                    <span v-if=\"!isEditMode\" v-html=\"renderCell(rowData, cellRenderer)\"></span>\n                    <input v-if=\"canEdit(cellRenderer)\" v-model=\"rowData[cellRenderer]\" type=\"text\" size=\"0\" />\n                    <span v-if=\"cellRenderer && cellRenderer.cmpName\">\n                        <component v-if=\"!isEditMode || !cellRenderer.editMode\" v-bind:is=\"cellRenderer.cmpName\" v-bind=\"cellRenderer.cmpProps(rowData)\"></component>\n                        <component v-if=\"isEditMode && cellRenderer.editMode\" v-bind:is=\"cellRenderer.editCmpName\" v-bind=\"cellRenderer.cmpProps(rowData)\">\n                                    </component> \n                    </span>\n                </td>\n                <td v-if=\"showControl\" class=\"control\">\n                    <aj-cell-renderer v-if=\"controlUi\" :html=\"controlUi\" :form=\"rowData\"></aj-cell-renderer>\n                    <span @click=\"onEditClk\" class=\"edit\"><i class=\"fa fa-pencil\" aria-hidden=\"true\"></i>\n                        {{!isEditMode ? \"\u7F16\u8F91\" : \"\u786E\u5B9A\"}}</span>\n                    <span @click=\"dele\" class=\"delete\"><i class=\"fa fa-times\" aria-hidden=\"true\"></i> \u5220\u9664</span>\n                </td>\n            </tr>"], ["\n            <tr class=\"aj-grid-inline-edit-row\" :class=\"{editing: isEditMode}\">\n                <td v-if=\"showCheckboxCol\" class=\"selectCheckbox\">\n                    <input type=\"checkbox\" @change=\"selectCheckboxChange\" :data-id=\"id\" />\n                </td>\n                <td v-if=\"showIdCol\">{{id}}</td>\n                <td v-for=\"cellRenderer in columns\" :style=\"styleModifly\" class=\"cell\" @dblclick=\"dbEdit\">\n                    <span v-if=\"!isEditMode\" v-html=\"renderCell(rowData, cellRenderer)\"></span>\n                    <input v-if=\"canEdit(cellRenderer)\" v-model=\"rowData[cellRenderer]\" type=\"text\" size=\"0\" />\n                    <span v-if=\"cellRenderer && cellRenderer.cmpName\">\n                        <component v-if=\"!isEditMode || !cellRenderer.editMode\" v-bind:is=\"cellRenderer.cmpName\" v-bind=\"cellRenderer.cmpProps(rowData)\"></component>\n                        <component v-if=\"isEditMode && cellRenderer.editMode\" v-bind:is=\"cellRenderer.editCmpName\" v-bind=\"cellRenderer.cmpProps(rowData)\">\n                                    </component> \n                    </span>\n                </td>\n                <td v-if=\"showControl\" class=\"control\">\n                    <aj-cell-renderer v-if=\"controlUi\" :html=\"controlUi\" :form=\"rowData\"></aj-cell-renderer>\n                    <span @click=\"onEditClk\" class=\"edit\"><i class=\"fa fa-pencil\" aria-hidden=\"true\"></i>\n                        {{!isEditMode ? \"\u7F16\u8F91\" : \"\u786E\u5B9A\"}}</span>\n                    <span @click=\"dele\" class=\"delete\"><i class=\"fa fa-times\" aria-hidden=\"true\"></i> \u5220\u9664</span>\n                </td>\n            </tr>"]));
                    _this.props = {
                        initRowData: { type: Object, required: true },
                        showIdCol: { type: Boolean, default: true },
                        showCheckboxCol: { type: Boolean, default: true },
                        showControl: { type: Boolean, default: true },
                        enableInlineEdit: { type: Boolean, default: false },
                        columns: Array,
                        filterField: Array,
                        deleApi: String,
                        controlUi: String // 自定义“操作”按钮，这里填组件的名字
                    };
                    /**
                     * 固定不可编辑的字段
                     */
                    _this.filterField = [];
                    /**
                     * 每行记录它的 id
                     */
                    _this.id = "";
                    /**
                     * 输入的数据
                     */
                    _this.initRowData = {};
                    /**
                     * 每行记录的数据
                     */
                    _this.rowData = {};
                    /**
                     * 是否处于编辑模式
                     */
                    _this.isEditMode = false;
                    /**
                     * 表格是否可以被编辑
                     */
                    _this.enableInlineEdit = false;
                    /**
                     * 单元格渲染器的类型，这是一个有序的数组
                     */
                    _this.columns = [];
                    _this.$parent = null;
                    _this.computed = {
                        // filterData(this: GridEditRow) {// dep
                        //     let data = JSON.parse(JSON.stringify(this.rowData));// 剔除不要的字段
                        //     delete data.id;
                        //     delete data.dirty;
                        //     if (this.filterField && this.filterField.length)
                        //         this.filterField.forEach(i => delete data[i]);
                        //     return data;
                        // },
                        /**
                         * 修改样式
                         *
                         * @param this
                         */
                        styleModifly: function () {
                            return {
                                padding: this.isEditMode ? 0 : '',
                            };
                        }
                    };
                    return _this;
                }
                GridEditRow.prototype.data = function () {
                    return {
                        id: this.initRowData.id,
                        rowData: this.initRowData,
                        isEditMode: false
                    };
                };
                GridEditRow.prototype.mounted = function () {
                    for (var i in this.rowData) // 监视每个字段
                        this.$watch('rowData.' + i, makeWatch.call(this, i));
                };
                /**
                 * 选区模型的写入，记录哪一行被选中了
                 *
                 * @param ev 事件对象
                 */
                GridEditRow.prototype.selectCheckboxChange = function (ev) {
                    var checkbox = ev.target, parent = this.$parent;
                    if (parent) {
                        if (checkbox.checked)
                            parent.$set(parent.selected, this.id, true);
                        else
                            parent.$set(parent.selected, this.id, false);
                    }
                };
                /**
                 * 渲染单元格
                 *
                 * @param data
                 * @param cellRenderer
                 */
                GridEditRow.prototype.renderCell = function (data, cellRenderer) {
                    var v = "";
                    if (cellRenderer === '')
                        return v;
                    if (typeof cellRenderer == 'string')
                        v = data[cellRenderer] + "";
                    if (typeof cellRenderer == 'function')
                        v = cellRenderer(data);
                    if (typeof cellRenderer == 'object') {
                        var cfg = cellRenderer;
                        if (!!cfg.renderer)
                            v = cfg.renderer(data);
                    }
                    return v;
                };
                /**
                 * 没有指定编辑器的情况下，使用 input 作为编辑器
                 *
                 * @param cellRenderer
                 */
                GridEditRow.prototype.canEdit = function (cellRenderer) {
                    return this.isEditMode && !isFixedField.call(this, cellRenderer) && !(cellRenderer.editMode);
                };
                /**
                 * 渲染编辑模式下的行
                 *
                 * @param data
                 * @param cellRenderer
                 */
                GridEditRow.prototype.rendererEditMode = function (data, cellRenderer) {
                    if (typeof cellRenderer === 'string')
                        return cellRenderer.toString();
                    var cfg = cellRenderer;
                    if (cfg.editMode && typeof cfg.editRenderer === 'function')
                        return cfg.editRenderer(data);
                    return "NULL";
                };
                /**
                 * 编辑按钮事件
                 */
                GridEditRow.prototype.onEditClk = function () {
                    if (this.enableInlineEdit)
                        this.isEditMode = !this.isEditMode;
                    //@ts-ignore    
                    else if (this.$parent.onEditClk) // 打开另外的编辑界面
                        //@ts-ignore    
                        this.$parent.onEditClk(this.id);
                };
                /**
                 * 双击单元格进入编辑
                 *
                 * @param ev
                 */
                GridEditRow.prototype.dbEdit = function (ev) {
                    this.onEditClk();
                    if (this.enableInlineEdit && this.isEditMode) {
                        var el_1 = ev.target;
                        if (el_1.tagName !== 'TD')
                            el_1 = el_1.up('td');
                        setTimeout(function () {
                            var _el;
                            if (el_1.tagName !== 'INPUT')
                                _el = el_1.$('input');
                            _el && _el.focus();
                        }, 200);
                    }
                };
                /**
                 * 删除记录
                 */
                GridEditRow.prototype.dele = function (id) {
                    var _this = this;
                    aj.showConfirm("\u786E\u5B9A\u5220\u9664\u8BB0\u5F55 id:[" + this.id + "] \u5417\uFF1F", function () {
                        if (_this.$parent)
                            aj.xhr.dele(_this.$parent.apiUrl + "/" + _this.id + "/", function (j) {
                                if (j.isOk) {
                                    aj.msg.show('删除成功');
                                    _this.$parent && _this.$parent.reload();
                                }
                            });
                    });
                };
                return GridEditRow;
            }(aj.VueComponent));
            new GridEditRow().register();
            /**
             * 生成该字段的 watch 函数
             *
             * @param this
             * @param field
             */
            function makeWatch(field) {
                return function (_new) {
                    if (this.$parent) {
                        var arr = this.$parent.list, data = void 0;
                        for (var i = 0, j = arr.length; i < j; i++) { // 已知 id 找到原始数据
                            if (this.id && (String(arr[i].id) == this.id)) {
                                data = arr[i];
                                break;
                            }
                        }
                        if (!data)
                            throw '找不到匹配的实体！目标 id: ' + this.id;
                        if (!data.dirty)
                            data.dirty = { id: this.id };
                        data.dirty[field] = _new; // 保存新的值，key 是字段名
                    }
                };
            }
            /**
             * 是否固定的字段，固定的字段不能被编辑
             *
             * @param this
             * @param cellRenderer
             */
            function isFixedField(cellRenderer) {
                if (this.filterField && this.filterField.length) {
                    for (var i = 0, j = this.filterField.length; i < j; i++) {
                        if (this.filterField[i] == cellRenderer)
                            return true;
                    }
                }
                return false;
            }
        })(grid = list.grid || (list.grid = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";

var aj;
(function (aj) {
    var list;
    (function (list) {
        var grid;
        (function (grid) {
            /**
             * 工具条
             */
            Vue.component('aj-entity-toolbar', {
                template: html(__makeTemplateObject(["\n            <div class=\"toolbar\">\n                <form v-if=\"search\" class=\"right\">\n                    <input type=\"text\" name=\"keyword\" placeholder=\"\u8BF7\u8F93\u5165\u5173\u952E\u5B57\" size=\"12\" />\n                    <button @click=\"doSearch\"><i class=\"fa fa-search\" style=\"color:#417BB5;\"></i>\u641C\u7D22</button>\n                </form>\n                <aj-form-between-date v-if=\"betweenDate\" class=\"right\"></aj-form-between-date>\n                <ul>\n                    <li v-if=\"create\" @click=\"$emit('on-create-btn-clk')\"><i class=\"fa fa-plus\" style=\"color:#0a90f0;\"></i> \u65B0\u5EFA</li>\n                    <li v-if=\"save\" @click=\"$emit('on-save-btn-clk')\"><i class=\"fa fa-floppy-o\"\n                            style=\"color:rgb(205, 162, 4);\"></i>\u4FDD\u5B58</li>\n                    <li v-if=\"deleBtn\" @click=\"$emit('on-delete-btn-clk')\"><i class=\"fa fa-trash-o\" style=\"color:red;\"></i> \u5220\u9664</li>\n                    <li v-if=\"excel\"><i class=\"fa fa-file-excel-o\" style=\"color:green;\"></i> \u5BFC\u51FA</li>\n                    <slot></slot>\n                </ul>\n            </div>\n        "], ["\n            <div class=\"toolbar\">\n                <form v-if=\"search\" class=\"right\">\n                    <input type=\"text\" name=\"keyword\" placeholder=\"\u8BF7\u8F93\u5165\u5173\u952E\u5B57\" size=\"12\" />\n                    <button @click=\"doSearch\"><i class=\"fa fa-search\" style=\"color:#417BB5;\"></i>\u641C\u7D22</button>\n                </form>\n                <aj-form-between-date v-if=\"betweenDate\" class=\"right\"></aj-form-between-date>\n                <ul>\n                    <li v-if=\"create\" @click=\"$emit('on-create-btn-clk')\"><i class=\"fa fa-plus\" style=\"color:#0a90f0;\"></i> \u65B0\u5EFA</li>\n                    <li v-if=\"save\" @click=\"$emit('on-save-btn-clk')\"><i class=\"fa fa-floppy-o\"\n                            style=\"color:rgb(205, 162, 4);\"></i>\u4FDD\u5B58</li>\n                    <li v-if=\"deleBtn\" @click=\"$emit('on-delete-btn-clk')\"><i class=\"fa fa-trash-o\" style=\"color:red;\"></i> \u5220\u9664</li>\n                    <li v-if=\"excel\"><i class=\"fa fa-file-excel-o\" style=\"color:green;\"></i> \u5BFC\u51FA</li>\n                    <slot></slot>\n                </ul>\n            </div>\n        "])),
                props: {
                    betweenDate: { type: Boolean, default: true },
                    create: { type: Boolean, default: true },
                    save: { type: Boolean, default: true },
                    excel: { type: Boolean, default: false },
                    deleBtn: { type: Boolean, default: true },
                    search: { type: Boolean, default: true }
                },
                methods: {
                    /**
                     * 获取关键字进行搜索
                     *
                     * @param this
                     * @param ev
                     */
                    doSearch: function (ev) {
                        ev.preventDefault();
                        aj.apply(this.$parent.$store.extraParam, { keyword: aj.form.utils.getFormFieldValue(this.$el, 'input[name=keyword]') });
                        this.$parent.$store.reload();
                    }
                }
            });
        })(grid = list.grid || (list.grid = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var list;
    (function (list) {
        var tree;
        (function (tree) {
            /**
             * 寻找节点
             *
             * @param obj
             * @param queen
             * @returns
             */
            function findNodesHolder(obj, queen) {
                if (!queen.shift)
                    return null;
                var first = queen.shift();
                for (var i in obj) {
                    if (i === first) {
                        var target_1 = obj[i];
                        if (queen.length == 0) // 找到了
                            return target_1;
                        else
                            return findNodesHolder(obj[i], queen);
                    }
                }
                return null;
            }
            tree.findNodesHolder = findNodesHolder;
        })(tree = list.tree || (list.tree = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";

var aj;
(function (aj) {
    var tree;
    (function (tree) {
        /**
         * 下拉分类选择器，异步请求远端获取分类数据
         */
        var TreeLikeSelect = /** @class */ (function (_super) {
            __extends(TreeLikeSelect, _super);
            function TreeLikeSelect() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = 'aj-tree-like-select';
                _this.template = '<select :name="fieldName" class="aj-select" @change="onSelected" style="min-width:180px;"></select>';
                _this.props = {
                    fieldName: { type: String, required: false, default: 'catalogId' },
                    apiUrl: { type: String, default: function () { return aj.ctx + '/admin/tree-like/'; } },
                    isAutoLoad: { type: Boolean, default: true },
                    isAutoJump: Boolean,
                    initFieldValue: String
                };
                _this.apiUrl = "";
                /**
                 * 是否自动跳转 catalogId
                 */
                _this.isAutoJump = false;
                _this.isAutoLoad = true;
                _this.fieldName = "";
                _this.fieldValue = "";
                _this.initFieldValue = "";
                return _this;
            }
            TreeLikeSelect.prototype.data = function () {
                return {
                    fieldValue: this.initFieldValue
                };
            };
            TreeLikeSelect.prototype.mounted = function () {
                this.isAutoLoad && this.getData();
            };
            TreeLikeSelect.prototype.onSelected = function (ev) {
                var el = ev.target;
                this.fieldValue = el.selectedOptions[0].value;
                if (this.isAutoJump)
                    location.assign('?' + this.fieldName + '=' + this.fieldValue);
                else
                    this.BUS && this.BUS.$emit('aj-tree-catelog-select-change', ev, this);
            };
            TreeLikeSelect.prototype.getData = function () {
                var _this = this;
                var fn = function (j) {
                    var arr = [{ id: 0, name: "请选择分类" }];
                    rendererOption(arr.concat(j.result), _this.$el, _this.fieldValue, { makeAllOption: false });
                    if (_this.fieldValue) // 有指定的选中值
                        //@ts-ignore
                        aj.form.utils.selectOption.call(_this, _this.fieldValue);
                };
                // aj.xhr.get(this.ajResources.ctx + this.apiUrl + "/admin/tree-like/getListAndSubByParentId/", fn);
                aj.xhr.get(this.apiUrl, fn);
            };
            return TreeLikeSelect;
        }(aj.VueComponent));
        tree.TreeLikeSelect = TreeLikeSelect;
        new TreeLikeSelect().register();
        /**
         * 渲染 Option 标签的 DOM
         *
         * @param jsonArray
         * @param select
         * @param selectedId
         * @param cfg
         */
        function rendererOption(jsonArray, select, selectedId, cfg) {
            if (cfg && cfg.makeAllOption) {
                var option = document.createElement('option');
                option.value = option.innerHTML = "全部分类";
                select.appendChild(option);
            }
            var treeNode = tree.toTreeMap(jsonArray);
            if (treeNode) {
                // 生成 option
                var temp_1 = document.createDocumentFragment();
                console.log(treeNode);
                tree.output(treeNode, function (node, nodeId) {
                    var option = document.createElement('option'); // 节点
                    option.value = nodeId;
                    if (selectedId && selectedId == nodeId) // 选中的
                        option.selected = true;
                    option.dataset['pid'] = node.pid + "";
                    //option.style= "padding-left:" + (node.level - 1) +"rem;";
                    option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
                    temp_1.appendChild(option);
                });
                select.appendChild(temp_1);
            }
        }
        tree.rendererOption = rendererOption;
    })(tree = aj.tree || (aj.tree = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var list;
    (function (list) {
        var tree;
        (function (tree) {
            /**
             * 排序
             * 父id 必须在子 id 之前，不然下面 findParent() 找不到后面的父节点，故先排序
             *
             * @param jsonArray
             */
            function makeTree(jsonArray) {
                var arr = [];
                for (var i = 0, j = jsonArray.length; i < j; i++) {
                    var n = jsonArray[i];
                    if (n.pid === -1)
                        arr.push(n);
                    else {
                        var parentNode = findParentInArray(arr, n.pid);
                        if (parentNode) {
                            if (!parentNode.children)
                                parentNode.children = [];
                            parentNode.children.push(n);
                        }
                        else
                            console.log('parent not found!');
                    }
                }
                return arr;
            }
            tree.makeTree = makeTree;
            /**
             * 根据传入 id 在一个数组中查找父亲节点
             *
             * @param jsonArray
             * @param id
             */
            function findParentInArray(jsonArray, id) {
                for (var i = 0, j = jsonArray.length; i < j; i++) {
                    var map = jsonArray[i];
                    if (map.id == id)
                        return map;
                    if (map.children) {
                        var result = findParentInArray(map.children, id);
                        if (result != null)
                            return result;
                    }
                }
                return null;
            }
            tree.findParentInArray = findParentInArray;
            /**
             * 根据传入 id 查找父亲节点
             *
             * @param map
             * @param id
             */
            function findParentInMap(map, id) {
                for (var i in map) {
                    if (i == id)
                        return map[i];
                    var c = map[i].children;
                    if (c) {
                        for (var q = 0, p = c.length; q < p; q++) {
                            var result = findParentInMap(c[q], id);
                            if (result != null)
                                return result;
                        }
                    }
                }
                return null;
            }
            tree.findParentInMap = findParentInMap;
            var stack = [];
            /**
             * 遍历各个元素，输出
             *
             * @param map
             * @param cb
             */
            function output(map, cb) {
                stack.push(map);
                for (var i in map) {
                    map[i].level = stack.length; // 层数，也表示缩进多少个字符
                    cb(map[i], i);
                    var c = map[i].children;
                    if (c) {
                        for (var q = 0, p = c.length; q < p; q++)
                            output(c[q], cb);
                    }
                }
                stack.pop();
            }
            tree.output = output;
            /**
             * 生成树，将扁平化的数组结构 还原为树状的结构
             * 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故前提条件是，这个数组必须先排序
             *
             * @param jsonArray
             */
            function toTree(jsonArray) {
                if (!jsonArray)
                    return {};
                var m = {};
                for (var i = 0, j = jsonArray.length; i < j; i++) {
                    var n = jsonArray[i], parentNode = findParentInMap(m, n.pid + "");
                    if (parentNode == null) { // 没有父节点，那就表示这是根节点，保存之
                        m[n.id] = n; // id 是key，value 新建一对象
                    }
                    else { // 有父亲节点，作为孩子节点保存
                        var obj = {};
                        obj[n.id] = n;
                        if (!parentNode.children)
                            parentNode.children = [];
                        parentNode.children.push(obj);
                    }
                }
                return m;
            }
            tree.toTree = toTree;
            /**
             * 渲染 Option 标签的 DOM
             *
             * @param jsonArray
             * @param select
             * @param selectedId
             * @param cfg
             */
            function rendererOption(jsonArray, select, selectedId, cfg) {
                if (cfg && cfg.makeAllOption) {
                    var option = document.createElement('option');
                    option.value = option.innerHTML = "全部分类";
                    select.appendChild(option);
                }
                // 生成 option
                var temp = document.createDocumentFragment();
                output(toTree(jsonArray), function (node, nodeId) {
                    var option = document.createElement('option'); // 节点
                    option.value = nodeId;
                    if (selectedId && selectedId == nodeId) // 选中的
                        option.selected = true;
                    option.dataset['pid'] = node.pid + "";
                    //option.style= "padding-left:" + (node.level - 1) +"rem;";
                    option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
                    temp.appendChild(option);
                });
                select.appendChild(temp);
            }
            tree.rendererOption = rendererOption;
        })(tree = list.tree || (list.tree = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var list;
    (function (list) {
        var tree;
        (function (tree) {
            /**
             * 注意递归组件的使用
             */
            var TreeItem = /** @class */ (function (_super) {
                __extends(TreeItem, _super);
                function TreeItem() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = 'aj-tree-item';
                    _this.template = html(__makeTemplateObject(["\n            <li>\n                <div :class=\"{bold: isFolder, node: true}\" @click=\"toggle\">\n                    <span>\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7</span>{{model.name}}\n                    <span v-if=\"isFolder\">[{{open ? '-' : '+'}}]</span>\n                </div>\n                <ul v-show=\"open\" v-if=\"isFolder\" :class=\"{show: open}\">\n                    <aj-tree-item class=\"item\" v-for=\"(model, index) in model.children\" :key=\"index\" :model=\"model\">\n                    </aj-tree-item>\n                    <li v-if=\"allowAddNode\" class=\"add\" @click=\"addChild\">+</li>\n                </ul>\n            </li>\n        "], ["\n            <li>\n                <div :class=\"{bold: isFolder, node: true}\" @click=\"toggle\">\n                    <span>\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7</span>{{model.name}}\n                    <span v-if=\"isFolder\">[{{open ? '-' : '+'}}]</span>\n                </div>\n                <ul v-show=\"open\" v-if=\"isFolder\" :class=\"{show: open}\">\n                    <aj-tree-item class=\"item\" v-for=\"(model, index) in model.children\" :key=\"index\" :model=\"model\">\n                    </aj-tree-item>\n                    <li v-if=\"allowAddNode\" class=\"add\" @click=\"addChild\">+</li>\n                </ul>\n            </li>\n        "]));
                    _this.props = {
                        model: Object,
                        allowAddNode: { type: Boolean, default: false } // 是否允许添加新节点
                    };
                    _this.model = { children: [] };
                    _this.open = false;
                    _this.allowAddNode = false;
                    // isFolder = false;
                    _this.computed = {
                        isFolder: function () {
                            return !!(this.model.children && this.model.children.length);
                        }
                    };
                    return _this;
                }
                /**
                 * 点击节点时的方法
                 *
                 * @param this
                 */
                TreeItem.prototype.toggle = function () {
                    //@ts-ignore
                    if (this.isFolder)
                        this.open = !this.open;
                    this.BUS && this.BUS.$emit('tree-node-click', this.model);
                };
                /**
                 * 变为文件夹
                 *
                 * @param this
                 */
                TreeItem.prototype.changeType = function () {
                    //@ts-ignore
                    if (!this.isFolder) {
                        Vue.set(this.model, 'children', []);
                        this.addChild();
                        this.open = true;
                    }
                };
                TreeItem.prototype.addChild = function () {
                    this.model.children.push({
                        //@ts-ignore
                        name: 'new stuff'
                    });
                };
                return TreeItem;
            }(aj.VueComponent));
            tree.TreeItem = TreeItem;
            new TreeItem().register();
        })(tree = list.tree || (list.tree = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var tree;
    (function (tree) {
        var Tree = /** @class */ (function (_super) {
            __extends(Tree, _super);
            function Tree() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = 'aj-tree';
                _this.template = '<ul class="aj-tree"><aj-tree-item :model="treeData"></aj-tree-item></ul>';
                _this.props = {
                    apiUrl: String,
                    topNodeName: String
                };
                /**
                 * 根节点显示名称
                 */
                _this.topNodeName = "";
                _this.apiUrl = "";
                _this.isAutoLoad = false;
                _this.treeData = {
                    name: _this.topNodeName || 'TOP',
                    children: null
                };
                return _this;
            }
            Tree.prototype.getData = function () {
                var _this = this;
                aj.xhr.get(this.apiUrl, function (j) {
                    // @ts-ignore
                    _this.treeData.children = tree.toTreeArray(j.result);
                });
                // 递归组件怎么事件上报呢？通过事件 bus
                this.BUS && this.BUS.$on('treenodeclick', function (data) { return _this.$emit('treenodeclick', data); });
            };
            Tree.prototype.mounted = function () {
                this.getData();
            };
            return Tree;
        }(aj.VueComponent));
        tree.Tree = Tree;
        new Tree().register();
        /**
         * 注意递归组件的使用
         */
        var TreeItem = /** @class */ (function (_super) {
            __extends(TreeItem, _super);
            function TreeItem() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = 'aj-tree-item';
                _this.template = html(__makeTemplateObject(["\n                <li>\n                    <div :class=\"{bold: isFolder, node: true}\" @click=\"toggle\">\n                        <span>\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7</span>{{model.name}}\n                        <span v-if=\"isFolder\">[{{open ? '-' : '+'}}]</span>\n                    </div>\n                    <ul v-show=\"open\" v-if=\"isFolder\" :class=\"{show: open}\">\n                        <aj-tree-item class=\"item\" v-for=\"(model, index) in model.children\" :key=\"index\" :model=\"model\">\n                        </aj-tree-item>\n                        <li v-if=\"allowAddNode\" class=\"add\" @click=\"addChild\">+</li>\n                    </ul>\n                </li>\n            "], ["\n                <li>\n                    <div :class=\"{bold: isFolder, node: true}\" @click=\"toggle\">\n                        <span>\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7</span>{{model.name}}\n                        <span v-if=\"isFolder\">[{{open ? '-' : '+'}}]</span>\n                    </div>\n                    <ul v-show=\"open\" v-if=\"isFolder\" :class=\"{show: open}\">\n                        <aj-tree-item class=\"item\" v-for=\"(model, index) in model.children\" :key=\"index\" :model=\"model\">\n                        </aj-tree-item>\n                        <li v-if=\"allowAddNode\" class=\"add\" @click=\"addChild\">+</li>\n                    </ul>\n                </li>\n            "]));
                _this.props = {
                    model: Object,
                    allowAddNode: { type: Boolean, default: false } // 是否允许添加新节点
                };
                _this.model = { children: [] };
                _this.open = false;
                _this.allowAddNode = false;
                // isFolder = false;
                _this.computed = {
                    isFolder: function () {
                        return !!(this.model.children && this.model.children.length);
                    }
                };
                return _this;
            }
            /**
             * 点击节点时的方法
             */
            TreeItem.prototype.toggle = function () {
                //@ts-ignore
                if (this.isFolder)
                    this.open = !this.open;
                this.BUS && this.BUS.$emit('tree-node-click', this.model);
            };
            /**
             * 变为文件夹
             */
            TreeItem.prototype.changeType = function () {
                //@ts-ignore
                if (!this.isFolder) {
                    Vue.set(this.model, 'children', []);
                    this.addChild();
                    this.open = true;
                }
            };
            TreeItem.prototype.addChild = function () {
                this.model.children.push({
                    //@ts-ignore
                    name: 'new stuff'
                });
            };
            return TreeItem;
        }(aj.VueComponent));
        tree.TreeItem = TreeItem;
        new TreeItem().register();
    })(tree = aj.tree || (aj.tree = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var list;
    (function (list) {
        var tree;
        (function (tree) {
            /**
             * 寻找配置说明的节点
             *
             * @param obj
             * @param queen
             * @returns
             */
            function findNodesHolder(obj, queen) {
                if (!queen.shift)
                    return null;
                var first = queen.shift();
                for (var i in obj) {
                    if (i === first) {
                        var target_1 = obj[i];
                        if (queen.length == 0) // 找到了
                            return target_1;
                        else
                            return findNodesHolder(obj[i], queen);
                    }
                }
                return null;
            }
            tree.findNodesHolder = findNodesHolder;
        })(tree = list.tree || (list.tree = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var tree;
    (function (tree) {
        /**
         * 寻找节点
         *
         * @param obj
         * @param queue
         * @returns
         */
        function findNodesHolder(obj, queue) {
            if (!queue.shift)
                return null;
            var first = queue.shift();
            for (var i in obj) {
                if (i === first) {
                    var target_1 = obj[i];
                    if (queue.length == 0) // 找到了
                        return target_1;
                    else
                        return findNodesHolder(obj[i], queue);
                }
            }
            return null;
        }
        tree.findNodesHolder = findNodesHolder;
        //------------------------------------------------------------------------------
        /**
         * 根据传入 id 在一个数组中查找父亲节点
         *
         * @param map
         * @param id
         */
        function findParentInArray(arr, id) {
            for (var i = 0; i < arr.length; i++) {
                var n = arr[i];
                if (id == n.id)
                    return n;
                var c = n.children;
                if (c) {
                    var result = findParentInArray(c, id);
                    if (result != null)
                        return result;
                }
            }
            return null;
        }
        tree.findParentInArray = findParentInArray;
        /**
         * 生成树，将扁平化的数组结构 还原为树状的 Array结构
         * 父id 必须在子 id 之前，不然下面 findParent() 找不到后面的父节点，故先排序
         *
         * @param jsonArray
         */
        function toTreeArray(jsonArray) {
            var arr = [];
            for (var i = 0, j = jsonArray.length; i < j; i++) {
                var n = jsonArray[i];
                if (n.pid === -1)
                    arr.push(n);
                else {
                    var parentNode = findParentInArray(arr, n.pid);
                    if (parentNode) {
                        if (!parentNode.children)
                            parentNode.children = [];
                        parentNode.children.push(n);
                    }
                    else
                        console.log('parent not found!');
                }
            }
            return arr;
        }
        tree.toTreeArray = toTreeArray;
        //------------------------------------------------------------------------------
        /**
         * 根据传入 id 查找父亲节点
         *
         * @param map
         * @param id
         */
        function findParentInMap(map, id) {
            for (var i in map) {
                var obj = map[i];
                if (i == id)
                    return obj;
                if (obj === null)
                    continue;
                var c = obj.children;
                if (c) {
                    var result = findParentInMap(c, id);
                    if (result != null)
                        return result;
                }
            }
            return null;
        }
        tree.findParentInMap = findParentInMap;
        /**
         * 生成树，将扁平化的数组结构 还原为树状的 Map 结构
         * 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故前提条件是，这个数组必须先排序
         *
         * @param jsonArray
         */
        function toTreeMap(jsonArray) {
            if (!jsonArray)
                return null;
            var m = {};
            for (var i = 0, j = jsonArray.length; i < j; i++) {
                var n = jsonArray[i];
                if (n.pid === -1)
                    m[n.id] = n;
                else {
                    var parentNode = findParentInMap(m, n.pid + "");
                    if (parentNode) {
                        if (!parentNode.children)
                            parentNode.children = {};
                        parentNode.children[n.id] = n;
                    }
                    else
                        console.log('parent not found!');
                }
            }
            return m;
        }
        tree.toTreeMap = toTreeMap;
        var stack = [];
        /**
         * 遍历各个元素，输出
         *
         * @param map
         * @param cb
         */
        function output(map, cb) {
            stack.push(map);
            for (var i in map) {
                map[i].level = stack.length; // 层数，也表示缩进多少个字符
                cb && cb(map[i], i);
                var c = map[i].children;
                c && output(c, cb);
            }
            stack.pop();
        }
        tree.output = output;
    })(tree = aj.tree || (aj.tree = {}));
})(aj || (aj = {}));

