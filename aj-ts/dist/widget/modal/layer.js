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
                        isClosed = aj.widget.modal.msgbox.$options.methods.close.call(this, {
                            target: document.body.$('.aj-modal')
                        });
                    }
                    else {
                        // @ts-ignore
                        if (e.isForceClose || !this.notCloseWhenTap)
                            isClosed = aj.widget.modal.msgbox.$options.methods.close.apply(this, arguments);
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
