namespace aj.svg {
    /**
     * 连线路径
     */
    export class Path implements SimpleSharp {
        /**
         * 默认 path 就是 Transition 类型
         */
        public type: string = 'transition';

        /**
         * 起点
         */
        public fromDot: Dot;

        /**
         * 终点
         */
        public toDot: Dot;

        /**
         * 起点
         */
        public from: Raphael;

        /**
         * 终点
         */
        public to: Raphael;

        /**
         * 来自 Raphael 的对象
         */
        public svg: Raphael;

        /**
         * 原始 JSON 数据
         */
        public rawData: JsonTransition;

        /**
         * 
         */
        private moveFn: Function;

        /**
         * 箭头图形
         */
        private arrow: Raphael;

        constructor(from: Raphael, to: Raphael, pathCfg: JsonTransition) {
            this.from = from;
            this.to = to;
            this.rawData = pathCfg;

            let fromBB: VBox = from.getBBox(),
                toBB: VBox = to.getBBox(),
                // 起点是 box 中央，这个是起点坐标
                fromPos: Point = Utils.connPoint(fromBB, { x: toBB.x + toBB.width / 2, y: toBB.y + toBB.height / 2 }),
                // 终点坐标
                toPos: Point = Utils.connPoint(toBB, fromPos),
                /* 先创建中间点，一开始左右点都是 null 的，——这没关系，先空着，下面会计算 */
                // 中间点
                midDot: Dot = new Dot(DOT_TYPE.SMALL, { x: (fromPos.x + toPos.x) / 2, y: (fromPos.y + toPos.y) / 2 }, this);

            /* 起点没有 left 点，right 点就是 midDot 了 */
            this.fromDot = new Dot(DOT_TYPE.FROM, fromPos, this, undefined, midDot);// 起点对象
            /* 终点没有 right 点，left 点就是 midDot 了 */
            this.toDot = new Dot(DOT_TYPE.TO, toPos, this, midDot);// 终点对象

            /* 补充中间点的左右两点信息 */
            midDot.left(this.fromDot);
            midDot.right(this.toDot);

            this.svg = PAPER.path().addClass('path');
            this.arrow = PAPER.path().addClass('arrow');

            wf.Mgr.register(this);
            this.hide();

            this.moveFn = rectResizeHandler.bind(this);
            from.vue.addUpdateHandler(this.moveFn);
            to.vue.addUpdateHandler(this.moveFn);

            this.refreshPath();
        }

        /**
         * 刷新路径
         */
        refreshPath(): void {
            let d: Dot = this.fromDot;

            if (!d)
                return;

            let path: string[] = [`M${d.pos().x} ${d.pos().y}`];

            // 渲染线的路径
            while (d.right()) {
                d = d.getRightDot();
                path.push(`L${d.pos().x} ${d.pos().y}`);
            }

            this.svg.attr({ path: path.join('') });
            this.rendererArrow(d);

            // if (this.textObj) {
            // let mid = this.midDot().pos();
            //     let textPos = this.textObj.getXY();// 定位文字
            //     this.textObj.setXY(mid.x + 30, mid.y + 10);
            // }
        }

        /**
         * 渲染箭头
         * 根据箭头三点的坐标生成 SVG 路径
         * 
         * @param d 最后一个点
         */
        private rendererArrow(d: Dot): void {
            let arrPos: Point[] = Utils.arrow(d.getLeftDot().pos(), d.pos(), 4),
                path: string = `M${arrPos[0].x} ${arrPos[0].y}L${arrPos[1].x} ${arrPos[1].y}L${arrPos[2].x} ${arrPos[2].y}z`;

            this.arrow.attr({ path: path });
        }

        /**
         * 
         * @param o 
         */
        attr(o: any): void {
            o && o.path && this.svg.attr(o.path);
            o && o.arrow && this.arrow.attr(o.arrow);
        }

        /**
         * 遍历链表，查找中间的点
         * 
         * @returns  中间的点
         */
        midDot(): Dot | undefined {
            let mid: Dot | undefined = this.fromDot.right(),
                end: Dot | undefined = mid?.right();

            while (end && end.right() && end.right()?.right()) {
                end = end.right()?.right();
                mid = mid?.right();
            }

            return mid;
        }

        toJson(): string {
            return wf.serialize.dotList.toJson(this.fromDot);
        }

        /**
         * 输入坐标数据，还原图形
         * 
         * @param points 坐标数组
         */
        restore(points: Point[]): void {   /* 如果遇到未创建的点 会自动创建（SmallDot 的 case 分支） */
            let d: Dot | undefined = this.fromDot.right(),
                point: Point;

            for (let i = 0, j = points.length; i < j; i++) {
                point = points[i];

                if (d) {
                    d.moveTo(point.x, point.y);
                    d = d.right();
                }
            }
        }

        remove(): void {
            let d: Dot = this.fromDot;

            while (d) {
                if (d.right()) {
                    d = d.getRightDot();
                    d.getLeftDot().remove();
                } else {
                    d.remove();
                    // @ts-ignore
                    d = null;
                }
            }

            // this.svg.remove();
            // this.arrow.remove();

            // if (this.textObj) {
            //     this.textObj.$el.parentNode.removeChild(this.textObj.$el);
            //     this.textObj = null;
            // }

            // this.from.vue.removeUpdateHandler(this.moveFn); // 卸载事件
            // this.to.vue.removeUpdateHandler(this.moveFn);
        }

        show(): void {
            display.call(this, this.fromDot, true);
        }

        hide(): void {
            display.call(this, this.fromDot, false);
        }

        moveTo(x: number, y: number): void {
            throw new Error("Method not implemented.");
        }

        pos(p?: Point): Point {
            throw new Error("Method not implemented.");
        }
    }

    /**
     * 显示或隐藏
     * 
     * @param {Dot}     开始的点 fromDot
     * @param {boolean} true=显示
     */
    function display(dotObj: Dot | undefined, isShow: boolean): void {
        while (dotObj) {
            isShow ? dotObj.show() : dotObj.hide();
            dotObj = dotObj.right();
        }
    }

    /**
     * 矩形移动时处理器
     * 
     * @param this 
     */
    function rectResizeHandler(this: Path): void {
        let o, dot;

        //            if (from && from.node.id == event.target.id) {
        dot = this.fromDot.getRightDot().getRightDot();
        if (dot.type == DOT_TYPE.TO)
            o = { x: this.to.getBBox().x + this.to.getBBox().width / 2, y: this.to.getBBox().y + this.to.getBBox().height / 2 };
        else
            o = dot.pos();

        let r: Point = Utils.connPoint(this.from.getBBox(), o);
        this.fromDot.moveTo(r.x, r.y);

        this.refreshPath();
        //            }

        //            if (to && to.node.id == event.target.id) {
        dot = this.toDot.getLeftDot().getLeftDot();
        if (dot.type == DOT_TYPE.FROM)
            o = { x: this.from.getBBox().x + this.from.getBBox().width / 2, y: this.from.getBBox().y + this.from.getBBox().height / 2 };
        else
            o = dot.pos();

        let r: Point = Utils.connPoint(this.to.getBBox(), o);
        this.toDot.moveTo(r.x, r.y);

        this.refreshPath();
        //            }
    }
}
