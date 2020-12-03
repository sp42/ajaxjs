;(function() {
	var Dot = aj.svg.Dot, DotList = aj.svg.DotList;
	var center = aj.svg.util.center, connPoint = aj.svg.util.connPoint, isLine = aj.svg.util.isLine, arrow = aj.svg.util.arrow;

	/**
	 * 路径
	 * 
	 * @class
	 */
    aj.svg.Path = function (from, to, text) {
    	var _path = PAPER.path().addClass('path'), _arrow = PAPER.path().addClass('arrow');
    	this.id = null;
        this.svg = _path;
        
        // 继承
        aj.svg.DotList.call(this, from, to);
        aj.apply(this, aj.svg.DotList.prototype);
        
        aj.svg.Mgr.register(this);
        this.hide();
    	
        // $([_path.node, _arrow.node]).bind("click", () => {
        // console.log('选中路径 path 或 箭头');
        // if (!ajflow.config.editable)
        // return;
        //	         
        // snaker(_r).trigger("click", this);
        // ajflow.DESIGNER.currentNode = this;
        //
        // return false;
        // });
        
        this.setText = text => {
        	if (this.textObj) 
        		this.textObj.text = text;
        	else 
        		this.textObj = aj.svg.createTextNode(text, 0, 0);
        } 
        
        text && this.setText(text);
        
        // 处理点击事件，线或矩形
        var clickHandler = function (e, src) {
                if (!ajflow.config.editable)
                    return;

                if (src && src.getId() == _id) {
                    this.show();
                    // snaker(_r).trigger("showprops", [_o.props, _this])
                } else
                    this.hide()

                // switch (snaker(_r).data("mod")) {
                // case "pointer":
                // break;
                // case "path":
                // break
                // }
            }
            // _r.bind("click", clickHandler);

        // 矩形移动时处理器
        this.rectResizeHandler = () => {
        	var o, dot;
        	
//            if (from && from.node.id == event.target.id) {
	        	dot = this.fromDot.right().right();
	            if (dot.type == Dot.TO)
	                o = { x: to.getBBox().x + to.getBBox().width / 2, y: to.getBBox().y + to.getBBox().height / 2 };
	            else
	                o = dot.pos();
	
	            var r = connPoint(from.getBBox(), o);
	            this.fromDot.moveTo(r.x, r.y);
	
	            this.refreshPath();
//            }

//            if (to && to.node.id == event.target.id) {
	            dot = this.toDot.left().left();
	            if (dot.type == Dot.FROM)
	                o = { x: from.getBBox().x + from.getBBox().width / 2, y: from.getBBox().y + from.getBBox().height / 2 };
	            else
	                o = dot.pos();
	
	            var r = connPoint(to.getBBox(), o);
	            this.toDot.moveTo(r.x, r.y);
	
	            this.refreshPath();
//            }
        }
        
    	var moveFn = this.rectResizeHandler.bind(this);
    	from.vue.addUpdateHandler(moveFn);
    	to.vue.addUpdateHandler(moveFn);

        // 函数-------------------------------------------------
        this.from = function () {
            return from;
        }

        this.to = function () {
            return to;
        }

        this.remove = () => {
        	aj.svg.DotList.prototype.remove.call(this);
            _path.remove();
            _arrow.remove();
            
            if (this.textObj) {
            	this.textObj.$el.parentNode.removeChild(this.textObj.$el);
            	this.textObj = null;
            }
            
            from.vue.removeUpdateHandler(moveFn); // 卸载事件
            to.vue.removeUpdateHandler(moveFn);
        };

        // 刷新路径
        this.refreshPath = () => {
            var r = this.toPathString(), mid = this.midDot().pos();
            
	        _path.attr({ path: r[0] });
	        _arrow.attr({ path: r[1] });
	        
	        if(this.textObj) {
	        	var textPos = this.textObj.getXY();// 定位文字
	        	this.textObj.setXY( mid.x + 30, mid.y + 10);
	        }
	    }
        
        this.refreshPath();

        this.getId = function () {
            return _id;
        };

        this.text = function () {
            return _text.attr("text")
        };

        this.name = function () {
            return _o.props["name"].value;
        }

        this.attr = function (o) {
            o && o.path && _path.attr(o.path);
            o && o.arrow && _arrow.attr(o.arrow);
        }
    };

})();