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
