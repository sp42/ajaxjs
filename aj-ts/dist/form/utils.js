"use strict";
/**
 * 表单工具函数
 */
var aj;
(function (aj) {
    var form;
    (function (form) {
        var utils;
        (function (utils) {
            /**
             * 获取表单控件的值
             *
             * @param el
             * @param cssSelector
             */
            function getFormFieldValue(_el, cssSelector) {
                var el = _el.$(cssSelector);
                if (el)
                    return el.value;
                else
                    throw "\u627E\u4E0D\u5230" + cssSelector + "\u5143\u7D20";
            }
            utils.getFormFieldValue = getFormFieldValue;
            /**
             * 指定 id 的那个 option 选中
             *
             * @param this
             * @param id
             */
            function selectOption(id) {
                this.$el.$('option', function (i) {
                    console.log(i.value);
                    if (i.value == id)
                        i.selected = true;
                });
            }
            utils.selectOption = selectOption;
        })(utils = form.utils || (form.utils = {}));
    })(form = aj.form || (aj.form = {}));
})(aj || (aj = {}));
