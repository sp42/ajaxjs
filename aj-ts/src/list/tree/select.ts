/**
 * 下拉分类选择器，异步请求远端获取分类数据
 */
interface TreeLikeSelect extends Vue, FormFieldElementComponent, Ajax {
    isAutoJump: boolean;
}

interface TreeOption {
    /**
     * 是否创建全部
     */
    makeAllOption: boolean;
}

Vue.component('aj-tree-like-select', {
    template: '<select :name="fieldName" class="aj-select" @change="onSelected" style="min-width:180px;"></select>',
    props: {
        fieldName: { type: String, required: false, default: 'catalogId' },// 表单 name，字段名
        apiUrl: { type: String, default: '/admin/tree-like/' },
        isAutoLoad: { type: Boolean, default: true },
        isAutoJump: Boolean, // 是否自动跳转 catalogId
        initFieldValue: String
    },
    data() {
        return {
            fieldValue: this.initFieldValue
        }
    },
    mounted(this: TreeLikeSelect): void {
        this.isAutoLoad && this.getData();
    },
    methods: {
        onSelected(this: TreeLikeSelect, ev: Event): void {
            let el: HTMLSelectElement = <HTMLSelectElement>ev.target;
            this.fieldValue = el.selectedOptions[0].value;

            if (this.isAutoJump)
                location.assign('?' + this.fieldName + '=' + this.fieldValue);
            else
                this.BUS && this.BUS.$emit('aj-tree-catelog-select-change', ev, this);
        },
        getData(this: TreeLikeSelect): void {
            let fn = (j: RepsonseResult) => {
                let arr = [{ id: 0, name: "请选择分类" }];
                aj.list.tree.rendererOption(<TreeNode[]>arr.concat(j.result), <HTMLSelectElement>this.$el, this.fieldValue, { makeAllOption: false });

                if (this.fieldValue) // 有指定的选中值
                    aj.form.utils.selectOption.call(this, this.fieldValue);
            }

            // aj.xhr.get(this.ajResources.ctx + this.apiUrl + "/admin/tree-like/getListAndSubByParentId/", fn);
            aj.xhr.get(this.apiUrl, fn);
        }
    }
});