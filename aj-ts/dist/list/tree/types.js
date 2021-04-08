"use strict";
var aj;
(function (aj) {
    var tree;
    (function (tree) {
        /**
         * 寻找节点
         *
         * @param obj
         * @param queue
         * @returns
         */
        function findNodesHolder(obj, queue) {
            if (!queue.shift)
                return null;
            var first = queue.shift();
            for (var i in obj) {
                if (i === first) {
                    var target_1 = obj[i];
                    if (queue.length == 0) // 找到了
                        return target_1;
                    else
                        return findNodesHolder(obj[i], queue);
                }
            }
            return null;
        }
        tree.findNodesHolder = findNodesHolder;
        //------------------------------------------------------------------------------
        /**
         * 根据传入 id 在一个数组中查找父亲节点
         *
         * @param map
         * @param id
         */
        function findParentInArray(arr, id) {
            for (var i = 0; i < arr.length; i++) {
                var n = arr[i];
                if (id == n.id)
                    return n;
                var c = n.children;
                if (c) {
                    var result = findParentInArray(c, id);
                    if (result != null)
                        return result;
                }
            }
            return null;
        }
        tree.findParentInArray = findParentInArray;
        /**
         * 生成树，将扁平化的数组结构 还原为树状的 Array结构
         * 父id 必须在子 id 之前，不然下面 findParent() 找不到后面的父节点，故先排序
         *
         * @param jsonArray
         */
        function toTreeArray(jsonArray) {
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
        tree.toTreeArray = toTreeArray;
        //------------------------------------------------------------------------------
        /**
         * 根据传入 id 查找父亲节点
         *
         * @param map
         * @param id
         */
        function findParentInMap(map, id) {
            for (var i in map) {
                if (i == id)
                    return map[i];
                var c = map[i].children;
                if (c) {
                    for (var j in c) {
                        var result = findParentInMap(c[j], id);
                        if (result != null)
                            return result;
                    }
                }
            }
            return null;
        }
        tree.findParentInMap = findParentInMap;
        /**
         * 生成树，将扁平化的数组结构 还原为树状的 Map 结构
         * 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故前提条件是，这个数组必须先排序
         *
         * @param jsonArray
         */
        function toTreeMap(jsonArray) {
            if (!jsonArray)
                return null;
            var m = {};
            for (var i = 0, j = jsonArray.length; i < j; i++) {
                var n = jsonArray[i];
                if (n.pid === -1)
                    m[n.id] = n;
                else {
                    var parentNode = findParentInMap(m, n.pid + "");
                    if (parentNode) {
                        if (!parentNode.children)
                            parentNode.children = {};
                        parentNode.children[n.id] = n;
                    }
                    else
                        console.log('parent not found!');
                }
            }
            return m;
        }
        tree.toTreeMap = toTreeMap;
        var stack = [];
        /**
         * 遍历各个元素，输出
         *
         * @param map
         * @param cb
         */
        function output(map, cb) {
            stack.push(map);
            for (var i in map) {
                map[i].level = stack.length; // 层数，也表示缩进多少个字符
                cb && cb(map[i], i);
                var c = map[i].children;
                c && output(c, cb);
            }
            stack.pop();
        }
        tree.output = output;
    })(tree = aj.tree || (aj.tree = {}));
})(aj || (aj = {}));
