Vue.component('aj-oauth-login', {
	template: '<a href="###" @click="loginWeibo"><img src="https://www.sinaimg.cn/blog/developer/wiki/240.png" /></a>',
	props: {
		clientId: { // App Key
			required: true,
			type: String
		},
		redirectUri: { // 回调地址
			required: true,
			type: String
		}
	},
	data() {
		return {
			weiboAuthWin: null,
			result: ''
		}
	},
	mounted() {
		aj.OauthLoginInstance = this;
	},
	methods: {
		loginWeibo() {
			var url = 'https://api.weibo.com/oauth2/authorize?client_id=' + this.clientId;
			url += '&response_type=code&state=register&redirect_uri=';
			url += this.redirectUri;
			
			console.log(url);
			this.weiboAuthWin = window.open(url, '微博授权登录', 'width=770,height=600,menubar=0,scrollbars=1,resizable=1,status=1,titlebar=0,toolbar=0,location=1');
		},
		
		/**
		 * 关闭窗口
		 */
		closeWin() {			
			if (this.result) {
				console.log(this.result);
				this.weiboAuthWin.close();
			} else {
				console.log("请求返回值为空");
			}
		}
		
	}
});