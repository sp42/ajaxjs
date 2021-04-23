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
                onStageClk: function () {
                    var el = event.target;
                    if ('INPUT' === el.tagName)
                        this.focusEl = el;
                },
                onDragEnter: function () {
                    var div = event.currentTarget;
                    //			debugger;
                    // 当拖拽元素进入潜在放置区域时，高亮处理
                    div.style.backgroundColor = '#ead1d1';
                    event.dataTransfer.dropEffect = 'copy';
                },
                onDragLeave: function () {
                    var div = event.currentTarget;
                    div.style.backgroundColor = '';
                },
                onDrop: function () {
                    this.onDragLeave();
                    var div = event.currentTarget;
                    var text = event.dataTransfer.getData("text");
                    var el;
                    switch (text) {
                        case 'Text Field':
                            el = document.createElement('input');
                            el.type = 'text';
                            div.$('label').appendChild(el);
                            break;
                    }
                    this.focusEl = el;
                }
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
