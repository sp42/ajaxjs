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
var __makeTemplateObject = (this && this.__makeTemplateObject) || function (cooked, raw) {
    if (Object.defineProperty) { Object.defineProperty(cooked, "raw", { value: raw }); } else { cooked.raw = raw; }
    return cooked;
};
var aj;
(function (aj) {
    var list;
    (function (list) {
        var tree;
        (function (tree) {
            /**
             * 注意递归组件的使用
             */
            var TreeItem = /** @class */ (function (_super) {
                __extends(TreeItem, _super);
                function TreeItem() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = 'aj-tree-item';
                    _this.template = html(__makeTemplateObject(["\n            <li>\n                <div :class=\"{bold: isFolder, node: true}\" @click=\"toggle\">\n                    <span>\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7</span>{{model.name}}\n                    <span v-if=\"isFolder\">[{{open ? '-' : '+'}}]</span>\n                </div>\n                <ul v-show=\"open\" v-if=\"isFolder\" :class=\"{show: open}\">\n                    <aj-tree-item class=\"item\" v-for=\"(model, index) in model.children\" :key=\"index\" :model=\"model\">\n                    </aj-tree-item>\n                    <li v-if=\"allowAddNode\" class=\"add\" @click=\"addChild\">+</li>\n                </ul>\n            </li>\n        "], ["\n            <li>\n                <div :class=\"{bold: isFolder, node: true}\" @click=\"toggle\">\n                    <span>\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7\u00B7</span>{{model.name}}\n                    <span v-if=\"isFolder\">[{{open ? '-' : '+'}}]</span>\n                </div>\n                <ul v-show=\"open\" v-if=\"isFolder\" :class=\"{show: open}\">\n                    <aj-tree-item class=\"item\" v-for=\"(model, index) in model.children\" :key=\"index\" :model=\"model\">\n                    </aj-tree-item>\n                    <li v-if=\"allowAddNode\" class=\"add\" @click=\"addChild\">+</li>\n                </ul>\n            </li>\n        "]));
                    _this.props = {
                        model: Object,
                        allowAddNode: { type: Boolean, default: false } // 是否允许添加新节点
                    };
                    _this.model = { children: [] };
                    _this.open = false;
                    _this.allowAddNode = false;
                    // isFolder = false;
                    _this.computed = {
                        isFolder: function () {
                            return !!(this.model.children && this.model.children.length);
                        }
                    };
                    return _this;
                }
                /**
                 * 点击节点时的方法
                 *
                 * @param this
                 */
                TreeItem.prototype.toggle = function () {
                    //@ts-ignore
                    if (this.isFolder)
                        this.open = !this.open;
                    this.BUS && this.BUS.$emit('tree-node-click', this.model);
                };
                /**
                 * 变为文件夹
                 *
                 * @param this
                 */
                TreeItem.prototype.changeType = function () {
                    //@ts-ignore
                    if (!this.isFolder) {
                        Vue.set(this.model, 'children', []);
                        this.addChild();
                        this.open = true;
                    }
                };
                TreeItem.prototype.addChild = function () {
                    this.model.children.push({
                        //@ts-ignore
                        name: 'new stuff'
                    });
                };
                return TreeItem;
            }(aj.VueComponent));
            tree.TreeItem = TreeItem;
            new TreeItem().register();
        })(tree = list.tree || (list.tree = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
