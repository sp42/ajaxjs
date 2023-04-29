// iView 组件的配置定义

//  cfg: any;

/**
 * iView Table 配置字段， data 字段
 */
type TableListConfig = {
    /**
     * 列定义
     */
    columns: any[];

    /**
     * 表格数据
     */
    data: any[];

    /**
     * 记录总数
     */
    total: number;

    /**
     * MySQL 分页方式 start
     */
    start?: number;

    /**
     * MySQL 分页方式 limit
     */
    limit?: number;

    /**
     * 页码，表示第几页
     */
    pageNo?: number;

    /**
     * 每页记录数，等同于 MySQL 分页方式 limit
     */
    pageSize?: number;

    /**
     * 表格处于加载中状态
     */
    loading?: boolean;
};

/**
 * iView Table 配置方法
 */
type TableListMethod = {
    /**
     * 异步加载列表数据
     */
    getData(): void;

    /**
     * 打开详情 UI
     * 
     * @param id 
     */
    openInfo?(id: number): void;

    /**
     * 删除
     * 
     * @param id 
     * @param index 行号
     */
    deleteInfo?(id: number, index: number): void;
};