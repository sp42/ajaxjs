"use strict";
Vue.component('aj-tree-like-select', {
    template: '<select :name="name" class="aj-select" @change="onSelected"></select>',
    props: {
        catalogId: { type: Number, required: true },
        selectedId: { type: Number, required: false },
        name: { type: String, required: false, default: 'catalogId' },
        api: { type: String, default: '/admin/tree-like/' }
    },
    mounted: function () {
        var _this = this;
        var url = this.ajResources.ctx + this.api + this.catalogId + "/";
        var fn = function (j) { return _this.rendererOption(j.result, _this.$el, _this.selectedCatalogId, { makeAllOption: false }); };
        aj.xhr.get(url, fn);
    },
    methods: {
        onSelected: function (ev) {
            var el = ev.target, catalogId = el.selectedOptions[0].value;
            this.$emit('selected', Number(catalogId));
        },
        select: aj.selectOption
    }
});
// 下拉分类选择器，异步请求远端获取分类数据
Vue.component('aj-tree-catelog-select', {
    template: '<select :name="fieldName" class="aj-tree-catelog-select aj-select" @change="onSelected"  style="width: 200px;"></select>',
    props: {
        catalogId: { type: Number, required: true },
        selectedCatalogId: { type: Number, required: false },
        fieldName: { type: String, default: 'catalogId' },
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
        onSelected: function (ev) {
            if (this.isAutoJump) {
                var el = $event.target, catalogId = el.selectedOptions[0].value;
                location.assign('?' + this.fieldName + '=' + catalogId);
            }
            else
                this.BUS.$emit('aj-tree-catelog-select-change', $event, this);
        }
    }
});
