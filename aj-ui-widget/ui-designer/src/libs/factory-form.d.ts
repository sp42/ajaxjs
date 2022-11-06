/**
 * 表单生成器的配置
 */
declare type FormFactory_Config = {
    /**
     * label 宽度
     */
    labelWidth: number;

    /**
     * 数据绑定配置（读取）
     */
    dataBinding: DataBinding;

    /**
     * 数据绑定配置（创建）
     */
    createApi?: DataBinding;
    
    /**
     * 数据绑定配置（修改）
     */
    updateApi?: DataBinding;

    /**
     * 是否创新/修改同一个接口，只是 HTTP 方法不同
     */
    isRESTful_writeApi: boolean;

    jsonBased?: JsonBased;

    /**
     * 是否显示表单按钮
     */
    isShowBtns: boolean;

    /**
     * 各个字段的配置
     */
    fields: FormFactory_ItemConfig[];
};

/**
 * JSON 表单的配置
 */
declare type JsonBased = {
    /**
     * 是否基于 JSON  新建的
     */
    isJsonBased: boolean;

    /**
     * JSON 为多层结构，须指定某个对象，这里指定一个字段
     */
    key: string;
};
/**
 * JSON 值类型
 */
declare type JsonType = 'string' | 'long_string' | 'number' | 'boolean' | undefined;

/**
 * 表单生成器各个字段的配置
 */
declare type FormFactory_ItemConfig = BaseModel & {
    /**
     * 是否显示
     */
    isShow: boolean;

    /**
     * JSON 类型
     */
    jsonType?: JsonType;

    /**
     * UI 类型
     */
    uiType: number;

    /**
     * UI 布局
     */
    uiLayout: number;

    /**
     * 校验的正则表达式
     */
    regexp?: string;

    /**
     * 验证错误时的信息
     */
    validMsg?: string;
};

