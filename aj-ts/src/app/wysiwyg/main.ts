namespace aj.wysiwyg {
	/**
	 * 状态栏
	 */
	interface StatusBar extends Vue {
		show: boolean;
		text: string;
		timer: number;
		showMsg(text: string): void;
	}

	export let statusBar: StatusBar = <StatusBar>new Vue({
		el: '.statusBar',
		data: {
			show: false,
			text: ''
		},
		mounted(this: StatusBar) {
			setTimeout(() => this.$el.classList.remove('hide'), 900);
		},
		methods: {
			showMsg(this: StatusBar, text: string): void {
				this.text = text;
				this.show = true;
				if (this.timer)
					clearTimeout(this.timer);

				this.timer = setTimeout(() => this.show = false, 4000);
			}
		}
	});

	interface CenterStage extends Vue {
		/**
		 * 是否代码模式
		 */
		codeMode: boolean;

		focusEl: any;
	}

	// 舞台中央区域
	export let center: CenterStage = <CenterStage>new Vue({
		el: 'body > .center',
		data: {
			codeMode: false,
			focusEl: null
		},
		mounted(): void {
			let arr = this.$el.querySelectorAll('input');
			// 输入框禁止显示历史记录
			for (let i = 0, j = arr.length; i < j; i++)
				arr[i].setAttribute('autocomplete', 'off');
		},
		methods: {
			toggleCodeMode(this: CenterStage): void {
				this.codeMode = !this.codeMode;
			},
			onStageClk(this: CenterStage): void {
				let el = event.target;
				if ('INPUT' === el.tagName)
					this.focusEl = el;
			},
			onDragEnter(this: CenterStage): void {
				let div = event.currentTarget;
				//			debugger;
				// 当拖拽元素进入潜在放置区域时，高亮处理
				div.style.backgroundColor = '#ead1d1';
				event.dataTransfer.dropEffect = 'copy';
			},
			onDragLeave(): void {
				let div = event.currentTarget;
				div.style.backgroundColor = '';
			},
			onDrop(this: CenterStage): void {
				this.onDragLeave();
				let div = event.currentTarget;
				let text = event.dataTransfer.getData("text");
				let el;

				switch (text) {
					case 'Text Field':
						el = document.createElement('input');
						el.type = 'text';
						div.$('label').appendChild(el);
						break;
				}

				this.focusEl = el;
			}
		},
		watch: {
			focusEl(el, old): void {
				fb.PropertyEditor.name = el.name;
				fb.PropertyEditor.placeHolder = el.placeholder;
				el.style.borderColor = 'red';

				if (old)
					old.style.borderColor = '';
			}
		}
	});
}

