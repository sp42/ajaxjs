/**
 * 利用最快出错即最快响应站点的做法，实现多线路、多镜像下选择最快的速度。
 * 参数 sites 形如：
	var sites = [];
	autourl[1] = "http://www.cidianwang.com"
	autourl[2] = "http://www.aspxhome.com"
	autourl[3] = "http://www.llkyx.com"
	autourl[4] = "http://tool.aspxhome.com"
	autourl[5] = "http://baike.aspxhome.com"
	autourl[6] = "http://yohoo.com/intels"
 * @param {Array} sites
 */
$$.dhtml.speedDetect = function(sites){
	var img = '<img src="{0}" class="hide" onerror="$$.dhtml.speedDetect.skip = true;top.location(\"{0}\");" />';
	for(var i = 0, j = sites.length; i < j; i++){
		document.write(img.format(sites[i]));
	}
}
$$.dhtml.speedDetect.skip = false; // 为避免过多循环的而设的全局变量。


	/**
	 * 打印。
	 */
function print(){
	var btns = Ext.select('.edk-printBtn');
	btns.elements.length && btns.on('click', function(){
		if(this.length > 0){
			var win = window.open("", "打印窗体……");
			
			var html = '<center><h1>{0}</h1></center>'.format(this[0][0].innerHTML);
			html += this[1][0].innerHTML;
			
			win.document.write(html + "<script>window.print();</script>");
			win.location.reload();
		}
	}, [Ext.query('.edk_contentPanel h1'), Ext.query('.edk_content div.contentBody')]);	
}

/**
 * 加入onCopy的事件，在用户复制的时候，强行加入声明版权。
 * FF 通过 Flash插件实现复制功能。
 */
function onCopyNote(){
	document.body.oncopy = function(e){
		var text;
		if($$.isIE){
			text =  document.selection.createRange();
			text = text.text;
		}else if($$.isFF){
			text = window.getSelection();
			text = text.toString();
		}

		text += "\r\n转载请保留本站{0}的链接，谢谢你的举手之劳！出处：{0}".format(location.href);
		
		(e || event).returnValue = false;
		$$.isIE && clipboardData.setData('Text' , text);
	}
}

/**
 * 禁止选择形成可复制的选区。
 */
function unSelectable(){
	if($$.isIE){
		document.onselectstart = function(){
			var el = event.srcElement;
			if (!((el.tagName == "INPUT" && el.type.toLowerCase() == "text") || el.tagName == "TEXTAREA"|| el.tagName == "SELECT")){
				return false;
			}
			return true;
		}
	}else if($$.isFF){
		Ext.onReady(function(){
			document.getElementsByTagName('body')[0].style.MozUserSelect = 'none';
		});
		Ext.fly(document).on('mousedown', function(e){
			var bodyEl = document.getElementsByTagName('body')[0];
			var isFormEl = /(input|textarea|select)/ig.test(e.target.tagName)
			bodyEl.style.MozUserSelect = isFormEl ? 'text' : 'none';
		});
	}
}
	

/**
 * 防止误退出。
 * @param {Boolean} giveWarnWhenRefreshinIE True 表示为免除刷新时会提示的影响。
 */
function noExit(preventGivingWarnWhenRefreshinIE){
	var leave = function(){
		return "提示：\“退出系统\”请点系统的\“安全退出\”!";
	}

	if(preventGivingWarnWhenRefreshinIE){
		//只有点右上角 X 的时候弹出提示,让用户确认(是否关闭)
		//解决了 onbeforeunload() 函数在刷新页面也弹出的问题)
		//而且无需再 body 中加载
		window.onbeforeunload = function(){
			//?event.clientX>   document.body.clientWidth-20   &&   event.clientY   <   0
			if (event.clientX > 360 && event.clientY < 0) {
				window.event.returnValue = $$.leaveWarning.Text
		    }
		}
		if(!$$.isIE){
			window.onbeforeunload = leave;
		}
		return 0;
	}
	window.onbeforeunload = leave;
	// 使 BackSpace、F5 等键无效
	typeof Ext == 'undefined' && alert('该函数依赖于 Ext Core 库！');
	Ext.getBody().on('keydown', function (e) {
	    // [Alt] + [←]
	    if (e.altKey && (e.keyCode == 37)) {
	        e.stopEvent();
	    }
	    // [Alt] + [→]
	    if (e.altKey && (e.keyCode == 39)) {
	        e.stopEvent();
	    }
	    // [F5]
	    if (e.keyCode == 116) {
	        if (Ext.isIE) e.browserEvent.keyCode = 0;
	        e.stopEvent();
	    }
	    // [Ctrl] + [R]
	    if (e.ctrlKey && (e.keyCode == 82)) {
	        e.stopEvent();
	    }
	    // [Esc]
	    if (e.keyCode == 27) {
	        e.stopEvent();
	    }
	    // [Backspace]
	    if (e.keyCode == 8
	            && ((e.getTarget().type != 'text'
	                && e.getTarget().type != 'textarea'
	                && e.getTarget().type != 'password')
	                || e.getTarget().readOnly)) {
	        e.stopEvent();
	    }
	});
}
