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
			afterClose : null,	// 关闭弹窗后的回调
			showOk : false,
			showYes : false,
			showNo : false
		},
		template : 
			'<div class="aj-modal hide" @click="close($event);">\
				<div><div v-html="showText"></div>\
					<div>\
						<button v-show="showOk"  @click="onBtnClk($event)" class="ok">确定</button>\
						<button v-show="showYes" @click="onBtnClk($event)" class="yes">是</button>\
						<button v-show="showNo"  @click="onBtnClk($event)" class="no">否</button>\
					</div>\
				</div>\
			</div>',
		methods : {
			show : function(text, cfg) {
				this.showText = text;
				this.$el.classList.remove('hide');
				
				aj.apply(this, cfg);
				
				return this;
			},
			close : function(e) {
				var div = e.target; // check if in the box
				if (div && div.className.indexOf('modal') != -1) {
					this.$el.classList.add('hide');
					
					this.afterClose && this.afterClose(div, this);
				}
			},
			onBtnClk : function(e) {
				var el = e.target;
				switch(el.className) {
					case 'ok':
						this.onOkClk && this.onOkClk(e, this);
					break;
					case 'no':
						this.onNoClk && this.onNoClk(e, this);
						break;
					case 'yes':
						this.onYesClk && this.onYesClk(e, this);
						break;
				}
			},
			
		}
		
	});
	
	/**
	 * 顯示確定的對話框
	 * @param {String} text 显示的文本
	 * @param {Function} callback 回调函数
	 */
	aj.showOk = function(text, callback) {
		var alertObj = aj.alert.show(text, {
			showYes : false,
			showNo :false,
			showOk :true,
			onOkClk : function(e) { // 在box里面触发关闭，不能直接用 alertObj.close(e);
				alertObj.$el.classList.add('hide');
				callback && callback();
			}
		});
	}
	
	/**
	 * 顯示“是否”選擇的對話框
	 * @param {String} text 显示的文本
	 * @param {Function} callback 回调函数
	 */
	aj.showConfirm = function(text, callback) {
		var alertObj = aj.alert.show(text, {
			showYes : true,
			showNo :true,
			showOk :false,
			onYesClk : function(e) {
				alertObj.$el.classList.add('hide');
				callback && callback();
			},
			onNoClk : function(e) { // 在box里面触发关闭，不能直接用 alertObj.close(e);
				alertObj.$el.classList.add('hide');
			}
		});
	}
	
	document.body.appendChild(document.createElement('div')).className = 'msgHolder';

	// 顶部出现，用于后台提示信息多
	aj.msg = new Vue({
		el : '.msgHolder',
		data : {
			showText : '' // 显示的内容
		},
		template : '<div class="aj-topMsg" v-html="showText"></div>',
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

// 浮層組件，通常要復用這個組件
Vue.component('aj-layer', {
	template : '<div class="aj-modal hide" @click="close($event);"><div><slot></slot></div></div>',
	methods : {
		show : function(cfg) {
			this.$el.classList.remove('hide');
//			this.BUS.emit('aj-layer-closed', this);
			if(cfg && cfg.afterClose)
				this.afterClose = cfg.afterClose;
		},
		close : function(e) {
			aj.alert.$options.methods.close.apply(this, arguments);
		}
	}
});

