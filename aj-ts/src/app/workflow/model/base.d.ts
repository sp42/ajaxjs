//////////////////////////////////////////////////////
///------------------ 基础对象模型---------------------
//////////////////////////////////////////////////////


/**
 * 定义一个点的 x、y 坐标
 */
declare interface Point {
    x: number,
    y: number
}

/**
 * 箱子规格，有高、宽和 x、y 坐标四个元素
 */
declare interface VBox extends Point {
    width: number,
    height: number
}

/**
 * Raphael 对象
 */
declare interface Raphael {
    /**
     * 添加样式
     * 
     * @param css 
     */
    addClass(css: string): Raphael;

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

    /**
     * 自定义的，拖放用的 x 坐标
     */
    movingX: number;

    /**
     * 自定义的，拖放用的 y 坐标
     */
    movingY: number;

    /**
     * 返回坐标和大小
     */
    getBBox(): VBox;
}

