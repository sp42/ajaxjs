// 点
ajflow.Dot = function (type, pos, left, right, _o, _from, _to, path) {
    var _n, // 形状实例
		_lt = left, _rt = right, 
		_ox, _oy, // 缓存移动前时位置
		_pos = pos;// 缓存位置信息{x,y}, 注意：这是计算出中心点

    switch (type) {
    case "from":
        _n = ajflow.PAPER.rect(pos.x - _o.attr.fromDot.width / 2, pos.y - _o.attr.fromDot.height / 2, _o.attr.fromDot.width, _o.attr.fromDot.height).attr(_o.attr.fromDot);
        break;
    case "big":
        _n = ajflow.PAPER.rect(pos.x - _o.attr.bigDot.width / 2, pos.y - _o.attr.bigDot.height / 2, _o.attr.bigDot.width, _o.attr.bigDot.height).attr(_o.attr.bigDot);
        break;
    case "small":
        _n = ajflow.PAPER.rect(pos.x - _o.attr.smallDot.width / 2, pos.y - _o.attr.smallDot.height / 2, _o.attr.smallDot.width, _o.attr.smallDot.height).attr(_o.attr.smallDot);
        break;
    case "to":
        _n = ajflow.PAPER.rect(pos.x - _o.attr.toDot.width / 2, pos.y - _o.attr.toDot.height / 2, _o.attr.toDot.width, _o.attr.toDot.height).attr(_o.attr.toDot);
        break;
    }

    if (_n && (type == "big" || type == "small")) {
        var dragStart = () => {// 开始拖动
            if (type == "big") {
                _ox = _n.attr("x") + _o.attr.bigDot.width / 2;
                _oy = _n.attr("y") + _o.attr.bigDot.height / 2
            }

            if (type == "small") {
                _ox = _n.attr("x") + _o.attr.smallDot.width / 2;
                _oy = _n.attr("y") + _o.attr.smallDot.height / 2
            }
        }, dragMove = (dx, dy) => this.moveTo(_ox + dx, _oy + dy); // 拖动中

    	_n.drag(dragMove, dragStart);
    }

    this.type = function(P) {
        if (P) type = P;
        else return type;
    };

    this.node = function(P) {
        if (P) _n = P;
        else return _n;
    };

    this.left = function(P) {
        if (P) _lt = P;
        else return _lt;
    };

    this.right = function(P) {
        if (P) _rt = P;
        else return _rt;
    };

    this.remove = function() {
        _lt = null;
        _rt = null;
        _n.remove();
    };

    this.pos = function(P) {
        if (P) {
            _pos = P;
            _n.attr({ x: _pos.x - _n.attr("width") / 2, y: _pos.y - _n.attr("height") / 2 });

            return this;
        } else return _pos;
    };

    this.moveTo = function(Q, T) {
        this.pos({x: Q,  y: T});

        switch (type) {
        case "from":
            if (_rt && _rt.right() && _rt.right().type() == "to") 
                _rt.right().pos(ajflow.util.connPoint(_to.getBBox(), _pos))
            
            if (_rt && _rt.right()) 
                _rt.pos(ajflow.util.center(_pos, _rt.right().pos()))
            
            break;
        case "big":
            if (_rt && _rt.right() && _rt.right().type() == "to") 
                _rt.right().pos(ajflow.util.connPoint(_to.getBBox(), _pos))
            
            if (_lt && _lt.left() && _lt.left().type() == "from") 
                _lt.left().pos(ajflow.util.connPoint(_from.getBBox(), _pos))
            
            if (_rt && _rt.right()) 
                _rt.pos(ajflow.util.center(_pos, _rt.right().pos()))
            
            if (_lt && _lt.left()) 
                _lt.pos(ajflow.util.center(_pos, _lt.left().pos()))
            
			// 三个大点在一条线上，移除中间的小点
            var S = {x: _pos.x, y: _pos.y};

            if (ajflow.util.isLine(_lt.left().pos(), S, _rt.right().pos())) {
                type = "small";
                _n.attr(_o.attr.smallDot);
                this.pos(S);

                var P = _lt;
                _lt.left().right(_lt.right());
                _lt = _lt.left();
                P.remove();
                var R = _rt;
                _rt.right().left(_rt.left());
                _rt = _rt.right();
                R.remove();
            }

            break;
        case "small":// 移动小点时，转变为大点，增加俩个小点
            if (_lt && _rt && !ajflow.util.isLine(_lt.pos(), { x: _pos.x, y: _pos.y}, _rt.pos())) {
                type = "big";
                _n.attr(_o.attr.bigDot);
 
                var P = new ajflow.Dot("small", ajflow.util.center(_lt.pos(), _pos), _lt, _lt.right(), _o, _from, _to, path);
                _lt.right(P);
                _lt = P;
                var R = new ajflow.Dot("small", ajflow.util.center(_rt.pos(), _pos), _rt.left(), _rt, _o, _from, _to, path);
                _rt.left(R);
                _rt = R;
            }

            break;
        case "to":
            if (_lt && _lt.left() && _lt.left().type() == "from") 
                _lt.left().pos(ajflow.util.connPoint(_from.getBBox(), _pos));
            
            if (_lt && _lt.left()) 
                _lt.pos(ajflow.util.center(_pos, _lt.left().pos()));
            
            break;
        }

        path.refreshpath();
    }
}

ajflow.DotList = function(_from, _to, _this, _o) {
    var _fromBB = _from.getBBox(), _toBB = _to.getBBox();
	var _fromPos = ajflow.util.connPoint(_fromBB, { x: _toBB.x + _toBB.width / 2, y: _toBB.y + _toBB.height / 2});
    var o = ajflow.util.connPoint(_toBB, _fromPos);
	var smallDot = new ajflow.Dot("small", { x: (_fromPos.x + o.x) / 2, y: (_fromPos.y + o.y) / 2 }, null, null, _o, _from, _to, _this);
   
	var _fromDot = new ajflow.Dot("from", _fromPos, null, smallDot, _o, _from, _to, _this);
    _fromDot.right().left(_fromDot);
    var _toDot = new ajflow.Dot("to", o, _fromDot.right(), null, _o, _from, _to, _this);
    _fromDot.right().right(_toDot);

	// 转换为path格式的字串
    this.toPathString = function() {
        if (!_fromDot) 
            return "";
        
        var d = _fromDot, p = "M" + d.pos().x + " " + d.pos().y, arr = "";

		// 线的路径
        while (d.right()) {
            d = d.right();
            p += "L" + d.pos().x + " " + d.pos().y
        }

		// 箭头路径
        var arrPos = ajflow.util.arrow(d.left().pos(), d.pos(), _o.attr.arrow.radius);
        arr = "M" + arrPos[0].x + " " + arrPos[0].y + "L" + arrPos[1].x + " " + arrPos[1].y + "L" + arrPos[2].x + " " + arrPos[2].y + "z";

        return [p, arr];
    }

    this.toJson = function() {
        return ajflow.serialize.dotList.toJson(_fromDot);
    }

    this.toXml = function() {
        return ajflow.serialize.dotList.toXml(_fromDot);
    }

    this.restore = function(data) {
        var obj = data, d = _fromDot.right();

        for (var i = 0; i < obj.length; i++) {
            d.moveTo(obj[i].x, obj[i].y);
            d.moveTo(obj[i].x, obj[i].y);
            d = d.right();
        }

        this.hide();
    }

    this.fromDot = function() {
        return _fromDot;
    }

    this.toDot = function() {
        return _toDot;
    }

    this.midDot = function() {
        var mid = _fromDot.right(), end = _fromDot.right().right();

        while (end.right() && end.right().right()) {
            end = end.right().right();
            mid = mid.right();
        }

        return mid;
    }

    this.show = function() {
        var d = _fromDot;
        while (d) {
            d.node().show();
            d = d.right();
        }
    }

    this.hide = function() {
        var d = _fromDot;
        while (d) {
            d.node().hide();
            d = d.right();
        }
    }

    this.remove = function() {
        var d = _fromDot;
        while (d) {
            if (d.right()) {
                d = d.right();
                d.left().remove()
            } else {
                d.remove();
                d = null;
            }
        }
    }
}