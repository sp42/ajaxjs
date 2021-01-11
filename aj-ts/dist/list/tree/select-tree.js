"use strict";
// 下拉分类选择器，异步请求远端获取分类数据
Vue.component('aj-tree-catelog-select', {
    template: '<select :name="fieldName" @change="onSelected" class="aj-tree-catelog-select aj-select" style="width: 200px;"></select>',
    props: {
        catalogId: {
            type: Number, required: true
        },
        selectedCatalogId: {
            type: Number, required: false
        },
        fieldName: {
            type: String, default: 'catalogId'
        },
        isAutoJump: Boolean // 是否自动跳转 catalogId
    },
    mounted: function () {
        var _this = this;
        var fn = function (j) {
            var arr = [{ id: 0, name: "请选择分类" }];
            _this.rendererOption(arr.concat(j.result), _this.$el, _this.selectedCatalogId, { makeAllOption: false });
        };
        aj.xhr.get(this.ajResources.ctx + "/admin/tree-like/getListAndSubByParentId/" + this.catalogId + "/", fn);
    },
    methods: {
        onSelected: function ($event) {
            if (this.isAutoJump) {
                var el = $event.target, catalogId = el.selectedOptions[0].value;
                location.assign('?' + this.fieldName + '=' + catalogId);
            }
            else
                this.BUS.$emit('aj-tree-catelog-select-change', $event, this);
        }
    }
});
