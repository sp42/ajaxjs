namespace aj.list.grid {
    export var SectionModel = {
        data() {
            return {
                isSelectAll: false,// TODO 顶部是否全选的状态
                selected: {},		// 选择的行
                selectedTotal: 0,	// 选中了多少？
                maxRows: 0			// 最多的行数，用于判断是否全选
            }
        },
        mounted(this: GridSectionModel): void {
            this.BUS && this.BUS.$on('on-delete-btn-clk', this.batchDelete);
        },

        methods: {
            /**
             * 批量删除
             */
            batchDelete(this: Grid): void {
                if (this.selectedTotal > 0) {
                    aj.showConfirm('确定批量删除记录？', () => {
                        for (var id in this.selected) {
                            aj.xhr.dele(`${this.apiUrl}/${id}/`, (j: RepsonseResult) => {
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
                let checkAll = (item: HTMLInputElement) => {
                    item.checked = true;
                    let id = item.dataset.id;

                    if (!id)
                        throw '需要提供 id 在 DOM 属性中';

                    this.$set(this.selected, Number(id), true);
                }, diskCheckAll = (item: HTMLInputElement) => {
                    item.checked = false;
                    let id = item.dataset.id;

                    if (!id)
                        throw '需要提供 id 在 DOM 属性中';

                    this.$set(this.selected, Number(id), false);
                }

                this.$el.$('table .selectCheckbox input[type=checkbox]', this.selectedTotal === this.maxRows ? diskCheckAll : checkAll);
            }

        },
        watch: {
            selected: {
                handler(this: GridSectionModel, _new: any): void {
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
                        (<HTMLInputElement>this.$el.$('.top-checkbox')).checked = true;
                    else
                        (<HTMLInputElement>this.$el.$('.top-checkbox')).checked = false;
                },
                deep: true
            }
        }
    };

    Vue.component('aj-grid', {
        mixins: [SectionModel],
        template: '<div class="aj-grid"><slot v-bind="this"></slot></div>',
        props: {
            apiUrl: { type: String, required: true }
        },
        data() {
            return {
                list: [],
                updateApi: null,
                showAddNew: false
            };
        },
        mounted(this: Grid): void {
            this.$children.forEach((child: Vue) => { // 建立子组件访问的快捷方式
                switch (child.$options._componentTag) {
                    case 'aj-entity-toolbar':
                        this.$toolbar = <GridToolbar>child;
                        break;
                    case 'aj-grid-inline-edit-row':
                        this.$row = child;
                        break;
                    case 'aj-list':
                        this.$store = child;
                        break;
                }
            });

            this.$store.$on("pager-result", (result: BaseObject[]) => {
                console.log(result);
                this.list = result;
                this.maxRows = result.length;
            });

            // this.$store.autoLoad && this.$store.getDataData();
        },
        methods: {
            /**
             * 按下【新建】按钮时候触发的事件，你可以覆盖这个方法提供新的事件
             * 
             * @param this 
             */
            onCreateClk(this: Grid): void {
                this.showAddNew = true;
            },


            /**
             * 重新加载数据
             * 
             * @param this 
             */
            reload(this: Grid): void {
                this.$store.getData();
            },

            /**
             * 
             * @param this 
             */
            onDirtySaveClk(this: Grid): void {
                let dirties: GridRecord[] = getDirty.call(this);

                if (!dirties.length) {
                    aj.msg.show('没有修改过的记录');
                    return;
                }

                dirties.forEach((item: GridRecord) => {
                    aj.xhr.put(`${this.apiUrl}/${item.id}/`, (j: RepsonseResult) => {
                        if (j.isOk) {
                            this.list.forEach((item: GridRecord) => { // clear
                                if (item.dirty)
                                    delete item.dirty;
                            });

                            aj.msg.show('修改记录成功');
                        }
                    }, item.dirty);
                });
            }
        }
    });

    /**
     * 获取修改过的数据
     * 
     * @param this 
     */
    function getDirty(this: Grid): GridRecord[] {
        let dirties: GridRecord[] = [];

        this.list.forEach((item: GridRecord) => {
            if (item.dirty) { // 有这个 dirty 就表示修改过的
                // item.dirty.id = item.id;
                dirties.push(item);
            }
        });

        return dirties;
    }
}