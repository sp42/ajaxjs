/**
 * 消息框、弹窗、对话框组件
 */
document.addEventListener("DOMContentLoaded", function() {
	document.body.appendChild(document.createElement('div')).className = 'alertHolder';
	
	// 全屏幕弹窗，居中显示文字。
	aj.alert = new Vue({
		el : '.alertHolder',
		data : {
			showText : '', 		// 显示的内容
			afterClose : null	// 关闭弹窗后的回调
		},
		template : '<div class="modal hide" @click="close($event);"><div>{{showText}}</div></div>',
		methods : {
			show : function(text, cfg) {
				this.showText = text;
				this.$el.classList.remove('hide');
				
				if(cfg && cfg.afterClose)
					this.afterClose = cfg.afterClose;
			},
			close : function(e) {
				var div = e.target; // check if in the box
				if (div && div.className.indexOf('modal') != -1) {
					this.$el.classList.add('hide');
					
					this.afterClose && this.afterClose(div, this);
				}
			}
		}
		
	});
		
	document.body.appendChild(document.createElement('div')).className = 'msgHolder';

	// 顶部出现，用于后台提示信息多
	aj.msg = new Vue({
		el : '.msgHolder',
		data : {
			showText : '' // 显示的内容
		},
		template : '<div class="topMsg">{{showText}}</div>',
		methods : {
			show : function(text, cfg) {
				this.showText = text;
				var el = this.$el;
				
				setTimeout(function() {
					el.classList.remove('fadeOut');
					el.classList.add('fadeIn');
				}, 0);
				
				setTimeout(function() { // 自动隐藏，无须 close
					el.classList.remove('fadeIn');
					el.classList.add('fadeOut');
					cfg && cfg.afterClose && cfg.afterClose(div, this);
				}, cfg && cfg.showTime || 3000);
			}
		}
	});
});
