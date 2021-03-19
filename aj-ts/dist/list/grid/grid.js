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
        var grid;
        (function (grid) {
            grid.SectionModel = {
                data: function () {
                    return {
                        isSelectAll: false,
                        selected: {},
                        selectedTotal: 0,
                        maxRows: 0 // 最多的行数，用于判断是否全选
                    };
                },
                mounted: function () {
                    this.BUS && this.BUS.$on('on-delete-btn-clk', this.batchDelete);
                },
                methods: {
                    /**
                     * 批量删除
                     */
                    batchDelete: function () {
                        var _this = this;
                        if (this.selectedTotal > 0) {
                            aj.showConfirm('确定批量删除记录？', function () {
                                for (var id in _this.selected) {
                                    // @ts-ignore
                                    aj.xhr.dele(_this.apiUrl + "/" + id + "/", function (j) {
                                        console.log(j);
                                    });
                                }
                            });
                        }
                        else
                            aj.alert('未选择记录');
                    },
                    /**
                     * 全选
                     */
                    selectAll: function () {
                        var _this = this;
                        var checkAll = function (item) {
                            item.checked = true;
                            var id = item.dataset.id;
                            if (!id)
                                throw '需要提供 id 在 DOM 属性中';
                            _this.$set(_this.selected, Number(id), true);
                        }, diskCheckAll = function (item) {
                            item.checked = false;
                            var id = item.dataset.id;
                            if (!id)
                                throw '需要提供 id 在 DOM 属性中';
                            _this.$set(_this.selected, Number(id), false);
                        };
                        this.$el.$('table .selectCheckbox input[type=checkbox]', this.selectedTotal === this.maxRows ? diskCheckAll : checkAll);
                    }
                },
                watch: {
                    selected: {
                        handler: function (_new) {
                            var j = 0;
                            // clear falses
                            for (var i in this.selected) {
                                if (this.selected[i] === false)
                                    delete this.selected[i];
                                else
                                    j++;
                            }
                            this.selectedTotal = j;
                            if (j === this.maxRows)
                                this.$el.$('.top-checkbox').checked = true;
                            else
                                this.$el.$('.top-checkbox').checked = false;
                        },
                        deep: true
                    }
                }
            };
            /**
             * 标准表格
             */
            var Grid = /** @class */ (function (_super) {
                __extends(Grid, _super);
                function Grid() {
                    var _this = _super !== null && _super.apply(this, arguments) || this;
                    _this.name = "aj-grid";
                    _this.template = '<div class="aj-grid"><slot v-bind:grid="this"></slot></div>';
                    _this.mixins = [grid.SectionModel];
                    _this.props = {
                        apiUrl: { type: String, required: true }
                    };
                    /**
                     * 数据层，控制分页
                     */
                    _this.$store = null;
                    /**
                     * 工具条 UI
                     */
                    _this.$toolbar = null;
                    /**
                     * 行 UI
                     */
                    _this.$row = null;
                    /**
                     *
                     */
                    _this.showAddNew = false;
                    /**
                     *
                     */
                    _this.list = [];
                    _this.apiUrl = "";
                    _this.maxRows = 0;
                    return _this;
                }
                Grid.prototype.data = function () {
                    return {
                        list: [],
                        updateApi: null,
                        showAddNew: false
                    };
                };
                Grid.prototype.mounted = function () {
                    var _this = this;
                    this.$children.forEach(function (child) {
                        switch (child.$options._componentTag) {
                            case 'aj-entity-toolbar':
                                _this.$toolbar = child;
                                break;
                            case 'aj-grid-inline-edit-row':
                                _this.$row = child;
                                break;
                            case 'aj-list':
                                _this.$store = child;
                                break;
                        }
                    });
                    this.$store.$on("pager-result", function (result) {
                        _this.list = result;
                        _this.maxRows = result.length;
                    });
                    // this.$store.autoLoad && this.$store.getDataData();
                };
                /**
                 * 按下【新建】按钮时候触发的事件，你可以覆盖这个方法提供新的事件
                 */
                Grid.prototype.onCreateClk = function () {
                    aj.alert('dfd');
                    this.showAddNew = true;
                };
                /**
                 * 重新加载数据
                 */
                Grid.prototype.reload = function () {
                    this.$store.getData();
                };
                /**
                 *
                 */
                Grid.prototype.onDirtySaveClk = function () {
                    var _this = this;
                    var dirties = getDirty.call(this);
                    if (!dirties.length) {
                        aj.msg.show('没有修改过的记录');
                        return;
                    }
                    dirties.forEach(function (item) {
                        aj.xhr.put(_this.apiUrl + "/" + item.id + "/", function (j) {
                            if (j.isOk) {
                                _this.list.forEach(function (item) {
                                    if (item.dirty)
                                        delete item.dirty;
                                });
                                aj.msg.show('修改记录成功');
                            }
                        }, item.dirty);
                    });
                };
                return Grid;
            }(aj.VueComponent));
            grid.Grid = Grid;
            new Grid().register();
            /**
             * 获取修改过的数据
             *
             * @param this
             */
            function getDirty() {
                var dirties = [];
                this.list.forEach(function (item) {
                    if (item.dirty) { // 有这个 dirty 就表示修改过的
                        // item.dirty.id = item.id;
                        dirties.push(item);
                    }
                });
                return dirties;
            }
        })(grid = list.grid || (list.grid = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
