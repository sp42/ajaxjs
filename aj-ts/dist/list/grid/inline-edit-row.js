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
             * 行的 UI
                总体就是一个普通的 HTML Table，程序是通过读取 JSON 数据（实际 JS 数组）里面的字段，
                然后通过动态添加表格的方法逐行添加 tr，td，在添加行的过程中根据需要设置样式，添加方法等。
                每一行数据（即 tr 的 DOM 对象）都有 id 属性，其值就等于数据中的 idColumn 对应的列的值
             */
            var GridEditRow = /** @class */ (function (_super) {
                __extends(GridEditRow, _super);
                function GridEditRow() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = "aj-grid-inline-edit-row";
                    _this.template = html(__makeTemplateObject(["\n            <tr class=\"aj-grid-inline-edit-row\" :class=\"{editing: isEditMode}\">\n                <td v-if=\"showCheckboxCol\" class=\"selectCheckbox\">\n                    <input type=\"checkbox\" @change=\"selectCheckboxChange\" :data-id=\"id\" />\n                </td>\n                <td v-if=\"showIdCol\">{{id}}</td>\n                <td v-for=\"cellRenderer in columns\" :style=\"styleModifly\" class=\"cell\" @dblclick=\"dbEdit\">\n                    <aj-cell-renderer v-if=\"!isEditMode\" :html=\"renderCell(rowData, cellRenderer)\" :form=\"rowData\"></aj-cell-renderer>\n                    <aj-cell-renderer v-if=\"isEditMode && cellRenderer && cellRenderer.editMode\"\n                        :html=\"rendererEditMode(rowData, cellRenderer)\" :form=\"rowData\">\n                    </aj-cell-renderer>\n                    <input type=\"text\" v-if=\"canEdit(cellRenderer)\" size=\"0\" v-model=\"rowData[cellRenderer]\" />\n                </td>\n                <td v-if=\"showControl\" class=\"control\">\n                    <aj-cell-renderer v-if=\"controlUi\" :html=\"controlUi\" :form=\"rowData\"></aj-cell-renderer>\n                    <span @click=\"onEditClk\" class=\"edit\"><i class=\"fa fa-pencil\" aria-hidden=\"true\"></i>\n                        {{!isEditMode ? \"\u7F16\u8F91\" : \"\u786E\u5B9A\"}}</span>\n                    <span @click=\"dele(id)\" class=\"delete\"><i class=\"fa fa-times\" aria-hidden=\"true\"></i> \u5220\u9664</span>\n                </td>\n            </tr>\n        "], ["\n            <tr class=\"aj-grid-inline-edit-row\" :class=\"{editing: isEditMode}\">\n                <td v-if=\"showCheckboxCol\" class=\"selectCheckbox\">\n                    <input type=\"checkbox\" @change=\"selectCheckboxChange\" :data-id=\"id\" />\n                </td>\n                <td v-if=\"showIdCol\">{{id}}</td>\n                <td v-for=\"cellRenderer in columns\" :style=\"styleModifly\" class=\"cell\" @dblclick=\"dbEdit\">\n                    <aj-cell-renderer v-if=\"!isEditMode\" :html=\"renderCell(rowData, cellRenderer)\" :form=\"rowData\"></aj-cell-renderer>\n                    <aj-cell-renderer v-if=\"isEditMode && cellRenderer && cellRenderer.editMode\"\n                        :html=\"rendererEditMode(rowData, cellRenderer)\" :form=\"rowData\">\n                    </aj-cell-renderer>\n                    <input type=\"text\" v-if=\"canEdit(cellRenderer)\" size=\"0\" v-model=\"rowData[cellRenderer]\" />\n                </td>\n                <td v-if=\"showControl\" class=\"control\">\n                    <aj-cell-renderer v-if=\"controlUi\" :html=\"controlUi\" :form=\"rowData\"></aj-cell-renderer>\n                    <span @click=\"onEditClk\" class=\"edit\"><i class=\"fa fa-pencil\" aria-hidden=\"true\"></i>\n                        {{!isEditMode ? \"\u7F16\u8F91\" : \"\u786E\u5B9A\"}}</span>\n                    <span @click=\"dele(id)\" class=\"delete\"><i class=\"fa fa-times\" aria-hidden=\"true\"></i> \u5220\u9664</span>\n                </td>\n            </tr>\n        "]));
                    _this.props = {
                        initRowData: { type: Object, required: true },
                        showIdCol: { type: Boolean, default: true },
                        columns: Array,
                        showCheckboxCol: { type: Boolean, default: true },
                        showControl: { type: Boolean, default: true },
                        filterField: Array,
                        enableInlineEdit: { type: Boolean, default: false },
                        deleApi: String,
                        controlUi: String // 自定义“操作”按钮，这里填组件的名字
                    };
                    /**
                     * 固定不可编辑的字段
                     */
                    _this.filterField = [];
                    /**
                     * 每行记录它的 id
                     */
                    _this.id = "";
                    /**
                     * 输入的数据
                     */
                    _this.initRowData = {};
                    /**
                     * 每行记录的数据
                     */
                    _this.rowData = {};
                    /**
                     * 是否处于编辑模式
                     */
                    _this.isEditMode = false;
                    /**
                     * 表格是否可以被编辑
                     */
                    _this.enableInlineEdit = false;
                    /**
                     * 单元格渲染器的类型，这是一个有序的数组
                     */
                    _this.columns = [];
                    //@ts-ignore
                    _this.$parent = null;
                    _this.computed = {
                        filterData: function () {
                            var data = JSON.parse(JSON.stringify(this.rowData)); // 剔除不要的字段
                            delete data.id;
                            delete data.dirty;
                            if (this.filterField && this.filterField.length)
                                this.filterField.forEach(function (i) { return delete data[i]; });
                            return data;
                        },
                        /**
                         *
                         *
                         * @param this
                         */
                        styleModifly: function () {
                            return {
                                padding: this.isEditMode ? 0 : '',
                            };
                        }
                    };
                    return _this;
                }
                GridEditRow.prototype.data = function () {
                    return {
                        id: this.initRowData.id,
                        rowData: this.initRowData,
                        isEditMode: false
                    };
                };
                GridEditRow.prototype.mounted = function () {
                    for (var i in this.rowData) // 监视每个字段
                        this.$watch('rowData.' + i, makeWatch.call(this, i));
                };
                /**
                 * 没有指定编辑器的情况下，使用 input 作为编辑器
                 *
                 * @param cellRenderer
                 */
                GridEditRow.prototype.canEdit = function (cellRenderer) {
                    return this.isEditMode && !isFixedField.call(this, cellRenderer) && !(cellRenderer.editMode);
                };
                /**
                 * 渲染单元格
                 *
                 * @param data
                 * @param cellRenderer
                 */
                GridEditRow.prototype.renderCell = function (data, cellRenderer) {
                    var v;
                    if (cellRenderer === '')
                        return '';
                    if (typeof cellRenderer == 'string') {
                        v = data[cellRenderer];
                        return v + "";
                    }
                    if (typeof cellRenderer == 'function') {
                        v = cellRenderer(data);
                        return v;
                    }
                    if (typeof cellRenderer == 'object') {
                        var cfg = cellRenderer;
                        if (!!cfg.renderer)
                            v = cfg.renderer(data);
                        // if (typeof key.showMode === 'function')
                        //     return key.showMode(data);
                        // if (typeof key.showMode === 'string')
                        //     return data[key.showMode];
                    }
                    return (v === null ? '' : v) + '';
                };
                /**
                 * 编辑按钮事件
                 *
                 */
                GridEditRow.prototype.onEditClk = function () {
                    if (this.enableInlineEdit)
                        this.isEditMode = !this.isEditMode;
                    //@ts-ignore    
                    else if (this.$parent.onEditClk) // 打开另外的编辑界面
                        //@ts-ignore    
                        this.$parent.onEditClk(this.id);
                };
                /**
                 * 渲染编辑模式下的行
                 *
                 * @param data
                 * @param cellRenderer
                 */
                GridEditRow.prototype.rendererEditMode = function (data, cellRenderer) {
                    if (typeof cellRenderer === 'string')
                        return cellRenderer.toString();
                    if (cellRenderer.editMode && typeof cellRenderer.editRenderer === 'function')
                        return cellRenderer.editRenderer(data);
                    return "NULL";
                };
                /**
                 * 双击单元格进入编辑
                 *
                 * @param ev
                 */
                GridEditRow.prototype.dbEdit = function (ev) {
                    if (!this.enableInlineEdit)
                        return;
                    this.isEditMode = !this.isEditMode;
                    if (this.isEditMode) {
                        var el_1 = ev.target;
                        setTimeout(function () {
                            if (el_1.tagName !== 'INPUT')
                                el_1 = el_1.$('input');
                            el_1 && el_1.focus();
                        }, 200);
                    }
                };
                /**
                 *
                 * @param ev
                 */
                GridEditRow.prototype.selectCheckboxChange = function (ev) {
                    var checkbox = ev.target, parent = this.$parent;
                    if (checkbox.checked)
                        parent.$set(parent.selected, this.id, true);
                    //this.$parent.selected[this.id] = true;
                    else
                        parent.$set(parent.selected, this.id, false);
                };
                /**
                 * 删除记录
                 *
                 * @param id
                 */
                GridEditRow.prototype.dele = function (id) {
                    var _this = this;
                    aj.showConfirm("\u786E\u5B9A\u5220\u9664\u8BB0\u5F55 id:[" + id + "] \u5417\uFF1F", function () {
                        return aj.xhr.dele(_this.$parent.apiUrl + "/" + id + "/", function (j) {
                            if (j.isOk) {
                                aj.msg.show('删除成功');
                                _this.$parent.reload();
                            }
                        });
                    });
                };
                return GridEditRow;
            }(aj.VueComponent));
            new GridEditRow().register();
            /**
             * 生成该字段的 watch 函数
             *
             * @param this
             * @param field
             */
            function makeWatch(field) {
                return function (_new) {
                    var arr = this.$parent.list, data;
                    for (var i = 0, j = arr.length; i < j; i++) { // 已知 id 找到原始数据
                        if (this.id && (String(arr[i].id) == this.id)) {
                            data = arr[i];
                            break;
                        }
                    }
                    if (!data)
                        throw '找不到匹配的实体！目标 id: ' + this.id;
                    if (!data.dirty)
                        data.dirty = {
                            id: this.id
                        };
                    data.dirty[field] = _new; // 保存新的值，key 是字段名
                };
            }
            /**
             * 是否固定的字段，固定的字段不能被编辑
             *
             * @param this
             * @param cellRenderer
             */
            function isFixedField(cellRenderer) {
                if (this.filterField && this.filterField.length) {
                    for (var i = 0, j = this.filterField.length; i < j; i++) {
                        if (this.filterField[i] == cellRenderer)
                            return true;
                    }
                }
                return false;
            }
        })(grid = list.grid || (list.grid = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
