// 路径
// @class
ajflow.Path = function(o, _from, _to) {
	var snaker = window.$;
    var _this = this, _r = ajflow.PAPER
	var _o = aj.apply({}, ajflow.config.path), _textPos = _o.textPos;

	// 初始化操作
    _o = aj.extend(true, _o, o);
    var _ox, _oy, _id = "path" + ajflow.util.nextId(),
	_path = ajflow.PAPER.path(_o.attr.path.path).attr(_o.attr.path),
    _arrow = ajflow.PAPER.path(_o.attr.arrow.path).attr(_o.attr.arrow),
    _dotList = new ajflow.DotList(_from, _to, this, _o);
    _dotList.hide();

    var _text = ajflow.PAPER.text(0, 0, _o.text.text).attr(_o.text).attr({text: _o.text.text.replace("{from}", _from.text()).replace("{to}", _to.text())});

    _text.drag((r, o) => {
        if (!ajflow.config.editable)
            return;
        
        _text.attr({x: _ox + r, y: _oy + o});
    }, () => {
        _ox = _text.attr("x");
        _oy = _text.attr("y");
    }, () => {
        var o = _dotList.midDot().pos();
        _textPos = {x: _text.attr("x") - o.x, y: _text.attr("y") - o.y};
    });

    refreshpath();

    $([_path.node, _arrow.node]).bind("click", () => {
		console.log('选中路径 path 或 箭头');
        if (!ajflow.config.editable) 
            return;
        
        snaker(_r).trigger("click", this);
        ajflow.DESIGNER.currentNode = this;

        return false;
    });

	// 处理点击事件，线或矩形
    var clickHandler = function(e, src) {
        if (!ajflow.config.editable) 
            return;
        
        if (src && src.getId() == _id) {
            _dotList.show();
            snaker(_r).trigger("showprops", [_o.props, _this])
        } else 
            _dotList.hide()

        switch (snaker(_r).data("mod")) {
        case "pointer":
            break;
        case "path":
            break
        }
    }

    $(_r).bind("click", clickHandler);

	// 删除事件处理
    var removerectHandler = function(e, src) {
		console.log('删除路径 path');
        if (!ajflow.config.editable) 
            return;
        
        if (src && (src.getId() == _from.getId() || src.getId() == _to.getId())) 
            snaker(_r).trigger("removepath", _this);
    }

    $(_r).bind("removerect", removerectHandler);

	// 矩形移动时处理器
    var rectresizeHandler = function(e, src) {
        if (!ajflow.config.editable)
            return;
        
        if (_from && _from.getId() == src.getId()) {
            var o;
            if (_dotList.fromDot().right().right().type() == "to") 
                o = { x: _to.getBBox().x + _to.getBBox().width / 2, y: _to.getBBox().y + _to.getBBox().height / 2 };
            else
                o = _dotList.fromDot().right().right().pos();
            
            var r = ajflow.util.connPoint(_from.getBBox(), o);
            _dotList.fromDot().moveTo(r.x, r.y);

            refreshpath();
        }

        if (_to && _to.getId() == src.getId()) {
            var o;
            if (_dotList.toDot().left().left().type() == "from")
                o = { x: _from.getBBox().x + _from.getBBox().width / 2, y: _from.getBBox().y + _from.getBBox().height / 2 };
            else 
                o = _dotList.toDot().left().left().pos();
            
            var r = ajflow.util.connPoint(_to.getBBox(), o);
            _dotList.toDot().moveTo(r.x, r.y);

            refreshpath();
        }
    };

    $(_r).bind("rectresize", rectresizeHandler);

    var textchangeHandler = function(e, v, src) {
        if (src.getId() == _id) // 改变自身文本
            _text.attr({ text: v});
    }

    $(_r).bind("textchange", textchangeHandler);

	// 函数-------------------------------------------------
    this.from = function() {
        return _from;
    };

    this.to = function() {
        return _to;
    };

	// 转化json数据
    this.toJson = function() {
        return ajflow.serialize.path.toJson(_from, _to, _dotList, _text, _textPos);
    };

    this.toXml = function() {
        return ajflow.serialize.path.toXml(_o, _to, _dotList, _textPos);
    };

    this.restore = function(data) {
        var obj = data;
        _o.props.displayName.value = obj.text.text;
        _o = aj.extend(true, _o, data);
        _dotList.restore(obj.dots)
    };

    this.remove = function() {
        _dotList.remove();
        _path.remove();
        _arrow.remove();
        _text.remove();

        try {
            snaker(_r).unbind("click", clickHandler)
        } catch(o) {}
        try {
            snaker(_r).unbind("removerect", removerectHandler)
        } catch(o) {}
        try {
            snaker(_r).unbind("rectresize", rectresizeHandler)
        } catch(o) {}
        try {
            snaker(_r).unbind("textchange", textchangeHandler)
        } catch(o) {}
    };

	// 刷新路径
    function refreshpath() {
        var r = _dotList.toPathString(), mid = _dotList.midDot().pos();
        _path.attr({ path: r[0] });
        _arrow.attr({ path: r[1]});
        _text.attr({ x: mid.x + _textPos.x, y: mid.y + _textPos.y });
    }

	this.refreshpath = refreshpath;

    this.getId = function() {
        return _id;
    };

    this.text = function() {
        return _text.attr("text")
    };

    this.name = function() {
        return _o.props["name"].value;
    }

    this.attr = function(o) {
        o && o.path &&_path.attr(o.path);
		o && o.arrow && _arrow.attr(o.arrow);
    }
};