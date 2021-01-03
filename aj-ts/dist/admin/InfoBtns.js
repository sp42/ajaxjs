"use strict";
/**
 * 后台增加、编辑、复位、删除按钮
 */
Vue.component('ajaxjs-admin-info-btns', {
    props: {
        isCreate: { type: Boolean, default: false },
        listUrl: { type: String, default: '../list/' } // 成功删除后跳转的地址
    },
    template: "\n\t\t<div class=\"ajaxjs-admin-info-btns\">\n\t\t\t<button><img :src=\"ajResources.commonAsset + '/icon/save.gif'\" /> {{isCreate ? \"\u65B0\u5EFA\":\"\u4FDD\u5B58\"}}</button>\n\t\t\t<button onclick=\"this.up('form').reset();return false;\">\u590D \u4F4D</button>\n\t\t\t<button v-if=\"!isCreate\" v-on:click.prevent=\"del\">\n\t\t\t\t<img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" /> \u5220 \u9664\n\t\t\t</button>\n\t\t\t<button onclick=\"history.back();return false;\">\u8FD4\u56DE</button><slot></slot>\n        </div>\n    ",
    methods: {
        /**
         * 执行删除
         */
        del: function () {
            var _this = this;
            // TODO
            if (confirm('确定删除？'))
                aj.xhr.dele('.', function (j) {
                    if (j && j.isOk)
                        location.assign(_this.listUrl);
                });
        }
    }
});
