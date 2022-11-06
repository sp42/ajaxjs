/**
 * 当在舞台渲染时候，不同控件的渲染方式
 * 函数 this 指针为 舞台 对象
 */
export default {
    Form(tag: VNode_RenderTag, vnodeCfg: VNode_Cfg, def: RenderedMeta): void {
        vnodeCfg.class = 'widget-form';
        // set default value
        if (!vnodeCfg.props.labelWidth)
            vnodeCfg.props.labelWidth = 120;

        this.assignHighlightEvent(vnodeCfg, def);
        this.makeDargDrop(tag, def, "FormItem");
    },

    FormItem(tag: VNode_RenderTag, vnodeCfg: VNode_Cfg, def: RenderedMeta): void {
        if (!vnodeCfg.props.label)
            vnodeCfg.props.label = '（未命名）';

        this.assignHighlightEvent(vnodeCfg, def);
        this.makeDargDrop(tag, def, AnyDrop);
    },

    Row(tag: VNode_RenderTag, vnodeCfg: VNode_Cfg, def: RenderedMeta): void {
        tag.tagName = 'draggable'; // Row DOM 下面不能有因为 DD 生成的 div，那样会破坏 Row/Col 的结构，故用 tag，减少一层 div
        this.makeDragDropProp(vnodeCfg, def.children, "Cols"); // Col 只能在 Row 下面，故独立一个 Cols 分组
        this.assignHighlightEvent(vnodeCfg, def);
        vnodeCfg.attrs['tag'] = 'Row';
        vnodeCfg.class = 'column-container';
    },

    Col(tag: VNode_RenderTag, vnodeCfg: VNode_Cfg, def: RenderedMeta): void {
        if (!vnodeCfg.props.span)
            vnodeCfg.props.span = 12;

        this.makeDargDrop(tag, def, AnyDrop);
        this.assignHighlightEvent(vnodeCfg, def);
    },

    Tabs(tag: VNode_RenderTag, vnodeCfg: VNode_Cfg, def: RenderedMeta): void {
        // this.makeDargDrop(tag, def, AnyDrop);
        this.assignHighlightEvent(vnodeCfg, def);

        if (!vnodeCfg.on)
            vnodeCfg.on = {};

        Object.assign(vnodeCfg.on, { // 点击 tab 撤销高亮
            'on-click': (name: string): void => {
                if (this.ActiveWdiget)
                    this.ActiveWdiget = null;
            }
        });
    },

    TabPane(tag: VNode_RenderTag, vnodeCfg: VNode_Cfg, def: RenderedMeta): void {
        this.makeDargDrop(tag, def, AnyDrop);
    },

    div(tag: VNode_RenderTag, vnodeCfg: VNode_Cfg, def: RenderedMeta): void {
        vnodeCfg.class = 'ui-d-div';
        this.makeDargDrop(tag, def, AnyDrop);
        this.assignHighlightEvent(vnodeCfg, def, false);
    }
};

/**
 * 能够置下任意组件的容器
 */
const AnyDrop: string = "AnyDrop";