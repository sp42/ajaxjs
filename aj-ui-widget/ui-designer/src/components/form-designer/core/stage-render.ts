import draggable from "vuedraggable";
import _ from 'lodash';
import { CreateElement, VNode } from "vue";
import SelectionMixins from "./selection-mixins";
import CommFn from '../common-function';
import WidgetTagMap from '../meta/widget-tag-map';
import WidgetRender from '../meta/widget-render';

/**
 * 核心可视化的渲染器
 */
export default {
    components: { draggable },
    mixins: [SelectionMixins],
    data() {
        let metaData = CommFn.makeUid(this.$parent.initMetaData);

        return {
            RenderedMeta: metaData as RenderedMeta[],
            metaDataBackup: JSON.stringify(metaData), // 复位刷新用
            ActiveWdiget: null as RenderedMeta, // 当前选中的组件
            dragging: false // 用户是否在拖动
        };
    },

    /**
     * 核心渲染函数
     * 
     * @param h 
     * @returns 
     */
    render(h: CreateElement): VNode {
        render = [];
        this.traverse(0, this.RenderedMeta, render);
        const vnodeCfg: VNode_Cfg = { attrs: {}, props: {} };
        this.makeDragDropProp(vnodeCfg, this.RenderedMeta, AnyDrop);

        return createComponent({ tagName: 'draggable', properties: vnodeCfg, children: render }, h);
    },

    mounted() {
        // @ts-ignore
        window.MAIN_STAGE = this;
    },

    methods: {
        /**
         * 遍历元数据，转换为 Vue 的 VNode 渲染 JSON。转换在 def2vnode() 内完成
         * 这是一个递归函数
         * 
         * @param level 
         * @param arr 
         * @param every 
         * @param parent 
         * @param renders 
         */
        traverse(level: number, arr: JsonTree[], renders: VNode_RenderTag[], every: (level: number, item: JsonTree, arr: JsonTree[]) => void): void {
            level++;
            let path: string = stack.join('/');

            for (let i = 0; i < arr.length; i++) {
                let item: JsonTree = arr[i];
                item.level = level;

                every && every(level, item, arr);

                let def: RenderedMeta = <RenderedMeta>item;
                item.path = path + "/" + def.type + '#' + def.uid;

                let tag: VNode_RenderTag = this.def2vnode(def);
                renders.push(tag);

                if (item.children && item.children.length) {
                    stack.push(def.type);

                    this.traverse(level, item.children, tag.isDragDropContainer ? tag.children[0].children : tag.children, every);
                    stack.pop();
                }
            }
        },

        /**
         * 转换为 VNode 配置，主要为布局类型的组件服务。其他一般组件使用 RenderWidget 渲染（基于标签）
         * 
         * @param def 
         * @returns 
         */
        def2vnode(def: RenderedMeta): VNode_RenderTag {
            let type: string = def.type, tagName: string = WidgetTagMap[type] || type;
            if (!def.props)
                def.props = {};

            const vnodeCfg: VNode_Cfg = { attrs: {}, props: def.props };
            let tag: VNode_RenderTag = { tagName: tagName, properties: vnodeCfg, isDragDropContainer: false, children: [] };

            switch (tagName) {
                case 'Input':
                    vnodeCfg.class = 'input';
                    this.assignHighlightEvent(vnodeCfg, def);

                    if (!def.props)
                        def.props = { placeholder: '' };
                    vnodeCfg.props = def.props;
                    // properties.props = { placeholder: def.placeholder };

                    if (type === 'input_textarea') {
                        vnodeCfg.props = { type: 'textarea', placeholder: '' };
                    }

                    if (type === 'input_password') {
                        vnodeCfg.props = { type: 'password', placeholder: '' };
                    }

                    break;
            }

            if (WidgetRender[tagName]) { // 执行每个控件的渲染
                let fn: Function = <Function>WidgetRender[tagName];
                fn.call(this, tag, vnodeCfg, def);
            }

            if (tagName === 'Button') {
                this.assignHighlightEvent(vnodeCfg, def);
            }

            if (def.text)
                tag.textNode = def.text;

            if (def.isHtmlTag)
                this.assignHighlightEvent(vnodeCfg, def, false);

            console.log('渲染：', tagName, tag);
            return tag;
        },

        /**
         * 使得下面的元素可以拖放。这个方法会在容器下面创建一个 div 元素。
         * 如果不想，要使用 VNode_RenderTag.tagName = 'draggable' 的方法。
         * 
         * @param tag 
         * @param def 
         * @param group 
         */
        makeDargDrop(tag: VNode_RenderTag, def: RenderedMeta, group: string): void {
            const vnodeCfg: VNode_Cfg = { attrs: {}, props: {} };
            this.makeDragDropProp(vnodeCfg, def.children, group);

            tag.isDragDropContainer = true;// 如果是必须为拖放创建一个 div
            tag.children = [{ tagName: 'draggable', properties: vnodeCfg, children: [] }];
        },

        /**
         * 配置可以拖放
         * 这是为 Vue.drag 组件所配置的
         * 
         * @param vnodeCfg  节点的配置
         * @param children  数组，绑定数据
         * @param group     分组
         * @returns 组件的配置，特别这是给 <draggable> 元素用的属性
         */
        makeDragDropProp(vnodeCfg: VNode_Cfg, children: RenderedMeta[], group: string): any {
            Object.assign(vnodeCfg.attrs, { group: group, chosenClass: "chosen", ghostClass: 'ghost', forceFallback: true, animation: 500 });
            vnodeCfg.props.list = children;
            vnodeCfg.on = {
                start: (): void => {
                    this.dragging = true;
                },

                /**
                 * 使用 Render/Vnode 的代价是无法使用 V-Model 双向绑定。
                 * 此时 Vue.Draggable 令 DOM 变化了但没同步修改 Data，要自己给出。
                 * 百度不了任何资料。不过自己写，其实也不难。
                 * 主要是 list 输入数据（Array），然后 change 事件里面同步数据
                 * 
                 * @param $event 
                 */
                change: ($event: any): void => {
                    let e: any;
                    console.log($event);

                    if ($event.moved) {
                        e = $event.moved;
                        let newIndex: number = e.newIndex, oldIndex: number = e.oldIndex;
                        let old: any = children[oldIndex], temp: any = children[newIndex];
                        children[newIndex] = old;
                        children[oldIndex] = temp;
                    } else if ($event.added) {
                        e = $event.added;
                        children.splice(e.newIndex, 0, e.element);
                    } else if ($event.removed) {
                        e = $event.removed;
                        children.splice(e.oldIndex, 1);
                    } else {
                        alert('todo');
                        debugger
                    }

                    this.$forceUpdate();
                },
                end: (): void => {
                    this.dragging = false;
                    this.ActiveWdiget = null;
                }
            };
        },

        /**
        * 分配 uid 和 高亮事件。高亮事件包括 hover 移上提示 和 click 点击选中
        * 
        * @param properties 
        * @param def 
        */
        assignHighlightEvent(properties: any, def: RenderedMeta, isNativeEvent: boolean = true): void {
            properties.attrs = { 'data-uid': def.uid };// 分配 uid

            let event = {
                click: (event: MouseEvent): void => {
                    event.stopPropagation();
                    this.activeSection(event, def);
                },
                mouseenter: (event: MouseEvent): void => {
                    event.stopPropagation();
                    this.showHoverBorder(event, def);
                },
                mouseleave: (event: MouseEvent): void => {
                    event.stopPropagation();
                    this.hideHoverBorder();
                }
            };

            if (isNativeEvent) {
                if (!properties.nativeOn)
                    properties.nativeOn = {};
                Object.assign(properties.nativeOn, event);
            } else {
                if (!properties.on)
                    properties.on = {};
                Object.assign(properties.on, event);
            }
        },

        /**
         * 复制组件
         */
        copyWidget(): void {
            let parentArr: RenderedMeta[], index: number;
            // find it first, then del, more safe
            CommFn.find(this.RenderedMeta, this.ActiveWdiget, (parentNode: JsonTree, arr: RenderedMeta[], _index: number) => { parentArr = arr; index = _index });

            let newly = JSON.parse(JSON.stringify(this.ActiveWdiget)); // deep clone

            CommFn.makeUid([newly]);
            parentArr.splice(index + 1, 0, newly);
            this.ActiveWdiget = null;
            // this.$forceUpdate()
        },

        /**
         * 删除控件
         */
        delWidget(): void {
            if (!this.ActiveWdiget) {
                this.$Message.info('未选择任何控件');
                return;
            }

            let parentArr: JsonTree[], index: number;
            // find it first, then del, more safe
            CommFn.find(this.RenderedMeta, this.ActiveWdiget, (parentNode: JsonTree, arr: JsonTree[], _index: number) => { parentArr = arr; index = _index });
            this.$delete(parentArr, index);

            this.ActiveWdiget = null;
        },
    },
    watch: {
        ActiveWdiget(meta: RenderedMeta): void {
            if (meta) {
                // for Nav info
                this.$parent.$refs.NavBar.render(meta);
                this.$parent.$refs.RightTab.type = meta.type;
                this.$parent.$refs.RightTab.meta = meta;
                this.$parent.$refs.RightTab.props = meta.props;
            } else {
                this.$parent.$refs.NavBar.activeCmpLinks = '';
                this.$parent.$refs.RightTab.type = '';
                this.hideActivedBorder();
            }
        }
    }
};

function getColSpan(arr: []): {} {
    if (arr && arr.length)
        return {
            props: { span: 12 }
        };
    else
        return {
            props: { span: 24 }
        };
}

/**
 * 能够置下任意组件的容器
 */
const AnyDrop: string = "AnyDrop";

/**
* Create component is a recursive function that takes a DOM structure
* and a rendering function (of vue.js) and returns a Vuejs component.
*/
const createComponent = (dNode: VNode_RenderTag, h: CreateElement) => {
    // Handle empty elements and return empty array in case the dNode passed in is empty
    if (_.isEmpty(dNode))
        return [];

    // if the el is array call createComponent for all nodes
    if (_.isArray(dNode))
        // @ts-ignore
        return dNode.map((child) => createComponent(child, h));

    let children = [];

    if (dNode.children && dNode.children.length > 0) {
        dNode.children.forEach((c) => {
            if (_.isString(c)) {
                children.push(c)
            } else {
                children.push(createComponent(c, h))
            }
        });
    }
    // Need to clone 
    const properties = _.cloneDeep(dNode.properties)
    return h(dNode.tagName, properties, children.length > 0 ? children : dNode.textNode)
}

let render: VNode_RenderTag[] = [];

let stack = [];