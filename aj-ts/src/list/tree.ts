interface Tree extends Ajax, Vue {
    treeData: TreeNode
}

namespace aj.list.tree {
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
            aj.xhr.get(this.apiUrl, (j: RepsonseResult) => this.treeData.children = makeTree(j.result));
            // 递归组件怎么事件上报呢？通过事件 bus
            this.BUS.$on('treenodeclick', (data: any) => this.$emit('treenodeclick', data));
        }
    });

    /**
     * 排序
     * 父id 必须在子 id 之前，不然下面 findParent() 找不到后面的父节点，故先排序
     * 
     * @param jsonArray 
     */
    function makeTree(jsonArray: TreeNode[]): TreeNode[] {
        let arr: TreeNode[] = [];

        for (var i = 0, j = jsonArray.length; i < j; i++) {
            let n: TreeNode = jsonArray[i];

            if (n.pid === -1)
                arr.push(n);
            else {
                let parentNode = findParent(arr, n.pid);

                if (parentNode) {
                    if (!parentNode.children)
                        parentNode.children = [];

                    parentNode.children.push(n);
                } else
                    console.log('parent not found!');
            }
        }

        return arr;
    }

    /**
     * 递归查找父亲节点，根据传入 id
     * 
     * @param jsonArray 
     * @param id 
     */
    function findParent(jsonArray: TreeNode[], id: number): TreeNode | null {
        for (let i = 0, j = jsonArray.length; i < j; i++) {
            let map: TreeNode = jsonArray[i];

            if (map.id == id)
                return map;

            if (map.children) {
                let result = findParent(map.children, id);

                if (result != null)
                    return result;
            }
        }

        return null;
    }
}