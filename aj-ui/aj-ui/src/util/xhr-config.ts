/**
 * XHR 请求配置
 */
export interface XhrConfig {
    /**
     * 请求头里面的 Content-Type 字段
     */
    contentType?: string;

    /**
     * XHR 响应结果的类型
     */
    parseContentType?: 'text' | 'xml' | 'json';

    /**
     * 超时时间，单位是毫秒
     * 设为 0 适合不控制超时
     */
    timeout?: number;

    /**
     * 是否跨域
     */
    withCredentials?: boolean;
}