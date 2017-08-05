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
 * HTML Editor HTML 在线编辑器
 * --------------------------------------------------------
 */
ajaxjs.HtmlEditor = function(el) {
	this.iframeEl 	  = el.querySelector('iframe');
	this.sourceEditor = el.querySelector('textarea');
	this.iframeWin 	  = this.iframeEl.contentWindow;
	this.mode         = 'iframe'; // 当前可视化编辑 iframe|textarea
	
	this.toolbarEl    = el.querySelector('.toolbar');
	this.toolbarEl.onclick = function(e) {
		var el = e.target, clsName = el.className;
		
		clsName = clsName.split(' ').shift();
		switch(clsName) {
			case 'createLink':
				this.format("createLink", prompt("请输入 URL 地址"));
			break;
			case 'insertImage':
				this.format("insertImage", prompt("请输入图片地址", "http://"));
			break;
			case 'switchMode':
				this.setMode();
			break;
			default:
				this.format(clsName);
		}
	}.bind(this);
	
	this.format = function (type, para) {
//		this.iframeWin.focus();
		if (!para) {
			if (document.all) 
				this.iframeDoc.execCommand(type);
			else 
				this.iframeDoc.execCommand(type, false, false);
		} else 
			this.iframeDoc.execCommand(type, false, para);
//		this.iframeWin.focus();
	}
	
	this.setValue = function(v) {
		var self2 = this;
		setTimeout(function() {
			self2.iframeBody.innerHTML = v; 
		}, 500);
	}
	
	this.getValue = function() {
		return this.iframeBody.innerHTML;
	}
	
	this.setMode = function () {
		if (this.mode == 'iframe') {
			this.iframeEl.classList.add('hide');
			this.sourceEditor.classList.remove('hide');
			this.sourceEditor.value = this.iframeBody.innerHTML;
			this.mode = 'text';
			grayImg(this.toolbarEl, true);
		} else {
			this.iframeEl.classList.remove('hide');
			this.sourceEditor.classList.add('hide');
			this.iframeBody.innerHTML = this.sourceEditor.value;
			this.mode = 'iframe';
			grayImg(this.toolbarEl, false);
		}
	}
	// 使图片灰色
	function grayImg(toolbarEl, isGray) {
		var imgs = toolbarEl.querySelectorAll('img');
		for(var i = 0, j = imgs.length; i < j; i++) {
			var img = imgs[i];
			img.style.filter = isGray ? 'grayscale(100%)' : '';
		}
	}
	
	el.querySelector('.fontfamilyChoser').onclick = function(e) {
		var el = e.target; 
		
		this.format('fontname', el.innerHTML);
		// 如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：
		var menuPanel = el.parentNode;
		menuPanel.style.display = 'none';

		setTimeout(function() {
			menuPanel.style.display = '';
		}, 300);
	}.bind(this);

	el.querySelector('.fontsizeChoser').addEventListener('click', function(e) {	
		var el = e.target;
		for(var els = e.currentTarget.children, i = 0, j = els.length; i < j; i++)
			if(el == els[i]) break;
		
		this.format('fontsize', i);
	}.bind(this));
	
	// 这个方法只能写在 onload 事件 不写 onload 里还不执行
	this.iframeWin.onload = function() {
		this.iframeDoc 	= this.iframeWin.document;
		this.iframeDoc.designMode = 'on';
		this.iframeBody = this.iframeDoc.body;

		// 有内容
		this.sourceEditor.value && this.setValue(this.sourceEditor.value);
	}.bind(this);
}

/**
 * 创建颜色拾取器
 * @returns {String}
 */
ajaxjs.HtmlEditor.createColorPickerHTML = function() {
	// 定义变量
	var cl = ['00', '33', '66', '99', 'CC', 'FF'], a, b, c, d, e, f, i, j, k, T;
	// 创建head
	var h = '<div class="colorhead"><span class="colortitle">颜色选择</span></div>\
				<div class="colorbody"><table cellspacing="0" cellpadding="0"><tr>';// 创建body [6 x 6的色盘]
	
	for (var i = 0; i < 6; ++i) {
		h += '<td><table class="colorpanel" cellspacing="0" cellpadding="0">';
		for (var j = 0, a = cl[i]; j < 6; ++j) {
			h += '<tr>';
			for (var k = 0, c = cl[j]; k < 6; ++k) {
				b = cl[k];
				e = k == 5 && i != 2 && i != 5 ? ';border-right:none;' : '';
				f = j == 5 && i < 3 ? ';border-bottom:none' : '';
				d = '#' + a + b + c;
				T = document.all ? '&nbsp;' : '';
				/* 切记设置unselectable='on' */
				h += '<td unselectable="on" style="background-color: ' + d + e + f + '" title="' + d + '">' + T + '</td>'; 
			}
			h += '</tr>';
		}
		h += '</table></td>';
		if (cl[i] == '66') h += '</tr><tr>';
	}
	h += '</tr></table></div>';
	
	return h;
}


 

/*
 * --------------------------------------------------------
 * 图片上传预览
 * --------------------------------------------------------
 */
ajaxjs.Upload_perview = function(perviewImg, uploadInput, maxSize) {			
	uploadInput.addEventListener('change', function(perviewImg_event) {
		var filepacker = perviewImg_event.target;

		if (!checkImgByExtName(filepacker.value)){
			alert('根据文件后缀名判断，此文件不是图片');								
			return;
		}

		if (uploadInput.files && uploadInput.files[0]) {
			var reader = new FileReader();
			reader.onload = function(evt) {
				if (!checkImgByBin(evt.target.result)) {
					alert('亲，改了扩展名我还能认得你不是图片哦');
					uploadInput.value = '';
					return;
				}
				perviewImg.src = evt.target.result;
			}

			var file = filepacker.files[0], fileSize = file.size; // 文件的大小，单位为字节B

			var maxSize = 600;

			if(fileSize > maxSize * 1024) {
				alert("提示信息\n上传图片过大，请将图片压缩到 " + maxSize + "kb 以下");
				uploadInput.value = '';
				return;
			}

			reader.readAsDataURL(file);
		} else {
			// ie@todo
		}
	});

	var MAXWIDTH = 300, MAXHEIGHT = 150;

	perviewImg.onload = function(e){
		var rect = clacImgZoomParam(MAXWIDTH, MAXHEIGHT, perviewImg.offsetWidth, perviewImg.offsetHeight);
	    perviewImg.width = rect.width;
	    perviewImg.height = rect.height;
	    //   img.style.marginLeft = rect.left+'px';
	    //   img.style.marginTop = rect.top+'px';
	}
	
	/**
	 * 扩展名的检测，看看是否为图片
	 * @return false = 你选择的不是图片哦
	 */
	function checkImgByExtName(filename) {
		var ext = filename.split('.').pop();
		return /png|gif|jpg|jpeg/i.test(ext);
	}
	
	/**
	 * 文件头判别，看看是否为图片
	 */
	function checkImgByBin(base64_str) {
		for ( var i in checkImgByBin.header) {
			if (~base64_str.indexOf(checkImgByBin.header[i]))
				return true;
		}
		return false;
	}
	
	checkImgByBin.header = {
		"jpeg" : "/9j/4",
		"gif" : "R0lGOD",
		"png" : "iVBORw"
	};

	function sizeLimit(img, maxSize) {
		if (img.width > maxSize.width || img.height > maxSize.height) {
			alert('图片大小尺寸不符合要求哦，请重新图片吧~');
			return false;
		}

		return true;
	}

	/*
	实现本地图片等比例缩放预览 
	 * 	 
	 */
	function clacImgZoomParam(maxWidth, maxHeight, width, height){
	    var param = {
	        top : 0, left : 0, width : width, height : height
	    };
	    if (width > maxWidth || height > maxHeight){
	        rateWidth = width / maxWidth;
	        rateHeight = height / maxHeight;
	        
	        if(rateWidth > rateHeight){
	            param.width =  maxWidth;
	            param.height = Math.round(height / rateWidth);
	        }else{
	            param.width = Math.round(width / rateHeight);
	            param.height = maxHeight;
	        }
	    }
	    
	    param.left = Math.round((maxWidth -  param.width)  / 2);
	    param.top  = Math.round((maxHeight - param.height) / 2);
	    return param;
	}
}


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
	ajaxjs.SimpleTab.prototype.jump = function(index){
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
			
			isWithAnimated && this.btn.classList.add('withAnimated');
			
			this.btn.setLeft(left);
			
			var btn = this.btn;
			isWithAnimated && setTimeout(function(){
				btn.removeCls('withAnimated');
			}, 500);
		}
		
		sliderEl.onClk(onSliderClick.bind(this));
	}
})();