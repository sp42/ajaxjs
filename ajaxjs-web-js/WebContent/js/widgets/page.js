/**
 * 页面常见组件
 */

// 全屏幕加载指示器
// 推荐一款loading图标的网站，https://icons8.com/ preloaders/
Vue.component('aj-page-fullscreen-loading-indicator', {
	template : '<div class="aj-fullscreen-loading"></div>',
	beforeCreate () {
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
		title() {
			return encodeURIComponent(document.title);
		},
		url() {
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
		onClk(e) {
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

		setFontSize (fontSize) {
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
		printContent() {
      		var printHTML = "<html><head><title></title><style>body{padding:2%};</style></head><body>";
      		printHTML +=  aj('article').innerHTML;
      		printHTML += "</body></html>";
	        var oldstr = document.body.innerHTML;
	        document.body.innerHTML = printHTML;
	        window.print();
	        document.body.innerHTML = oldstr;
		},
	
		// 发送邮件
		sendMail() {
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
		initCreateDate : String,
		initContent : { // 正文，还可以通过 slot 添加额外内容
			type: String,
			required: false
		},
		isShowTools : { // 是否显示扩展工具栏
			type: Boolean,
			required: false
		},
		neighbor : { // 相邻的两笔记录
			type: Object,
			default : function(){
				return {};
			},
			required: false
		}
	},
	data(){
		return {
			content : this.initContent,
			createDate : this.initCreateDate
		};
	},
	template : 	
		'<div class="aj-article-body">\
			<article>\
				<h3>{{title}}</h3>\
				<h4>{{createDate}}</h4>\
				<section v-html="content"></section>\
				<slot></slot>\
			</article>\
			<div v-if="isShowTools">\
				<a :href="neighbor.pervInfo.url" v-if="neighbor.pervInfo">上则记录：{{neighbor.pervInfo.name}}</a>\
				<a :href="neighbor.nextInfo.url" v-if="neighbor.nextInfo">下则记录：{{neighbor.nextInfo.name}}</a>\
			</div>\
			<aj-misc-function v-if="isShowTools"></aj-misc-function><aj-adjust-font-size v-if="isShowTools"></aj-adjust-font-size><aj-page-share v-if="isShowTools"></aj-page-share>\
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
		getSiteDomainName() {
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
	created() {
		document.body.appendChild(document.createElement('script')).src = this.$props.jsurl;
	}
});

// 简单选项卡
Vue.component('aj-simple-tab', {
	template : 
		'<div :class="isVertical ? \'aj-simple-tab-vertical\' : \'aj-simple-tab-horizontal\' ">\
			<ul>\
				<li v-for="(item, index) in items" :class="{\'selected\': index === selected}" @click="changeTab(index);">{{item.name}}</li>\
			</ul>\
			<div class="content">\
				<div v-for="(item, index) in items" :class="{\'selected\': index === selected}" v-html="item.content"></div>\
			</div>\
		</div>',
	props: {
		isVertical : Boolean, // 是否垂直方向的布局，默认 false,
		initItems : Array
	},
	data : function() {
		return {
			selected : 0,
			items : this.initItems || [
				{name : '杜甫：望岳', content : '岱宗夫如何，齊魯青未了。<br>\
											    造化鐘神秀，陰陽割昏曉。<br>\
											    蕩胸生層云，決眥入歸鳥，<br>\
											    會當凌絕頂，一覽眾山小。'
				},
				{name : '资质证照', content : '之所以如此，端在于中国传统中存在着发达的契约。予谓不信，可看看早年由福建师范大学内部印行的两册本《明清福建经济契约文书选集》，中国社会科学出版社出版的《自贡盐业契约档案选集》，花山文艺出版社出版的共二十册的《徽州千年契约文书》;再看看近些年由安徽师范大学出版社出版的十卷本的《千年徽州契约文书集萃》，广西师范大学出版社出版的四辑共四十册《徽州文书》、三辑共三十册的《清水江文书》，民族出版社出版的多卷本《贵州清水江流域明清契约文书》，凤凰出版社出版的《敦煌契约文书辑校》，天津古籍出版社出版的《清代宁波契约文书辑校》，浙江大学出版社出版的《清代浙东契约文书辑选》……不难发现，中国传统契约文书的整理出版，完全可称为如雨后春笋般在迅速成长!'},
				{name : '资质证照', content : '笔者出于个人兴趣，在关注这些已经整理出版的、卷帙浩繁的契约文献的同时，也游走各地，或亲自搜集各类契约文书，或到一些地方档案馆查看其业已编辑成册、内部印行的传统契约文书，如在台湾宜兰、高雄，山东青岛、威海，贵州锦屏、从江等地档案机构或民间都见到过相关契约文献。记忆尤深的一次，是我和几位学生共游山东浮来山。在一处值班室里，居然发现有人以清代契约文本粘糊墙壁!足见只要我们稍加留意，在这个文明发展历经数千年的国度，不时可以发现一些令人称心的古代契约文献。'}
			]
	
		};
	},
	
	methods : {
		changeTab : function(index) {
			this.selected = index;
		}
	}
});

// 回到顶部
Vue.component('aj-back-top', {
	template : 
		'<a href="###" @click="go">回到顶部</a>',
	methods : {
		go : function() {
//			 var b = 0;//作为标志位，判断滚动事件的触发原因，是定时器触发还是其它人为操作
//			 UserEvent2.onWinResizeFree(function(e) {
//				 if (b != 1) clearInterval(timer);
//				 b = 2;
//			 }, 'scroll');	
			this.$timerId && window.clearInterval(this.$timerId);
			var top = speed = 0;

			this.$timerId = window.setInterval(function() {
				top = document.documentElement.scrollTop || document.body.scrollTop;
				speed = Math.floor((0 - top) / 8);

				if (top === 0)
					clearInterval(this.$timerId);
				else
					document.documentElement.scrollTop = document.body.scrollTop = top + speed;
//				b = 1;
			}.bind(this), 30);
		}
	}
});


// 进度条
Vue.component('aj-process-line', {
	template :
		'<div class="aj-process-line">\
			<div class="process-line">\
				<div v-for="(item, index) in items" :class="{current : index == current, done : index < current}">\
					<span>{{index + 1}}</span><p>{{item}}</p>\
				</div>\
			</div>\
		</div>',
	props : {
		items : {
			type: Array,
			default : function() { 
				return ['Step 1', 'Step 2', 'Step 3']; 
			}
		}
	},
	data : function() {
		return {
			current : 0
		}
	},
	methods: {
		go : function(i) {
			this.current = i;
		},
		perv: function() {
			var perv = this.current - 1;
			if (perv < 0)
				perv = this.items.length - 1;
			
		    this.go(perv); 
		},
		next: function() {
	    	var next = this.current + 1;
	        if (this.items.length == next)
	        	next = 0; // 循环
	        	
	        this.go(next);
		}
	}
});

// 函数节流
function throttleV2(fn, delay, mustRunDelay) {
 	var timer = null;
 	var t_start;
 	
 	return function() {
 		var context = this, args = arguments, t_curr = +new Date();
 		clearTimeout(timer);
 		
 		if(!t_start) 
 			t_start = t_curr;
 		
 		if(t_curr - t_start >= mustRunDelay) {
 			fn.apply(context, args);
 			t_start = t_curr;
 		} else {
 			timer = setTimeout(function() {
 				fn.apply(context, args);
 			}, delay);
 		}
 	};
};
 
// 悬浮显示大图。工厂方法
aj.imageEnlarger = function() {
	var vue = new Vue({
		el : document.body.appendChild(document.createElement('div')),
		template: 
			'<div class="aj-image-large-view">\
				<div style="position: fixed;max-width:400px;transition: top ease-in 200ms, left ease-in 200ms;">\
				<img :src="imgUrl" style="width: 100%;" />\
			</div></div>',
		data : {
			imgUrl: null
		},
		mounted: function(){// 不能用 onmousemove 直接绑定事件
			document.addEventListener('mousemove', throttleV2(this.move.bind(this), 50, 5000), false);
		},
		methods: {
			move: function(e) {
				if(this.imgUrl) {
					var el = this.$el.$('div');
					var w = 0, imgWidth = this.$el.$('img').clientWidth;
					
					if(imgWidth > e.pageX) {
						w = imgWidth;
					}
				
					el.style.top = (e.pageY + 20)+ 'px';
					el.style.left = (e.pageX - el.clientWidth + w) + 'px';
				}
			}
		}
	});
	
	aj.imageEnlarger.singleInstance = vue; // 单例
	return vue;
}