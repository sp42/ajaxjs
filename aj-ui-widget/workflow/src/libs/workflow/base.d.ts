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
     * 组件引用，保留一个，方向引用
     */
    comp: aj.wf.BaseState;

    node: any;

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

/**
 * 桌布
 */
declare interface Raphael_Paper {
    path(): Raphael;
    rect(x: number, y: number): Raphael;
}

/**
 * 全局工作流数据
 */
declare interface GLOBAL_WF_DATA {
    /**
     * 所有的状态，通常是 key= box 的 ref，value 是 box 的 vue 实例
     */
    STATES: { [key: string]: BaseState };

    /**
     * 所有的路径
     */
    PATHS: { [key: string]: svg.Path };

    /**
     * 所有的组件，key=id
     */
    ALL_COMPS: { [key: string]: Component };

    /**
    * 流程定义 JSON 数据
    */
    JSON_DATA: {};
}