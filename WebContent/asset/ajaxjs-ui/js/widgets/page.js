/**
 * 页面常见组件
 */

// 全屏幕加载指示器
// 推荐一款loading图标的网站，https://icons8.com/ preloaders/
Vue.component('aj-page-fullscreen-loading-indicator', {
	template : '<div class="aj-fullscreen-loading"></div>',
	beforeCreate : function () {
		document.onreadystatechange = function () {
		    if(document.readyState === "complete") {
		        aj(".aj-fullscreen-loading").classList.add('fadeOut');
		    }
		}
	}
});

// 分享
Vue.component('aj-page-share', {
	template : 
		'<div class="aj-page-share">\
			分享到 &nbsp;&nbsp;\
			<a title="转发至QQ空间" :href="\'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=\' + url" target="_blank">\
				<img src="http://static.youku.com/v1.0.0691/v/img/ico_Qzone.gif" /></a>\
			<a title="转发至新浪微博" :href="\'http://v.t.sina.com.cn/share/share.php?appkey=2684493555&Uid=&source=&sourceUrl=&url=\' + url + \'&title=\' + title" target="_blank">\
				<img src="http://static.youku.com/v1.0.0691/v/img/ico_sina.gif" /></a>\
			<a title="分享到腾讯朋友" :href="\'http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?to=pengyou&url=\' + url + \'&title=\' + title" target="_blank">\
				<img src="http://static.youku.com/v1.0.0691/v/img/ico_pengyou.png" /></a>\
			<a title="推荐到豆瓣" :href="\'http://www.douban.com/recommend/?url=\' + url + \'&title=\' + title" target="_blank">\
				<img src="http://static.youku.com/v1.0.0691/v/img/ico_dou_16x16.png" /></a>\
		</div>',
	computed : {
		title : function() {
			return encodeURIComponent(document.title);
		},
		url : function() {
			return document.location.href;
		}
	}
});

// 字体大小
Vue.component('aj-adjust-font-size', {
	props : {
		articleTarget : { // 正文所在的位置，通过 CSS Selector 定位
			type: String,
			required: false,
			default : 'article p'
		}
	},
	template : 
		'<div class="aj-adjust-font-size" @click="onClk($event);">\
			<span>字体大小</span>\
			<ul>\
				<li><label><input type="radio" name="fontSize" /> 小</label></li>\
				<li><label><input type="radio" name="fontSize" /> 中</label></li>\
				<li><label><input type="radio" name="fontSize" /> 大</label></li>\
			</ul>\
		</div>',
	methods : {
		onClk : function(e) {
			var el = e.target, target = el.innerHTML;
			
			if(el.tagName != 'LABEL')
				el = el.up('label');
			
			if(el.innerHTML.indexOf('大') != -1) {
				this.setFontSize('12pt');
			}else if(el.innerHTML.indexOf('中') != -1) {
				this.setFontSize('10.5pt');
			}else if(el.innerHTML.indexOf('小') != -1) {
				this.setFontSize('9pt');
			}
		},

		setFontSize : function (fontSize) {
			aj(this.$props.articleTarget, function(p){
				p.style.fontSize = fontSize;
			});
		}
	}
});

// 页面按钮
Vue.component('aj-misc-function', {
	props : {
		articleTarget : { // 要打印的区域
			type: String,
			required: false,
			default : 'article'
		}
	},
	template : 
		'<div class="aj-misc-function">\
			<a href="javascript:printContent();"><span style="font-size:1rem;">&#12958;</span>打 印</a>\
			<a href="javascript:sendMail_onClick();"><span style="font-size:1rem;">&#9993;</span>发送邮件</a>\
			<a href="javascript:;"><span style="font-size:1.2rem;">★ </span>收 藏</a>\
		</div>',
	methods : {
		// 打印页面
		printContent : function () {
      		var printHTML = "<html><head><title></title><style>body{padding:2%};</style></head><body>";
      		printHTML +=  aj('article').innerHTML;
      		printHTML += "</body></html>";
	        var oldstr = document.body.innerHTML;
	        document.body.innerHTML = printHTML;
	        window.print();
	        document.body.innerHTML = oldstr;
		},
	
		// 发送邮件
		sendMail : function () {
			location.href = 'mailto:xxx@tagzine.com?subject= '
					+ document.title
					+ '&body=\u6211\u5411\u4F60\u63A8\u8350\u8FD9\u6761\u6587\u7AE0\uFF0C\u5E0C\u671B\u4F60\u559C\u6B22\uFF01\u6587\u7AE0\u6807\u9898\uFF1A'
					+ document.title
					+ '\u3002\u8BF7\u70B9\u51FB\u67E5\u770B\uFF1A ' + location.href;
		}
	}
});

// 正文
Vue.component('aj-article-body', {
	props : {
		title : { // 标题
			type: String,
			required: true
		},
		createDate : String,
		content : { // 正文，还可以通过 slot 添加额外内容
			type: String,
			required: true
		},
		neighbor : { // 相邻的两笔记录
			type: Object,
			default : function(){
				return {};
			},
			required: false
		}
	},
	template : 	
		'<div class="aj-article-body">\
			<article>\
				<h3>{{title}}</h3>\
				<h4>{{createDate}}</h4>\
				{{content}}\
				<slot></slot>\
			</article>\
			<div>\
				<a :href="neighbor.pervInfo.url" v-if="neighbor.pervInfo">上则记录：{{neighbor.pervInfo.name}}</a>\
				<a :href="neighbor.nextInfo.url" v-if="neighbor.nextInfo">下则记录：{{neighbor.nextInfo.name}}</a>\
			</div>\
			<aj-misc-function></aj-misc-function><aj-adjust-font-size></aj-adjust-font-size><aj-page-share></aj-page-share>\
		</div>'
});

// Baidu 自定义搜索
Vue.component('aj-baidu-search', {
	props : ['siteDomainName'],
	template : 
		'<div class="aj-baidu-search"><form method="GET" action="http://www.baidu.com/baidu" onsubmit="//return g(this);">\
		     <input type="text" name="word" placeholder="请输入搜索之关键字" />\
		     <input name="tn" value="bds" type="hidden" />\
		     <input name="cl" value="3" type="hidden" />\
		     <input name="ct" value="2097152" type="hidden" />\
		     <input name="si" :value="getSiteDomainName" type="hidden" />\
		 	<div class="searchBtn" onclick="this.parentNode.submit();"></div>\
		 </form></div>',
	computed : {
		getSiteDomainName : function() {
			return this.$props.siteDomainName || location.host || document.domain;
		}
	}
});

// 正体中文
Vue.component('aj-chinese-switch', {
	props : {
		jsurl : { // js字库文件较大，外部引入
			type: String,
			required: true
		}
	},
	template : 
		'<span>\
			<a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>\
			/<a href="javascript:;" class="Chinese" onclick="toChinese(this);">正体中文</a>\
		</span>',
	created: function() {
		document.body.appendChild(document.createElement('script')).src = this.$props.jsurl;
	}
});