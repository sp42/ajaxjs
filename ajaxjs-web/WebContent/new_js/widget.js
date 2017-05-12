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
        
        //initDD();
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

    function initDD() {
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

            var dd = Object.create(bf_touch), dialogStyled = msgbox.style;

            // box 左端 到 鼠标 x 坐标之间的距离，
            // 应由 onmousedown 那一刻，记录 距离，
            // 不要放在 onMoving 里，不然会 一跳 一跳
            // another way
            var boxLeft = msgbox.getBoundingClientRect().left,
                diff = e.screenX - boxLeft;

            dd.onMoving = function(e, data) {
                dialogStyled.left = (e.screenX - diff) + 'px';
                // dialogStyled.left = (data.x - dialog.clientWidth / 2) + 'px';
                dialogStyled.top = data.y + 'px';
            }

            dd.el = parent.document; // 可拖放的范围，documement 表示整张桌布

            dd.init();
        }
    }

}

// 优化 
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

						if(typeof obj == 'function'){
							obj();
						}else if(typeof obj.fn == 'function' && !obj.executeOnce){
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



UserEvent2 = {};
UserEvent2.onWinResizeFree = function(){};
UserEvent2.onWinResizeFree.init = {};
UserEvent2.onWinResizeFree.handler = [];

UserEvent2.onEl_in_viewport = function(el, fn) {
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

UserEvent2.onEl_in_viewport.actions = [];

;(function(){
	// 回到顶部
	var timer = null;
	var b = 0;//作为标志位，判断滚动事件的触发原因，是定时器触发还是其它人为操作
	UserEvent2.onWinResizeFree(function(e) {
		if (b != 1) clearInterval(timer);
		b = 2;
	}, 'scroll');
	
	window.goTop = function () {
		clearInterval(timer);
		var iCur = speed = 0;
		
		timer = setInterval(function() {
			iCur = document.documentElement.scrollTop || document.body.scrollTop;
			speed = Math.floor((0 - iCur) / 8);
			
			if (iCur === 0) 
				clearInterval(timer);
			else 
				document.documentElement.scrollTop = document.body.scrollTop = iCur + speed;
			b = 1;
		}, 30);
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
                        btn.removeCls('pressed'); // 再次点击，隐藏！
                        if (ul)
                            ul.style.height = '0px';
                    } else {
                        if (ul)
                            ul.style.height = ul.scrollHeight + 'px';
                        btn.addCls('pressed');
                    }
                } else {
                    btn.removeCls('pressed');
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
                    _el.addCls('selected');
                else
                    _el.removeCls('selected');
            });
        }
    };
})();


/*
 * --------------------------------------------------------
 * HTML Editor
 * --------------------------------------------------------
 */
ajaxjs.HtmlEditor = function(el){
	this.iframeEl 	  = el.querySelector('iframe');
	this.sourceEditor = el.querySelector('textarea');
	this.iframeWin 	  = this.iframeEl.contentWindow;
	this.mode         = 'iframe'; // 当前可视化编辑 iframe|textarea
	
	this.toolbarEl    = el.querySelector('.toolbar');
	this.toolbarEl.onclick = function(e) {
		var el = e.target, clsName = el.className;
		
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
				this.format(el.className);
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
		this.iframeBody.innerHTML = v; 
	}
	
	this.getValue = function() {
		return this.iframeBody.innerHTML;
	}
	
	this.setMode = function () {
		if (this.mode == 'iframe') {
			this.iframeEl.addCls('hide');
			this.sourceEditor.removeCls('hide');
			this.sourceEditor.value = this.iframeBody.innerHTML;
			this.mode = 'text';
			grayImg(this.toolbarEl, true);
		} else {
			this.iframeEl.removeCls('hide');
			this.sourceEditor.addCls('hide');
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
 * 无缝上下滚动
 * 鸣谢原作者： cloudgamer
 * --------------------------------------------------------
 */
ajaxjs.MarqueeText = {
	init : function(el) {
		this.el = el;
		this.boxHeight = el.offsetHeight;
		
		if (getComputedStyle(el).overflow != 'hidden') // 强制设置不溢出
			el.style.overflow = "hidden";

		var listEl = el.qs('div');
		listEl.onmouseover = this.stop.bind(this);
		listEl.onmouseout  = this.scroll.bind(this);

		this.listHeight = listEl.offsetHeight;

		// autoPauseHeight.call(this, oScroller);
		if (this.listHeight <= this.boxHeight) {
			console.warn('父容器有足够高度显示，无须滚动，有时可能因为数据源只有一条记录返回，也就是无须上下滚动了');
			return;
		}

		el.appendChild(listEl.cloneNode(true)); // 复制多一份

		this.pause = 0;
	},
	// 设置默认属性
	side : 1,
	// 滚动方向1是上 -1是下
	step : 1,
	// 每次变化的px量
	time : 20,
	// 速度(越大越慢)
	pauseHeight : 25,
	// 隔多高停一次
	pauseStep : 1000,
	// 停顿时间(PauseHeight大于0该参数才有效)

	// 开始滚动
	scroll : function() {
		var iScroll = this.el.scrollTop, time = this.time, iStep = this.step * this.side;

		if (this.side > 0) { 
			if (iScroll >= (this.listHeight * 2 - this.boxHeight))
				iScroll -= this.listHeight;
		} else {
			if (iScroll <= 0)
				iScroll += this.listHeight;
		}

		if (this.pauseHeight > 0) {
			if (this.pause >= this.pauseHeight) {
				time = this.pauseStep;
				this.pause = 0;

			} else {
				this.pause += Math.abs(iStep);
				this.el.scrollTop = iScroll + iStep;
			}
		} else {
			this.el.scrollTop = iScroll + iStep;
			console.log('never?');
		}
		if(this.el.scrollTop ==51)
		console.log(this.el.scrollTop);
		this.timer = window.setTimeout(this.scroll.bind(this), time);
	},

	// 停止
	stop : function() {
		window.clearTimeout(this.timer);
	}
};

function autoPauseHeight(ul) {
	var li = ul.qs('li'), lineHeight = getComputedStyle(ul.qs('li')).lineHeight;

	// 有设置 line-height
	if (lineHeight && lineHeight != 'normal')
		lineHeight = parseInt(lineHeight);

	if (!isNaN(lineHeight))
		this.pauseHeight = lineHeight;
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
				btn.addCls(onPressed_ClassName);
				showTab.addCls(onPressed_ClassName);
				this.currentIndex = i; // 保存当前游标

				if (this.afterSwitch && typeof this.afterSwitch == 'function')
					this.afterSwitch(i, btn, showTab);

				// 是否已经渲染
				var isRendered_marked = ~showTab.className.indexOf('rendered');
				if (!isRendered_marked)
					showTab.addCls('rendered');
				if (!isRendered_marked && this.afterRender && typeof this.afterRender == 'function')
					this.afterRender(i, btn, showTab);
			} else if (btn == el && btn.className.indexOf(onPressed_ClassName) != -1) {
				// 已在当前项
			} else if (btn.className.indexOf(onPressed_ClassName) != -1) {
				btn.removeCls(onPressed_ClassName);
				showTab.removeCls(onPressed_ClassName);
			}
		}
	}
})();