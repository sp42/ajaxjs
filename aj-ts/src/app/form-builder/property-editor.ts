
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
			placeHolder(v): void {
				if (wysiwyg.center.focusEl) {
					wysiwyg.center.focusEl.placeholder = v;
				}
			}
		}
	});

}