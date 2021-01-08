"use strict";
var aj;
(function (aj) {
    var list;
    (function (list) {
        var grid;
        (function (grid) {
            grid.common = {
                data: function () {
                    return {
                        list: [],
                        updateApi: null,
                        showAddNew: false
                    };
                },
                mounted: function () {
                    var _this = this;
                    this.$refs.pager.$on("pager-result", function (result) {
                        _this.list = result;
                        _this.maxRows = result.length;
                    });
                    this.$refs.pager.autoLoad && this.$refs.pager.get();
                },
                methods: {
                    // 按下【新建】按钮时候触发的事件，你可以覆盖这个方法提供新的事件
                    onCreateClk: function () {
                        this.showAddNew = true;
                    },
                    getDirty: function () {
                        var dirties = [];
                        this.list.forEach(function (item) {
                            if (item.dirty) {
                                item.dirty.id = item.id;
                                dirties.push(item);
                            }
                        });
                        return dirties;
                    },
                    reload: function () {
                        this.$refs.pager.get();
                    },
                    onDirtySaveClk: function () {
                        var _this = this;
                        var dirties = this.getDirty();
                        if (!dirties.length) {
                            aj.msg.show('没有修改过的记录');
                            return;
                        }
                        dirties.forEach(function (item) {
                            aj.xhr.put(_this.updateApi + '/' + item.id + '/', function (j) {
                                if (j.isOk) {
                                    _this.list.forEach(function (item) {
                                        if (item.dirty)
                                            delete item.dirty;
                                    });
                                    aj.msg.show('修改记录成功');
                                }
                            }, item.dirty);
                        });
                    }
                }
            };
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
                    this.BUS.$on('on-delete-btn-clk', this.batchDelete);
                },
                methods: {
                    /**
                     * 批量删除
                     */
                    batchDelete: function () {
                        var _this = this;
                        if (this.selectedTotal > 0) {
                            aj.showConfirm('确定批量删除记录?', function () {
                                for (var id in _this.selected) {
                                    aj.xhr.dele(_this.deleteApi + '/' + id + '/', function (j) {
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
                        handler: function (n) {
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
                                (this.$el.$('.top-checkbox')).checked = true;
                            else
                                this.$el.$('.top-checkbox').checked = false;
                        },
                        deep: true
                    }
                }
            };
        })(grid = list.grid || (list.grid = {}));
    })(list = aj.list || (aj.list = {}));
})(aj || (aj = {}));
