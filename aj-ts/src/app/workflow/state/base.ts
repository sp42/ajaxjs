namespace aj.wf {
    export abstract class BaseState implements IDisplayControl {
        constructor(PAPER: any, type: string, ref: string, rawData: JsonState, vBox: VBox, svg: Raphael) {
            this.id = ComMgr.nextId();
            this.PAPER = PAPER;
            this.type = type;
            this.ref = ref;
            this.rawData = rawData;
            this.vBox = vBox;
            this.svg = svg;

            this.vue = new Vue({
                mixins: [aj.svg.BaseRect],
                data: { vBox: vBox }
            });

            this.isDrag && this.svg.drag(onDragMove, onDragStart, onDragEnd);	// 使对象可拖动
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

        init(): void {
            throw new Error("Method not implemented.");
        }

        toJson(): string {
            throw new Error("Method not implemented.");
        }

        isDrag: boolean = true;

        resize: boolean = true;

        // resizeController: svg.ResizeControl;

        onDragStart: any;

        onDragMove: any;

        onDragEnd: any;

        show(): void {
            throw new Error("Method not implemented.");
        }
        hide(): void {
            throw new Error("Method not implemented.");
        }

        pos(p?: Point): Point {
            throw new Error("Method not implemented.");
        }

        moveTo(x: number, y: number): void {
            throw new Error("Method not implemented.");
        }

        remove(): void {
            throw new Error("Method not implemented.");
        }
    }

    /**
     * 开始拖动
     */
     function onDragStart(this: Raphael): void {
        let x = Number(this.attr('x')), y = Number(this.attr('y'));
        this.movingX = x, this.movingY = y;

        this.attr({ opacity: .3 }); // 拖动时半透明效果
        this.vue.onDragStart && this.vue.onDragStart(this.vue, x, y);
    }

    /**
     * 拖动中
     * 
     * @param x 
     * @param y 
     */
    function onDragMove(this: Raphael, x: number, y: number): void {
        let _x: number = this.movingX + x, _y: number = this.movingY + y;

        this.attr({ x: _x, y: _y });
        this.vue.vBox.x = _x;
        this.vue.vBox.y = _y;

        this.vue.onDragMove && this.vue.onDragMove(this.vue, _x, _y);
    }

    /**
     * 拖动完毕
     */
    function onDragEnd(this: Raphael): void {
        this.attr({ opacity: 1 });
        // why more one arg 'this'?
        this.vue.onDragEnd && this.vue.onDragEnd(this.vue, this, this.attr('x'), this.attr('y'));
    }
}