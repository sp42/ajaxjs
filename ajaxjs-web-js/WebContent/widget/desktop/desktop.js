Vue.component('aj-window', {
	template : 
			'<div class="window">\
				<header><div>\
						<div class="min" @click="min">&#8259;</div><div class="max" @click="max">{{isMax ? \'&#10696;\' : \'☐\'}}</div><div class="close">×</div>\
					</div><h1>{{title}}</h1>\
				</header>\
				<div>\
					<iframe src="http://qq.com"></iframe>\
				</div>\
			</div>',
	props : {},
	data: function(){
		return {
			title:'This is a windows',
			isMax: false,
			notMaxSize: {w:0, h:0},
			notMaxXY:   {x:0, y:0}
		};
	},
	methods:{
		max(){
			if(!this.isMax) {
				this.notMaxSize.w = this.$el.clientWidth;
				this.notMaxSize.h = this.$el.clientHeight;
				
				setXY.call(this.$el, 0, 0);
				
				this.$el.style.height  = '100%';
				this.$el.style.width =  '100%';
			} else {
				setSize.call(this.$el, this.notMaxSize.h, this.notMaxSize.w);
			}
			
			this.isMax = !this.isMax;
		},
		min(){
			
		}
	}
});

new Vue({
	el : '.stage'
});

var box = aj(".window");
var dragBar = box.$('h1');
// 鼠标按下的函数
dragBar.onmousedown = function(oEvent) {
	// 求出鼠标和box的位置差值
	var x = oEvent.clientX - box.offsetLeft;
	var y = oEvent.clientY - box.offsetTop;
	
	// 鼠标移动的函数
	// 把事件加在document上，解决因为鼠标移动太快时，
	// 鼠标超过box后就没有了拖拽的效果的问题
	document.onmousemove = function(oEvent) {
		// 只能拖动窗口标题才能移动
		if (oEvent.target != dragBar) {
			// return;
		}
		
		// 保证拖拽框一直保持在浏览器窗口内部，不能被拖出的浏览器窗口的范围
		var l = oEvent.clientX - x, t = oEvent.clientY - y;
		var doc = document.documentElement;
		
		if (l < 0) {
			l = 0;
		} else if (l > doc.clientWidth - box.offsetWidth) {
			l = doc.clientWidth - box.offsetWidth;
		}
		
		if (t < 0) {
			t = 0;
		} else if (t > doc.clientHeight - box.offsetHeight) {
			t = doc.clientHeight - box.offsetHeight;
		}
		
		box.style.left = l + "px";
		box.style.top = t + "px";
	}
	
	// 鼠标抬起的函数
	document.onmouseup = function() {
		document.onmousemove = document.onmouseup = null;
	}
	
	// 火狐浏览器在拖拽空div时会出现bug
	// return false阻止默认事件，解决火狐的bug
	return false;
}

var el = document.body, mask = aj('.mask');


function setXY (x, y)  {
	this.style.left = x + 'px';
	this.style.top  = y + 'px';
}

function setSize(h, w)  {
	this.style.height  = h + 'px';
	this.style.width = w + 'px';
}

el.onmousedown = (e) => {
	var startX = e.clientX, startY = e.clientY;
	// setXY.call(mask, startX, startY);

	el.onmousemove = (e) => {
		var x = e.clientX, y = e.clientY;
		// setSize.call(mask, x - startX, y - startY);

	}
	
	// 鼠标抬起的函数
	el.onmouseup = () => {
		// setSize.call(mask, 0, 0);
		el.onmousemove = el.onmouseup = null;
	}
	
	
	return false;
}