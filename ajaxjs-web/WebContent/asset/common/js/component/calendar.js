;(function(){
	/**
	 * 提取天数
	 * @param {String} className 样式
	 */
	function className2Date(className) {
		className = className.match(/\bday_([\d-]+)\b/);
		return className[1];
	}
	
	/**
	 * @param {Event} e 浏览器事件对象
	 */
	function onSelect(e) {
		var el = e.target;
		while (el) {
			if (el.tagName == 'TD') {
				this.onSelect && this.onSelect(className2Date(el.className));
				break;
			} else {
				el = el.parentNode;
			}
		}
	}
	
	bf_calendar = {
		init : function(options) {
			if(!this.el)throw '未指定容器！';
			
			this.Container = this.el.querySelector('tbody');
			this.Container.onclick =(onSelect.bind(this));
			this.Days = [];//日期对象列表

			for(var i in options)
				this.options[i] = options[i];

			this.Year = this.options.Year || new Date().getFullYear();
			this.Month = this.options.Month || new Date().getMonth() + 1;
			this.SelectDay = this.options.SelectDay ? new Date(this.options.SelectDay) : null;
			this.onSelectDay = this.options.onSelectDay;
			this.onToday = this.options.onToday;
			this.onFinish = this.options.onFinish;	

			this.draw();
			
			var sliderEls = document.querySelectorAll('.slider');
			for(var i = 0, j = sliderEls.length; i < j; i++)
				new slider(sliderEls[i]);

			this.el.querySelector(".idCalendarPre").onclick = this.PreMonth.bind(this);
			this.el.querySelector(".idCalendarNext").onclick = this.NextMonth.bind(this);

			this.el.querySelector(".idCalendarPreYear").onclick = this.PreYear.bind(this);
			this.el.querySelector(".idCalendarNextYear").onclick = this.NextYear.bind(this);
			this.el.querySelector(".idCalendarNow").onclick = this.NowMonth.bind(this);
		},
		Container : null,//容器(table结构)

		// 设置默认属性
		options : {
			Year:			0,//显示年
			Month:			0,//显示月
			SelectDay:		null,//选择日期
			onSelectDay:	function(){},//在选择日期触发
			onToday:		function(){},//在当天日期触发
			onFinish:		function(){}//日历画完后触发
		},

		// 当前月
		NowMonth: function() {
			this.preDraw(new Date);
		},
		// 上一月
		PreMonth: function() {
			this.preDraw(new Date(this.Year, this.Month - 2, 1));
		},
		// 下一月
		NextMonth: function() {
			this.preDraw(new Date(this.Year, this.Month, 1));
		},
		// 上一年
		PreYear: function() {
			this.preDraw(new Date(this.Year - 1, this.Month - 1, 1));
		},
		// 下一年
		NextYear: function() {
			this.preDraw(new Date(this.Year + 1, this.Month - 1, 1));
		},
		// 根据日期画日历
		preDraw: function(date) {
			this.Year  = date.getFullYear(); 
			this.Month = date.getMonth() + 1;// 再设置属性
			this.draw();// 重新画日历
		},
		// 画日历
		draw : function() {
			var arr = [];// 用来保存日期列表

			// 用当月第一天在一周中的日期值作为当月离第一天的天数
			for(var i = 1, firstDay = new Date(this.Year, this.Month - 1, 1).getDay(); i <= firstDay; i++)
				arr.push(0);
			// 用当月最后一天在一个月中的日期值作为当月的天数
			for(var i = 1, monthDay = new Date(this.Year, this.Month, 0).getDate(); i <= monthDay; i++)
				arr.push(i);

			// 清空原来的日期对象列表
			this.Days = [];

			// 插入日期
			var frag = document.createDocumentFragment();
			while (arr.length) {
				var row = document.createElement("tr"); // 每个星期插入一个tr
				for (var i = 1; i <= 7; i++) {			// 每个星期有7天
					var cell = document.createElement("td");
					cell.innerHTML = "";

					if (arr.length) {
						var d = arr.shift();
						if (d) {
							cell.innerHTML = d;
							cell.className = 'day day_' + this.Year + '-' + this.Month + '-' + d;
							this.Days[d] = cell;
							var on = new Date(this.Year, this.Month - 1, d);

							this.isSame(on, new Date()) && this.onToday(cell);// 判断是否今日
							this.SelectDay && this.isSame(on, this.SelectDay) && this.onSelectDay(cell);// 判断是否选择日期
						}
					}
					row.appendChild(cell);
				}
				frag.appendChild(row);
			}
			// 先清空内容再插入(ie的table不能用innerHTML)
			while (this.Container.hasChildNodes())
				this.Container.removeChild(this.Container.firstChild);

			this.Container.appendChild(frag);

			this.el.querySelector(".idCalendarYear").innerHTML  = this.Year; 
			this.el.querySelector(".idCalendarMonth").innerHTML = this.Month;
			// 附加程序
			this.onFinish();
		},
		// 判断是否同一日
		isSame : function(d1, d2) {
			return (d1.getFullYear() == d2.getFullYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate());
		} 
	};
	
	
	// 滑动杆
//	var _dd = require('bigfoot/dhtml/dd');
	function slider(sliderEl){
		var btn = sliderEl.querySelector('button');
		
		this.btn = btn, this.sliderEl = sliderEl;
		
		btn.onmousedown = function(e){
			if(e.target.tagName != 'BUTTON')return;
			e.preventDefault();
			
			var dd = Object.create(_dd);
			dd.onMoving = moveBtn;
//			dd.onAfterMove = function(e, data){
//				var sliderWidth = this.sliderEl.clientWidth,
//				left = data.x - this.sliderEl.getLeft() - (this.btn.clientWidth / 2);
//				
//				if(left < 0)left = 0;
//				if(left > sliderWidth)left = sliderWidth;
//				
//				getValue(left)
//			}.bind(this);
			dd.el = document; // 可拖放的范围，documement 表示整张桌布
			dd.init();
			
			return false;
		}.bind(this);
		

		
		// 函数表达式的作用！
		var moveBtn = (function(e, data){
			var sliderWidth = this.sliderEl.clientWidth;
			//sliderWidth - 30;
			var left = data.x - this.sliderEl.getLeft() - (this.btn.clientWidth / 2);
			
			if(sliderWidth && left > (sliderWidth - 15))left = sliderWidth - 15;
			
			setBtnLeft.call(this, left);
			getValue(left)
		}).bind(this);
		
		function onfocus(e){
			var el = e.target;
			var onFocusEl;
			if('hour'.indexOf(el.className) != -1){
				onFocusEl = 'hour';
			}else if('minute'.indexOf(el.className) != -1){
				onFocusEl = 'minute';
			}else if('second'.indexOf(el.className) != -1){
				onFocusEl = 'second';
			}
			
			onFocusEl && el.up('ul').setAttribute('data-focus', onFocusEl);
		}
		// 输入框当有值改变时 并且获取了焦点的状态的时候，滑动杆变化
		function onkeyup(e){
			var input = e.target;
			if((input.value/60) > 1)return;
			console.log('input.value/60 '+(input.value/60) );
			var left = this.sliderEl.clientWidth * (input.value/60);
			left = window.parseInt(left, 10);
			
			setBtnLeft.call(this, left, true);
		}
		
		var hour	= sliderEl.parentNode.querySelector('.hour'),  
			minute	= sliderEl.parentNode.querySelector('.minute'),
			second	= sliderEl.parentNode.querySelector('.second');
		
		second.onfocus = minute.onfocus = hour.onfocus = onfocus;
		second.onkeyup = minute.onkeyup = hour.onkeyup = onkeyup.bind(this);
		
		var getValue = (function (left){
			var inputEl;
			switch(hour.up('ul').getAttribute('data-focus')){
				case 'hour':
					inputEl = hour;
					type = 24;
				break;
				case 'minute':
					type = 60;
					inputEl = minute;
				break;
				case 'second':
					inputEl = second;
					type = 60;
				break;
			}

			var percent = left / (this.sliderEl.clientWidth - 14);
			console.log('getValue():' + percent);
			
			if(inputEl && type && percent > 0)
				inputEl.value = window.parseInt(percent * type);
		}).bind(this);
		
		
		// 点击，跳到那个刻度
		function onSliderClick(e){
			if(e.target.tagName == 'BUTTON')return; // 原本 阻止事件上报 就可以了 为什么不行？
			var btn = this.btn, left = e.offsetX - (btn.clientWidth / 2);
			
			setBtnLeft.call(this, left, true);
			getValue(left);
		}
		
		function setBtnLeft(left, isWithAnimated){
			if(isNaN(left))return;
			if(left < 0  || left > this.sliderEl.clientWidth)return;
			
			isWithAnimated && this.btn.addCls('withAnimated');
			
			this.btn.setLeft(left);
			
			var btn = this.btn;
			isWithAnimated && setTimeout(function(){
				btn.removeCls('withAnimated');
			}, 500);
		}
		
		sliderEl.onClk(onSliderClick.bind(this));
	}
})();