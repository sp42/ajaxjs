// 折叠菜单
Vue.component('aj-accordion-menu', {
	template : '<ul class="aj-accordion-menu" @click="onClk($event);"><slot></slot></ul>',
	methods : {
		onClk : function (e) {
			this.children = this.$el.children;
			
			this.highlightSubItem(e);
	        
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
	    },
	    
	    // 内部子菜单的高亮
	    highlightSubItem : function (e) {
	        var li, el = e.target;
	        if (el.tagName == 'A' && el.getAttribute('target')) {
	            li = el.parentNode;
	            li.parentNode.$('li', function(_el) {
	                if (_el == li)
	                    _el.classList.add('selected');
	                else
	                    _el.classList.remove('selected');
	            });
	        }
	    }
	}
});

// 展开闭合器
Vue.component('aj-expander', {
	data : function () {
	    return {
	    	expended : false
	    }
	},
	
	props : {
		openHeight : { 		// 展开状态的高度
			type : Number,
			default : 200
		},
		closeHeight : {		// 闭合状态的高度
			type : Number,
			default : 50
		}
	},
	
	template : 
		'<div class="aj-expander" :style="\'height:\' + (expended ? openHeight : closeHeight) + \'px;\'">\
			<div :class="expended ? \'closeBtn\' : \'openBtn\'" @click="expended = !expended;"></div>\
			<slot></slot>\
		</div>'
});