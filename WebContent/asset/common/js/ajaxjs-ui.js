// 查找元素
aj = function(cssSelector, fn){
	if(fn) {
		var arr = document.querySelectorAll(cssSelector);
		if(arr) {
			for(var i = 0, j = arr.length; i< j; i++) {
				fn(arr[i], i, j, arr);
			}
		}
	} else {
		return document.querySelector(cssSelector);
	}
}
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
		var h =  cfg.text || '';
		if(cfg.isShowCloseBtn)
			h += '<a href="#" oncclick="ajaxjs.modal()">关 闭</a>'
		div.innerHTML = '<div style="text-align:center;">' + h + '</div>';
		div.onclick = ajaxjs.modal;
		document.body.appendChild(div);
		element = div;
	}
	

	//element.setAttribute('onclick', "ajaxjs.modal(arguments[0], {el:'." + el + "'});");
	
	if (e) {
		var p = e.target; // check if in the box
		if (p && p.className.indexOf(el.replace('.', '')) != -1)
			element.classList.toggle('hide');
	} else {
		element.onclick = ajaxjs.modal.delegate(null, cfg); 
		element.classList.toggle('hide');
	}
}

ajaxjs.alert = function(text, cfg) {
	var div = document.createElement('div');
	div.className = 'modal';
	
	if(cfg && cfg.isShowCloseBtn)
		text += '<a href="#" onclick="ajaxjs.modal()">关 闭</a>'
	div.innerHTML = '<div style="text-align:center;">' + text + '</div>';
	div.onclick = function(e) {
		var p = e.target; // check if in the box
		if (p && p.className.indexOf('modal') != -1) {
			p.die();
			cfg && cfg.afterClose && cfg.afterClose();
		}
	}
	
	document.body.appendChild(div);
}

ajaxjs.layer = function(el, cfg) {
	el = typeof(el) == 'string' ? document.querySelector(el) : el;
	ajaxjs.alert(el.value, cfg);
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
 * 基于 HTML5 增强的表单验证
 */
ajaxjs.formValid = function FormValid(formEl, cfg) {
	this.cfg = cfg;
	formEl = typeof formEl == 'string' ? document.querySelector(formEl) : formEl;
	var items = formEl.querySelectorAll('input[type=text], input[type=password], input[type=number]');

	for(var i = 0 , j = items.length; i < j; i++) {
		var el = items[i];
		el.oninvalid = this.onInvalid.bind(this);
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
		if(this.cfg && this.cfg.alignRight) { // 右对齐
			msg.style.top = (b.top + 5) + 'px';
			var moveLeft = 0;
			if(el.dataset.erruileft)
				moveLeft = Number(el.dataset.erruileft);
			msg.style.left = (b.left + el.clientWidth + moveLeft + 10) +  'px';
		} else {
			msg.style.left = b.left + 'px';
			msg.style.top = (b.top + 28) + 'px';
			
		}
		
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
		msg.die();
		el.style.borderColor = '#abadb3';
	}, 3000);
	
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



// Tree-like option control
;(function() {
	
	/*
	 * 参考结构
	var map = {
		a : 1,
		b : 2,
		c : {
			children : [ {
				d : 3
			} ]
		}
	};
	*/
	
	ajaxjs.tree = function() {
		this.initData();
	};
	
	ajaxjs.tree.prototype = {
		// 重置数据
		initData : function() {
			this.stack = [];
			this.tree = {};
		},
		
		// 生成树
		makeTree : function (jsonArray) {
			jsonArray.sort(sortByPid);// 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故先排序
			
			for (var i = 0, j = jsonArray.length; i < j; i++) {
				var n = jsonArray[i];

				var parentNode = findParent(this.tree, n.pid);
				if (parentNode == null) { // 没有父节点，那就表示这是根节点，保存之
					this.tree[n.id] = 	{ // id 是key，value 新建一对象
						name : n.name,
						pid : n.pid
					};				
				} else { // 有父亲节点，作为孩子节点保存
					var obj = {};
					obj[n.id] = {
						name : n.name,
						pid : n.pid
					};
					if (!parentNode.children)
						parentNode.children = [];

					parentNode.children.push(obj);
				}
			}
		},
		
		// 遍历各个元素，输出
		output : function (map, cb) {
			this.stack.push(map);
			
			for (var i in map) {
				map[i].level = this.stack.length;
				cb(map[i], i);
				
				var c = map[i].children;
				if (c) {
					for (var q = 0, p = c.length; q < p; q++) 
						this.output(c[q], cb);
				}
			}
			
			this.stack.pop();
		}
	}
	
	// 递归查找父亲节点，根据传入 id
	var findParent = function (map, id) {
		for ( var i in map) {
			if (i == id)
				return map[i];
			
			var c = map[i].children;
			if (c) {
				for (var q = 0, p = c.length; q < p; q++) {
					var result = arguments.callee(c[q], id);
					if (result != null)
						return result;
				}
			}
		}

		return null;
	}
	
	// Chrome sucks
	function sortByPid(a, b) {
		return[a.pid, a.pid] > [b.pid, b.pid] ? 1:-1;;
	}
	
	ajaxjs.tree.selectUI = function() {
		ajaxjs.tree.call(this);
		
		
		/**
		 * 渲染 DOM
		 */
		this.renderer = function (json, select, selectedId) {
			selectUI.makeTree(json);
			
			// 生成 option
			var temp = document.createDocumentFragment();

			this.output(this.tree, function(node, nodeId) {
				var option = document.createElement('option'); // 节点
				option.value = nodeId;
				
				if(selectedId && selectedId == nodeId) { // 选中的
					option.selected = true;
				}
				
				option.dataset['pid'] = node.pid;
				//option.style= "padding-left:" + (node.level - 1) +"rem;";
				option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
				temp.appendChild(option);
				//console.log(node);
			});
			
			select.appendChild(temp)
		}
	}
	
	ajaxjs.tree.selectUI.prototype = ajaxjs.tree.prototype;
	
})();