"use strict";
var aj;
(function (aj) {
    var wysiwyg;
    (function (wysiwyg) {
        wysiwyg.statusBar = new Vue({
            el: '.statusBar',
            data: {
                show: false,
                text: ''
            },
            mounted: function () {
                var _this = this;
                setTimeout(function () { return _this.$el.classList.remove('hide'); }, 900);
            },
            methods: {
                showMsg: function (text) {
                    var _this = this;
                    this.text = text;
                    this.show = true;
                    if (this.timer)
                        clearTimeout(this.timer);
                    this.timer = setTimeout(function () { return _this.show = false; }, 4000);
                }
            }
        });
        // 舞台中央区域
        wysiwyg.center = new Vue({
            el: 'body > .center',
            data: {
                codeMode: false,
                focusEl: null
            },
            mounted: function () {
                var arr = this.$el.querySelectorAll('input');
                // 输入框禁止显示历史记录
                for (var i = 0, j = arr.length; i < j; i++)
                    arr[i].setAttribute('autocomplete', 'off');
            },
            methods: {
                toggleCodeMode: function () {
                    this.codeMode = !this.codeMode;
                },
            },
            watch: {
                focusEl: function (el, old) {
                    aj.fb.PropertyEditor.name = el.name;
                    aj.fb.PropertyEditor.placeHolder = el.placeholder;
                    el.style.borderColor = 'red';
                    if (old)
                        old.style.borderColor = '';
                }
            }
        });
    })(wysiwyg = aj.wysiwyg || (aj.wysiwyg = {}));
})(aj || (aj = {}));
