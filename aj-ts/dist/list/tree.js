"use strict";
;
(function () {
    Vue.component('aj-tree', {
        template: '<ul class="aj-tree"><aj-tree-item :model="treeData"></aj-tree-item></ul>',
        props: {
            url: String,
            topNodeName: String // 根节点显示名称
        },
        data: function () {
            return {
                treeData: { name: this.topNodeName || 'TOP', children: null }
            };
        },
        mounted: function () {
            var _this = this;
            aj.xhr.get(this.ajResources.ctx + this.url, function (j) { return _this.treeData.children = makeTree(j.result); });
            // 递归组件怎么事件上报呢？通过事件 bus
            // this.BUS.$on('treenodeclick', data => this.$emit('treenodeclick', data));
        }
    });
    /**
     *
     * @param jsonArray
     */
    function makeTree(jsonArray) {
        var arr = [];
        // 父id 必须在子 id 之前，不然下面 findParent() 找不到后面的父节点，故先排序
        for (var i = 0, j = jsonArray.length; i < j; i++) {
            var n = jsonArray[i];
            if (n.pid === -1)
                arr.push(n);
            else {
                var parentNode = findParent(arr, n.pid);
                if (parentNode) {
                    if (!parentNode.children)
                        parentNode.children = [];
                    parentNode.children.push(n);
                }
                else
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
    function findParent(jsonArray, id) {
        for (var i = 0, j = jsonArray.length; i < j; i++) {
            var map = jsonArray[i];
            if (map.id == id)
                return map;
            if (map.children) {
                var result = arguments.callee(map.children, id);
                if (result != null)
                    return result;
            }
        }
        return null;
    }
})();
