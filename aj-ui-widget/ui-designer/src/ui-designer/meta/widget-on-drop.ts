import CommFn from "../common-function";

/**
 * 当组件从配置拖到舞台上时候，增加的属性
 * 
 * 特定控件的某些扩展，复合控件
 */
export default {
    Form(def: RenderedMeta): void {
        def.children = [
            {
                type: "FormItem",
                name: "栅格cc列容器",
                uid: CommFn.getUid(),
                children: [],
            }
        ];
    },
    Row(def: RenderedMeta): void {
        def.children = [
            {
                type: "Col",
                name: "栅格列容器",
                uid: CommFn.getUid(),
                children: [],
            },
            {
                type: "Col",
                name: "栅格列容器",
                uid: CommFn.getUid(),
                children: [],
            },
        ];
    },
    Tabs(def: RenderedMeta): void {
        def.children = [
            {
                type: "TabPane",
                name: "TabPane",
                uid: CommFn.getUid(),
                props: {
                    label: "Tab 1",
                },
                children: [],
            },
            {
                type: "TabPane",
                name: "TabPane",
                uid: CommFn.getUid(),
                props: {
                    label: "Tab 2",
                },
                children: [],
            },
        ];
    },
    div(def: RenderedMeta): void {
        def.children = [];
    },
    fieldset(def: RenderedMeta): void {
        def.children = [{
            type: 'legend',
            name: 'd',
            uid: CommFn.getUid(),
            text: '未命名标题',
        }];
    }
};