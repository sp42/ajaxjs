"use strict";
/**
 * 消息框、弹窗、对话框组件
 */
var aj;
(function (aj) {
    var widget;
    (function (widget) {
        document.addEventListener("DOMContentLoaded", function () {
            document.body.appendChild(document.createElement('div')).className = 'alertHolder';
            // 全屏幕弹窗，居中显示文字。
            // 不应直接使用该组件，而是执行 aj.showOk
            widget.msgbox = new Vue({
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
                     * @param text
                     * @param cfg
                     */
                    show: function (text, cfg) {
                        this.showText = text;
                        this.$el.classList.remove('hide');
                        aj.apply(this, cfg);
                        return this;
                    },
                    close: function (e) {
                        if (!e) { // 直接关闭
                            this.$el.classList.add('hide');
                            this.afterClose && this.afterClose(this);
                            return true;
                        }
                        var div = e.target; // check if in the box
                        if (div && div.className.indexOf('modal') != -1) {
                            this.$el.classList.add('hide');
                            this.afterClose && this.afterClose(div, this);
                            return true;
                        }
                    },
                    onBtnClk: function (e) {
                        var el = e.target;
                        switch (el.className) {
                            case 'ok':
                                this.onOkClk && this.onOkClk(e, this);
                                break;
                            case 'no':
                                this.onNoClk && this.onNoClk(e, this);
                                break;
                            case 'yes':
                                this.onYesClk && this.onYesClk(e, this);
                                break;
                        }
                    }
                }
            });
        });
        /**
         * 顯示確定的對話框
         *
         * @param {String} text 显示的文本
         * @param {Function} callback 回调函数
         */
        aj.alert = function (text, callback) {
            var alertObj = widget.msgbox.show(text, {
                showYes: false,
                showNo: false,
                showOk: true,
                onOkClk: function (e) {
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
            var alertObj = widget.msgbox.show(text, {
                showYes: true,
                showNo: true,
                showOk: false,
                showSave: showSave,
                onYesClk: function (e) {
                    alertObj.$el.classList.add('hide');
                    callback && callback(alertObj.$el, e);
                },
                onNoClk: function () {
                    alertObj.$el.classList.add('hide');
                }
            });
        };
        aj.simpleOk = function (text, callback) {
            var alertObj = widget.msgbox.show(text, {
                showYes: false,
                showNo: false,
                showOk: false,
                onOkClk: function () {
                    alertObj.$el.classList.add('hide');
                    callback && callback();
                }
            });
        };
    })(widget = aj.widget || (aj.widget = {}));
})(aj || (aj = {}));
