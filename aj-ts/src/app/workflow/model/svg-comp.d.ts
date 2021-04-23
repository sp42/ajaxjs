/**
 * 图形组件
 */
declare interface SvgComp {
    /**
     * 每个图形对象赋予一个 id
     */
    id: number;

    /**
     * 引用名称
     */
    ref: string;

    /**
     * 原始 JSON 数据
     */
    rawData: JsonState;

    /**
     * 组件类型
     */
    type: string;

    /**
     * 高宽大小
     */
    vBox: VBox;

    /**
     * Vue 实例，可选
     */
    vue?: Vue;

    /**
     * 来自 Raphael 的对象
     */
    svg: Raphael;

    /**
     * 初始化
     */
    init(): void;

    /**
     * 显示图型
     */
    show(): void;

    /**
     * 隐藏图型
     */
    hide(): void;

    /**
     * 移动到某个点
     * 
     * @param x 
     * @param y 
     */
    pos(x: number, y: number): void;

    /**
     * 在 DOM 中删除，并注销图形
     */
    remove(): void;

    /**
     * 可否拖动
     */
    isDrag: boolean;

    /**
     * 是否可以放大缩小
     */
    resize: boolean;

    /**
     * 放大缩小控制器
     */
    resizeController: aj.svg.ResizeControl;

    /**
     * 序列化这个组件到 JSON
     */
    toJson(): string;

    /**
     * 组件所在的桌布
     */
    PAPER: any;

    /**
     * 显示的文字
     */
    text: string;

    /**
     * 文字 svg 节点
     */
    textNode: TextSvgComp;

    /**
     * 当组件 VBox 修改时候要执行的事件
     */
    updateHandlers: updateVBoxHandler[];

    /**
     * 拖放时的事件
     */
    onDragStart: function;
    onDragMove: function;
    onDragEnd: function;
}

/**
 * 
 */
type updateVBoxHandler = (newVbox: VBox) => void;

/**
 * Vue 组件包装的 
 */
declare interface SvgVue extends SvgComp, Vue {
}