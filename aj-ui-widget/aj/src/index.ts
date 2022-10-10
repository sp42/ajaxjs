import * as sys_config from './util/system/config';
import * as util from './util/utils';
import { setBaseHeadParams } from './util/xhr';
import { xhr_get, xhr_post, xhr_put, xhr_del } from './util/xhr';

/**
 * API 统一前缀
 */
let API_BASE_URL: string;

/**
 * 暴露各个组件
 */
export default {
    system: sys_config,

    /**
     * 设置 API 统一前缀
     * 
     * @param perfix API 统一前缀
     */
    setApiBaseUrl(perfix: string): void {
        API_BASE_URL = perfix;
    },

    /**
     * 获取 API 统一前缀
     * 
     * @returns API 统一前缀
     */
    getApiBaseUrl(): string {
        return API_BASE_URL;
    },

    setBaseHeadParams,

    util: util,

    xhr: {
        xhr_get, xhr_post, xhr_put, xhr_del
    }
};