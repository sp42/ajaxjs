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

    /**
     * URL 前缀
     */
    urlPrefix: string;

    /**
     * 
     */
    actived: boolean;
};

/**
 * HTTP 方法
 */
declare type API_HELPER_HTTP_METHOD = 'GET' | 'POST' | 'PUT' | 'DELETE' | 'PATCH' | 'OPTION';

/**
 * 历史记录
 */
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
    isDefault?: boolean;

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
    /**
     * 是否使用
     */
    isEnable: boolean;

    /**
     * 变量
     */
    name: string;

    /**
     * 参数说明
     */
    desc: string;

    /**
     * 参数位置
     */
    type: 'HEADER' | 'PATH' | 'QUERY' | 'BODY' | 'FORM';

    /**
     * 数据类型
     */
    dataType: any;

    /**
     * 对应后端语言的类型或者枚举
     */
    mappingType?: any;

    /**
     * 值，枚举值
     */
    values?: API_HELPER_ARGUMENT_ENU[];

    /**
     * 是否必填
     */
    isRequired: boolean;

    /**
     * 说明，描述
     */
    comment: string;
};

/**
 * JSON 类型参数
 */
declare type API_HELPER_JSON_ARGUMENT = API_HELPER_ARGUMENT & {
    /**
     * 类型
     */
    dataType: number | string | boolean | null | array | object;
};

/**
 * 文档
 */
declare type API_HELPER_DOCUMENT = {
    /**
     * ID
     */
    id: any;

    /**
     * 名称
     */
    name: string;

    /**
     * 分组
     */
    catalogId: any;

    /**
     * 说明
     */
    description: string;

    /**
     * HTTP 请求方法
     */
    httpMethod: API_HELPER_HTTP_METHOD;

    url: string;

    /**
     * 对应 UI，图片地址
     */
    ui: string;

    /**
     * 请求示例
     */
    demoUrl: string;

    /**
     * 接口编码状态
     */
    codeState: 'INITED' | 'DEFINED' | 'DONE';

    /**
     * 是否在文档汇总中展示
     */
    isShow: boolean;
};