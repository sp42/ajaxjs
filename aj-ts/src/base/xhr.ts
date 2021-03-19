/*
* -------------------------------------------------------- 
* 封装 XHR，支持
* GET/POST/PUT/DELETE/FormData
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
         * XHR 响应结果的类型
         */
        parseContentType?: 'text' | 'xml' | 'json';
    }

    /**
     * 响应的原始数据，可以是下面的几种类型之一
     */
    type ResponseRawData = null | string | Document | JsonParam | RepsonseResult;

    /**
     * XHR 回调函数基类，私有的，请使用 XHR_Callback
     */
    type _XHR_Callback = (data: any, xhr?: XMLHttpRequest) => void;

    /**
     * XHR 回调函数
     */
    export interface XHR_Callback extends _XHR_Callback {
        /**
         * 保存 url 以便记录请求路径，可用于调试
         */
        url?: string;
    }

    /**
     * 执行请求，这是内部的函数
     * 
     * @param url       注意 url 部分带有 # 的话则不能传参数过来
     * @param cb        回调函数
     * @param args      请求参数
     * @param method    请求 HTTP 方法
     * @param cfg       配置
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
        xhr.onreadystatechange = requestHandler.delegate(null, cb, cfg && cfg.parseContentType);
        xhr.setRequestHeader('Accept', 'application/json');

        if (method == 'POST' || method == 'PUT') {
            xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
            xhr.send(params);
        } else
            xhr.send(null);
    }

    /**
     * JSON 转换为 URL。
     * 注意这个方法不会作任何编码处理。
     * 
     * @param json      JSON
     * @param appendUrl 附加的地址
     * @returns 如 a=1&b=true&c=foo 的参数字符串
     */
    export function json2url(json: JsonParam, appendUrl?: string): string {
        let params: string[] = new Array<string>();

        for (let i in json)
            params.push(i + '=' + json[i]);

        let _params: string = params.join('&');

        if (appendUrl) // 如果有 ? 则追加，否则加入 ?
            _params = ~appendUrl.indexOf('?') ? appendUrl + '&' + params : appendUrl + '?' + params;

        return _params;
    }

    /**
     * XHR 前期执行的回调函数，进行一些初始化的工作
     * 
     * @param this 
     * @param ev                XHR 事件，不使用
     * @param cb                回调函数
     * @param parseContentType  解析响应数据的类型
     */
    export function requestHandler(this: XMLHttpRequest, ev: XMLHttpRequestEventTarget, cb: XHR_Callback, parseContentType: 'text' | 'xml' | 'json'): void {
        if (this.readyState === 4 && this.status === 200) {
            let responseText: string = this.responseText.trim();
            let data: ResponseRawData = null;

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
                                data = eval("window.TEMP_VAR = " + responseText);  // for {ok: true}
                            } catch (e) {
                                throw e;
                            }
                        }
                }
            } catch (e) {
                window.alert(`XHR 错误:${e}\n访问地址是: ${cb.url}`); // 提示用户异常
            }

            cb(data, this);
        }

        if (this.readyState === 4 && this.status == 500)
            window.alert('服务端 500 错误！');
    }

    export var get = (url: string, cb: XHR_Callback, args?: JsonParam, cfg?: XHR_Config) => request(url, cb, args, 'GET', cfg);
    export var post = (url: string, cb: XHR_Callback, args?: JsonParam, cfg?: XHR_Config) => request(url, cb, args, 'POST', cfg);
    export var put = (url: string, cb: XHR_Callback, args?: JsonParam, cfg?: XHR_Config) => request(url, cb, args, 'PUT', cfg);
    export var dele = (url: string, cb: XHR_Callback, args?: JsonParam, cfg?: XHR_Config) => request(url, cb, args, 'DELETE', cfg);

    /**
     *  默认的回调，有专属的字段并呼叫专属的控件
     * 
     * @param j         响应结果 JSON
     * @param xhr       XHR 请求对象
     * @param onOK      当 isOk = true 时执行的回调函数
     * @param onFail    当 isOk = false 时执行的回调函数
     */
    export var defaultCallBack = function (j: RepsonseResult, xhr?: XMLHttpRequest, onOK?: XHR_Callback, onFail?: XHR_Callback): void {
        if (j) {
            if (j.result)
                // @ts-ignore
                j = j.result;

            if (j.isOk) {
                onOK && onOK(j);
                aj.msg.show(j.msg || '操作成功！');
            } else {
                onFail && onFail(j);
                aj.msg.show(j.msg || '执行失败！原因未知！');
            }
        } else {
            onFail && onFail(j);
            aj.msg.show('服务端执行错误，不是标准的消息体 ServerSide Error! ');
        }
    }

    // --------------------------------下面是 AJAX 表单的逻辑 -----------------------------------
    /**
     * 表单请求的配置参数
     */
    export interface XhrFormConfig extends XHR_Config {
        /**
         * 表单中要忽略的字段
         */
        ignoreField?: String;

        /**
         * true 表示为不进行表单验证
         */
        noFormValid?: boolean;

        /**
         * true 表示为启用 Google 防注册机验证
         */
        googleReCAPTCHA?: boolean;

        /**
         * 提交之前的触发的事件。
         * 如果返回 fasle 表示阻止提交表单
         */
        beforeSubmit?: (form: HTMLFormElement, json: StringJsonParam) => boolean;

    }

    /**
     * 初始化 AJAX 表单
     * 
     * @param form  表单元素，可以是 CSS 选择符，或者是 HTML 元素
     * @param cb    回调函数，可选的
     * @param cfg   表单请求的配置参数，可选的
     */
    export function form(form: cssSelector | HTMLFormElement, cb: XHR_Callback = defaultCallBack, cfg: XhrFormConfig = {}): void {
        if (!form) return;

        if (typeof form == 'string')
            form = <HTMLFormElement>document.body.$(form);

        if (!form.action)
            throw '请在 form 表单中指定 action 属性。Please fill the url in ACTION attribute.';

        if (!cfg.noFormValid)
            new aj.form.Validator(form);

        if (cfg.googleReCAPTCHA && !document.body.$("#googleReCAPTCHA")) // 加载脚本
            aj.loadScript(`https://www.recaptcha.net/recaptcha/api.js?render=${cfg.googleReCAPTCHA}`, 'googleReCAPTCHA');

        // @ts-ignore
        form.addEventListener('submit', formSubmit.delegate(null, cb, cfg));

        let returnBtn: HTMLButtonElement = <HTMLButtonElement>form.$('button.returnBtn'); // shorthand for back btn
        if (returnBtn)
            returnBtn.onclick = goBack;
    }

    /**
     * 执行表单的 XHR 请求
     * 通过拦截表单的 submit 事件触发。
     * @param ev    事件对象
     * @param cb    回调函数
     * @param cfg   表单请求的配置参数
     */
    function formSubmit(ev: Event, cb: XHR_Callback, cfg: XhrFormConfig): void {
        ev.preventDefault();// 禁止 form 默认提交
        let form: HTMLFormElement = <HTMLFormElement>ev.target,
            method = form.getAttribute('method');  // form.method always GET, so form.getAttribute('method') instead

        if (method)
            method = method.toLowerCase();

        cfg.method = method || cfg.method || 'post';

        if (!cfg.noFormValid && !aj.form.Validator.onSubmit(form))
            return;

        let json: StringJsonParam = serializeForm(form, cfg);

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
            if (cfg.method == 'put')
                put(form.action, cb, json);
            else
                post(form.action, cb, json);
        }
    }

    /**
     * 为表单里面的 返回按钮 添加后退的事件处理器
     * 
     * @param ev 
     */
    function goBack(ev: Event): void {
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
    function serializeForm(form: HTMLFormElement, cfg: XhrFormConfig): StringJsonParam {
        let json: StringJsonParam = {}, formData: FormData = new FormData(form);

        formData.forEach((value: FormDataEntryValue, name: string): void => {
            if (cfg && cfg.ignoreField != name) // 忽略的字段
                json[name] = encodeURIComponent(value.toString());
        });

        return json;
    }
}