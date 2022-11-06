import { TextSvgComp } from "../svg/utils";
import Vue from "vue";
import { ResizeControl } from "../svg/resize";
import CmpMgr from '../cmp-mgr';

/**
 * 基础 Base State
 */
export abstract class BaseState implements IDisplayControl, DargDrop {
    constructor(PAPER: any, ref: string, data: JsonState, svg: Raphael) {
        this.id = CmpMgr.nextId();
        this.PAPER = PAPER;
        this.type = data.type;
        this.ref = ref;
        this.rawData = data;
        this.vBox = data.attr;
        this.svg = svg;
        this.svg.comp = this;
        this.vue = this.initVue();

        this.isDrag && this.svg.drag(onDragMove, onDragStart, onDragEnd);	// 使对象可拖动
        CmpMgr.register(this);
    }

    id: number;

    ref: string;

    updateHandlers: updateVBoxHandler[] = [];

    rawData: JsonState;

    svg: Raphael;

    vue?: Vue | undefined;

    PAPER: any;

    vBox: VBox;

    type!: string;

    text?: TextSvgComp;

    init(): void {
        throw new Error("Method not implemented.");
    }

    toJson(): string {
        throw new Error("Method not implemented.");
    }

    isDrag: boolean = true;

    resize: boolean = true;

    resizeController?: ResizeControl;

    onDragStart: any;

    onDragMove: any;

    onDragEnd: any;

    show(): void {
        this.svg.show();
    }

    hide(): void {
        this.svg.hide();
    }

    pos(p?: Point): Point {
        if (p)
            this.moveTo(p.x, p.y);

        return { x: this.vBox.x, y: this.vBox.y };
    }

    moveTo(x: number, y: number): void {
        this.svg.attr({ x: x, y: y });
        this.vBox.x = x;
        this.vBox.y = y;
    }

    remove(): void {
        this.svg.remove();
        this.updateHandlers = [];
        CmpMgr.unregister(this.id);
    }

    /**
     * 初始化 Vue 数据驱动
     * 
     * @returns 
     */
    private initVue(): Vue {
        let self: BaseState = this,
            vue: Vue = new Vue({
                data: { vBox: this.vBox, text: '' },	// 显示的文字，居中显示
                watch: {
                    vBox: {
                        handler(val: VBox): void {
                            self.updateVBox(val);
                        },
                        deep: true
                    }
                }
            });

        return vue;
    }

    /**
     * 更新 vbox 的事件
     * 
     * @param val 
     */
    public updateVBox(val: VBox): void {
        this.updateHandlers.forEach((fn: updateVBoxHandler) => fn.call(this, val));

        if (this.resize && this.resizeController) {
            this.resizeController.setDotsPosition();
            this.resizeController.updateBorder();
        }

        if (this.text) // 文字伴随着图形拖放
            this.text.setXY_vBox(this.vBox);
    }

    /**
     * 加入 updateVBoxHandler
     * 
     * @param fn 
     */
    addUpdateHandler(fn: updateVBoxHandler): void {
        this.updateHandlers.push(fn);
    }

    /**
     * 移除 updateVBoxHandler
     * 
     * @param fn 
     */
    removeUpdateHandler(fn: updateVBoxHandler): void {
        let index: number | null = null;

        for (let i = 0, j = this.updateHandlers.length; i < j; i++) {
            if (this.updateHandlers[i] == fn)
                index = i;
        }

        if (index != null)
            this.updateHandlers.splice(index, 1);
    }
}

/**
 * 开始拖动
 */
function onDragStart(this: Raphael): void {
    let x = Number(this.attr('x')), y = Number(this.attr('y'));
    this.movingX = x, this.movingY = y;

    this.attr({ opacity: .3 }); // 拖动时半透明效果
    this.comp.onDragStart && this.comp.onDragStart(this.comp, x, y);
}

/**
 * 拖动中
 * 
 * @param x 
 * @param y 
 */
function onDragMove(this: Raphael, x: number, y: number): void {
    let _x: number = this.movingX + x,
        _y: number = this.movingY + y;

    this.attr({ x: _x, y: _y });
    let s: BaseState = <BaseState>this.comp;
    s.vBox.x = _x;
    s.vBox.y = _y;

    this.comp.onDragMove && this.comp.onDragMove(this.comp, _x, _y);
}

/**
 * 拖动完毕
 */
function onDragEnd(this: Raphael): void {
    this.attr({ opacity: 1 });
    // why more one arg 'this'?
    this.comp.onDragEnd && this.comp.onDragEnd(this.comp, this, this.attr('x'), this.attr('y'));
}
