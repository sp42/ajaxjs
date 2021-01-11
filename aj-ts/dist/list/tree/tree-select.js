"use strict";
Vue.component('aj-tree-like-select', {
    mixins: [aj.treeLike],
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
