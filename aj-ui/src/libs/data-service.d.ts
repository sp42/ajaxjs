/**
 * 数据服务的配置，在数据库中对应的字段保存（不是在 json 里面）
 */
declare type DataService_TableConfig = {
    apiRoot: string;

    /**
     * 表名
     */
    tableName: string;

    /**
     * URL 前缀
     */
    urlRoot: string;

    /**
     * 数据源 id
     */
    dataSourceId?: number;

    /**
     * 数据源 URL 前缀
     */
    dataSourceUrlDir: string;

    /**
     * URL 目录
     */
    urlDir: string;

    /**
     * 描述
     */
    name: string;

    fields: any;

    /**
     * 字段映射
     */
    fieldsMapping?: DataServiceFieldsMapping;

    /**
     * 所有字段的注解
     */
    fieldsComments?: any;

    /**
    * JSON 数据
    */
    json?: string;

    /**
     * 主键生成策略
     */
    keyGen:number;
}

/**
 * 创建 or 修改时候的配置
 */
type DataService_Writable = {
    /**
     * 动态解析字段
     */
    isDynamicParse: boolean;

    /**
     * 生成唯一 uid
     */
    addUuid: boolean;

    /**
     * 插入创建/修改日期
     */
    autoDate: boolean;
};

/**
 * 基础配置
 */
type DataService_DML_Base = {
    /**
     * 命令类型（在 others 时会有用）
     */
    type?: string;


    /**
     * 是否启用
     */
    enable: boolean;

    /**
     * SQL 语句
     */
    sql: string;

    /**
     * 子目录
     */
    dir: string;
};

/**
 * 单笔查询配置
 */
type DataService_Cfg_Info = DataService_DML_Base & {
    /**
     * 是否返回表所有字段
     */
    isReadAllField: boolean;

    /**
     * 已选择字段
     */
    fields: string[];
};

/**
 * 列表查询配置
 */
type DataService_Cfg_List = DataService_DML_Base & {
    /**
     * 分页模式，0=不分页;1=MySQL的 start/limit;2=pageNo/pageSize
     */
    pageMode: 0 | 1 | 2;

    /**
     * 是否返回表所有字段
     */
    isReadAllField: boolean;

    /**
     * 已选择字段
     */
    fields: string[];

    /**
     * 允许条件搜索
     */
    isQuerySearch: boolean;
};

/**
 * 创建配置
 */
type DataService_Cfg_Create = DataService_DML_Base & {
    /**
     * 动态解析字段
     */
    isDynamicParse: boolean;

    /**
     * 已选择字段
     */
    fields: string[];

    /**
     * 插入创建/修改日期
     */
    autoDate: boolean;

    /**
     * 生成唯一 uid
     */
    addUuid: boolean;

    /**
     * 当有 id 参数时采用“修改”的规则
     */
    createOrSave: boolean;
};

/**
 * 修改配置
 */
type DataService_Cfg_Update = DataService_DML_Base & {
    /**
     * 动态解析字段
     */
    isDynamicParse: boolean;

    /**
     * 已选择字段
     */
    fields: string[];

    /**
     * 更新修改日期
     */
    isUpdateDate: boolean;
};

/**
 * 删除配置
 */
type DataService_Cfg_Delete = DataService_DML_Base & {
    /**
     * 是否物理删除
     */
    isPhysicallyDelete: boolean;
};

/**
 * 
 */
type DataServiceFieldsMapping = {
    id: string;
    createDate: string;
    createUser: string;
    updateDate: string;
    updateUser: string;
    delStatus: string;
};