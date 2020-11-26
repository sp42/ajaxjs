// 图片验证码
Vue.component('aj-page-captcha', {
	props: {
		imgSrc: {
			type: String, // 生成图片验证码地址
			required: false,
		},
		fieldName: {	// 提交的字段名
			type: String, required: false, default : 'captcha'
		}
	},
	template: '#aj-page-captcha',
	methods: {
		onClk($event) {
			var img = $event.target;
			img.src = img.src.replace(/\?\d+$/, '') + '?' + new Date().valueOf();
		},
		refreshCode(){
			var img = this.$el.querySelector('img');
			img.src = img.src.replace(/\?\d+$/, '') + '?' + new Date().valueOf();
		}
	}
});