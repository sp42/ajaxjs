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
        var grid;
        (function (grid) {
            /**
             * 新建记录的行
             */
            var EditRowCreate = /** @class */ (function (_super) {
                __extends(EditRowCreate, _super);
                function EditRowCreate() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = "aj-grid-inline-edit-row-create";
                    _this.template = html(__makeTemplateObject(["\n            <tr class=\"aj-grid-inline-edit-row isEditMode\">\n                <td><input type=\"checkbox\" /></td>\n                <td></td>\n                <td v-for=\"key in columns\" style=\"padding:0\" class=\"cell\" @dblclick=\"dbEdit\">\n                    <aj-select v-if=\"key != null && key.type == 'select'\" :name=\"key.name\" :options=\"key.data\" style=\"width: 200px;\"></aj-select>\n                    <input v-if=\"key != null && !key.type\" type=\"text\" size=\"0\" :name=\"key\" />\n                </td>\n                <td class=\"control\">\n                    <span @click=\"addNew\"> \u65B0\u589E</span>\n                    <span @click=\"$parent.showAddNew = false\"> \u64A4\u9500</span>\n                </td>\n            </tr>\n        "], ["\n            <tr class=\"aj-grid-inline-edit-row isEditMode\">\n                <td><input type=\"checkbox\" /></td>\n                <td></td>\n                <td v-for=\"key in columns\" style=\"padding:0\" class=\"cell\" @dblclick=\"dbEdit\">\n                    <aj-select v-if=\"key != null && key.type == 'select'\" :name=\"key.name\" :options=\"key.data\" style=\"width: 200px;\"></aj-select>\n                    <input v-if=\"key != null && !key.type\" type=\"text\" size=\"0\" :name=\"key\" />\n                </td>\n                <td class=\"control\">\n                    <span @click=\"addNew\"> \u65B0\u589E</span>\n                    <span @click=\"$parent.showAddNew = false\"> \u64A4\u9500</span>\n                </td>\n            </tr>\n        "]));
                    _this.props = {
                        columns: { type: Array, required: true },
                        createApi: { type: String, required: false, default: '.' }
                    };
                    _this.columns = [];
                    /**
                     * 创建的 API 地址
                     */
                    _this.createApi = "";
                    /**
                     * 是否处于编辑模式
                     */
                    _this.isEditMode = false;
                    return _this;
                }
                /**
                 * 新增按钮事件
                 *
                 */
                EditRowCreate.prototype.addNew = function () {
                    var _this = this;
                    var map = {}; // 创建动作的表单数据
                    this.$el.$('*[name]', function (i) { return map[i.name] = i.value; });
                    this.BUS.$emit('before-add-new', map);
                    aj.xhr.post(this.createApi, function (j) {
                        if (j && j.isOk) {
                            aj.msg.show('新建实体成功');
                            _this.$el.$('input[name]', function (i) {
                                i.value = '';
                            });
                            // @ts-ignore
                            _this.$parent.reload();
                            // @ts-ignore
                            _this.$parent.showAddNew = false;
                        }
                        else if (j && j.msg) {
                            aj.msg.show(j.msg);
                        }
                    }, map);
                };
                /**
                 * 编辑按钮事件
                 *
                 * @param ev
                 */
                EditRowCreate.prototype.dbEdit = function (ev) {
                    this.isEditMode = !this.isEditMode;
                    var el = ev.target;
                    if (el.tagName !== 'INPUT')
                        el = el.$('input');
                    setTimeout(function () { return el && el.focus(); }, 200);
                };
                return EditRowCreate;
            }(aj.VueComponent));
            new EditRowCreate().register();
        })(grid = list.grid || (list.grid = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
