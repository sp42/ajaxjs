
FB.center = new Vue({
	el : '.center',
	data : {
		codeMode: false,
		focusEl : null
	},
	mounted(){
		var arr = this.$el.querySelectorAll('input');
		// 输入框禁止显示历史记录
		for(var i = 0, j = arr.length; i < j; i++)
			arr[i].setAttribute('autocomplete', 'off');
	},
	methods:{
		toggleCodeMode(){
			this.codeMode = !this.codeMode;
		},
		onStageClk(){
			var el = event.target;
			if('INPUT' === el.tagName) {
				this.focusEl = el;

			}
		}
	},
	watch:{
		focusEl(el, old) {
			FB.propertyEditor.name = el.name;
			FB.propertyEditor.placeHolder = el.placeholder;
			el.style.borderColor = 'red';
			
			if(old)
				old.style.borderColor = '';
		}
	}
});
