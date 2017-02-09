bf_EditHTML = function(el){
	this.iframeEl 	  = el.querySelector('iframe');
	this.sourceEditor = el.querySelector('textarea');
	this.iframeWin 	= this.iframeEl.contentWindow;
	this.mode = 'iframe'; // 当前可视化编辑 iframe|textarea
	
	this.toolbarEl = el.querySelector('.toolbar');
	this.toolbarEl.onclick = function(e){
		var el = e.target, clsName = el.className;
		switch(clsName){
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
		if (!para){
			if (document.all) this.iframeDoc.execCommand(type);
			else this.iframeDoc.execCommand(type, false, false);
		}else this.iframeDoc.execCommand(type, false, para);
//		this.iframeWin.focus();
	}
	
	this.setValue = function(v){
		this.iframeBody.innerHTML = v; 
	}
	
	this.getValue = function(){
		return this.iframeBody.innerHTML;
	}
	
	this.setMode = function () {
		if (this.mode == 'iframe') {
			this.iframeEl.addCls('hide');
			this.sourceEditor.removeCls('hide');
			this.sourceEditor.value = this.iframeBody.innerHTML;
			this.mode = 'text';
		} else {
			this.iframeEl.removeCls('hide');
			this.sourceEditor.addCls('hide');
			this.iframeBody.innerHTML = this.sourceEditor.value;
			this.mode = 'iframe';
		}
	}
	
	el.querySelector('.fontfamilyChoser').onclick = function(e){
		var el = e.target; 
		
		this.format('fontname', el.innerHTML);
		// 如何解决点击之后马上隐藏面板？由于 js（单击事件） 没有控制 CSS 的 :hover 伪类的方法，故所以必须使用以下技巧：
		var menuPanel = el.parentNode;
		menuPanel.style.display = 'none';
		setTimeout(function(){
			menuPanel.style.display = '';
		}, 300);
	}.bind(this);

	el.querySelector('.fontsizeChoser').addEventListener('click', function(e){	
		var el = e.target;
		for(var els = e.currentTarget.children, i = 0, j = els.length; i < j; i++)
			if(el == els[i]) break;
		
		this.format('fontsize', i);
	}.bind(this));
	
	// 这个方法只能写在 onload 事件 不写 onload 里还不执行
	this.iframeWin.onload = function(){
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
bf_EditHTML.createColorPickerHTML = function(){
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