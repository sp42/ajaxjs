"use strict";
var aj;
(function (aj) {
    var list;
    (function (list) {
        var tree;
        (function (tree) {
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
            tree.makeTree = makeTree;
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
            var stack = [];
            // 遍历各个元素，输出
            function output(map, cb) {
                stack.push(map);
                for (var i in map) {
                    map[i].level = stack.length; // 层数，也表示缩进多少个字符
                    cb(map[i], i);
                    var c = map[i].children;
                    if (c) {
                        for (var q = 0, p = c.length; q < p; q++)
                            output(c[q], cb);
                    }
                }
                stack.pop();
            }
            tree.output = output;
            // 生成树，将扁平化的结构 还原为树状的结构
            // 父id 必须在子id之前，不然下面 findParent() 找不到后面的父节点，故这个数组必须先排序
            function toTree(jsonArray) {
                if (!jsonArray)
                    return;
                var m = {};
                for (var i = 0, j = jsonArray.length; i < j; i++) {
                    var n = jsonArray[i];
                    var parentNode = findParentInMap(m, n.pid);
                    if (parentNode == null) { // 没有父节点，那就表示这是根节点，保存之
                        m[n.id] = n; // id 是key，value 新建一对象
                    }
                    else { // 有父亲节点，作为孩子节点保存
                        var obj = {};
                        obj[n.id] = n;
                        if (!parentNode.children)
                            parentNode.children = [];
                        parentNode.children.push(obj);
                    }
                }
                return m;
            }
            tree.toTree = toTree;
            /**
             * 渲染 Option 标签的 DOM
             *
             * @param jsonArray
             * @param select
             * @param selectedId
             * @param cfg
             */
            function rendererOption(jsonArray, select, selectedId, cfg) {
                if (cfg && cfg.makeAllOption) {
                    var option = document.createElement('option');
                    option.value = option.innerHTML = "全部分类";
                    select.appendChild(option);
                }
                // 生成 option
                var temp = document.createDocumentFragment();
                output(toTree(jsonArray), function (node, nodeId) {
                    var option = document.createElement('option'); // 节点
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
            tree.rendererOption = rendererOption;
        })(tree = list.tree || (list.tree = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
