type Entity = {
    /**
     * 类型
     */
    type: string;

    /**
     * 数据长度
     */
    width: number;

    /**
     * 是否允许为空
     */
    isNotNull: boolean;

    /**
     * 默认值
     */
    defaultValue: any;

    /**
     * 校验规则
     */
    regexp: RegExp;

    /**
     * 注释说明
     */
    comment: string;
};