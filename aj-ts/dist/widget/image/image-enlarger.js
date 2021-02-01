"use strict";
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var img;
        (function (img) {
            function initImageEnlarger() {
                img.imageEnlarger = new Vue({
                    el: document.body.appendChild(document.createElement('div')),
                    template: '<div class="aj-image-large-view"><div><img :src="imgUrl" /></div></div>',
                    data: {
                        imgUrl: null
                    },
                    mounted: function () {
                        var _this = this;
                        // @ts-ignore
                        document.addEventListener('mousemove', aj.throttle(function (e) {
                            if (_this.imgUrl) {
                                var w = 0, imgWidth = _this.$el.$('img').clientWidth;
                                if (imgWidth > e.pageX)
                                    w = imgWidth;
                                var el = _this.$el.$('div');
                                el.style.top = (e.pageY + 20) + 'px';
                                el.style.left = (e.pageX - el.clientWidth + w) + 'px';
                            }
                        }, 50, 5000), false);
                    }
                });
                return img.imageEnlarger;
            }
            img.initImageEnlarger = initImageEnlarger;
            Vue.component('aj-img-thumb', {
                template: "<a class=\"aj-img-thumb\" :href=\"getImgUrl\" v-if=\"imgUrl\" target=\"_blank\">\n                <img :src=\"getImgUrl\"\n                    :onmouseenter=\"'aj.widget.img.imageEnlarger.imgUrl = \\'' + getImgUrl + '\\';'\" \n                    onmouseleave=\"aj.widget.img.imageEnlarger.imgUrl = null;\" />\n            </a>",
                props: {
                    imgUrl: String
                },
                computed: {
                    getImgUrl: function () {
                        if (!this.imgUrl)
                            return "";
                        if (this.imgUrl.indexOf('http') != -1) // 图片地址已经是完整的 http 地址，直接返回
                            return this.imgUrl;
                        // uploadFile.imgPerfix
                        if (!this.ajResources.imgPerfix)
                            throw "未提供图片前缀地址";
                        return this.ajResources.imgPerfix + this.imgUrl;
                    }
                }
            });
        })(img = widget.img || (widget.img = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));
