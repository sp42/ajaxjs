// @class 
ajflow.Rect = function(o) {
	var snaker = window.$;
    var _this = this, _id = 'rect' + ajflow.util.nextId(),
	    _o = aj.extend(true, {}, ajflow.config.rect, o),
	    _r = ajflow.PAPER,
	    _ox, _oy; // 拖动时，保存起点位置;

    var _rect = _r.rect(_o.attr.x, _o.attr.y).hide().addClass('ajflowRect').attr(_o.attr),// 图标
    _img  = _r.image(ajflow.config.basePath + _o.img.src, _o.attr.x + _o.img.width / 2, _o.attr.y + (_o.attr.height - _o.img.height) / 2).hide();

    var _name = _r.text(_o.attr.x + _o.img.width + (_o.attr.width - _o.img.width) / 2, 
				_o.attr.y + ajflow.config.lineHeight / 2, _o.name.text).hide().attr(_o.name);// 状态名称
				
    var _text = _r.text(_o.attr.x + _o.img.width + (_o.attr.width - _o.img.width) / 2, // 显示文本
				_o.attr.y + (_o.attr.height - ajflow.config.lineHeight) / 2 + 
				ajflow.config.lineHeight, _o.text.text).hide().attr(_o.text); 

	// 点击事件
    _text.click(() => {
        if (!ajflow.config.editable) 
            ajflow.util.tip(_rect, _o.props['name'].value);
        else {
            /*var returnValue = window.showModalDialog(ajflow.config.ctxPath + '/config/form?lookup=1', window, 'dialogWidth:1000px;dialogHeight:600px');
            
			if (returnValue) {
                var formPath = "/config/form/use/" + returnValue;
                _o.props.form.value = formPath;
                document.getElementById("pform").innerHTML = '<input style="width:98%;" value="' + formPath + '"/>';
            }*/
        }
    });

    _rect.click(() => {
        if (!ajflow.config.editable) 
			ajflow.util.tip(_rect, _o.props['name'].value);
    });

    _rect.dblclick(() => {
        if (ajflow.config.editable) {
  /*          var returnValue = window.showModalDialog(ajflow.config.ctxPath + '/config/form?lookup=1', window, 'dialogWidth:1000px;dialogHeight:600px');
           
			if (returnValue) {
                var formPath = "/config/form/use/" + returnValue;
                _o.props.form.value = formPath;
                document.getElementById("pform").innerHTML = '<input style="width:98%;" value="' + formPath + '"/>';
            }*/
        }
    });

    // 拖动处理----------------------------------------
    var dragStart = () => { // 开始拖动
        _ox = _rect.attr("x"), _oy = _rect.attr("y");

		var o = {opacity: .5};
        _rect.attr(o);
        _img.attr(o);
        _text.attr(o);
    }, dragMove = (dx, dy) => { // 拖动中
        if (!ajflow.config.editable) 
			return;
		console.log("拖动中");
        
		var x = (_ox + dx); // -((_ox+dx)%10);
        var y = (_oy + dy); // -((_oy+dy)%10);
        _bbox.x = x - _o.margin;
        _bbox.y = y - _o.margin;

        resize();
    }, dragUp = () => { // 拖动结束
		var o = {opacity: 1};
        _rect.attr(o);
        _img.attr(o);
        _text.attr(o);
    } 

    _rect.drag(dragMove, dragStart, dragUp);
    _img.drag(dragMove, dragStart, dragUp);
    _name.drag(dragMove, dragStart, dragUp);
    _text.drag(dragMove, dragStart, dragUp);// 文字，连同 rect 一起

	// 改变大小的边框
	function bdragMove(dx, dy, t) {
        if (!ajflow.config.editable) 
			return;
			
        var x = _bx + dx, y = _by + dy;

        switch (t) {
        case 't':
            _bbox.height += _bbox.y - y; _bbox.y = y;
            break;
        case 'lt':
            _bbox.width += _bbox.x - x; _bbox.height += _bbox.y - y;
            _bbox.x = x; _bbox.y = y;
            break;
        case 'l':
            _bbox.width += _bbox.x - x;
            _bbox.x = x;
            break;
        case 'lb':
            _bbox.height = y - _bbox.y; _bbox.width += _bbox.x - x;
            _bbox.x = x;
            break;
        case 'b':
            _bbox.height = y - _bbox.y;
            break;
        case 'rb':
            _bbox.height = y - _bbox.y; _bbox.width = x - _bbox.x;
            break;
        case 'r':
            _bbox.width = x - _bbox.x;
            break;
        case 'rt':
            _bbox.width = x - _bbox.x; _bbox.height += _bbox.y - y;
            _bbox.y = y;
            break;
        }

        resize();
        // $('body').append(t);
    }

    function bdragStart() {
        _bx = this.attr('x') + _bw, _by = this.attr('y') + _bw;
    }

    var _bdots = {}, _bw = 5 / 2, _bbox = {
        x: _o.attr.x - _o.margin, y: _o.attr.y - _o.margin, width: _o.attr.width + _o.margin * 2, height: _o.attr.height + _o.margin * 2
    }, _bpath = _r.path('M0 0L1 1').hide();
	
    _bdots['t'] = _r.rect().addClass('resizeDot').attr({cursor: 's-resize'}).hide().drag((dx, dy) => bdragMove(dx, dy, 't'), bdragStart); 		// 上
	_bdots['lt'] = _r.rect().addClass('resizeDot').attr({cursor: 'nw-resize'}).hide().drag((dx, dy) => bdragMove(dx, dy, 'lt'), bdragStart); 	// 左上
    _bdots['l'] = _r.rect().addClass('resizeDot').attr({cursor: 'w-resize'}).hide().drag((dx, dy) => bdragMove(dx, dy, 'l'),bdragStart); 		// 左
    _bdots['lb'] = _r.rect().addClass('resizeDot').attr({cursor: 'sw-resize'}).hide().drag((dx, dy) => bdragMove(dx, dy, 'lb'), bdragStart); 	// 左下
    _bdots['b'] = _r.rect().addClass('resizeDot').attr({cursor: 's-resize'}).hide().drag((dx, dy) => bdragMove(dx, dy, 'b'), bdragStart); 		// 下
    _bdots['rb'] = _r.rect().addClass('resizeDot').attr({cursor: 'se-resize'}).hide().drag((dx, dy) => bdragMove(dx, dy, 'rb'), bdragStart); 	// 右下
    _bdots['r'] = _r.rect().addClass('resizeDot').attr({cursor: 'w-resize'}).hide().drag((dx, dy) => bdragMove(dx, dy, 'r'), bdragStart); 		// 右
    _bdots['rt'] = _r.rect().addClass('resizeDot').attr({cursor: 'ne-resize'}).hide().drag((dx, dy) => bdragMove(dx, dy, 'rt'), bdragStart); 	// 右上
	
    $([_bdots['t'].node, _bdots['lt'].node, _bdots['l'].node, _bdots['lb'].node,
		 _bdots['b'].node, _bdots['rb'].node, _bdots['r'].node, _bdots['rt'].node]).click(() => {
        return false;
    });

    // 事件处理--------------------------------
    snaker([_rect.node, _text.node, _name.node, _img.node]).bind('click', () => {
        if (!ajflow.config.editable)
			return;

        showBox();
        var mod = snaker(_r).data('mod');

        switch (mod) {
            case 'pointer':
                break;
            case 'path':
                var pre =  ajflow.DESIGNER.currentNode;
                if (pre && pre.getId() != _id && pre.getId().substring(0, 4) == 'rect') 
                    ajflow.DESIGNER.addPath(pre, _this);
                
                break;
        }

        snaker(_r).trigger('click', _this);
		ajflow.DESIGNER.currentNode = _this;

        return false;
    });
	
	// 
    snaker(_r).bind('click', (e, src) => {
        if (!ajflow.config.editable) return;
       
		if (src.getId() == _id) 
            snaker(_r).trigger('showprops', [_o.props, src]);
        else 
            hideBox();
    });

    snaker(_r).bind('textchange', (e, text, src) => {
        if (src.getId() == _id) 
            _text.attr({text: text});
    });

    // 私有函数-----------------------
    // 边框路径
    function getBoxPathString() {
        return 'M' + _bbox.x + ' ' + _bbox.y + 'L' + _bbox.x + ' ' + (_bbox.y + _bbox.height) + 'L' + (_bbox.x + _bbox.width) +
			  ' ' + (_bbox.y + _bbox.height) + 'L' + (_bbox.x + _bbox.width) + ' ' + _bbox.y + 'L' + _bbox.x + ' ' + _bbox.y;
    }

    function showBox() {
        _bpath.show();  // 显示边框

        for (var k in _bdots) // 逐个点显示
            _bdots[k].show();
    }

    // 隐藏
    function hideBox() {
        _bpath.hide();

        for (var k in _bdots) 
            _bdots[k].hide();
    }

    // 根据_bbox，更新位置信息
    function resize() {
        var rx = _bbox.x + _o.margin, ry = _bbox.y + _o.margin, rw = _bbox.width - _o.margin * 2, rh = _bbox.height - _o.margin * 2;
        _rect.attr({ x: rx, y: ry, width: rw, height: rh });

        switch (_o.showType) {
        case 'image':
            _img.attr({ x: rx + (rw - _o.img.width) / 2, y: ry + (rh - _o.img.height) / 2 }).show();
            break;
        case 'text':
            _rect.show();
            _text.attr({ x: rx + rw / 2, y: ry + rh / 2 }).show(); // 文本
            break;
        case 'image&text':
            _rect.show();
            _name.attr({ x: rx + _o.img.width + (rw - _o.img.width) / 2, y: ry + ajflow.config.lineHeight / 2 }).show();
            _text.attr({ x: rx + _o.img.width + (rw - _o.img.width) / 2, y: ry + (rh - ajflow.config.lineHeight) / 2 + ajflow.config.lineHeight }).show(); // 文本
            _img.attr({ x: rx + _o.img.width / 2, y: ry + (rh - _o.img.height) / 2 }).show();
            break;
        }

        _bdots['t'].attr({ x: _bbox.x + _bbox.width / 2 - _bw, y: _bbox.y - _bw }); 				// 上
        _bdots['lt'].attr({ x: _bbox.x - _bw, y: _bbox.y - _bw }); 									// 左上
        _bdots['l'].attr({ x: _bbox.x - _bw, y: _bbox.y - _bw + _bbox.height / 2 }); 				// 左
        _bdots['lb'].attr({ x: _bbox.x - _bw, y: _bbox.y - _bw + _bbox.height }); 					// 左下
        _bdots['b'].attr({ x: _bbox.x - _bw + _bbox.width / 2, y: _bbox.y - _bw + _bbox.height }); 	// 下
        _bdots['rb'].attr({ x: _bbox.x - _bw + _bbox.width, y: _bbox.y - _bw + _bbox.height }); 	// 右下
        _bdots['r'].attr({ x: _bbox.x - _bw + _bbox.width,y: _bbox.y - _bw + _bbox.height / 2 }); 	// 右
        _bdots['rt'].attr({ x: _bbox.x - _bw + _bbox.width, y: _bbox.y - _bw }); 					// 右上
        _bpath.attr({path: getBoxPathString()});

        $(_r).trigger('rectresize', _this);
    };

    // 函数----------------
    // 转化json字串
    this.toJson = function() {
        return ajflow.serialize.rect.toJson(_o, _text, _rect);
    };

    this.toBeforeXml = function() {
        return ajflow.serialize.rect.toBeforeXml(_o, _text, _rect);
    };

    this.toAfterXml = function() {
        return "</" + _o.type + ">";
    };

    // 从数据中恢复图
    this.restore = function(data) {
        var obj = data;
        // if (typeof data === 'string')
        // obj = eval(data);
        _o = aj.extend(true, _o, data);
        _text.attr({text: obj.text.text});

        resize();
    };

    this.getName = function() {
        for (var k in _o.props) {
            if (k == "name") 
                return _o.props[k].value;
        }
    }

    this.getBBox = function() {
        return _bbox;
    };

    this.getId = function() {
        return _id;
    }

    this.remove = function() {
        _rect.remove();
        _text.remove();
        _name.remove();
        _img.remove();
        _bpath.remove();

        for (var k in _bdots) 
            _bdots[k].remove();
    };

    this.text = function() {
        return _text.attr('text');
    };

    this.attr = function(attr) {
        attr && _rect.attr(attr);
    };

    resize(); // 初始化位置
};
