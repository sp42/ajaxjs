/**
 * 表示一个节点
 */
interface JsonState {
    /**
     * 节点类型
     */
    type: string;

    /**
     * 文本节点
     */
    text?: JsonText;

    /**
     * 坐标、尺寸 TODO 改为 vBox
     */
    attr: vBox;

    /**
     * 其他属性
     */
    props: { [key: string]: JsonStateProps };
}

/**
 * 节点属性
 */
interface JsonStateProps {
    /**
     * 显示名称
     */
    displayName: JsonParam;

    /**
     * 名称？？
     */
    name: JsonParam;

    /**
     * 坐标、尺寸 TODO 是否重复？
     */
    layout: JsonParam;

    /**
     * 
     */
    form?: JsonParam;

    /**
     * 表达式
     */
    expr?: JsonParam;

    /**
     * 
     */
    assignee?: JsonParam;

    /**
     * 
     */
    performType?: JsonParam;

    /**
     * 
     */
    taskType?: JsonParam;
}