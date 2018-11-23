/**
 * @class App UI 类
 */
ajaxjs.appFrame = function() {
	var bodyEl = aj('.body');

	var PANEL_STACK = [];
	PANEL_STACK.push(aj('.body > .active')); // 首页入栈
	
	this.isOnlyOne = function() {
		return PANEL_STACK.length == 1;
	}

	// 锁定宽度防止 过场动画 压扁
	var lockWidth = document.querySelectorAll('.lockWidth');
	for (var i = 0, j = lockWidth.length; i < j; i++)
		lockWidth[i].style.width = window.innerWidth + 'px';

	// 点击下方导航 Tab
	if (aj('footer ul'))
		aj('footer ul').onclick = onTabClick;

	function onTabClick(e) {
		var el = e.target, li = el.tagName == 'LI' ? el : el.up('li');

		var toShowIndex = getTabItemIndex(li);
		var activeBtn = li.parentNode.$('.active');

		if (toShowIndex == getTabItemIndex(activeBtn))
			return; // 在当前页面 无须跳转

		li.classList.add('active'); // 按钮高亮
		activeBtn.classList.remove('active');

		PANEL_STACK = []; // 重新设置
		hideCurrentActivePanel();
		showPanel(bodyEl.children[toShowIndex]);
	}

	/**
	 * 获取 tab 按钮的索引
	 * 
	 * @param {Element}
	 *            li LI 元素
	 * @returns {Number}
	 */
	function getTabItemIndex(li) {
		return Number(li.className.match(/\d/)[0]);
	}

	/**
	 * 显示某个面板
	 * 
	 * @param {String}
	 *            id 面板 class
	 * @returns void
	 */
	this.pushPanel = function(id) {
		hideCurrentActivePanel();
		showPanel(bodyEl.$(id));
	}

	/**
	 * 后退
	 * 
	 * @param {Object}
	 *            cfg 配置
	 * @returns void
	 */
	this.popPanel = function(cfg) {
		var active = aj('.body > .active');
		if(active.className.indexOf('iframePanel') != -1 && !active.$('iframe').contentWindow.AppFrame.isOnlyOne()) {
			active.$('iframe').contentWindow.AppFrame.popPanel();
		} else {		
			hideCurrentActivePanel(cfg && cfg.cleanHTML);
	
			PANEL_STACK.pop();
			var toShowPanel = PANEL_STACK[PANEL_STACK.length - 1]; // 取出最后一个元素
	
			showPanel(toShowPanel, true);
		}
	}

	/**
	 * 通过 XHR 插入页面，退栈时候会销毁 HTML 节省资源
	 * 
	 * @param {String}
	 *            url 页面地址
	 * @returns void
	 */
	this.pushUrlPanel = function(url) {
		var div = document.createElement('div');

		ajaxjs.xhr.get(url, function(html) {
			div.innerHTML = '<div class="lockWidth">' + html + '</div>';
			renderTitle(div);
		}, null, {
			parseContentType : 'text'
		});

		hideCurrentActivePanel();
		div.className = 'urlPanel active';
		PANEL_STACK.push(div);
		bodyEl.appendChild(div);
	}

	this.pushIframePanel = function(url) {
		hideCurrentActivePanel();

		var div = document.createElement('div');
		div.className = 'iframePanel';
		div.innerHTML = '<div><iframe src="' + url + '" scrolling="yes"></iframe></div>';// div for iOS iframe height issue

		bodyEl.appendChild(div);
		showPanel(div);
	}

	/**
	 * @param {Element}
	 *            要显示的面板
	 * @param {Boolean}
	 *            isLastPanelInStack 是否最后一个元素，是的话就不用 push 新的
	 * @returns void
	 */
	function showPanel(toShowPanel, isLastPanelInStack) {
		if (!toShowPanel) {
			alert('找不到目标面板！');
			return;
		}

		renderTitle(toShowPanel);
		toShowPanel.classList.add('active');

		if (!isLastPanelInStack)
			PANEL_STACK.push(toShowPanel);
	}

	/**
	 * 隐藏当前显示的面板。某一个时刻只有一个活跃的面板
	 * 
	 * @param {Boolean}
	 *            isCleanHTML 是否清空 HTML
	 * @returns {Element} 面板元素
	 */
	function hideCurrentActivePanel(isCleanHTML) {
		var active = aj('.body > .active');

		if (isCleanHTML) {
			bodyEl.removeChild(active);// 清空 HTML
		} else {
			setTimeout(function() {// 当前面板也不要太快消失
				active.classList.remove('active');
				if(active.className.indexOf('urlPanel') != -1 || active.className.indexOf('iframePanel') != -1) {
					active.die();
				}
			}, 200);
		}

		return active;
	}

	/**
	 * 渲染顶部标题
	 * 
	 * @param {Element}
	 *            toShowPanel 要显示的面板
	 * @returns void
	 */
	function renderTitle(toShowPanel) {
		// 导航，为避免性能竞争，故押后执行
		setTimeout(function(header) {
			if(toShowPanel.className.indexOf('iframePanel') != -1) {
				var fn = arguments.callee;
				var win = toShowPanel.$('iframe').contentWindow;
				win.onload = function() {
					fn(win.aj('.active header.hide'));
				};
				
			} else
				header = header || toShowPanel.$('header.hide');
			
			if (header) {
				var nav = aj('body > nav');
				if (nav) {
					nav.innerHTML = header.innerHTML;
				} else if(parent.aj('body > nav')) { // for iframe
					parent.aj('body > nav').innerHTML = header.innerHTML;
				}
			} 
		}, 0);
	}
}