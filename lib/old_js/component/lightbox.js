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
	
	// 从网页监听Android设备的返回键
	
//	window.addEventListener('popstate', function(){
//		alert(99)
//	});
	
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
	
})();