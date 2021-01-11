Vue.component('aj-tree', {
    template: '<ul class="aj-tree"><aj-tree-item :model="treeData"></aj-tree-item></ul>',
    props: {
        apiUrl: String,        // 接口地址
        topNodeName: String // 根节点显示名称
    },
    data() {
        return {
            treeData: { name: this.topNodeName || 'TOP', children: null }
        };
    },
    mounted(this: Tree) {
        // aj.xhr.get(this.ajResources.ctx + this.url, (j: RepsonseResult) => this.treeData.children = makeTree(j.result));
        aj.xhr.get(this.apiUrl, (j: RepsonseResult) => this.treeData.children = aj.list.tree.makeTree(j.result));
        // 递归组件怎么事件上报呢？通过事件 bus
        this.BUS.$on('treenodeclick', (data: any) => this.$emit('treenodeclick', data));
    }
});