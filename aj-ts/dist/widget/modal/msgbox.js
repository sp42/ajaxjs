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
                    template: "\n                <div class=\"aj-modal hide\" @click=\"close\">\n                    <div>\n                        <div v-html=\"showText\"></div>\n                        <div class=\"aj-btnsHolder\">\n                            <button v-show=\"showOk\"  @click=\"onBtnClk\" class=\"ok\">\u786E\u5B9A</button>\n                            <button v-show=\"showYes\" @click=\"onBtnClk\" class=\"yes\">{{showSave? '\u4FDD\u5B58': '\u662F'}}</button>\n                            <button v-show=\"showNo\"  @click=\"onBtnClk\" class=\"no\">{{showSave? '\u5426': '\u5426'}}</button>\n                        </div>\n                    </div>\n                </div>\n            ",
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
