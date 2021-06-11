/**
 * 组件化编程的基本类型
 */
interface Component {
    /**
     * 每个组件对象赋予一个 id
     */
    id: number;

    /**
     * 组件引用名称
     */
    ref: string;

    /**
     * 原始 JSON 数据
     */
    rawData: any;

    /**
     * 组件类型
     */
    type: string;
    
    /**
     * 序列化这个组件到 JSON
     */
    toJson(): string;
}