aj = {};

/**
 * 计算矩形中心到 p 的连线与矩形的交叉点
 */
connPoint = (rect, p) => {
    var start = p, end = {
        x: rect.x + rect.width / 2,
        y: rect.y + rect.height / 2
    };

    // 计算正切角度
    var tag = (end.y - start.y) / (end.x - start.x);
    tag = isNaN(tag) ? 0 : tag;

    var rectTag = rect.height / rect.width;
    // 计算箭头位置
    var xFlag = start.y < end.y ? -1 : 1, yFlag = start.x < end.x ? -1  : 1, arrowTop, arrowLeft;

    // 按角度判断箭头位置
    if (Math.abs(tag) > rectTag && xFlag == -1) {// top边
        arrowTop = end.y - rect.height / 2;
        arrowLeft = end.x + xFlag * rect.height / 2 / tag;
    } else if (Math.abs(tag) > rectTag && xFlag == 1) {// bottom边
        arrowTop = end.y + rect.height / 2;
        arrowLeft = end.x + xFlag * rect.height / 2 / tag;
    } else if (Math.abs(tag) < rectTag && yFlag == -1) {// left边
        arrowTop = end.y + yFlag * rect.width / 2 * tag;
        arrowLeft = end.x - rect.width / 2;
    } else if (Math.abs(tag) < rectTag && yFlag == 1) {// right边
        arrowTop = end.y + rect.width / 2 * tag;
        arrowLeft = end.x + rect.width / 2;
    }

    return {
        x: arrowLeft,
        y: arrowTop
    };
}

/**
 * 画箭头，p1 开始位置,p2 结束位置, r前头的边长
 */
arrow = (p1, p2, r) => {
    var atan = Math.atan2(p1.y - p2.y, p2.x - p1.x) * (180 / Math.PI);

    var centerX = p2.x - r * Math.cos(atan * (Math.PI / 180));
    var centerY = p2.y + r * Math.sin(atan * (Math.PI / 180));

    var x2 = centerX + r * Math.cos((atan + 120) * (Math.PI / 180));
    var y2 = centerY - r * Math.sin((atan + 120) * (Math.PI / 180));

    var x3 = centerX + r * Math.cos((atan + 240) * (Math.PI / 180));
    var y3 = centerY - r * Math.sin((atan + 240) * (Math.PI / 180));

    return [p2, {
        x : x2, y : y2
    }, {
        x : x3, y : y3
    }];
}


Raphael.el.addClass = function(className) {
    this.node.setAttribute("class", className);
    return this;
}

// 管理器，单例
aj.svgMgr = (function(){
	var uid = 0; 			// 每个图形对象赋予一个 id
	var selectedComponent; 	// 选中的 SVG 图形对象
	var allSvg = {}; 		// key=uid, value = 图形实例 component
	
	var vue = new Vue({
		data: {
			selectedComponent: null
		},
		watch: {
			selectedComponent(newCop, old) {
				newCop.resizeController && newCop.resizeController.showBox();
				old && old.resizeController && old.resizeController.hideBox();
			}
		}
	});
	
	return {
		PAPER: null,		// Raphael.js 画布
		init(el) {
			this.PAPER = Raphael(el, window.innerWidth, window.innerHeight);
			
			document.addEventListener('click', e => {
				var el = e.target;
				var isSVGAElement = !!el.ownerSVGElement;
				
				if (isSVGAElement && el.id.indexOf('ajSVG') != -1) {
					var component = allSvg[el.id];
					
					if(!component) {
						throw '未登记组件 ' + el.id;
					}
					
					this.setSelectedComponent(component);
				}
			});
		},
		setSelectedComponent(cop) {
			vue.selectedComponent = cop;
		},
		getSelectedComponent() {
			return vue.selectedComponent;
		},
		nextId() {
            return ++uid;
        },
		// 登记组件
		register(component) {
			allSvg[component.svg.node.id] = component;
		}
	};
})();

;(function(){
	/**
	 * 图形基类
	 */
	class SvgBaseComponent {
		constructor(svg) {
			this.svg = svg;
			
			// 登记注册
			this.id = aj.svgMgr.nextId();
			svg.node.id = "ajSVG-" + this.id;
			aj.svgMgr.register(this);
			
			this.updateHandlers = []; // 当大小、位置有变化时候执行的函数列表
		}
		
		addUpdateHandler(fn) {
			this.updateHandlers.push(fn);
		}
	}
	
	SvgBaseComponent.prototype.svg = null; 		// 图形对象
	SvgBaseComponent.prototype.id = 0;			// 每个图形对象赋予一个 id
	
	aj.SvgBaseComponent = SvgBaseComponent;
	
	/**
	 * SVG 的图形对象
	 */
	class RectBox extends SvgBaseComponent {
		constructor(rectCfg, cfg) {
			super(aj.svgMgr.PAPER.rect().attr(rectCfg));
			// cfg && aj.apply(this, cfg);
			
			this.svg.box  = this; // 保留引用
			
			var self = this;
			// this.vbox = {x: rectCfg.x, y: rectCfg.y, width: rectCfg.width,
			// height: rectCfg.height};
			// 数据同步
			var V = new Vue({
				data: {
					vBox : {x: rectCfg.x, y: rectCfg.y, width: rectCfg.width, height: rectCfg.height}
				},
				watch: {
					vBox: {
						handler(val) {
							self.updateHandlers.forEach(fn => fn.call(self, val));
							resize && resize.setDotsPosition();
				        },
				        deep: true
					}
				}
			});
			
			this.vBox = V.vBox;
			
			this.isDrag && this.svg.drag(onDragMove, onDragStart, onDragEnd);
			
			if(this.resize) {
				var resize = new aj.ResizeControl(this);
				resize.renderer(); 
			}
		}
	}
		
	RectBox.prototype.vBox = {}; 		// 虚拟的一个控制框
	RectBox.prototype.isDrag = true; 	// 是否启用鼠标拖放
	RectBox.prototype.resize = true; 	// 是否启用放大缩小
	
	function onDragStart() {
		var x = this.attr('x'), y = this.attr('y');
        this.movingX = x, this.movingY =y;
		this.box.onDragStart && this.box.onDragStart(this.box, x, y);
    }
    
    function onDragMove(x, y) {
		var _x = this.movingX + x, _y = this.movingY + y;
		
		this.attr({
			x: _x, y: _y
		});
		
		var vBox = this.box.vBox;
		vBox.x = _x;
		vBox.y = _y;
		
		this.box.onDragMove && this.box.onDragMove(this.box, _x, _y);
    }

    function onDragEnd() {
		this.box.onDragEnd && this.box.onDragEnd(this.box, this, this.attr('x'), this.attr('y'));
    }
	
	aj.RectBox = RectBox;
	
	/**
	 * 改变大小的边框
	 */
	class ResizeControl {
		constructor(rectComp){
			this.rectComp = rectComp;
			this.vBox = rectComp.vBox; // 虚拟的一个控制框
			rectComp.resizeController = this;
		}
		
		// 该方法只执行一次
		renderer(){
		 	var allDots = {}; // 保存所有控制点的 map
		 	this.allDots = allDots;
		 	
			var self = this;
			this.dotX = this.dotY = 0;
			
		    function bdragStart() {
		    	self.dotX = this.attr('x'),
		    	self.dotY = this.attr('y');
		    }
	
		    var PAPER = aj.svgMgr.PAPER;
		    allDots['t'] = PAPER.rect().addClass('resizeDot').attr({cursor: 's-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 't'), bdragStart); 		// 上
			allDots['lt'] = PAPER.rect().addClass('resizeDot').attr({cursor: 'nw-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 'lt'), bdragStart); 	// 左上
		    allDots['l'] = PAPER.rect().addClass('resizeDot').attr({cursor: 'w-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 'l'),bdragStart); 		// 左
		    allDots['lb'] = PAPER.rect().addClass('resizeDot').attr({cursor: 'sw-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 'lb'), bdragStart); 	// 左下
		    allDots['b'] = PAPER.rect().addClass('resizeDot').attr({cursor: 's-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 'b'), bdragStart); 		// 下
		    allDots['rb'] = PAPER.rect().addClass('resizeDot').attr({cursor: 'se-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 'rb'), bdragStart); 	// 右下
		    allDots['r'] = PAPER.rect().addClass('resizeDot').attr({cursor: 'w-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 'r'), bdragStart); 		// 右
		    allDots['rt'] = PAPER.rect().addClass('resizeDot').attr({cursor: 'ne-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 'rt'), bdragStart); 	// 右上
			
			this.hideBox();
			this.setDotsPosition();
		 	this.resize();			
		}
		
		enableBorder(isBorder) {
			this.isBorder = isBorder;
		}
		
		setDotsPosition() {
			var _bw = 2.5;
			var allDots = this.allDots, vBox = this.vBox;
			
	        allDots['t'].attr({ x: vBox.x + vBox.width / 2 - _bw, y: vBox.y - _bw }); 				// 上
	        allDots['lt'].attr({ x: vBox.x - _bw, y: vBox.y - _bw }); 								// 左上
	        allDots['l'].attr({ x: vBox.x - _bw, y: vBox.y - _bw + vBox.height / 2 }); 				// 左
	        allDots['lb'].attr({ x: vBox.x - _bw, y: vBox.y - _bw + vBox.height }); 				// 左下
	        allDots['b'].attr({ x: vBox.x - _bw + vBox.width / 2, y: vBox.y - _bw + vBox.height }); // 下
	        allDots['rb'].attr({ x: vBox.x - _bw + vBox.width, y: vBox.y - _bw + vBox.height }); 	// 右下
	        allDots['r'].attr({ x: vBox.x - _bw + vBox.width,y: vBox.y - _bw + vBox.height / 2 }); 	// 右
	        allDots['rt'].attr({ x: vBox.x - _bw + vBox.width, y: vBox.y - _bw }); 					// 右上
		}
		
		// 定位各个点的坐标
		resize(){
			// this.setDotsPosition();
			this.rectComp.svg.attr(this.vBox);
	        this.updateBorder();
		}
		
	    showBox() {
	        this.border && this.border.show();  // 显示边框
	
	        for (var i in this.allDots) // 逐个点显示
	            this.allDots[i].show();
	    }
	
	    // 隐藏
	    hideBox() {
	        this.border && this.border.hide();
	
	        for (var i in this.allDots) 
	            this.allDots[i].hide();
	    }
		
		// dy= 移动距离（移动宽度），dy = 移动距离（移动高度）
		dotMove(dx, dy, t) {					
	        var x = this.dotX + dx, y = this.dotY + dy;
	        var vBox = this.vBox;
	        
/*
 * console.log('-----------------') console.log(this.dotY) console.log(dy)
 */
	    	
	        switch (t) {
	        case 't':
	 /*
		 * console.log(vBox.y) console.log(y) console.log(vBox.y - y)
		 */
	            vBox.height += vBox.y - y; vBox.y = y;
	            break;
	        case 'lt':
	            vBox.width += vBox.x - x; vBox.height += vBox.y - y;
	            vBox.x = x; vBox.y = y;
	            break;
	        case 'l':
	            vBox.width += vBox.x - x;
	            vBox.x = x;
	            break;
	        case 'lb':
	            vBox.height = y - vBox.y; vBox.width += vBox.x - x;
	            vBox.x = x;
	            break;
	        case 'b':
	            vBox.height = y - vBox.y;
	            break;
	        case 'rb':
	            vBox.height = y - vBox.y; vBox.width = x - vBox.x;
	            break;
	        case 'r':
	            vBox.width = x - vBox.x;
	            break;
	        case 'rt':
	            vBox.width = x - vBox.x; vBox.height += vBox.y - y;
	            vBox.y = y;
	            break;
	        }
	        
	        this.resize();
	    }
		
		// 边框路径的描边
	    updateBorder() {
			if(!this.isBorder)
				return;
			if(!this.border)
				this.border = aj.svgMgr.PAPER.path('M0 0L1 1'); // 边框
			
	    	var vBox = this.vBox;
	        var str = 'M' + vBox.x + ' ' + vBox.y + 'L' + vBox.x + ' ' + (vBox.y + vBox.height) + 'L' + (vBox.x + vBox.width) +
				  ' ' + (vBox.y + vBox.height) + 'L' + (vBox.x + vBox.width) + ' ' + vBox.y + 'L' + vBox.x + ' ' + vBox.y;
	        
	    	this.border.attr({path: str});
	    }
	}
	
	ResizeControl.prototype.vBox = {};			// 虚拟的一个控制框
	ResizeControl.prototype.isBorder = false;	// 是否显示边框
	ResizeControl.prototype.allDots = {};		// 保存所有控制点的 map
	
	aj.ResizeControl = ResizeControl;
})();