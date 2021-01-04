"use strict";
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        document.addEventListener("DOMContentLoaded", function () {
            var msgEl = document.createElement('div');
            msgEl.className = 'aj-topMsg';
            msgEl.setAttribute('v-html', "showText");
            document.body.appendChild(msgEl);
            aj.msg = new Vue({
                el: msgEl,
                data: {
                    showText: '' // 显示的内容
                },
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
                            cfg && cfg.afterClose && cfg.afterClose(_this);
                        }, cfg && cfg.showTime || 3000);
                    }
                }
            });
        });
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));
