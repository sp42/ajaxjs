
/**
 * 流程变迁
 */
interface JsonTransition {
    /**
     * 起点
     */
    from: string;

    /**
     * 终点
     */
    to: string;

    /**
     * 曲折的点列表
     */
    dots: Point[];

    /**
     * 文本节点
     */
    text?: JsonText;

    /**
     * 其他属性
     */
    props: { [key: string]: JsonTransitionProps };
}

/**
 * 流程变迁其他属性
 */
interface JsonTransitionProps {

}