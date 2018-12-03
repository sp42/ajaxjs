// 读/写DOM元素属性
var attribute = {
	get : function(elem, attr) {
		var val = elem.currentStyle ? elem.currentStyle[attr]
				: getComputedStyle(elem)[attr];
		if (attr === "opacity")
			val *= 100;

		return val;
	},

	set : function(elem, attr, val) {
		if (attr == 'opacity') {
			elem.style.filter = 'alpha(opacity=' + (val) + ')';
			elem.style.opacity = (val) / 100;
		} else
			elem.style[attr] = val + 'px';
	}
};

// 动画对象
var effect = {
	animate : function(elem, params, fps) {
		clearTimeout(elem.timer);
		fps = fps || 25;

		setTimeout(function() {
			effect.change(elem, params, params.complete);
			elem.timer = setTimeout(arguments.callee, 1000 / fps);
		}, 1000 / fps);
	},

	stop : function(elem) {
		clearTimeout(elem.timer);
	},

	change : function(elem, params, fnEnd) {
		var i = 0;
		var attr = null, complete = false, speed = 0;

		for (attr in params) {
			i = parseFloat(attribute.get(elem, attr)) || 0;
			if (i == params[attr]) {
				complete = true;
				break;
			}
			speed = (params[attr] - i) / 8;
			speed = (speed > 0) ? Math.ceil(speed) : Math.floor(speed);
			// console.log("i=" + i + "; speed="+ speed+"; s="+s+"; k="+k);
			i += speed;
			// console.log(i);
			attribute.set(elem, attr, i);
		}

		if (complete) {
			effect.stop(elem);
			fnEnd && fnEnd.call(elem);
		}
	}
};

// 原生JavaScript焦点图切换控件
function PicSlide() {
	var controls = document.getElementById('slide-controls').getElementsByTagName('li');// 根据需要选择元素
	var list = document.getElementById("slide-list").getElementsByTagName('li');// 根据需要选择元素

	var delay = 6000;
	var _this = this;
	this.active = 0; // 当前显示内容的下标
	this.list = list;
	this.controls = controls;

	// 切换
	for (var i = 0; i < controls.length; i++) {
		controls[i].index = i;
		controls[i].onmouseenter = function() {
			if (this.index === _this.active)
				return;

			clearInterval(_this.timer);
			_this.clear();
			_this.select(this);
		};

		controls[i].onmouseleave = function() {
			clearInterval(_this.timer);
			_this.timer = setInterval(function() {
				_this.run()
			}, delay);

		};
	}
	var nextFn = function() {
		_this.run();
	};
	_this.timer = setInterval(nextFn, delay);
	document.querySelector('.nextBtn').onclick = nextFn;
	document.querySelector('.perBtn').onclick = function() {
		_this.per();
	};

};

PicSlide.prototype = {
	/* 内容淡入 */
	select : function(target) {
		target.className = 'active';
		this.active = target.index;
		effect.animate(this.list[target.index], {
			'opacity' : 100
		});
	},

	/* 内容淡出 */
	clear : function() {
		var active = this.active;

		this.controls[active].className = '';
		effect.animate(this.list[active], {
			'opacity' : 0
		});
	},

	/* 顺序播放焦点图 */
	run : function() {
		var controls = this.controls;
		var list = this.list;
		var active = this.active;

		this.clear();
		active += 1;
		active = active % controls.length;
		controls[active].className = 'active';

		effect.animate(list[active], {
			'opacity' : 100
		});
		this.active = active;
	},
	per : function() {
		var controls = this.controls;
		var list = this.list;
		var active = this.active;

		this.clear();
		active -= 1;
		active = active % controls.length;
		if (active < 0)
			active = controls.length - 1;
		controls[active].className = 'active';

		effect.animate(list[active], {
			'opacity' : 100
		});
		this.active = active;
	}
};

function fixHeight(img) {
	var height = img.height;
	document.querySelector('.opacityBanner').style.height = height + 'px';
}