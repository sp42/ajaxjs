
// 下拉分类选择器，异步请求远端获取分类数据
Vue.component('aj-tree-catelog-select', {
    template: '<select :name="fieldName" @change="onSelected" class="aj-tree-catelog-select aj-select" style="width: 200px;"></select>',
    props: {
        catalogId: { 			// 请求远端的分类 id，必填
            type: Number, required: true
        },
        selectedCatalogId: {	// 已选中的分类 id
            type: Number, required: false
        },
        fieldName: { // 表单 name，字段名
            type: String, default: 'catalogId'
        },
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
        onSelected($event): void {
            if (this.isAutoJump) {
                var el = $event.target, catalogId = el.selectedOptions[0].value;
                location.assign('?' + this.fieldName + '=' + catalogId);
            } else
                this.BUS.$emit('aj-tree-catelog-select-change', $event, this);
        }
    }
});
