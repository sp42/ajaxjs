import { XhrConfig } from './xhr-config';

/**
 * 默认的请求配置
 */
const DEFAULT_XHR_CFG: XhrConfig = {
    timeout: 5000,
    withCredentials: false,
    parseContentType: 'json'
};

/**
 * 处理响应的回调函数
 */
type XhrCallback = (json: {}, text: string) => void;

/**
 * 全局请求的 head 参数
 */
let BASE_HEAD_PARAMS = null;

/**
 * 设置全局请求的 head 参数
 * 
 * @param param 
 */
export function setBaseHeadParams(params: any): void {
    if (BASE_HEAD_PARAMS === null)
        BASE_HEAD_PARAMS = {};

    Object.assign(BASE_HEAD_PARAMS, params);
}

/**
 * 
 * @param getOrDel 
 * @param url 
 * @param cb 
 * @param params 
 * @param cfg 
 */
function getOrDel(getOrDel: 'get' | 'delete', url: string, cb: XhrCallback, params?: {}, cfg: XhrConfig = DEFAULT_XHR_CFG): void {
    let xhr: XMLHttpRequest = initXhr(cfg);

    if (params != null) {
        if (url.indexOf('?') != -1)
            url += '&' + toParams(params);
        else
            url += '?' + toParams(params);
    }

    xhr.open(getOrDel.toUpperCase(), url, true);
    xhr.onreadystatechange = function () {
        responseHandle(this, cb, cfg);
    }

    if (BASE_HEAD_PARAMS)// 设置自定义请求头
        for (let key in BASE_HEAD_PARAMS)
            xhr.setRequestHeader(key, BASE_HEAD_PARAMS[key]);

    setAuthHeader(xhr);

    xhr.send();
}

function setAuthHeader(xhr: XMLHttpRequest) {
    const token: string = localStorage.getItem("accessToken");

    if (token) {
        const json = JSON.parse(token);
        xhr.setRequestHeader("Authorization", "Bearer " + json.id_token);
    }
}

/**
 * 
 * @param method 
 * @param url 
 * @param cb 
 * @param params 
 * @param cfg 
 */
function postOrPut(method: 'post' | 'put', url: string, cb: XhrCallback, params: string | {}, cfg: XhrConfig = DEFAULT_XHR_CFG): void {
    let xhr: XMLHttpRequest = initXhr(cfg);
    xhr.open(method, url, true);
    xhr.onreadystatechange = function () {
        responseHandle(this, cb, cfg);
    }

    if (BASE_HEAD_PARAMS) // 设置自定义请求头
        for (let key in BASE_HEAD_PARAMS)
            xhr.setRequestHeader(key, BASE_HEAD_PARAMS[key]);

    // 此方法必须在 open() 方法和 send() 之间调用

    if (!cfg.contentType) // 如未设置，默认为表单请求
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    else
        xhr.setRequestHeader("Content-Type", cfg.contentType);

    setAuthHeader(xhr);

    let _params: string = typeof params != 'string' ? toParams(params) : <string>params;

    if (_params)
        xhr.send(_params);
    else
        xhr.send();
}

/**
 * 
 * @param url 
 * @param cb 
 * @param params 
 * @param cfg 
 */
export function xhr_post_upload(url: string, cb: XhrCallback, params: Document | XMLHttpRequestBodyInit, cfg: XhrConfig = DEFAULT_XHR_CFG): void {
    let xhr: XMLHttpRequest = initXhr(cfg);
    xhr.open('post', url, true);
    xhr.onreadystatechange = function () {
        responseHandle(this, cb, cfg);
    }

    if (BASE_HEAD_PARAMS) // 设置自定义请求头
        for (let key in BASE_HEAD_PARAMS)
            xhr.setRequestHeader(key, BASE_HEAD_PARAMS[key]);

    // 什么 Content-Type 都不设置

    xhr.send(params);
}

/**
 * XHR GET 请求
 * 
 * @param url       请求地址
 * @param cb        回调函数 @example (json: {}, text: string) => void;
 * @param params    参数，必填，如无填空字符串 ""；参数类型是json；参数值会进行 URL 编码，最后附加到 QueryString 中
 * @param cfg       配置，可选的
 */
export function xhr_get(url: string, cb: XhrCallback, params?: {}, cfg: XhrConfig = DEFAULT_XHR_CFG): void {
    getOrDel('get', url, cb, params, cfg);
}

/**
 * XHR DELETE 请求
 * 
 * @param url       请求地址
 * @param cb        回调函数 @example (json: {}, text: string) => void;
 * @param params    参数，必填，如无填空字符串 ""；参数类型是json；参数值会进行 URL 编码，最后附加到 QueryString 中
 * @param cfg       配置，可选的
 */
export function xhr_del(url: string, cb: XhrCallback, params?: {}, cfg: XhrConfig = DEFAULT_XHR_CFG): void {
    getOrDel('delete', url, cb, params, cfg);
}

/**
 * XHR POST 请求
 * 
 * @param url       请求地址
 * @param cb        回调函数 @example (json: {}, text: string) => void;
 * @param params    参数，必填，如无填空字符串 ""；参数类型可以是字符串或 json；参数值会进行 URL 编码
 * @param cfg       配置，可选的
 */
export function xhr_post(url: string, cb: XhrCallback, params: string | {}, cfg: XhrConfig = DEFAULT_XHR_CFG): void {
    postOrPut('post', url, cb, params, cfg);
}

/**
 * XHR PUT 请求
 * 
 * @param url       请求地址
 * @param cb        回调函数 @example (json: {}, text: string) => void;
 * @param params    参数，必填，如无填空字符串 ""；参数类型可以是字符串或 json；参数值会进行 URL 编码
 * @param cfg       配置，可选的
 */
export function xhr_put(url: string, cb: XhrCallback, params: string | {}, cfg: XhrConfig = DEFAULT_XHR_CFG): void {
    postOrPut('put', url, cb, params, cfg);
}

/**
 * 初始化 XHR
 * 
 * @param cfg 
 * @returns 
 */
function initXhr(cfg: XhrConfig): XMLHttpRequest {
    let xhr: XMLHttpRequest = new XMLHttpRequest();

    if (cfg && cfg.timeout) {
        xhr.timeout = cfg.timeout;
        xhr.ontimeout = (e: ProgressEvent<EventTarget>) => console.error('系统异常，XHR 连接服务超时');
    }

    if (cfg && cfg.withCredentials)
        xhr.withCredentials = true;

    return xhr;
}

/**
 * 错误处理 
 * 
 * @param xhr 
 */
function errHandle(xhr: XMLHttpRequest): void {
    let msg: string;

    if (xhr.status <= 400)
        msg = '请求参数错误或者权限不足。';
    else if (xhr.status <= 500)
        msg = '服务端异常。';
    else
        msg = `未知异常，HTTP code:${xhr.status}。`;

    let respText: string = xhr.responseText;

    if (!respText)
        msg += " 服务端返回空的字符串！";

    if (respText[0] == '{') {
        // json
        let r: any = JSON.parse(respText);
        // @ts-ignore
        let loginUrl: string | null = window.loginUrl;

        if (loginUrl && xhr.status === 403 && r.error === 'forbidden' && confirm('token 已失效，是否跳到重新登录？')) {
            location.assign(loginUrl);
        } else
            r.error_description && console.error(msg, r.error_description);
    } else
        console.error(msg, respText);
}

/**
 * 响应处理
 * 
 * @param xhr 
 * @param cb 
 * @param cfg 
 */
function responseHandle(xhr: XMLHttpRequest, cb: XhrCallback, cfg: XhrConfig): void {
    if (xhr.readyState == 4) {
        if ((xhr.status >= 200 && xhr.status < 300) || xhr.status == 304) {
            let text: string = xhr.responseText;
            let json: any;

            if (!text)
                console.warn('服务端没有返回任何字符串');

            switch (cfg.parseContentType) {
                case 'text':
                    break;
                case 'xml':
                    json = xhr.responseXML;
                    break;
                case 'json':
                default:
                    try {
                        json = JSON.parse(text);
                    } catch (e) {
                        console.error('解析 JSON 时候发生错误，非法 JSON');
                        console.warn(e);
                    }
            }

            cb && cb(json, text);
        } else errHandle(xhr);
    }
}

/**
 * 对象转换为 URL 参数列表，用 & 分隔
 * 
 * @param {Object} param JSON 对象 
 * @returns URL 参数列表
 */
export function toParams(param: any): string {
    let result: string = "";

    for (let name in param) {
        if (typeof param[name] != "function")
            result += "&" + name + "=" + encodeURIComponent(param[name]);
    }

    return result.substring(1);
}

/**
 * 获取 QueryString 的某个参数
 * 
 * @param val 
 * @returns 
*/
export function getQuery(val: string): string {
    const w: number = location.hash.indexOf('?');
    const query: string = location.hash.substring(w + 1);
    let vars: string[] = query.split('&');

    for (let i = 0; i < vars.length; i++) {
        const pair = vars[i].split('=');

        if (pair[0] == val)
            return pair[1];
    }

    return '';
}

export function getPageList(self: any, listArray: any, callback?: Function): XhrCallback {
    return (j: JsonResponse) => {
        if (j.status) {
            listArray.total = j.total;
            listArray.data = j.data;

            callback && callback();
        } else
            self.$Message.warning(j.message || '获取数据失败');
    }
}