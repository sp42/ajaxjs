"use strict";

/**
 * 折叠菜单
 */
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var AccordionMenu = /** @class */ (function (_super) {
            __extends(AccordionMenu, _super);
            function AccordionMenu() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-accordion-menu";
                _this.template = '<ul class="aj-accordion-menu" @click="onClk"><slot></slot></ul>';
                return _this;
            }
            AccordionMenu.prototype.onClk = function (ev) {
                var children = this.$el.children;
                highlightSubItem(ev);
                var _btn = ev.target;
                if (_btn && _btn.tagName == 'H3' && _btn.parentNode.tagName == 'LI') {
                    _btn = _btn.parentNode;
                    for (var btn = void 0, i = 0, j = children.length; i < j; i++) {
                        btn = children[i];
                        var ul = btn.querySelector('ul');
                        if (btn == _btn) {
                            if (btn.className.indexOf('pressed') != -1) {
                                btn.classList.remove('pressed'); // 再次点击，隐藏！
                                if (ul)
                                    ul.style.height = '0px';
                            }
                            else {
                                if (ul)
                                    ul.style.height = ul.scrollHeight + 'px';
                                btn.classList.add('pressed');
                            }
                        }
                        else {
                            btn.classList.remove('pressed');
                            if (ul)
                                ul.style.height = '0px';
                        }
                    }
                }
                else
                    return;
            };
            return AccordionMenu;
        }(aj.VueComponent));
        widget.AccordionMenu = AccordionMenu;
        new AccordionMenu().register();
        /**
         * 内部子菜单的高亮
         *
         * @param ev
         */
        function highlightSubItem(ev) {
            var _a;
            var li, el = ev.target;
            if (el.tagName == 'A' && el.getAttribute('target')) {
                li = el.parentNode;
                (_a = li.parentNode) === null || _a === void 0 ? void 0 : _a.$('li', function (_el) {
                    if (_el == li)
                        _el.classList.add('selected');
                    else
                        _el.classList.remove('selected');
                });
            }
        }
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var widget;
    (function (widget) {
        /**
         * Baidu 自定义搜索
         */
        var BaiduSearch = /** @class */ (function (_super) {
            __extends(BaiduSearch, _super);
            function BaiduSearch() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = 'aj-widget-baidu-search';
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-widget-baidu-search\">\n                <form method=\"GET\" action=\"http://www.baidu.com/baidu\" onsubmit=\"//return g(this);\">\n                    <input type=\"text\" name=\"word\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" />\n                    <input name=\"tn\" value=\"bds\" type=\"hidden\" />\n                    <input name=\"cl\" value=\"3\" type=\"hidden\" />\n                    <input name=\"ct\" value=\"2097152\" type=\"hidden\" />\n                    <input name=\"si\" :value=\"getSiteDomainName\" type=\"hidden\" />\n                    <div class=\"searchBtn\" onclick=\"this.parentNode.submit();\"></div>\n                </form>\n            </div>\n        "], ["\n            <div class=\"aj-widget-baidu-search\">\n                <form method=\"GET\" action=\"http://www.baidu.com/baidu\" onsubmit=\"//return g(this);\">\n                    <input type=\"text\" name=\"word\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" />\n                    <input name=\"tn\" value=\"bds\" type=\"hidden\" />\n                    <input name=\"cl\" value=\"3\" type=\"hidden\" />\n                    <input name=\"ct\" value=\"2097152\" type=\"hidden\" />\n                    <input name=\"si\" :value=\"getSiteDomainName\" type=\"hidden\" />\n                    <div class=\"searchBtn\" onclick=\"this.parentNode.submit();\"></div>\n                </form>\n            </div>\n        "]));
                _this.siteDomainName = String;
                _this.computed = {
                    getSiteDomainName: function () {
                        //@ts-ignore
                        return this.siteDomainName || location.host || document.domain;
                    }
                };
                return _this;
            }
            return BaiduSearch;
        }(aj.VueComponent));
        widget.BaiduSearch = BaiduSearch;
        new BaiduSearch().register();
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var widget;
    (function (widget) {
        /**
         * 展开闭合器
         */
        var Expander = /** @class */ (function (_super) {
            __extends(Expander, _super);
            function Expander() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = "aj-expander";
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-expander\" :style=\"'height:' + (expended ? openHeight : closeHeight) + 'px;'\">\n                <div :class=\"expended ? 'closeBtn' : 'openBtn'\" @click=\"expended = !expended;\"></div>\n                <slot></slot>\n            </div>\n        "], ["\n            <div class=\"aj-expander\" :style=\"'height:' + (expended ? openHeight : closeHeight) + 'px;'\">\n                <div :class=\"expended ? 'closeBtn' : 'openBtn'\" @click=\"expended = !expended;\"></div>\n                <slot></slot>\n            </div>\n        "]));
                _this.expended = false;
                _this.openHeight = { type: Number, "default": 200 };
                _this.closeHeight = { type: Number, "default": 50 };
                return _this;
            }
            return Expander;
        }(aj.VueComponent));
        widget.Expander = Expander;
        new Expander().register();
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var widget;
    (function (widget) {
        /**
         * 进度条
         */
        var ProcessLine = /** @class */ (function (_super) {
            __extends(ProcessLine, _super);
            function ProcessLine() {
                var _this = _super !== null && _super.apply(this, arguments) || this;
                _this.name = 'aj-process-line';
                _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-process-line\">\n                <div class=\"process-line\">\n                    <div v-for=\"(item, index) in items\" :class=\"{current: index == current, done: index < current}\">\n                        <span>{{index + 1}}</span>\n                        <p>{{item}}</p>\n                    </div>\n                </div>\n            </div>\n        "], ["\n            <div class=\"aj-process-line\">\n                <div class=\"process-line\">\n                    <div v-for=\"(item, index) in items\" :class=\"{current: index == current, done: index < current}\">\n                        <span>{{index + 1}}</span>\n                        <p>{{item}}</p>\n                    </div>\n                </div>\n            </div>\n        "]));
                _this.props = {
                    items: {
                        type: Array,
                        default: function () {
                            return ['Step 1', 'Step 2', 'Step 3'];
                        }
                    }
                };
                _this.items = [];
                _this.current = 0;
                return _this;
            }
            /**
             *
             * @param i
             */
            ProcessLine.prototype.go = function (i) {
                this.current = i;
            };
            /**
             *
             */
            ProcessLine.prototype.perv = function () {
                var perv = this.current - 1;
                if (perv < 0)
                    perv = this.items.length - 1;
                this.go(perv);
            };
            /**
             *
             */
            ProcessLine.prototype.next = function () {
                var next = this.current + 1;
                if (this.items.length == next)
                    next = 0; // 循环
                this.go(next);
            };
            return ProcessLine;
        }(aj.VueComponent));
        widget.ProcessLine = ProcessLine;
        new ProcessLine().register();
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";
/**
 * Tab 组件
 *
 * 参考：https://vuejs.org/v2/guide/components.html#Content-Distribution-with-Slots
 */
Vue.component('aj-tab', {
    template: "\n        <div :class=\"isVertical ? 'aj-simple-tab-vertical' : 'aj-tab' \">\n\t      <button v-for=\"tab in tabs\" v-bind:key=\"tab.name\"\n\t        v-bind:class=\"['tab-button', { active: currentTab.name === tab.name }]\"\n\t        v-on:click=\"currentTab = tab\">{{tab.name}}\n\t      </button>\n\t      <component v-bind:is=\"currentTab.component\" class=\"tab\"></component>\n        </div>\n    ",
    props: {
        isVertical: Boolean // 是否垂直方向的布局，默认 false,
    },
    data: function () {
        return {
            tabs: [],
            currentTab: {}
        };
    },
    mounted: function () {
        var arr = this.$slots.default;
        for (var i = 0; i < arr.length; i++) {
            var el = arr[i];
            if (el.tag === 'textarea') {
                this.tabs.push({
                    name: el.data.attrs['data-title'],
                    component: {
                        template: '<div>' + el.children[0].text + "</div>"
                    }
                });
            }
        }
        this.currentTab = this.tabs[0];
    }
});
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var tab;
        (function (tab) {
            tab.tabable = {
                mounted: function () {
                    var _this = this;
                    var ul = this.$el.$('.aj-simple-tab-horizontal > ul');
                    ul.onclick = function (e) {
                        var _a, _b;
                        var el = e.target;
                        select(el);
                        var index = Array.prototype.indexOf.call((_a = el.parentElement) === null || _a === void 0 ? void 0 : _a.children, el);
                        // @ts-ignore
                        var _new = (_b = _this.$el.$('.aj-simple-tab-horizontal > div')) === null || _b === void 0 ? void 0 : _b.children[index];
                        select(_new);
                    };
                    // @ts-ignore
                    ul.onclick({ target: ul.children[0] });
                    //this.$options.watch.selected.call(this, 0);
                }
            };
            // 按次序选中目标
            var select = function (_new) {
                var oldSelected = _new.parentNode.$('.selected');
                if (_new === oldSelected) // 没变化
                    return;
                oldSelected && oldSelected.classList.remove('selected');
                _new.classList.add('selected');
            };
        })(tab = widget.tab || (widget.tab = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";


var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var img;
        (function (img) {
            /**
             * 显示头像
             */
            var Avatar = /** @class */ (function (_super) {
                __extends(Avatar, _super);
                function Avatar() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = 'aj-avatar';
                    _this.template = html(__makeTemplateObject(["\n            <a :href=\"avatar\" target=\"_blank\">\n                <img :src=\"avatar\" style=\"max-width:50px;max-height:60px;vertical-align: middle;\" \n                    @mouseenter=\"mouseEnter\"\n                    @mouseleave=\"mouseLeave\" />\n            </a>\n        "], ["\n            <a :href=\"avatar\" target=\"_blank\">\n                <img :src=\"avatar\" style=\"max-width:50px;max-height:60px;vertical-align: middle;\" \n                    @mouseenter=\"mouseEnter\"\n                    @mouseleave=\"mouseLeave\" />\n            </a>\n        "]));
                    _this.props = {
                        avatar: { type: String, required: true }
                    };
                    /**
                     * 头像图片地址
                     */
                    _this.avatar = "";
                    return _this;
                }
                Avatar.prototype.mouseEnter = function () {
                    if (img.imageEnlarger)
                        img.imageEnlarger.imgUrl = this.avatar;
                };
                Avatar.prototype.mouseLeave = function () {
                    if (img.imageEnlarger)
                        img.imageEnlarger.imgUrl = null;
                };
                return Avatar;
            }(aj.VueComponent));
            img.Avatar = Avatar;
            new Avatar().register();
        })(img = widget.img || (widget.img = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

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

"use strict";
/**
 * 图片工具库
 */
var aj;
(function (aj) {
    var img;
    (function (img_1) {
        /**
         * 为了避免压缩图片变形，一般采用等比缩放，
         * 首先要计算出原始图片宽高比 aspectRatio，
         * 用户设置的高乘以 aspectRatio，得出等比缩放后的宽，
         * 若比用户设置宽的小，则用户设置的高为为基准缩放，否则以宽为基准缩放。
         *
         * @param originWidth
         * @param originHeight
         * @param maxWidth
         * @param maxHeight
         */
        function fitSize(originWidth, originHeight, maxWidth, maxHeight) {
            // 目标尺寸
            var targetWidth = originWidth, targetHeight = originHeight;
            // 图片尺寸超过的限制
            if (originWidth > maxWidth || originHeight > maxHeight) {
                if (originWidth / originHeight > maxWidth / maxHeight) {
                    // 更宽，按照宽度限定尺寸
                    targetWidth = maxWidth;
                    targetHeight = Math.round(maxWidth * (originHeight / originWidth));
                }
                else {
                    targetHeight = maxHeight;
                    targetWidth = Math.round(maxHeight * (originWidth / originHeight));
                }
            }
            return { targetWidth: targetWidth, targetHeight: targetHeight };
        }
        img_1.fitSize = fitSize;
        /**
         * 图片对象转换为 Canvas
         *
         * @param img
         * @param targetWidth
         * @param targetHeigh
         */
        function imgObj2Canvas(img, targetWidth, targetHeigh) {
            var _a;
            var canvas = document.createElement('canvas');
            canvas.width = targetWidth || img.width;
            canvas.height = targetHeigh || img.height;
            (_a = canvas.getContext('2d')) === null || _a === void 0 ? void 0 : _a.drawImage(img, 0, 0);
            return canvas;
        }
        img_1.imgObj2Canvas = imgObj2Canvas;
        /**
        * Canvas.toDataURL() 也能调整图片大小（压缩图片），但不推荐
        * 参见：https://www.zhihu.com/question/59267048
        * 除了作为转换图片的 base64 编码的时候
        *
        * 推荐使用 reszieAsBlob()
        *
        * @param img
        * @param targetWidth
        * @param targetHeight
        * @param format         目标格式，mime 格式
        * @param quality        介于 0-1 之间的数字，用于控制输出图片质量，仅当格式为 jpg 和 webp 时才支持质量，png 时 quality 参数无效
        */
        function reszieCompressCompressAsDataURL(img, targetWidth, targetHeight, format, quality) {
            if (format === void 0) { format = 'image/jpeg'; }
            if (quality === void 0) { quality = .9; }
            return imgObj2Canvas(img, targetWidth, targetHeight).toDataURL(format, quality);
        }
        img_1.reszieCompressCompressAsDataURL = reszieCompressCompressAsDataURL;
        /**
         * 转换图片为 base64 编码
         * 如果你要展示 blob 图片，更推荐用 img.src = URL.createObjectURL(newBlob); 方法，这个 base64 性能耗损比较大
         *
         * @param img
         */
        function img2base64(img) {
            return reszieCompressCompressAsDataURL(img, img.width, img.height);
        }
        img_1.img2base64 = img2base64;
        /**
         * 调整图片大小（压缩图片）异步方法
         *
         * @param img
         * @param targetWidth
         * @param targetHeight
         * @param cb
         * @param format         目标格式，mime 格式
         * @param quality        介于 0-1 之间的数字，用于控制输出图片质量，仅当格式为 jpg 和 webp 时才支持质量，png 时 quality 参数无效
         */
        function reszieCompressAsBlob(img, targetWidth, targetHeight, cb, format, quality) {
            if (format === void 0) { format = 'image/jpeg'; }
            if (quality === void 0) { quality = .9; }
            imgObj2Canvas(img, targetWidth, targetHeight).toBlob(function (blob) { return blob && cb(blob); }, format, quality);
        }
        img_1.reszieCompressAsBlob = reszieCompressAsBlob;
        function compressAsBlob(img, cb, format, quality) {
            if (format === void 0) { format = 'image/jpeg'; }
            if (quality === void 0) { quality = .9; }
            return reszieCompressAsBlob(img, img.width, img.height, cb, format, quality);
        }
        img_1.compressAsBlob = compressAsBlob;
        /**
         * 对粘贴板的图片，都是原始 bitmap 比较大，压缩一下
         *
         * @param blob  图片对象
         * @param cb    转换成功回调，接收一个新的 blob 对象作为参数
         */
        function changeBlobImageQuality(blob, cb) {
            var img = new Image();
            img.onload = function () { return compressAsBlob(img, cb); };
            // 也可以用 FileReader 转 base64，但 img 实际支持 blob 加载的
            img.src = URL.createObjectURL(blob);
        }
        img_1.changeBlobImageQuality = changeBlobImageQuality;
        // EXIF {
        //     getTag(ob: any, ori: string):number;
        // }
        /**
         * 获取图片的方向
         *
         * @param img
         */
        function getPhotoOrientation(img) {
            var orient = 0;
            EXIF.getData(img, function () {
                //@ts-ignore
                orient = EXIF.getTag(this, 'Orientation');
            });
            return orient;
        }
        img_1.getPhotoOrientation = getPhotoOrientation;
        /**
         * 调整图片方向，返回新图片的 Base64 编码
         *
         * @param img
         * @param orient
         */
        function rotate(img, orient) {
            var width = img.width, height = img.height, canvas = document.createElement('canvas'), ctx = canvas.getContext("2d");
            // set proper canvas dimensions before transform & export
            if ([5, 6, 7, 8].indexOf(orient) > -1) {
                canvas.width = height;
                canvas.height = width;
            }
            else {
                canvas.width = width;
                canvas.height = height;
            }
            switch (orient) { // transform context before drawing image
                case 2:
                    ctx.transform(-1, 0, 0, 1, width, 0);
                    break;
                case 3:
                    ctx.transform(-1, 0, 0, -1, width, height);
                    break;
                case 4:
                    ctx.transform(1, 0, 0, -1, 0, height);
                    break;
                case 5:
                    ctx.transform(0, 1, 1, 0, 0, 0);
                    break;
                case 6:
                    ctx.transform(0, 1, -1, 0, height, 0);
                    break;
                case 7:
                    ctx.transform(0, -1, -1, 0, height, width);
                    break;
                case 8:
                    ctx.transform(0, -1, 1, 0, 0, width);
                    break;
                default:
                    ctx.transform(1, 0, 0, 1, 0, 0);
            }
            ctx.drawImage(img, 0, 0);
            return canvas.toDataURL('image/jpeg');
        }
        img_1.rotate = rotate;
        /**
         * 图片压缩
         *
         * @param imgObj
         */
        function compress(imgObj, vueCmp) {
            var maxWidth = 1000, maxHeight = 1500, fitSizeObj = fitSize(imgObj.width, imgObj.height, maxWidth, maxHeight), targetWidth = fitSizeObj.targetWidth, targetHeight = fitSizeObj.targetHeight;
            var orient = getPhotoOrientation(imgObj); // 获取照片的拍摄方向
            if (orient == 6) {
                targetWidth = fitSizeObj.targetHeight;
                targetHeight = fitSizeObj.targetWidth;
            }
            var comp = new Image();
            comp.onload = function () {
                var _a;
                var canvas = document.createElement('canvas');
                canvas.width = targetWidth;
                canvas.height = targetHeight;
                (_a = canvas.getContext('2d')) === null || _a === void 0 ? void 0 : _a.drawImage(comp, 0, 0, targetWidth, targetHeight); // 图片压缩
                canvas.toBlob(function (blob) {
                    vueCmp.$blob = blob;
                }, vueCmp.$fileType || 'image/jpeg');
            };
            comp.src = rotate(imgObj, orient);
        }
        img_1.compress = compress;
        /**
         *
         * @param dataurl
         */
        function dataURLtoBlob(dataurl) {
            // @ts-ignore
            var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1], bstr = atob(arr[1]), len = bstr.length, u8arr = new Uint8Array(len);
            while (len--)
                u8arr[len] = bstr.charCodeAt(len);
            return new Blob([u8arr], { type: mime });
        }
        img_1.dataURLtoBlob = dataURLtoBlob;
    })(img = aj.img || (aj.img = {}));
})(aj || (aj = {}));

"use strict";

var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var modal;
        (function (modal) {
            /**
             * 浮層組件，通常要復用這個組件
             */
            var Layer = /** @class */ (function (_super) {
                __extends(Layer, _super);
                function Layer() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = "aj-layer";
                    _this.template = '<div class="aj-modal hide" @click="close"><div><slot></slot></div></div>';
                    _this.props = {
                        notCloseWhenTap: Boolean,
                        cleanAfterClose: Boolean // 关闭是否清除
                    };
                    _this.afterClose = function () { };
                    _this.notCloseWhenTap = false;
                    _this.cleanAfterClose = false;
                    return _this;
                }
                /**
                 * 显示浮层
                 *
                 * @param cfg
                 */
                Layer.prototype.show = function (cfg) {
                    var _this = this;
                    var my = Number(getComputedStyle(this.$el).zIndex); // 保证最后显示的总在最前面
                    document.body.$('.aj-modal', function (i) {
                        if (i != _this.$el) {
                            var o = Number(getComputedStyle(i).zIndex);
                            if (o >= my)
                                _this.$el.style.zIndex = String(o + 1);
                        }
                    });
                    this.$el.classList.remove('hide');
                    this.BUS && this.BUS.$emit('aj-layer-closed', this);
                    if (cfg && cfg.afterClose)
                        this.afterClose = cfg && cfg.afterClose;
                };
                /**
                 * 关闭浮层
                 *
                 * @param ev
                 */
                Layer.prototype.close = function (ev) {
                    var isClosed = false;
                    if (!ev) {
                        isClosed = modal.msgbox.$options.methods.close.call(this, {
                            target: document.body.$('.aj-modal')
                        });
                    }
                    else {
                        // @ts-ignore
                        if (ev.isForceClose || !this.notCloseWhenTap)
                            isClosed = modal.msgbox.$options.methods.close.apply(this, arguments);
                    }
                    if (isClosed && this.cleanAfterClose) {
                        this.$el.parentNode && this.$el.parentNode.removeChild(this.$el);
                        this.$destroy();
                    }
                };
                return Layer;
            }(aj.VueComponent));
            modal.Layer = Layer;
            new Layer().register();
        })(modal = widget.modal || (widget.modal = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";

/**
 * 消息框、弹窗、对话框组件
 */
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var modal;
        (function (modal) {
            document.addEventListener("DOMContentLoaded", function () {
                document.body.appendChild(document.createElement('div')).className = 'alertHolder';
                // 全屏幕弹窗，居中显示文字。
                // 不应直接使用该组件，而是执行 aj.showOk
                modal.msgbox = new Vue({
                    el: '.alertHolder',
                    template: html(__makeTemplateObject(["\n                <div class=\"aj-modal hide\" @click=\"close\">\n                    <div>\n                        <div v-html=\"showText\"></div>\n                        <div class=\"aj-btnsHolder\">\n                            <button v-show=\"showOk\"  @click=\"onBtnClk\" class=\"ok\">\u786E\u5B9A</button>\n                            <button v-show=\"showYes\" @click=\"onBtnClk\" class=\"yes\">{{showSave? '\u4FDD\u5B58': '\u662F'}}</button>\n                            <button v-show=\"showNo\"  @click=\"onBtnClk\" class=\"no\">{{showSave? '\u5426': '\u5426'}}</button>\n                        </div>\n                    </div>\n                </div>\n            "], ["\n                <div class=\"aj-modal hide\" @click=\"close\">\n                    <div>\n                        <div v-html=\"showText\"></div>\n                        <div class=\"aj-btnsHolder\">\n                            <button v-show=\"showOk\"  @click=\"onBtnClk\" class=\"ok\">\u786E\u5B9A</button>\n                            <button v-show=\"showYes\" @click=\"onBtnClk\" class=\"yes\">{{showSave? '\u4FDD\u5B58': '\u662F'}}</button>\n                            <button v-show=\"showNo\"  @click=\"onBtnClk\" class=\"no\">{{showSave? '\u5426': '\u5426'}}</button>\n                        </div>\n                    </div>\n                </div>\n            "])),
                    data: {
                        showText: '',
                        afterClose: null,
                        showOk: false,
                        showYes: false,
                        showNo: false,
                        showSave: false // 是否显示“保存”按钮
                    },
                    methods: {
                        /**
                         * 显示
                         *
                         * @param this
                         * @param text  显示文字，支持 HTML 标签
                         * @param cfg   配置项，可选的
                         */
                        show: function (text, cfg) {
                            this.showText = text;
                            this.$el.classList.remove('hide');
                            cfg && aj.apply(this, cfg);
                            return this;
                        },
                        /**
                         * 关闭窗体
                         *
                         * @param this
                         * @param ev    事件对象，可选的
                         */
                        close: function (ev) {
                            if (!ev) { // 直接关闭
                                this.$el.classList.add('hide');
                                this.afterClose && this.afterClose(null, this);
                                return true;
                            }
                            var div = ev.target; // check if in the box
                            if (div && div.className.indexOf('modal') != -1) {
                                this.$el.classList.add('hide');
                                this.afterClose && this.afterClose(div, this);
                                return true;
                            }
                            return false;
                        },
                        onBtnClk: function (ev) {
                            switch (ev.target.className) {
                                case 'ok':
                                    this.onOkClk && this.onOkClk(ev, this);
                                    break;
                                case 'no':
                                    this.onNoClk && this.onNoClk(ev, this);
                                    break;
                                case 'yes':
                                    this.onYesClk && this.onYesClk(ev, this);
                                    break;
                            }
                        }
                    }
                });
            });
            /**
             * 顯示確定的對話框
             *
             * @param {String} text         显示的文本
             * @param {Function} callback   回调函数
             */
            aj.alert = function (text, callback) {
                var alertObj = modal.msgbox.show(text, {
                    showYes: false,
                    showNo: false,
                    showOk: true,
                    onOkClk: function () {
                        alertObj.$el.classList.add('hide');
                        callback && callback();
                    }
                });
            };
            /**
             * 顯示“是否”選擇的對話框
             *
             * @param {String} text         显示的文本
             * @param {Function} callback   回调函数
             */
            aj.showConfirm = function (text, callback, showSave) {
                var alertObj = modal.msgbox.show(text, {
                    showYes: true,
                    showNo: true,
                    showOk: false,
                    showSave: false,
                    onYesClk: function (ev) {
                        alertObj.$el.classList.add('hide');
                        callback && callback(alertObj.$el, ev);
                    },
                    onNoClk: function () {
                        alertObj.$el.classList.add('hide');
                    }
                });
            };
            modal.popup = function (text, callback) {
                modal.msgbox.show(text, {
                    showYes: false,
                    showNo: false,
                    showOk: false,
                    showSave: false
                });
            };
            //----------------------------------------------------------------------------------------
            /**
            * 顶部出现，用于后台提示信息多
            */
            document.addEventListener("DOMContentLoaded", function () {
                var msgEl = document.createElement('div');
                msgEl.className = 'aj-topMsg';
                msgEl.setAttribute('v-html', "showText");
                document.body.appendChild(msgEl);
                aj.msg = new Vue({
                    el: msgEl,
                    data: { showText: '' },
                    methods: {
                        show: function (text, cfg) {
                            var _this = this;
                            this.showText = text;
                            var el = this.$el;
                            setTimeout(function () {
                                el.classList.remove('fadeOut');
                                el.classList.add('fadeIn');
                            }, 0);
                            setTimeout(function () {
                                el.classList.remove('fadeIn');
                                el.classList.add('fadeOut');
                                cfg && cfg.afterClose && cfg.afterClose(el, _this);
                            }, cfg && cfg.showTime || 3000);
                        }
                    }
                });
            });
        })(modal = widget.modal || (widget.modal = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";


/**
 * 调整正文字体大小
 */
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var page;
        (function (page) {
            var AdjustFontSize = /** @class */ (function (_super) {
                __extends(AdjustFontSize, _super);
                function AdjustFontSize() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = 'aj-adjust-font-size';
                    _this.template = html(__makeTemplateObject(["\n            <div class=\"aj-adjust-font-size\">\n                <span>\u5B57\u4F53\u5927\u5C0F</span>\n                <ul @click=\"onClk\">\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5C0F</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u4E2D</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5927</label></li>\n                </ul>\n            </div>\n        "], ["\n            <div class=\"aj-adjust-font-size\">\n                <span>\u5B57\u4F53\u5927\u5C0F</span>\n                <ul @click=\"onClk\">\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5C0F</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u4E2D</label></li>\n                    <li><label><input type=\"radio\" name=\"fontSize\" /> \u5927</label></li>\n                </ul>\n            </div>\n        "]));
                    _this.articleTarget = { type: String, default: 'article p' }; // 正文所在的位置，通过 CSS Selector 定位
                    return _this;
                }
                AdjustFontSize.prototype.onClk = function (ev) {
                    var _this = this;
                    var el = ev.target;
                    var setFontSize = function (fontSize) {
                        document.body.$(_this.$props.articleTarget, function (p) { return p.style.fontSize = fontSize; });
                    };
                    if (el.tagName == 'LABEL' || el.tagName == 'input') {
                        if (el.tagName != 'LABEL')
                            el = el.up('label');
                        if (el.innerHTML.indexOf('大') != -1)
                            setFontSize('12pt');
                        else if (el.innerHTML.indexOf('中') != -1)
                            setFontSize('10.5pt');
                        else if (el.innerHTML.indexOf('小') != -1)
                            setFontSize('9pt');
                    }
                };
                return AdjustFontSize;
            }(aj.VueComponent));
            page.AdjustFontSize = AdjustFontSize;
            new AdjustFontSize().register();
        })(page = widget.page || (widget.page = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var back2topTimerId = 0;
        /**
         *  回到顶部  <a href="###" @click="go">回到顶部</a>
         */
        function back2top() {
            var top = 0, speed = 0;
            back2topTimerId && window.clearInterval(back2topTimerId);
            back2topTimerId = window.setInterval(function () {
                top = document.documentElement.scrollTop || document.body.scrollTop;
                speed = Math.floor((0 - top) / 8);
                if (top === 0)
                    clearInterval(back2topTimerId);
                else
                    document.documentElement.scrollTop = document.body.scrollTop = top + speed;
            }, 20);
        }
        widget.back2top = back2top;
        /**
         * 渲染浮动的按钮
         */
        function initBack2top() {
            var vue = new Vue({
                el: document.body.appendChild(document.createElement('div')),
                template: '<div @click="clk" class="aj-widget-back2top" title="回到顶部"><i class="fa fa-arrow-up" aria-hidden="true"></i> </div>',
                methods: {
                    clk: back2top
                }
            });
            var handler = aj.throttle(function () {
                vue.$el.style.top = (document.body.scrollTop + 100) + "px";
            }, 2000, 0);
            // @ts-ignore
            window.addEventListener('scroll', handler);
            handler();
        }
        widget.initBack2top = initBack2top;
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));

"use strict";
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        var page;
        (function (page) {
            var TraditionalChinese;
            (function (TraditionalChinese_1) {
                /*
                    正体中文
                    <span>
                         <a href="javascript:;" onclick="aj.widget.page.TraditionalChinese.toSimpleChinese(this);" class="simpleChinese selected">简体中文</a>
                        /<a href="javascript:;" onclick="aj.widget.page.TraditionalChinese.toChinese(this);" class="Chinese">正体中文</a>
                    </span>
                */
                var TraditionalChinese = 1, SimpleChinese = 2;
                /**
                 *
                 * 默认是否正体中文：true为正体；false简体。 HTTP
                 * 头读取：Request.ServerVariables("http_accept_language")
                 *
                 * @return {Boolean}
                 */
                function getClientLanguage() {
                    // @ts-ignore
                    var s = navigator.userLanguage || navigator.language;
                    switch (s.toLowerCase()) {
                        case 'zh-tw':
                            return TraditionalChinese;
                        case 'zh-cn':
                            return SimpleChinese;
                        default:
                            return 0;
                    }
                }
                var Cookie = {
                    set: function (name, val) {
                        var exp = new Date();
                        exp.setDate(exp.getDate() + 600 * 1000);
                        document.cookie = name + "=" + escape(val) + ";expires=" + exp.toUTCString();
                    },
                    del: function (name) {
                        document.cookie = name + "=;expires=" + (new Date(0)).toUTCString();
                    },
                    get: function (name) {
                        var cookieArray = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
                        return cookieArray != null ? unescape(cookieArray[2]) : "";
                    }
                };
                /**
                 * 转换对象，使用递归，逐层剥到文本
                 *
                 * @param {HTMLElement} obj 从document.body开始，
                 */
                function walk(el, coverntFn) {
                    var chidlNodes = el.childNodes;
                    var node;
                    for (var i = 0, j = chidlNodes.length; i < j; i++) {
                        node = chidlNodes.item(i);
                        // || (node == $$.big5.el)
                        if (("||BR|HR|TEXTAREA|".indexOf("|" + node.tagName + "|")) > 0)
                            continue;
                        if (node.title)
                            node.title = coverntFn(node.title);
                        // @ts-ignore
                        else if (node.alt)
                            // @ts-ignore
                            node.alt = coverntFn(node.alt);
                        // @ts-ignore
                        else if (node.tagName == "INPUT" && node.value != "" && node.type != "text" && node.type != "hidden")
                            // @ts-ignore
                            node.value = coverntFn(node.value);
                        else if (node.nodeType == 3)
                            // @ts-ignore
                            node.data = coverntFn(node.data);
                        else
                            walk(node, coverntFn);
                    }
                }
                /**
                 * 一对一替换字符
                 *
                 * @param text      输入的文本
                 * @param oldChars  原来所在的字符集
                 * @param newChars  要替换到的新字符集
                 */
                function translateText(text, oldChars, newChars) {
                    var str = [];
                    var char, charIndex, result;
                    for (var i = 0, j = text.length; i < j; i++) {
                        char = text.charAt(i);
                        charIndex = oldChars.indexOf(char);
                        result = newChars.charAt(charIndex);
                        str.push(charIndex != -1 ? result : char); // 匹配不到，用原来的字符
                    }
                    return str.join('');
                }
                var isLoaded = false;
                function loadChars(cb, el) {
                    var _a;
                    if (isLoaded)
                        cb();
                    else
                        aj.loadScript("https://framework.ajaxjs.com/src/widget/page/ChineseChars.js", "", function () {
                            isLoaded = true;
                            cb();
                        });
                    ((_a = el.up('span')) === null || _a === void 0 ? void 0 : _a.$(aj.SELECTED_CSS)).classList.remove(aj.SELECTED);
                    el.classList.add(aj.SELECTED);
                }
                var self = aj.widget.page.TraditionalChinese;
                var currentLanguageState; // 当前语言选择
                var cookieName = 'ChineseType';
                currentLanguageState = getClientLanguage();
                function toSimpleChinese(el) {
                    if (currentLanguageState === SimpleChinese) // 已经是，无须进行
                        return;
                    loadChars(function () {
                        //@ts-ignore
                        walk(document.body, translateText.delegate(null, self.正体中文, self.简化中文));
                        currentLanguageState = SimpleChinese;
                        Cookie.set(cookieName, currentLanguageState + "");
                    }, el);
                }
                TraditionalChinese_1.toSimpleChinese = toSimpleChinese;
                function toChinese(el) {
                    if (currentLanguageState === TraditionalChinese) // 已经是，无须进行
                        return;
                    loadChars(function () {
                        //@ts-ignore
                        walk(document.body, translateText.delegate(null, self.简化中文, self.正体中文));
                        currentLanguageState = TraditionalChinese;
                        Cookie.set(cookieName, currentLanguageState + "");
                    }, el);
                }
                TraditionalChinese_1.toChinese = toChinese;
                /**
                 * 初始化
                 */
                function init() {
                    var valueInCookie = Cookie.get(cookieName);
                    if (valueInCookie)
                        valueInCookie = Number(valueInCookie);
                    // 浏览器是繁体中文的，或者 Cookie 设置了是正体的，进行转换（当然默认文本是简体的）
                    if (currentLanguageState == TraditionalChinese || valueInCookie == TraditionalChinese)
                        toChinese(document.querySelector(".Chinese"));
                }
                TraditionalChinese_1.init = init;
            })(TraditionalChinese = page.TraditionalChinese || (page.TraditionalChinese = {}));
        })(page = widget.page || (widget.page = {}));
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));
