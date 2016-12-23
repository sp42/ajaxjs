/**
 * @class $$.scrollViewer
 * 拖到事件
 */
;(function() {
	/**
	* 观察者类，用于推送消息或者广播事件。addEventListener 方法为对象新增消息类型，fire 方法进行消息推送。
	* 使用该类后，增加一个 msgs 属性，结构如下：
	* msg = Object : [ 
	* 	{
	* 	   name : String,
	*      bodies : Object [
	* 			{
	* 				scope	: Mixed,
	* 				fn		: Function,
	* 				args	: Mixed []
	* 			}
	* 	   ]
	*  }
	* ]
	*/
	var observe = function (){
		var msg = {
	        name : '',
			bodies : [{
			    scope : null,
				fn : null,
				agrs : []
			}], /* ... */
			isCustomDomEvent : false
	    }
	    function observe(){
	    	// I'm Event
	    	this.event = true;
	    }
	    this.init = observe;

	    /**
	    * @param {String}		msgName		消息名称
	    * @param {Function}	    msgHandler	消息行为
	    * @param {*}		    scope		函数作用域
	    * @param {*|Array}	    args		消息行为的前期参数
	    * @return {Boolean}
	    */
	    this.addEventListener = this.on = function (msgName, msgHandler, scope, args) {
	        if (!this.msgs) {
	            this.msgs = [];// 全部消息之容器
	        }

	        check_msgBody_Queen(this.msgs);
	        var msgObj;

	        var hasToken = false;
	        for (var i = 0, j = this.msgs.length; i < j; i++) {
	            msgObj = this.msgs[i];
	            if (msgObj.name == msgName) {
	                hasToken = true;
	                break; // 找到已存在的消息队列
	            }
	        }
	        if (hasToken == false) {
	            // 消息结构体 @todo remove create()
	            msgObj = Object.create(msg); // 新建消息对象
	            msgObj.name = msgName;
	            msgObj.bodies = [];         // you must new it!
	            if (msgName)
	                msgObj.isCustomDomEvent = this.customDomEventNames
	                                         ? (indexOf(msgName, this.customDomEventNames) != -1)
	                                         : false;
	        }

	        check_msgBody_Queen(msgObj.bodies);

	        if (!(typeof args instanceof Array)) { // 前期输入的参数
	            if (args == undefined) {
	                args = [];
	            } else {
	                args = [args];
	            }
	        }

	        msgObj.bodies.push({        // 压入消息队列
	            fn: msgHandler,
				scope: scope || this,  // 保存函数作用域
				args: args
	        });

	        if (hasToken == false) {
	            this.msgs.push(msgObj);
	        }

	        return true;
	    }

	    /**
	    * 检查消息体队列其类型
	    * @param {Array} arr
	    */
	    function check_msgBody_Queen(arr) {
	        if (!arr || !arr instanceof Array) {
	            throw new TypeError();
	        }
	    }

	    function indexOf(item, arr) {
	        for (var i = 0, j = arr.length; i < j; i++) {
	            if (item == arr[i]) {
	                return i;
	            }
	        }

	        return -1;
	    }

	    /**
	    * 根据 msgName，查找指定的消息并讲其分发。
	    * @param {String} msgName
	    * @param {Array/Mixed} args 可选
	    * @param {Mixed} scope 可选
	    */
	    this.fire = function (msgName, args, scope) {
	    	var foundMsgObj = false;
	        for (var msgObj, i = 0, j = this.msgs.length; i < j; i++) {
	            msgObj = this.msgs[i];
	            if (msgObj.name == msgName) {
	            	foundMsgObj = true;
	                break;
	            }
	        }

	        if (foundMsgObj === false) {
	        	debugger;
	            throw '没有此消息类型' + msgName;
	        }


	        var _fn, _body, _args;
	        for (var body, i = 0, j = msgObj.bodies.length; i < j; i++) {
	            body = msgObj.bodies[i];

	            _fn = body.fn;
	            _scope = scope || body.scope;
	            if(args == undefined){
	            	_args = body.args;
	            }else{
	            	// args 应该在数组前面，否则数组顺序会颠倒
	            	if (args instanceof Array){
	            		_args = args.concat(body.args);
	            	}else{
	            		_args = [args].concat(body.args);
	            	}
	            }
	            
	            _fn.apply(_scope, _args);
	        }
	    }
	    // event-->msg
	    this.customDomEvent = function (el, eventType, msgName) {
	        if (!this.customDomEventNames) {
	            this.customDomEventNames = [];
	        }
	        this.customDomEventNames.push(msgName);
	        el.addEventListener(eventType, (function (e) {
	            e = e || window.event;
	            this.instance.fire(this.msgName, e, this.instance);
	        }).bind({
	            instance: this,
	            msgName: msgName
	        }));
	    }
	};
		
			
	/**
	 * 触控类
	 */
			
	// 是否可以支持 触控 事件
	var canTouch = ("createTouch" in document) || ('ontouchstart' in window);

	var beforDragEvent = canTouch ? "ontouchstart" : "onmousedown";
	var onDragingEvent = canTouch ? "ontouchmove"  : "onmousemove";
	var afterDragEvent = canTouch ? "ontouchend"   : "onmouseup";
					

	/**
	 * 构造函数
	 * @param {[type]} cfg [description]
	 */
	function Touch(cfg){
		observe.call(this);
		
		if(!this.cfg){
			this.cfg = {};
		}

		for(var i in cfg){
			this.cfg[i] = cfg[i];
		}

		this.on('beforeMove', function(){
			// console.log('开始拖动');
		});
		this.on('moving', function(){
			// console.log('开始拖动');
		});
		this.on('afterMove', function(){
			// console.log('开始拖动');
		});

		if(!this.cfg.moverEl){
			throw this.toString() + '拖动的目标元素未准备好！';
		}
		this.cfg.moverEl[beforDragEvent] = initMove.bind(this);
		
		if(window.navigator.isAndroid_4){
			this.touchendTimerId;
		}
	}

	/**
	 * 初始化 touch 事件，清空上次记录（direction、distance），并记录第一击的事件信息。
	 * 绑定 move、end 事件。
	 * @param  {EventObject} e [description]
	 * @return {[type]}   [description]
	 */
	function initMove(e){
		e = e || window.event; // if that is W3C Event Object, take the first one.

		// 上次移动的参数
		this.lastData = {
			direction : null,
			distance  : null,
			/*
			 * clientX和clientY表示的位置是相对浏览器窗口的，而不是对文档的，
			 * 因此当你在滚动页面之后仍然在窗口中的同一位置上单击时，所得到的坐标的值是相同的。
			 */
			x 		  : canTouch ? e.touches.item(0).pageX : e.clientX,
			y         : canTouch ? e.touches.item(0).pageY : e.clientY
		};

		var cfg = this.cfg;

		// 每次拖动都会分别登记一次 onmousemove 事件和 onmouseup 事件。
		// 拖动完，又会自动撤销上述事件的。
		// 登记到 onmousedown 事件中，不撤销，而 onmousemove / onmouseup 则会撤销
		cfg.moverEl[onDragingEvent] = moving.bind(this);	// 拖动时都会触发该事件
		cfg.moverEl[afterDragEvent] = afterMove.bind(this); // 松开按键时，要撤销 onmousemove 和 onmouseup 事件。

		this.fire('beforeMove', e);

		// 这里控制是否控制上报事件，
		// return false; // android 这里允许 事件上报的话很慢
	}

	/**
	 * 记录 touch 信息，信息包括坐标、角度、方向、距离。
	 * 把这些信息记录在 this.lastData 对象中。
	 * @param  {EventObject} e [description]
	 * @return {Array}   [description]
	 */
	function moving(e){
		e = e || window.event;

		var lastData = this.lastData;

		// 当前坐标 注意相反的
		var coordinate = {
			x : canTouch ? e.touches.item(0).pageX : e.clientX,
			y : canTouch ? e.touches.item(0).pageY : e.clientY
		};

		var lastXY = {
			x : lastData.x,
			y : lastData.y
		};

		// 角度:number
		var angle = getAngle(coordinate, lastXY);
		// 方向:string
		var direction = getDirectionFromAngle(angle);
		// 距离:number
		var disX = Math.abs(coordinate.x - lastData.x), disY = Math.abs(coordinate.y - lastData.y);

		var args = [e, direction, coordinate.x, lastData.x, disX, coordinate.y, disY]; // 供 after(fn) 调用的参数列表 

		fixTouchEndNotFire.apply(this, args);

		this.fire('moving', args);

		// 记录参数，放在最后
		this.lastData.direction = direction;
		this.lastData.disX = disX;
		this.lastData.disY = disY;
		this.lastData.x = coordinate.x;
		this.lastData.y = coordinate.y;
		
		return args;
	}

	/**
	 * 获取 move 事件最后一次的信息，送入到用户提供的事件处理器中。
	 * @param  {EventObject} e [description]
	 * @return {Array}   [description]
	 */
	function afterMove(e){
		e = e || window.event;
		var lastData = this.lastData;
		// 当前坐标就是上次的坐标、最后一次的坐标。
		this.fire('afterMove', [e, lastData.direction, lastData.x, lastData.disX, lastData.y, lastData.disY]);
		cleanMove.call(this, e);
	}

	/**
	 * 撤销 moving、moveend 事件
	 * @param  {EventObject} e [description]
	 * @return {[type]}   [description]
	 */
	function cleanMove(e){
		var moverEl = this.cfg.moverEl;
		moverEl[onDragingEvent] = moverEl[afterDragEvent] = null;
		// console.log('clean move finished!');
	}

	/**
	 * 计算两点之间的角度。
	 * calculate the angle between two points
	 * @param  {Object}  pos1 { x: int, y: int }
	 * @param  {Object}  pos2 { x: int, y: int }
	 * @return {Number}
	 */
	function getAngle(pos1, pos2){
	    return Math.atan2(pos2.y - pos1.y, pos2.x - pos1.x) * 180 / Math.PI;
	}

	/**
	 * 根据角度计算出方向。
	 * angle to direction define
	 * @param  {float}    angle
	 * @return {string}   direction
	 */
	function getDirectionFromAngle(angle){
	    var directions = {
	        down: angle >= 45 && angle < 135, //90
	        left: angle >= 135 || angle <= -135, //180
	        up: angle < -45 && angle > -135, //270
	        right: angle >= -45 && angle <= 45 //0
	    };
	    var direction, key;
	    for(key in directions){
	        if(directions[key] === true){
	            direction = key;
	            break;
	        }
	    }
	    return direction;
	}

	function fixTouchEndNotFire(e, direction, disX, disY){
		/*
			@bug touchend事件丢失，
			或者到touchmove事件之后就终止掉。这是一个非常严重的bug。
			使用touch事件时，在android 4.0 上面的浏览器手指在a元素上做滑动操作，然后手指离开，
			结果不会触发 touchend事件，同样的操作在 android 2.x / ios上会触发 touchend。
			解决办法：1、e.preventDefault(); 
			2、兼容的解决办法是在 touchmove 时判断手势趋势大于预设值时（大于预设值证明有 move的动作趋势），停止默认的操作e.preventDefault()
			http://www.mansonchor.com/blog/blog_detail_68.html
			http://code.google.com/p/android/issues/detail?id=4549
			http://www.uedspace.com/blog/576.html
		*/
		if(window.navigator.isAndroid_4){
			// window.clearTimeout(this.touchendTimerId);
			e.preventDefault();
			// alert(99999999996);
			// this.touchendTimerId = window.setTimeout(function(){
			// 	cleanMove.call(this);
			// 	this.afterDrag && obj.afterDrag(e, direction, disX, disY);
			// }, 20);
		} 	
	}
				    
	/**
	 * @class $$.scrollViewer
	 * 拖到事件
	 */
	function scrollViewer(moveStep){
		moveStep = moveStep || 50;

		this.init = function(el){
			if(!el){
				throw '!el';
			}
			this.container      = el;
			this.moveRightBtn   = this.container.querySelector('.rightIcon');
			this.moveLeftBtn    = this.container.querySelector('.leftIcon');
			this.mover          = this.container.querySelector('.x_carouselMover');

			/**
			 * @type {Number} 可见宽度，通常是屏幕宽度
			 */
			this.viewEl_width   = this.mover.parentNode.clientWidth;
			/**
			 * @type {Number} 容器宽度
			 */
			this.containerWidth = this.mover.scrollWidth;

			if(!this.viewEl_width)
				console.warn('获取 DOM 可见宽度失败！');
			if(!this.containerWidth)
				console.warn('获取 DOM 容器宽度失败！');

			// 左边的
			if(this.moveLeftBtn)this.moveLeftBtn.onClick = (onLeftBtnClick.bind(this));
			// 右边的
			if(this.moveRightBtn)this.moveRightBtn.onClick = (onRightBtnClick.bind(this));

			//Ext.fly(this.moveLeftBtn).hide();

//			selectItem.call(this);
			var touch = new Touch({ 
	            moverEl : this.mover
	        });
	        touch.on('moving', isReachEdge.bind(this));
		}

		function isReachEdge(e, direction, x, lastX,  moved){
			var el = this.mover;          // 正在移动着的元素
			var moved = parseInt(el.style.left) || 0;

			// debugger;
			// 从左向右移动
			if(direction == 'left' && moved >= 0){
				// alert('从左向右移动, 到达边缘')
				return false;
			// 从右向左移动
			}else if(direction == 'right'){
				// if(-(moved) > (this.containerWidth - this.viewEl_width)){
				// 	return false;
				// }
				// if(moved < -(this.containerWidth - this.viewEl_width)){
				// 	return false;
				// }
				if(moved < (this.viewEl_width - this.containerWidth)){
					return false;
				}
			}
			var leftValue = moved + (x - lastX);
	    	el.style.left = leftValue + "px";
		}

		function onRightBtnClick(e){
			var moved = Math.abs(parseInt(this.mover.style.left)) || 0; // 获取绝对值
			var move = moved + moveStep;

			if(move > ( this.containerWidth - this.viewEl_width + moveStep)){
				console.log('到达边缘');
			}else{
				this.scrollTo(-move);
			}
		}

		function onLeftBtnClick(e){
			var moved = parseInt(this.mover.style.left) || 0;
			if(moved >= 0)
				return false;

			this.scrollTo(moved + moveStep);
		}

		/**
		 * 选择当前项，有居左、居中、居右三种对齐方式。
		 */
		this.selectItemLeft = function (){
			var current = this.container.querySelector('.y_pressed');
			if(current){
				this.scrollTo(-current.offsetLeft + this.moveLeftBtn.clientWidth);
			}
		}

		this.selectItem_Center = function (){
			var current	= this.container.querySelector('.y_pressed');
			var center  = (document.documentElement.clientWidth / 2 >> 0);

			if(current){
				this.scrollTo(center - current.offsetLeft);
			}else{
				console.log('找不到高亮元素！该方法执行无效！');
			}
		}

		this.selectItem_Right = function(){
			var current		= this.container.querySelector('.y_pressed');
			var screenWidth = document.documentElement.clientWidth;
			var offsetLeft	= current.offsetLeft;
			var itemWidth	= current.clientWidth;

			if((offsetLeft + itemWidth) > screenWidth){
				// item 宽度＋按钮宽度
				this.scrollTo(-(offsetLeft - screenWidth + itemWidth + this.moveRightBtn.clientWidth));
			}
		}

		/**
		 * 设置滚动条 left 距离
		 * @param  {Number} left [description]
		 */
//		this.scrollTo = function(left){
//			console.log(this.mover);
//			left = parseInt(left);
//			left = left + 'px';
//			this.mover.style.left = left;
//		}
	}
	
	window.bf_scrollViewer = scrollViewer;
})();