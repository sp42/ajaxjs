/**
 * 数据绑定的公用方法
 */

const API_ROOT_PREFIX = '{API_ROOT_PREFIX}';


/**
 * 请求前的准备
 * 
 * @param dataBinding   配置对象
 * @param params        请求参数，可选的。如果没有则创建一个 空对象
 * @param cmp           组件实例，可选的。用于 beforeRequest 函数指定 this 指针
 * @returns 请求参数
 */
export function prepareRequest(dataBinding: DataBinding, params?: any, cmp?: any): ManagedRequest {
    if (!dataBinding) {
        alert("未有数据绑定！");
        return;
    }

    if (!dataBinding.url) {
        alert("未有 API 地址接口");
        return;
    }

    let url: string = dataBinding.url;

    if (url.indexOf(API_ROOT_PREFIX) != -1)
        url = dataBinding.url.replace(API_ROOT_PREFIX, window['config'].dsApiRoot);

    if (!params)
        params = {};

    if (dataBinding.baseParams)
        Object.assign(params, JSON.parse(dataBinding.baseParams));

    if (dataBinding.beforeRequest) {
        let before: Function = new Function('params', dataBinding.beforeRequest);
        before.call(cmp || this, params);
    }

    return { url: url, params: params };
}
