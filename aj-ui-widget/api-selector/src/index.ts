import * as sys_config from './util/system/config';
import * as util from './util/utils';
import { setBaseHeadParams } from './util/xhr';

import DataSource from './components/data-service/datasource/datasource.vue';
import DataService from './components/data-service/data-service.vue';
import DataServiceInfo from './components/data-service/info/info.vue';

import FactoryFormAdmin from './components/factory-form/list.vue';
import FactoryFormAdminInfo from './components/factory-form/form-factory.vue';
import FactoryFormLoader from './components/factory-form/loader.vue';

import FactoryListAdmin from './components/factory-list/list.vue';
import FactoryListAdminInfo from './components/factory-list/list-factory.vue';
import FactoryListLoader from './components/factory-list/list-loader.vue';

import FactoryExtForm from './components/ext-form/list.vue';

import ApiSelector from './components/api-selector/index.vue';
import { xhr_get, xhr_post, xhr_put, xhr_del } from './util/xhr';

import DeveloperTools from './components/admin-page/developer-tools/index.vue';
import SysConfig from './components/admin-page/system/config.vue';
import DataDict from './components/admin-page/system/data-dict.vue';
import OrgIndex from './components/admin-page/user/org/index.vue';
import RBAC from './components/admin-page/user/rbac/rbac.vue';
import WebsiteConfig from './components/admin-page/website/config.vue';
import FormDesigner from './components/form-designer/demo.vue';
import WorkflowDesigner from './components/work-flow/index.vue';

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
    },

    DataSource, DataService, DataServiceInfo,
    FactoryListLoader, FactoryListAdmin, FactoryListAdminInfo, FactoryFormAdmin, FactoryFormAdminInfo, FactoryFormLoader,
    FactoryExtForm, ApiSelector, DeveloperTools, SysConfig, DataDict, OrgIndex, RBAC, WebsiteConfig, FormDesigner, WorkflowDesigner
};