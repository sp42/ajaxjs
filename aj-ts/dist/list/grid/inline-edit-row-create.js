"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
Vue.component('aj-grid-inline-edit-row-create', {
    template: html(__makeTemplateObject(["\n        <tr class=\"aj-grid-inline-edit-row isEditMode\">\n            <td><input type=\"checkbox\" /></td>\n            <td></td>\n            <td v-for=\"key in columns\" style=\"padding:0\" class=\"cell\" @dblclick=\"dbEdit\">\n                <aj-select v-if=\"key != null && key.type == 'select'\" :name=\"key.name\" :options=\"key.data\"\n                    style=\"width: 200px;\"></aj-select>\n                <input v-if=\"key != null && !key.type\" type=\"text\" size=\"0\" :name=\"key\" />\n            </td>\n            <td class=\"control\">\n                <span @click=\"addNew\"><img :src=\"ajResources.commonAsset + '/icon/update.gif'\" />\u65B0\u589E</span>\n                <span @click=\"$parent.showAddNew = false\"><img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" /> \u64A4\u9500</span>\n            </td>\n        </tr>\n    "], ["\n        <tr class=\"aj-grid-inline-edit-row isEditMode\">\n            <td><input type=\"checkbox\" /></td>\n            <td></td>\n            <td v-for=\"key in columns\" style=\"padding:0\" class=\"cell\" @dblclick=\"dbEdit\">\n                <aj-select v-if=\"key != null && key.type == 'select'\" :name=\"key.name\" :options=\"key.data\"\n                    style=\"width: 200px;\"></aj-select>\n                <input v-if=\"key != null && !key.type\" type=\"text\" size=\"0\" :name=\"key\" />\n            </td>\n            <td class=\"control\">\n                <span @click=\"addNew\"><img :src=\"ajResources.commonAsset + '/icon/update.gif'\" />\u65B0\u589E</span>\n                <span @click=\"$parent.showAddNew = false\"><img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" /> \u64A4\u9500</span>\n            </td>\n        </tr>\n    "])),
    props: {
        columns: { type: Array, required: true },
        createApi: { type: String, required: false, default: '.' }
    },
    methods: {
        /**
         * 编辑按钮事件
         *
         * @param this
         */
        addNew: function () {
            var _this = this;
            var map = {};
            this.$el.$('*[name]', function (i) { return map[i.name] = i.value; });
            this.BUS.$emit('before-add-new', map);
            aj.xhr.post(this.createApi, function (j) {
                if (j && j.isOk) {
                    aj.msg.show('新建实体成功');
                    _this.$el.$('input[name]', function (i) {
                        i.value = '';
                    });
                    // @ts-ignore
                    _this.$parent.reload();
                    // @ts-ignore
                    _this.$parent.showAddNew = false;
                }
                else if (j && j.msg) {
                    aj.msg.show(j.msg);
                }
            }, map);
        },
        /**
         *
         * @param this
         * @param $event
         */
        dbEdit: function ($event) {
            this.isEditMode = !this.isEditMode;
            var el = $event.target;
            if (el.tagName !== 'INPUT')
                el = el.$('input');
            setTimeout(function () { return el && el.focus(); }, 200);
        }
    }
});
