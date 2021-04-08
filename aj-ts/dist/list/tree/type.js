"use strict";
var aj;
(function (aj) {
    var list;
    (function (list) {
        var tree;
        (function (tree) {
            /**
             * 寻找配置说明的节点
             *
             * @param obj
             * @param queen
             * @returns
             */
            function findNodesHolder(obj, queen) {
                if (!queen.shift)
                    return null;
                var first = queen.shift();
                for (var i in obj) {
                    if (i === first) {
                        var target_1 = obj[i];
                        if (queen.length == 0) // 找到了
                            return target_1;
                        else
                            return findNodesHolder(obj[i], queen);
                    }
                }
                return null;
            }
            tree.findNodesHolder = findNodesHolder;
        })(tree = list.tree || (list.tree = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
