// 关闭窗体
function closeDlg(){
	var msgbox = document.querySelector('.msgbox');
	msgbox.parentNode.removeChild(msgbox);
	var msgbox_mask = document.querySelector('.msgbox_mask');
	if(msgbox_mask)
		msgbox_mask.parentNode.removeChild(msgbox_mask);
}

function showDlg(cfg) {
	// 读取模板
	var holder = document.createElement('div');
	holder.innerHTML = document.querySelector('.msgboxTpl').value;
	document.body.appendChild(holder);
	
	var msgbox = document.querySelector('.msgbox');
	
	if(cfg){
		if(cfg.hideYES_NO){
			var btn = msgbox.querySelector('.btn');
			btn.removeChild(msgbox.querySelector('.btn .yesAction'));
			btn.removeChild(msgbox.querySelector('.btn .noAction'));
		}
		if(cfg.hideClose){
			var btn = msgbox.querySelector('.btn');
			btn.removeChild(msgbox.querySelector('.btn .closeAction'));
		}
	}
	// 获取页面内容高度赋予 mask
	document.querySelector('.msgbox_mask').style.height = document.body.scrollHeight + 'px';
	// 计算居中
	msgbox.style.left = (window.innerWidth / 2 - msgbox.clientWidth / 2) + 'px';
	msgbox.style.top = (window.innerHeight / 2 - msgbox.clientHeight / 2) + 'px';
	
	// 平均分按钮宽度
	var btn = msgbox.querySelector('.btn'), btns = msgbox.querySelectorAll('.btn>div');
	var j = btns.length, width = btn.clientWidth / j - 12; // 要减去间隙
	
	if(j == 1){
		// 居中

	} else {
		for(var i = 0; i < j; i ++) {
			btns[i].style.marginRight = '5px'; // 添加间隙
			btns[i].style.width = width + 'px';
		}
	}
	// 登记关闭事件
	function closeAction(){
		if(cfg && cfg.beforeClose){
			if(cfg.beforeClose() === false){
				return; // 退出不执行下一步
			}
		} 
		closeDlg();
		cfg && cfg.afterClose && cfg.afterClose();
	}
	
	[].forEach.call(msgbox.querySelectorAll('.closeAction'), function(closeBtn){
		closeBtn.onclick = closeAction;
	});
	  		// 键盘事件
	function onEnterAndEsc(e){
       e = e || event;
        var keycode = e.which || e.keyCode;
        switch(keycode){  
            case 13: //enter   
            	// 如果 form 里有 action，按下回车自动提交
         /*    	var formEl = this.el.$('form');
            	if(formEl && formEl.action){
            		formEl.submit();
            	} */
            break;
            case 339: //exit   
            case 340: //back    
            case 27:
            	closeAction();
        } 
	}
	  		
	document.onkeydown = onEnterAndEsc;
	
	// 
	if(cfg){
		if(cfg.innerText)
			msgbox.querySelector('.inner').innerHTML = cfg.innerText;
		if(cfg.title)
			msgbox.querySelector('h1').innerHTML = cfg.title;
		if(cfg.closeAsConfirm)
			msgbox.querySelector('.btn .closeAction').innerHTML = '确定';
		if(cfg.yesHandler)
			msgbox.querySelector('.btn .yesAction').onclick = cfg.yesHandler;
		if(cfg.noHandler)
			msgbox.querySelector('.btn .noAction').onclick = cfg.noHandler;
	}
	
	function initDD(){
		document.onselectstart = function(e) { return false; }
		// ，代码就把Dialog的left和top设为了鼠标当前位置，可是用户在拖动的时候不会刻意去点Dialog的左上角，这样就跳了，soga！改进一下
		// http://www.cnblogs.com/dolphinX/p/3290520.html
		// http://www.cnblogs.com/dolphinX/p/3293455.html
		 msgbox.onmousedown = (function(e){
			if(e.target.tagName != 'H1')return;
			e.preventDefault();
			var dd = Object.create(bf_touch), dialogStyled = msgbox.style;
			
			// box 左端 到 鼠标 x 坐标之间的距离，
			// 应由 onmousedown 那一刻，记录 距离，
			// 不要放在 onMoving 里，不然会 一跳 一跳
			// another way
			var boxLeft = msgbox.getBoundingClientRect().left,
				diff = e.screenX - boxLeft;
			dd.onMoving = function(e, data){
				dialogStyled.left = (e.screenX - diff) + 'px';
				// dialogStyled.left = (data.x - dialog.clientWidth  /  2) + 'px';
				dialogStyled.top = data.y + 'px';
			}
			dd.el = parent.document; // 可拖放的范围，documement 表示整张桌布
			dd.init();
		});
	}
	initDD();
}

function showWarningDlg(innerText){
	showDlg({
		innerText : '<div class="leftIcon warning">!</div>' + innerText,
		title : '警告',
		hideYES_NO : true
	});
}
function showSuccessDlg(innerText){
	// ✓ &#10003; &#x2713; ✔&#10004;&#x2714; 
	showDlg({
		innerText : '<div class="leftIcon success">✔</div>' + innerText,
		title : '完成',
		closeAsConfirm : true,
		hideYES_NO : true
	});
}
function showQueryDlg(innerText){
	// ✓ &#10003; &#x2713; ✔&#10004;&#x2714; 
	showDlg({
		innerText : '<div class="leftIcon query">?</div>' + innerText,
		title : '询问',
		hideClose : true,
		yesHandler : function(){
			alert('yes');
		},
		noHandler : function(){
			alert('no');
		}
	});
}