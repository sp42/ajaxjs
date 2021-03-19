namespace aj.list.grid {
    /**
     * 选区模型
     */
    interface GridSectionModel extends Vue {
        isSelectAll: boolean;

        /**
         * 选择的行，key 是 id，value 为 true 表示选中
         * 没选中，则删除这个 key，所以也不存在 value 为 false 的情况
         */
        selected: { [key: number]: boolean };

        selectedTotal: number;

        maxRows: number;

        /**
         * 批量删除
         */
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
        mounted(this: GridSectionModel): void {
            this.BUS && this.BUS.$on('on-delete-btn-clk', this.batchDelete);
        },

        methods: {
            /**
             * 批量删除
             */
            batchDelete(this: GridSectionModel): void {
                if (this.selectedTotal > 0) {
                    aj.showConfirm('确定批量删除记录？', () => {
                        for (var id in this.selected) {
                            // @ts-ignore
                            xhr.dele(`${this.apiUrl}/${id}/`, (j: RepsonseResult) => {
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

    /**
     * 标准表格
     */
    export class Grid extends VueComponent {
        name = "aj-grid";

        template = '<div class="aj-grid"><slot v-bind:grid="this"></slot></div>';

        mixins = [SectionModel];

        props = {
            apiUrl: { type: String, required: true }
        };

        data() {
            return {
                list: [], 
                updateApi: null,
                showAddNew: false
            };
        }

        /**
         * 数据层，控制分页
         */
        $store: any = null;

        /**
         * 工具条 UI
         */
        $toolbar: GridToolbar | null = null;

        /**
         * 行 UI
         */
        $row: Vue | null = null;

        /**
         * 
         */
        showAddNew: boolean = false;

        /**
         * 
         */
        list: GridRecord[] = [];

        apiUrl = "";
        maxRows: number = 0;

        mounted(): void {
            this.$children.forEach((child: Vue) => { // 建立子组件访问的快捷方式
                switch (child.$options._componentTag) {
                    case 'aj-entity-toolbar':
                        this.$toolbar = (<GridToolbar><unknown>child);
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
                this.list = result;
                this.maxRows = result.length;
            });

            // this.$store.autoLoad && this.$store.getDataData();
        }

        /**
         * 按下【新建】按钮时候触发的事件，你可以覆盖这个方法提供新的事件 
         */
        onCreateClk(): void {
            alert('dfd')
            this.showAddNew = true;
        }

        /**
         * 重新加载数据
         */
        reload(): void {
            this.$store.getData();
        }

        /**
         * 
         */
        onDirtySaveClk(): void {
            let dirties: GridRecord[] = getDirty.call(this);

            if (!dirties.length) {
                msg.show('没有修改过的记录');
                return;
            }

            dirties.forEach((item: GridRecord) => {
                xhr.put(`${this.apiUrl}/${item.id}/`, (j: RepsonseResult) => {
                    if (j.isOk) {
                        this.list.forEach((item: GridRecord) => { // clear
                            if (item.dirty)
                                delete item.dirty;
                        });

                        msg.show('修改记录成功');
                    }
                }, item.dirty);
            });
        }
    }

    new Grid().register();

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