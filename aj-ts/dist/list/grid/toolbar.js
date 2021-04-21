"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
var aj;
(function (aj) {
    var list;
    (function (list) {
        var grid;
        (function (grid) {
            /**
             * 工具条
             */
            Vue.component('aj-entity-toolbar', {
                template: html(__makeTemplateObject(["\n            <div class=\"toolbar\">\n                <form v-if=\"search\" class=\"right\">\n                    <input type=\"text\" name=\"keyword\" placeholder=\"\u8BF7\u8F93\u5165\u5173\u952E\u5B57\" size=\"12\" />\n                    <button @click=\"doSearch\"><i class=\"fa fa-search\" style=\"color:#417BB5;\"></i>\u641C\u7D22</button>\n                </form>\n                <aj-form-between-date v-if=\"betweenDate\" class=\"right\" :is-ajax=\"true\"></aj-form-between-date>\n                <ul>\n                    <li v-if=\"create\" @click=\"$emit('on-create-btn-clk')\"><i class=\"fa fa-plus\" style=\"color:#0a90f0;\"></i> \u65B0\u5EFA</li>\n                    <li v-if=\"save\" @click=\"$emit('on-save-btn-clk')\"><i class=\"fa fa-floppy-o\"\n                            style=\"color:rgb(205, 162, 4);\"></i>\u4FDD\u5B58</li>\n                    <li v-if=\"deleBtn\" @click=\"$emit('on-delete-btn-clk')\"><i class=\"fa fa-trash-o\" style=\"color:red;\"></i> \u5220\u9664</li>\n                    <li v-if=\"excel\"><i class=\"fa fa-file-excel-o\" style=\"color:green;\"></i> \u5BFC\u51FA</li>\n                    <slot></slot>\n                </ul>\n            </div>\n        "], ["\n            <div class=\"toolbar\">\n                <form v-if=\"search\" class=\"right\">\n                    <input type=\"text\" name=\"keyword\" placeholder=\"\u8BF7\u8F93\u5165\u5173\u952E\u5B57\" size=\"12\" />\n                    <button @click=\"doSearch\"><i class=\"fa fa-search\" style=\"color:#417BB5;\"></i>\u641C\u7D22</button>\n                </form>\n                <aj-form-between-date v-if=\"betweenDate\" class=\"right\" :is-ajax=\"true\"></aj-form-between-date>\n                <ul>\n                    <li v-if=\"create\" @click=\"$emit('on-create-btn-clk')\"><i class=\"fa fa-plus\" style=\"color:#0a90f0;\"></i> \u65B0\u5EFA</li>\n                    <li v-if=\"save\" @click=\"$emit('on-save-btn-clk')\"><i class=\"fa fa-floppy-o\"\n                            style=\"color:rgb(205, 162, 4);\"></i>\u4FDD\u5B58</li>\n                    <li v-if=\"deleBtn\" @click=\"$emit('on-delete-btn-clk')\"><i class=\"fa fa-trash-o\" style=\"color:red;\"></i> \u5220\u9664</li>\n                    <li v-if=\"excel\"><i class=\"fa fa-file-excel-o\" style=\"color:green;\"></i> \u5BFC\u51FA</li>\n                    <slot></slot>\n                </ul>\n            </div>\n        "])),
                props: {
                    betweenDate: { type: Boolean, default: true },
                    create: { type: Boolean, default: true },
                    save: { type: Boolean, default: true },
                    excel: { type: Boolean, default: false },
                    deleBtn: { type: Boolean, default: true },
                    search: { type: Boolean, default: true }
                },
                methods: {
                    /**
                     * 获取关键字进行搜索
                     *
                     * @param this
                     * @param ev
                     */
                    doSearch: function (ev) {
                        ev.preventDefault();
                        aj.apply(this.$parent.$store.extraParam, { keyword: aj.form.utils.getFormFieldValue(this.$el, 'input[name=keyword]') });
                        this.$parent.$store.reload();
                    }
                }
            });
        })(grid = list.grid || (list.grid = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
