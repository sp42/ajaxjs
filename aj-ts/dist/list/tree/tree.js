"use strict";
var __extends = (this && this.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var aj;
(function (aj) {
    var list;
    (function (list) {
        var tree;
        (function (tree) {
            var Tree = /** @class */ (function (_super) {
                __extends(Tree, _super);
                function Tree() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = 'aj-tree';
                    _this.template = '<ul class="aj-tree"><aj-tree-item :model="treeData"></aj-tree-item></ul>';
                    _this.props = {
                        apiUrl: String,
                        topNodeName: String
                    };
                    /**
                     * 根节点显示名称
                     */
                    _this.topNodeName = "";
                    _this.apiUrl = "";
                    _this.isAutoLoad = false;
                    _this.treeData = { name: _this.topNodeName || 'TOP', children: null };
                    return _this;
                }
                Tree.prototype.getData = function () {
                    var _this = this;
                    // @ts-ignore
                    aj.xhr.get(this.apiUrl, function (j) { return _this.treeData.children = tree.makeTree(j.result); });
                    // 递归组件怎么事件上报呢？通过事件 bus
                    this.BUS && this.BUS.$on('treenodeclick', function (data) { return _this.$emit('treenodeclick', data); });
                };
                Tree.prototype.mounted = function () {
                    this.getData();
                };
                return Tree;
            }(aj.VueComponent));
            tree.Tree = Tree;
            new Tree().register();
        })(tree = list.tree || (list.tree = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
