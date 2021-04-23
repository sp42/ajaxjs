
namespace aj.fb {
	interface PropertyEditor extends Vue {
		name: string;
		placeHolder: string;
	}

	export var PropertyEditor: PropertyEditor = <PropertyEditor>new Vue({
		el: '.right .bottom',
		data: {
			name: '',
			placeHolder: ''
		},
		watch: {
			placeHolder(v: string): void {
				if (wysiwyg.center.focusEl)
					// @ts-ignore
					wysiwyg.center.focusEl.placeholder = v;
			}
		}
	});

}