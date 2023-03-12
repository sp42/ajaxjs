

/**
 * 预览用的
 */
 type FormWidgetBase = {
    /**
     * 组件类型
     */
    type: string;

    /**
     * 标签
     */
    label?: string;

    /**
     * 数据绑定的索引
     */
    key?: string;

    /**
     * 是否禁用
     */
    disable?: boolean;
};


type FormWidget_Column = FormWidgetBase & {
    /**
     * 列数组
     */
    children: any[];
};

/**
 * 
 */
type FormWidget = FormWidgetBase | FormWidget_Column;
// interface FormWidget {
//     /**
//      * 提示文本
//      */
//     placeholder?: string;

// }

/**
 * 表单本身的配置
 */
declare interface FormConfig {
    /**
     * 表单域标签的位置，可选值为 left、right、top
     */
    labelPosition: 'left' | 'right' | 'top';

    /**
     * 表单域标签的宽度，所有的 FormItem 都会继承 Form 组件的 label-width 的值
     */
    labelWidth: number;

    /**
     * 是否显示校验错误信息
     */
    showMessage: boolean;

    /**
     * 是否自动在 label 名称后添加冒号
     */
    labelColon: boolean;
}

/**
 * 表单数据绑定的配置
 */
interface FormDataBinding {
    /**
     * 是否创建合并修改
     */
    isCreateOrUpdate: boolean;
}

type Widget = {
    /**
     * 返回选中组件的名称
     * 
     * @returns 
     */
    getTypeName(): string;
}

/**
 * 基础元数据
 */
type MetaBase = {
    /**
     * 组件类型
     */
    type: string;

    /**
     * 名称
     */
    name: string;

    /**
     * 是否原生的 HTML 元素
     */
    isHtmlTag?: boolean;

    /**
     * 文本节点
     */
    text?: string;
};

/**
 * 组件定义
 */
type WidgetDefinition = MetaBase & {
    /**
     * 英文名称
     */
    eng_name: string;

    /**
     * 图标
     */
    icon: string;

    /**
     * 用于 UI 隐藏而已
     */
    isHide?: boolean;
};

/**
 * 在候选菜单中的组件（左侧的）
 */
type WidgetInMenu = WidgetDefinition & {
    /**
     * 用于 UI 隐藏而已
     */
    isHide?: boolean;
}

/**
 * 组件元数据，组合了各属性
 */
type RenderedMeta = MetaBase & {
    /**
     * 唯一 id
     */
    uid: number;

    /**
     * 数据绑定的索引
     */
    key?: string;

    /**
     * 组件属性
     */
    props?: any;

    /**
     * 子节点
     */
    children?: RenderedMeta[];
}

/**
 * 树
 */
type JsonTree = {
    /**
     * 该节点所在的层数（深度 deep）
     */
    level?: number;

    /**
     * 父级节点数组
     */
    parent?: JsonTree[];

    /**
     * 路径
     */
    path?: string;

    /**
     * 子节点
     */
    children?: JsonTree[];
}

/**
 * 节点的配置
 */
type VNode_Cfg = {
    /**
     * 样式
     */
    class?: string;

    /**
     * DOM 属性
     */
    attrs?: any;

    /**
     * 组件属性
     */
    props?: any;

    /**
     * 组件事件
     */
    on?: any;

    /**
     * 原生事件
     */
    nativeOn?: any;
};

/**
 * VNode 
 */
type VNode_RenderTag = {
    /**
     * 标签名称
     */
    tagName: string;

    /**
     * 节点的配置
     */
    properties: VNode_Cfg;

    /**
     * 文本节点
     */
    textNode?: string;

    /**
     * 子节点
     */
    children: VNode_RenderTag[];

    /**
     * 是否包含拖动的容器。如果是必须为拖放创建一个 div
     */
    isDragDropContainer?: boolean;
};
