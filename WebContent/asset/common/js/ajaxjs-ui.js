/**
 * @param e
 *            事件对象
 * @param el
 *            元素
 */
ajaxjs.modal = function(e,  cfg) {
	var el = cfg && cfg.el || '.modal';
	var element = document.querySelector(el);
	
	if(!element) {
		var div = document.createElement('div');
		div.className = 'modal hide';
		var h =  cfg.text;
		if(cfg.isShowCloseBtn)
			h += '<a href="#" oncclick="ajaxjs.modal()">关 闭</a>'
		div.innerHTML = '<div style="text-align:center;">' + h + '</div>';
		div.onclick = ajaxjs.modal;
		document.body.appendChild(div);
		element = div;
	}
	
	if (e) {
		var p = e.target.parentNode; // check if in the box
		if (p && p.className.indexOf(el) == -1)
			element.classList.toggle('hide');
	} else {
		element.classList.toggle('hide');
	}
}

ajaxjs.msg = function (text, showTime) {
	var el = document.querySelector('.topMsg');
	if(!el){
		var div = document.createElement('div');
		div.className = 'topMsg';
		document.body.appendChild(div);
		el = div;
	}
	
	if (text)
		el.innerHTML = text;
	
	setTimeout(function() {
		el.classList.remove('fadeOut');
		el.classList.add('fadeIn');
	}, 0);
	
	setTimeout(function() {
		el.classList.remove('fadeIn');
		el.classList.add('fadeOut');
	}, showTime || 3000)
}

/**
 * 表单验证
 */
ajaxjs.formValid = function FormValid(formEl) {
	var items = formEl.querySelectorAll('input[type=text]');

	for(var i = 0 , j = items.length; i < j; i++) {
		var el = items[i];
		el.oninvalid = this.onInvalid;
	}
}

ajaxjs.formValid.prototype.onInvalid = function (e) {
	e.preventDefault();
	var el = e.target;
	el.style.borderColor = 'red';
	
	// 新增错误提示的容器
	var msg = el.parentNode.querySelector('.errMsg');
	if(!msg) {
		msg = document.createElement('div');
		msg.className = 'errMsg';
		
		var b = el.getBoundingClientRect();
		msg.style.left = b.left + 'px';
		msg.style.top = (b.top + 25) + 'px';
		
		el.insertAfter(msg);
	}
	
	var m;
	if(el.validity.patternMismatch)
		m = '该内容格式不正确';
	if(el.validity.valueMissing)
		m = '该内容不可为空';
	//debugger;
	
	msg.innerHTML = m;
	
	setTimeout(function() {
		//msg.die();
	}, 2000);
	
	//el.setCustomValidity(undefined);
}

/*
 * --------------------------------------------------------
 * 简单选项卡控件
 * --------------------------------------------------------
 */
;(function() {
	ajaxjs.SimpleTab = function(el) {
		this.el = el;
		var ul = el.querySelector('ul');
		this.buttons = ul.children, // tab候选栏strip
		this.tabs = el.querySelector('.content').children;
		ul.onclick = onTabChooserPressHandler.bind(this);
	}
	
	/**
	 * 跳到指定的 tab，仿佛好象点击那样
	 * @param {int} index
	 */
	ajaxjs.SimpleTab.prototype.jump = function(index) {
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