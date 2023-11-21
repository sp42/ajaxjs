/**
 * 后端响应的消息
 */
declare interface RepsonseResult {
    /**
     * 操作代码
     */
    code?: number;

    /**
     * 操作是否成功
     */
    status: boolean;

    /**
     * 操作说明
     */
    message: string;

    /**
     * 结果，实体，
     */
    data: any;

    /**
     * 进行新建的时候返回的实体 id
     */
    newlyId?: number;

    /**
     * 分页列表的总数
     */
    total?: number;
}

/**
 * JSON 接口返回的通用
 */
type JsonResponse = {
    /**
     * 1 = 成功，0 = 失败
     */
    status: number;

    /**
     * 信息
     */
    message: string;

    /**
     * 分页的总数
     */
    total?: number;

    /**
     * 返回数据
     */
    data: any;
};

// 官方 API 还没提供，自己写一个
interface FileReaderEventTarget extends EventTarget {
    result: string
}

interface FileReaderEvent extends Event {
    target: FileReaderEventTarget;
    getMessage(): string;
}

/**
 * JSON 实体
 */
declare type JsonParam = { [key: string]: string | number | boolean | JsonParam | any[] };

/**
 * key 和 value 都是 string 类型的 JSON 实体
 */
declare type StringJsonParam = { [key: string]: string };

/*
 * 公共类型
 */

declare type ManagedWidget = {
};

/**
 * 按钮事件
 */
declare type ButtonEvent = {
    /**
     * 按钮文本
     */
    name: string;

    /**
     * JS 事件代码
     */
    event: string;
}

/**
 * 数据绑定配置
 */
declare type DataBinding = {
    /**
     * 请求方法
     */
    httpMethod?: string;

    /**
     * 请求地址
     */
    url: string,

    /**
     * 固定参数
     */
    baseParams: string,

    /**
     * 动态获取参数的函数
     */
    beforeRequest: string;
};

/**
 * 解析过后的请求对象
 */
type ManagedRequest = {
    /**
     * 请求地址
     */
    url: string;

    /**
     * 请求参数
     */
    params: any;
};

/**
 * 模型与表单共有的字段
 */
declare type BaseModel = {
    /**
    * 字段名、Key
    */
    name: string;

    /**
     * 名称，UI 外显名称
     */
    label: string;

    /**
     * 说明
     */
    comment?: string;

    /**
    * 是否必填
    */
    isNull?: boolean;

    /**
     * 数据长度/最大长度
     */
    length?: number;

    /**
     * 其他扩展属性
     */
    ext_attribs?: any;
};

/**
 * 模型
 */
declare type Model = BaseModel & {
    /**
     * 数据类型
     */
    dataType?: string;

    /**
     * 是否主键
     */
    isKey?: boolean;

    /**
     *默认值
    */
    defaultValue?: string | number | boolean | null;

    /**
     * 是否跨表字段
     */
    isCrossTable?: boolean;
};