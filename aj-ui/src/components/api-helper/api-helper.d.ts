/**
 * 表格定义
 */
declare type API_HELPER_TABLE = {
    mode: 'SIMPLE' | 'SCHEME';
};

/**
 * 环境
 */
declare type API_HELPER_ENV = {
    id: number;
    name: string;
    urlPrefix: string;
    actived: boolean;
};

/**
 * HTTP 方法
 */
declare type API_HELPER_HTTP_METHOD = 'GET' | 'POST' | 'PUT' | 'DELETE';

declare type API_HELPER_HISTORY = {
    id: number;

    /**
     * 任务名称，可选
     */
    name?: string;

    /**
     * 任务 id，可选
     */
    nameId: number;

    httpMethod: API_HELPER_HTTP_METHOD;

    url: string;

    /**
     * 请求日期
     */
    date: string;

    /**
     * 耗时
     */
    time: number;

    stateCode: number;
};

/**
 * 参数的枚举值
 */
declare type API_HELPER_ARGUMENT_ENU = {
    /**
     * 是否默认
     */
    isDefault: boolean;

    /**
     * 值
     */
    value: any;

    /**
     * 值是什么含义
     */
    comment: string;
};

/**
 * 参数
 */
declare type API_HELPER_ARGUMENT = {
    name: string;

    type: string;

    /**
     * 值，枚举值
     */
    values: API_HELPER_ARGUMENT_ENU[];

    /**
     * 是否必填
     */
    isRequired: boolean;

    /**
     * 说明
     */
    comment: string;
};