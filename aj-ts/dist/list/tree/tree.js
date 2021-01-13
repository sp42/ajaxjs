"use strict";
Vue.component('aj-tree', {
    template: '<ul class="aj-tree"><aj-tree-item :model="treeData"></aj-tree-item></ul>',
    props: {
        apiUrl: String,
        topNodeName: String // 根节点显示名称
    },
    data: function () {
        return {
            treeData: { name: this.topNodeName || 'TOP', children: null }
        };
    },
    mounted: function () {
        var _this = this;
        // aj.xhr.get(this.ajResources.ctx + this.url, (j: RepsonseResult) => this.treeData.children = makeTree(j.result));
        aj.xhr.get(this.apiUrl, function (j) { return _this.treeData.children = aj.list.tree.makeTree(j.result); });
        // 递归组件怎么事件上报呢？通过事件 bus
        this.BUS && this.BUS.$on('treenodeclick', function (data) { return _this.$emit('treenodeclick', data); });
    }
});
