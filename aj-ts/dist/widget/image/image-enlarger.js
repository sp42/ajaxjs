"use strict";
var aj;
(function (aj) {
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
    })(img = aj.img || (aj.img = {}));
})(aj || (aj = {}));
