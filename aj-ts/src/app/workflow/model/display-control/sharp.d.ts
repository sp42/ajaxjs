/**
 * 简易图形对象
 */
interface SimpleSharp {
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
    moveTo(x: number, y: number): void;

    /*
    * 定位这个点，或者获取该点的坐标。如果有新的定位送入，则采用新的定位。
    * 这个方法仅仅是改变点的坐标，要同步修改相关联的点，请使用 moveTo() 方法。
    * 
    * @param p 新的定位坐标，这是可选的
    * @returns 这个点的坐标
    */
    pos(p?: Point): Point;

    /**
     * 在 DOM 中删除，并注销图形
     */
    remove(): void;
}

/**
 * 常见的图像操作
 */
interface SharpAction extends SimpleSharp {
    /**
     * 组件所在的桌布
     */
    PAPER: any;

    /**
     * 高宽大小
     */
    vBox: VBox;

    /**
     * 当组件 VBox 修改时候要执行的事件列表
     */
    updateHandlers: updateVBoxHandler[];
}

/**
 * 修改 VBox 时候触发的事件
 */
type updateVBoxHandler = (newVbox: VBox) => void;