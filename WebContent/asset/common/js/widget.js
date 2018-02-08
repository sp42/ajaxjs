// Small widgets that you can use them freely
// Bought to u by RongZhen 2017, Canton

if (!this.ajaxjs)
    throw '依赖 dom.js';

// --------------------------------------------------------
// Dialog
// -------------------------------------------------------- 
ajaxjs.Popup = function(cfg) {
	cfg = cfg || {};

	cfg.el 	 = cfg.el   || '.msgbox';
	cfg.tpl  = cfg.tpl  || '.msgboxTpl';
	cfg.mask = cfg.mask || '.msgbox_mask';

	this.show = function() {
	    // 读取模板
        var holder = document.createElement('div');
        holder.innerHTML = qs(cfg.tpl).value;
        document.body.appendChild(holder);

        var msgbox = qs(cfg.el);

        // 获取页面内容高度赋予 mask
        qs(cfg.mask).style.height = document.body.scrollHeight + 'px';
        // 计算居中
        msgbox.style.left = (window.innerWidth  / 2 - msgbox.clientWidth  / 2) + 'px';
        msgbox.style.top  = (window.innerHeight / 2 - msgbox.clientHeight / 1) + 'px';

    	// 自定义按钮事件
        if (cfg.hideYES_NO) {
            var btn = msgbox.qs('.btn');
            btn.removeChild(msgbox.qs('.btn .yesAction'));
            btn.removeChild(msgbox.qs('.btn .noAction'));
        }
        if (cfg.hideClose) {
            var btn = msgbox.qs('.btn');
            btn.removeChild(msgbox.qs('.btn .closeAction'));
        }
        
        var closeAction = this.close.bind(this);
        msgbox.every_child('.closeAction', function(closeBtn) {
                closeBtn.onclick = closeAction;
            });

        // 键盘事件
        document.onkeydown = function (e) {
            e = e || event;
            var keycode = e.which || e.keyCode;

            switch (keycode) {
                case 13: // enter
                    // 如果 form 里有 action，按下回车自动提交
                    /*
                     * var formEl = this.el.$('form'); if(formEl && formEl.action){
                     * formEl.submit(); }
                     */
                    break;
                case 339: // exit
                case 340: // back
                case 27:
                    closeAction();
            }
        }

        if (cfg.innerText)
            msgbox.qs('.inner').innerHTML = cfg.innerText;
        if (cfg.title)
            msgbox.qs('h1').innerHTML = cfg.title;
        if (cfg.closeAsConfirm)
            msgbox.qs('.btn .closeAction').innerHTML = '确定';
        if (cfg.yesHandler)
            msgbox.qs('.btn .yesAction').onclick = cfg.yesHandler;
        if (cfg.noHandler)
            msgbox.qs('.btn .noAction').onclick = cfg.noHandler;

        cfg.onShow && cfg.onShow();
        
        initDD(msgbox);
    }

     // close popup
	this.close = function() {
        if (cfg.beforeClose && cfg.beforeClose() === false) 
                return; // 退出不执行下一步

        qs(cfg.el).die();

        var m = qs('.msgbox_mask');
        m && m.die();

        document.onkeydown = null;

        cfg.afterClose && cfg.afterClose();
    }

    function initDD(msgbox) {
        document.onselectstart = function(e) {
            return false;
        }
        
        // 代码就把Dialog的left和top设为了鼠标当前位置，可是用户在拖动的时候不会刻意去点Dialog的左上角，这样就跳了，soga！改进一下
        // http://www.cnblogs.com/dolphinX/p/3290520.html
        // http://www.cnblogs.com/dolphinX/p/3293455.html
        msgbox.onmousedown = function(e) {
            if (e.target.tagName != 'H1')
                return;
            e.preventDefault();

            var dd = Object.create(ajaxjs.dd), dialogStyled = msgbox.style;

            // box 左端 到 鼠标 x 坐标之间的距离，应由 onmousedown 那一刻，记录 距离，不要放在 onMoving 里，不然会 一跳 一跳
            // another way
            var boxLeft = msgbox.getBoundingClientRect().left, diff = e.screenX - boxLeft;

            dd.onMoving = function(e, data) {
                dialogStyled.left = (e.screenX - diff) + 'px';
                //dialogStyled.left = (data.x - dialog.clientWidth / 2) + 'px';
                dialogStyled.top = data.y + 'px';
            }

            dd.el = parent.document; // 可拖放的范围，documement 表示整张桌布

            dd.init();
        }
    }
}


//--------------------------------------------------------
// 拖放/触控 Drag&Drop
// 例子 http://i.ifeng.com/ent/ylch/news?ch=ifengweb_2014&aid=91654101&mid=5e7Mzq&vt=5
//-------------------------------------------------------- 
;(function() {
	// 是否可以支持 触控 事件
	var canTouch = ("createTouch" in document) || ('ontouchstart' in window),
		beforDragEvent = canTouch ? "ontouchstart" : "onmousedown", 
		onDragingEvent = canTouch ? "ontouchmove"  : "onmousemove",
		afterDragEvent = canTouch ? "ontouchend"   : "onmouseup";
	
	ajaxjs.dd = {
		init : function(){
			if(!this.el)alert('el 元素未准备好！');
			
			this.el[beforDragEvent] = initMove.bind(this);

			// if(window.navigator.isAndroid_4){
			// 	this.touchendTimerId;
			// }
		}
	};

	ajaxjs.dd.getAngle = getAngle;
	ajaxjs.dd.getDirectionFromAngle = getDirectionFromAngle;
	
	/**
	 * 初始化 touch 事件，清空上次记录（direction、distance），并记录第一击的事件信息。
	 * 绑定 move、end 事件。
	 * @param  {EventObject} e [description]
	 * @return {[type]}   [description]
	 */
	function initMove(e) {
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

		// 每次拖动都会分别登记一次 onmousemove 事件和 onmouseup 事件。
		// 拖动完，又会自动撤销上述事件的。
		// 登记到 onmousedown 事件中，不撤销，而 onmousemove / onmouseup 则会撤销
		this.el[onDragingEvent] = moving.bind(this);	// 拖动时都会触发该事件
		this.el[afterDragEvent] = afterMove.bind(this); // 松开按键时，要撤销 onmousemove 和 onmouseup 事件。

		this.onBeforeMove && this.onBeforeMove(e);

		// 这里控制是否控制上报事件，
		// return false; // android 这里允许 事件上报的话很慢
	}

	/**
	 * 记录 touch 信息，信息包括坐标、角度、方向、距离。
	 * 把这些信息记录在 this.lastData 对象中。
	 * @param  {EventObject} e [description]
	 * @return {Array}   [description]
	 */
	function moving(e) {
		e = e || window.event;
		var lastData = this.lastData;

		// 当前坐标 注意相反的
		var coordinate = {
			x : canTouch ? e.touches.item(0).pageX : e.clientX, 
			y : canTouch ? e.touches.item(0).pageY : e.clientY
		};

		var lastXY = { x : lastData.x, y : lastData.y };

		// 角度:number
		var angle = getAngle(coordinate, lastXY);
		// 方向:string
		var direction = getDirectionFromAngle(angle);
		// 距离:number
		var disX = Math.abs(coordinate.x - lastData.x), disY = Math.abs(coordinate.y - lastData.y);

		var args = [e, direction, coordinate.x, lastData.x, disX, coordinate.y, disY]; // 供 after(fn) 调用的参数列表

		// fixTouchEndNotFire.apply(this, args);
		this.onMoving && this.onMoving(e, coordinate);
		// 记录参数，放在最后
		this.lastData.direction = direction;
		this.lastData.disX = disX, lastData.disY = disY;
		this.lastData.x = coordinate.x, this.lastData.y = coordinate.y;
		
		return args;
	}

	/**
	 * 获取 move 事件最后一次的信息，送入到用户提供的事件处理器中。
	 * @param  {EventObject} e [description]
	 * @return {Array}   [description]
	 */
	function afterMove(e) {
		e = e || window.event;
		// 当前坐标就是上次的坐标、最后一次的坐标。
		this.onAfterMove && this.onAfterMove(e, this.lastData);
		this.el[onDragingEvent] = this.el[afterDragEvent] = null; // 撤销 moving、moveend 事件
		this.el[beforDragEvent] = null;
		
		if(this.cancelStartEvent) {
		}
	}

    /**
     * 计算两点之间的角度。
     * calculate the angle between two points
     * @param  {Object}  pos1 { x: int, y: int }
     * @param  {Object}  pos2 { x: int, y: int }
     * @return {Number}
     */
	function getAngle(pos1, pos2) {
        return Math.atan2(pos2.y - pos1.y, pos2.x - pos1.x) * 180 / Math.PI;
    }

    /**
     * 根据角度计算出方向。
     * angle to direction define
     * @param  {float}    angle
     * @return {string}   direction
     */
	function getDirectionFromAngle(angle) {
        var directions = {
            down: angle >= 45 && angle < 135, //90
            left: angle >= 135 || angle <= -135, //180
            up: angle < -45 && angle > -135, //270
            right: angle >= -45 && angle <= 45 //0
        };
        
        var direction, key;
        
        for(key in directions) {
            if(directions[key] === true) {
                direction = key;
                break;
            }
        }
        
        return direction;
    }

	/*
		@bug touchend 事件丢失，或者到 touchmove 事件之后就终止掉。这是一个非常严重的bug。
		使用touch事件时，在 android 4.0 上面的浏览器手指在a元素上做滑动操作，然后手指离开，
		结果不会触发 touchend事件，同样的操作在 android 2.x / ios上会触发 touchend。
		解决办法：
		1、e.preventDefault(); 
		2、兼容的解决办法是在 touchmove 时判断手势趋势大于预设值时（大于预设值证明有 move的动作趋势），停止默认的操作e.preventDefault()
		https://issuetracker.google.com/issues/36910235
		https://github.com/TNT-RoX/android-swipe-shim/blob/master/_ezswipe.js
	 */
    function fixTouchEndNotFire(e, direction, disX, disY) {
    	if(window.navigator.isAndroid_4) {
			e.preventDefault();
		} 	
    }
})();

/*
 * --------------------------------------------------------
 * 折叠菜单
 * --------------------------------------------------------
 */
;(function() {
    ajaxjs.AccordionMenu = function(ul) {
        this.ul = ul;
        this.children = ul.children;
        ul.addEventListener('click', onClk.bind(this));
        ul.addEventListener('click', highlightSubItem);
    }

    function onClk(e) {
        var _btn = e.target;
        if (_btn.tagName == 'H3' && _btn.parentNode.tagName == 'LI') {
            _btn = _btn.parentNode;

            for (var btn, i = 0, j = this.children.length; i < j; i++) {
                btn = this.children[i];
                var ul = btn.querySelector('ul');

                if (btn == _btn) {
                    if (btn.className.indexOf('pressed') != -1) {
                        btn.classList.remove('pressed'); // 再次点击，隐藏！
                        if (ul)
                            ul.style.height = '0px';
                    } else {
                        if (ul)
                            ul.style.height = ul.scrollHeight + 'px';
                        btn.classList.add('pressed');
                    }
                } else {
                    btn.classList.remove('pressed');
                    if (ul)
                        ul.style.height = '0px';
                }
            }
        } else {
            return;
        }
    }

    // 内部子菜单的高亮
    function highlightSubItem(e) {
        var li, el = e.target;
        if (el.tagName == 'A' && el.getAttribute('target')) {
            li = el.parentNode;
            li.parentNode.eachChild('li', function(_el) {
                if (_el == li)
                    _el.classList.add('selected');
                else
                    _el.classList.remove('selected');
            });
        }
    };
})();


/*
 * --------------------------------------------------------
 * 简单选项卡控件
 * --------------------------------------------------------
 */
;(function() {
	ajaxjs.SimpleTab = function(container) {
		this.el = container;
		var ul = container.querySelector('ul');
		this.buttons = ul.children, // tab候选栏strip
		this.tabs = container.querySelector('.content').children;
		ul.onclick = onTabChooserPressHandler.bind(this);
	}
	
	/**
	 * 跳到指定的 tab，仿佛好象点击那样
	 * @param {int} index
	 */
	ajaxjs.SimpleTab.prototype.jump = function(index) {
		var btn = this.buttons[index];
		onTabChooserPressHandler.call(this, {
			target : btn,
			currentTarget : this.el.querySelector('ul')
		});
	}
	
	var onPressed_ClassName = 'selected';
	// 登记的单击事件是整个 tan panel
	function onTabChooserPressHandler(e) {
		// 搜索 el 下的 li 元素，到容器为止
		var el = e.target;
		if (el.tagName != 'LI') return;

		var buttons = e.currentTarget.children, // tab候选栏strip
			tabs = e.currentTarget.parentNode.querySelector('.content').children;
		!buttons.length && console.log('该控件未发现任何 strip。');
		
		for (var nextIndex = 0, j = buttons.length; nextIndex < j; nextIndex++)
			if (buttons[nextIndex] == el)
				break; // 获取 nextIndex
				
		// 拦截事件
		if (
			this.beforeSwitch && typeof this.beforeSwitch == 'function' && 
			this.beforeSwitch(this.currentIndex, nextIndex, j, buttons[nextIndex], tabs[nextIndex]) === false
		) {
			return;
		}
				
		// 查找与 index 相等的 item 设置其高亮，否则移除样式。
		var btn, showTab;
		for (var i = 0, j = buttons.length; i < j; i++) {
			btn = buttons[i], showTab = tabs[i];
			// debugger;
			if (nextIndex == i && btn.className.indexOf(onPressed_ClassName) == -1) { // 找到目标项
				btn.classList.add(onPressed_ClassName);
				showTab.classList.add(onPressed_ClassName);
				this.currentIndex = i; // 保存当前游标

				if (this.afterSwitch && typeof this.afterSwitch == 'function')
					this.afterSwitch(i, btn, showTab);

				// 是否已经渲染
				var isRendered_marked = ~showTab.className.indexOf('rendered');
				if (!isRendered_marked)
					showTab.classList.add('rendered');
				if (!isRendered_marked && this.afterRender && typeof this.afterRender == 'function')
					this.afterRender(i, btn, showTab);
			} else if (btn == el && btn.className.indexOf(onPressed_ClassName) != -1) {
				// 已在当前项
			} else if (btn.className.indexOf(onPressed_ClassName) != -1) {
				btn.classList.remove(onPressed_ClassName);
				showTab.classList.remove(onPressed_ClassName);
			}
		}
	}
})();


//--------------------------------------------------------
//拖放/触控 Drag&Drop
//例子 http://i.ifeng.com/ent/ylch/news?ch=ifengweb_2014&aid=91654101&mid=5e7Mzq&vt=5
//--------------------------------------------------------  
ajaxjs.throttle = {
	event : {},
	handler : [],
	init : function(fn, eventType) {
		eventType = eventType || 'resize'; // resize|scroll
		
		this.handler.push(fn);
		
		if(!this.event[eventType])	
			this.event[eventType] = true;
		
		(function() {  
			var me = arguments.callee;  

			window.addEventListener(eventType, function() {  
				window.removeEventListener(eventType, arguments.callee);  // 开始的触发事件的时候就只执行一次，之后的就让 setTimeout 来接管，从而避免了多次调用

				window.setTimeout(function() {  
					for(var i = 0, j = self.handler.length; i < j; i++){
						var obj = self.handler[i];

						if(typeof obj == 'function') {
							obj();
						}else if(typeof obj.fn == 'function' && !obj.executeOnce) {
							obj.fn.call(obj);
							//obj.done = true;
						}
					}

					me();  // 回调  resize|scroll  事件，过程：再登记、再撤销、再调用--》再登记、再撤销、再调用--》（如此反复）
				}, 300); //300毫秒执行一次  
			});  
		})();
		
		// 先执行一次
		var obj = fn;
		if(typeof obj == 'function'){
			obj();
		}else if(typeof obj.fn == 'function' && !obj.done){
			obj.fn.call(obj);
		}
	}
};

ajaxjs.throttle.onEl_in_viewport = function(el, fn) {
	setTimeout(function(){ // 加入延时，让 dom pic 加载好，高度动态的
		UserEvent2.onWinResizeFree({
			executeOnce : false,
			fn: function(){
				var scrollTop = document.body.scrollTop, docHeight = window.innerHeight;
				
				var elTop = el.getBoundingClientRect().top, 
					isFirstPage = /*scrollTop == 0 &&*/ docHeight > elTop, 
					isInPage = scrollTop > elTop;
					
				// console.log(isFirstPage || isInPage);
				
				if (isFirstPage || isInPage) {
					this.executeOnce = true;
					fn();
				}
			}
		}, 'scroll');
	}, 1500);
}

ajaxjs.throttle.onEl_in_viewport.actions = [];

//;(function(){
//	// 回到顶部
//	var timer = null;
//	var b = 0;//作为标志位，判断滚动事件的触发原因，是定时器触发还是其它人为操作
//	UserEvent2.onWinResizeFree(function(e) {
//		if (b != 1) clearInterval(timer);
//		b = 2;
//	}, 'scroll');
//	
//	window.goTop = function () {
//		clearInterval(timer);
//		var iCur = speed = 0;
//		
//		timer = setInterval(function() {
//			iCur = document.documentElement.scrollTop || document.body.scrollTop;
//			speed = Math.floor((0 - iCur) / 8);
//			
//			if (iCur === 0) 
//				clearInterval(timer);
//			else 
//				document.documentElement.scrollTop = document.body.scrollTop = iCur + speed;
//			b = 1;
//		}, 30);
//	}
//})();