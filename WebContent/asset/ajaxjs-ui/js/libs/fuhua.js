
// 禁止调试 <body onload="disableInfo()">
function disableInfo() {
    document.onkeydown = function() {
        var e = window.event || arguments[0];
        //屏蔽F12
        if(e.keyCode == 123) {
            return false;
            //屏蔽Ctrl+Shift+I
        } else if((e.ctrlKey) && (e.shiftKey) && (e.keyCode == 73)) {
            return false;
            //屏蔽Shift+F10
        } else if((e.shiftKey) && (e.keyCode == 121)){
            return false;
        }
    };
    //屏蔽右键单击
    document.oncontextmenu = function() {
        return false;
    }
}


/**
			* 使用 HTML5 的 History 新 API pushState 来曲线监听 Android 设备的返回按钮
			* http://www.alloyteam.com/2013/02/cong-wang-ye-jian-ting-android-she-bei-di-fan-hui-jian/
			* @author azrael
			* @date 2013/02/04
			* @version 1.0
			* @example
			* XBack.listen(function(){
			alert('oh! you press the back button');
			});
			*/
			;!function(pkg, undefined){
				var STATE = 'x-back';
				var element;
				var onPopState = function(event){
					event.state === STATE && fire();
				}
				var record = function(state){
					history.pushState(state, null, location.href);
				}
				var fire = function(){
					var event = document.createEvent('Events');
					event.initEvent(STATE, false, false);
					element.dispatchEvent(event);
				}
				var listen = function(listener){
					element.addEventListener(STATE, listener, false);
				}
				!function(){
					element = document.createElement('span');
					window.addEventListener('popstate', onPopState);
					this.listen = listen;
					record(STATE);
				}.call(window[pkg] = window[pkg] || {});
			}('XBack');
			
			
			// 从网页监听Android设备的返回键
			
//			window.addEventListener('popstate', function(){
//				alert(99)
//			});
			
			
			/*
			 * --------------------------------------------------------
			 * 图片浏览器
			 * --------------------------------------------------------
			 */
			;(function() {
				ajaxjs.Tab.Lightbox = Object.create(ajaxjs.Tab);
				ajaxjs.Tab.Lightbox.moverTagName = 'ul';
				ajaxjs.Tab.Lightbox.isEnableLoop = false;
				ajaxjs.Tab.Lightbox.isEnableTouch = true;
				ajaxjs.Tab.Lightbox.isUsePx = false; 
				ajaxjs.Tab.Lightbox.isGetCurrentHeight = false;
				ajaxjs.Tab.Lightbox.isDirectShow = true;
				ajaxjs.Tab.Lightbox.showing = false;
				// override
				ajaxjs.Tab.Lightbox.go = function(i) {
					var el = this.el;
					
					// 直接跳到那一幀并伴随有渐显示效果
					
					if(this.isDirectShow && !this.showing)el.style.display = 'none';
					this.isDirectShow && !this.showing && setTimeout(function() {
			        	//bf_Fx.fadeIn(el);
			           el.style.display = 'block';
			           ajaxjs.Tab.Lightbox.showing = true;
			        }, 500);
			        
					// location.setUrl_hash('picIndex', i);
					ajaxjs.Tab.go.call(this, i, true);
				}
				// override
				ajaxjs.Tab.Lightbox.init = function() {
					ajaxjs.Tab.init.apply(this, arguments);
					document.addEventListener('touchmove', noScroll);	// 不允许屏幕滚动
					this.el.onclick = this.close.bind(this);			// 点击关闭退出
					parent.document.onkeydown = onEnterAndEsc.bind(this);
					
					XBack.listen(this.close.bind(this));
				}
				
				ajaxjs.Tab.Lightbox.close = function() {
					this.showing = false;
					this.el.parentNode.removeChild(this.el);
					document.removeEventListener('touchmove', noScroll);
				}
				
				function noScroll(e) {
			    	e.preventDefault();
			    }
				
				// 键盘事件
				function onEnterAndEsc(e) {
			       e = e || event;
			       var keycode = e.which || e.keyCode;
			       
			       switch(keycode){  
				       	case 13: //enter   
						break;
						case 37:   
						case 3:   
						case 271: //left 
							this.goPrevious(); 
						break;   
						case 39:   
						case 4:   
						case 272: //right
							this.goNext(); 
						break;  
						case 339: //exit   
						case 340: //back    
						case 27:
							this.close(); 
				   } 
				}
				

			});
			
				
			/*	  parent.document.onmousewheel = function(e){
			          if(e.wheelDelta == -120){
			              if((++wheelDelta) > 4){
			                  // console.log('next'); // 加大 wheelDelta 值 就不会跳帧
			                  this.goNext();
			                  wheelDelta = 0;
			              }
			          }
			          if(e.wheelDelta == 120){
			              if((++wheelDelta) > 4){
			                  this.goPrevious();
			                  wheelDelta = 0;
			              }
			          }
			      }.bind(this);*/
			
			
var scrollSpy = new ScrollSpy();
scrollSpy.init();

var el = document.querySelector('#nav');
scrollSpy.spyOn(el);

/* Element is visible again on the screen. */
document.addEventListener('ScrollSpyBackInSight', function (e) {
    /* ie. Make the nav to float. */
  el.style.position = 'relative';
});

/* Element is not visible anymore on the screen. */
document.addEventListener('ScrollSpyOutOfSight', function (e) {
  /* ie. Make the nav to fix. */
  el.style.position = 'fixed';
  el.style.top = '0px';
});

var ScrollSpy = (function() {
    var elements = {};

    function init() {
        if (document.addEventListener) {
            document.addEventListener("touchmove", handleScroll, false);
            document.addEventListener("scroll", handleScroll, false);
        } else if (window.attachEvent) {
            window.attachEvent("onscroll", handleScroll);
        }
    }

    function spyOn(domElement) {
        var element = {};
        element['domElement'] = domElement;
        element['isInViewPort'] = true;
        elements[domElement.id] = element;
    }

    function handleScroll() {
        var currentViewPosition = document.documentElement.scrollTop ? document.documentElement.scrollTop: document.body.scrollTop;

        for (var i in elements) {
            var element = elements[i];
            var elementPosition = getPositionOfElement(element.domElement);

            var usableViewPosition = currentViewPosition;
            if (element.isInViewPort == false) 
                usableViewPosition -= element.domElement.clientHeight;

            if (usableViewPosition > elementPosition) {
                fireOutOfSightEvent(element.domElement);
                element.isInViewPort = false;
            } else if (element.isInViewPort == false) {
                fireBackInSightEvent(element.domElement);
                element.isInViewPort = true;
            }
        };
    }

    function fireOutOfSightEvent(domElement) {
        fireEvent('ScrollSpyOutOfSight', domElement);
    }

    function fireBackInSightEvent(domElement) {
        fireEvent('ScrollSpyBackInSight', domElement);
    }

    function fireEvent(eventName, domElement) {
        if (document.createEvent) {
            var event = document.createEvent('HTMLEvents');
            event.initEvent(eventName, true, true);
            event.data = domElement;
            document.dispatchEvent(event);
        } else if (document.createEventObject) {
            var event = document.createEventObject();
            event.data = domElement
            event.expando = eventName;
            document.fireEvent('onpropertychange', event);
        }
    }

    function getPositionOfElement(domElement) {
        var pos = 0;
        while (domElement != null) {
            pos += domElement.offsetTop;
            domElement = domElement.offsetParent;
        }
        
        return pos;
    }

    return {
        init: init,
        spyOn: spyOn
    };
});

/**
 * 随机生成固定位数或者一定范围内的字符串数字组合 https://www.cnblogs.com/moqiutao/p/8191214.html
 * 
 * var rand01 = randomRange(); var rand02 = randomRange(5,8,'abcdef012'); var
 * rand03 = randomRange(10); var rand04 = randomRange(5,'123');
 * 
 * @param {Number}
 *            min 范围最小值
 * @param {Number}
 *            max 范围最大值，当不传递时表示生成指定位数的组合
 * @param {String}
 *            charStr指定的字符串中生成组合
 * @returns {String} 返回字符串结果
 */
function randomRange(min, max, charStr){
    var returnStr = "", // 返回的字符串
        range; // 生成的字符串长度
        
    // 随机生成字符
    var autoGetStr = function(){
        var charFun = function(){
            var n= Math.floor(Math.random()*62);
            if(n<10){
                return n; // 1-10
            }
            else if(n<36){
                return String.fromCharCode(n+55); // A-Z
            }
            else{
                return String.fromCharCode(n+61); // a-z
            }
        }
        while(returnStr.length< range){
            returnStr += charFun();
        }
    };
    
    // 根据指定的字符串中生成组合
    var accordCharStrGet = function(){
        for(var i=0; i<range; i++){
            var index = Math.round(Math.random() * (charStr.length-1));
            returnStr += charStr.substring(index,index+1);
        }
    };
    if(typeof min == 'undefined'){
        min = 10;
    }
     if(typeof max == 'string'){
         charStr = max;
     }
     range = ((max && typeof max == 'number') ? Math.round(Math.random() * (max-min)) + min : min);
     
     if(charStr){
         accordCharStrGet();
     }else{
         autoGetStr();
     }
    return returnStr;
}

/**
 * 惯性原理: 产生的速度 = 移动距离 / 移动时间 距离 = 松开的坐标 - 上次的坐标 (距离差) 时间 = 松开的时间 - 按下的时间 (时间差)
 * https://www.cnblogs.com/moqiutao/p/8529508.html 只是实现了上下滑动惯性，没有写水平滑动惯性
 * 
 * 当滑动到页面底部的时候才触发touchmove事件
 * 
 * 
 * dragFun.init({ dargDom:'#contractContanier' });
 * 
 * 
 */

var dargFun = {
    dargDom:null, // 惯性滑动的DOM区域
    startX:0, // 开始偏移的X
    startY:0, // 开始偏移的Y
    clientX:0, 
    clientY:0,
    translateX:0, // 保存的X偏移
    translateY:0, // 保存的Y偏移
    maxWidth:0, // 滑动的最大宽度
    maxHeight:0, // 滑动的最大高度
    startTime:0, // 记录初始按下时间
    init:function(config){
        this.dargDom = document.querySelector(config.dargDom);
        this.maxWidth = this.dargDom.offsetWidth;
        this.maxHeight = this.dargDom.offsetHeight;
        
        this.dargDom.addEventListener('touchstart',(event)=>{
            event.stopPropagation(); // 停止事件传播
            this.clientX = event.changedTouches[0].clientX;
            this.clientY = event.changedTouches[0].clientY;
            this.dargDom.style.WebkitTransition = this.dargDom.style.transition = '';
            this.startX = this.translateX;
            this.startY = this.translateY;
            this.startTime = Date.now();
        },false);
        
        this.dargDom.addEventListener('touchmove',(event)=>{
            if(document.documentElement.scrollTop >= this.dargDom.scrollHeight - this.dargDom.clientHeight){
                
            }else{
                return;
            }
        
            event.stopPropagation(); // 停止事件传播
            this.translateX = event.changedTouches[0].clientX - this.clientX + this.startX;
            this.translateY = event.changedTouches[0].clientY - this.clientY + this.startY;
            if(this.translateY > 0 ){ // 拖动系数. 拉力的感觉
                this.translateY *= 0.4;
            }else if( this.translateY < -(this.dargDom.scrollHeight - this.dargDom.clientHeight)){ 
                this.translateY = (event.changedTouches[0].clientY - this.clientY) * 0.4 + this.startY;
            }
            this.animate(this.translateY);
        },false);
        
        this.dargDom.addEventListener('touchend',(event)=>{
            event.stopPropagation(); // 停止事件传播
            var distanceY = event.changedTouches[0].clientY - this.clientY,
            timeDis = Date.now() - this.startTime,  // 时间差
            speed = (distanceY / timeDis) * 100;
            
            // 惯性
            this.translateY += speed;
    
            this.translateY  = 0;
            // 添加贝塞尔曲线
            this.dargDom.style.WebkitTransition = this.dargDom.style.transition = 'transform 500ms cubic-bezier(0.1, 0.57, 0.1, 1)';
            this.animate(this.translateY);
        
        },false);
        
    },
    animate:function(y){
        this.dargDom.style.WebkitTransform = this.dargDom.style.transform = 'translateY('+y+'px)';
    }
}


/**
 * 函数节流 https://www.cnblogs.com/moqiutao/p/6875955.html
 */

var throttleV2 = function(fn, delay, mustRunDelay){
     var timer = null;
     var t_start;
     return function(){
         var context = this, args = arguments, t_curr = +new Date();
         clearTimeout(timer);
         if(!t_start){
             t_start = t_curr;
         }
         if(t_curr - t_start >= mustRunDelay){
             fn.apply(context, args);
             t_start = t_curr;
         }
         else {
             timer = setTimeout(function(){
                 fn.apply(context, args);
             }, delay);
         }
     };
 };


 /**
	 * touch.js 拖动、缩放、旋转 （鼠标手势）
	 * 
	 * 
	 * 
	 * <div style="position:relative;width: 100%;height: 250px;overflow:
	 * hidden;border: 1px dashed #ff0000;"> <img id="targetObj"
	 * style="position:relative;transform-origin:center"
	 * src="http://demo.somethingwhat.com/images/flower1.jpg" /> </div>
	 * 
	 * 
	 * https://www.somethingwhat.com/Project/Detail?id=f092710d3f7255b4
	 */
 var cat = window.cat || {};  
 cat.touchjs = {  
     left: 0,  
     top: 0,  
     scaleVal: 1,    // 缩放
     rotateVal: 0,   // 旋转
     curStatus: 0,   // 记录当前手势的状态, 0:拖动, 1:缩放, 2:旋转
     // 初始化
     init: function ($targetObj, callback) {  
         touch.on($targetObj, 'touchstart', function (ev) {  
             cat.touchjs.curStatus = 0;  
             ev.preventDefault();// 阻止默认事件
         });  
         if (!window.localStorage.cat_touchjs_data)  
             callback(0, 0, 1, 0);  
         else {  
             var jsonObj = JSON.parse(window.localStorage.cat_touchjs_data);  
             cat.touchjs.left = parseFloat(jsonObj.left), cat.touchjs.top = parseFloat(jsonObj.top), cat.touchjs.scaleVal = parseFloat(jsonObj.scale), cat.touchjs.rotateVal = parseFloat(jsonObj.rotate);  
             callback(cat.touchjs.left, cat.touchjs.top, cat.touchjs.scaleVal, cat.touchjs.rotateVal);  
         }  
     },  
     // 拖动
     drag: function ($targetObj, callback) {  
         touch.on($targetObj, 'drag', function (ev) {  
             $targetObj.css("left", cat.touchjs.left + ev.x).css("top", cat.touchjs.top + ev.y);  
         });  
         touch.on($targetObj, 'dragend', function (ev) {  
             cat.touchjs.left = cat.touchjs.left + ev.x;  
             cat.touchjs.top = cat.touchjs.top + ev.y;  
             callback(cat.touchjs.left, cat.touchjs.top);  
         });  
     },  
     // 缩放
     scale: function ($targetObj, callback) {  
         var initialScale = cat.touchjs.scaleVal || 1;  
         var currentScale;  
         touch.on($targetObj, 'pinch', function (ev) {  
             if (cat.touchjs.curStatus == 2) {  
                 return;  
             }  
             cat.touchjs.curStatus = 1;  
             currentScale = ev.scale - 1;  
             currentScale = initialScale + currentScale;  
             cat.touchjs.scaleVal = currentScale;  
             var transformStyle = 'scale(' + cat.touchjs.scaleVal + ') rotate(' + cat.touchjs.rotateVal + 'deg)';  
             $targetObj.css("transform", transformStyle).css("-webkit-transform", transformStyle);  
             callback(cat.touchjs.scaleVal);  
         });  
   
         touch.on($targetObj, 'pinchend', function (ev) {  
             if (cat.touchjs.curStatus == 2) {  
                 return;  
             }  
             initialScale = currentScale;  
             cat.touchjs.scaleVal = currentScale;  
             callback(cat.touchjs.scaleVal);  
         });  
     },  
     // 旋转
     rotate: function ($targetObj, callback) {  
         var angle = cat.touchjs.rotateVal || 0;  
         touch.on($targetObj, 'rotate', function (ev) {  
             if (cat.touchjs.curStatus == 1) {  
                 return;  
             }  
             cat.touchjs.curStatus = 2;  
             var totalAngle = angle + ev.rotation;  
             if (ev.fingerStatus === 'end') {  
                 angle = angle + ev.rotation;  
             }  
             cat.touchjs.rotateVal = totalAngle;  
             var transformStyle = 'scale(' + cat.touchjs.scaleVal + ') rotate(' + cat.touchjs.rotateVal + 'deg)';  
             $targetObj.css("transform", transformStyle).css("-webkit-transform", transformStyle);  
             $targetObj.attr('data-rotate', cat.touchjs.rotateVal);  
             callback(cat.touchjs.rotateVal);  
         });  
     }  
 }; 