"use strict";
var aj;
(function (aj) {
    var carousel;
    (function (carousel) {
        /**
         * 简单跑马灯 Super Simple Marquee
         */
        carousel.marqueeBase = {
            props: {
                interval: { type: Number, default: 500 },
                canstop: { type: Boolean, default: true } //  是否可以鼠标移入时候暂停动画
            },
            mounted: function () {
                if (this.canstop) {
                    this.$el.onmouseover = this.clearTimer.bind(this);
                    this.$el.onmouseout = this.start.bind(this);
                }
            },
            methods: {
                start: function () {
                    this.$timerId = window.setInterval(this.scroll, this.interval);
                },
                clearTimer: function () {
                    this.$timerId && window.clearInterval(this.$timerId);
                }
            }
        };
        Vue.component('aj-super-simple-marquee', {
            mixins: [carousel.marqueeBase],
            template: '<div><slot>这是一段滚动的文字；这是一段滚动的文字；这是一段滚动的文字</slot></div>',
            mounted: function () {
                this.$arr = this.$el.innerHTML.split("");
                this.start();
            },
            methods: {
                scroll: function () {
                    this.$arr.push(this.$arr.shift());
                    this.$el.innerHTML = this.$arr.join("");
                }
            }
        });
        /**
         * 上下字幕
         */
        Vue.component('aj-simple-marquee', {
            mixins: [carousel.marqueeBase],
            template: '<ol><li>11111111111</li><li>22222222222</li><li>33333333333</li><li>44444444444</li><li>55555555555</li></ol>',
            mounted: function () {
                this.start();
            },
            methods: {
                scroll: function () {
                    var lastEl = this.$el.firstChild;
                    while (lastEl.nodeType != 1)
                        lastEl = lastEl.nextSibling; // 找到最后一个元素
                    this.$el.appendChild(this.$el.removeChild(lastEl)); // 把最后一个元素放到前头
                }
            }
        });
        Vue.component('aj-marquee', {
            mixins: [carousel.marqueeBase],
            template: "\n            <div class=\"aj-simple-marquee\" style=\"width: 100%; overflow: hidden;\">\n                <div class=\"items\"><slot></slot></div>\n                <div class=\"clone\"></div>\n            </div>\n        ",
            props: {
                interval: { default: 20 },
                pauseInterval: { type: Number, default: 2000 },
                itemHeight: { type: Number, default: 20 } // 每一项的高度
            },
            mounted: function () {
                var el = this.$el, children = el.$('.items').children, itemHeight = this.itemHeight;
                el.style.height = itemHeight + "px";
                var allHeight = 0;
                for (var i = 0, j = children.length; i < j; i++) { // 设置每行高度
                    var item = children[i];
                    item.style.display = 'block';
                    item.style.height = itemHeight + "px";
                    allHeight += itemHeight;
                }
                el.$('.clone').style.height = allHeight + 'px'; // 相同高度
                // 复制第一个元素
                var clone = children[0].cloneNode(true);
                el.$('.clone').appendChild(clone);
                setTimeout(this.start.bind(this), 2000);
            },
            methods: {
                scroll: function () {
                    var el = this.$el, top = el.scrollTop, height = el.$('.items').clientHeight;
                    if (top <= height) {
                        el.scrollTop++;
                        if (top != 0 && (top % this.itemHeight) === 0) {
                            this.clearTimer();
                            setTimeout(this.start.bind(this), this.pauseInterval);
                        }
                    }
                    else { // 第一个恰好滑完
                        el.scrollTop -= height; //返回至开头处
                    }
                }
            }
        });
    })(carousel = aj.carousel || (aj.carousel = {}));
})(aj || (aj = {}));
