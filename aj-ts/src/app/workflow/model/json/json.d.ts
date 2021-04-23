/**
 * 一段文本，包含其坐标。
 * 一般流程节点没有坐标，变迁才有。
 */
interface JsonText extends Point {
    /**
     * 文本内容
     */
    text: string;
}

/**
 * 流程实体信息
 */
interface WorkflowInfo {
    name: string;
    displayName: string;
    expireTime: string;
    instanceUrl: string;
    instanceNoClass: string;
}

/**
 * 流程定义转换为 JSON
 */
interface RawWorkflowData {
    /**
     * 所有的状态，通常是 key= box 的 name，value 是 box 的 vue 实例
     */
    states: { [key: string]: JsonState };

    /**
     * 流程变迁
     */
    paths: { [key: string]: JsonTransition };

    /**
     * 该流程自身属性
     */
    props: WorkflowInfo;
}