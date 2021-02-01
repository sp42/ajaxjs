"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
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
