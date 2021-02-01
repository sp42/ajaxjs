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
