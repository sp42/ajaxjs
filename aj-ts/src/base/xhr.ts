/*
* -------------------------------------------------------- 
* 封装 XHR，支持
* GET/POST/PUT/DELETE/JSONP/FormData
* http://blog.csdn.net/zhangxin09/article/details/78879244
* --------------------------------------------------------
*/
namespace aj.xhr {
    /**
     * XHR 请求配置
     */
    export interface XHR_Config {
        /**
         * HTTP 请求方法
         */
        method?: string;

        /**
         * true 表示为不进行表单验证
         */
        noFormValid?: boolean;

        /**
         * true 表示为启用 Google 验证
         */
        googleReCAPTCHA?: boolean;

        /**
         * 提交之前的触发的事件
         */
        beforeSubmit?: (form: HTMLFormElement, json: JsonParam) => boolean;

        /**
         * 表单中要忽略的字段
         */
        ignoreField?: String;

        /**
         * XHR 响应结果的类型
         */
        parseContentType?: 'text' | 'xml' | 'json';
    }

    /**
     * XHR 回调函数
     */
    interface XHR_Callback extends Function {
        /**
         * 保存 url 以便记录请求路径，可用于调试
         */
        url?: string;
    }

    /**
     * JSON 转换为 URL
     * 
     * @param json      JSON
     * @param appendUrl 附加的地址
     */
    export function json2url(json: { [key: string]: any }, appendUrl?: string): string {
        let params: string[] = new Array<string>();

        for (var i in json)
            params.push(i + '=' + json[i]);

        let _params: string = params.join('&');

        if (appendUrl) // 如果有 ? 则追加，否则加入 ?
            _params = ~appendUrl.indexOf('?') ? appendUrl + '&' + params : appendUrl + '?' + params;

        return _params;
    }

    /**
     * 
     * 
     * @param url   注意 url 部分带有 # 的话则不能传参数过来
     * @param cb 
     * @param args 
     * @param method 
     * @param cfg 
     */
    export function request(url: string, cb: XHR_Callback, args?: JsonParam, method: string = "GET", cfg?: XHR_Config): void {
        let params: string = args ? json2url(args) : "";
        let xhr: XMLHttpRequest = new XMLHttpRequest();

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
        } else
            xhr.send(null);
    }

    /**
     * 表单序列化，兼容旧浏览器和 H5 FormData，返回 JSON
     * 
     * @param form 
     * @param cfg 
     */
    export function serializeForm(form: HTMLFormElement, cfg: XHR_Config): JsonParam {
        var json: { [key: string]: string } = {};
        let formData: FormData = new FormData(form);

        formData.forEach((value: FormDataEntryValue, name: string) => {
            if (cfg && cfg.ignoreField != name) // 忽略的字段
                json[name] = encodeURIComponent(value.toString());
        });

        return json;
    }

    /**
     * 
     * @param form 
     * @param cb 
     * @param cfg 
     */
    export function form(form: string | HTMLFormElement, cb: XHR_Callback, cfg: XHR_Config) {
        cb = cb || defaultCallBack;
        cfg = cfg || {};

        if (!form) return;

        if (typeof form == 'string')
            form = <HTMLFormElement>document.body.$(form);

        if (!form.action)
            throw 'Please fill the url in ACTION attribute.';

        !cfg.noFormValid && aj.form.Validator(form);

        if (cfg.googleReCAPTCHA) {// 加载脚本
            let script: HTMLScriptElement = <HTMLScriptElement>document.body.$("#googleReCAPTCHA");

            if (!script) {
                let src: string = 'https://www.recaptcha.net/recaptcha/api.js?render=';
                src += cfg.googleReCAPTCHA;
                aj.loadScript(src, 'googleReCAPTCHA');
            }
        }

        // @ts-ignore
        form.addEventListener('submit', formSubmit.delegate(null, cb, cfg));

        let returnBtn: HTMLButtonElement = <HTMLButtonElement>form.$('button.returnBtn'); // shorthand for back btn
        if (returnBtn)
            returnBtn.onclick = goBack;
    }


    function formSubmit(e: Event, cb: XHR_Callback, cfg: XHR_Config): void {
        e.preventDefault();// 禁止 form 默认提交
        var form: HTMLFormElement = <HTMLFormElement>e.target;

        // form.method always GET, so form.getAttribute('method') instead
        let method = form.getAttribute('method');
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
            grecaptcha.ready(() => {
                // @ts-ignore
                grecaptcha.execute(cfg.googleReCAPTCHA, { action: 'submit' }).then((token: string) => {
                    // Add your logic to submit to your backend server here.
                    json.grecaptchaToken = token;

                    if (cfg.method == 'put')
                        put(form.action, cb, json);
                    else
                        post(form.action, cb, json);
                });
            });
        } else {
            if (cfg && cfg.method == 'put')
                put(form.action, cb, json);
            else
                post(form.action, cb, json);
        }

    }

    function goBack(e: Event) {
        e.preventDefault();
        history.back();
    }

    export var get = (url: string, cb: XHR_Callback, args?: JsonParam, cfg?: XHR_Config) => aj.xhr.request(url, cb, args, 'GET', cfg);
    export var post = (url: string, cb: XHR_Callback, args?: JsonParam, cfg?: XHR_Config) => aj.xhr.request(url, cb, args, 'POST', cfg);
    export var put = (url: string, cb: XHR_Callback, args?: JsonParam, cfg?: XHR_Config) => aj.xhr.request(url, cb, args, 'PUT', cfg);
    export var dele = (url: string, cb: XHR_Callback, args?: JsonParam, cfg?: XHR_Config) => aj.xhr.request(url, cb, args, 'DELETE', cfg);

    /**
     *  默认的回调，有专属的字段并呼叫专属的控件
     * 
     * @param j 
     * @param xhr 
     * @param onOK 
     * @param onFail 
     */
    export var defaultCallBack = function (j: RepsonseResult, xhr: XMLHttpRequest, onOK: Function, onFail: Function): void {
        if (j) {
            if (j.isOk) {
                onOK && onOK(j);
                aj.msg.show(j.msg || '操作成功！');
            } else {
                onFail && onFail(j);
                aj.msg(j.msg || '执行失败！原因未知！');
            }
        } else {
            onFail && onFail(j);
            aj.msg.show('ServerSide Error!');
        }
    }

    // export var defaultCallBack = defaultCallBack_cb.delegate(null);

    /**
     * 
     * @param event 
     * @param cb 
     * @param parseContentType 
     */
    function requestCallback(this: XMLHttpRequest, event: XMLHttpRequestEventTarget, cb: XHR_Callback, parseContentType: 'text' | 'xml' | 'json'): void {
        if (this.readyState === 4 && this.status === 200) {
            let responseText: string = this.responseText.trim();
            let data = null;

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
                        } catch (e) {
                            try {
                                data = eval("TEMP_VAR = " + responseText);  // for {ok: true}
                            } catch (e) {
                                throw e;
                            }
                        }
                }
            } catch (e) {
                alert('XHR 错误:\n' + e + '\nUrl is:' + cb.url); // 提示用户 异常
            }

            if (cb)
                cb(data, this);
            else
                throw '你未提供回调函数';
        }

        if (this.readyState === 4 && this.status == 500)
            alert('服务端 500 错误！');
    }
}