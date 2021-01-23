namespace aj.svg {
    const diff = 3;

    /**
     * 点类型的常量
     */
    export enum DOT_TYPE {
        TO = 1,
        FROM = 2,
        SMALL = 3,
        BIG = 4
    }

    /**
     * 表示一个点。点只有坐标，没有大小。
     */
    export class Dot extends BaseComponent {
        /**
         * 左边的点
         */
        private _leftDot: Dot;

        /**
         * 右边的点
         */
        private _rightDot: Dot;

        /**
         * 点的坐标
         */
        private _pos: Point;

        /**
         * 点所在的路径对象
         */
        private path: Path;

        /**
         * 创建一个新的点
         * 
         * @param type      点类型
         * @param _pos      点的坐标
         * @param _lt       左边的点
         * @param _rt       右边的点
         * @param path      点所在的路径对象
         */
        constructor(type: DOT_TYPE, _pos: Point, _lt: Dot, _rt: Dot, path: Path) {
            super();
            let svg: Raphael = PAPER.rect(_pos.x - diff, _pos.y - diff); // 形状实例
            svg.addClass('dot');
            this.svg = svg;
            this.type = type;

            this._leftDot = _lt;
            this._rightDot = _rt;
            this._pos = _pos;
            this.path = path;

            if (type == DOT_TYPE.BIG || type == DOT_TYPE.SMALL) {
                let _ox: number,
                    _oy: number; // 缓存移动前的位置
                    
                svg.drag(
                    (dx: number, dy: number) => this.moveTo(_ox + dx, _oy + dy),
                    () => {
                        _ox = Number(svg.attr("x")) + diff;
                        _oy = Number(svg.attr("y")) + diff;
                    }
                ); // 开始拖动
            }
        }

        /**
         * 获取左边的节点，或者设置左边的点
         * 
         * @param dot 新的点
         */
        left(dot?: Dot): Dot {
            if (dot)
                this._leftDot = dot;

            return this._leftDot;
        }

        /**
         * 获取右边的节点，或者设置右边的点
         * 
         * @param dot 新的点
         */
        right(dot?: Dot): Dot {
            if (dot)
                this._rightDot = dot;

            return this._rightDot;
        }

        remove(): void {
            //@ts-ignore
            this._leftDot = this._rightDot = null;
            this.svg.remove();
        }

        /**
         * 定位这个点，或者获取该点的坐标。如果有新的定位送入，则采用新的定位。
         * 这个方法仅仅是改变点的坐标，要同步修改相关联的点，请使用 moveTo() 方法。
         * 
         * @param p 新的定位坐标，这是可选的
         * @returns 这个点的坐标
         */
        pos(p?: Point): Point {
            if (p && !isNaN(p.x) && !isNaN(p.y)) {
                this._pos = p;
                this.svg.attr({ x: p.x - diff, y: p.y - diff });
            }

            return this._pos;
        }

        /**
         * 移动到某个点，并同步相关的点。
         * 
         * @param x X 坐标 
         * @param y y 坐标
         */
        moveTo(x: number, y: number): void {
            let _pos: Point = this.pos({ x: x, y: y }),
                path: Path = this.path,
                _lt: Dot = this.left(),
                _rt: Dot = this.right(),
                right2x: Dot = _rt && _rt.right(),
                left2x: Dot = _lt && _lt.left();

            switch (this.type) {
                case DOT_TYPE.FROM:
                    if (right2x && right2x.type == DOT_TYPE.TO)
                        right2x.pos(Utils.connPoint(path.to().getBBox(), _pos));

                    if (_rt && right2x)
                        _rt.pos(Utils.center(_pos, right2x.pos()));
                    break;
                case DOT_TYPE.BIG:
                    if (right2x && right2x.type == DOT_TYPE.TO)
                        right2x.pos(Utils.connPoint(path.to().getBBox(), _pos));

                    if (left2x && left2x.type == DOT_TYPE.FROM)
                        left2x.pos(Utils.connPoint(path.from().getBBox(), _pos));

                    if (right2x)
                        _rt?.pos(Utils.center(_pos, right2x.pos()));

                    if (left2x)
                        _lt?.pos(Utils.center(_pos, left2x.pos()));

                    // 三个大点在一条线上，移除中间的小点
                    if (Utils.isLine(left2x?.pos(), _pos, right2x?.pos())) {
                        this.type = DOT_TYPE.SMALL;
                        this.svg.attr({ width: 5, height: 5, stroke: "#fff", fill: "#000", cursor: "move", "stroke-width": 3 });

                        let P = _lt;
                        left2x.right(_lt.right());
                        this._leftDot = _lt = left2x;
                        P?.remove();

                        let R = _rt;
                        right2x.left(_rt.left());
                        this._rightDot = _rt = right2x;
                        R.remove();
                    }

                    break;
                case DOT_TYPE.SMALL: // 移动小点时，转变为大点，增加俩个小点
                    if (_lt && _rt && !Utils.isLine(_lt.pos(), _pos, _rt.pos())) {
                        this.type = DOT_TYPE.BIG; // 变为 BIG 类型之后，只执行一次了
                        this.svg.attr({ width: 5, height: 5, stroke: "#fff", fill: "#000", cursor: "move", "stroke-width": 2 });

                        let P = new Dot(DOT_TYPE.SMALL, Utils.center(_lt.pos(), _pos), _lt, _lt.right(), path);
                        _lt.right(P);
                        this._leftDot = _lt = P;
                        let R = new Dot(DOT_TYPE.SMALL, Utils.center(_rt.pos(), _pos), _rt.left(), _rt, path);
                        _rt.left(R);
                        this._rightDot = _rt = R;
                    }

                    break;
                case DOT_TYPE.TO:
                    if (left2x && left2x.type == DOT_TYPE.FROM)
                        left2x.pos(Utils.connPoint(path.from().getBBox(), _pos));

                    if (left2x)
                        _lt.pos(Utils.center(_pos, left2x.pos()));
                    break;
            }

            path.refreshPath(); // 线的路径, 转换为 path 格式的字串
        }

        toJson() { return ""; }
        show() { }
        hide() { }
    }
}