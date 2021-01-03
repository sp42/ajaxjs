interface Banner extends Carousel {
	autoLoop: number;
}

Vue.component('aj-banner', {
	mixins: [aj.carousel.base],
	template: `
		<div class="aj-carousel aj-banner">
			<header>
				<ul v-show="showTitle">
					<li v-for="(item, index) in items" :class="{'hide': index !== selected}">{{item.name}}</li>
				</ul>
				<ol v-show="showDot">
					<li v-for="n in items.length" :class="{'active': (n - 1) === selected}" @click="changeTab(n - 1);"></li>
				</ol>
			</header>
			<div class="content">
				<div v-for="(item, index) in items" :class="{'active': index === selected}" v-html="getContent(item.content, item.href)">
				</div>
			</div>
		</div>
	`,
	props: {
		isUsePx: { default: true },
		isGetCurrentHeight: { default: false },
		autoLoop: { type: Number, default: 4000 },		// autoLoop = 0 时不自动轮播
		showTitle: { type: Boolean, default: false },	// 是否显示下方的标题
		showDot: { type: Boolean, default: true }		// 是否显示一点点，一般显示了标题就不显示点
	},
	data() {
		return {
			items: this.initItems || [
				{ name: '杜甫：望岳', content: '<img src="images/1.jpg" />', href: 'http://qq.com' },
				{ name: '资质证照', content: '<img src="images/2.jpg" />', href: 'javascript:alert(9);' },
				{ name: '资质证照', content: '<img src="images/3.jpg" />', href: '#' }
			]
		};
	},
	mounted() {
		// this.loop();
	},
	methods: {
		/**
		 * 
		 * @param this 
		 */
		loop(this: Banner): void {
			if (this.autoLoop)
				window.setInterval(this.goNext.bind(this), this.autoLoop);
		},

		/**
		 * 
		 * @param content 
		 * @param href 
		 */
		getContent(content: string, href: string): string {
			if (!href)
				return content;
			else
				return '<a href="' + href + '">' + content + '</a>';
		}
	}
});