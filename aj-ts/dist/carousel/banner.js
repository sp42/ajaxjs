"use strict";
Vue.component('aj-banner', {
    mixins: [aj.carousel.base],
    template: "\n\t\t<div class=\"aj-carousel aj-banner\">\n\t\t\t<header>\n\t\t\t\t<ul v-show=\"showTitle\">\n\t\t\t\t\t<li v-for=\"(item, index) in items\" :class=\"{'hide': index !== selected}\">{{item.name}}</li>\n\t\t\t\t</ul>\n\t\t\t\t<ol v-show=\"showDot\">\n\t\t\t\t\t<li v-for=\"n in items.length\" :class=\"{'active': (n - 1) === selected}\" @click=\"changeTab(n - 1);\"></li>\n\t\t\t\t</ol>\n\t\t\t</header>\n\t\t\t<div class=\"content\">\n\t\t\t\t<div v-for=\"(item, index) in items\" :class=\"{'active': index === selected}\" v-html=\"getContent(item.content, item.href)\">\n\t\t\t\t</div>\n\t\t\t</div>\n\t\t</div>\n\t",
    props: {
        isUsePx: { default: true },
        isGetCurrentHeight: { default: false },
        autoLoop: { type: Number, default: 4000 },
        showTitle: { type: Boolean, default: false },
        showDot: { type: Boolean, default: true } // 是否显示一点点，一般显示了标题就不显示点
    },
    data: function () {
        return {
            items: this.initItems || [
                { name: '杜甫：望岳', content: '<img src="images/1.jpg" />', href: 'http://qq.com' },
                { name: '资质证照', content: '<img src="images/2.jpg" />', href: 'javascript:alert(9);' },
                { name: '资质证照', content: '<img src="images/3.jpg" />', href: '#' }
            ]
        };
    },
    mounted: function () {
        // this.loop();
    },
    methods: {
        /**
         *
         * @param this
         */
        loop: function () {
            if (this.autoLoop)
                window.setInterval(this.goNext.bind(this), this.autoLoop);
        },
        /**
         *
         * @param content
         * @param href
         */
        getContent: function (content, href) {
            if (!href)
                return content;
            else
                return '<a href="' + href + '">' + content + '</a>';
        }
    }
});
