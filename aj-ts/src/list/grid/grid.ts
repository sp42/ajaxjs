namespace aj.list.grid {
    export var common = {
        data() {
            return {
                list: [],
                updateApi: null,
                showAddNew: false
            };
        },
        mounted() { // 加载数据
            this.$refs.pager.$on("pager-result", result => {
                this.list = result;
                this.maxRows = result.length;
            });

            this.$refs.pager.autoLoad && this.$refs.pager.get();
        },
        methods: {
            // 按下【新建】按钮时候触发的事件，你可以覆盖这个方法提供新的事件
            onCreateClk() {
                this.showAddNew = true;
            },
            getDirty() {
                var dirties = [];
                this.list.forEach(item => {
                    if (item.dirty) {
                        item.dirty.id = item.id;
                        dirties.push(item);
                    }
                });

                return dirties;
            },
            reload() {
                this.$refs.pager.get();
            },
            onDirtySaveClk() {
                var dirties = this.getDirty();

                if (!dirties.length) {
                    aj.msg.show('没有修改过的记录');
                    return;
                }

                dirties.forEach(item => {
                    aj.xhr.put(this.updateApi + '/' + item.id + '/', j => {
                        if (j.isOk) {
                            this.list.forEach(item => { // clear
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

    /**
     * 选区模型
     */
    interface GridSectionModel extends Vue {
        isSelectAll: boolean;
        selected: Object;
        selectedTotal: number;
        maxRows: number;
        batchDelete(): void;
    }

    export var SectionModel = {
        data() {
            return {
                isSelectAll: false,// TODO 顶部是否全选的状态
                selected: {},		// 选择的行
                selectedTotal: 0,	// 选中了多少？
                maxRows: 0			// 最多的行数，用于判断是否全选
            }
        },
        mounted(this: GridSectionModel) {
            this.BUS.$on('on-delete-btn-clk', this.batchDelete);
        },

        methods: {
            /**
             * 批量删除
             */
            batchDelete(this: GridSectionModel): void {
                if (this.selectedTotal > 0) {
                    aj.showConfirm('确定批量删除记录?', () => {
                        for (var id in this.selected) {
                            aj.xhr.dele(this.deleteApi + '/' + id + '/', (j: RepsonseResult) => {
                                console.log(j)
                            });
                        }
                    });
                } else
                    aj.alert('未选择记录');
            },

            /**
             * 全选
             */
            selectAll(this: GridSectionModel): void {
                var checkAll = (item) => {
                    item.checked = true;
                    var id = item.dataset.id;

                    if (!id)
                        throw '需要提供 id 在 DOM 属性中';

                    this.$set(this.selected, Number(id), true);
                }, diskCheckAll = (item) => {
                    item.checked = false;
                    var id = item.dataset.id;

                    if (!id)
                        throw '需要提供 id 在 DOM 属性中';

                    this.$set(this.selected, Number(id), false);
                }

                this.$el.$('table .selectCheckbox input[type=checkbox]', this.selectedTotal === this.maxRows ? diskCheckAll : checkAll);
            }

        },
        watch: {
            selected: {
                handler(this: GridSectionModel, n): void {
                    let j = 0;

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
}


