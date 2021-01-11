"use strict";
var aj;
(function (aj) {
    var list;
    (function (list) {
        var tree;
        (function (tree) {
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
                    aj.xhr.get(this.apiUrl, function (j) { return _this.treeData.children = makeTree(j.result); });
                    // 递归组件怎么事件上报呢？通过事件 bus
                    this.BUS.$on('treenodeclick', function (data) { return _this.$emit('treenodeclick', data); });
                }
            });
            /**
             * 排序
             * 父id 必须在子 id 之前，不然下面 findParent() 找不到后面的父节点，故先排序
             *
             * @param jsonArray
             */
            function makeTree(jsonArray) {
                var arr = [];
                for (var i = 0, j = jsonArray.length; i < j; i++) {
                    var n = jsonArray[i];
                    if (n.pid === -1)
                        arr.push(n);
                    else {
                        var parentNode = findParentInArray(arr, n.pid);
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
             * 根据传入 id 在一个数组中查找父亲节点
             *
             * @param jsonArray
             * @param id
             */
            function findParentInArray(jsonArray, id) {
                for (var i = 0, j = jsonArray.length; i < j; i++) {
                    var map = jsonArray[i];
                    if (map.id == id)
                        return map;
                    if (map.children) {
                        var result = findParent(map.children, id);
                        if (result != null)
                            return result;
                    }
                }
                return null;
            }
            tree.findParentInArray = findParentInArray;
            // 递归查找父亲节点，根据传入 id
            function findParentInMap(map, id) {
                for (var i in map) {
                    if (i == id)
                        return map[i];
                    var c = map[i].children;
                    if (c) {
                        for (var q = 0, p = c.length; q < p; q++) {
                            var result = findParent(c[q], id);
                            if (result != null)
                                return result;
                        }
                    }
                }
                return null;
            }
            tree.findParentInMap = findParentInMap;
        })(tree = list.tree || (list.tree = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
