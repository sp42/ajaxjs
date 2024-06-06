/**
 * 抽象列配置
 */
type BaseCol = {
    /**
     * json 的 key
     */
    key: string;

    /**
     * 标题
     */
    title: string;

    /**
     * 对齐方式
     */
    align?: 'left' | 'center' | 'right';

    /**
     * 列宽
     */
    width?: number;

    /**
     * 最小列宽
     */
    minWidth?: number;

    /**
     * 最大列宽
     */
    maxWidth?: number;

    /**
     * 列的样式名称
     */
    className?: string;

    /**
     * 对应列是否可以排序
     */
    sortable?: boolean;
};

/**
 * 列配置
 */
declare type TableColumn = BaseCol & {
    /**
     * 是否渲染
     */
    isShow: boolean;

    /**
     * 渲染器
     */
    render?: 'raw' | 'link' | 'link_http' | 'thumb' | 'email' | 'date' | 'short_date' | 'long_date' | 'sex' | 'render' | 'clk_event' | 'stat';

    /**
     * true = 不换行，一行
     */
    isOneLine?: boolean;

    /**
     * 自定义渲染函数（true） or 自定义键值对（false）
     */
    isCode?: boolean;

    /**
     * 自定义键值对
     */
    customRenderKV?: JsonParam;

    /**
     * 自定义渲染函数
     */
    customRender?: string;

    /**
     * 自定义点击事件
     */
    clkEvent?: string;

    /**
     * 可否搜索
     */
    canSearch?: boolean;
    
    /**
     * 可否下拉筛选
     */
    canDropdownFilter?: boolean;
}

/**
 * iView 的列配置
 */
declare type iViewTableColumn = BaseCol & {
    /**
     * 开启后，文本将不换行，超出部分显示为省略号
     */
    ellipsis?: boolean;

    /**
     * 开启后，文本将不换行，超出部分显示为省略号，并用 Tooltip 组件显示完整内容
     */
    tooltip?: boolean;

    /**
    * 渲染器
    */
    render?: Function;
}

/**
 * 列表其他配置
 */
declare type ListFactory_ListConfig = {
    /**
     * 是否分页
     */
    isPage?: boolean;

    /**
     * 分页 0 = 不分页；1 = start/limit； 2 = pageNo/pageSize
     */
    page: 0 | 1 | 2;

    /**
     * 数据绑定配置（读取列表）
     */
    dataBinding: DataBinding;

    /**
     * 删除的数据绑定
     */
    deleteDataBinding?: DataBinding;

    /**
     * 工具条按钮
     */
    toolbarButtons?: ButtonEvent[];

    /**
     * 列表内的操作按钮
     */
    actionButtons?: ButtonEvent[];

    /**
     * 各列配置
     */
    fields: TableColumn[];

    /**
     * 绑定的表单
     */
    bindingForm: {
        /**
         * id
         */
        id: Number;

        name: string;
    }
};