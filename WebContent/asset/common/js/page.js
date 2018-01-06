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