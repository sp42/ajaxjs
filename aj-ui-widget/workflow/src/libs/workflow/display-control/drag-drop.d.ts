/**
 * 可 Reszie、拖放的对象
 */
interface DargDrop {
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
    resizeController?: aj.svg.ResizeControl;

    /**
    * 拖放时的事件
    */
    onDragStart?: function;

    onDragMove?: function;

    onDragEnd?: function;
}