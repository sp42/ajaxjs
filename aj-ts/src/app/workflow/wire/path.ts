namespace aj.svg {
    /**
     * 连线路径
     */
    export class Path extends DotList {
        _from: any;
        _to: any;

        private moveFn: Function;

        /**
         * 箭头图形
         */
        private arrow: Raphael;

        constructor(from, to, text) {
            // 继承
            super(from, to);

            this._from = from;
            this._to = to;

            this.type = 'transition'; // 默认 path 就是 Transition 类型
            this.svg = PAPER.path().addClass('path');
            this.arrow = PAPER.path().addClass('arrow');

            // aj.svg.DotList.call(this, from, to);
            // aj.apply(this, aj.svg.DotList.prototype);

            aj.svg.Mgr.register(this);
            this.hide();

            this.moveFn = this.rectResizeHandler.bind(this);
            from.vue.addUpdateHandler(this.moveFn);
            to.vue.addUpdateHandler(this.moveFn);

            this.refreshPath();
        }

        // 矩形移动时处理器
        rectResizeHandler(): void {
            let o, dot;

            //            if (from && from.node.id == event.target.id) {
            dot = this.fromDot.right().right();
            if (dot.type == DOT_TYPE.TO)
                o = { x: this._to.getBBox().x + this._to.getBBox().width / 2, y: this._to.getBBox().y + this._to.getBBox().height / 2 };
            else
                o = dot.pos();

            let r = Utils.connPoint(this._from.getBBox(), o);
            this.fromDot.moveTo(r.x, r.y);

            this.refreshPath();
            //            }

            //            if (to && to.node.id == event.target.id) {
            dot = this.toDot.left().left();
            if (dot.type == DOT_TYPE.FROM)
                o = { x: this._from.getBBox().x + this._from.getBBox().width / 2, y: this._from.getBBox().y + this._from.getBBox().height / 2 };
            else
                o = dot.pos();

            let r = Utils.connPoint(this._to.getBBox(), o);
            this.toDot.moveTo(r.x, r.y);

            this.refreshPath();
            //            }
        }

        from() {
            return this._from;
        }

        to() {
            return this._to;
        }

        remove(): void {
            aj.svg.DotList.prototype.remove.call(this);
            this.svg.remove();
            this.arrow.remove();

            if (this.textObj) {
                this.textObj.$el.parentNode.removeChild(this.textObj.$el);
                this.textObj = null;
            }

            this.from.vue.removeUpdateHandler(this.moveFn); // 卸载事件
            this.to.vue.removeUpdateHandler(this.moveFn);
        };

        // 刷新路径
        refreshPath(): void {
            let r = this.toPathString(), mid = this.midDot().pos();

            this.svg.attr({ path: r[0] });
            this.arrow.attr({ path: r[1] });

            if (this.textObj) {
                let textPos = this.textObj.getXY();// 定位文字
                this.textObj.setXY(mid.x + 30, mid.y + 10);
            }
        }

        attr(o): void {
            o && o.path && this.svg.attr(o.path);
            o && o.arrow && this.arrow.attr(o.arrow);
        }
    }

}
