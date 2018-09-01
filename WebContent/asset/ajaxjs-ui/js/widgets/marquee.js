aj._simple_marquee_text = {
	props : {
		interval : {
			type : Number, // 动画时间间隔
			default : 500
		},
		
		canstop :{
			type : Boolean, //  是否可以鼠标移入时候暂停动画
			default : true
		}
	},
	mounted : function() {
		if (this.canstop) {
			this.$el.onmouseover = this.clearTimer.bind(this);
			this.$el.onmouseout = this.start.bind(this);
		}
	},
	methods : {
		start : function() {
			this.$timerId = window.setInterval(this.scroll, this.interval);
		},
		clearTimer : function() {
			this.$timerId && window.clearInterval(this.$timerId);
		}
	}
};

// 超简单跑马灯
// 插槽中不能包含 tag
Vue.component('aj-super-simple-marquee-text', {
	mixins : [aj._simple_marquee_text],
	template : '<div><slot>这是一段滚动的文字；这是一段滚动的文字；这是一段滚动的文字</slot></div>',
	
	mounted : function() {
		this.$arr = this.$el.innerHTML.split("");
		this.start();
	},
	methods : {
		scroll : function () {
			this.$arr.push(this.$arr.shift());
			this.$el.innerHTML = this.$arr.join("");
		}
	}
});

// 上下字幕
Vue.component('aj-simple-marquee-text', {
	mixins : [aj._simple_marquee_text],
	
	template : '<ol><li>11111111111</li><li>22222222222</li><li>33333333333</li><li>44444444444</li><li>55555555555</li></ol>',
	
	mounted : function() {
		this.start();
	},
	
	methods : {
		scroll : function () {
			var lastEl = this.$el.firstChild;
			
			while (lastEl.nodeType != 1)
				lastEl = lastEl.nextSibling;// 找到最后一个元素

			this.$el.appendChild(this.$el.removeChild(lastEl)); // 把最后一个元素放到前头
		}
	}
});