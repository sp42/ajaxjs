"use strict";
/**
 * 工具条
 */
;
(function () {
    Vue.component('aj-entity-toolbar', {
        template: "\n            <div class=\"toolbar\">\n                <form v-if=\"search\" class=\"right\">\n                    <input type=\"text\" name=\"keyword\" placeholder=\"\u8BF7\u8F93\u5165\u5173\u952E\u5B57\" size=\"12\" />\n                    <button @click=\"doSearch\"><i class=\"fa fa-search\" style=\"color:#417BB5;\"></i>\u641C\u7D22</button>\n                </form>\n                <aj-form-between-date v-if=\"betweenDate\" class=\"right\"></aj-form-between-date>\n                <ul>\n                    <li v-if=\"create\" @click=\"$emit('on-create-btn-clk')\"><i class=\"fa fa-plus\" style=\"color:#0a90f0;\"></i> \u65B0\u5EFA</li>\n                    <li v-if=\"save\" @click=\"$emit('on-save-btn-clk')\"><i class=\"fa fa-floppy-o\" style=\"color:rgb(205, 162, 4);\"></i> \u4FDD\u5B58</li>\n                    <li v-if=\"deleBtn\" @click=\"BUS.$emit('on-delete-btn-clk')\"><i class=\"fa fa-trash-o\" style=\"color:red;\"></i> \u5220\u9664</li>\n                    <li v-if=\"excel\"><i class=\"fa fa-file-excel-o\" style=\"color:green;\"></i> \u5BFC\u51FA</li>\n                    <slot></slot>\n                </ul>\n            </div>\n        ",
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
             * 检查日期是否有效
             *
             * @param this
             * @param e
             */
            valid: function (e) {
                var start = getValue.call(this, 'input[name=startDate]'), end = getValue.call(this, 'input[name=endDate]');
                if (!start || !end) {
                    aj.showOk("输入数据不能为空");
                    e.preventDefault();
                }
                if (new Date(start) > new Date(end)) {
                    aj.showOk("起始日期不能晚于结束日期");
                    e.preventDefault();
                }
            },
            /**
             * 获取关键字进行搜索
             *
             * @param this
             * @param e
             */
            doSearch: function (e) {
                e.preventDefault();
                // @ts-ignore
                aj.apply(this.$parent.$refs.pager.extraParam, { keyword: getValue.call(this, 'input[name=keyword]') });
                // @ts-ignore
                this.$parent.reload();
            }
        }
    });
    /**
     * 获取控件的值
     *
     * @param this
     * @param cssSelector
     */
    function getValue(cssSelector) {
        var el = this.$el.$(cssSelector);
        if (el)
            return el.value;
        return null;
    }
})();
