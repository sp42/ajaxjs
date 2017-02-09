/**
 * 折叠菜单
 */
;(function() {
	bf_acMenu = function(ul){
		this.ul = ul;
		this.children = ul.children;
		ul.addEventListener('click', onClk.bind(this));
		ul.addEventListener('click', highlightSubItem);
	}

	function onClk(e){
		var _btn = e.target;
		if(_btn.tagName == 'H3' && _btn.parentNode.tagName == 'LI'){
			_btn = _btn.parentNode;

			for(var btn, i = 0, j = this.children.length; i < j; i++){
				btn = this.children[i];
				var ul = btn.querySelector('ul');

				if(btn == _btn){
					if(btn.className.indexOf('pressed') != -1){
						btn.removeCls('pressed'); // 再次点击，隐藏！
						if(ul)ul.style.height = '0px';
					}else{
						if(ul)ul.style.height = ul.scrollHeight + 'px';
						btn.addCls('pressed');
					}
				}else {
					btn.removeCls('pressed');
					if(ul)ul.style.height = '0px';
				}
			}
		}else{
			return;
		}
	}
	
	// 内部子菜单的高亮
	function highlightSubItem(e){
		var li, el = e.target;
		if(el.tagName == 'A' && el.getAttribute('target')){
			li = el.parentNode;
			li.parentNode.eachChild('li', function(_el){
				if(_el == li)_el.addCls('selected');else _el.removeCls('selected');
			});
		}
	};
})();