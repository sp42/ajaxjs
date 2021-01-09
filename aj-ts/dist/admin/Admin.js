"use strict";
var aj;
(function (aj) {
    aj.admin = {
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
})(aj || (aj = {}));
Vue.component('aj-admin-state', {
    template: "\n\t\t<div>\n\t\t\t<div class=\"label\">\u72B6\u6001\uFF1A</div>\n\t\t\t<label>\n\t\t\t\t<input name=\"stat\" value=\"1\" type=\"radio\" :checked=\"checked == 1\" /> \u4E0A\u7EBF\u4E2D \n\t\t\t</label> \n\t\t\t<label>\n\t\t\t\t<input name=\"stat\" value=\"0\" type=\"radio\" :checked=\"checked == 0\" /> \u5DF2\u4E0B\u7EBF \n\t\t\t</label> \n\t\t\t<label>\n\t\t\t\t<input name=\"stat\" value=\"2\" type=\"radio\" :checked=\"checked == 2\" /> \u5DF2\u5220\u9664\n\t\t\t</label>\n\t\t</div>\n\t",
    props: { checked: Number } // 哪个选中了？
});
Vue.component('aj-admin-xsl', {
    template: "\n\t\t<div style=\"float:left;margin-top: .5%;\">\n\t\t\t<a :href=\"'?downloadXSL=true&' + params\" download>\n\t\t\t\t<i class=\"fa fa-file-excel-o\" aria-hidden=\"true\" style=\"color:#0bac00;\"></i> \u4E0B\u8F7D Excel \u683C\u5F0F\n\t\t\t</a>\n\t\t</div>\n\t",
    props: { params: String } // 参数
});
Vue.component('aj-admin-control', {
    template: "\n\t\t<td> <slot></slot>\n\t\t\t<a v-if=\"preview\" :href=\"ajResources.ctx + preview + id + '/'\" target=\"_blank\">\u6D4F\u89C8</a>\n\t\t\t<a :href=\"'../' + id +'/'\"><img :src=\"ajResources.commonAsset + '/icon/update.gif'\" style=\"vertical-align: sub;\" /> \u7F16\u8F91</a>\n\t\t\t<a href=\"javascript:;\" @click=\"del(id, name)\"><img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" style=\"vertical-align: sub;\" /> \u5220\u9664</a>\n\t\t</td>\n\t",
    props: {
        id: String,
        name: String,
        preview: String // 浏览的链接 
    },
    methods: {
        del: function (id, name) {
            aj.admin.del(id, name);
        }
    }
});
