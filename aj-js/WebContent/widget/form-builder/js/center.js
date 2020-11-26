
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
		},
		onDragEnter() {
			var div = event.currentTarget;
//			debugger;
			// 当拖拽元素进入潜在放置区域时，高亮处理
			div.style.backgroundColor = '#ead1d1';
			event.dataTransfer.dropEffect ='copy';
		},
		onDragLeave(){
			var div = event.currentTarget;
			div.style.backgroundColor = '';
		},
		onDrop(){
			this.onDragLeave();
			var div = event.currentTarget;
			var text = event.dataTransfer.getData("text");
			var el;
			switch(text) {
				case 'Text Field':
					el = document.createElement('input');
					el.type = 'text';
					div.$('label').appendChild(el);
				break;
			}
			
			this.focusEl = el;
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
