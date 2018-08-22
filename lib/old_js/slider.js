//--------------------------------------------------------
//拖放/触控 Drag&Drop
//例子 http://i.ifeng.com/ent/ylch/news?ch=ifengweb_2014&aid=91654101&mid=5e7Mzq&vt=5
//-------------------------------------------------------- 
;
(function() {
	// 是否可以支持 触控 事件
	var canTouch = ("createTouch" in document) || ('ontouchstart' in window), beforDragEvent = canTouch ? "ontouchstart"
			: "onmousedown", onDragingEvent = canTouch ? "ontouchmove"
			: "onmousemove", afterDragEvent = canTouch ? "ontouchend"
			: "onmouseup";

	ajaxjs_dd = {
		init : function() {
			this.el[beforDragEvent] = initMove.bind(this);
		}
	};

	ajaxjs_dd.getAngle = getAngle;
	ajaxjs_dd.getDirectionFromAngle = getDirectionFromAngle;

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
			distance : null,
			/*
			 * clientX和clientY表示的位置是相对浏览器窗口的，而不是对文档的，
			 * 因此当你在滚动页面之后仍然在窗口中的同一位置上单击时，所得到的坐标的值是相同的。
			 */
			x : canTouch ? e.touches.item(0).pageX : e.clientX,
			y : canTouch ? e.touches.item(0).pageY : e.clientY
		};

		// 每次拖动都会分别登记一次 onmousemove 事件和 onmouseup 事件。
		// 拖动完，又会自动撤销上述事件的。
		// 登记到 onmousedown 事件中，不撤销，而 onmousemove / onmouseup 则会撤销
		this.el[onDragingEvent] = moving.bind(this); // 拖动时都会触发该事件
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

		var lastXY = {
			x : lastData.x,
			y : lastData.y
		};

		// 角度:number
		var angle = getAngle(coordinate, lastXY);
		// 方向:string
		var direction = getDirectionFromAngle(angle);
		// 距离:number
		var disX = Math.abs(coordinate.x - lastData.x), disY = Math
				.abs(coordinate.y - lastData.y);

		var args = [ e, direction, coordinate.x, lastData.x, disX,
				coordinate.y, disY ]; // 供 after(fn) 调用的参数列表

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

		if (this.cancelStartEvent) {
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
			down : angle >= 45 && angle < 135, //90
			left : angle >= 135 || angle <= -135, //180
			up : angle < -45 && angle > -135, //270
			right : angle >= -45 && angle <= 45 //0
		};

		var direction, key;

		for (key in directions) {
			if (directions[key] === true) {
				direction = key;
				break;
			}
		}

		return direction;
	}
})();

// 滑动杆
function slider(sliderEl) {
	var btn = sliderEl.querySelector('button');

	this.btn = btn, this.sliderEl = sliderEl;

	btn.onmousedown = function(e) {
		if (e.target.tagName != 'BUTTON')
			return;
		e.preventDefault();

		var dd = Object.create(ajaxjs_dd);
		dd.onMoving = moveBtn;

		dd.el = document; // 可拖放的范围，documement 表示整张桌布
		dd.init();

		return false;
	}

	// 函数表达式的作用！
	var moveBtn = (function(e, data) {
		var sliderWidth = this.sliderEl.clientWidth;
		var left = data.x - getLeft(this.sliderEl) - (this.btn.clientWidth)
				- 320;

		if (sliderWidth && left > (sliderWidth - 15)) {
			left = sliderWidth - 15;
		}

		if (left < 0)
			left = 0;

		setBtnLeft.call(this, left);
		//				getValue(left)
	}).bind(this);

	function onfocus(e) {
		var el = e.target;
		var onFocusEl;
		if ('hour'.indexOf(el.className) != -1) {
			onFocusEl = 'hour';
		} else if ('minute'.indexOf(el.className) != -1) {
			onFocusEl = 'minute';
		} else if ('second'.indexOf(el.className) != -1) {
			onFocusEl = 'second';
		}

		onFocusEl && el.up('ul').setAttribute('data-focus', onFocusEl);
	}

	// 输入框当有值改变时 并且获取了焦点的状态的时候，滑动杆变化
	function onkeyup(e) {
		var input = e.target;
		if ((input.value / 60) > 1)
			return;

		console.log('input.value/60 ' + (input.value / 60));

		var left = this.sliderEl.clientWidth;
		left = left * (input.value / 60);
		left = window.parseInt(left, 10);
		//				setBtnLeft.call(this, left, true);
	}

	var hour = sliderEl.parentNode.querySelector('.hour'), minute = sliderEl.parentNode
			.querySelector('.minute'), second = sliderEl.parentNode
			.querySelector('.second');

	//			second.onfocus = minute.onfocus = hour.onfocus = onfocus;
	//			second.onkeyup = minute.onkeyup = hour.onkeyup = onkeyup.bind(this);

	var getValue = (function(left) {
		//				var inputEl;
		//				switch (hour.up('ul').getAttribute('data-focus')) {
		//					case 'hour':
		//						inputEl = hour;
		//						type = 24;
		//						break;
		//					case 'minute':
		//						type = 60;
		//						inputEl = minute;
		//						break;
		//					case 'second':
		//						inputEl = second;
		//						type = 60;
		//						break;
		//				}
		//
		//				var percent = left / (this.sliderEl.clientWidth - 14);
		//				console.log('getValue():' + percent);
		//
		//				if (inputEl && type && percent > 0)
		//					inputEl.value = window.parseInt(percent * type);
	}).bind(this);

	// 点击，跳到那个刻度
	function onSliderClick(e) {
		if (e.target.tagName == 'BUTTON')
			return; // 原本 阻止事件上报 就可以了 为什么不行？

		var btn = this.btn, left = e.offsetX - (btn.clientWidth / 2);

		setBtnLeft.call(this, left, true);
		//				getValue(left);
	}

	function setBtnLeft(left, isWithAnimated) {
		if (isNaN(left) || left < 0 || left > this.sliderEl.clientWidth)
			return;

		//				isWithAnimated && this.btn.addCls('withAnimated');

		this.btn.style.left = left + 'px';

		var btn = this.btn;
		isWithAnimated && setTimeout(function() {
			//					btn.removeCls('withAnimated');
		}, 500);
	}

	sliderEl.onclick = onSliderClick.bind(this);
}

new slider(document.querySelector('.slider'));

function setLeft(el, left) {
	el.style.left = left + 'px';
}
function getLeft(el) {
	//			debugger;
	return el.style.left ? parseInt(el.style.left) : 0;
}