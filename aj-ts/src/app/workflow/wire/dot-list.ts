namespace aj.svg {
    /**
    * 先有点再有线
    */
    export class DotList extends BaseComponent {
        /**
         * 起点
         */
        public fromDot: Dot;

        /**
         * 终点
         */
        public toDot: Dot;

        /**
         * 
         * @param from  起始连接的组件
         * @param to    终点连接的组件
         */
        constructor(from: Raphael, to: Raphael) {
            super();
            let fromBB: VBox = from.getBBox(),
                toBB: VBox = to.getBBox(),
                fromPos: Point = Utils.connPoint(fromBB, { x: toBB.x + toBB.width / 2, y: toBB.y + toBB.height / 2 }), // 起点是 box 中央，这个是起点坐标
                toPos: Point = Utils.connPoint(toBB, fromPos),// 终点坐标
                /* 先创建中间点，一开始左右点都是 null 的，——这没关系，先空着，下面会计算 */
                midDot: Dot = new Dot(DOT_TYPE.SMALL, { x: (fromPos.x + toPos.x) / 2, y: (fromPos.y + toPos.y) / 2 }, null, null, this); // 中间点

            /* 起点没有 left 点，right 点就是 midDot 了 */
            this.fromDot = new Dot(DOT_TYPE.FROM, fromPos, null, midDot, this);// 起点对象
            /* 终点没有 right 点，left 点就是 midDot 了 */
            this.toDot = new Dot(DOT_TYPE.TO, toPos, midDot, null, this);// 终点对象

            /* 补充中间点的左右两点信息 */
            midDot.left(this.fromDot), midDot.right(this.toDot);
        }

        /**
         * 转换为 path 格式的字串
         * 
         * @returns 数组，其中元素1是线的路径，元素2是箭头的路径
         */
        toPathString(): [string, string] | string {
            if (!this.fromDot)
                return "";

            let d: Dot = this.fromDot,
                p: string = "M" + d.pos().x + " " + d.pos().y, // 线的路径
                arr: string[] = [];

            // 线的路径
            while (d.right()) {
                d = d.right();
                arr.push("L" + d.pos().x + " " + d.pos().y);
            }

            p += arr.join('');

            // 根据箭头三点的坐标生成 SVG 路径
            let arrPos: Point[] = Utils.arrow(d.left().pos(), d.pos(), 4),
                arrow: string = "M" + arrPos[0].x + " " + arrPos[0].y + "L" + arrPos[1].x + " " + arrPos[1].y + "L" + arrPos[2].x + " " + arrPos[2].y + "z";

            return [p, arrow];
        }

        /**
         * 遍历链表，查找中间的点
         */
        midDot(): Dot {
            let mid: Dot = this.fromDot.right(), end: Dot = mid.right();

            while (end.right() && end.right().right()) {
                end = end.right().right();
                mid = mid.right();
            }

            return mid;
        }

        remove(): void {
            let d: Dot = this.fromDot;

            while (d) {
                if (d.right()) {
                    d = d.right();
                    d.left().remove()
                } else {
                    d.remove();
                    // @ts-ignore
                    d = null;
                }
            }
        }

        toJson() {
            return aj.svg.serialize.dotList.toJson(this.fromDot);
        }

        /**
         * 输入坐标数据，还原图形
         * 
         * @param points 坐标数组
         */
        restore(points: Point[]): void {   /* 如果遇到未创建的点 会自动创建（SmallDot 的 case 分支） */
            let d: Dot = this.fromDot.right(), point: Point;

            for (let i = 0, j = points.length; i < j; i++) {
                point = points[i];

                if (d) {
                    d.moveTo(point.x, point.y);
                    d = d.right();
                }
            }
        }
        /**
         * 显示
         */
        show() {
            display.call(this, this.fromDot, true);
        }

        /**
         * 隐藏
         */
        hide() {
            display.call(this, this.fromDot, false);
        }
    }


    /**
     * 显示或隐藏
     * 
     * @param {Dot}     开始的点 fromDot
     * @param {boolean} true=显示
     */
    function display(dotObj: Dot | null, isShow: boolean): void {
        while (dotObj) {
            isShow ? dotObj.svg.show() : dotObj.svg.hide();
            dotObj = dotObj.right();
        }
    }
}