import main from './../../index';

/**
 * 根据配置名称（name/key）获取配置内容（value）
 * 
 * @param key           配置名称（name/key）
 * @param defaultValue  如果没有配置，默认使用的配置值。该参数可选的
 * @returns 配置内容（value）Promise 
 */
export async function getConfig(key: string, defaultValue?: string): Promise<ConfigJson> {
    return request<ConfigJson>({ url: 'getConfig/' + key });
}

/**
 * 
 */
type RequestConfig = {
    url: string,
    method?: "GET" | "POST" | "PUT" | "DELETE",
    param?: any
}

type ConfigJson = {
    code: number;
    data: any;
}

/**
* 请求网络
* @static
* @template T 指定响应实体
* @param {RequestConfig} option 请求url+参数+请求方式
* @return {*}  {Promise<T>}
*/
function request<T>(option: RequestConfig): Promise<T> {
    return new Promise((resolve, reject) => {
        option.url = `${main.getApiBaseUrl()}/${option.url}`;
        option.method = option.method || "GET";// 默认get
        // 如果是get请求把参数整合到url后面
        if (option.method === 'GET')
            formatUrl(option);

        let xhr: XMLHttpRequest = new XMLHttpRequest();
        xhr.responseType = 'json';

        xhr.onreadystatechange = function (e) {
            if (xhr.readyState === 4) {
                if (xhr.status !== 200)
                    return reject({ code: xhr.status, msg: 'request error' });// 可自行封装错误内容

                if (xhr.response?.code !== undefined) {
                    if (xhr.response.code === 200) {
                        let t: T = xhr.response.data;
                        return resolve(t);
                    } else
                        // 这里可以通过公共方法 把异常以弹窗形式抛出 如:showRequestError(xhr.response)
                        return reject(xhr.response);
                } else
                    return reject(xhr.response);
            }
        }

        xhr.open(option.method, option.url, true);
        if (option.method === 'POST')
            xhr.setRequestHeader('Content-Type', 'application/json')
        // xhr.setRequestHeader('token', '')

        xhr.send(option.method === 'POST' ? JSON.stringify(option.param) : null);
    });
}

/**
 * 格式化get请求url 
 * 
 * @param {RequestConfig} option
 */
function formatUrl(option: RequestConfig): void {
    let formData = [];
    for (let key in option.param)
        formData.push(''.concat(key, '=', option.param[key]))

    let formStr: string = formData.join('&');

    if (formData)
        option.url += option.url.indexOf('?') === -1 ? ''.concat('?', formStr) : ''.concat('&', formStr)
}