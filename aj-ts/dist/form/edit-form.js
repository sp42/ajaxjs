"use strict";
/**
    新建、编辑都同一表单
 */
Vue.component('aj-edit-form', {
    template: "\n        <form class=\"aj-table-form\" :action=\"getInfoApi + (isCreate ? '' : info.id + '/')\" :method=\"isCreate ? 'POST' : 'PUT'\">\n            <h3>{{isCreate ? \"\u65B0\u5EFA\" : \"\u7F16\u8F91\" }}{{uiName}}</h3>\n            <!-- \u4F20\u9001 id \u53C2\u6570 -->\n            <input v-if=\"!isCreate\" type=\"hidden\" name=\"id\" :value=\"info.id\" />\n            <slot v-bind:info=\"info\"></slot>\n            <div class=\"aj-btnsHolder\">\n                <button><img :src=\"ajResources.commonAsset + '/icon/save.gif'\" /> {{isCreate ? \"\u65B0\u5EFA\":\"\u4FDD\u5B58\"}}</button>\n                <button onclick=\"this.up('form').reset();return false;\">\u590D \u4F4D</button>\n                <button v-if=\"!isCreate\" v-on:click.prevent=\"del()\">\n                    <img :src=\"ajResources.commonAsset + '/icon/delete.gif'\" /> \u5220 \u9664\n                </button>\n                <button @click.prevent=\"close\">\u5173\u95ED</button>\n            </div>\n        </form>\n    ",
    props: {
        isCreate: Boolean,
        uiName: String,
        getInfoApi: { type: String, required: true } // 获取实体详情的接口地址 
    },
    data: function () {
        return {
            id: 0,
            info: {},
        };
    },
    mounted: function () {
        var _this = this;
        aj.xhr.form(this.$el, function (j) {
            if (j) {
                if (j.isOk) {
                    var msg = (_this.isCreate ? "新建" : "保存") + _this.uiName + "成功";
                    aj.alert.show(msg);
                    _this.$parent.close();
                }
                else
                    aj.alert.show(j.msg);
            }
        }, {
            beforeSubmit: function (form, json) {
                //json.content = App.$refs.htmleditor.getValue({cleanWord : eval('${aj_allConfig.article.cleanWordTag}'), encode : true});
            }
        });
    },
    methods: {
        load: function (id, cb) {
            var _this = this;
            this.id = id;
            aj.xhr.get(this.getInfoApi + id + "/", function (j) {
                _this.info = j.result;
                cb && cb(j);
            });
        },
        close: function () {
            if (this.$parent.$options._componentTag === 'aj-layer') {
                this.$parent.close();
            }
            else
                history.back();
        },
        del: function () {
            var id = this.$el.$('input[name=id]').value, title = this.$el.$('input[name=name]').value;
            aj.showConfirm('请确定删除记录：\n' + title + ' ？', function () {
                return aj.xhr.dele('../' + id + '/', function (j) {
                    if (j.isOk) {
                        aj.msg.show('删除成功！');
                        //setTimeout(() => location.reload(), 1500);
                    }
                    else
                        aj.alert('删除失败！');
                });
            });
        }
    }
});
