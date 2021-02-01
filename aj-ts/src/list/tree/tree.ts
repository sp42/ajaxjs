namespace aj.list.tree {
    export class Tree extends VueComponent implements Ajax {
        name = 'aj-tree';

        template = '<ul class="aj-tree"><aj-tree-item :model="treeData"></aj-tree-item></ul>';

        props = {
            apiUrl: String,     // 接口地址
            topNodeName: String
        };

        /**
         * 根节点显示名称
         */
        topNodeName = "";

        apiUrl = "";

        isAutoLoad: boolean = false;

        treeData = { name: this.topNodeName || 'TOP', children: null };

        getData(): void {
            // @ts-ignore
            xhr.get(this.apiUrl, (j: RepsonseResult) => this.treeData.children = makeTree(<TreeNode[]>j.result));
            // 递归组件怎么事件上报呢？通过事件 bus
            this.BUS && this.BUS.$on('treenodeclick', (data: any) => this.$emit('treenodeclick', data));
        }

        mounted(): void {
            this.getData();
        }
    }

    new Tree().register();
}