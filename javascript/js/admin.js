// 判断当前页面是否在iframe中 
if (self != top) {    
	parent.window.location.reload();
}  
// 收缩菜单
function hideSider(el){
	if(hideSider.isHidden){
		document.querySelector('.side').style.width = '20%';
		document.querySelector('.iframe').style.width = '80%';
		hideSider.isHidden = false;
		el.style.right = '0'; 
	}else{
		document.querySelector('.side').style.width = '0';
		document.querySelector('.iframe').style.width = '100%';
		hideSider.isHidden = true;
		el.style.right = '-'+ el.clientWidth +'px';
	}				
}

// 初始化菜单
new bf_acMenu(document.querySelector('.leftSidebar'));

// 需要把链接配置属性  target="iframepage"
;(function () {
	// 获取 #target=XXX 参数
	function getTarget() {
		var target = window.location.hash.match(/target=([^$]+)/);
		return target && target.pop();
	}
	
	// 展开菜单
	function highlightMenu(target) {
		var a = document.querySelector('a[href="' + target + '"]');
		if (a) {        	
			var li = a.up('li'), ul = li.up('ul');
			li.addCls('selected');
			ul.style.height = 'auto';
			ul.up('li').addCls('pressed');
		}
	}
	var prefix = location.origin + "", iframepageName = 'iframepage';
	
    var target = getTarget();
    var iframeEl = document.querySelector('iframe[name={0}]'.format(iframepageName));
    
    if(target) {
    	iframeEl.src = target; // 跳转 iframe
    	highlightMenu(target);
    }

    // 点击菜单时保存按钮
	document.body.eachChild('a[target={0}]'.format(iframepageName), function(a) {
        a.onclick = add_Hash;
    });
    
    function add_Hash(e) {
    	var target = new String(iframeEl.contentWindow.location);
    	var target = e.target.getAttribute('href');

    	window.location.assign('#target=' + target); // 为主窗体添加描点记录，以便 F5 刷新可以回到这里
        //alert(99)
    	return false; // onhashchange() 里面已经跳转了，这里避免 a 再次跳转
    }
    
    window.onhashchange = function(e) {
    	var target = getTarget();
    	iframeEl.src = target;
//    	highlightMenu(target);
    }
})();
	