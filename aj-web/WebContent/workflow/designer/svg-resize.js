;(function() {
	/**
	 * 改变大小的边框
	 */
	class ResizeControl {
		constructor(rectComp) {
			this.rectComp = rectComp;
			this.vBox = rectComp.vBox; // 虚拟的一个控制框
			rectComp.resizeController = this;
		}
		
		// 该方法只执行一次
		renderer() {
		 	var allDots = {}; // 保存所有控制点的 map
		 	this.allDots = allDots;
		 	
			var self = this;
			this.dotX = this.dotY = 0;
			
		    var bdragStart= function () {// 一定要用 fn，why？
		    	self.dotX = this.attr('x'), self.dotY = this.attr('y');
		    }
	
		    var PAPER = this.rectComp.PAPER;
		    allDots['t']  = PAPER.rect().addClass('resizeDot').attr({cursor: 's-resize'}).drag((dx, dy)  => this.dotMove(dx, dy, 't'),  bdragStart); // 上
			allDots['lt'] = PAPER.rect().addClass('resizeDot').attr({cursor: 'nw-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 'lt'), bdragStart); // 左上
		    allDots['l']  = PAPER.rect().addClass('resizeDot').attr({cursor: 'w-resize'}).drag((dx, dy)  => this.dotMove(dx, dy, 'l'),  bdragStart); // 左
		    allDots['lb'] = PAPER.rect().addClass('resizeDot').attr({cursor: 'sw-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 'lb'), bdragStart); // 左下
		    allDots['b']  = PAPER.rect().addClass('resizeDot').attr({cursor: 's-resize'}).drag((dx, dy)  => this.dotMove(dx, dy, 'b'),  bdragStart); // 下
		    allDots['rb'] = PAPER.rect().addClass('resizeDot').attr({cursor: 'se-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 'rb'), bdragStart); // 右下
		    allDots['r']  = PAPER.rect().addClass('resizeDot').attr({cursor: 'w-resize'}).drag((dx, dy)  => this.dotMove(dx, dy, 'r'),  bdragStart); // 右
		    allDots['rt'] = PAPER.rect().addClass('resizeDot').attr({cursor: 'ne-resize'}).drag((dx, dy) => this.dotMove(dx, dy, 'rt'), bdragStart); // 右上
			
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
			var x = vBox.x - this.margin, y = vBox.y - this.margin, width = vBox.width + this.margin * 2, height = vBox.height + this.margin * 2;
			
	        allDots['t'].attr({  x: x + width/2 - _bw, 	y: y - _bw }); 				// 上
	        allDots['lt'].attr({ x: x - _bw, 			y: y - _bw }); 				// 左上
	        allDots['l'].attr({  x: x - _bw, 		 	y: y - _bw + height/2 }); 	// 左
	        allDots['lb'].attr({ x: x - _bw, 		 	y: y - _bw + height }); 	// 左下
	        allDots['b'].attr({  x: x - _bw + width/2, 	y: y - _bw + height }); 	// 下
	        allDots['rb'].attr({ x: x - _bw + width, 	y: y - _bw + height }); 	// 右下
	        allDots['r'].attr({  x: x - _bw + width, 	y: y - _bw + height/2 }); 	// 右
	        allDots['rt'].attr({ x: x - _bw + width, 	y: y - _bw }); 				// 右上
		}
		
		// 定位各个点的坐标
		resize() {
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
	        
			// console.log('-----------------') console.log(this.dotY) console.log(dy)
	        switch (t) {
	        case 't':
			 	// console.log(vBox.y) console.log(y) console.log(vBox.y - y)
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
				this.border = this.rectComp.PAPER.path('M0 0L1 1').hide(); // 边框
			
	    	var vBox = this.vBox;
			var x = vBox.x - this.margin, y = vBox.y - this.margin, width = vBox.width + this.margin * 2, height = vBox.height + this.margin * 2;
	        var str = 'M' + x + ' ' + y + 'L' + x + ' ' + (y + height) + 'L' + (x + width) +
				  ' ' + (y + height) + 'L' + (x + width) + ' ' + y + 'L' + x + ' ' + y;
	        
	    	this.border.attr({path: str});
	    }
	}
	
	ResizeControl.prototype.vBox = {};			// 虚拟的一个控制框
	ResizeControl.prototype.isBorder = true;	// 是否显示边框
	ResizeControl.prototype.allDots = {};		// 保存所有控制点的 map
	ResizeControl.prototype.margin = 10;		// 外边距，0=重合
	
	aj.svg.ResizeControl = ResizeControl;
})();