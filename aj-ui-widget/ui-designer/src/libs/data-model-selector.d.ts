/**
 * JDBC 返回的数据库列的元数据
 */
type CheckableDataBaseColumnMeta = {
    /**
     * 是否主键
     */
     isKey?: boolean;

     /**
      *默认值
     */
    defaultValue?: string | number | boolean | null;

    /**
     * 列名称
     */
    name: string;

    /**
     * 数据类型
     */
    type: string;

    /**
     * 数据长度
     */
    length?: number;

    /**
     * 字段说明
     */
    comment: string;

    /**
     * 是否非空
     */
    null: "YES" | "NO";


    /**
     * 可否选择的
     */
    checked?: boolean;
};

/**
 * 选择了的表及其模型（各字段）
 */
type SelectedTable = {
    /**
     * 数据源 id
     */
    datasourceId: number;

    /**
     * 避免关联表查询，直接给出
     */
    datasourceName?: string;

    /**
     * 表名
     */
    tableName: string;

    /**
     * 表注释
     */
    comment?: string;

    /**
     * 多个字段
     */
    fields?: CheckableDataBaseColumnMeta[];

    /**
     * 单个字段
     */
    field?: CheckableDataBaseColumnMeta;
};