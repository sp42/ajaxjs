Vue.component('aj-tree-like-select', {
    template: '<select :name="name" class="aj-select" @change="onSelected"></select>',
    props: {
        catalogId: {  type: Number, required: true },	// 请求远端的分类 id，必填
        selectedId: {	type: Number, required: false  },// 已选中的分类 id
        name: { type: String, required: false, default: 'catalogId' },// 表单 name，字段名
        api: {type: String, default: '/admin/tree-like/'}
    },
    mounted(): void {
        var url = this.ajResources.ctx + this.api + this.catalogId + "/";
        var fn = j => this.rendererOption(j.result, this.$el, this.selectedCatalogId, { makeAllOption: false });
        aj.xhr.get(url, fn);
    },
    methods: {
        onSelected(this: Vue, ev: Event): void {
            let el: HTMLSelectElement = <HTMLSelectElement>ev.target, catalogId = el.selectedOptions[0].value;
            this.$emit('selected', Number(catalogId));
        },
        select: aj.selectOption
    }
});

// 下拉分类选择器，异步请求远端获取分类数据
Vue.component('aj-tree-catelog-select', {
    template: '<select :name="fieldName" class="aj-tree-catelog-select aj-select" @change="onSelected"  style="width: 200px;"></select>',
    props: {
        catalogId: { type: Number, required: true },// 请求远端的分类 id，必填
        selectedCatalogId: {type: Number, required: false },// 已选中的分类 id
        fieldName: {type: String, default: 'catalogId'}, // 表单 name，字段名
        isAutoJump: Boolean // 是否自动跳转 catalogId
    },
    mounted(): void {
        var fn = j => {
            var arr = [{ id: 0, name: "请选择分类" }];
            this.rendererOption(arr.concat(j.result), this.$el, this.selectedCatalogId, { makeAllOption: false });
        }
        aj.xhr.get(this.ajResources.ctx + "/admin/tree-like/getListAndSubByParentId/" + this.catalogId + "/", fn);
    },

    methods: {
        onSelected(this: Vue, ev: Event): void {
            if (this.isAutoJump) {
                var el = $event.target, catalogId = el.selectedOptions[0].value;
                location.assign('?' + this.fieldName + '=' + catalogId);
            } else
                this.BUS.$emit('aj-tree-catelog-select-change', $event, this);
        }
    }
});
