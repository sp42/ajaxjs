"use strict";
/**
 * 调整正文字体大小
 */
Vue.component('aj-adjust-font-size', {
    template: "\n        <div class=\"aj-adjust-font-size\" @click=\"onClk\">\n            <span>\u5B57\u4F53\u5927\u5C0F</span>\n            <ul>\n                <li><label><input type=\"radio\" name=\"fontSize\" /> \u5C0F</label></li>\n                <li><label><input type=\"radio\" name=\"fontSize\" /> \u4E2D</label></li>\n                <li><label><input type=\"radio\" name=\"fontSize\" /> \u5927</label></li>\n            </ul>\n        </div>\n    ",
    props: {
        articleTarget: { type: String, default: 'article p' } // 正文所在的位置，通过 CSS Selector 定位
    },
    methods: {
        onClk: function (e) {
            var _this = this;
            var el = e.target;
            var setFontSize = function (fontSize) {
                document.body.$(_this.$props.articleTarget, function (p) { return p.style.fontSize = fontSize; });
            };
            if (el.tagName != 'LABEL')
                el = el.up('label');
            if (el.innerHTML.indexOf('大') != -1)
                setFontSize('12pt');
            else if (el.innerHTML.indexOf('中') != -1)
                setFontSize('10.5pt');
            else if (el.innerHTML.indexOf('小') != -1)
                setFontSize('9pt');
        }
    }
});
