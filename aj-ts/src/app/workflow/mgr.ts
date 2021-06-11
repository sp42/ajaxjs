/**
 * 组件管理器
 */
namespace aj.wf.ComMgr {
    /**
     * id 记数器
     */
    let uid: number = 0;

    /**
     * 生成下一个 id
     * 
     * @returns 下一个 id
     */
    export function nextId(): number {
        return ++uid;
    }

    /**
     * 登记组件
     * 
     * @param vueObj 
     */
    export function register(comp: Component): void {
        let id: string = "ajComp-" + comp.id;
        // comp.svg.node.id = id;

        let w: Warpper = <Warpper><unknown>comp;
        w.svg.node.id = id;

        DATA.ALL_COMPS[id] = comp;
    }

    /**
     * 注销组件
     * 
     * @param id 
     */
    export function unregister(id: number): void {
        delete DATA.ALL_COMPS[id];
    }
}

namespace aj.wf {
    // 定义常量
    export enum SELECT_MODE {
        /**
         * 选中的模式，点选类型
         */
        POINT_MODE = 1,

        /**
         * 选中的模式，添加路径类型
         */
        PATH_MODE = 2
    }

    /**
     * 只读模式不可编辑
     */
    export let isREAD_ONLY: boolean = false;

    /**
     * 全局数据
     */
    interface _DATA {
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

    export let DATA: _DATA = {
        STATES: {},
        PATHS: {},
        ALL_COMPS: {},
        JSON_DATA: {}
    };

    interface _MAIN_DATA {
        selectedComponent: Component | null;
        currentNode: any;
        currentMode: SELECT_MODE;
    }

    let MAIN_DATA: _MAIN_DATA = {
        selectedComponent: null,			 	// 选中的 SVG 图形对象
        currentNode: null,						// 当前选中的节点
        currentMode: SELECT_MODE.POINT_MODE,	// 当前选中的选择模式
    };

    /**
     * Main 程序
     */
    class Main {
        /**
         * 设置选中的组件
         * 
         * @param cop 
         */
        setSelectedComponent(cop: Component | null): void {
            MAIN_DATA.selectedComponent = cop;
        }

        /**
         * 获取选中的组件
         * 
         * @returns 
         */
        getSelectedComponent(): Component | null {
            return MAIN_DATA.selectedComponent;
        }

        /**
         * 清除桌布
         */
        clearStage(): void {
            this.setSelectedComponent(null);
            svg.PAPER.clear();

            for (var i in DATA)
                // @ts-ignore
                DATA[i] = {};
        }

        /**
         * 无 UI Vue 实例
         */
        vue: Vue = new Vue({
            data: MAIN_DATA,
            watch: {
                selectedComponent(newCop: Component, old: Component): void {
                    main.updateSelectedComponent(newCop, old);
                }
            }
        });

        updateSelectedComponent(newCop: Component, old: Component): void {
            if (newCop) {
                if (newCop.hasOwnProperty('resizeController')) {
                    let state: State = <State>newCop;
                    state.resizeController?.showBox();
                }

                if (newCop instanceof svg.Path)
                    newCop.show();

                setTypeName(newCop);

                if (newCop.rawData)
                    console.log(newCop.rawData)
            }

            if (old) {
                if (old.hasOwnProperty('resizeController')) {
                    let state: State = <State>old;
                    state.resizeController?.hideBox();
                }

                if (old instanceof svg.Path)
                    old.hide();
            }
        }
    }

    /**
     * 
     * @param cop 
     */
    function setTypeName(cop: Component): void {
        // ui.PropertyForm.cop = cop;
        // ui.PropertyForm.selected = name[cop.type];
    }

    // 单例
    export let main = new Main();

    let name: StringJsonParam = { start: '开始节点', end: '结束节点', task: '任务节点', decision: '抉择节点', transition: '变迁路径' };
}