"use strict";
/**
 * 折叠菜单
 */
;
(function () {
    Vue.component('aj-accordion-menu', {
        template: '<ul class="aj-accordion-menu" @click="onClk"><slot></slot></ul>',
        methods: {
            onClk: function ($event) {
                this.children = this.$el.children;
                highlightSubItem($event);
                var _btn = $event.target;
                if (_btn && _btn.tagName == 'H3' && _btn.parentNode.tagName == 'LI') {
                    _btn = $event.target;
                    _btn = _btn.parentNode;
                    for (var btn, i = 0, j = this.children.length; i < j; i++) {
                        btn = this.children[i];
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
            }
        }
    });
    /**
     * 内部子菜单的高亮
     *
     * @param $event
     */
    function highlightSubItem($event) {
        var _a;
        var li, el = $event.target;
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
})();
