namespace aj.tree {
    interface TreeOption {
        /**
         * 是否创建全部
         */
        makeAllOption?: boolean;

        /**
         * 默认为"所有分类", 或者你在这里自定义一个名字
         */
        allOptionName?: string;
    }

    /**
     * 下拉分类选择器，异步请求远端获取分类数据
     */
    export class TreeLikeSelect extends VueComponent implements FormFieldElementComponent, Ajax {
        name = 'aj-tree-like-select';

        template = '<select :name="fieldName" class="aj-select" @change="onSelected" style="min-width:180px;"></select>';

        props = {
            fieldName: { type: String, required: false, default: 'catalogId' },// 表单 name，字段名
            apiUrl: { type: String, default() { return ctx + '/admin/tree-like/'; } },
            isAutoLoad: { type: Boolean, default: true },
            isAutoJump: Boolean,
            initFieldValue: String
        };

        apiUrl: string = "";

        /**
         * 是否自动跳转 catalogId
         */
        isAutoJump: boolean = false;

        isAutoLoad: boolean = true;

        fieldName: string = "";

        fieldValue: string = "";

        initFieldValue: string = "";

        data() {
            return {
                fieldValue: this.initFieldValue
            }
        }

        mounted(): void {
            this.isAutoLoad && this.getData();
        }

        onSelected(ev: Event): void {
            let el: HTMLSelectElement = <HTMLSelectElement>ev.target;
            this.fieldValue = el.selectedOptions[0].value;

            if (this.isAutoJump)
                location.assign('?' + this.fieldName + '=' + this.fieldValue);
            else
                this.BUS && this.BUS.$emit('aj-tree-catelog-select-change', ev, this);
        }

        getData(): void {
            let fn = (j: RepsonseResult) => {
                let arr = [{ id: 0, name: "请选择分类" }];
                rendererOption(<TreeNode[]>arr.concat(j.result), <HTMLSelectElement>this.$el, this.fieldValue, { makeAllOption: false });

                if (this.fieldValue) // 有指定的选中值
                    //@ts-ignore
                    form.utils.selectOption.call(this, this.fieldValue);
            }

            // aj.xhr.get(this.ajResources.ctx + this.apiUrl + "/admin/tree-like/getListAndSubByParentId/", fn);
            xhr.get(this.apiUrl, fn);
        }
    }

    new TreeLikeSelect().register();

    /**
     * 渲染 Option 标签的 DOM
     * 
     * @param jsonArray 
     * @param select 
     * @param selectedId 
     * @param cfg 
     */
    export function rendererOption(jsonArray: TreeNode[], select: HTMLSelectElement, selectedId?: string, cfg?: TreeOption): void {
        if (cfg && cfg.makeAllOption) {
            let option: HTMLOptionElement = document.createElement('option');
            option.innerHTML = cfg.allOptionName || "全部分类";
            select.appendChild(option);
        }

        let treeNode: TreeNode | null = toTreeMap(jsonArray);
        if (treeNode) {
            // 生成 option
            let temp: DocumentFragment = document.createDocumentFragment();
            console.log(treeNode)

            output(<TreeNode>treeNode, (node: TreeNode, nodeId: string) => {
                let option: HTMLOptionElement = document.createElement('option'); // 节点
                option.value = nodeId;

                if (selectedId && selectedId == nodeId) // 选中的
                    option.selected = true;

                option.dataset['pid'] = node.pid + "";
                //option.style= "padding-left:" + (node.level - 1) +"rem;";
                option.innerHTML = new Array(node.level * 5).join('&nbsp;') + (node.level == 1 ? '' : '└─') + node.name;
                temp.appendChild(option);
            });

            select.appendChild(temp);
        }

    }
}