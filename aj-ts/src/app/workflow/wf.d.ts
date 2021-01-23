/**
 * Raphael 对象
 */
declare interface Raphael {
    /**
     * 获取属性
     * 
     * @param name 
     */
    attr(name: string): string;

    /**
     * 设置属性
     * 
     * @param map 
     */
    attr(map: { [key: string]: any }): Raphael;

    /**
     * Vue 组件引用，保留一个，方向引用
     */
    vue: SvgVue;

    /**
     * 显示图型
     */
    show(): void;

    /**
     * 隐藏图型
     */
    hide(): void;

    /**
     * 在 DOM 中删除，并注销图形
     */
    remove(): void;

    drag(...fu: function): void;
}

/**
 * 定义一个点的 x、y 坐标
 */
declare interface Point {
    x: number,
    y: number
}

/**
* 箱子规格，有高宽和 xy 坐标四个元素
*/
declare interface VBox extends Point {
    width: number,
    height: number
}

/**
 * 图形组件
 */
declare interface SvgComp {
    /**
     * 高宽大小
     */
    vBox: VBox;

    /**
     * 来自 Raphael 的对象
     */
    svg: Raphael;

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
}

/**
 * Vue 组件包装的 
 */
declare interface SvgVue extends SvgComp, Vue {

}