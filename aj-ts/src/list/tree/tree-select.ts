Vue.component('aj-tree-like-select', {
    mixins: [aj.treeLike],
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