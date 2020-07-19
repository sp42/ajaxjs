aj._carousel = {
	props : {
		isMagic : {			// 是否无缝模式
			type : Boolean, default : false 
		},
		// 推荐使用 百分比，px 的话要考虑滚动条，比较麻烦
		// 要使用 px 推荐指定 stepWidth
		// banner，要使用 px
		// 如果要跟手移动，要使用 px
		isUsePx :  {
			type : Boolean, default : false 
		},
		autoHeight :  {// 是否自动高度
			type : Boolean, default : false 
		},
		disableTabHeaderJump : {// 是否禁止通过 tab 候选栏来跳转。一般 wizzard 向导式的时候不能跳转
			type : Boolean, default : false 
		}, 
		
		isGetCurrentHeight : {// 自动当前项最高，忽略其他高度，这个用在 tab很好，比 autoHeight 的好，可视作autoHeight 2.-
			type : Boolean, default : true 
		},
		initItems : Array // 输入的数据
	},
	data() {
		return {
			selected : 0
		};
	},
	mounted() {
		this.mover = this.$el.$('div.content'); 
		var mover = this.mover, children = mover.children, len = children.length;
		
		setTimeout(() => {
			var stepWidth = this.setItemWidth();
			
			if(this.isMagic) 
				mover.style.width = this.isUsePx ? (stepWidth * 2) +'px' : '200%';
			else
				mover.style.width = this.isUsePx ? (stepWidth * len) +'px' : len + '00%';
			
			var tabWidth = this.isUsePx ? stepWidth + 'px' : (1 / len * 100).toFixed(5) + '%';// 分配  tab 宽度
			this.tabWidth = tabWidth;
			
			
			for(var  i = 0; i < len; i++) 
				children[i].style.width = this.isMagic ? '50%' : tabWidth;
			
			// 如果有头部候选栏，也要设置
			var headerUl = this.$el.$('header ul');
			
			if(headerUl)
				for(var  i = 0; i < len; i++) 
					headerUl.children[i].style.width = tabWidth;
			
			this.doHeight(this.selected);
		}, 400);
		
		// 登记 resize 事件，以便 resize 容器的时候调整大小。
		// 使用 isUsePx = true 的好处是不用登记 resize 事件
		// this.isUsePx && ajaxjs.throttle.init(this.onResize.bind(this));
	},
	
	watch:{
		selected(index, oldIndex) {
			if(this.$isStop) // 停止，不切换，在做向导式时有用
				return;
			
			var children = this.$el.$('header ul').children;
			var contentChild = this.$el.$('.content').children;
			 
			if(children && contentChild && children[oldIndex] && contentChild[oldIndex]) {
				children[oldIndex].classList.remove('active');
				contentChild[oldIndex].classList.remove('active');
		
				children[index].classList.add('active');
				contentChild[index].classList.add('active');
				 
				this.go(index);
			}
		}
	},
	
	methods : {
		setItemWidth() {
			this.stepWidth = this.stepWidth || this.mover.parentNode.clientWidth || window.innerWidth; // 获取容器宽度作为 item 宽度
			return this.stepWidth;
		},
		
		changeTab(index) {
			this.selected = index;
			this.go(index);
		},
		
		/**
		 * 跳到指定的那一帧
		 * 
		 * @param {Number} i
		 */
		go(i) {
			this.$emit('before-carousel-item-switch', this, i);
			if(this.$isStop) // 停止，不切换，在做向导式时有用
				return;
			
			var mover = this.mover, children = mover.children, len = children.length;
			this.doHeight(i);
		
			if(this.isMagic) {
				// clear before
				for(var p = 0; p < len; p++) {
					if(this.selected == p) {// 当前的
						continue;
					}else if(i == p) {// 要显示的
						children[p].classList.remove('hide');
					}else {	
						children[p].classList.add('hide');
					}
				}
		
				var cssText = i > this.selected
					? 'translate3d({0}, 0px, 0px)'.replace('{0}', '-50%')
					: 'translate3d({0}, 0px, 0px)'.replace('{0}', '0%');
					mover.style.webkitTransition = '-webkit-transform 400ms linear';
				
				mover.style.webkitTransform = cssText;
	 			
			}else{
				var isWebkit = navigator.userAgent.toLowerCase().indexOf('webkit') != -1;
				
				if(!this.stepWidth)
					this.setItemWidth();
				var leftValue =  this.isUsePx ? ('-' + (i * this.stepWidth) + 'px') : ('-' + (1 / len * 100  * i).toFixed(2) + '%');
				mover.style[this.isWebkit ? 'webkitTransform' : 'transform'] = 'translate3d({0}, 0px, 0px)'.replace('{0}', leftValue);
				
				// 使用 left 移动！
// mover.style.left = this.isUsePx ? ('-' + (i * this.stepWidth) + 'px') : ('-'+ i + '00%');
			}
			
			this.$emit('carousel-item-switch', this, i, children[i]);
		},
		
	    // 跳到前一帧。
		 goPrevious() {
			if(this.$isStop) // 停止，不切换，在做向导式时有用
				return;
				
			 var len = this.mover.children.length;
			 
	        this.selected--;
	        if (this.selected < 0)
	        	this.selected = len - 1;
	        this.go(this.selected); 
	    },
	    
	    /**
		 * 跳到下一帧。
		 */
	    goNext() {
			if(this.$isStop) // 停止，不切换，在做向导式时有用
				return;
			
	    	var len = this.mover.children.length;
	        this.selected++;
	        
	        if (this.selected == len)
	        	this.selected = 0; // 循环
	        this.go(this.selected);
	    },
		
	    // 重复了
	    onResize() {
			var stepWidth   = this.mover.parentNode.clientWidth; // 获取容器宽度作为
																	// item 宽度
			this.mover.style.width = this.isUsePx ? (stepWidth * this.len) +'px' : this.len + '00%';
			
			for(var i = 0; i < this.len; i++) 
				this.children[i].style.width = stepWidth + 'px';
		},
		
		// 控制高度 解决高度问题
		doHeight(i) {
			if(this.isGetCurrentHeight) {
				var mover = this.mover, children = mover.children, len = children.length;
				for(var p = 0; p < len; p++) {
					if(i == p) {
						children[p].style.height = 'initial';	
					}else{
						children[p].style.height = '1px';					
					}
				}
			}
		},
		
		doAutoHeight(nextItem) {
			 if(this.autoHeight) {
				 var tabHeaderHeight = 0;
				 if(this.tabHeader) 
					 tabHeaderHeight = this.tabHeader.scrollHeight;
				 
				 this.el.style.height = (nextItem.scrollHeight + tabHeaderHeight + 50) + 'px'; 
			 }
		 },
		 
		 autoChangeTab(e) {
			if(this.$isStop) // 停止，不切换，在做向导式时有用
				return;
				
			 var el = e.currentTarget;
			 var children = el.parentNode.children;
			 for(var i = 0, j = children.length; i < j; i++) {
				 if(el == children[i]) 
					 break;
			 }

			 this.selected = i;
		 }
	}
};

Vue.component('aj-carousel', {
	mixins: [aj._carousel],
	template : 
		'<div class="aj-carousel aj-carousel-tab">\
			<header><ul>\
				<li v-for="(item, index) in items" :class="{\'active\': index === selected}" @click="changeTab(index);">{{item.name}}</li>\
			</ul></header>\
			<div>\
				<div v-for="(item, index) in items" :class="{\'active\': index === selected}" v-html="item.content"></div>\
			</div>\
		</div>',
	data() {
		return {
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
	}
});

Vue.component('aj-banner', {
	mixins: [aj._carousel],
	props: {
		isUsePx: {
			default: true
		},
		isGetCurrentHeight: {
			default: false
		},
		autoLoop: { // autoLoop = 0 时不自动轮播
			type : Number, default : 4000 
		},
		showTitle: { 		// 是否显示下方的标题
			type: Boolean, default : false
		},
		showDot: {			// 是否显示一点点，一般显示了标题就不显示点
			type : Boolean, default : true
		}
	},
	template: 
		'<div class="aj-carousel aj-banner">\
			<header><ul v-show="showTitle">\
				<li v-for="(item, index) in items" :class="{\'hide\': index !== selected}">{{item.name}}</li>\
			</ul><ol v-show="showDot">\
					<li v-for="n in items.length" :class="{\'active\': (n - 1) === selected}" @click="changeTab(n - 1);"></li></ol></header>\
			<div class="content">\
				<div v-for="(item, index) in items" :class="{\'active\': index === selected}" v-html="getContent(item.content, item.href)"></div>\
			</div>\
		</div>',
	data() {
		return {
			items : this.initItems || [
				{name : '杜甫：望岳', content : '<img src="../images/20150826162352938.jpg" />', href : 'http://qq.com'},
				{name : '资质证照',  content : '<img src="../images/20150906125934797.jpg" />', href : 'javascript:alert(9);'},
				{name : '资质证照',   content : '<img src="../images/20150906113629683.jpg" />', href: '#'}
			]
		}; 
	},
	
	mounted() {
		this.loop();
	},
	methods: {
	    loop() {
	    	if(this.autoLoop)
	    		this.loopTimer = window.setInterval(this.goNext.bind(this), this.autoLoop);
	    },
	    getContent(content, href) {
	    	if(!href)
	    		return content;
	    	else
	    		return '<a href="' + href + '">' + content + '</a>';
	    }
	}
});

// 注意：定时器保存在 DOM 元素的属性上，是否会内存泄流呢？
Vue.component('aj-opacity-banner', {
	template: '<ul class="aj-opacity-banner"><slot></slot></ul>',
	props: {
		delay: {
			default: 3000 // 延时
		},
		fps: {
			default: 25 // 帧速
		}
	},
	data() {
		return {
			active:	0, // 当前索引
			list:	[],	// 各帧
			timer:	null
		}
	},
	mounted() {
		this.list = this.$el.querySelectorAll('li');
		this.list[0].style.opacity = 1;
		this.run();
	},
	methods: {
		/* 顺序播放焦点图 */
		run() {
			this.timer = window.setInterval(() => {			
	//		var controls = this.controls;
				var active = this.active;
				
				this.clear();
				active += 1;
				active = active % this.list.length;
	//		controls[active].className = 'active';
				
				this.active = active;
				this.animate(100);
			}, this.delay);
		},
		
		per() {
//			var controls = this.controls;
			var active = this.active;
			this.clear();
			active -= 1;
			active = active % this.list.length;
			
			if (active < 0)
				active = this.list.length - 1;
//			controls[active].className = 'active';

			this.active = active;
			this.animate(100);
		},
		/* 内容淡出 */
		clear() {
	//		this.controls[active].className = '';
			this.animate(0);
		},
	
		getOpacity(el) {
			var val = getComputedStyle(el)["opacity"];
			val *= 100;
			
			return parseFloat(val) || 0;
		},
		animate(params) {
			var el = this.list[this.active], getOpacity = this.getOpacity, fps = 1000 / this.fps;
			window.clearTimeout(el.timer);
			
			window.setTimeout(function() {
				var i = getOpacity(el), speed = (params - i) / 8;
				speed = speed > 0 ? Math.ceil(speed) : Math.floor(speed);
				// console.log("i=" + i + "; speed="+ speed+"; s="+s+"; k="+k);
				i += speed;
				el.style.opacity = i / 100;
	
				window.clearTimeout(el.timer);
	//			params.complete && params.complete.call(elem);
	
				el.timer = window.setTimeout(arguments.callee, fps);
			}, fps);
		}
	}
});