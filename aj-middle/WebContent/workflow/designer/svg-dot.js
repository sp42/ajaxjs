;(function () {
	var center = aj.svg.util.center, connPoint = aj.svg.util.connPoint, isLine = aj.svg.util.isLine, arrow = aj.svg.util.arrow;
    var diff = 3;
    
    // 点
    function Dot(type, _pos, _lt, _rt, path) {
        var svg = PAPER.rect(_pos.x - diff, _pos.y - diff); // 形状实例
        svg.addClass('dot');
        this.svg = svg;
        this.type = type;

        if (type == Dot.BIG || type == Dot.SMALL) {
            var _ox, _oy; // 缓存移动前的位置
            svg.drag((dx, dy) => this.moveTo(_ox + dx, _oy + dy), () => { _ox = svg.attr("x") + diff, _oy = svg.attr("y") + diff; }); // 开始拖动
        }

        this.left = p => {
            if (p) _lt = p;
            else return _lt;
        }

        this.right = p => {
            if (p) _rt = p;
            else return _rt;
        }

        this.remove = () => {
            _lt = _rt = null;
            svg.remove();
        }

        this.pos = p => {
            if (p) {
                _pos = p;
                svg.attr({ x: p.x - diff, y: p.y - diff });

                return this;
            } else return _pos;
        }

        this.moveTo = (Q, T) => {
            this.pos({ x: Q,  y: T });
            
            var right2x = _rt && _rt.right(), left2x = _lt && _lt.left();

            switch (this.type) {
            case Dot.FROM:
                if (right2x && right2x.type == Dot.TO)
                    right2x.pos(connPoint(path.to().getBBox(), _pos));

                if (right2x)
                    _rt.pos(center(_pos, right2x.pos()));
                break;
            case Dot.BIG:
                if (right2x && right2x.type == Dot.TO)
                    right2x.pos(connPoint(path.to().getBBox(), _pos));

                if (left2x && left2x.type == Dot.FROM)
                    left2x.pos(connPoint(path.from().getBBox(), _pos));

                if (right2x)
                    _rt.pos(center(_pos, right2x.pos()));

                if (left2x)
                    _lt.pos(center(_pos, left2x.pos()));

                // 三个大点在一条线上，移除中间的小点
                if (isLine(left2x.pos(), _pos, right2x.pos())) {
                    this.type = Dot.SMALL;
                    svg.attr({ width: 5, height: 5, stroke: "#fff", fill: "#000", cursor: "move", "stroke-width": 3 });

                    var P = _lt;
                    left2x.right(_lt.right());
                    _lt = left2x;
                    P.remove();

                    var R = _rt;
                    right2x.left(_rt.left());
                    _rt = right2x;
                    R.remove();
                }

                break;
            case Dot.SMALL: // 移动小点时，转变为大点，增加俩个小点
                if (_lt && _rt && !isLine(_lt.pos(), _pos, _rt.pos())) {
                    this.type = Dot.BIG; // 变为 BIG 类型之后，只执行一次了
                    svg.attr({ width: 5, height: 5,stroke: "#fff",  fill: "#000", cursor: "move","stroke-width": 2 });

                    var P = new Dot(Dot.SMALL, center(_lt.pos(), _pos), _lt, _lt.right(), path);
                    _lt.right(P);
                    _lt = P;
                    var R = new Dot(Dot.SMALL, center(_rt.pos(), _pos), _rt.left(), _rt, path);
                    _rt.left(R);
                    _rt = R;
                }

                break;
            case Dot.TO:
                if (left2x && left2x.type == Dot.FROM)
                    left2x.pos(connPoint(path.from().getBBox(), _pos));

                if (left2x)
                    _lt.pos(center(_pos, left2x.pos()));
                break;
            }

            path.refreshPath(); // 线的路径, 转换为 path 格式的字串
        }
    }

    Dot.TO = 1;
    Dot.FROM = 2;
    Dot.SMALL = 3;
    Dot.BIG = 4;
    
    /**
     * 先有点再有线
     * @class
     */
    function DotList(from, to) {
        var fromBB = from.getBBox(),  toBB = to.getBBox();
        var fromPos = connPoint(fromBB, {  x: toBB.x + toBB.width / 2,  y: toBB.y + toBB.height / 2 }), // 起点是 box 中央，这个是起点坐标
        	toPos   = connPoint(toBB, fromPos); // 终点坐标
        
        /* 先创建中间点，一开始左右点都是 null 的，——这没关系，先空着，下面会计算 */
        var midDot = new Dot(Dot.SMALL, { x: (fromPos.x + toPos.x) / 2, y: (fromPos.y + toPos.y) / 2 }, null, null, this); // 中间点
        /* 起点没有 left 点，right 点就是 midDot 了 */ 
        var fromDot = new Dot(Dot.FROM, fromPos, null, midDot, this);// 起点对象
        /* 终点没有 right 点，left 点就是 midDot 了 */ 
        var toDot = new Dot(Dot.TO, toPos, midDot, null, this);// 重点对象
        /* 补充中间点的左右两点信息 */
        midDot.left(fromDot), midDot.right(toDot);

        this.fromDot = fromDot, this.toDot = toDot;

        // 转换为 path 格式的字串
        this.toPathString = () => {
            if (!fromDot)
                return "";

            var d = fromDot, p = "M" + d.pos().x + " " + d.pos().y, arr = [];

            // 线的路径
            while (d.right()) {
                d = d.right();
                arr.push("L" + d.pos().x + " " + d.pos().y);
            }
            
            p += arr.join('');

            // 箭头路径
            var arrPos = arrow(d.left().pos(), d.pos(), 4);
            var _arrow = "M" + arrPos[0].x + " " + arrPos[0].y + "L" + arrPos[1].x + " " + arrPos[1].y + "L" + arrPos[2].x + " " + arrPos[2].y + "z";

            return [p, _arrow];
        }

        // 遍历链表，查找中间的点
        this.midDot = () => {
            var mid = fromDot.right(), end = mid.right();

            while (end.right() && end.right().right()) {
                end = end.right().right();
                mid = mid.right();
            }

            return mid;
        }

        // 显示或隐藏
        this.show = display.bind(this, fromDot, true);
        this.hide = display.bind(this, fromDot, false);
    }
    
    // for super
    DotList.prototype = {
		// 移除
        remove () {
            var d = this.fromDot;
            
            while (d) {
                if (d.right()) {
                    d = d.right();
                    d.left().remove()
                } else {
                    d.remove();
                    d = null;
                }
            }
        },
    	toJson() {
    		return aj.svg.serialize.dotList.toJson(this.fromDot);
    	},
        restore (obj) {/* 如果遇到未创建的点 会自动创建（SmallDot的 case 分支） */
            var d = this.fromDot.right();

            for (var i = 0; i < obj.length; i++) {
                d.moveTo(obj[i].x, obj[i].y);
                d.moveTo(obj[i].x, obj[i].y);
                d = d.right();
            }
        }
    };
    
    aj.svg.Dot = Dot;
    aj.svg.DotList = DotList;

    /**
     * @param {Dot}
     *            开始的点 fromDot
     * @param {boolean}
     *            true=显示
     */
    function display(dotObj, isShow) {
        while (dotObj) {
            isShow ? dotObj.svg.show() : dotObj.svg.hide();
            dotObj = dotObj.right();
        }
    }

})();