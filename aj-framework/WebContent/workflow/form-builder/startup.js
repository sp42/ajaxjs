			FB={};
			FB.statusBar = new Vue({
				el:'.statusBar',
				data: {
					show : false,
					text:'',
					timer:null
				},
				mounted() {
					this.showMsg('欢迎来到 From Builder');
				},
				methods:{
					showMsg(text) {
						this.text = text;
						this.show = true;
						if(this.timer) {
							clearTimeout(this.timer);
						}
						this.timer = setTimeout(()=>this.show = false, 4000);
					}
				}
			});