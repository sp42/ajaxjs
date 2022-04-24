import IdeLayout from "./ide-layout/ide-layout.vue";
import Vue from "vue";
import Topbar from "./topbar.vue";
import Toolbox from "./toolbox.vue";
import { TEST_DATA } from './test-data';
import { BaseState } from "./state/base";
import { ImageState } from './state/image';
import { State } from './state/state';
import { Path } from './svg/path';
import CmpMgr from './cmp-mgr';
import Raphael from 'raphael';
import PropertyTask from './property-forms/task.vue';
import PropertyStart from './property-forms/start.vue';
import PropertyEnd from './property-forms/end.vue';
import PropertyCustom from './property-forms/custom.vue';
import PropertyDecision from './property-forms/decision.vue';
import PropertySubprocess from './property-forms/subprocess.vue';

import { codemirror } from 'vue-codemirror';
import 'codemirror/lib/codemirror.css';
import 'codemirror/addon/hint/show-hint.css';
import 'codemirror/mode/javascript/javascript';
import 'codemirror/addon/selection/active-line.js';
import 'codemirror/addon/edit/closebrackets.js';
import 'codemirror/theme/base16-light.css';
import 'codemirror/addon/display/autorefresh'

// 使的 SVG 图形可以添加样式类
Raphael.el.addClass = function (className: string): object {
    this.node.setAttribute("class", className);
    return this;
}

export default Vue.extend({
    components: { IdeLayout, Toolbox, Topbar, PropertyTask, PropertyStart, PropertyEnd, PropertyCustom, PropertyDecision, PropertySubprocess, codemirror },
    data() {
        return {
            codeMode: false,
            isREAD_ONLY: false,                     // 只读模式不可编辑
            selectedComponent: null,			 	// 选中的 SVG 图形对象
            currentNode: null,						// 当前选中的节点
            currentMode: SELECT_MODE.POINT_MODE,	// 当前选中的选择模式
            DATA: {
                STATES: {},
                PATHS: {},
                ALL_COMPS: CmpMgr.ALL_COMPS,
                JSON_DATA: {}
            } as GLOBAL_WF_DATA,
            PAPER: null as Raphael_Paper,            // 
            jsonCode: `var menuPanel = el.parentNode;
                    menuPanel.style.display = 'none';
                    setTimeout(function(){
                    menuPanel.style.display = '';
                    }, 500);`,
            cmOption: {
                autoRefresh: true,                    // 重点是这句，为true
                styleActiveLine: true,                // 高亮选中行
                lineNumbers: true,                    // 显示行号
                line: true,
                lint: true,
                mode: 'application/json',
                hintOptions: { completeSingle: false },// 当匹配只有一项的时候是否自动补全
                theme: 'monokai', // 主题 
            }
        };
    },
    mounted() {
        this.init();
        this.initEvent();
    },
    methods: {
        init(): void {
            let el: HTMLElement = <HTMLElement>this.$el.querySelector(".canvas");
            //@ts-ignore
            this.PAPER = window.PAPER = Raphael(el, el.clientWidth, el.clientHeight);

            // JSON 渲染为图形
            let states: { [key: string]: BaseState } = this.DATA.STATES;

            let data: RawWorkflowData = TEST_DATA;

            for (let i in data.states) {
                let stateData: JsonState = data.states[i],
                    state: BaseState;

                switch (stateData.type) { // 生成 box
                    case 'start':
                    case 'decision':
                    case 'end':
                        state = new ImageState(this.PAPER, i, stateData);
                        break;
                    default:
                        state = new State(this.PAPER, i, stateData);
                }

                if (this.isREAD_ONLY)
                    state.resize = state.isDrag = false;

                states[i] = state;
            }

            // 连线
            let paths: { [key: string]: Path } = this.DATA.PATHS;

            for (let i in data.paths) {
                let pathData: JsonTransition = data.paths[i],
                    from: BaseState = states[pathData.from],
                    to: BaseState = states[pathData.to],
                    path: Path = new Path(this.PAPER, i, from.svg, to.svg, pathData);

                paths[i] = path;
                pathData.dots && pathData.dots.length && path.restore(pathData.dots);
            }

            console.log('欢迎来到 工作流流程设计器');
        },
        initEvent(): void {
            document.addEventListener('click', (ev: Event) => {
                let el: HTMLElement = <HTMLElement>ev.target,
                    // @ts-ignore
                    isSVGAElement: boolean = !!el.ownerSVGElement; // 点击页面任何一个元素，若为 SVG 且是组件，使其选中的状态

                if (isSVGAElement && el.id.indexOf('ajComp') != -1) {
                    let component: Component = this.DATA.ALL_COMPS[el.id];

                    if (!component)
                        throw '未登记组件 ' + el.id;

                    this.setSelectedComponent(component);
                }
            });

            /**
             * 删除： 删除状态时，触发removerect事件，连接在这个状态上当路径监听到这个事件，触发removepath删除自身；
             * 删除路径时，触发removepath事件
             */
            document.addEventListener('keydown', (ev: KeyboardEvent) => {
                if (this.isREAD_ONLY)
                    return;

                // 键盘删除节点
                if (ev.key == 'Delete' && this.selectedComponent) {
                    this.selectedComponent.remove();
                    this.selectedComponent = null;
                }
            });
        },
        /**
         * 设置选中的组件
         * 
         * @param cop 
         */
        setSelectedComponent(cop: Component | null): void {
            this.selectedComponent = cop;
        },

        /**
         * 清除桌布
         */
        clearStage(): void {
            this.setSelectedComponent(null);
            this.PAPER.clear();

            for (let i in this.DATA)
                this.DATA[i] = {};
        },

        /**
         * 是否显示属性面板
         * 
         * @returns 
         */
        isShowProperty(): boolean {
            return this.selectedComponent != null;
        }
    },
    watch: {
        selectedComponent(newCop: Component, old: Component): void {
            if (newCop) {
                if (newCop.hasOwnProperty('resizeController')) {
                    let state: State = <State>newCop;
                    state.resizeController?.showBox();
                }

                if (newCop instanceof Path)
                    newCop.show();

                // setTypeName(newCop);

                // if (newCop.rawData)
                //     console.log(newCop.rawData)
            }

            if (old) {
                if (old.hasOwnProperty('resizeController')) {
                    let state: State = <State>old;
                    state.resizeController?.hideBox();
                }

                if (old instanceof Path)
                    old.hide();
            }
        }
    },
    computed: {
        /**
         * 
         * @returns 
         */
        currentForm(): string {
            if (!this.selectedComponent)
                return '';

            console.log(this.selectedComponent)
            let str: String = new String(this.selectedComponent.type);
            let arr: string[] = str.split('');
            arr[0] = str[0].toUpperCase();
            str = arr.join('');

            return 'Property' + str;
            // return `aj-wf-${this.selectedComponent.type}-form`;
        }
    }
});

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
};

