"use strict";
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
var aj;
(function (aj) {
    var admin;
    (function (admin) {
        admin.helper = {
            /**
             * 删除
             *
             * @param id
             * @param title
             */
            del: function (id, title) {
                aj.showConfirm('请确定删除记录：\n' + title + ' ？', function () {
                    aj.xhr.dele('../' + id + '/', function (j) {
                        if (j.isOk) {
                            aj.msg.show('删除成功！');
                            setTimeout(function () { return location.reload(); }, 1500);
                        }
                        else {
                            aj.alert('删除失败！');
                        }
                    });
                });
            },
            setStatus: function (id, status) {
                aj.xhr.post('../setStatus/' + id + '/', function (j) {
                    if (j.isOk) {
                    }
                }, { status: status + "" });
            },
            /**
             * 创建之后转向编辑界面
             *
             * @param j
             */
            defaultAfterCreate: function (j) {
                if (j && j.msg)
                    aj.alert(j.msg);
                //@ts-ignore
                window.isCreate && j && j.isOk && setTimeout(function () { return location.assign(j.newlyId + "/"); }, 2000);
            }
        };
        Vue.component('aj-admin-state', {
            template: html(__makeTemplateObject(["\n\t\t<div>\n\t\t\t<div class=\"label\">\u72B6\u6001\uFF1A</div>\n\t\t\t<label>\n\t\t\t\t<input name=\"stat\" value=\"1\" type=\"radio\" :checked=\"checked == 1\" /> \u4E0A\u7EBF\u4E2D\n\t\t\t</label>\n\t\t\t<label>\n\t\t\t\t<input name=\"stat\" value=\"0\" type=\"radio\" :checked=\"checked == 0\" /> \u5DF2\u4E0B\u7EBF\n\t\t\t</label>\n\t\t\t<label>\n\t\t\t\t<input name=\"stat\" value=\"2\" type=\"radio\" :checked=\"checked == 2\" /> \u5DF2\u5220\u9664\n\t\t\t</label>\n\t\t</div>\n\t"], ["\n\t\t<div>\n\t\t\t<div class=\"label\">\u72B6\u6001\uFF1A</div>\n\t\t\t<label>\n\t\t\t\t<input name=\"stat\" value=\"1\" type=\"radio\" :checked=\"checked == 1\" /> \u4E0A\u7EBF\u4E2D\n\t\t\t</label>\n\t\t\t<label>\n\t\t\t\t<input name=\"stat\" value=\"0\" type=\"radio\" :checked=\"checked == 0\" /> \u5DF2\u4E0B\u7EBF\n\t\t\t</label>\n\t\t\t<label>\n\t\t\t\t<input name=\"stat\" value=\"2\" type=\"radio\" :checked=\"checked == 2\" /> \u5DF2\u5220\u9664\n\t\t\t</label>\n\t\t</div>\n\t"])),
            props: { checked: Number } // 哪个选中了？
        });
        Vue.component('aj-admin-xsl', {
            template: html(__makeTemplateObject(["\n\t\t<div style=\"float:left;margin-top: .5%;\">\n\t\t\t<a :href=\"'?downloadXSL=true&' + params\" download>\n\t\t\t\t<i class=\"fa fa-file-excel-o\" aria-hidden=\"true\" style=\"color:#0bac00;\"></i> \u4E0B\u8F7D Excel \u683C\u5F0F\n\t\t\t</a>\n\t\t</div>\n\t"], ["\n\t\t<div style=\"float:left;margin-top: .5%;\">\n\t\t\t<a :href=\"'?downloadXSL=true&' + params\" download>\n\t\t\t\t<i class=\"fa fa-file-excel-o\" aria-hidden=\"true\" style=\"color:#0bac00;\"></i> \u4E0B\u8F7D Excel \u683C\u5F0F\n\t\t\t</a>\n\t\t</div>\n\t"])),
            props: { params: String } // 参数
        });
        Vue.component('aj-admin-control', {
            template: html(__makeTemplateObject(["\n\t\t<td>\n\t\t\t<slot></slot>\n\t\t\t<a v-if=\"preview\" :href=\"ajResources.ctx + preview + id + '/'\" target=\"_blank\">\u6D4F\u89C8</a>\n\t\t\t<a :href=\"'../' + id +'/'\"><img :src=\"ajResources.commonAssetIcon + '/update.gif'\" style=\"vertical-align: sub;\" />\n\t\t\t\t\u7F16\u8F91</a>\n\t\t\t<a href=\"javascript:;\" @click=\"del(id, name)\"><img :src=\"ajResources.commonAssetIcon + '/delete.gif'\"\n\t\t\t\t\tstyle=\"vertical-align: sub;\" /> \u5220\u9664</a>\n\t\t</td>\n\t"], ["\n\t\t<td>\n\t\t\t<slot></slot>\n\t\t\t<a v-if=\"preview\" :href=\"ajResources.ctx + preview + id + '/'\" target=\"_blank\">\u6D4F\u89C8</a>\n\t\t\t<a :href=\"'../' + id +'/'\"><img :src=\"ajResources.commonAssetIcon + '/update.gif'\" style=\"vertical-align: sub;\" />\n\t\t\t\t\u7F16\u8F91</a>\n\t\t\t<a href=\"javascript:;\" @click=\"del(id, name)\"><img :src=\"ajResources.commonAssetIcon + '/delete.gif'\"\n\t\t\t\t\tstyle=\"vertical-align: sub;\" /> \u5220\u9664</a>\n\t\t</td>\n\t"])),
            props: {
                id: String,
                name: String,
                preview: String // 浏览的链接 
            },
            methods: {
                del: function (id, name) {
                    aj.admin.helper.del(id, name);
                }
            }
        });
        /**
         * 后台头部导航
         */
        Vue.component('aj-admin-header', {
            template: html(__makeTemplateObject(["\t\n\t\t<header class=\"aj-admin-header\">\n\t\t\t<div>\n\t\t\t\t<slot name=\"btns\"></slot>\n\t\t\t\t<a href=\"#\" target=\"_blank\">\n\t\t\t\t\t<img width=\"12\"\n\t\t\t\t\t\tsrc=\"data:image/gif;base64,R0lGODlhEAAQAIABAAAAAP///yH5BAEAAAEALAAAAAAQABAAAAImjG+gq+je3gOBWURrlvVEuWlcKE4T2Xkql6zshkLuOIO1mVj6VgAAOw==\" />\n\t\t\t\t\t\u65B0\u7A97\u53E3\u6253\u5F00\n\t\t\t\t</a>\n\t\t\t</div>\n\t\t\t<fieldset>\n\t\t\t\t<legend>\n\t\t\t\t\t<slot name=\"title\">\n\t\t\t\t\t\t{{isCreate ? \"\u65B0\u5EFA\":\"\u7F16\u8F91\"}}{{uiName}} \uFF1A<span v-if=\"infoId\">No.{{infoId}}</span>\n\t\t\t\t\t</slot>\n\t\t\t\t</legend>\n\t\t\t</fieldset>\n\t\t</header>\n    "], ["\t\n\t\t<header class=\"aj-admin-header\">\n\t\t\t<div>\n\t\t\t\t<slot name=\"btns\"></slot>\n\t\t\t\t<a href=\"#\" target=\"_blank\">\n\t\t\t\t\t<img width=\"12\"\n\t\t\t\t\t\tsrc=\"data:image/gif;base64,R0lGODlhEAAQAIABAAAAAP///yH5BAEAAAEALAAAAAAQABAAAAImjG+gq+je3gOBWURrlvVEuWlcKE4T2Xkql6zshkLuOIO1mVj6VgAAOw==\" />\n\t\t\t\t\t\u65B0\u7A97\u53E3\u6253\u5F00\n\t\t\t\t</a>\n\t\t\t</div>\n\t\t\t<fieldset>\n\t\t\t\t<legend>\n\t\t\t\t\t<slot name=\"title\">\n\t\t\t\t\t\t{{isCreate ? \"\u65B0\u5EFA\":\"\u7F16\u8F91\"}}{{uiName}} \uFF1A<span v-if=\"infoId\">No.{{infoId}}</span>\n\t\t\t\t\t</slot>\n\t\t\t\t</legend>\n\t\t\t</fieldset>\n\t\t</header>\n    "])),
            props: {
                isCreate: Boolean,
                uiName: String,
                infoId: Number // 实体 id
            }
        });
        /**
         * 搜索、分类下拉
         */
        Vue.component('aj-admin-filter-panel', {
            template: html(__makeTemplateObject(["\n\t\t<div class=\"aj-admin-filter-panel\">\n\t\t\t<form action=\"?\" method=\"GET\">\n\t\t\t\t<input type=\"hidden\" name=\"searchField\" :value=\"searchFieldValue\" />\n\t\t\t\t<input type=\"text\" name=\"keyword\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" style=\"float: inherit;\" class=\"aj-input\" />\n\t\t\t\t<button style=\"margin-top: 0;\" class=\"aj-btn\">\u641C\u7D22</button> &nbsp;\n\t\t\t</form>\n\t\t\t<slot></slot>\n\t\t\t<span v-if=\"!noCatalog\">{{label || '\u5206\u7C7B'}}\uFF1A\n\t\t\t\t<aj-tree-like-select :api-url=\"getTreeUrl\" :init-field-value=\"selectedCatalogId\" :is-auto-jump=\"true\"></aj-tree-like-select>\n\t\t\t</span>\n\t\t</div>\n    "], ["\n\t\t<div class=\"aj-admin-filter-panel\">\n\t\t\t<form action=\"?\" method=\"GET\">\n\t\t\t\t<input type=\"hidden\" name=\"searchField\" :value=\"searchFieldValue\" />\n\t\t\t\t<input type=\"text\" name=\"keyword\" placeholder=\"\u8BF7\u8F93\u5165\u641C\u7D22\u4E4B\u5173\u952E\u5B57\" style=\"float: inherit;\" class=\"aj-input\" />\n\t\t\t\t<button style=\"margin-top: 0;\" class=\"aj-btn\">\u641C\u7D22</button> &nbsp;\n\t\t\t</form>\n\t\t\t<slot></slot>\n\t\t\t<span v-if=\"!noCatalog\">{{label || '\u5206\u7C7B'}}\uFF1A\n\t\t\t\t<aj-tree-like-select :api-url=\"getTreeUrl\" :init-field-value=\"selectedCatalogId\" :is-auto-jump=\"true\"></aj-tree-like-select>\n\t\t\t</span>\n\t\t</div>\n    "])),
            props: {
                label: { type: String, required: false },
                getTreeUrl: { type: String, required: false },
                selectedCatalogId: { type: String, required: false },
                noCatalog: { type: Boolean, default: false },
                searchFieldValue: { required: false, default: 'name' } // 搜索哪个字段？默认为 name
            }
        });
        /**
         * 后台增加、编辑、复位、删除按钮
         */
        Vue.component('aj-admin-info-btns', {
            template: html(__makeTemplateObject(["\n\t\t<div class=\"aj-admin-info-btns\">\n\t\t\t<button><img :src=\"ajResources.commonAssetIcon + '/save.gif'\" /> {{isCreate ? \"\u65B0 \u5EFA\":\"\u4FDD \u5B58\"}}</button>\n\t\t\t<button onclick=\"this.up('form').reset();return false;\">\u590D \u4F4D</button>\n\t\t\t<button v-if=\"!isCreate\" v-on:click.prevent=\"del\">\n\t\t\t\t<img :src=\"ajResources.commonAssetIcon + '/delete.gif'\" /> \u5220 \u9664\n\t\t\t</button>\n\t\t\t<button onclick=\"history.back();return false;\">\u8FD4 \u56DE</button>\n\t\t\t<slot></slot>\n\t\t</div>\n\t"], ["\n\t\t<div class=\"aj-admin-info-btns\">\n\t\t\t<button><img :src=\"ajResources.commonAssetIcon + '/save.gif'\" /> {{isCreate ? \"\u65B0 \u5EFA\":\"\u4FDD \u5B58\"}}</button>\n\t\t\t<button onclick=\"this.up('form').reset();return false;\">\u590D \u4F4D</button>\n\t\t\t<button v-if=\"!isCreate\" v-on:click.prevent=\"del\">\n\t\t\t\t<img :src=\"ajResources.commonAssetIcon + '/delete.gif'\" /> \u5220 \u9664\n\t\t\t</button>\n\t\t\t<button onclick=\"history.back();return false;\">\u8FD4 \u56DE</button>\n\t\t\t<slot></slot>\n\t\t</div>\n\t"])),
            props: {
                isCreate: { type: Boolean, default: false },
                listUrl: { type: String, default: '../list/' } // 成功删除后跳转的地址
            },
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
                                //@ts-ignore
                                location.assign(_this.listUrl);
                        });
                }
            }
        });
    })(admin = aj.admin || (aj.admin = {}));
})(aj || (aj = {}));
