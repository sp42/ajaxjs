<%@page pageEncoding="UTF-8"%>
<h3>跑马灯 Marquee</h3>
<h4>超简单跑马灯 Super Simple Marquee</h4>

<p>这是最简单的跑马灯，原理是把前面的字符放到后面去。鼠标移入时候可暂停动画，移出恢复。支持插槽，但插槽中不能包含 tag。</p>
<p>属性说明：</p>
<table class="ajaxjs-borderTable">
	<tr>
		<td>属性</td>
		<td>含义</td>
		<td>类型</td>
		<td>是否必填</td>
		<td>默认值</td>
	</tr>
	<tr>
		<td>interval</td>
		<td>动画时间间隔，定时器的参数</td>
		<td>Number</td>
		<td>N</td>
		<td>500</td>
	</tr>
	<tr>
		<td>canstop</td>
		<td>是否可以鼠标移入时候暂停动画</td>
		<td>Boolean</td>
		<td>N</td>
		<td>true，可以</td>
	</tr>
</table>

<p>例子：</p>
<pre class="prettyprint"><xmp><aj-super-simple-marquee-text></aj-super-simple-marquee-text></xmp></pre>

<p>演示：</p>
<section class="marquee">
	<aj-super-simple-marquee-text></aj-super-simple-marquee-text>
</section>
<script>
	new Vue({
		el : '.marquee'
	});
</script>



<h4>上下字幕跑马灯 Marquee</h4>

<p>属性说明：</p>
<table class="ajaxjs-borderTable">
	<tr>
		<td>属性</td>
		<td>含义</td>
		<td>类型</td>
		<td>是否必填</td>
		<td>默认值</td>
	</tr>
	<tr>
		<td>interval</td>
		<td>动画时间间隔，定时器的参数</td>
		<td>Number</td>
		<td>N</td>
		<td>500</td>
	</tr>
	<tr>
		<td>canstop</td>
		<td>是否可以鼠标移入时候暂停动画</td>
		<td>Boolean</td>
		<td>N</td>
		<td>true，可以</td>
	</tr>
</table> 

<p>例子：</p>
<pre class="prettyprint"><xmp><aj-simple-marquee-text></aj-simple-marquee-text></xmp></pre>
<p>演示：</p>
<section class="marquee2">
	<aj-simple-marquee-text></aj-simple-marquee-text>
</section>
<script>
	aj._simple_marquee_text = {
		props: {
			interval: {
				type: Number, // 动画时间间隔
				default: 500
			},
			canstop: {
				type: Boolean, // 是否可以鼠标移入时候暂停动画
				default: true
			}
		},
		mounted() {
			if (this.canstop) {
				this.$el.onmouseover = this.clearTimer.bind(this);
				this.$el.onmouseout = this.start.bind(this);
			}
		},
		methods: {
			start() {
				this.$timerId = window.setInterval(this.scroll, this.interval);
			},
			clearTimer() {
				this.$timerId && window.clearInterval(this.$timerId);
			}
		}
	};

	// 超简单跑马灯
	// 插槽中不能包含 tag
	Vue.component('aj-super-simple-marquee-text', {
		mixins: [aj._simple_marquee_text],
		template: '<div><slot>这是一段滚动的文字；这是一段滚动的文字；这是一段滚动的文字</slot></div>',
		mounted () {
			this.$arr = this.$el.innerHTML.split("");
			this.start();
		},
		methods: {
			scroll() {
				this.$arr.push(this.$arr.shift());
				this.$el.innerHTML = this.$arr.join("");
			}
		}
	});

	// 上下字幕
	Vue.component('aj-simple-marquee-text', {
		mixins: [aj._simple_marquee_text],
		template: '<ol><li>11111111111</li><li>22222222222</li><li>33333333333</li><li>44444444444</li><li>55555555555</li></ol>',
		mounted() {
			this.start();
		},
		methods: {
			scroll() {
				var lastEl = this.$el.firstChild;
				
				while (lastEl.nodeType != 1)
					lastEl = lastEl.nextSibling;// 找到最后一个元素

				this.$el.appendChild(this.$el.removeChild(lastEl)); // 把最后一个元素放到前头
			}
		}
	});

	Vue.component('aj-simple-marquee', {
		props: {
			interval: {
				default : 20
			},
			pauseInterval: { // 暂停间隔时间
				type: Number,
				default : 2000
			},
			itemHeight: { // 每一项的高度
				type: Number,
				required: 20,
			},
		},
		template: 
			'<div class="aj-simple-marquee" style="width: 100%; overflow: hidden;">\
				<div class="items"><slot></slot>\
				</div>\
				<div class="clone"></div>\
			</div>',
		mixins: [aj._simple_marquee_text],
		mounted() {
			var el = this.$el, children = el.$('.items').children, itemHeight = this.itemHeight;
			el.style.height = itemHeight + "px";
			
			var allHeight = 0;
			for(var i = 0, j = children.length; i < j; i++) { // 设置每行高度
				var item = children[i];
				item.style.display = 'block';
				item.style.height = itemHeight + "px";
				
				allHeight += itemHeight;
			}
			
			el.$('.clone').style.height = allHeight + 'px';// 相同高度
			
			// 复制第一个元素
			var clone = children[0].cloneNode(true);
			el.$('.clone').appendChild(clone);
			
			setTimeout(this.start.bind(this), 2000);
		},
		
		methods: {
			scroll() {
				var el = this.$el, top = el.scrollTop, height = el.$('.items').clientHeight;

				if (top <= height) {
					el.scrollTop ++;
					
					if(top != 0 && (top % this.itemHeight) === 0) {
						this.clearTimer();
						setTimeout(this.start.bind(this), this.pauseInterval);
					}
					
				} else {// 第一个恰好滑完
					el.scrollTop -= height; //返回至开头处
				}
			}
		}
	});


	new Vue({
		el : '.marquee2'
	});
</script>



