/**
 * 页面常见组件
 */

// 全屏幕加载指示器
// 推荐一款loading图标的网站，https://icons8.com/ preloaders/
Vue.component('aj-page-fullscreen-loading-indicator', {
	template : '<div class="aj-fullscreen-loading"></div>',
	beforeCreate () {
		document.onreadystatechange = () => {
		    if(document.readyState === "complete") 
		        aj(".aj-fullscreen-loading").classList.add('fadeOut');
		}
	}
});


// 字体大小
Vue.component('aj-adjust-font-size', {
	props : {
		articleTarget: { // 正文所在的位置，通过 CSS Selector 定位
			type: String,
			required: false,
			default : 'article p'
		}
	},
	template: 
		'<div class="aj-adjust-font-size" @click="onClk($event);">\
			<span>字体大小</span>\
			<ul>\
				<li><label><input type="radio" name="fontSize" /> 小</label></li>\
				<li><label><input type="radio" name="fontSize" /> 中</label></li>\
				<li><label><input type="radio" name="fontSize" /> 大</label></li>\
			</ul>\
		</div>',
	methods: {
		onClk(e) {
			var el = e.target, target = el.innerHTML;
			
			if(el.tagName != 'LABEL')
				el = el.up('label');
			
			if(el.innerHTML.indexOf('大') != -1) 
				this.setFontSize('12pt');
			else if(el.innerHTML.indexOf('中') != -1) 
				this.setFontSize('10.5pt');
			else if(el.innerHTML.indexOf('小') != -1) 
				this.setFontSize('9pt');
		},

		setFontSize(fontSize) {
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
	props: {
		title: { // 标题
			type: String,
			required: true
		},
		initCreateDate: String,
		initContent: { // 正文，还可以通过 slot 添加额外内容
			type: String,
			required: false
		},
		isShowTools: { // 是否显示扩展工具栏
			type: Boolean,
			required: false
		},
		neighbor: { // 相邻的两笔记录
			type: Object,
			default: ()=>{
				return {};
			},
			required: false
		}
	},
	data(){
		return {
			content: this.initContent,
			createDate: this.initCreateDate
		};
	},
	template: 	
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
		jsurl: { // js字库文件较大，外部引入
			type: String,
			required: true
		}
	},
	template: 
		'<span>\
			<a href="javascript:;" onclick="toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>\
			/<a href="javascript:;" class="Chinese" onclick="toChinese(this);">正体中文</a>\
		</span>',
	created() {
		document.body.appendChild(document.createElement('script')).src = this.$props.jsurl;
	}
});

// https://vuejs.org/v2/guide/components.html#Content-Distribution-with-Slots
Vue.component('aj-tab', {
   	template: 
   		'<div :class="isVertical ? \'aj-simple-tab-vertical\' : \'aj-tab\' ">\
	      <button v-for="tab in tabs" v-bind:key="tab.name"\
	        v-bind:class="[\'tab-button\', { active: currentTab.name === tab.name }]"\
	        v-on:click="currentTab = tab">{{tab.name}}\
	      </button>\
	      <component v-bind:is="currentTab.component" class="tab"></component>\
	    </div>',
	props: {
		isVertical : Boolean // 是否垂直方向的布局，默认 false,
	},
    data() {
    	return {
          tabs: [],
          currentTab: {}
        };
    },
    mounted() {
		var arr =  this.$slots.default;
		
		for(var i = 0; i < arr.length; i++) {
			var el = arr[i];
			
			if(el.tag === 'textarea') {
				this.tabs.push({
					name : el.data.attrs['data-title'],
					component: {
		 	            template: '<div>' + el.children[0].text + "</div>"
		   	 	    }
	    		});
	    	}
    	}

    	this.currentTab = this.tabs[0];
    }
});

aj.tabable = {
	data() {
		return {
			selected: 0
		};
	},
	mounted() {
		var ul = this.$el.querySelector('.aj-simple-tab-horizontal > ul');
		ul.onclick = e => {
			var el = e.target;
			var index = Array.prototype.indexOf.call(el.parentElement.children, el);
			this.selected = index;
		};
		
		this.$options.watch.selected.call(this, 1);
	},
	watch: {
		selected(v) {
			var headers  = this.$el.querySelectorAll('.aj-simple-tab-horizontal > ul > li');
			var contents = this.$el.querySelectorAll('.aj-simple-tab-horizontal > div > div');
			var each = arr => {							
				for(var i = 0, j = arr.length; i < j; i++) {
					if(v === i) {
						arr[i].classList.add('selected');
					} else {
						arr[i].classList.remove('selected');
					}
				}
			};
			
			each(headers);
			each(contents);
		}
	}
};

// 回到顶部
Vue.component('aj-back-top', {
	template : 
		'<a href="###" @click="go">回到顶部</a>',
	methods : {
		go() {
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
	template:
		'<div class="aj-process-line">\
			<div class="process-line">\
				<div v-for="(item, index) in items" :class="{current : index == current, done : index < current}">\
					<span>{{index + 1}}</span><p>{{item}}</p>\
				</div>\
			</div>\
		</div>',
	props: {
		items: {
			type: Array,
			default: function() { 
				return ['Step 1', 'Step 2', 'Step 3']; 
			}
		}
	},
	data() {
		return {
			current : 0
		};
	},
	methods: {
		go(i) {
			this.current = i;
		},
		perv() {
			var perv = this.current - 1;
			if (perv < 0)
				perv = this.items.length - 1;
			
		    this.go(perv); 
		},
		next() {
	    	var next = this.current + 1;
	        if (this.items.length == next)
	        	next = 0; // 循环
	        	
	        this.go(next);
		}
	}
});

// 函数节流 https://www.cnblogs.com/moqiutao/p/6875955.html
aj.throttleV2 = function(fn, delay, mustRunDelay) {
 	var timer = null;
 	var t_start;
 	
 	return function() {
 		var t_curr = +new Date();
 		window.clearTimeout(timer);
 		
 		if(!t_start) 
 			t_start = t_curr;
 		
 		if(t_curr - t_start >= mustRunDelay) {
 			fn.apply(this, arguments);
 			t_start = t_curr;
 		} else {
 			var args = arguments;
 			timer = window.setTimeout(() => {
 				fn.apply(this, args);
 			}, delay);
 		}
 	};
};
 
aj.img = (function() {
	function dataURLtoBlob(dataurl) {
		var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1], bstr = atob(arr[1]), len = bstr.length, u8arr = new Uint8Array(len);
		while (len--)
			u8arr[len] = bstr.charCodeAt(len);
		
		return new Blob([ u8arr ], {
			type : mime
		});
	}
	
	return {	
		/**
		 * image转canvas：图片地址
		 */
		imageToCanvas(imgUrl, cb, isCovernt2DataUrl) {
			var img = new Image();
			img.onload = () => {
				var canvas = document.createElement('canvas');
				canvas.width = img.width;
				canvas.height = img.height;
				canvas.getContext('2d').drawImage(img, 0, 0);
				
				if(isCovernt2DataUrl) 
					cb(canvas.toDataURL(isCovernt2DataUrl.format || 'image/jpeg', isCovernt2DataUrl.quality || .9));
				else 
					cb(canvas);
			}
			
			img.src = imgUrl;
		},
		
		imageElToBlob(imgUrl, cb) {
			this.imageToCanvas(imgUrl, (canvas) => {
				cb(dataURLtoBlob(canvas));
			}, true)
		},

		/**
		 * 改变blob图片的质量，为考虑兼容性
		 * 
		 * @param blob 图片对象
		 * @param callback 转换成功回调，接收一个新的blob对象作为参数
		 * @param format  目标格式，mime格式
		 * @param quality 介于0-1之间的数字，用于控制输出图片质量，仅当格式为jpg和webp时才支持质量，png时quality参数无效
		 */
		changeBlobImageQuality (blob, callback, format, quality) {
			format = format || 'image/jpeg';
			quality = quality || 0.9; // 经测试0.9最合适
			var fr = new FileReader();
			
			fr.onload = (e) => {
				var dataURL = e.target.result;
				var img = new Image();
				img.onload = () => {
					var canvas = document.createElement('canvas');
					var ctx = canvas.getContext('2d');
					canvas.width = img.width;
					canvas.height = img.height;
					ctx.drawImage(img, 0, 0);
					
					var newDataURL = canvas.toDataURL(format, quality);
					callback && callback(dataURLtoBlob(newDataURL));
					canvas = null;
				};
				
				img.src = dataURL;
			};
			
			fr.readAsDataURL(blob); // blob 转 dataURL
		}
	};
})();

// 悬浮显示大图。工厂方法
aj.imageEnlarger = function() {
	var vue = new Vue({
		el: document.body.appendChild(document.createElement('div')),
		template: 
			'<div class="aj-image-large-view">\
				<div style="position: fixed;max-width:400px;transition: top ease-in 200ms, left ease-in 200ms;">\
				<img :src="imgUrl" style="width: 100%;" />\
			</div></div>',
		data: {
			imgUrl: null
		},
		mounted() {// 不能用 onmousemove 直接绑定事件
			document.addEventListener('mousemove', aj.throttleV2(this.move.bind(this), 50, 5000), false);
		},
		methods: {
			move(e) {
				if(this.imgUrl) {
					var el = this.$el.$('div');
					var w = 0, imgWidth = this.$el.$('img').clientWidth;
					
					if(imgWidth > e.pageX) 
						w = imgWidth;
				
					el.style.top = (e.pageY + 20) + 'px';
					el.style.left = (e.pageX - el.clientWidth + w) + 'px';
				}
			}
		}
	});
	
	aj.imageEnlarger.singleInstance = vue; // 单例
	return vue;
}