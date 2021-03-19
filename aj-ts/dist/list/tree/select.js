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
            /**
             * 下拉分类选择器，异步请求远端获取分类数据
             */
            var TreeLikeSelect = /** @class */ (function (_super) {
                __extends(TreeLikeSelect, _super);
                function TreeLikeSelect() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = 'aj-tree-like-select';
                    _this.template = '<select :name="fieldName" class="aj-select" @change="onSelected" style="min-width:180px;"></select>';
                    _this.props = {
                        fieldName: { type: String, required: false, default: 'catalogId' },
                        apiUrl: { type: String, default: function () { return aj.ctx + '/admin/tree-like/'; } },
                        isAutoLoad: { type: Boolean, default: true },
                        isAutoJump: Boolean,
                        initFieldValue: String
                    };
                    _this.apiUrl = "";
                    /**
                     * 是否自动跳转 catalogId
                     */
                    _this.isAutoJump = false;
                    _this.isAutoLoad = false;
                    _this.fieldName = "";
                    _this.fieldValue = "";
                    _this.initFieldValue = "";
                    return _this;
                }
                TreeLikeSelect.prototype.data = function () {
                    return {
                        fieldValue: this.initFieldValue
                    };
                };
                TreeLikeSelect.prototype.mounted = function () {
                    this.isAutoLoad && this.getData();
                };
                TreeLikeSelect.prototype.onSelected = function (ev) {
                    var el = ev.target;
                    this.fieldValue = el.selectedOptions[0].value;
                    if (this.isAutoJump)
                        location.assign('?' + this.fieldName + '=' + this.fieldValue);
                    else
                        this.BUS && this.BUS.$emit('aj-tree-catelog-select-change', ev, this);
                };
                TreeLikeSelect.prototype.getData = function () {
                    var _this = this;
                    var fn = function (j) {
                        var arr = [{ id: 0, name: "请选择分类" }];
                        tree.rendererOption(arr.concat(j.result), _this.$el, _this.fieldValue, { makeAllOption: false });
                        if (_this.fieldValue) // 有指定的选中值
                            //@ts-ignore
                            aj.form.utils.selectOption.call(_this, _this.fieldValue);
                    };
                    // aj.xhr.get(this.ajResources.ctx + this.apiUrl + "/admin/tree-like/getListAndSubByParentId/", fn);
                    aj.xhr.get(this.apiUrl, fn);
                };
                return TreeLikeSelect;
            }(aj.VueComponent));
            tree.TreeLikeSelect = TreeLikeSelect;
            new TreeLikeSelect().register();
        })(tree = list.tree || (list.tree = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
